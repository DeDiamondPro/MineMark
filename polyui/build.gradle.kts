import org.jetbrains.kotlin.gradle.dsl.JvmTarget

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
    kotlin("jvm") version "2.0.21" // Same version as PolyUI
}

repositories {
    maven("https://repo.polyfrost.org/releases")
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

dependencies {
    implementation(libs.polyui)
    implementation(libs.commonmark.ext.striketrough)
    implementation(libs.commonmark.ext.tables)

    testImplementation(kotlin("test"))

    // Taken from PolyUI for testing since they don't publish any rendering implementations?
    // version of LWJGL to use. Recommended to be latest.
    val lwjglVersion = "3.3.3"

    // list of modules that this implementation needs to work.
    val lwjglModules = listOf("nanovg", "opengl", "stb", "glfw", null)

    // list of platforms that this implementation will support.
    val nativePlatforms = listOf("windows", "linux", "macos", "macos-arm64")

    for (module in lwjglModules) {
        val dep = if(module == null) "org.lwjgl:lwjgl:$lwjglVersion" else "org.lwjgl:lwjgl-$module:$lwjglVersion"
        testImplementation(dep)
        for (platform in nativePlatforms) {
            testRuntimeOnly("$dep:natives-$platform")
        }
    }
    testImplementation("org.apache.logging.log4j:log4j-api:2.24.1")
}

tasks.test {
    useJUnitPlatform()
}