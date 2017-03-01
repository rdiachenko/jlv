package com.rdiachenko.jlv.plugin.view;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.plugin.LogField;
import com.rdiachenko.jlv.plugin.PreferenceStoreUtils;
import com.rdiachenko.jlv.plugin.preference.StructuralModel;

public class ColumnResizeListener implements ControlListener, Runnable {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final int INITIAL_EXECUTION_DELAY_MS = 1000;
    
    private AtomicLong lastEventTimeMs = new AtomicLong(0);
    private AtomicReference<ControlEvent> event = new AtomicReference<>();
    
    @Override
    public void controlMoved(ControlEvent e) {
        // nothing to do
    }
    
    @Override
    public void controlResized(ControlEvent e) {
        if (e.getSource() instanceof TableColumn) {
            event.set(e);
            lastEventTimeMs.set(System.currentTimeMillis());
            Display.getDefault().timerExec(INITIAL_EXECUTION_DELAY_MS, this);
        }
    }
    
    @Override
    public void run() {
        if ((lastEventTimeMs.get() + INITIAL_EXECUTION_DELAY_MS) < System.currentTimeMillis()) {
            TableColumn column = (TableColumn) event.get().getSource();

            if (column.getWidth() > 0) {
                StructuralModel model = PreferenceStoreUtils.getStructuralModel();
                model.getModelItem(LogField.valueOf(column.getText().toUpperCase())).setWidth(column.getWidth());
                PreferenceStoreUtils.setStructuralModel(model);
                logger.info("Width for column {} updated to {}", column.getText(), column.getWidth());
            }
        } else {
            Display.getDefault().timerExec(INITIAL_EXECUTION_DELAY_MS, this);
        }
    }
}
