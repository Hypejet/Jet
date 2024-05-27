plugins {
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(libs.netty)
    implementation(project(":api"))
}