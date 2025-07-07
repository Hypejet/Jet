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
}

application {
    mainClass.set("net.hypejet.jet.data.generator.GeneratorMain")
}

minecraft {
    version(libs.versions.minecraft.get())
    platform(MinecraftPlatform.SERVER)
}