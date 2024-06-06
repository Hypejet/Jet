plugins {
    `maven-publish`
    alias(libs.plugins.checker.framework)
}

dependencies {
    api(libs.slf4j)
    api(libs.bundles.adventure)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}