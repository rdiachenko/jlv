package com.rdiachenko.jlv.plugin.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.plugin.JlvActivator;
import com.rdiachenko.jlv.plugin.JlvConstants;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
        store.setDefault(JlvConstants.SERVER_PORT_PREF_KEY, 4445);
        store.setDefault(JlvConstants.SERVER_AUTO_START_PREF_KEY, true);
        store.setDefault(JlvConstants.LOGLIST_BUFFER_SIZE_PREF_KEY, 10000);
        store.setDefault(JlvConstants.LOGLIST_REFRESH_TIME_MS_PREF_KEY, 500);
        store.setDefault(JlvConstants.QUICK_SEARCH_VISIBLE_PREF_KEY, true);
    }
}
