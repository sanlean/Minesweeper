plugins {
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlinCocoapods).apply(false)
    alias(libs.plugins.sqldelight).apply(false)
    alias(libs.plugins.foojayResolverConvention).apply(false)
    alias(libs.plugins.vanniktech.mavenPublish) apply false
}
