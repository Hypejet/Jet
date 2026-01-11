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
    implementation(libs.guava)
    implementation(libs.fastutil)
    implementation(libs.bundles.configs)
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly(libs.junit.platform)
}

application {
    mainClass.set("net.hypejet.jet.server.JetMinecraftServer")
}

private val generatedSourcesRoot = layout.projectDirectory.dir("src").dir("generated")
private val generatedJavaPath = generatedSourcesRoot.dir("java")
private val generatedResourcesPath = generatedSourcesRoot.dir("resources")

sourceSets.main {
    java.srcDirs(generatedJavaPath)
    resources.srcDirs(generatedResourcesPath)
}

tasks {
    val generateSourcesTask = register<GenerateSourcesTask>(
        "generateSources",
        generatedJavaPath,
        generatedResourcesPath
    )
    sourcesJar {
        dependsOn(generateSourcesTask)
    }
    processResources {
        dependsOn(generateSourcesTask)
    }
    withType<AbstractCompile> {
        dependsOn(generateSourcesTask)
    }
    withType<ShadowJar> {
        minimize {
            exclude(project(":api")) // Dependencies of the API may be used by plugins
            exclude(dependency(libs.logback.get())) // Minimizing logback causes problems with finding an SLF4J provider
        }
    }
}

@CacheableTask
abstract class GenerateSourcesTask @Inject constructor(
    private val generatedJavaPath: Directory,
    private val generatedResourcesPath: Directory,
    private val execOperations: ExecOperations
) : DefaultTask() {

    private val generatorMainSourceSet: SourceSet

    init {
        val dataGeneratorProject = project.project(":data:generator")
        generatorMainSourceSet = dataGeneratorProject.sourceSets.main.get()

        inputs.files(generatorMainSourceSet.allSource.srcDirs)
        inputs.files(dataGeneratorProject.configurations.runtimeClasspath.get().resolve())

        outputs.dir(generatedJavaPath)
        outputs.dir(generatedResourcesPath)

        dependsOn(dataGeneratorProject.tasks.build)
    }

    @TaskAction
    fun run() {
        execOperations.javaexec {
            classpath = generatorMainSourceSet.runtimeClasspath
            mainClass = "net.hypejet.jet.data.generator.GeneratorMain"
            args(
                "--server=" + generatedJavaPath.asFile.absolutePath,
                "--resources=" + generatedResourcesPath.asFile.absolutePath
            )
        }
    }
}