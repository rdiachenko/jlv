package com.github.rd.jlv.eclipse.ui.preferences;

import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.github.rd.jlv.props.JlvProperties;

public abstract class FieldEditor {

    /** Gap between label and control. */
    protected static final int HORIZONTAL_GAP = 8;
    
    /** The page containing this field editor */
    private DialogPage page;
    
    /** The preference store */
	private JlvProperties store;
	
    protected abstract void fillIntoGrid(Composite parent);
    
    protected abstract int getGridColumnsNumber();
    
    protected abstract void load();
    
    protected abstract void loadDefault();

    protected abstract void save();
	
	public void setPage(DialogPage page) {
		this.page = page;
	}
	
	public void setStore(JlvProperties store) {
		this.store = store;
	}
	
	public JlvProperties getStore() {
		return store;
	}
	
	public void clearErrorMessage() {
		if (page != null) {
			page.setErrorMessage(null);
		}
	}
	
	public void showErrorMessage(String message) {
		if (page != null) {
			page.setErrorMessage(message);
		}
	}
	
	public void createControl(Composite parent) {
        GridLayout layout = new GridLayout();
        layout.numColumns = getGridColumnsNumber();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        layout.horizontalSpacing = HORIZONTAL_GAP;
        parent.setLayout(layout);
        fillIntoGrid(parent);
    }
}
