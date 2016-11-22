package com.kiwi.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.xml.XmlConfigurationFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kiwi.conf.GlobalConfig;
import com.kiwi.controller.Controller;

@SuppressWarnings("serial")
public class StartUI extends JFrame implements ActionListener {

	private JTextArea messageArea;
	private JButton button;
	private Logger logger;
	private StartUI instance;
	private TrayIcon tray_icon; // Tray 的操作功能
	private SystemTray tray;
	private String edition = "Spring+Mybatis程式基本版";

	@Autowired
	private Controller controller;

	public StartUI() {
		super();
		instance = this;
	}

	public void start(String edition) {
		this.edition = edition;
		initUI();
		initTray(GlobalConfig.TrayPassword, edition);
		initIcon();
		setCloseConfirm();
	}

	/** 初始化UI介面 */
	private void initUI() {

		// #[[ UI物件設定
		this.setTitle(edition);
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
		initLogger();
		this.setSize(400, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// ]]

		showMessage("初始化UI介面完成.");
	}

	private void initLogger() {
		try {
			ConfigurationFactory factory = XmlConfigurationFactory.getInstance();
			ConfigurationSource configurationSource = new ConfigurationSource(new FileInputStream(new File("./log4j2.xml")));
			Configuration configuration = factory.getConfiguration(configurationSource);
			LoggerContext context = new LoggerContext("JournalDevLoggerContext");
			context.start(configuration);

			logger = context.getLogger(StartUI.class.getName());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/** 點選右上叉叉時顯現確認訊息 */
	private void setCloseConfirm() {
		// 系統關閉按鈕
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = new Object[] { "結束", "縮小到系統列" };
				int result = JOptionPane.showOptionDialog(null, "確定要結束程式嗎？", "詢問", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				if (result == 0) {
					tray.remove(tray_icon); // 清除系統列圖示
					System.exit(0);
				}
			}
		});
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
	}

	private void initTray(final String password, String trayName) {
		try {
			if (SystemTray.isSupported()) {

				ActionListener exitListener = new ActionListener() {// Tray 操作區
					public void actionPerformed(ActionEvent e) {
						String result = (String) JOptionPane.showInputDialog("Password", "請輸入密碼");
						if (result != null && result.equals(password)) {
							System.exit(0);
						}
					}
				};

				ActionListener restoreListener = new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						instance.setVisible(true);
						int state = instance.getExtendedState();
						state &= ~JFrame.ICONIFIED;
						instance.setExtendedState(state);
					}
				};
				// 雙擊滑鼠, 還原視窗大小
				MouseAdapter mouseAdapter = new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() >= 2) {
							setVisible(true);
						}
					}
				};

				MenuItem tray_exit_item = new MenuItem("Exit");// 在 Tray下的 指令 關閉
				tray_exit_item.addActionListener(exitListener);

				MenuItem tray_restore_item = new MenuItem("Restore");// 在 Tray下的 指令 復原
				tray_restore_item.addActionListener(restoreListener);

				Image image = GlobalConfig.FrameIconImage.getImage();
				PopupMenu tray_popup = new PopupMenu();
				tray_popup.add(tray_exit_item);
				tray_popup.add(tray_restore_item);

				tray_icon = new TrayIcon(image, trayName, tray_popup);// 加入
				tray_icon.addMouseListener(mouseAdapter);
				tray_icon.setImageAutoSize(true);

				tray = SystemTray.getSystemTray(); // 加在主系統的Tray
				tray.add(tray_icon);

			} else {
				JOptionPane.showMessageDialog(null, "部分支援工具遺失!"); // System Tray is not supported
			}
		} catch (AWTException e) {
			e.printStackTrace();
			System.err.println("TrayIcon could not be added.");
		}
	}
	
	private void initIcon(){
		this.setIconImage(GlobalConfig.FrameIconImage.getImage());
	}

	/** 訊息視窗呈現 */
	private void showMessage(String message) {
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
		logger.log(Level.TRACE, message);
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
