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

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.tp23.antinstaller.input.ResultContainer;
import org.tp23.antinstaller.runtime.ConfigurationException;

/**
 * @author mwilson
 * @version $Id: ExpressionBuilder.java,v 1.2 2006/12/21 00:03:18 teknopaul Exp $
 * @since 0.7.4 patch 2
 */
public class ExpressionBuilder {
	private static final int GROUPING_START_OPERATOR = '(';

	private static final int GROUPING_END_OPERATOR = ')';

	private static final ValuesTest[] testValueConditions = { 
		new EndsWithTest(), 
		new EqualsTest(), 
		new GreaterThanOrEqualsTest(),
		new LessThanOrEqualsTest(), 
		new NotEqualsTest(), 
		new StartsWithTest() 
	};

	private static final LogicalTest[] testLogicalConditions = { 
		new LogicalAndTest(), 
		new LogicalOrTest() 
	};

	private static final SingleExpressionTest SINGLE_EXPRESSION_TEST = new SingleExpressionTest();

	private static Map tokenMap = new HashMap();

	private static String[] valueTokens;

	private static String[] logicalTokens;

	static {
		for (int i = 0; i < testValueConditions.length; i++) {
			String[] tmpTokens = testValueConditions[i].getTestTokens();
			for (int j = 0; j < tmpTokens.length; j++) {
				tokenMap.put(tmpTokens[j], testValueConditions[i]);
			}
		}

		valueTokens = new String[tokenMap.size()];
		int index = 0;
		for (int i = 0; i < testValueConditions.length; i++) {
			String[] tmpTokens = testValueConditions[i].getTestTokens();
			for (int j = 0; j < tmpTokens.length; j++) {
				valueTokens[index++] = tmpTokens[j];
			}
		}

		Comparator lengthComparator = new StringLengthComparator();

		//Have to sort so that longest test operator is checked for first
		Arrays.sort(valueTokens, lengthComparator);

		index = 0;
		final int mapInitialSize = tokenMap.size();

		for (int i = 0; i < testLogicalConditions.length; i++) {
			String[] tmpTokens = testLogicalConditions[i].getTestTokens();
			for (int j = 0; j < tmpTokens.length; j++) {
				tokenMap.put(tmpTokens[j], testLogicalConditions[i]);
			}
		}

		logicalTokens = new String[tokenMap.size() - mapInitialSize];
		for (int i = 0; i < testLogicalConditions.length; i++) {
			String[] tmpTokens = testLogicalConditions[i].getTestTokens();
			for (int j = 0; j < tmpTokens.length; j++) {
				logicalTokens[index++] = tmpTokens[j];
			}
		}

		Arrays.sort(logicalTokens, lengthComparator);

	}

	public static Expression parseLogicalExpressions(ResultContainer container, String exprStr) throws ConfigurationException {
		int startIndex = skipWhiteSpace(exprStr, 0);
		int index = exprStr.indexOf(GROUPING_START_OPERATOR, startIndex);

		if (index == -1) {
			return getSimpleExpression(container, exprStr.substring(startIndex));
		}

		if (index != 0) {
			throw new ConfigurationException("Illegal ifProperty value: If present, grouping operator " + GROUPING_START_OPERATOR
					+ " must be at start of property string");
		}

		++startIndex; //Skip over grouping operator

		int endIndex = exprStr.indexOf(GROUPING_END_OPERATOR, startIndex);

		if (endIndex == -1) {
			throw new ConfigurationException("Missing closing grouping bracket " + GROUPING_END_OPERATOR + " in expression " + exprStr);
		}

		//Check that this isn't an attempt tu use nested logical tests - not supported
		int tstIndex = exprStr.indexOf(GROUPING_START_OPERATOR, startIndex);
		if ((tstIndex != -1) && (tstIndex < endIndex)) {
			throw new ConfigurationException("Nesting of logical operations is not supported: " + exprStr);
		}

		try {
			Expression expr1 = parseLogicalExpressions(container, exprStr.substring(startIndex, endIndex));

			LogicalTest test = null;

			startIndex = endIndex + 1;

			//Look for logical operator token
			String logicalToken = getLogicalToken(exprStr, startIndex);

			for (int i = 0; i < logicalTokens.length; i++) {

				if (logicalTokens[i].compareTo(logicalToken) == 0) {
					test = (LogicalTest) tokenMap.get(logicalTokens[i]);
					index = exprStr.indexOf(logicalTokens[i], startIndex);
					startIndex = index + logicalTokens[i].length();
					break;
				}
			}

			if (test == null) {
				return new CompoundExpression(expr1, SINGLE_EXPRESSION_TEST, null);
			} else {
				startIndex = skipWhiteSpace(exprStr, startIndex);
				String expr2Str = exprStr.substring(startIndex, exprStr.length());
				return new CompoundExpression(expr1, test, parseLogicalExpressions(container, expr2Str));

			}
		} catch (Exception e) {
			throw new ConfigurationException("Invalid ifProperty expression");
		}
	}

	private static Expression getSimpleExpression(ResultContainer resultContainer, String exprStr) throws ConfigurationException {
		try {
			int index = -1;
			int i;

			for (i = 0; i < valueTokens.length; i++) {
				index = exprStr.indexOf(valueTokens[i]);

				if (index != -1) {
					break;
				}
			}

			return new SimpleExpression(resultContainer, exprStr.substring(0, index), (ValuesTest) tokenMap.get(valueTokens[i]), exprStr
					.substring(index + valueTokens[i].length()));
		} catch (Exception e) { // can be StringIndexOutOfBoundsException - PH
			throw new ConfigurationException("Invalid ifProperty expression");
		}
	}

	private static int skipWhiteSpace(final String str, final int startIndex) {
		final int strLen = str.length();
		int index = startIndex;

		while (index < strLen) {
			if ((str.charAt(index) != ' ') && (str.charAt(index) != '\t')) {
				break;
			}

			++index;
		}

		return index;
	}

	private static String getLogicalToken(final String str, final int startIndex) {
		final int strLen = str.length();
		int tokenStart = skipWhiteSpace(str, startIndex);
		int endIndex = tokenStart;

		while (endIndex < strLen) {
			int chr = str.charAt(endIndex);
			if ((chr == ' ') || (chr == '\t') || (chr == GROUPING_START_OPERATOR)) {
				break;
			}

			++endIndex;
		}

		return str.substring(tokenStart, endIndex);
	}
}
