/*
 * This file is part of MineMark
 * Copyright (C) 2024-2025 DeDiamondPro
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

import dev.dediamondpro.buildsource.Platform
import dev.dediamondpro.buildsource.VersionDefinition

plugins {
    id("dev.architectury.loom") version "1.10-SNAPSHOT"
}

buildscript {
    // Set loom platform to correct loader
    extra["loom.platform"] = project.name.split('-')[1]
}

val mcPlatform = Platform.fromProject(project)
val buildTestMod = false

stonecutter {
    const("fabric", mcPlatform.isFabric)
    const("forge", mcPlatform.isForge)
    const("neoforge", mcPlatform.isNeoForge)
    const("forgelike", mcPlatform.isForgeLike)
}

val javaVersion = VersionDefinition(
    "1.20.1" to "17",
    default = "21",
)
val fabricApiVersion = VersionDefinition(
    "1.20.1" to "0.92.3+1.20.1",
    "1.21.1" to "0.114.0+1.21.1",
    "1.21.4" to "0.118.0+1.21.4",
    "1.21.5" to "0.119.4+1.21.5",
)
val forgeVersion = VersionDefinition(
    "1.20.1" to "1.20.1-47.3.0",
    "1.21.1" to "1.21.1-52.0.40",
    "1.21.4" to "1.21.4-54.1.0",
    "1.21.5" to "1.21.5-55.0.4"
)
val neoForgeVersion = VersionDefinition(
    "1.21.1" to "21.1.95",
    "1.21.4" to "21.4.124",
    "1.21.5" to "21.5.34-beta"
)

repositories {
    mavenCentral()
    maven("https://maven.neoforged.net/releases/")
}

dependencies {
    minecraft("com.mojang:minecraft:${mcPlatform.versionString}")
    mappings(loom.officialMojangMappings())

    implementation(libs.commonmark.ext.striketrough)
    implementation(libs.commonmark.ext.tables)

    if (mcPlatform.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:0.16.10")
    } else if (mcPlatform.isForge) {
        "forge"("net.minecraftforge:forge:${forgeVersion.get(mcPlatform)}")
    } else if (mcPlatform.isNeoForge) {
        "neoForge"("net.neoforged:neoforge:${neoForgeVersion.get(mcPlatform)}")
    }

    if (buildTestMod) {
        include(rootProject)
        include(libs.commonmark)
        include(libs.commonmark.ext.striketrough)
        include(libs.commonmark.ext.tables)
        include(libs.tagsoup)

        if (mcPlatform.isFabric) {
            modImplementation("net.fabricmc.fabric-api:fabric-api:${fabricApiVersion.get(mcPlatform)}")
        }
    }
}

if (mcPlatform.isFabric) {
    tasks.jar {
        manifest {
            attributes("Fabric-Loom-Remap" to "true")
        }
    }
}

tasks.processResources {
    if (!buildTestMod) {
        exclude("META-INF/mods.toml", "pack.mcmeta")
    }
    if (mcPlatform.isForgeLike || !buildTestMod) {
        exclude("fabric.mod.json")
    }
}
tasks.compileJava {
    if (!buildTestMod) {
        exclude("com/example/examplemod/**")
    }
}
tasks.javadoc {
    exclude("com/example/examplemod/**")
}
tasks.sourcesJar {
    exclude("com/example/examplemod/**")
}

configure<JavaPluginExtension> {
    toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.get(mcPlatform)))
}