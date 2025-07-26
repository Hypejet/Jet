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
val dataJsonProject = project(":data:json")
val generatedJavaPath = layout.projectDirectory.dir("src").dir("generated").dir("java")

sourceSets.main {
    java.srcDirs(generatedJavaPath)
}

tasks {
    val generatedSourcesTask = register("generatedSources") {
        projectInput(dataGeneratorProject, inputs)
        projectInput(dataJsonProject, inputs)
        outputs.dir(generatedJavaPath)
        doLast { dataGeneratorProject.tasks.named<JavaExec>("run").get().exec() }
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

private fun projectInput(project: Project, inputs: TaskInputs) {
    inputs.files(project.sourceSets.main.get().allSource.srcDirs)
    inputs.files(project.configurations.compileClasspath.get().resolvedConfiguration.files)
}