import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
    application
    alias(libs.plugins.vanillaGradle)
}

dependencies {
    implementation(libs.javapoet)
    implementation(libs.picocli)
}

application {
    mainClass.set("net.hypejet.jet.data.generator.GeneratorMain")
}

minecraft {
    version(libs.versions.minecraft.get())
    platform(MinecraftPlatform.SERVER)
}