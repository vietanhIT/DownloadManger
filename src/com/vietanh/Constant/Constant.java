/**
 * 
 */
package com.vietanh.Constant;

/**
 * @author Nguyen Viet Anh
 * @version 1
 * @since Jun 1, 2016
 *
 */
public class Constant {
	public static final int DOWNLOADING = 0;
	public static final int PAUSED = 1;
    public static final int COMPLETE = 2;
    public static final int CANCELLED = 3;
    public static final int ERROR = 4;
    public static int downloaded=0;
    public static int sizeThread=14;
    public static final String SAVE_DIR="E:\\";
    public static boolean isPause=false;
    public static boolean isResume=false;
    public static boolean isComplete=false;
    public static boolean isCancel=false;
}
