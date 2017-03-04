package com.rdiachenko.jlv.plugin.view;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.CircularBuffer;
import com.rdiachenko.jlv.Log;
import com.rdiachenko.jlv.plugin.JlvConstants;
import com.rdiachenko.jlv.plugin.preference.StructuralModel;
import com.rdiachenko.jlv.plugin.preference.StructuralModelConverter;

public class LogListViewPreferenceChangeListener implements IPropertyChangeListener {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final LogListView view;

  public LogListViewPreferenceChangeListener(LogListView view) {
    this.view = view;
  }

  @Override
  public void propertyChange(PropertyChangeEvent event) {
    if (JlvConstants.QUICK_SEARCH_VISIBLE_PREF_KEY.equals(event.getProperty())) {
      boolean visible = (Boolean) event.getNewValue();
      view.setSearchFieldVisible(visible);
      logger.info("Quick search field visibility set to {}", visible);
    }

    if (JlvConstants.LOGLIST_BUFFER_SIZE_PREF_KEY.equals(event.getProperty())) {
      int bufferSize = (Integer) event.getNewValue();
      CircularBuffer<Log> newInput = new CircularBuffer<>(bufferSize);
      CircularBuffer<Log> oldInput = view.getLogListInput();

      synchronized (oldInput) {
        for (Log log : oldInput) {
          newInput.add(log);
        }
      }
      view.setLogListInput(newInput);
      logger.info("Log list buffer size set to {}", bufferSize);
    }

    if (JlvConstants.STRUCTURAL_UI_PREF_KEY.equals(event.getProperty())) {
      StructuralModel model = StructuralModelConverter.toModel((String) event.getNewValue());
      view.updateLogListViewerColumns(model.getModelItems());
    }

    if (JlvConstants.LOGLIST_REFRESH_TIME_MS_PREF_KEY.equals(event.getProperty())) {
      view.getController().stopViewRefresher();
      view.getController().startViewRefresher();
    }
  }
}
