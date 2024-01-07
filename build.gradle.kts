plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.dediamondpro"
version = "1.0-SNAPSHOT92"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.commonmark)
    implementation(libs.tagsoup)
    compileOnly(libs.jetbrains.annotations)
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
        withSourcesJar()
        withJavadocJar()
    }

    publishing {
        publications {
            var name = project.name.lowercase()
            if (name != "minemark") name = "minemark-$name"
            else name = "minemark-core"
            register<MavenPublication>(name) {
                groupId = "dev.dediamondpro"
                artifactId = name

                from(components["java"])
            }
        }
        repositories {
            maven {
                name = "diamond"

                url = uri("https://maven.dediamondpro.dev/releases")

                credentials {
                    username = System.getenv("MAVEN_DIAMOND_USER")
                    password = System.getenv("MAVEN_DIAMOND_PASSWORD")
                }

                version = rootProject.version
            }
        }
    }
}

subprojects {
    dependencies {
        api(project.rootProject)
    }
}

tasks.test {
    useJUnitPlatform()
}