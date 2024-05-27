plugins {
    `maven-publish`
    alias(libs.plugins.checker.framework)
}

dependencies {
    api(libs.adventure)
    api(libs.slf4j)
}