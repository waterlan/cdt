/*******************************************************************************
 * Copyright (c) 2019 Martin Weber.
 *
 * Content is provided to you under the terms and conditions of the Eclipse Public License Version 2.0 "EPL".
 * A copy of the EPL is available at http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/

package org.eclipse.cdt.cmake.is.hpenonstop;

import org.eclipse.cdt.cmake.is.core.Arglets;
import org.eclipse.cdt.cmake.is.core.DefaultToolCommandlineParser;
import org.eclipse.cdt.cmake.is.core.DefaultToolDetectionParticipant;
import org.eclipse.cdt.cmake.is.core.IArglet;
import org.eclipse.cdt.cmake.is.core.ResponseFileArglets;

/**
 * HPE NonStop c89 C & C++.
 *
 * @author Martin Weber
 */
public class HpeC89ToolDetectionParticipant extends DefaultToolDetectionParticipant {

	public HpeC89ToolDetectionParticipant() {
		super("c89", true, "exe", new ToolCommandlineParser());
	}

	private static class ToolCommandlineParser extends DefaultToolCommandlineParser {

		private static final IArglet[] arglets = { new Arglets.IncludePath_C_POSIX(), new Arglets.MacroDefine_C_POSIX(),
				new Arglets.MacroUndefine_C_POSIX() };

		private ToolCommandlineParser() {
			super(null, new ResponseFileArglets.At(), null, arglets);
		}
	}
}
