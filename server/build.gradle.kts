import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    application
    alias(libs.plugins.shadow)
    alias(libs.plugins.checker.framework)
}

sourceSets.main {
    val generatedDir = layout.projectDirectory.dir("src").dir("generated")
    java.srcDirs(generatedDir.dir("java"))
    resources.srcDirs(generatedDir.dir("resources"))
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

val generatorProject = project(":data:generator")

tasks {
    generatorProject.afterEvaluate {
        classes {
            dependsOn(generatorProject.tasks.named("run"))
        }
    }
    withType<ShadowJar> {
        minimize {
            exclude(project(":api")) // Dependencies of the API may be used by plugins
            exclude(dependency(libs.logback.get())) // Minimizing logback causes problems with finding an SLF4J provider
        }
    }
}