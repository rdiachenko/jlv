package com.rdiachenko.jlv.plugin;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SourceProvider extends AbstractSourceProvider {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  public static final String JLV_SERVER_STARTED_VAR_NAME = "jlvServerStarted";

  private boolean serverStarted;

  public SourceProvider() {
    serverStarted = PreferenceStoreUtils.getBoolean(JlvConstants.SERVER_AUTO_START_PREF_KEY);
    logger.info("serverStarted source variable initialized to {}", serverStarted);
  }

  public void setServerStarted(boolean serverStarted) {
    this.serverStarted = serverStarted;
    fireSourceChanged(ISources.ACTIVE_PART_ID, JLV_SERVER_STARTED_VAR_NAME, serverStarted);
  }

  @Override
  public void dispose() {
    // nothing to dispose
  }

  @Override
  public Map<String, Boolean> getCurrentState() {
    Map<String, Boolean> state = new HashMap<>();
    state.put(JLV_SERVER_STARTED_VAR_NAME, serverStarted);
    return state;
  }

  @Override
  public String[] getProvidedSourceNames() {
    return new String[] { JLV_SERVER_STARTED_VAR_NAME };
  }
}
