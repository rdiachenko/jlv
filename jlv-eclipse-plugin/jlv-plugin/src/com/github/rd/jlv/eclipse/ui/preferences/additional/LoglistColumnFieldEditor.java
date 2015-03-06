package com.github.rd.jlv.eclipse.ui.preferences.additional;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Widget;

import com.github.rd.jlv.eclipse.ui.preferences.FieldEditor;
import com.github.rd.jlv.props.LoglistColumn;
import com.github.rd.jlv.props.PropertyKey;

public class LoglistColumnFieldEditor extends FieldEditor {

	private static final String DISPLAY_COLUMN_HEADER = "Display";
	private static final String NAME_COLUMN_HEADER = "Name";
	private static final String WIDTH_COLUMN_HEADER = "Width";
	private static final String[] COLUMN_HEADERS = {
			DISPLAY_COLUMN_HEADER,
			NAME_COLUMN_HEADER,
			WIDTH_COLUMN_HEADER
	};

	private static final int DISPLAY_COLUMN_WIDTH = 70;
	private static final int NAME_COLUMN_WIDTH = 120;
	private static final int WIDTH_COLUMN_WIDTH = 120;
	private static final int[] COLUMN_WIDTHS = { DISPLAY_COLUMN_WIDTH, NAME_COLUMN_WIDTH, WIDTH_COLUMN_WIDTH };

	private TableViewer tableViewer;
	private Button upButton;
	private Button downButton;
	private SelectionListener selectionListener;
	private List<LoglistColumn> value;
	private PropertyKey key;

	public LoglistColumnFieldEditor(PropertyKey key, Composite parent) {
		this.key = key;
		selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				Widget widget = event.widget;

				if (widget == upButton) {
					upPressed();
				} else if (widget == downButton) {
					downPressed();
				} else if (widget == tableViewer.getControl()) {
					selectionChanged();
				}
			}
		};
		createControl(parent);
	}

	@Override
	protected void fillIntoGrid(Composite parent) {
		tableViewer = createTableViewerControl(parent);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);

		Composite buttonBox = createButtonBox(parent);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		buttonBox.setLayoutData(gridData);
	}

	@Override
	protected int getGridColumnsNumber() {
		return 2; // Table composite and Button box composite
	}

	@Override
	protected void load() {
		value = getStore().load(key);
		tableViewer.setInput(value);
		tableViewer.refresh();
	}

	@Override
	protected void loadDefault() {
		value = getStore().loadDefault(key);
		tableViewer.setInput(value);
		tableViewer.refresh();
	}

	@Override
	protected void save() {
		getStore().save(key, value);
	}

	private TableViewer createTableViewerControl(Composite parent) {
		TableViewer tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		tableViewer.setUseHashlookup(true);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(selectionListener);
		createTableColumns(tableViewer);
		tableViewer.setContentProvider(new ArrayContentProvider());
		return tableViewer;
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_HEADERS.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_HEADERS[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_HEADERS[i]) {
			case DISPLAY_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new DisplayColumnLabelProvider());
				viewerColumn.setEditingSupport(new DisplayColumnCellEditor(tableViewer));
				break;
			case NAME_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						LoglistColumn column = (LoglistColumn) element;
						return column.getName();
					}
				});
				break;
			case WIDTH_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						LoglistColumn column = (LoglistColumn) element;
						return String.valueOf(column.getWidth());
					}
				});
				viewerColumn.setEditingSupport(new WidthColumnCellEditor(tableViewer));
				break;
			default:
				throw new IllegalArgumentException("No column with such a name: " + COLUMN_HEADERS[i]
						+ ". Only [Name, Width, Display] are allowed.");
			}
		}
	}

	private Composite createButtonBox(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		Composite buttonBox = new Composite(parent, SWT.NULL);
		buttonBox.setLayout(layout);

		upButton = createButton(buttonBox, "Up");
		downButton = createButton(buttonBox, "Down");

		selectionChanged();
		return buttonBox;
	}

	private Button createButton(Composite parent, String name) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(name);
		button.addSelectionListener(selectionListener);

		GridData gridData = new GridData(SWT.NONE);
		gridData.widthHint = 95;
		button.setLayoutData(gridData);
		return button;
	}

	private void upPressed() {
		swap(true);
	}

	private void downPressed() {
		swap(false);
	}

	private void swap(boolean up) {
		int index = tableViewer.getTable().getSelectionIndex();
		int target = up ? index - 1 : index + 1;

		if (index >= 0) {
			Collections.swap(value, index, target);
			tableViewer.refresh();
			tableViewer.getTable().setSelection(target);
		}
		selectionChanged();
	}

	private void selectionChanged() {
		int index = tableViewer.getTable().getSelectionIndex();
		int size = tableViewer.getTable().getItemCount();

		upButton.setEnabled(size > 1 && index > 0);
		downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
	}
}
