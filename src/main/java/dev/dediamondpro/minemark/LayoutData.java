package dev.dediamondpro.minemark;

import java.util.ArrayList;
import java.util.function.Consumer;

public class LayoutData {
    private final ArrayList<Consumer<MarkDownElementPosition>> elementListeners = new ArrayList<>();
    private MarkDownLine currentLine = new MarkDownLine(0f);
    private final float maxWidth;
    private boolean topSpacingLocked = false;
    private boolean bottomSpacingLocked = false;

    public LayoutData(float maxWidth) {
        this.maxWidth = maxWidth;
    }

    public boolean isLineEmpty() {
        return currentLine.width == 0f;
    }

    public boolean isLineOccupied() {
        return currentLine.width != 0f;
    }

    public void nextLine() {
        currentLine = new MarkDownLine(currentLine.getBottomY());
        topSpacingLocked = false;
        bottomSpacingLocked = false;
    }

    public MarkDownElementPosition addElement(LayoutStyle.Alignment alignment, float width, float height) {
        MarkDownElementPosition position = new MarkDownElementPosition(currentLine, currentLine.width, width, height, maxWidth, alignment);
        if (!elementListeners.isEmpty()) {
            elementListeners.get(0).accept(position);
        }
        addX(width);
        updateLineHeight(height);
        return position;
    }

    /**
     * Add a consumer that will be called when an element is added.
     * Only the last added listener is called to avoid conflicts
     *
     * @param listener The listener
     */
    public void addElementListener(Consumer<MarkDownElementPosition> listener) {
        elementListeners.add(0, listener);
    }

    /**
     * Remove the last listener that was added
     */
    public void removeElementListener() {
        elementListeners.remove(0);
    }

    public float getX() {
        return currentLine.width;
    }

    public void addX(float xMovement) {
        currentLine.width += xMovement;
    }

    public float getY() {
        return currentLine.y;
    }

    public float getLineHeight() {
        return currentLine.height;
    }

    public void setLineHeight(float lineHeight) {
        currentLine.height = lineHeight;
    }

    public void updateLineHeight(float lineHeight) {
        currentLine.height = Math.max(currentLine.height, lineHeight);
    }

    public float getMaxWidth() {
        return maxWidth;
    }

    public MarkDownLine getCurrentLine() {
        return currentLine;
    }

    public void updateTopSpacing(float spacing) {
        if (topSpacingLocked) return;
        currentLine.topSpacing = Math.max(currentLine.topSpacing, spacing);
    }

    public void updateBottomSpacing(float spacing) {
        if (bottomSpacingLocked) return;
        currentLine.bottomSpacing = Math.max(currentLine.bottomSpacing, spacing);
    }

    public void updatePadding(float padding) {
        updateTopSpacing(padding);
        updateBottomSpacing(padding);
    }

    public void setTopSpacing(float spacing) {
        if (topSpacingLocked) return;
        currentLine.topSpacing = spacing;
    }

    public void setBottomSpacing(float spacing) {
        if (bottomSpacingLocked) return;
        currentLine.bottomSpacing = spacing;
    }

    public void lockTopSpacing() {
        topSpacingLocked = true;
    }

    public void lockBottomSpacing() {
        bottomSpacingLocked = true;
    }

    public static class MarkDownElementPosition {
        private final MarkDownLine line;
        private final float x;
        private final float width;
        private final float height;
        private final float maxWidth;
        private final LayoutStyle.Alignment alignment;

        public MarkDownElementPosition(MarkDownLine line, float x, float width, float height, float maxWidth, LayoutStyle.Alignment alignment) {
            this.line = line;
            this.x = x;
            this.width = width;
            this.height = height;
            this.maxWidth = maxWidth;
            this.alignment = alignment;
        }

        public MarkDownLine getLine() {
            return line;
        }

        public float getX() {
            switch (alignment) {
                case LEFT:
                    return x;
                case CENTER:
                    return maxWidth / 2f - line.width / 2f + x;
                case RIGHT:
                    return maxWidth - line.width + x;
            }
            throw new IllegalStateException("Unknown alignment" + alignment);
        }

        public float getRightX() {
            return getX() + getWidth();
        }

        public float getY() {
            return getBottomY() - getHeight();
        }

        public float getBottomY() {
            return line.getBottomY() - line.getBottomSpacing();
        }

        public float getHeight() {
            return height;
        }

        public float getWidth() {
            return width;
        }

        public boolean isInside(float x, float y) {
            return x >= getX() && x <= getRightX() &&
                    y >= getY() && y <= getBottomY();
        }
    }

    public static class MarkDownLine {
        private final float y;
        private float width = 0f;
        private float height = 0f;
        private float topSpacing = 0f;
        private float bottomSpacing = 0f;

        public MarkDownLine(float y) {
            this.y = y;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return topSpacing + height + bottomSpacing;
        }

        public float getRawHeight() {
            return height;
        }

        public float getBottomY() {
            return y + getHeight();
        }

        public float getBottomSpacing() {
            return bottomSpacing;
        }

        public float getTopSpacing() {
            return topSpacing;
        }
    }
}
