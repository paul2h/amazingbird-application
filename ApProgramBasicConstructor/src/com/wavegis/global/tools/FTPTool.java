package com.wavegis.global.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

public class FTPTool {

	/**
	 * FTP - 上傳單一檔案
	 */
	public static synchronized void UploadFile(String ip, String targetDirectory, String loginID, String passwd, String fileSource,
			String fileName, boolean isPhoto) {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fileInputStream = null;

		try {
			// #[[ 連線
			ftpClient.connect(ip);
			ftpClient.login(loginID, passwd);
			// ]]

			// #[[ 取得要上傳的檔案 & 上傳串流
			File srcFile = new File(fileSource);
			fileInputStream = new FileInputStream(srcFile);
			// ]]

			// #[[ 上傳檔案
			if (isPhoto) {
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);// 上傳圖片
			}
			// 選擇上傳的目標資料夾
			ftpClient.changeWorkingDirectory(targetDirectory);
			// 上傳
			ftpClient.storeFile(fileName, fileInputStream);
			// ]]
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客戶端問題！", e);
		} finally {
			IOUtils.closeQuietly(fileInputStream);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("關閉FTP連線時出現異常！", e);
			}
		}
	}

	/**
	 * 下載單一檔案
	 */
	public static synchronized void DownLoadFile(String ip, String targetDirectory, String loginID, String passwd, String fileName,
			String savePath) {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fileOutputStream = null;
		try {
			// #[[ 連線
			ftpClient.connect(ip);
			ftpClient.login(loginID, passwd);
			// ]]

			// #[[ 取檔案
			String remoteFilePath = targetDirectory + "/" + fileName;
			if (ftpClient.listFiles(remoteFilePath).length > 0) {
				fileOutputStream = new FileOutputStream(savePath + "/" + fileName);
				ftpClient.setBufferSize(1024);
				ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
				ftpClient.retrieveFile(remoteFilePath, fileOutputStream);
			} else {
				System.out.println("檔案不存在!");
			}
			// ]]
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客戶端問題！", e);
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("關閉FTP連接發生異常！", e);
			}
		}

	}

	/** 檢視指定資料夾裡面的檔案 */
	public static synchronized void ListFiles(String ip, String targetDirectory, String loginID, String passwd) {
		FTPClient ftpClient = new FTPClient();
		FileOutputStream fileOutputStream = null;
		try {
			// #[[ 連線
			ftpClient.connect(ip);
			ftpClient.login(loginID, passwd);
			// ]]

			// #[[ 取檔案
			int count = 0;
			String[] fileNames = ftpClient.listNames(targetDirectory);
			for (String fileName : fileNames) {
				System.out.println(fileName);
				count++;
			}
			System.out.println(count);

			// ]]
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客戶端問題！", e);
		} finally {
			IOUtils.closeQuietly(fileOutputStream);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("關閉FTP連接發生異常！", e);
			}
		}

	}
}
