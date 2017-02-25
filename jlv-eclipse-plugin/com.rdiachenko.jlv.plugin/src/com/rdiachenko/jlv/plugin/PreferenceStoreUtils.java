package com.rdiachenko.jlv.plugin;

import org.eclipse.jface.preference.IPreferenceStore;

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
}
