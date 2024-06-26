plugins {
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(libs.netty)
    implementation(libs.logback)
    implementation(libs.bundles.configs)
    implementation(project(":api"))
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("net.hypejet.jet.server.JetServerEntrypoint")
}