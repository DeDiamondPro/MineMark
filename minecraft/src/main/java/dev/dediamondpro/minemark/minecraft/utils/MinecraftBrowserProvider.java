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

package dev.dediamondpro.minemark.minecraft.utils;

import dev.dediamondpro.minemark.providers.BrowserProvider;
import dev.dediamondpro.minemark.providers.DefaultBrowserProvider;
import net.minecraft.Util;

import java.net.URI;
import java.net.URISyntaxException;

public class MinecraftBrowserProvider implements BrowserProvider {
    public static MinecraftBrowserProvider INSTANCE = new MinecraftBrowserProvider();

    private MinecraftBrowserProvider() {
    }

    @Override
    public void browse(String url) {
        try {
            // Don't allow things like "file://"
            URI uri = new URI(url);
            if (!DefaultBrowserProvider.isWebUri(uri)) return;
            Util.getPlatform().openUri(uri);
        } catch (URISyntaxException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }
}
