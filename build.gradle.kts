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
version = "1.2.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.commonmark)
    implementation(libs.tagsoup)
    compileOnly(libs.jetbrains.annotations)
}

tasks {
    build {
        dependsOn(gradle.includedBuild("minecraft").task(":buildAll"))
    }
    clean {
        dependsOn(gradle.includedBuild("minecraft").task(":cleanAll"))
    }
    publish {
        dependsOn(gradle.includedBuild("minecraft").task(":publishAll"))
    }
    publishToMavenLocal {
        dependsOn(gradle.includedBuild("minecraft").task(":publishToMavenLocalAll"))
    }
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
            name = if (name != "minemark") "minemark-$name" else "minemark-core"
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