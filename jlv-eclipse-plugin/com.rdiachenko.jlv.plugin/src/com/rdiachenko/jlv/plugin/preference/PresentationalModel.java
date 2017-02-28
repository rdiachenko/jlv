package com.rdiachenko.jlv.plugin.preference;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.rdiachenko.jlv.plugin.LogLevel;

public class PresentationalModel {
    
    private boolean levelAsImage;
    
    private int fontSize;
    
    private final List<PresentationalModelItem> modelItems;

    private final Map<LogLevel, PresentationalModelItem> levelToMedelItem = new EnumMap<>(LogLevel.class);

    public PresentationalModel(boolean levelAsImage, int fontSize, List<PresentationalModelItem> modelItems) {
        Preconditions.checkNotNull(modelItems, "Model item list is null");
        this.levelAsImage = levelAsImage;
        this.fontSize = fontSize;
        this.modelItems = Collections.unmodifiableList(modelItems);

        for (PresentationalModelItem item : modelItems) {
            levelToMedelItem.put(LogLevel.toLogLevel(item.getLevelName()), item);
        }
    }
    
    public boolean isLevelAsImage() {
        return levelAsImage;
    }
    
    public void setLevelAsImage(boolean levelAsImage) {
        this.levelAsImage = levelAsImage;
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
    
    public List<PresentationalModelItem> getModelItems() {
        return modelItems;
    }
    
    public PresentationalModelItem getModelItem(LogLevel level) {
        return levelToMedelItem.get(level);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(PresentationalModel.class.getName());
        builder.append("[levelAsImage=").append(levelAsImage)
                .append(", fontSize=").append(fontSize)
                .append(", modelItems=").append(modelItems).append("]");
        return builder.toString();
    }
}
