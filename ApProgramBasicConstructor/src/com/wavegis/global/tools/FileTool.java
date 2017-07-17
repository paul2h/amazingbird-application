package com.wavegis.global.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class FileTool {
	
	public static File[] scanFold(String dirPath) {
		File dir = new File(dirPath);
		return dir.listFiles();
	}

	public static boolean moveFile(String moveFilePath, String targetPath) {
		boolean isSucces = false;
		File moveFile = new File(moveFilePath);
		File targetFile = new File(targetPath);
		try {
			FileUtils.copyFile(moveFile, targetFile);
			moveFile.delete();
			isSucces = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isSucces;
	}

}
