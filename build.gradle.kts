plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.dediamondpro"
version = "1.0-SNAPSHOT59"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.commonmark:commonmark:0.21.0")
    implementation("org.ccil.cowan.tagsoup:tagsoup:1.2.1")
    implementation("org.jetbrains:annotations:24.0.0")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.commonmark:commonmark-ext-gfm-strikethrough:0.21.0")
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