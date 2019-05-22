package org.benben.common.util;

/**
 * 七牛云上传文件格式
 */
public class FileUtil {
	// 允许文件的后缀扩展名
	public static String[] IMAGE_FILE_EXTD = new String[] { "png", "bmp", "jpg", "jpeg","pdf","AVI","mov","rmvb","rm","FLV","mp4","3GP","docs","doc","mp3" };

	public static boolean isFileAllowed(String fileName) {
		for (String ext : IMAGE_FILE_EXTD) {
			if (ext.equals(fileName)) {
				return true;
			}
		}
		return false;
	}
}
