package com.github.rd.jlv.ui.views;

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

import com.github.rd.jlv.StringConstants;
import com.github.rd.jlv.log4j.LogConstants;
import com.github.rd.jlv.log4j.domain.Log;

public class LogDetailsView extends ViewPart implements ISelectionListener {

	private Map<String, Text> logFields = new HashMap<String, Text>();

	@Override
	public void createPartControl(Composite parent) {
		getSite().getPage().addSelectionListener(StringConstants.JLV_LOG_LIST_VIEW_ID, this);

		ExpandBar bar = new ExpandBar(parent, SWT.V_SCROLL);

		int multiLineStyle = SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER;
		buildExpandItem(bar, LogConstants.MESSAGE, multiLineStyle);
		buildExpandItem(bar, LogConstants.THROWABLE, multiLineStyle);

		int singleLineStyle = SWT.BORDER;
		buildExpandItem(bar, LogConstants.LEVEL, singleLineStyle);
		buildExpandItem(bar, LogConstants.LINE, singleLineStyle);
		buildExpandItem(bar, LogConstants.DATE, singleLineStyle);
		buildExpandItem(bar, LogConstants.CATEGORY, singleLineStyle);
		buildExpandItem(bar, LogConstants.FILE, singleLineStyle);
		buildExpandItem(bar, LogConstants.CLASS, singleLineStyle);
		buildExpandItem(bar, LogConstants.METHOD, singleLineStyle);
		buildExpandItem(bar, LogConstants.LOCATION_INFO, singleLineStyle);
		buildExpandItem(bar, LogConstants.THREAD, singleLineStyle);
	}

	@Override
	public void setFocus() {
		// no code
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(StringConstants.JLV_LOG_LIST_VIEW_ID, this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		StructuredSelection logSelection = (StructuredSelection) selection;
		Log log = (Log) logSelection.getFirstElement();

		if (log != null) {
			logFields.get(LogConstants.CATEGORY).setText(log.getCategoryName());
			logFields.get(LogConstants.CLASS).setText(log.getClassName());
			logFields.get(LogConstants.DATE).setText(log.getDate());
			logFields.get(LogConstants.FILE).setText(log.getFileName());
			logFields.get(LogConstants.LOCATION_INFO).setText(log.getLocationInfo());
			logFields.get(LogConstants.LINE).setText(log.getLineNumber());
			logFields.get(LogConstants.METHOD).setText(log.getMethodName());
			logFields.get(LogConstants.LEVEL).setText(log.getLevel());
			logFields.get(LogConstants.THREAD).setText(log.getThreadName());
			logFields.get(LogConstants.MESSAGE).setText(log.getMessage());
			logFields.get(LogConstants.THROWABLE).setText(log.getThrowable());
		}
	}

	private void buildExpandItem(ExpandBar bar, String logFieldName, int logFieldStyle) {
		Composite composite = new Composite(bar, SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);

		Text field = new Text(composite, logFieldStyle);
		field.setEditable(false);
		GridData fieldGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		int fieldLinesNumber = 1;

		if (LogConstants.MESSAGE.equals(logFieldName) || LogConstants.THROWABLE.equals(logFieldName)) {
			fieldLinesNumber = 10;
		}
		fieldGridData.heightHint = fieldLinesNumber * field.getLineHeight();
		field.setLayoutData(fieldGridData);
		logFields.put(logFieldName, field);

		ExpandItem item = new ExpandItem(bar, SWT.NONE);
		item.setText(logFieldName.toUpperCase());
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);

		if (LogConstants.MESSAGE.equals(logFieldName) || LogConstants.THROWABLE.equals(logFieldName)) {
			item.setExpanded(true);
		}

//		Image image = display.getSystemImage(SWT.ICON_QUESTION);
//		item.setImage(image);
	}

}
