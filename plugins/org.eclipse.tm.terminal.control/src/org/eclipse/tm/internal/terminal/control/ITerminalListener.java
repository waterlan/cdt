/*******************************************************************************
 * Copyright (c) 2006, 2015 Wind River Systems, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Michael Scharf (Wind River) - initial API and implementation
 * Martin Oberhuber (Wind River) - fixed copyright headers and beautified
 *******************************************************************************/
package org.eclipse.tm.internal.terminal.control;

import org.eclipse.tm.internal.terminal.provisional.api.TerminalState;

/**
 * Provided by a view implementation.
 * @author Michael Scharf
 *
 */
public interface ITerminalListener {
	/**
	 * Called when the state of the connection has changed.
	 * @param state
	 */
	void setState(TerminalState state);

	/**
	 * Set the title of the terminal.
	 * @param title
	 */
	void setTerminalTitle(String title);

	/**
	 * selection has been changed internally e.g. select all
	 * clients might want to react on that
	 * NOTE: this does not include mouse selections
	 * those are handled in separate MouseListeners
	 * TODO should be unified
	 * 
	 * @since 4.1
	 */
	void setTerminalSelectionChanged();
}
