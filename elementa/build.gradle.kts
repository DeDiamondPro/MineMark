plugins {
    kotlin("jvm") version "1.6.10"
}

tasks.compileKotlin.configure {
    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs.filterNot {
            it.startsWith("-Xjvm-default=")
        } + listOf("-Xjvm-default=" + "all-compatibility")
    }
}

repositories {
    maven(url = "https://repo.essential.gg/repository/maven-public")
}

dependencies {
    implementation("gg.essential:elementa-1.8.9-forge:600")
    compileOnly("org.lwjgl:lwjgl-opengl:3.3.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}