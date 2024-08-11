plugins {
    `maven-publish`
    alias(libs.plugins.checker.framework)
}

dependencies {
    api(libs.slf4j)
    api(libs.gson)
    api(libs.guice)
    api(libs.bundles.adventure)
    api(libs.bundles.jetData)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}