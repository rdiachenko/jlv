package com.rdiachenko.jlv.ui.views;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rdiachenko.jlv.log4j.domain.Log;
import com.rdiachenko.jlv.log4j.domain.LogFieldName;
import com.rdiachenko.jlv.ui.ConstantUiIds;

public class JlvDetailedView extends ViewPart implements ISelectionListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private Text detailedLogArea;

	@Override
	public void createPartControl(Composite parent) {
		getSite().getPage().addSelectionListener(ConstantUiIds.JLV_MAIN_VIEW_ID, this);

		int style = SWT.MULTI | SWT.WRAP | SWT.V_SCROLL | SWT.BORDER;
		detailedLogArea = new Text(parent, style);
		detailedLogArea.setEditable(false);
	}

	@Override
	public void setFocus() {
		// no code
	}

	@Override
	public void dispose() {
		getSite().getPage().removeSelectionListener(this);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		StructuredSelection logSelection = (StructuredSelection) selection;
		Log log = (Log) logSelection.getFirstElement();

		if (log != null) {
			StringBuilder builder = new StringBuilder();
			String separator = ": ";
			String lineSeparator = "\n";
			builder.append(LogFieldName.CATEGORY.getName().toUpperCase()).append(separator)
					.append(log.getCategoryName()).append(lineSeparator)
					.append(LogFieldName.CLASS.getName().toUpperCase()).append(separator).append(log.getClassName())
					.append(lineSeparator)
					.append(LogFieldName.DATE.getName().toUpperCase()).append(separator).append(log.getDate())
					.append(lineSeparator)
					.append(LogFieldName.FILE.getName().toUpperCase()).append(separator).append(log.getFileName())
					.append(lineSeparator)
					.append(LogFieldName.LOCATION_INFO.getName().toUpperCase()).append(separator)
					.append(log.getLocationInfo()).append(lineSeparator)
					.append(LogFieldName.LINE.getName().toUpperCase()).append(separator).append(log.getLineNumber())
					.append(lineSeparator)
					.append(LogFieldName.METHOD.getName().toUpperCase()).append(separator).append(log.getMethodName())
					.append(lineSeparator)
					.append(LogFieldName.LEVEL.getName().toUpperCase()).append(separator).append(log.getLevel())
					.append(lineSeparator)
					.append(LogFieldName.THREAD.getName().toUpperCase()).append(separator).append(log.getThreadName())
					.append(lineSeparator)
					.append(LogFieldName.MESSAGE.getName().toUpperCase()).append(separator).append(log.getMessage())
					.append(lineSeparator)
					.append(LogFieldName.THROWABLE.getName().toUpperCase()).append(separator)
					.append(log.getThrowable()).append(lineSeparator);

			detailedLogArea.setText(builder.toString());
			logger.info(builder.toString());
		}
	}

}
