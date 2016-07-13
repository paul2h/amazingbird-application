package com.kiwi;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import com.kiwi.conf.GlobalConfig;
import com.kiwi.ui.StartUI;

public class Starter {
	
	private static final String edition = "Spring+Mybatis初始架構版";
	private ApplicationContext context;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		Starter starter = new Starter();
	}
	public Starter() {
		// 基本初始化
		try {
			initXmlSetting();
			initSpringConstruct();
			
			// 設定&顯現主視窗
			StartUI mainUI = ((StartUI) context.getBean("StartUI"));
			mainUI.setTitle(edition);
			mainUI.start();
		} catch (Exception e) {
			System.out.println("系統執行ERROR");
			e.printStackTrace();
		}
	}
	private void initXmlSetting() {
		try {
			System.out.println("讀取Xml設定檔...");
			// 讀取XML檔案
			File xmlFile = new File("./conf.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			// 讀取各設定
			GlobalConfig.conf01 = doc.getElementsByTagName("conf01").item(0).getFirstChild().getNodeValue();
			System.out.println("conf01 : " + GlobalConfig.conf01);
			GlobalConfig.conf02 = doc.getElementsByTagName("conf02").item(0).getFirstChild().getNodeValue();
			System.out.println("conf02 : " + GlobalConfig.conf02);

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
	public  void initSpringConstruct() {
		System.out.println("初始化Spring架構...");
		context = new ClassPathXmlApplicationContext(
				"./spring-config.xml");
		System.out.println("初始化Spring架構完成.");
	}
}
