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
package org.tp23.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
/**
 * GBCF  Grib Bag Constraints Factory.  This class exists mostly
 * for tidying up code that used the exsivly verbose GridBagConstraints
 * object, hence the shortened class name.
 * By default a two column equal with table is assumed, to support other
 * column layouts use the constructor that take an array of column wieghts
 * Factory class for generating constraints for a equal width column table.
 * the table is similar to the following table rendered in HTML
 * <table border="2">
 *   <tr>
 *     <td>data</td><td>data</td>
 *   </tr>
 *   <tr>
 *     <td colspan="2">data</td>
 *   </tr>
 *   <tr>
 *     <td>data</td><td>data</td>
 *   </tr>
 *   <tr>
 *     <td>data</td><td>data</td>
 *   </tr>
 * <table>
 * @author not attributable
 * @version 1.0
 */
public class GBCF {

	protected Insets insets = new Insets(1,4,1,4);
	protected Insets noinsets = new Insets(0,0,0,0);
	protected double[] columnWeights= new double[]{0.5,0.5};

	public GBCF(){

    }
	/**
	 * to support more than two columns or vary the column wieghtings
	 * @param columnWeights double[]
	 */
	public GBCF(double[] columnWeights){
		this.columnWeights=columnWeights;
	}
	/**
	 *
	 * @param row int the 0 indexed row
	 * @param col int the 0 indexed column
	 * @param span boolean does cell span both columns
	 * @return GridBagConstraints
	 */
	public GridBagConstraints getCell(int row,int col){
		return new GridBagConstraints(col,              //gridx
									  row,              //gridy
									  1,                //gridwidth
									  1,                //gridheight
									  columnWeights[col],//weightx
									  0.0,              //weithty
									  GridBagConstraints.WEST, // anchor
									  GridBagConstraints.NONE, //fill
									  insets,  // insets
									  1, //ipadx
									  1  //ipady
									  );
   }
   /**
	*
	* @param row int the 0 indexed row
	* @param first boolean is this the first column or the second
	* @param span boolean does cell span both columns
	* @return GridBagConstraints
	*/
   public GridBagConstraints getSpan(int row){
	   return new GridBagConstraints(0,               //gridx
									 row,             //gridy
									 2,               //gridwidth
									 1,               //gridheight
									 columnWeights[0],//weightx
									 0.0,             //weithty
									 GridBagConstraints.WEST, // anchor
									 GridBagConstraints.NONE, //fill
									 insets,  // insets
									 1, //ipadx
									 1  //ipady
									 );
  }
  public GridBagConstraints getVertGlue(int row){
	  return new GridBagConstraints(0,               //gridx
									row,             //gridy
									2,               //gridwidth
									1,               //gridheight
									columnWeights[0],//weightx
									1.0,             //weithty
									GridBagConstraints.NORTHWEST, // anchor
									GridBagConstraints.NONE, //fill
									noinsets,  // insets
									0, //ipadx
									0  //ipady
									);
  }
}
