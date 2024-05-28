subprojects {
    apply<JavaPlugin>()
    apply<JavaLibraryPlugin>()

    group = "net.hypejet.jet"
    version = "1.0"

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://libraries.minecraft.net")
    }

    tasks {
        withType<Javadoc> {
            val docletOptions = options as StandardJavadocDocletOptions
            docletOptions.addBooleanOption("html5", true)
        }
        withType<Test> {
            useJUnitPlatform()
        }
    }

    extensions.getByType<JavaPluginExtension>().apply {
        withJavadocJar()
        withSourcesJar()
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }
}