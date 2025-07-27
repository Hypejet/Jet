rootProject.name = "jet-parent"

pluginManagement {
    repositories {
        maven("https://repo.spongepowered.org/repository/maven-public/")
    }
}

include("api", "server", "data:generator", "data:json")