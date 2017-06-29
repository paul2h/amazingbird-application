package com.wavegis.global.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class TextTool {
	
	public static synchronized String readText(String textPath) throws IOException {
		String result = "";
		File file = new File(textPath);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		while (reader.ready()) {
			result += reader.readLine() + "\n";
		}
		reader.close();
		
		return result;
	}
	
	public static synchronized boolean writeText(String filePath , String text){
		boolean success = false;

		BufferedWriter fw = null;
		boolean isAppend = false;// 清空寫入 or 寫在原資料後面
		try {
			File file = new File(filePath);

			fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), "UTF-8"));
			fw.append(text);
			fw.flush(); // 全部寫入緩存中的內容
			success = true;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();

		} finally {

			if (fw != null) {

				try {

					fw.close();

				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}
		return success;
	}
}
