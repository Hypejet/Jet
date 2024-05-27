plugins {
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(libs.netty)
    implementation(libs.adventure.serializer.gson)
    implementation(project(":api"))
}