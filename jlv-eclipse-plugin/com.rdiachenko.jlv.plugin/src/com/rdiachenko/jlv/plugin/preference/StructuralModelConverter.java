package com.rdiachenko.jlv.plugin.preference;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.google.common.base.Preconditions;

public final class StructuralModelConverter {

  private static final String COLON = ":";
  private static final String SEMICOLON = ";";

  private StructuralModelConverter() {
    // Utility class
  }

  public static String toString(StructuralModel model) {
    Preconditions.checkNotNull(model, "Model is null");
    StringBuilder builder = new StringBuilder();
    builder.append(model.getModelItems().size()).append(SEMICOLON);

    for (StructuralModelItem item : model.getModelItems()) {
      builder.append(item.getFieldName()).append(COLON)
          .append(item.isDisplay()).append(COLON)
          .append(item.getWidth()).append(SEMICOLON);
    }
    return builder.toString();
  }

  public static StructuralModel toModel(String data) {
    Preconditions.checkNotNull(data, "Data is null");
    String delimeters = COLON + SEMICOLON;
    StringTokenizer tokenizer = new StringTokenizer(data, delimeters);
    int modelItemCount = Integer.parseInt(tokenizer.nextToken());
    List<StructuralModelItem> modelItems = new ArrayList<>();

    for (int i = 0; i < modelItemCount; i++) {
      String fieldName = tokenizer.nextToken();
      boolean display = Boolean.parseBoolean(tokenizer.nextToken());
      int width = Integer.parseInt(tokenizer.nextToken());
      modelItems.add(new StructuralModelItem(fieldName, display, width));
    }
    return new StructuralModel(modelItems);
  }
}
