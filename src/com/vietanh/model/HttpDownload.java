package com.vietanh.model;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;

import com.vietanh.Constant.Constant;
import com.vietanh.views.DownloadTableModel;

/**
 * 
 */

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since May 31, 2016
 *
 */
public class HttpDownload {
	String fileName = "";
	int sizepart = Constant.sizeThread+1;
	int part = 0;
	ArrayList<JButton> listButton;
	public void downloadThread(String fileURL, DownloadTableModel model,JLabel statusLabel,ArrayList<JButton> listButton) {
		this.listButton=listButton;
		int contentLength = 0;
		int[] partfile = null;
		HttpURLConnection httpConn = null;
		try {
			URL url = new URL(fileURL);
			httpConn = (HttpURLConnection) url.openConnection();
			int responseCode = httpConn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				contentLength = httpConn.getContentLength();
				String disposition = httpConn.getHeaderField("Content-Disposition");
				if (disposition != null) {
					// extracts file name from header field
					int index = disposition.indexOf("filename=");
					if (index > 0) {
						fileName = disposition.substring(index + 10,
								disposition.length() - 1);
					}
				} else {
					// extracts file name from URL
					fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
							fileURL.length());
				}
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		if (contentLength % (sizepart - 1) != 0) {
			sizepart += 1;
			partfile = new int[sizepart];
			for (int i = 0; i < sizepart - 1; i++) {
				partfile[i] = contentLength / (sizepart - 1) * i;
			}
			partfile[sizepart - 1] = contentLength;
		} else {
			partfile = new int[sizepart];
			for (int i = 0; i < sizepart; i++) {
				partfile[i] = contentLength / (sizepart - 1) * i;
			}

		}
		String filepath=Constant.SAVE_DIR+fileName;
		httpConn.disconnect();
		while (part < sizepart - 1) {

			ThreadFile thread = new ThreadFile(fileURL, partfile[part],
					partfile[part + 1] - 1, part,model);
			thread.start();
			part++;
		}
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
					System.out.println(Constant.downloaded);
					if(Constant.downloaded==part){
						MergeFile mergeFile=new MergeFile();
						statusLabel.setText("Status: Mergeing");
						mergeFile.mergeFile(filepath,part);
						Constant.downloaded++;
						statusLabel.setText("Status: SucessFull");
						Constant.isComplete=true;
						setStateButton();
						setState();
					}
				}
			}
		}).start();
	}
	
	public void setStateButton(){
		listButton.get(0).setEnabled(true);
		listButton.get(1).setEnabled(false);
		listButton.get(2).setEnabled(false);
		listButton.get(3).setEnabled(false);
	}
	
	public void setState(){
		Constant.downloaded=0;
		Constant.isPause=false;
		Constant.isResume=false;
		Constant.isComplete=false;
		Constant.isCancel=false;
	}
	
	public boolean checkFileDownload(int contentLength){
		if(contentLength>0) return true;
		return false;
	}

}

class ThreadFile extends Thread {
	String fileURL;
	int fromOffset, toOffset, part;
	HttpDownloadUtility http;
	DownloadTableModel model;

	public ThreadFile(String fileURL, int fromOffset, int toOffset, int part, DownloadTableModel model
			) {
		super();
		this.fileURL = fileURL;
		this.fromOffset = fromOffset;
		this.toOffset = toOffset;
		this.part = part;
		this.model=model;
	}

	public void run() {
		http= new HttpDownloadUtility(fileURL, Constant.SAVE_DIR,
				fromOffset, toOffset, part,model);
		try {
//			System.out.println("Thread dowload file from " + fromOffset
//					+ " to " + toOffset);
			http.downloadFile();
			
			if(http.getStatus()==2||http.getStatus()==4){
				interrupt();
				System.out.println(isAlive());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public HttpDownloadUtility getModel(){
		return http;
	}
}
