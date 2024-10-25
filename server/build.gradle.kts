plugins {
    kotlin("jvm") version "1.9.22"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

val versionVar = version
val groupIdVar = "com.undefined"
val artifactIdVar = "stellar"

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.6-R0.1-SNAPSHOT")

    implementation(project(":api"))
    implementation(project(":common"))
    implementation(project(":v1_20_6:", "reobf"))
    compileOnly(project(":v1_20_6"))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    val shadowJar by getting(com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar::class) {
        archiveFileName.set("Stellar-shadow.jar")
    }

    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }

    runServer {
        minecraftVersion("1.20.6")
        jvmArgs("-Xmx2G")
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}
