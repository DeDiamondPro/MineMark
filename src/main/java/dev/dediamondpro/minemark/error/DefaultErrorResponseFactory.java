/*
 * This file is part of MineMark
 * Copyright (C) 2026 DeDiamondPro
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

package dev.dediamondpro.minemark.error;

import dev.dediamondpro.minemark.MineMarkCore;
import dev.dediamondpro.minemark.elements.MineMarkElement;
import dev.dediamondpro.minemark.style.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class DefaultErrorResponseFactory<S extends Style, R> implements ErrorResponseFactory<S, R> {
    private static final String MINEMARK_PACKAGE = getRootPackage();
    private static final String ISSUES_URL = "https://github.com/DeDiamondPro/MineMark/issues";
    private static final String[] PLATFORM_PACKAGES = {"java.", "javax.", "jdk.", "sun.", "com.sun.", "kotlin."};

    private final MineMarkCore<S, R> core;

    public DefaultErrorResponseFactory(MineMarkCore<S, R> core) {
        this.core = core;
    }

    @Override
    public @Nullable MineMarkElement<S, R> build(@NotNull S style, @NotNull Phase phase, @NotNull Throwable exception) {
        try {
            MineMarkElement<S, R> element = core.parse(
                    style,
                    buildMarkdown(phase, exception),
                    StandardCharsets.UTF_8,
                    false // Don't catch errors in this parse, would cause error recursion
            );
            element.setErrorResponseFactory(null); // We don't want an error element to recursively cause more errors
            return element;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected String buildMarkdown(@NotNull Phase phase, @NotNull Throwable exception) {
        StringBuilder sb = new StringBuilder();
        sb.append("# An error has occurred\n");
        sb.append("An error has occurred");
        if (phase != Phase.UNKNOWN) {
            sb.append(" during the ").append(phase.name().toLowerCase(Locale.ROOT).replace('_', ' ')).append(" phase");
        }
        sb.append(" of the Markdown element: `").append(exception.getClass().getName()).append("`\n\n");
        if (isMineMarkError(exception)) {
            sb.append("This appears to be a bug in MineMark, please [report it here](").append(ISSUES_URL).append(").\n\n");
        }
        sb.append("```\n").append(getStackTrace(exception, 50).replace("```", "` ` `")).append("\n```\n");
        return sb.toString();
    }

    protected static String getStackTrace(Throwable exception, int maxLength) {
        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String result = writer.toString().trim().replace("\t", "    ");
        if (maxLength > 0) {
            String[] parts = result.split("\n");
            if (parts.length > maxLength) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < maxLength; i++) {
                    sb.append(parts[i]).append("\n");
                }
                result = sb.toString().trim();
            }
        }
        return result;
    }

    /**
     * Try to determine if the error came from MineMark's own code
     */
    protected boolean isMineMarkError(@NotNull Throwable exception) {
        Throwable root = exception;
        for (int depth = 0; depth < 32 && root.getCause() != null && root.getCause() != root; depth++) {
            root = root.getCause();
        }
        for (StackTraceElement frame : root.getStackTrace()) {
            String className = frame.getClassName();
            if (isPlatformClass(className)) continue;
            return className.startsWith(MINEMARK_PACKAGE);
        }
        return false;
    }

    private static boolean isPlatformClass(String className) {
        for (String pkg : PLATFORM_PACKAGES) {
            if (className.startsWith(pkg)) return true;
        }
        return false;
    }

    private static String getRootPackage() {
        // Get the MineMark package at runtime to support relocation
        String name = MineMarkCore.class.getName();
        return name.substring(0, name.lastIndexOf('.') + 1);
    }
}
