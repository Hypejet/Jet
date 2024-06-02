plugins {
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(libs.netty)
    implementation(libs.adventure.serializer.gson)
    implementation(libs.logback)
    implementation(project(":api"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}