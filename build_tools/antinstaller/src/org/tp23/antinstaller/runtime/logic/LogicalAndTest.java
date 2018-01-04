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

/**
 * @author mwilson
 * @version $Id: LogicalAndTest.java,v 1.1 2006/09/08 19:16:28 anothermwilson
 *          Exp $
 * @since 0.7.4 patch 2
 */
public class LogicalAndTest implements LogicalTest {

	private static final String[] TEST_TOKENS = { "&&", "AND" };

	public String[] getTestTokens() {
		return TEST_TOKENS;
	}

	public boolean getTestResult(Expression expression1, Expression expression2) {

		boolean result = false;

		if (expression1 != null && expression2 != null) {
			result = expression1.evaluate() && expression2.evaluate();
		}

		return result;
	}
}
