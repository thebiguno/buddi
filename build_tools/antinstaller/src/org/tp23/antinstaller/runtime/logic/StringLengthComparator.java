/*
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

package org.tp23.antinstaller.runtime.logic;

import java.util.Comparator;

import org.apache.tools.ant.taskdefs.Length;

/**
 * Compare strings based on their length. Used by ExpressionBuilder to ensure
 * that longest match is attempted first.
 * 
 * @author mwilson
 * @version $Id: StringLengthComparator.java,v 1.1 2006/09/08 19:16:31
 *          anothermwilson Exp $
 * @since 0.7.4 patch 2
 */

public class StringLengthComparator implements Comparator {

	/**
	 * Sort by descending String length
	 * 
	 * @param o1 first string
	 * @param o2 second string
	 * @return difference between the length of o2 and o1
	 */
	public int compare(Object o1, Object o2) {
		String str1 = (String) o1;
		String str2 = (String) o2;
		return (str2.length() - str1.length());
	}
    
}
