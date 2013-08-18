package com.rdiachenko.jlv.ui.views;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogFieldName;
import com.rdiachenko.jlv.ui.UiStringConstants;

public class JlvDetailedView extends ViewPart implements ISelectionListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Map<LogFieldName, Text> logFields = new HashMap<LogFieldName, Text>();

	@Override
	public void createPartControl(Composite parent) {
		getSite().getPage().addSelectionListener(UiStringConstants.JLV_MAIN_VIEW_ID, this);

		ExpandBar bar = new ExpandBar(parent, SWT.V_SCROLL);

		int multiLineStyle = SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER;
		buildExpandItem(bar, LogFieldName.MESSAGE, multiLineStyle);
		buildExpandItem(bar, LogFieldName.THROWABLE, multiLineStyle);

		int singleLineStyle = SWT.BORDER;
		buildExpandItem(bar, LogFieldName.LEVEL, singleLineStyle);
		buildExpandItem(bar, LogFieldName.LINE, singleLineStyle);
		buildExpandItem(bar, LogFieldName.DATE, singleLineStyle);
		buildExpandItem(bar, LogFieldName.CATEGORY, singleLineStyle);
		buildExpandItem(bar, LogFieldName.FILE, singleLineStyle);
		buildExpandItem(bar, LogFieldName.CLASS, singleLineStyle);
		buildExpandItem(bar, LogFieldName.METHOD, singleLineStyle);
		buildExpandItem(bar, LogFieldName.LOCATION_INFO, singleLineStyle);
		buildExpandItem(bar, LogFieldName.THREAD, singleLineStyle);
	}

	@Override
	public void setFocus() {
		// no code
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(UiStringConstants.JLV_MAIN_VIEW_ID, this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		StructuredSelection logSelection = (StructuredSelection) selection;
		Log log = (Log) logSelection.getFirstElement();

		if (log != null) {
			logFields.get(LogFieldName.CATEGORY).setText(log.getCategoryName());
			logFields.get(LogFieldName.CLASS).setText(log.getClassName());
			logFields.get(LogFieldName.DATE).setText(log.getDate());
			logFields.get(LogFieldName.FILE).setText(log.getFileName());
			logFields.get(LogFieldName.LOCATION_INFO).setText(log.getLocationInfo());
			logFields.get(LogFieldName.LINE).setText(log.getLineNumber());
			logFields.get(LogFieldName.METHOD).setText(log.getMethodName());
			logFields.get(LogFieldName.LEVEL).setText(log.getLevel());
			logFields.get(LogFieldName.THREAD).setText(log.getThreadName());
			logFields.get(LogFieldName.MESSAGE).setText(log.getMessage());
			logFields.get(LogFieldName.THROWABLE).setText(log.getThrowable());
		}
	}

	private void buildExpandItem(ExpandBar bar, LogFieldName logField, int logFieldStyle) {
		Composite composite = new Composite(bar, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		Text field = new Text(composite, logFieldStyle);
		field.setEditable(false);
		GridData fieldGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		int fieldLinesNumber = 1;

		if (logField == LogFieldName.MESSAGE) {
			fieldLinesNumber = 2;
		} else if (logField == LogFieldName.THROWABLE) {
			fieldLinesNumber = 10;
		}
		fieldGridData.heightHint = fieldLinesNumber * field.getLineHeight();
		field.setLayoutData(fieldGridData);
		logFields.put(logField, field);

		ExpandItem item = new ExpandItem(bar, SWT.NONE);
		item.setText(logField.getName().toUpperCase());
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);

		if (logField == LogFieldName.MESSAGE || logField == LogFieldName.THROWABLE) {
			item.setExpanded(true);
		}

//		Image image = display.getSystemImage(SWT.ICON_QUESTION);
//		item.setImage(image);
	}

}
