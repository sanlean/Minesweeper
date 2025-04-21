import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.kotlinCocoapods)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = ConfigsConstants.group
version = ConfigsConstants.version

kotlin {
    targets.all {
        compilations.all {
            this@kotlin.compilerOptions {
                freeCompilerArgs.add("-opt-in=kotlin.experimental.ExperimentalNativeApi")
                freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
    jvmToolchain(){
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    androidTarget {
        publishLibraryVariants("release")
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = ConfigsConstants.project
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        summary = ConfigsConstants.projectDescription
        homepage = ConfigsConstants.scmUrl
        version = ConfigsConstants.version
        ios.deploymentTarget = "16.0"
        framework {
            baseName = ConfigsConstants.project
            isStatic = true
        }
    }

    jvm {
        testRuns.named("test") {
            executionTask.configure {
                useJUnitPlatform()
            }
        }
        dependencies {
            implementation(libs.ktor.client.apache5)
            implementation(libs.sqldelight.driver.jvm)
        }
    }

    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            testTask{
                useKarma {
                    useChromium()
                }
            }
        }
        binaries.executable()
    }

    listOf(
        macosArm64(),
        macosX64()
    ).forEach {
        it.binaries {
            sharedLib {
                baseName = ConfigsConstants.project
            }
        }
    }
    macosArm64()
    macosX64()

    linuxX64()
    linuxArm64()

    val arch = System.getProperty("os.arch")
    listOf(
        linuxX64(),
        linuxArm64()
    ).forEach {
        it.binaries {
            all {
                linkerOpts += mutableListOf("-L$projectDir/libs/${it.targetName}")
            }
            sharedLib {
                baseName = ConfigsConstants.project
            }
        }
    }

    mingwX64("windows"){
            binaries.all {
                linkerOpts += mutableListOf("-L$projectDir\\libs\\${targetName}")
                linkerOpts += mutableListOf("-L$projectDir/libs/${targetName}")
            }
            binaries {
                sharedLib {
                    baseName = ConfigsConstants.project
                }
            }
        }

    
    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.serialization.kotlinx)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.io.core)
            implementation(libs.kotlinx.collections.immutable)
            implementation(libs.sqldelight.runtime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        val commonMain by getting
        val jsMain by getting
        val iosSimulatorArm64Main by getting
        val iosArm64Main by getting
        val iosX64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosSimulatorArm64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosX64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.driver.native)
            }
        }
        val macosArm64Main by getting
        val macosX64Main by getting
        val macMain by creating {
            dependsOn(commonMain)
            macosX64Main.dependsOn(this)
            macosArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.darwin)
                implementation(libs.sqldelight.driver.native)
                implementation("com.squareup.okio:okio:3.7.0")
            }
        }
        val linuxX64Main by getting
        val linuxArm64Main by getting
        val linuxMain by creating {
            dependsOn(commonMain)
            linuxX64Main.dependsOn(this)
            linuxArm64Main.dependsOn(this)
            dependencies {
                implementation(libs.ktor.client.cio)
                implementation(libs.sqldelight.driver.native)
                implementation("com.squareup.okio:okio:3.7.0")
            }
        }
        val windowsMain by getting
        windowsMain.dependencies {
            implementation("com.squareup.okio:okio:3.7.0")
            implementation(libs.ktor.client.winhttp)
            implementation(libs.sqldelight.driver.native)
        }
        jsMain.dependencies {
            implementation(libs.sqldelight.driver.js.browser)
            implementation(devNpm("copy-webpack-plugin", "9.1.0"))
        }
    }
}

android {
    namespace = ConfigsConstants.namespace
    compileSdk = ConfigsConstants.compileSdk
    defaultConfig {
        minSdk = ConfigsConstants.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        implementation(libs.ktor.client.okhttp)
        implementation(libs.kotlinx.coroutines.android)
        implementation(libs.sqldelight.driver.android)
    }
}

sqldelight {
    databases {
        create("Database") {
            packageName.set(ConfigsConstants.namespace)
            generateAsync.set(true)
        }
    }
}

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()
    coordinates(ConfigsConstants.group, ConfigsConstants.project, ConfigsConstants.version)
    pom {
        name = ConfigsConstants.projectName
        description = ConfigsConstants.projectDescription
        inceptionYear = "2025"
        url = ConfigsConstants.scmUrl
        licenses {
            license {
                name = "The MIT License"
                url = "https://opensource.org/license/mit"
                distribution = "repo"
            }
        }
        developers {
            developer {
                id = "sanlean"
                name = "Leandro Santana"
                url = "https://github.com/sanlean"
            }
        }
        scm {
            url = ConfigsConstants.scmUrl
            connection = "scm:git:git://github.com/sanlean/minesweeper.git"
            developerConnection = "scm:git:ssh://github.com:sanlean/minesweeper.git"
        }
    }
}
