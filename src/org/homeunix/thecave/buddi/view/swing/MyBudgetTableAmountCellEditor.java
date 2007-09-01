/*
 * Created on Aug 5, 2007 by wyatt
 */
package org.homeunix.thecave.buddi.view.swing;

import java.awt.Color;
import java.awt.Component;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.table.TableCellEditor;

import org.homeunix.thecave.moss.swing.MossDecimalField;

public class MyBudgetTableAmountCellEditor extends AbstractCellEditor implements TableCellEditor {
	public static final long serialVersionUID = 0;

	private final MossDecimalField editor;

	public MyBudgetTableAmountCellEditor(MossDecimalField editor) {
		this.editor = editor;
		this.editor.setBorder(new LineBorder(Color.black));
	}

	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if (value instanceof Object[]){
//			long l = Long.parseLong(value.toString().replaceAll("[^0-9-]", ""));
			Object[] values = (Object[]) value;
			editor.setValue((Long) values[1]);
		}
		else
			editor.setValue(0);
		return editor;
	}

	public Object getCellEditorValue() {
		return editor.getValue();
	}

	@Override
	public boolean isCellEditable(EventObject arg0) {
		return true;
	}
}