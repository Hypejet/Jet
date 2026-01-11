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

private val generatedJavaPath = layout.projectDirectory.dir("src").dir("generated").dir("java")

sourceSets.main {
    java.srcDirs(generatedJavaPath)
}

tasks {
    val generateSourcesTask = register<GenerateSourcesTask>(
        "generateSources",
        generatedJavaPath
    )
    withType<AbstractCompile> {
        dependsOn(generateSourcesTask)
    }
    sourcesJar {
        dependsOn(generateSourcesTask)
    }
    jar {
        manifest.attributes("Automatic-Module-Name" to "net.hypejet.jet.api")
    }
}

@CacheableTask
abstract class GenerateSourcesTask @Inject constructor(
    private val generatedJavaPath: Directory,
    private val execOperations: ExecOperations
) : DefaultTask() {

    private val generatorMainSourceSet: SourceSet

    init {
        val dataGeneratorProject = project.project(":data:generator")
        generatorMainSourceSet = dataGeneratorProject.sourceSets.main.get()

        inputs.files(generatorMainSourceSet.allSource.srcDirs)
        inputs.files(dataGeneratorProject.configurations.runtimeClasspath.get().resolve())
        outputs.dir(generatedJavaPath)

        dependsOn(dataGeneratorProject.tasks.build)
    }

    @TaskAction
    fun run() {
        execOperations.javaexec {
            classpath = generatorMainSourceSet.runtimeClasspath
            mainClass = "net.hypejet.jet.data.generator.GeneratorMain"
            args("--api=" + generatedJavaPath.asFile.absolutePath)
        }
    }
}