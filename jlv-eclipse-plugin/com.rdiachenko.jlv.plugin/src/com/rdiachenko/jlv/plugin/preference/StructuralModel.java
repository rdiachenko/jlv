package com.rdiachenko.jlv.plugin.preference;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.rdiachenko.jlv.plugin.LogField;

public class StructuralModel {
    
    private final List<StructuralModelItem> modelItems;
    
    private final Map<LogField, StructuralModelItem> fieldToMedelItem = new EnumMap<>(LogField.class);

    public StructuralModel(List<StructuralModelItem> modelItems) {
        Preconditions.checkNotNull(modelItems, "Model item list is null");
        this.modelItems = new ArrayList<>(modelItems);

        for (StructuralModelItem item : modelItems) {
            fieldToMedelItem.put(LogField.valueOf(item.getFieldName().toUpperCase()), item);
        }
    }
    
    public List<StructuralModelItem> getModelItems() {
        return modelItems;
    }
    
    public StructuralModelItem getModelItem(LogField field) {
        return fieldToMedelItem.get(field);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(StructuralModel.class.getName());
        builder.append("[modelItems=").append(modelItems).append("]");
        return builder.toString();
    }
}
