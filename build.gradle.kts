import kotlin.system.exitProcess

/*
 * This file is part of MineMark
 * Copyright (C) 2024 DeDiamondPro
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.dediamondpro"
version = "1.2.3"

dependencies {
    implementation(libs.commonmark)
    implementation(libs.tagsoup)
    compileOnly(libs.jetbrains.annotations)
}

tasks {
    register("chiseledBuild") {
        project.allprojects.forEach { subProject ->
            if (!subProject.name.contains("minecraft")) {
                dependsOn(subProject.tasks.named("build"))
            }
        }
    }
    register("chiseledPublish") {
        project.allprojects.forEach { subProject ->
            if (!subProject.name.contains("minecraft")) {
                dependsOn(subProject.tasks.named("publish"))
            }
        }
    }
    register("chiseledPublishToMavenLocal") {
        project.allprojects.forEach { subProject ->
            if (!subProject.name.contains("minecraft")) {
                dependsOn(subProject.tasks.named("publishToMavenLocal"))
            }
        }
    }
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    java {
        // Apply java 8 to everything except the mc project
        if (project.parent?.name != "minecraft") {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        withSourcesJar()
        withJavadocJar()
    }

    repositories {
        mavenCentral()
    }

    publishing {
        publications {
            var name = project.name.lowercase()
            name = if (project.parent?.name == "minecraft") {
                "minemark-minecraft-$name"
            } else if (name != "minemark") {
                "minemark-$name"
            } else "minemark-core"

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
        implementation(project.rootProject)
    }
}

tasks.test {
    useJUnitPlatform()
}