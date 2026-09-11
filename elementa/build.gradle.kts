/*
 * This file is part of MineMark
 * Copyright (C) 2024-2026 DeDiamondPro
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

import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    kotlin("jvm") version "2.2.20"
}

kotlin {
    compilerOptions {
        languageVersion = KotlinVersion.KOTLIN_1_9
        apiVersion = KotlinVersion.KOTLIN_1_9
        jvmTarget = JvmTarget.JVM_1_8
        freeCompilerArgs.add("-Xjvm-default=all-compatibility")
    }
}

repositories {
    maven("https://repo.essential.gg/repository/maven-public")
}

val testGui: SourceSet by sourceSets.creating
configurations["testGuiImplementation"].extendsFrom(configurations["api"], configurations["implementation"])

dependencies {
    // Lowest kotlin version that elementa supports.
    api(kotlin("stdlib", "1.6.10"))

    implementation(libs.elementa)
    compileOnly(libs.universalcraft) { isTransitive = false }
    api(libs.commonmark.ext.striketrough)
    api(libs.commonmark.ext.tables)

    "testGuiImplementation"(sourceSets["main"].output)
    "testGuiImplementation"(libs.universalcraft)
    "testGuiRuntimeOnly"(kotlin("stdlib"))
}

tasks.register<JavaExec>("runTestGui") {
    group = "verification"
    description = "Run the Elementa test GUI"
    mainClass.set("dev.dediamondpro.minemark.elementa.testgui.TestGuiKt")
    classpath = testGui.runtimeClasspath
    if (System.getProperty("os.name").startsWith("Mac")) {
        jvmArgs("-XstartOnFirstThread")
    }
}
