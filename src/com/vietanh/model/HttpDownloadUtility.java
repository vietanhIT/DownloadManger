package com.vietanh.model;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

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
public class HttpDownloadUtility extends Observable{
	private static final int BUFFER_SIZE = 4096;
    private int status=0;
	int downloaded = 0;
	int contentLength;
	String fileURL, saveDir;
	int fromOffset,toOffset,part;
	String fileName = "";
	DownloadTableModel model;
	HttpURLConnection httpConn;
	URL url ;
	public HttpDownloadUtility(String fileURL, String saveDir, int fromOffset,
			int toOffset, int part,DownloadTableModel model){
		this.fileURL =fileURL;
		this.saveDir =saveDir;
		this.fromOffset =fromOffset;
		this.toOffset =toOffset;
		this.part =part;
		this.model=model;
	}
	public void downloadFile() throws IOException {
		long startTime = System.currentTimeMillis();
		url = new URL(fileURL);
		httpConn= (HttpURLConnection) url.openConnection();
		//System.out.println(String.format("bytes=%d-%d", fromOffset, toOffset));
		httpConn.setRequestProperty("Range",
				String.format("bytes=%d-%d", fromOffset, toOffset));
		int responseCode = httpConn.getResponseCode();

		// always check HTTP response code first

		
		String disposition = httpConn.getHeaderField("Content-Disposition");
		String contentType = httpConn.getContentType();
		String range = httpConn.getRequestProperty("Range");
		contentLength= httpConn.getContentLength();
		//System.out.println(contentLength);

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

//		System.out.println("Content-Type = " + contentType);
//		System.out.println("Content-Disposition = " + disposition);
//		System.out.println("Content-Length = " + contentLength);
//		System.out.println("fileName = " + fileName);
//		System.out.println(range);
		model.addModel(this);
		// opens input stream from the HTTP connection
		InputStream inputStream = httpConn.getInputStream();
//		String saveFilePath = saveDir + File.separator + fileName + part;
//
//		// opens an output stream to save into file
//		FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//
//		int bytesRead = -1;
//		byte[] buffer = new byte[BUFFER_SIZE];
//		while ((bytesRead = inputStream.read(buffer)) != -1) {
//			outputStream.write(buffer, 0, bytesRead);
//			//System.out.println(bytesRead);
//			downloaded += bytesRead;
//			status = Constant.DOWNLOADING;
//			stateChanged();
//			if(pause()){
//				break;
//			}
//			//System.out.println(Constant.isPause);
//		}
//
//		outputStream.close();
//		inputStream.close();
//		checkDownload();
//		System.out.println("File downloaded");
//		//System.out.println(Constant.downloaded);
//		long endTime = System.currentTimeMillis();
//		long totalTime = endTime - startTime;
//		httpConn.disconnect();
		readStream(inputStream);
	}
	
	public void readStream(InputStream inputStream) throws IOException{
		String saveFilePath = saveDir + File.separator + fileName + part;

		// opens an output stream to save into file
		FileOutputStream outputStream = new FileOutputStream(saveFilePath);

		int bytesRead = -1;
		byte[] buffer = new byte[BUFFER_SIZE];
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, bytesRead);
			//System.out.println(bytesRead);
			downloaded += bytesRead;
			status = Constant.DOWNLOADING;
			stateChanged();
			while(pause()){
				if(resume()){
					System.out.println("yes");
					inputStream = getInputAfterResume();
					System.out.println(inputStream);
					if(inputStream==null){
						status = Constant.ERROR;
						stateChanged();
					}
					break;
				}
			}
			if(cancel()){
				break;
			}
			//System.out.println(Constant.isPause);
		}

		outputStream.close();
		inputStream.close();
		checkDownload();
		//System.out.println("File downloaded");
		//long endTime = System.currentTimeMillis();
		//long totalTime = endTime - startTime;
		httpConn.disconnect();
	}
	
	public float getProgress(){
		return ((float) downloaded/contentLength)*100;
	}
	
	public String getName(){
		return "part"+(part+1);
	}
	
	public int getStatus(){
		return status;
	}
	
	public int getFileSize(){
		return contentLength;
	}
	
	public void stateChanged(){
		setChanged();
		notifyObservers();
	}
	
	public int getState(){
		return status;
	}
	
	public void checkDownload(){
		if(status==Constant.DOWNLOADING){
			if(downloaded==contentLength){
				status=Constant.COMPLETE;
				Constant.downloaded++;
				stateChanged();
			}else{
				status=Constant.ERROR;
				stateChanged();
			}
		}
	}
	
	public boolean pause(){
		if(Constant.isPause){
			httpConn.disconnect();
			status = Constant.PAUSED;
			stateChanged();
			return true;
		}
		return false;
	}
	
	public InputStream getInputAfterResume(){
		InputStream inputStream = null;
		//HttpURLConnection httpCon;
		try {
			httpConn= (HttpURLConnection) url.openConnection();
			httpConn.setRequestProperty("Range",
					String.format("bytes=%d-%d", (fromOffset+downloaded), toOffset));
			inputStream=httpConn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return inputStream;
		
	}
	
	public boolean resume(){
		if(Constant.isResume){
				status = Constant.DOWNLOADING;
				stateChanged();
				return true;
		}
		return false;
	}
	
	public boolean cancel(){
		if(Constant.isCancel){
			status = Constant.ERROR;
			stateChanged();
			return true;
	}
		return false;
	}
}
