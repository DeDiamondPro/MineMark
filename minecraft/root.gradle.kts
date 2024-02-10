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
    alias(libs.plugins.loom) apply false
    alias(libs.plugins.preprocessorRoot)
}

preprocess {
    val fabric12004 = createNode("1.20.4-fabric", 12004, "yarn")
    val fabric11904 = createNode("1.19.4-fabric", 11904, "yarn")

    val forge12004 = createNode("1.20.4-forge", 12004, "yarn")
    val forge11904 = createNode("1.19.4-forge", 11904, "yarn")

    val neoforge12004 = createNode("1.20.4-neoforge", 12004, "yarn")

    fabric11904.link(fabric12004)

    forge12004.link(fabric12004)
    forge11904.link(fabric11904)

    neoforge12004.link(forge12004, file("versions/forge-neoforge"))
}


tasks {
    register("buildAll") {
        subprojects.forEach { dependsOn("${it.name}:build") }
    }
    register("cleanAll") {
        subprojects.forEach { dependsOn("${it.name}:clean") }
    }
    register("publishAll") {
        subprojects.forEach { dependsOn("${it.name}:publish") }
    }
    register("publishToMavenLocalAll") {
        subprojects.forEach { dependsOn("${it.name}:publishToMavenLocal") }
    }
}