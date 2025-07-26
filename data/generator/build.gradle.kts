import org.spongepowered.gradle.vanilla.repository.MinecraftPlatform

plugins {
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

minecraft {
    version(libs.versions.minecraft.get())
    platform(MinecraftPlatform.SERVER)
}