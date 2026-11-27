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

package dev.dediamondpro.minemark.providers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;

public class DefaultBrowserProvider implements BrowserProvider {
    public static final DefaultBrowserProvider INSTANCE = new DefaultBrowserProvider();

    protected DefaultBrowserProvider() {
    }

    @Override
    public void browse(String url) {
        try {
            URI uri = new URI(url);
            if (!isWebUri(uri)) return;
            java.awt.Desktop.getDesktop().browse(uri);
        } catch (java.io.IOException | URISyntaxException | UnsupportedOperationException e) {
            e.printStackTrace();
        }
    }

    public static boolean isWebUri(URI uri) {
        // Don't allow things like "file://"
        String scheme = uri.getScheme();
        if (scheme == null) return false;
        scheme = scheme.toLowerCase(Locale.ROOT);
        return scheme.equals("http") || scheme.equals("https");
    }
}
