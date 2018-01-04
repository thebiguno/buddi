/* 
 * Copyright 2005 Paul Hinds
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tp23.antinstaller.renderer.swing;

import java.awt.Dimension;
/**
 * Extracted contants with aim of making these variables configurable
 * @author teknopaul
 */
public class SizeConstants {

	// Page size constants
	public static int PAGE_WIDTH = 472; // orig value 472
	public static int PAGE_HEIGHT = 400;// orig value 400
	public static int LABEL_WIDTH = 175;// orig value 175
	
	public static int TITLE_IMAGE_HEIGHT = 100;
	public static int TITLE_PANEL_HEIGHT = 75; // suspect this is irrelevant

	// Field size constants
	public static int LEFT_INDENT = 15;
	public static int TOP_INDENT = 8;
	public static int BUTTON_WIDTH = 90; // file and directory chooser
	public static int FIELD_HEIGHT = 20;// field Y
	public static int FIELD_WIDTH = SizeConstants.PAGE_WIDTH - SizeConstants.LABEL_WIDTH - SizeConstants.LEFT_INDENT;
	public static int SHORT_FIELD_WIDTH = FIELD_WIDTH - SizeConstants.BUTTON_WIDTH;
	// overflow resizing
	// in the gridbaglayout is seems that only preferred size
	// is significant
	private static int OVERFLOW_REDUCTION = 40;
	public static Dimension OVERFLOW_FIELD_SIZE = new Dimension(FIELD_WIDTH - OVERFLOW_REDUCTION, FIELD_HEIGHT);
	public static Dimension OVERFLOW_SHORT_FIELD_SIZE = new Dimension(FIELD_WIDTH - SizeConstants.BUTTON_WIDTH - OVERFLOW_REDUCTION, FIELD_HEIGHT);
	public static Dimension OVERFLOW_TOTAL_SIZE = new Dimension(FIELD_WIDTH + SizeConstants.LABEL_WIDTH - OVERFLOW_REDUCTION, FIELD_HEIGHT);

}
