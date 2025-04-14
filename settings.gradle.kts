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

dependencyResolutionManagement {
    versionCatalogs {
        create("libs")
    }
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net")
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7-alpha.6"
}

include(":elementa")

val mcPlatforms = listOf(
    "1.20.1-fabric",
    "1.20.1-forge",
)

include(":minecraft")
stonecutter {
    centralScript = "build.gradle.kts"
    kotlinController = true

    create(project(":minecraft")) {
        for (version in mcPlatforms) {
            vers(version, version.split("-")[0])
        }
        vcsVersion = "1.20.1-fabric"
    }
}

rootProject.name = "minemark"