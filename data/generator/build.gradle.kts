import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    application
    alias(libs.plugins.vanillaGradle)
}

dependencies {
    implementation(project(":data:json"))
    implementation(libs.javapoet)
    implementation(libs.picocli)
    implementation(libs.jspecify)
    implementation(libs.adventure)
    implementation(libs.gson)
    implementation(libs.guava)
    implementation(libs.adventure.serializer.nbt)
    implementation(libs.adventure.serializer.gson)
}

application {
    mainClass.set("net.hypejet.jet.data.generator.GeneratorMain")
}

minecraft {
    version(libs.versions.minecraft.get())
    platform(MinecraftPlatform.SERVER)
}

tasks.named<JavaExec>("run") {
    args(
        createArgument("api", "api", "java"),
        createArgument("server", "server", "java"),
        createArgument("resources", "server", "resources"),
    )
}

private fun createArgument(name: String, project: String, path: String): String {
    return "--$name=" + project(":$project").layout.projectDirectory.dir("src").dir("generated").dir(path)
}