plugins {
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(project(":api"))
    implementation(libs.netty)
    implementation(libs.logback)
    implementation(libs.bundles.configs)
    implementation(libs.bundles.jetDataServer)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("net.hypejet.jet.server.JetServerEntrypoint")
}