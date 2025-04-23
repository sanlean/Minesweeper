plugins {
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.foojayResolverConvention).apply(false)
    alias(libs.plugins.vanniktech.mavenPublish).apply(false)
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.8" // Certifique-se de usar a versão mais recente
}

tasks.withType<Test> {
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("shared:jvmTest") // Defina a tarefa de teste que você deseja usar

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }

    classDirectories.setFrom(
        files(
            classDirectories.files.map {
                fileTree(it) {
                    exclude("**/generated/**")
                }
            }
        )
    )

    sourceDirectories.setFrom(files("src/commonMain/kotlin", "src/jvmMain/kotlin"))
    executionData.setFrom(fileTree(buildDir).include("**/jacoco/*.exec"))
}