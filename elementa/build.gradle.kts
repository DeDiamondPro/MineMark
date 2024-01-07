plugins {
    // Lowest kotlin version that elementa supports
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
    maven("https://repo.essential.gg/repository/maven-public")
}

dependencies {
    implementation(libs.elementa)
    implementation(libs.commonmark.ext.striketrough)
    implementation(libs.commonmark.ext.tables)
}