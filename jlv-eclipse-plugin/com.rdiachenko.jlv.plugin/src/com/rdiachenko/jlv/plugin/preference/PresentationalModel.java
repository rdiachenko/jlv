package com.rdiachenko.jlv.plugin.preference;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

public class PresentationalModel {

    private boolean levelAsImage;

    private int fontSize;

    private final List<PresentationalModelItem> modelItems;
    
    public PresentationalModel(boolean levelAsImage, int fontSize, List<PresentationalModelItem> modelItems) {
        Preconditions.checkNotNull(modelItems, "Model item list is null");
        this.levelAsImage = levelAsImage;
        this.fontSize = fontSize;
        this.modelItems = Collections.unmodifiableList(modelItems);
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
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(PresentationalModel.class.getName());
        builder.append("[levelAsImage=").append(levelAsImage)
                .append(", fontSize=").append(fontSize)
                .append(", modelItems=").append(modelItems);
        return builder.toString();
    }
}
