/*******************************************************************************
 * Copyright (c) 2009 Alena Laskavaia 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Alena Laskavaia  - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.codan.core.model;

import java.util.Iterator;

/**
 * Problem parameter usually key=value settings that allows to alter checker
 * behaviour for given problem. For example if checker finds violation of naming
 * conventions for function, parameter would be the pattern of allowed names.
 * ProblemParameterInfo represent parameter meta-info for the ui. If more that
 * one parameter required ParameterInfo should describe hash or array of
 * parameters. This is only needed for auto-generated ui for parameter editing.
 * For complex case custom ui control should be used
 * 
 * @noextend This interface is not intended to be extended by clients.
 * @noimplement This interface is not intended to be implemented by clients.
 */
public interface IProblemParameterInfo {
	enum ParameterTypes {
		TYPE_STRING("string"), //$NON-NLS-1$
		TYPE_INTEGER("integer"), //$NON-NLS-1$
		TYPE_BOOLEAN("boolean"), //$NON-NLS-1$
		TYPE_FILE("file"), //$NON-NLS-1$
		TYPE_LIST("list"), //$NON-NLS-1$
		TYPE_HASH("hash"); //$NON-NLS-1$
		private String literal;

		private ParameterTypes(String literal) {
			this.literal = literal;
		}

		public static ParameterTypes valueOfLiteral(String name) {
			ParameterTypes[] values = values();
			for (int i = 0; i < values.length; i++) {
				ParameterTypes e = values[i];
				if (e.literal.equals(name))
					return e;
			}
			return null;
		}

		@Override
		public String toString() {
			return literal;
		}
	}

	String getKey();

	/**
	 * type of the parameter, supports boolean, integer, string, file, list and
	 * hash. If list is the value - it is an array - subparameter can be
	 * accessed by number, if hash is the value - it is a hash - subparameter
	 * can be accesses by name
	 * 
	 * @return string value of the type
	 */
	ParameterTypes getType();

	/**
	 * Additional info on how it is represented in the ui, for example boolean
	 * can be represented as checkbox, drop-down and so on, Values TBD
	 * 
	 * @return ui info or null if not set
	 */
	String getUiInfo();

	/**
	 * User visible label for the parameter control in UI
	 * 
	 * @return the label
	 */
	String getLabel();

	/**
	 * Detailed explanation of parameter
	 * 
	 * @return the tooltip text
	 */
	String getToolTip();

	/**
	 * Available if type is list or hash. Returns value of subparamer with the
	 * name of key. For the "list" type key is the number (index).
	 * 
	 * @param key
	 *            - name of the subparameter.
	 * @return
	 */
	IProblemParameterInfo getElement(String key);

	/**
	 * Available if type is list or hash. Returns iterator over parameter values
	 * for list and hash.
	 * 
	 * @return
	 */
	Iterator<IProblemParameterInfo> getIterator();
}
