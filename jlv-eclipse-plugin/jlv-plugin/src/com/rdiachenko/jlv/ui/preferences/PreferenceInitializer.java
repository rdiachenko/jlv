package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JlvActivator.getDefault().getPreferenceStore();
		store.setDefault(PreferenceManager.JLV_SERVER_PORT_NUMBER, 4445);
		store.setDefault(PreferenceManager.JLV_SERVER_AUTO_START, true);
	}
}
