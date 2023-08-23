plugins {
    id("java-library")
}

group = "dev.dediamondpro"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.commonmark:commonmark:0.21.0")
    implementation("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

allprojects {
    apply(plugin = "java-library")
}

subprojects {
    dependencies {
        api(project.rootProject)
    }
}

tasks.test {
    useJUnitPlatform()
}