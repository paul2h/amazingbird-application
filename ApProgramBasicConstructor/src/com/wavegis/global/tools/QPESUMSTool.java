package com.wavegis.global.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

public class QPESUMSTool {

	private static int image_width = 0;
	private static int image_height = 0;

	private static BufferedImage bmp = null;
	private static boolean is_CBRAD_Format = false;

	private static float _min_lon;
	@SuppressWarnings("unused")
	private static float _max_lon;
	@SuppressWarnings("unused")
	private static float _min_lat;
	private static float _max_lat;

	private static float _dx = 0;
	private static float _dy = 0;

	private static short[][] valueData;
	private static int[] valueSpace = { 0, 1, 2, 5, 10, 15, 20, 30, 40, 50, 80, 100, 150, 200, 250 };
	private static int[] colorSpace = new int[] {
			0,
			(99 << 16 | 82 << 8 | 115),
			(115 << 16 | 99 << 8 | 132),
			(156 << 16 | 156 << 8 | 156),
			(0 << 16 | 206 << 8 | 0),
			(0 << 16 | 173 << 8 | 0),
			(0 << 16 | 148 << 8 | 0),
			(255 << 16 | 255 << 8 | 0),
			(231 << 16 | 198 << 8 | 0),
			(255 << 16 | 148 << 8 | 0),
			(255 << 16 | 99 << 8 | 99),
			(255 << 16 | 0 << 8 | 0),
			(206 << 16 | 0 << 8 | 0),
			(255 << 16 | 0 << 8 | 255),
			(156 << 16 | 49 << 8 | 206),
			(255 << 16 | 255 << 8 | 255)
	};

	public static void transDataProcess(String gzFilePath, String pngFilePath , String map_image_path , String boundaryMapImagePath, String outputResultPath) {
		checkFoldExist(pngFilePath, outputResultPath);
		extract_gz(gzFilePath, pngFilePath);
		mergeImage(map_image_path, pngFilePath, pngFilePath, boundaryMapImagePath, outputResultPath);
	}

	private static void checkFoldExist(String pngFilePath, String outputResultPath) {
		File pngFold = new File(pngFilePath).getParentFile();
		if (!pngFold.exists()) {
			pngFold.mkdirs();
		}

		File outputResult1Fold = new File(outputResultPath).getParentFile();
		if (!outputResult1Fold.exists()) {
			outputResult1Fold.mkdirs();
		}

	}

	/**
	 * <b>extract_gz</b><br>
	 * extract gz file into imageData[]
	 * 
	 * @param fileName
	 *            input gz format filename
	 * @return true : success <br>
	 *         false : fail
	 */
	public static boolean extract_gz(String fileName, String outputPngFilePath) {
		boolean result = false;
		int nRadars = 0;
		int x = 0, y = 0;
		short rainData = 0;
		@SuppressWarnings("unused")
		int colorPos = 0;

		if (!new File(fileName).exists())
			return result;

		try {
			FileInputStream is = new FileInputStream(fileName);
			GZIPInputStream zis = new GZIPInputStream(new BufferedInputStream(is));
			DataInputStream dis = new DataInputStream(zis);

			byte[] buf = new byte[100];

			// start reading
			for (int i = 0; i < 6; i++)
				readInt_LittleEndian(dis); // no use in our program

			image_width = readInt_LittleEndian(dis); // x : width
			image_height = readInt_LittleEndian(dis); // y : height

			int nz = readInt_LittleEndian(dis); // z : depth

			readInt_LittleEndian(dis); // projection

			readInt_LittleEndian(dis); // map scale

			for (int i = 0; i < 3; i++)
				readInt_LittleEndian(dis); // no use in my program

			_min_lon = readInt_LittleEndian(dis);

			_max_lat = readInt_LittleEndian(dis);

			int xy_scale = readInt_LittleEndian(dis); // xy scale
			_min_lon /= xy_scale;
			_max_lat /= xy_scale;

			_dx = readInt_LittleEndian(dis); // dx
			_dy = readInt_LittleEndian(dis); // dy

			int dxy_scale = readInt_LittleEndian(dis); // dxy scale

			_dx /= dxy_scale;
			_dy /= dxy_scale;

			_max_lon = _min_lon + (image_width - 1) * _dx;

			_min_lat = _max_lat - (image_height - 1) * _dy;

			for (int i = 0; i < nz; i++) {
				readInt_LittleEndian(dis);
			}

			readInt_LittleEndian(dis);
			readInt_LittleEndian(dis);

			for (int i = 0; i < 9; i++) {
				readInt_LittleEndian(dis);
			}

			dis.read(buf, 0, 20); // name

			is_CBRAD_Format = (buf[0] == 0x43 && buf[1] == 0x42); // CB 1H RAD

			dis.read(buf, 0, 6); // no use in my program

			readInt_LittleEndian(dis); // var_scale
			readInt_LittleEndian(dis); // imissing

			nRadars = readInt_LittleEndian(dis); // number of Radars

			dis.read(buf, 0, nRadars * 4); // no use in my program

			valueData = new short[image_width][image_height];
			bmp = new BufferedImage(image_width, image_height, BufferedImage.TYPE_INT_ARGB);

			// read remaining data ; raw data to image
			// every 2 bytes display 1 dot
			// bottom to up , left to right
			for (y = image_height - 1; y >= 0; y--) {
				colorPos = image_width * y;

				for (x = 0; x < image_width; x++) {
					rainData = readShort_LittleEndian(dis);

					if (rainData == 0xFFFC) {
						valueData[x][y] = (short) 0;
					} else if (rainData > 0 && rainData < 60000) {
						valueData[x][y] = (short) rainData;
						bmp.setRGB(x, y, 0xFF000000 | getColor(rainData));
					}

					if (is_CBRAD_Format && rainData == 0) {
						valueData[x][y] = (short) 0;
						bmp.setRGB(x, y, 0xFF000000 | (50 << 16 | 50 << 8 | 50));
					}

					colorPos++;
				}
			}

			dis.close();

			bmp.flush();

			File outputfile = new File(outputPngFilePath);
			ImageIO.write(bmp, "png", outputfile);

			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * <b>readIntLittleEndian</b><br>
	 * read little endian Integer
	 * 
	 * @param dis
	 *            DataInputStream
	 * @return int
	 */
	private static int readInt_LittleEndian(DataInputStream dis) {
		int result = -1;

		try {
			result = (dis.readByte() & 0xFF)
					| (dis.readByte() & 0xFF) << 8
					| (dis.readByte() & 0xFF) << 16
					| (dis.readByte() & 0xFF) << 24;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * <b>getColor</b><br>
	 * get input value mapped color
	 * 
	 * @param value
	 */
	private static int getColor(int value) {
		int result = 0;

		if (value > valueSpace[valueSpace.length - 1]) {
			result = colorSpace[colorSpace.length - 1];
			return result;
		}

		for (int i = 0; i < valueSpace.length - 1; i++) {
			if (value > valueSpace[i] && value <= valueSpace[i + 1]) {
				result = colorSpace[i + 1];
				break;
			}
		}

		return result;
	}

	/**
	 * <b>readShortLittleEndian</b><br>
	 * read little endian Short
	 * 
	 * @param dis
	 *            DataInputStream
	 * @return short in int
	 */
	private static short readShort_LittleEndian(DataInputStream dis) {
		short result = -1;

		try {
			result = (short) ((dis.readByte() & 0xFF) | (dis.readByte() & 0xFF) << 8);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	private static void mergeImage(String map_image_path, String originalFileString, String pngFilePath , String boundaryMapImagePath , String outputResultPath) {
		try {
			// #[[ 讀入所需資訊
			/* 底圖圖層 */
			File mapImageFile = new File(map_image_path);
			Image mapImage = ImageIO.read(mapImageFile);
			/* 圖片長寬 */
			int mapImageWidth = mapImage.getWidth(null);
			int mapImageHeight = mapImage.getHeight(null);
			/* QP附圖層 */
			File qpImageFile = new File(pngFilePath);
			Image qpImage = ImageIO.read(qpImageFile);
			/* 圖片長寬 */
			int qpImageWidth = qpImage.getWidth(null);
			int qpImageHeight = qpImage.getHeight(null);
			/* 再蓋一層分區圖層 */
			File countyImageFile = new File(boundaryMapImagePath);
			Image countyImage = ImageIO.read(countyImageFile);
			/* 圖片長寬 */
			int countyImageWidth = countyImage.getWidth(null);
			int countyImageHeight = countyImage.getHeight(null);
			// ]]
			// #[[ 編輯圖片 - 全島
			// 產生編輯圖片空間
			BufferedImage bufferedImage = new BufferedImage(mapImageWidth, mapImageHeight, BufferedImage.TYPE_INT_RGB);
			// 抓出圖片編輯物件
			Graphics2D g2d = bufferedImage.createGraphics();
			// 畫底圖
			g2d.drawImage(mapImage, 0, 0, mapImageWidth, mapImageHeight, null);
			// 畫第二張圖
			g2d.drawImage(qpImage, 0, 0, qpImageWidth * 3, qpImageHeight * 3, null);
			// 畫縣市圖
			g2d.drawImage(countyImage, 0, 0, countyImageWidth, countyImageHeight, null);

			// 寫入時間
			String dateString = originalFileString.split("/")[originalFileString.split("/").length - 1].replaceAll("qpfqpe_060min.", "").replaceAll(".gz", "");
			g2d.setFont(new Font("新細明體", Font.PLAIN, 10));
			g2d.setColor(Color.BLACK);
			g2d.drawString(dateString, 10, 10);

			// ]]
			// #[[ 輸出圖片
			ImageIO.write(bufferedImage, "jpg", new File(outputResultPath));
			// ]]
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
