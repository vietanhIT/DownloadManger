/**
 * 
 */
package com.vietanh.views;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JProgressBar;
import javax.swing.table.AbstractTableModel;

import com.vietanh.Constant.Constant;
import com.vietanh.model.HttpDownloadUtility;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class DownloadTableModel extends AbstractTableModel implements Observer {
	private String[] columns = new String[] { "NameFile", "Size", "Progress", "Status" };
	private static final Class[] columnClasses = { String.class, String.class,
			JProgressBar.class, String.class };
	private ArrayList<HttpDownloadUtility> downloadList = new ArrayList<HttpDownloadUtility>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columns.length;
	}
	
	public void setList(){
		if(downloadList.size()>0){
			downloadList.clear();
		}
	}

	public void addModel(HttpDownloadUtility model) {
		model.addObserver(this);
		downloadList.add(model);
		System.out.println(downloadList.get(0).getStatus());
		fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
	}

	@Override
	public String getColumnName(int col) {
		return columns[col];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return downloadList.size();
	}
	
	

	@Override
	public Class<?> getColumnClass(int col) {
		// TODO Auto-generated method stub
		return columnClasses[col];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	@Override
	public Object getValueAt(int row, int col) {
		// TODO Auto-generated method stub
		// Model model = (Model) downloadList.get(row);
		HttpDownloadUtility model = downloadList.get(row);
		switch (col) {
		case 0:
			return model.getName();
		case 1:
			return model.getFileSize();
		case 2:
			return new Float(model.getProgress());
		case 3:
			return getStatus(model.getStatus());
		}
		return "";
	}
	
	public String getStatus(int status){
		switch (status) {
		case Constant.DOWNLOADING:
			return "Downloading";
		case Constant.ERROR:
			return "Error";
		case Constant.CANCELLED:
			return "Cancelled";
		case Constant.COMPLETE:
			return "Complete";
		case Constant.PAUSED:
			return "Pause";
		default:
			break;
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	@Override
	public void update(Observable o, Object arg) {
		fireTableDataChanged();
		int index = downloadList.indexOf(o);
		// Fire table row update notification to table.
		fireTableRowsUpdated(index, index);
	}

}
