/***********************************************************************
 * Copyright (c) 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v0.5 
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors:
 * QNX Software Systems - Initial API and implementation
***********************************************************************/
package org.eclipse.cdt.ui.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.cdt.core.CCorePlugin;
import org.eclipse.cdt.core.ErrorParserManager;
import org.eclipse.cdt.internal.ui.wizards.dialogfields.CheckedListDialogField;
import org.eclipse.cdt.internal.ui.wizards.dialogfields.DialogField;
import org.eclipse.cdt.internal.ui.wizards.dialogfields.IDialogFieldListener;
import org.eclipse.cdt.internal.ui.wizards.dialogfields.LayoutUtil;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

public abstract class ErrorParserBlock extends AbstractCOptionPage {

	private static final String PREFIX = "ErrorParserBlock"; // $NON-NLS-1$
	private static final String LABEL = PREFIX + ".label"; // $NON-NLS-1$
	private static final String DESC = PREFIX + ".desc"; // $NON-NLS-1$

	private static String[] EMPTY = new String[0];
	private Preferences fPrefs;
	private HashMap mapParsers = new HashMap();
	private CheckedListDialogField fErrorParserList;
	protected boolean listDirty = false;

	class FieldListenerAdapter implements  IDialogFieldListener {

		/* (non-Javadoc)
		 * @see org.eclipse.cdt.internal.ui.wizards.dialogfields.IDialogFieldListener#dialogFieldChanged(org.eclipse.cdt.internal.ui.wizards.dialogfields.DialogField)
		 */
		public void dialogFieldChanged(DialogField field) {
			listDirty = true;
		}

	}

	public ErrorParserBlock(Preferences prefs) {
		super(CUIPlugin.getResourceString(LABEL));
		setDescription(CUIPlugin.getResourceString(DESC));
		fPrefs = prefs;
	}

	public Image getImage() {
		return null;
	}

	/**
	 * Returns a label provider for the error parsers
	 *
	 * @return the content provider
	 */
	protected ILabelProvider getLabelProvider() {
		return new LabelProvider() {
			/* (non-Javadoc)
			 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
			 */
			public String getText(Object element) {
				String name = (String)mapParsers.get(element.toString());
				return name != null ? name : "";
			}
		};
	}

	protected FieldListenerAdapter getFieldListenerAdapter() {
		return new FieldListenerAdapter();
	}

	protected String[] getErrorParserIDs(Preferences prefs) {
		String parserIDs = prefs.getString(ErrorParserManager.PREF_ERROR_PARSER);
		String[] empty = new String[0];
		if (parserIDs != null && parserIDs.length() > 0) {
			StringTokenizer tok = new StringTokenizer(parserIDs, ";");
			List list = new ArrayList(tok.countTokens());
			while (tok.hasMoreElements()) {
				list.add(tok.nextToken());
			}
			return (String[]) list.toArray(empty);
		}
		return empty;
	}

	/**
	 * To be implemented, abstract method.
	 * @param project
	 * @param list
	 */
	protected abstract String[] getErrorParserIDs(IProject project);

	/**
	 * To be implemented. abstract method.
	 * @param project
	 * @param parsers
	 */
	protected abstract void saveErrorParsers(IProject project, String[] parserIDs) throws CoreException;

	protected void saveErrorParsers(Preferences prefs, String[] parserIDs) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < parserIDs.length; i++) {
			buf.append(parserIDs[i]).append(';');
		}
		prefs.setValue(ErrorParserManager.PREF_ERROR_PARSER, buf.toString());
	}

	protected void initMapParsers() {
		mapParsers.clear();
		IExtensionPoint point = CCorePlugin.getDefault().getDescriptor().getExtensionPoint(CCorePlugin.ERROR_PARSER_SIMPLE_ID);
		if (point != null) {
			IExtension[] exts = point.getExtensions();
			for (int i = 0; i < exts.length; i++) {
				mapParsers.put(exts[i].getUniqueIdentifier(), exts[i].getLabel());
			}
		}
	}

	protected void initializeValues() {
		initMapParsers();
		List list = new ArrayList(mapParsers.size());
		Iterator items =  mapParsers.keySet().iterator();
		while( items.hasNext()) {
			list.add((String)items.next());
		}
		fErrorParserList.setElements(list);

		list.clear();
		String[] parserIDs = EMPTY;

		IProject project = getContainer().getProject();
		if (project == null) {
			// From a Preference.
			parserIDs =getErrorParserIDs(fPrefs);
		} else {
			// From the Project.
			parserIDs = getErrorParserIDs(project);
		}

		fErrorParserList.setCheckedElements(Arrays.asList(parserIDs));
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);

		String[] buttonLabels = new String[] {
			/* 0 */
			"Up", //$NON-NLS-1$
			/* 1 */
			"Down", //$NON-NLS-1$
			/* 2 */
			null,
			/* 3 */
			"Select All", //$NON-NLS-1$
			/* 4 */
			"Unselect All" //$NON-NLS-1$
		};

		fErrorParserList = new CheckedListDialogField(null, buttonLabels, getLabelProvider());
		fErrorParserList.setDialogFieldListener(getFieldListenerAdapter());
		fErrorParserList.setLabelText("Error Parsers"); //$NON-NLS-1$
		fErrorParserList.setUpButtonIndex(0);
		fErrorParserList.setDownButtonIndex(1);
		fErrorParserList.setCheckAllButtonIndex(3);
		fErrorParserList.setUncheckAllButtonIndex(4);

		LayoutUtil.doDefaultLayout(composite, new DialogField[] { fErrorParserList }, true);
		LayoutUtil.setHorizontalGrabbing(fErrorParserList.getListControl(null));

		initializeValues();
		setControl(composite);
	}

	public void performApply(IProgressMonitor monitor) throws CoreException {
		if (listDirty) {
			IProject project = getContainer().getProject();
			if (monitor == null) {
				monitor = new NullProgressMonitor();
			}
			monitor.beginTask("Setting Error Parsers...", 1);
			List list = fErrorParserList.getCheckedElements();
			
			String[] parserIDs = (String[])list.toArray(EMPTY);

			if (project == null) {
				saveErrorParsers(fPrefs, parserIDs);
			} else {
				saveErrorParsers(project, parserIDs);
			}
			monitor.worked(1);
			monitor.done();
		}
	}

	public void performDefaults() {
		initializeValues();
	}
}
