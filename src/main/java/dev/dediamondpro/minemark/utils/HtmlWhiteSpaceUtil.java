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

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlWhiteSpaceUtil {
    public static final HtmlWhiteSpaceUtil INSTANCE = new HtmlWhiteSpaceUtil();
    private final Pattern PREFORMATTED_ELEMENT = Pattern.compile("<pre>(?:(?!</?pre>).)*?</pre>", Pattern.DOTALL);
    private final Pattern BEFORE_AFTER_LINEBREAK = Pattern.compile("\\s+$|^\\s+", Pattern.MULTILINE);
    private final Pattern HORIZONTAL_WHITESPACE = Pattern.compile("\\h");
    private final Pattern SUBSEQUENT_SPACE = Pattern.compile(" {2,}");
    private final Pattern SPACES_ACROSS_TAGS = Pattern.compile(" +((</?[^<>]+>)+) +");
    private final Pattern SPACES_START = Pattern.compile("^((</?[^<>]+>)+) +");
    private final Pattern SPACES_END = Pattern.compile(" +((</?[^<>]+>)+)$");

    private HtmlWhiteSpaceUtil() {
    }

    public String removeUnnecessaryWhiteSpace(String html) {
        // Based on the steps outlined here: https://developer.mozilla.org/en-US/docs/Web/API/Document_Object_Model/Whitespace
        // Start by removing leading and trailing whitespaces
        html = html.trim();

        // Find preformatted elements and replace them with a placeholder, we don't want to change the formatting
        LinkedHashMap<String, String> preformattedElements = null;
        Matcher preformattedElementsMatcher = PREFORMATTED_ELEMENT.matcher(html);
        int num = 0;
        boolean found = false;
        while (true) {
            while (preformattedElementsMatcher.find()) {
                // Initialize here to avoid a wasted initialization
                if (preformattedElements == null) {
                    preformattedElements = new LinkedHashMap<>();
                }
                // Find a key to replace the element with, this can absolutely not already be in the string
                String key = "%%%preformattedElement-" + num + "%%%%";
                while (html.contains(key)) {
                    num++;
                    key = "%%%preformattedElement-" + num + "%%%%";
                }
                num++;
                // Replace the element in the html with the key
                String element = preformattedElementsMatcher.group();
                // Store the element so it can be re-added later
                preformattedElements.put(key, element);
                // Remove the element from the html
                html = html.replace(element, key);

                found = true;
            }
            // If no more occurrences are found with a new matcher, we are done, break out of the loop
            if (!found) {
                break;
            }
            // Recreate the matcher to handle nested elements
            preformattedElementsMatcher = PREFORMATTED_ELEMENT.matcher(html);
            found = false;
        }

        // Ignore whitespace before and after line breaks
        html = BEFORE_AFTER_LINEBREAK.matcher(html).replaceAll("");
        // Replace all whitespace characters with normal spaces
        html = HORIZONTAL_WHITESPACE.matcher(html).replaceAll(" ");
        // Remove subsequent spaces
        html = SUBSEQUENT_SPACE.matcher(html).replaceAll(" ");
        // Remove spaces across tags
        html = SPACES_ACROSS_TAGS.matcher(html).replaceAll(" $1");
        // Remove spaces at the start and end of the string across the first and last tag
        html = SPACES_START.matcher(html).replaceAll("$1");
        html = SPACES_END.matcher(html).replaceAll("$1");

        // Add preformatted elements back in, in reverse order to handle nested elements
        if (preformattedElements != null) {
            ArrayList<String> keys = new ArrayList<>(preformattedElements.keySet());
            Collections.reverse(keys);
            for (String key : keys) {
                html = html.replace(key, preformattedElements.get(key));
            }
        }

        return html;
    }
}