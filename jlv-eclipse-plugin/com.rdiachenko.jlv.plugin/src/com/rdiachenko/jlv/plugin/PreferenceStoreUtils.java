package com.rdiachenko.jlv.plugin;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.plugin.preference.PresentationalModel;
import com.rdiachenko.jlv.plugin.preference.PresentationalModelConverter;

public final class PreferenceStoreUtils {

    private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();

    private PreferenceStoreUtils() {
        // Utility class
    }
    
    public static boolean getBoolean(String key) {
        boolean value = STORE.getBoolean(key);

        if (STORE.isDefault(key)) {
            value = STORE.getDefaultBoolean(key);
        }
        return value;
    }

    public static int getInt(String key) {
        int value = STORE.getInt(key);

        if (STORE.isDefault(key)) {
            value = STORE.getDefaultInt(key);
        }
        return value;
    }

    public static String getString(String key) {
        String value = STORE.getString(key);

        if (STORE.isDefault(key)) {
            value = STORE.getDefaultString(key);
        }
        return value;
    }

    public static PresentationalModel getDefaultPresentationalModel() {
        String value = STORE.getDefaultString(JlvConstants.PRESENTATIONAL_UI_PREF_KEY);
        PresentationalModel model = PresentationalModelConverter.toModel(value);
        return model;
    }

    public static PresentationalModel getPresentationalModel() {
        String value = getString(JlvConstants.PRESENTATIONAL_UI_PREF_KEY);
        PresentationalModel model = PresentationalModelConverter.toModel(value);
        return model;
    }
    
    public static void setPresentationalModel(PresentationalModel model) {
        String value = PresentationalModelConverter.toString(model);
        STORE.setValue(JlvConstants.PRESENTATIONAL_UI_PREF_KEY, value);
    }
}
