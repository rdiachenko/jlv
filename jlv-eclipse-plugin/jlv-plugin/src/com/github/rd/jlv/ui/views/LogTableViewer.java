package com.github.rd.jlv.ui.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.github.rd.jlv.log4j.LogConstants;

public class LogTableViewer extends TableViewer {

	public LogTableViewer(Composite parent, int style) {
		super(parent, style);
		Table table = getTable();
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		table.setLayoutData(gridData);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		createColumns();
		setUseHashlookup(true);
		setContentProvider(new LogListContentProvider());
	}

	private void createColumns() {
		String[] headers = { LogConstants.LEVEL_FIELD_NAME,
				LogConstants.CATEGORY_FIELD_NAME,
				LogConstants.MESSAGE_FIELD_NAME,
				LogConstants.LINE_FIELD_NAME,
				LogConstants.DATE_FIELD_NAME,
				LogConstants.THROWABLE_FIELD_NAME,
		};
		int[] bounds = { 55, 100, 100, 100, 100, 100 };

		for (int i = 0; i < headers.length; i++) {
			TableViewerColumn column = new TableViewerColumn(this, SWT.NONE);
			column.getColumn().setText(headers[i]);
			column.getColumn().setWidth(bounds[i]);
			column.getColumn().setResizable(true);
			column.getColumn().setMoveable(false);

			switch (headers[i]) {
			case LogConstants.LEVEL_FIELD_NAME:
				column.setLabelProvider(new LevelColumnLabelProvider(this.getTable()));
				break;
			case LogConstants.MESSAGE_FIELD_NAME:
			case LogConstants.THROWABLE_FIELD_NAME:
				column.setLabelProvider(new TextColumnLabelProvider(this.getTable(), headers[i]));
				break;
			default:
				column.setLabelProvider(new DefaultColumnLabelProvider(this.getTable(), headers[i]));
			}
		}
	}
}
