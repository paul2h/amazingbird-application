package com.kiwi.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.w3c.dom.Document;

import com.kiwi.conf.GlobalConfig;
import com.kiwi.controller.Controller;

@SuppressWarnings("serial")
public class StartUI extends JFrame implements ActionListener {

	private JTextArea messageArea;
	private Controller controller;
	private JButton button;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		StartUI s = new StartUI();
	}

	public StartUI() {
		super("Spring+Mybatis程式基本版");
		initUI();
		controller = initSpringConstruct();
		initXmlSetting();
	}

	/** 初始化UI介面 */
	private void initUI() {

		// #[[ UI物件設定
		// 文字框
		this.setLayout(new BorderLayout());
		messageArea = new JTextArea();
		this.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		// 按鈕
		button = new JButton("測試寫入&取得資料");
		button.addActionListener(this);
		this.add(button, BorderLayout.SOUTH);
		// ]]

		// #[[ 其他基本設定
		this.setSize(400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// ]]

		showMessage("初始化UI介面完成.");
	}

	/**
	 * 初始化整個程式架構(使用Spring Framework)
	 * 
	 * @return controller 初始化完後回傳UI用的controller
	 * */
	@SuppressWarnings("resource")
	private Controller initSpringConstruct() {
		showMessage("初始化Spring架構...");
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"./spring-config.xml");
		controller = (Controller) context.getBean("Controller");
		showMessage("初始化Spring架構完成.");
		return controller;
	}

	private void initXmlSetting(){
		try {
			showMessage("讀取Xml設定檔...");
			// 讀取XML檔案
			File xmlFile = new File("./conf.xml");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(xmlFile);
			// 讀取各設定
			GlobalConfig.conf01 = doc.getElementsByTagName("conf01").item(0).getFirstChild().getNodeValue();
			showMessage("conf01 : " + GlobalConfig.conf01);
			GlobalConfig.conf02 = doc.getElementsByTagName("conf02").item(0).getFirstChild().getNodeValue();
			showMessage("conf02 : " + GlobalConfig.conf02);

			showMessage("讀取Xml設定完成.");
		} catch (Exception e) {
			showMessage("讀取Xml設定失敗.");
			e.printStackTrace();
		}
	}
	/** 訊息視窗呈現 */
	private void showMessage(String message) {
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button) {
			showMessage("測試寫入&讀取DB資料...");
			showMessage(controller.testProcess());
			showMessage("寫入&讀取完成.");
		}
	}
}
