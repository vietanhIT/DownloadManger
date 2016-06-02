/**
 * 
 */
package com.vietanh.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.vietanh.Constant.Constant;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class MergeFile {
	public void mergeFile(String filename, int part){
		File ofile = new File(filename);
		try {
			ofile.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(ofile.isFile()){
			String name= ofile.getName();
			String[] arg = name.split("\\(");
			if(arg.length ==1){
				String[] arg3 = name.split("\\.");
				name = arg3[0] + "(1)"+".";
				for(int i=1; i<arg3.length;i++ ){
					name+= arg3[i];
				}
			}else{
				String[] arg1 = arg[1].split("\\)");
				int i = Integer.parseInt(arg1[0]);
				name = arg[0]+ "(" + String.valueOf(++i) + ")";
				for(int j=1; j<arg1.length ; j++){
					 name +=  arg1[1];
				}	
			}
			ofile= new File(Constant.SAVE_DIR+name);
		}
		
		FileOutputStream fos;
		FileInputStream fis;
		byte[] fileBytes;
		int bytesRead = 0;
		List<File> list = new ArrayList<File>();
		for (int i=0;i<part;i++){
			list.add(new File(filename+i));
		}
		
		try {
		    fos = new FileOutputStream(ofile,true);
		    for (File file : list) {
		        fis = new FileInputStream(file);
		        fileBytes = new byte[(int) file.length()];
		        bytesRead = fis.read(fileBytes, 0,(int)  file.length());
		        assert(bytesRead == fileBytes.length);
		        assert(bytesRead == (int) file.length());
		        fos.write(fileBytes);
		        fos.flush();
		        fileBytes = null;
		        fis.close();
		        fis = null;
		    }
		    fos.close();
		    fos = null;
		}catch (Exception exception){
			exception.printStackTrace();
		}
		
		for (int i=0;i<part;i++){
			list.get(i).delete();
		}
	}
}
