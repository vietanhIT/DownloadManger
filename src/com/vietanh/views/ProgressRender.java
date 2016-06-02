/**
 * 
 */
package com.vietanh.views;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class ProgressRender extends JProgressBar implements TableCellRenderer{
	
	public ProgressRender(int min, int max) {
	    super(min, max);
	  }
	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		// TODO Auto-generated method stub
		setValue((int) ((Float) value).floatValue());
	    return this;
	}

}
