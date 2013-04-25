package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.model.LogField;

public class JlvView extends ViewPart {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void createPartControl(Composite parent) {
		TableViewer viewer = createViewer(parent);
	}

	@Override
	public void setFocus() {

	}

	private TableViewer createViewer(Composite parent) {
		int style = SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION | SWT.HIDE_SELECTION;
		TableViewer viewer = new TableViewer(parent, style);
		Table table = viewer.getTable();

        GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        table.setLayoutData(gridData);
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        createColumns(table);

        viewer.setUseHashlookup(true);
        viewer.setContentProvider(new JlvViewContentProvider());
        viewer.setLabelProvider(new JlvViewLabelProvider());
//        viewer.setInput(input);

        return viewer;
	}

	private void createColumns(Table table) {
		for (LogField logField : LogField.values()) {
            TableColumn column = new TableColumn(table, SWT.NONE);
            column.setText(logField.getName());
            column.setWidth(100);
        }
	}
}
