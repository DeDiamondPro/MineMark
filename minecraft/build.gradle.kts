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

import org.jetbrains.kotlin.com.intellij.lang.java.JavaLanguage

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
    java
    alias(libs.plugins.architecturyPlugin)
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    id(libs.plugins.loom.get().pluginId)
    id(libs.plugins.preprocessor.get().pluginId)
    `maven-publish`
}

val modPlatform = Platform.of(project)
buildscript {
    // Set loom to the correct platform
    project.extra.set("loom.platform", project.name.substringAfter("-"))
}

architectury {
    platformSetupLoomIde()
    when (modPlatform.loader) {
        Platform.Loader.Fabric -> fabric()
        Platform.Loader.Forge -> forge()
        Platform.Loader.NeoForge -> neoForge()
    }
}

val loadExampleMod = project.findProperty("loadExampleMod")?.toString()?.toBoolean() ?: false

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project
val mod_description: String by project
val mod_license: String by project

version = mod_version
group = "dev.dediamondpro"

blossom {
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
    replaceToken("@VERSION@", mod_version)
}

preprocess {
    vars.put("MC", modPlatform.mcVersion)
    vars.put("FABRIC", if (modPlatform.isFabric) 1 else 0)
    vars.put("FORGE", if (modPlatform.isForge) 1 else 0)
    vars.put("NEOFORGE", if (modPlatform.isNeoForge) 1 else 0)
    vars.put("FORGELIKE", if (modPlatform.isForgeLike) 1 else 0)
}

repositories {
    maven("https://maven.neoforged.net/releases")
}

dependencies {
    minecraft("com.mojang:minecraft:${modPlatform.mcVersionStr}")
    val yarnVersion = when (modPlatform.mcVersion) {
        12004 -> "1.20.4+build.3"
        11904 -> "1.19.4+build.2"
        else -> error("No mappings defined for ${modPlatform.mcVersion}")
    }
    mappings("net.fabricmc:yarn:${yarnVersion}:v2")

    if (modPlatform.isFabric) {
        modImplementation("net.fabricmc:fabric-loader:0.15.6")
        val fabricApiVersion = when (modPlatform.mcVersion) {
            12004 -> "0.92.1+1.20.4"
            11904 -> "0.87.2+1.19.4"
            else -> error("No fabric API version defined for ${modPlatform.mcVersion}")
        }
        // Only required for example mod
        modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
    } else if (modPlatform.isForge) {
        val forgeVersion = when (modPlatform.mcVersion) {
            12004 -> "49.0.26"
            11904 -> "45.2.0"
            else -> error("No forge version defined for ${modPlatform.mcVersion}")
        }
        "forge"("net.minecraftforge:forge:${modPlatform.mcVersionStr}-$forgeVersion")
    } else if (modPlatform.isNeoForge) {
        val neoforgeVersion = when (modPlatform.mcVersion) {
            12004 -> "20.4.148-beta"
            else -> error("No neoforge version defined for ${modPlatform.mcVersion}")
        }
        "neoForge"("net.neoforged:neoforge:$neoforgeVersion")
    }

    implementation("dev.dediamondpro:minemark:${mod_version}")
    implementation(libs.commonmark.ext.striketrough)
    implementation(libs.commonmark.ext.tables)
}

val shade: Configuration by configurations.creating {
    configurations.implementation.get().extendsFrom(this)
}

base.archivesName = "$mod_name (${modPlatform.mcVersionStr}-${modPlatform.loaderStr})"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(
            if (modPlatform.mcVersion >= 11800) 17 else if (modPlatform.mcVersion >= 11700) 16 else 8
        )
    }
    withSourcesJar()
    withJavadocJar()
}

tasks {
    listOf("sourcesJar", "javadocJar").forEach {
        named<Jar>(it) {
            exclude("com/example/examplemod/**", "META-INF/mods.toml", "fabric.mod.json")
        }
    }
    processResources {
        outputs.upToDateWhen { false }
        val properties = mapOf(
            "id" to mod_id,
            "name" to mod_name,
            "version" to mod_version,
            "description" to mod_description,
            "license" to mod_license,
        )
        inputs.properties(properties)
        filesMatching(listOf("fabric.mod.json", "META-INF/mods.toml")) {
            expand(properties)
        }
        if (modPlatform.isFabric || !loadExampleMod) {
            exclude("META-INF/mods.toml", "pack.mcmeta")
        }
        if (modPlatform.isForgeLike || !loadExampleMod) {
            exclude("fabric.mod.json")
        }
    }
    shadowJar {
        archiveClassifier.set("dev")
        configurations = listOf(shade)

        if (modPlatform.isForgeLike) {
            manifest.attributes(mapOf("FMLModType" to "LIBRARY"))
        }
        if (!loadExampleMod) {
            exclude("com/example/examplemod/**")
        }
    }
    remapJar {
        input.set(shadowJar.get().archiveFile)
        archiveClassifier.set("")
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<Javadoc> {
        options.encoding = "UTF-8"
    }
}

publishing {
    publications {
        register<MavenPublication>("minemark-minecraft-$modPlatform") {
            version = mod_version
            groupId = "dev.dediamondpro"
            artifactId = "minemark-minecraft-$modPlatform"

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

if (modPlatform.isForge) {
    sourceSets.forEach {
        val dir = layout.buildDirectory.dir("sourcesSets/$it.name")
        it.output.setResourcesDir(dir)
        it.java.destinationDirectory = dir
    }
}

data class Platform(
    val mcMajor: Int,
    val mcMinor: Int,
    val mcPatch: Int,
    val loader: Loader
) {
    val mcVersion = mcMajor * 10000 + mcMinor * 100 + mcPatch
    val mcVersionStr = listOf(mcMajor, mcMinor, mcPatch).dropLastWhile { it == 0 }.joinToString(".")
    val loaderStr = loader.toString().lowercase()

    val isFabric = loader == Loader.Fabric
    val isForge = loader == Loader.Forge
    val isNeoForge = loader == Loader.NeoForge
    val isForgeLike = loader == Loader.Forge || loader == Loader.NeoForge
    val isLegacy = mcVersion <= 11202

    override fun toString(): String {
        return "$mcVersionStr-$loaderStr"
    }

    enum class Loader {
        Fabric,
        Forge,
        NeoForge
    }

    companion object {
        fun of(project: Project): Platform {
            val (versionStr, loaderStr) = project.name.split("-", limit = 2)
            val (major, minor, patch) = versionStr.split('.').map { it.toInt() } + listOf(0)
            val loader = Loader.values().first { it.name.lowercase() == loaderStr.lowercase() }
            return Platform(major, minor, patch, loader)
        }
    }
}