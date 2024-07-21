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

package dev.dediamondpro.minemark.utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;


/**
 * Utility class to add a prefix to a reader, used by MineMarkCore to trick the Markdown parser to activate early
 */
public class PrefixedReader extends Reader {
    private final StringReader prefixReader;
    private final Reader mainReader;
    private boolean prefixDone;

    public PrefixedReader(String prefix, Reader mainReader) {
        this.prefixReader = new StringReader(prefix);
        this.mainReader = mainReader;
        this.prefixDone = false;
    }

    @Override
    public int read(char @NotNull [] cbuf, int off, int len) throws IOException {
        // Read from the prefixReader first
        if (!prefixDone) {
            int numRead = prefixReader.read(cbuf, off, len);
            if (numRead == -1) {
                prefixDone = true; // Prefix is done, switch to mainReader
            } else {
                return numRead;
            }
        }

        // Now read from the mainReader
        return mainReader.read(cbuf, off, len);
    }

    @Override
    public void close() throws IOException {
        prefixReader.close();
        mainReader.close();
    }
}
