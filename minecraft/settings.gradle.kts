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

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.minecraftforge.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.fabricmc.net")
        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
            }
        }
    }

    resolutionStrategy.eachPlugin {
        when (requested.id.id) {
            "com.replaymod.preprocess" -> useModule("com.github.replaymod:preprocessor:${requested.version}")
            "com.replaymod.preprocess-root" -> useModule("com.github.replaymod:preprocessor:${requested.version}")
        }
    }
}

dependencyResolutionManagement {
    versionCatalogs.create("libs")
}

rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.20.4-fabric",
    "1.20.4-forge",
    "1.20.4-neoforge",
    "1.19.4-fabric",
    "1.19.4-forge",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }
}