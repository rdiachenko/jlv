package com.rdiachenko.jlv.plugin.preference;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.plugin.JlvActivator;
import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.LogField;
import com.rdiachenko.jlv.plugin.LogLevel;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

  @Override
  public void initializeDefaultPreferences() {
    IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
    store.setDefault(JlvConstants.SERVER_PORT_PREF_KEY, 4445);
    store.setDefault(JlvConstants.SERVER_AUTO_START_PREF_KEY, true);
    store.setDefault(JlvConstants.LOGLIST_BUFFER_SIZE_PREF_KEY, 5000);
    store.setDefault(JlvConstants.LOGLIST_REFRESH_TIME_MS_PREF_KEY, 800);
    store.setDefault(JlvConstants.QUICK_SEARCH_VISIBLE_PREF_KEY, true);

    List<StructuralModelItem> structuralModelItems = new ArrayList<>();
    for (LogField field : LogField.values()) {
      structuralModelItems.add(new StructuralModelItem(field.getName(), true, 100));
    }
    StructuralModel structuralModel = new StructuralModel(structuralModelItems);
    store.setDefault(JlvConstants.STRUCTURAL_UI_PREF_KEY,
        StructuralModelConverter.toString(structuralModel));

    List<PresentationalModelItem> presentationalModelItems = new ArrayList<>();
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.DEBUG.name(), new Rgb(0, 0, 0), new Rgb(255, 255, 255)));
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.INFO.name(), new Rgb(0, 255, 0), new Rgb(255, 255, 255)));
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.WARN.name(), new Rgb(255, 128, 0), new Rgb(255, 255, 255)));
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.ERROR.name(), new Rgb(255, 0, 0), new Rgb(255, 255, 255)));
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.FATAL.name(), new Rgb(165, 42, 42), new Rgb(255, 255, 255)));
    presentationalModelItems.add(new PresentationalModelItem(
        LogLevel.OTHER.name(), new Rgb(51, 102, 255), new Rgb(255, 255, 255)));
    PresentationalModel presentationalModel = new PresentationalModel(true, 11, presentationalModelItems);
    store.setDefault(JlvConstants.PRESENTATIONAL_UI_PREF_KEY,
        PresentationalModelConverter.toString(presentationalModel));
  }
}
