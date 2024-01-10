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

package dev.dediamondpro.minemark.style;

public interface Style {

    TextStyleConfig getTextStyle();

    ParagraphStyleConfig getParagraphStyle();

    LinkStyleConfig getLinkStyle();

    HeadingStyleConfig getHeadingStyle();

    HorizontalRuleStyleConfig getHorizontalRuleStyle();

    ImageStyleConfig getImageStyle();

    ListStyleConfig getListStyle();

    BlockquoteStyleConfig getBlockquoteStyle();

    CodeBlockStyleConfig getCodeBlockStyle();

    TableStyleConfig getTableStyle();
}
