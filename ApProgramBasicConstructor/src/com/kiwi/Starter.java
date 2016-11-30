package com.kiwi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import com.kiwi.conf.GlobalConfig;
import com.kiwi.controller.Controller;

public class Starter {

	private static final String edition = "Spring+Mybatis初始架構版2.0";
	private ApplicationContext context;
	private Controller controller;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Starter starter = new Starter();
	}

	public Starter() {
		// 基本初始化
		try {
			initXmlSetting();
			initSpringConstruct();
			createKillBatFile(GlobalConfig.KillBATPath);
			// 設定&顯現主視窗
			controller = (Controller) context.getBean("Controller");
			controller.startApplication(edition);
		} catch (Exception e) {
			System.out.println("系統執行ERROR");
			e.printStackTrace();
		}
	}

	private void initXmlSetting() {
		try {
			System.out.println("讀取Xml設定檔...");
			// 讀取XML檔案
			File xmlFile = new File(GlobalConfig.Conf_XML_path);
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			// 讀取各設定
			GlobalConfig.TrayPassword = doc.getElementsByTagName("TrayPassword").item(0).getFirstChild().getNodeValue();
			System.out.println("TrayPassword : " + GlobalConfig.TrayPassword);
			GlobalConfig.KillBATPath = doc.getElementsByTagName("KillBATPath").item(0).getFirstChild().getNodeValue();
			System.out.println("KillBATPath : " + GlobalConfig.KillBATPath);

			System.out.println("讀取Xml設定完成.");
		} catch (Exception e) {
			System.out.println("讀取Xml設定失敗.");
			e.printStackTrace();
		}
	}

	/**
	 * 初始化整個程式架構(使用Spring Framework)
	 * 
	 * @return controller 初始化完後回傳UI用的controller
	 * */
	public void initSpringConstruct() {
		System.out.println("初始化Spring架構...");
		context = new ClassPathXmlApplicationContext(
				GlobalConfig.Spring_conf_path);
		System.out.println("初始化Spring架構完成.");
	}

	private void createKillBatFile(String filePath) {
		try {
			String process = ManagementFactory.getRuntimeMXBean().getName();
			String PID = process.split("@")[0];
			FileWriter writer;
			writer = new FileWriter(filePath);
			writer.write("taskkill /f /pid " + PID);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("程式停止BAT檔產生失敗!!");
			System.exit(0);
		}
	}
}
