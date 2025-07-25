plugins {
    `maven-publish`
    alias(libs.plugins.checker.framework)
}

sourceSets.main {
    java.srcDirs(layout.projectDirectory.dir("src").dir("generated").dir("java"))
}

dependencies {
    api(libs.slf4j)
    api(libs.gson)
    api(libs.guice)
    api(libs.brigadier)
    api(libs.concurrencyUtilities)
    api(libs.bundles.adventure)
    api(libs.bundles.jetData)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        pom.licenses {
            license {
                name = "MIT"
                url = "https://choosealicense.com/licenses/mit/"
            }
        }
    }
}

val generatorProject = project(":data:generator")

tasks {
    generatorProject.afterEvaluate {
        classes {
            dependsOn(generatorProject.tasks.named("run"))
        }
    }
    jar {
        manifest.attributes("Automatic-Module-Name" to "net.hypejet.jet.api")
    }
}