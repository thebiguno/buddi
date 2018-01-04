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
 * @version $Id: CompoundExpression.java,v 1.1 2006/09/08 19:16:22
 *          anothermwilson Exp $
 * @since 0.7.4 patch 2
 */
public class CompoundExpression implements Expression {

	private Expression expression1;
	private LogicalTest test;
	private Expression expression2;

	public CompoundExpression(Expression expr1, final LogicalTest logicalTest, Expression expr2) {
		this.expression1 = expr1;
		this.expression2 = expr2;
		this.test = logicalTest;
	}

	public boolean evaluate() {
		return test.getTestResult(expression1, expression2);
	}
}
