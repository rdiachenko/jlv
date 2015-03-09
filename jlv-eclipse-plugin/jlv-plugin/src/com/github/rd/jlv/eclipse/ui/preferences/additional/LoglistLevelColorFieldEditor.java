package com.github.rd.jlv.eclipse.ui.preferences.additional;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.eclipse.ui.preferences.FieldEditor;
import com.github.rd.jlv.props.EventScope;
import com.github.rd.jlv.props.LoglistLevelColor;
import com.github.rd.jlv.props.PropertyChangeEvent;
import com.github.rd.jlv.props.PropertyKey;
import com.google.common.eventbus.Subscribe;

public class LoglistLevelColorFieldEditor extends FieldEditor {

	private static final String LEVEL_COLUMN_HEADER = "Level";
	private static final String FOREGROUND_COLUMN_HEADER = "Foreground";
	private static final String BACKGROUND_COLUMN_HEADER = "Background";
	private static final String[] COLUMN_HEADERS = {
			LEVEL_COLUMN_HEADER,
			FOREGROUND_COLUMN_HEADER,
			BACKGROUND_COLUMN_HEADER
	};

	private static final int LEVEL_COLUMN_WIDTH = 60;
	private static final int FOREGROUND_COLUMN_WIDTH = 260;
	private static final int BACKGROUND_COLUMN_WIDTH = 260;
	private static final int[] COLUMN_WIDTHS = { LEVEL_COLUMN_WIDTH, FOREGROUND_COLUMN_WIDTH, BACKGROUND_COLUMN_WIDTH };

	private final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

	private TableViewer tableViewer;
	private PropertyChangeListener propertyChangeListener;
	private List<LoglistLevelColor> value;
	private PropertyKey key;

	public LoglistLevelColorFieldEditor(PropertyKey key, Composite parent) {
		this.key = key;
		propertyChangeListener = new PropertyChangeListener();
//		getStore().addPropertyChangeListener(propertyChangeListener);
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
		createTableViewer(parent);
	}

	@Override
	protected int getGridColumnsNumber() {
		return 1;
	}

	@Override
	protected void load() {
		value = getStore().load(key);
		tableViewer.setInput(value);
	}

	@Override
	protected void loadDefault() {
		value = getStore().loadDefault(key);
		tableViewer.setInput(value);
	}

	@Override
	protected void save() {
		List<LoglistLevelColor> oldValue = getStore().load(key);
		getStore().save(key, value);
		getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.APPLICATION);
	}

	@Override
	public void dispose() {
		getStore().removePropertyChangeListener(propertyChangeListener);
	}

	private void createTableViewer(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		tableViewer.setUseHashlookup(true);
		Table table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		createTableColumns(tableViewer);
		tableViewer.setContentProvider(new ArrayContentProvider());
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_HEADERS.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_HEADERS[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_HEADERS[i]) {
			case LEVEL_COLUMN_HEADER:
//				boolean levelAsImage = getStore().load(PropertyKey.LOGLIST_LEVEL_IMAGE_KEY);
				viewerColumn.setLabelProvider(getLevelColumnLabelProvider(true));
				break;
			case FOREGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelForegroundColumnLabelProvider());
				viewerColumn.setEditingSupport(new LevelForegroundColumnCellEditor(tableViewer));
				break;
			case BACKGROUND_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new LevelBackgroundColumnLabelProvider());
				viewerColumn.setEditingSupport(new LevelBackgroundColumnCellEditor(tableViewer));
				break;
			default:
				throw new IllegalArgumentException("No column with such a name: " + COLUMN_HEADERS[i]);
			}
		}
	}

	private CellLabelProvider getLevelColumnLabelProvider(boolean levelAsImage) {
		if (levelAsImage) {
			return new LevelImageColumnLabelProvider();
		} else {
			return new LevelTextColumnLabelProvider();
		}
	}

	private class PropertyChangeListener {

		@Subscribe
		public void propertyChange(PropertyChangeEvent event) {
			if (event.getScope() == EventScope.CONFIGURATION) {
				if (event.getProperty() == PropertyKey.LOGLIST_LEVEL_IMAGE_KEY) {
					boolean levelAsImage = (boolean) event.getNewValue();
					tableViewer.setLabelProvider(getLevelColumnLabelProvider(levelAsImage));
				} else if (event.getProperty() == PropertyKey.LOGLIST_FONT_SIZE_KEY) {
					int fontSize = (int) event.getNewValue();
					Font font = resourceManager.getFont(fontSize);

					for (TableItem item : tableViewer.getTable().getItems()) {
						item.setFont(font);
					}
				}
				tableViewer.refresh();
			}
		}
	}
}
