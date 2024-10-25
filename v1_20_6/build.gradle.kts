plugins{
    kotlin("jvm") version "1.9.22"
    id("io.papermc.paperweight.userdev") version "1.7.1"
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.REOBF_PRODUCTION

dependencies {
    paperweight.paperDevBundle("1.20.6-R0.1-SNAPSHOT")
    implementation("com.mojang:brigadier:1.0.18")
    compileOnly(project(":common"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "21"
    }
}

java {
    disableAutoTargetJvm()
}

kotlin {
    jvmToolchain(21)
}