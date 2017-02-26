package com.rdiachenko.jlv.plugin.preference;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.common.base.Preconditions;

public final class PresentationalModelConverter {
    
    private static final String COLON = ":";
    private static final String SEMICOLON = ";";
    private static final String PLUS = "+";
    
    private PresentationalModelConverter() {
        // Utility class
    }
    
    public static String toString(PresentationalModel model) {
        Preconditions.checkNotNull(model, "Model is null");
        StringBuilder builder = new StringBuilder();
        builder.append(model.isLevelAsImage()).append(SEMICOLON)
                .append(model.getFontSize()).append(SEMICOLON)
                .append(model.getModelItems().size()).append(SEMICOLON);

        for (PresentationalModelItem item : model.getModelItems()) {
            builder.append(item.getLevelName()).append(COLON)
                    .append(toString(item.getForeground())).append(COLON)
                    .append(toString(item.getBackground())).append(SEMICOLON);
        }
        return builder.toString();
    }
    
    public static PresentationalModel toModel(String data) {
        String delimeters = COLON + SEMICOLON;
        StringTokenizer tokenizer = new StringTokenizer(data, delimeters);
        boolean levelAsImage = Boolean.parseBoolean(tokenizer.nextToken());
        int fontSize = Integer.parseInt(tokenizer.nextToken());
        int modelItemCount = Integer.parseInt(tokenizer.nextToken());
        List<PresentationalModelItem> modelItems = new ArrayList<>();
        
        for (int i = 0; i < modelItemCount; i++) {
            String levelName = tokenizer.nextToken();
            Rgb foreground = toRgb(tokenizer.nextToken());
            Rgb background = toRgb(tokenizer.nextToken());
            modelItems.add(new PresentationalModelItem(levelName, foreground, background));
        }
        return new PresentationalModel(levelAsImage, fontSize, modelItems);
    }

    private static String toString(Rgb rgb) {
        StringBuilder builder = new StringBuilder();
        builder.append(rgb.getRed()).append(PLUS)
                .append(rgb.getGreen()).append(PLUS)
                .append(rgb.getBlue());
        return builder.toString();
    }
    
    private static Rgb toRgb(String data) {
        StringTokenizer tokenizer = new StringTokenizer(data, PLUS);
        int red = Integer.parseInt(tokenizer.nextToken());
        int green = Integer.parseInt(tokenizer.nextToken());
        int blue = Integer.parseInt(tokenizer.nextToken());
        return new Rgb(red, green, blue);
    }
}
