package com.github.rd.jlv.eclipse.ui.preferences.additional;

import java.util.Collections;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;

import com.github.rd.jlv.eclipse.ImageType;
import com.github.rd.jlv.eclipse.JlvActivator;
import com.github.rd.jlv.eclipse.ResourceManager;
import com.github.rd.jlv.eclipse.ui.preferences.PreferenceManager;
import com.github.rd.jlv.prefs.Converter;
import com.github.rd.jlv.prefs.PreferenceEnum;
import com.github.rd.jlv.prefs.StructuralModel;
import com.github.rd.jlv.prefs.StructuralModel.ModelItem;

public class StructuralTableEditor extends FieldEditor {

	private static final String DISPLAY_COLUMN_HEADER = "Display";
	private static final String NAME_COLUMN_HEADER = "Name";
	private static final String WIDTH_COLUMN_HEADER = "Width";
	private static final String[] COLUMN_NAMES = {
			DISPLAY_COLUMN_HEADER,
			NAME_COLUMN_HEADER,
			WIDTH_COLUMN_HEADER
	};

	private static final int DISPLAY_COLUMN_WIDTH = 70;
	private static final int NAME_COLUMN_WIDTH = 120;
	private static final int WIDTH_COLUMN_WIDTH = 120;
	private static final int[] COLUMN_WIDTHS = { DISPLAY_COLUMN_WIDTH, NAME_COLUMN_WIDTH, WIDTH_COLUMN_WIDTH };

	private static final PreferenceEnum TYPE = PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS;
	private static final Converter CONVERTER = Converter.get(TYPE);

	private Composite topComposite;

	private TableViewer tableViewer;

	private Button upButton;

	private Button downButton;

	private PreferenceManager preferenceManager;

	private StructuralModel model;

	public StructuralTableEditor(String name, Composite parent) {
		init(name, "");
		preferenceManager = JlvActivator.getDefault().getPreferenceManager();
		createControl(parent);
	}

	@Override
	public void adjustForNumColumns(int numColumns) {
		((GridData) topComposite.getLayoutData()).horizontalSpan = numColumns;
	}

	@Override
	public int getNumberOfControls() {
		return 2; // Table composite and Button box composite
	}

	@Override
	public void doFillIntoGrid(Composite parent, int numColumns) {
		topComposite = parent;

		tableViewer = getTableViewerControl(parent);
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableViewer.getControl().setLayoutData(gridData);

		Composite buttonBox = getButtonBox(parent);
		gridData = new GridData();
		gridData.verticalAlignment = GridData.BEGINNING;
		buttonBox.setLayoutData(gridData);
	}

	@Override
	public void doLoad() {
		model = (StructuralModel) CONVERTER.jsonToModel(getPreferenceStore().getString(TYPE.getName()));
		init();
	}

	@Override
	public void doLoadDefault() {
		model = (StructuralModel) CONVERTER.getDefaultModel();
		init();
	}

	@Override
	public void doStore() {
		preferenceManager.setValue(PreferenceEnum.LOG_LIST_STRUCTURAL_TABLE_SETTINGS, model);
	}

	private void init() {
		tableViewer.setInput(model);
		tableViewer.refresh();
	}

	private TableViewer getTableViewerControl(Composite parent) {
		tableViewer = new TableViewer(parent, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION);
		tableViewer.setUseHashlookup(true);
		final Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		table.setHeaderVisible(true);
		table.addSelectionListener(createSelectionListener());
		createTableColumns(tableViewer);
		tableViewer.setContentProvider(new ModelContentProvider());
		return tableViewer;
	}

	private void createTableColumns(TableViewer tableViewer) {
		for (int i = 0; i < COLUMN_NAMES.length; i++) {
			TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.LEAD);
			viewerColumn.getColumn().setText(COLUMN_NAMES[i]);
			viewerColumn.getColumn().setWidth(COLUMN_WIDTHS[i]);

			switch (COLUMN_NAMES[i]) {
			case DISPLAY_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new DisplayColumnLabelProvider());
				viewerColumn.setEditingSupport(new DisplayCellEditor(tableViewer));
				break;
			case NAME_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						ModelItem modelItem = (ModelItem) element;
						return modelItem.getName();
					}
				});
				break;
			case WIDTH_COLUMN_HEADER:
				viewerColumn.setLabelProvider(new ColumnLabelProvider() {
					@Override
					public String getText(Object element) {
						ModelItem modelItem = (ModelItem) element;
						return String.valueOf(modelItem.getWidth());
					}
				});
				viewerColumn.setEditingSupport(new WidthCellEditor(tableViewer));
				break;
			default:
				throw new IllegalArgumentException("No column with such a name: " + COLUMN_NAMES[i]
						+ ". Only [Name, Width, Display] are allowed.");
			}
		}
	}

	private SelectionListener createSelectionListener() {
		return new SelectionAdapter() {
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
	}

	private Composite getButtonBox(Composite parent) {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		Composite buttonBox = new Composite(parent, SWT.NULL);
		buttonBox.setLayout(layout);
		createButtons(buttonBox);
		selectionChanged();
		return buttonBox;
	}

	private void createButtons(Composite box) {
		upButton = createPushButton(box, "Up");
		downButton = createPushButton(box, "Down");
	}

	private Button createPushButton(Composite parent, String name) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(name);
		button.addSelectionListener(createSelectionListener());
		GridData gridData = new GridData(SWT.NONE);
		gridData.widthHint = 95;
		button.setLayoutData(gridData);
		return button;
	}

	private void selectionChanged() {
		int index = tableViewer.getTable().getSelectionIndex();
		int size = tableViewer.getTable().getItemCount();

		upButton.setEnabled(size > 1 && index > 0);
		downButton.setEnabled(size > 1 && index >= 0 && index < size - 1);
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
			Collections.swap(model.getModelItems(), index, target);
			tableViewer.refresh();
			tableViewer.getTable().setSelection(target);
		}
		selectionChanged();
	}

	private static class ModelContentProvider implements IStructuredContentProvider {

		@Override
		public void dispose() {
			// no code
		}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// no code
		}

		@Override
		public Object[] getElements(Object inputElement) {
			StructuralModel model = (StructuralModel) inputElement;
			return model.getModelItems().toArray();
		}
	}

	private static class DisplayColumnLabelProvider extends OwnerDrawLabelProvider {

		private final ResourceManager resourceManager = JlvActivator.getDefault().getResourceManager();

		@Override
		protected void measure(Event event, Object element) {
			// no code
		}

		@Override
		protected void paint(Event event, Object element) {
			TableItem item = (TableItem) event.item;
			ModelItem modelItem = (ModelItem) item.getData();
			Image image = (modelItem.isDisplay()) ? resourceManager.getImage(ImageType.CHECKBOX_CHECKED_ICON)
					: resourceManager.getImage(ImageType.CHECKBOX_UNCHECKED_ICON);
			Rectangle bounds = item.getBounds(event.index);
			Rectangle imageBounds = image.getBounds();
			int xOffset = bounds.width / 2 - imageBounds.width / 2;
			int yOffset = bounds.height / 2 - imageBounds.height / 2;
			int x = xOffset > 0 ? bounds.x + xOffset : bounds.x;
			int y = yOffset > 0 ? bounds.y + yOffset : bounds.y;
			event.gc.drawImage(image, x, y);
		}
	}

	private static class WidthCellEditor extends EditingSupport {

		private TableViewer viewer;

		private final CellEditor editor;

		public WidthCellEditor(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
			editor = new TextCellEditor(viewer.getTable());
			((Text) editor.getControl()).addVerifyListener(new VerifyListener() {
				@Override
				public void verifyText(final VerifyEvent e) {
					e.doit = e.text.matches("[\\d]*");
				}
			});
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			return String.valueOf(((ModelItem) element).getWidth());
		}

		@Override
		protected void setValue(Object element, Object value) {
			int width = Integer.parseInt(String.valueOf(value));
			((ModelItem) element).setWidth(width);
			viewer.update(element, null);
		}
	}

	private static class DisplayCellEditor extends EditingSupport {

		private final TableViewer viewer;

		private final CellEditor editor;

		public DisplayCellEditor(TableViewer viewer) {
			super(viewer);
			this.viewer = viewer;
			editor = new CheckboxCellEditor(viewer.getTable(), SWT.CHECK | SWT.CENTER);
		}

		@Override
		protected CellEditor getCellEditor(final Object element) {
			return editor;
		}

		@Override
		protected boolean canEdit(final Object element) {
			return true;
		}

		@Override
		protected Object getValue(final Object element) {
			return ((ModelItem) element).isDisplay();
		}

		@Override
		protected void setValue(final Object element, final Object value) {
			((ModelItem) element).setDisplay((Boolean) value);
			viewer.update(element, null);
		}
	}
}
