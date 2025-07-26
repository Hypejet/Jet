import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.checker.framework)
}

dependencies {
    implementation(project(":api"))
    implementation(project(":data:json"))
    implementation(libs.netty)
    implementation(libs.logback)
    implementation(libs.fastutil)
    implementation(libs.bundles.configs)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("net.hypejet.jet.server.JetServerEntrypoint")
}

val dataGeneratorProject = project(":data:generator")
val dataJsonProject = project(":data:json")

val generatedSourcesRoot = layout.projectDirectory.dir("src").dir("generated")
val generatedJavaPath = generatedSourcesRoot.dir("java")
val generatedResourcesPath = generatedSourcesRoot.dir("resources")

sourceSets.main {
    java.srcDirs(generatedJavaPath)
    resources.srcDirs(generatedResourcesPath)
}

tasks {
    val generatedSourcesTask = register("generatedSources") {
        projectInput(dataGeneratorProject, inputs)
        projectInput(dataJsonProject, inputs)
        outputs.dir(generatedJavaPath)
        outputs.dir(generatedResourcesPath)
        doLast { dataGeneratorProject.tasks.named<JavaExec>("run").get().exec() }
    }
    sourcesJar {
        dependsOn(generatedSourcesTask)
    }
    withType<AbstractCompile> {
        dependsOn(generatedSourcesTask)
    }
    withType<ShadowJar> {
        minimize {
            exclude(project(":api")) // Dependencies of the API may be used by plugins
            exclude(dependency(libs.logback.get())) // Minimizing logback causes problems with finding an SLF4J provider
        }
    }
}

private fun projectInput(project: Project, inputs: TaskInputs) {
    inputs.files(project.sourceSets.main.get().allSource.srcDirs)
    inputs.files(project.configurations.compileClasspath.get().resolvedConfiguration.files)
}