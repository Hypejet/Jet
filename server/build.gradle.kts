import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(project(":api"))
    implementation(libs.netty)
    implementation(libs.logback)
    implementation(libs.fastutil)
    implementation(libs.bundles.configs)
    implementation(libs.bundles.jetDataServer)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("net.hypejet.jet.server.JetServerEntrypoint")
}

tasks.withType<ShadowJar> {
    minimize {
        exclude(project(":api")) // All dependencies of the API may be used by plugins
        exclude(dependency(libs.logback.get())) // Minimizing logback causes problems with finding an SLF4J provider
    }
}