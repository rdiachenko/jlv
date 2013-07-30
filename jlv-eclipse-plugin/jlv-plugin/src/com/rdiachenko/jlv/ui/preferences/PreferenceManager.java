package com.rdiachenko.jlv.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.rdiachenko.jlv.JlvActivator;

public final class PreferenceManager {

	public static final String JLV_SERVER_PORT_NUMBER = "jlv.server.port.number";
	public static final String JLV_SERVER_AUTO_START = "jlv.server.auto.start";

	public static final String JLV_QUICK_SEARCH_FIELD_STATUS = "jlv.logview.main.quickSearchField.visible";

	private static final IPreferenceStore STORE = JlvActivator.getDefault().getPreferenceStore();

	private PreferenceManager() {
		throw new IllegalStateException("This is an util class. The object should not be created.");
	}

	public static int getServerPortNumber() {
		return STORE.getInt(JLV_SERVER_PORT_NUMBER);
	}

	public static boolean getServerAutoStart() {
		return STORE.getBoolean(JLV_SERVER_AUTO_START);
	}

	public static void setQuickSearchFieldStatus(boolean isVisible) {
		STORE.setValue(JLV_QUICK_SEARCH_FIELD_STATUS, isVisible);
	}

	public static boolean getQuickSearchFieldStatus() {
		return STORE.getBoolean(JLV_QUICK_SEARCH_FIELD_STATUS);
	}
}
