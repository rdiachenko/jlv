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
import com.github.rd.jlv.props.EventScope;
import com.github.rd.jlv.props.LoglistColumn;
import com.github.rd.jlv.props.PropertyKey;

public class LoglistColumnFieldEditor extends FieldEditor {

	private static final String NAME_COLUMN_HEADER = "Name";
	private static final String VISIBLE_COLUMN_HEADER = "Visible";
	private static final String WIDTH_COLUMN_HEADER = "Width";
	private static final String[] COLUMN_HEADERS = {
			NAME_COLUMN_HEADER,
			VISIBLE_COLUMN_HEADER,
			WIDTH_COLUMN_HEADER
	};

	private static final int NAME_COLUMN_WIDTH = 120;
	private static final int VISIBLE_COLUMN_WIDTH = 70;
	private static final int WIDTH_COLUMN_WIDTH = 120;
	private static final int[] COLUMN_WIDTHS = { NAME_COLUMN_WIDTH, VISIBLE_COLUMN_WIDTH, WIDTH_COLUMN_WIDTH };

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
		createTableViewer(parent);
		createButtonBox(parent);
	}

	@Override
	protected int getGridColumnsNumber() {
		return 2; // Table composite and Button box composite
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
		List<LoglistColumn> oldValue = getStore().load(key);
		getStore().save(key, value);
		getStore().firePropertyChangeEvent(key, oldValue, value, EventScope.APPLICATION);
	}

	private void createTableViewer(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginLeft = 5;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(layout);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		composite.setLayoutData(gridData);

		tableViewer = new TableViewer(composite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		tableViewer.setUseHashlookup(true);
		Table table = tableViewer.getTable();
		table.setLayoutData(gridData);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(selectionListener);
		createTableColumns(tableViewer);
		tableViewer.setContentProvider(new ArrayContentProvider());
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_HEADERS.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_HEADERS[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_HEADERS[i]) {
			case NAME_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						LoglistColumn column = (LoglistColumn) element;
						return column.getName();
					}
				});
				break;
			case VISIBLE_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new VisibleColumnLabelProvider());
				viewerColumn.setEditingSupport(new VisibleColumnCellEditor(tableViewer));
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
				throw new IllegalArgumentException("No column with such a name: " + COLUMN_HEADERS[i]);
			}
		}
	}

	private void createButtonBox(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginRight = 5;
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(layout);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		composite.setLayoutData(gridData);

		upButton = createButton(composite, "Up");
		downButton = createButton(composite, "Down");
		selectionChanged();
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
