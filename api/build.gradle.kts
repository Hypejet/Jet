plugins {
    `maven-publish`
    alias(libs.plugins.checker.framework)
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

val dataGeneratorProject = project(":data:generator")
val generatedJavaPath = layout.projectDirectory.dir("src").dir("generated").dir("java")

sourceSets.main {
    java.srcDirs(generatedJavaPath)
}

tasks {
    val generatedSourcesTask = register("generatedSources") {
        inputs.files(dataGeneratorProject.sourceSets.main.get().allSource.srcDirs)
        inputs.files(dataGeneratorProject.configurations.compileClasspath.get().resolvedConfiguration.files)
        inputs.files(dataGeneratorProject.configurations.runtimeClasspath.get().resolvedConfiguration.files)
        outputs.dir(generatedJavaPath)

        doLast {
            dataGeneratorProject.javaexec {
                classpath = dataGeneratorProject.sourceSets.main.get().runtimeClasspath
                mainClass = "net.hypejet.jet.data.generator.GeneratorMain"
                args("--api=" + generatedJavaPath.asFile.absolutePath)
            }
        }
    }
    withType<AbstractCompile> {
        dependsOn(generatedSourcesTask)
    }
    sourcesJar {
        dependsOn(generatedSourcesTask)
    }
    jar {
        manifest.attributes("Automatic-Module-Name" to "net.hypejet.jet.api")
    }
}