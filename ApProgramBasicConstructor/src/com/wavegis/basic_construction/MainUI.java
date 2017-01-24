package com.wavegis.basic_construction;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.wavegis.engin.Engin;
import com.wavegis.engin.EnginView;
import com.wavegis.global.EnginCenter;
import com.wavegis.global.GlobalConfig;
import com.wavegis.global.tools.LogTool;

@SuppressWarnings("serial")
public class MainUI extends JFrame implements ActionListener {

	private static final int frameWidth = 600;
	private static final int frameHeight = 600;
	private EnginView[] enginViews = EnginCenter.EnginViews;
	private JTextArea messageArea;
	private JButton startAllButton, stopAllButton;
	private JTabbedPane mainEnginViewPanel;
	private Logger logger;
	private MainUI instance;
	private TrayIcon tray_icon; // Tray 的操作功能
	private SystemTray tray;
	private String edition = "Spring+Mybatis程式基本版";

	@Autowired
	private Controller controller;

	public MainUI() {
		super();
		instance = this;
	}

	public void start(String edition) {
		this.edition = edition;
		initUI();
		initTray(GlobalConfig.CONFPIG_PROPERTIES.getProperty("TrayPassword"), edition);
		initIcon();
		setCloseConfirm();
	}

	/** 初始化UI介面 */
	private void initUI() {

		// #[[ UI物件設定
		this.setTitle(edition);
		this.setLayout(new BorderLayout());
		// 主文字&button框
		JPanel mainButtonsPanel = new JPanel();
		mainButtonsPanel.setLayout(new GridLayout(2, 1));
		startAllButton = new JButton("開啟所有Engin");
		startAllButton.addActionListener(this);
		mainButtonsPanel.add(startAllButton);
		stopAllButton = new JButton("關閉所有Engin");
		stopAllButton.addActionListener(this);
		mainButtonsPanel.add(stopAllButton);
		JPanel mainMessagePanel = new JPanel();
		mainMessagePanel.setLayout(new BorderLayout());
		messageArea = new JTextArea(5, 10);
		mainMessagePanel.add(new JScrollPane(messageArea), BorderLayout.CENTER);
		mainMessagePanel.add(mainButtonsPanel, BorderLayout.WEST);

		this.add(mainMessagePanel, BorderLayout.NORTH);
		// 各Engin控制 & View
		mainEnginViewPanel = new JTabbedPane();
		for (Engin engin : EnginCenter.Engins) {
			JPanel enginPanel = new JPanel();
			enginPanel.setLayout(new BorderLayout());
			JButton enginButton = new JButton(engin.getEnginName() + "開關");
			enginButton.setActionCommand(engin.getEnginID());
			enginButton.addActionListener(this);
			enginPanel.add(enginButton, BorderLayout.SOUTH);

			JPanel enginViewPanel = null;
			for (EnginView enginView : enginViews) {
				if (enginView.getEnginID().equals(engin.getEnginID())) {
					enginViewPanel = (JPanel) enginView;
					break;
				}
			}
			if (enginViewPanel != null) {
				enginPanel.add(enginViewPanel, BorderLayout.CENTER);
			} else {
				enginPanel.add(new JPanel(), BorderLayout.CENTER);// 沒有相對應的viewPanel
																	// 產生一個填補
			}
			mainEnginViewPanel.add(enginPanel, engin.getEnginName());
		}
		this.add(mainEnginViewPanel);

		// ]]

		// #[[ 其他基本設定
		initLogger();
		this.setSize(frameWidth, frameHeight);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		// ]]

		showMessage("初始化UI介面完成.");
	}

	private void initLogger() {
		logger = LogTool.getLogger(MainUI.class.getName());
	}

	/** 點選右上叉叉時顯現確認訊息 */
	private void setCloseConfirm() {
		// 系統關閉按鈕
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Object[] options = new Object[] { "結束", "縮小到系統列" };
				int result = JOptionPane.showOptionDialog(null, "確定要結束程式嗎？", "詢問", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
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

				MenuItem tray_restore_item = new MenuItem("Restore");// 在 Tray下的
																		// 指令 復原
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
				JOptionPane.showMessageDialog(null, "部分支援工具遺失!"); // System Tray
																	// is not
																	// supported
			}
		} catch (AWTException e) {
			e.printStackTrace();
			System.err.println("TrayIcon could not be added.");
		}
	}

	private void initIcon() {
		this.setIconImage(GlobalConfig.FrameIconImage.getImage());
	}

	/** 訊息視窗呈現 */
	private void showMessage(String message) {
		messageArea.append(message + "\n");
		messageArea.setCaretPosition(messageArea.getText().length());
		logger.info(message);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == startAllButton) {
			showMessage("開啟所有Engin...尚未完成");
		} else if (e.getSource() == stopAllButton) {
			showMessage("關閉所有Engin...尚未完成");
		} else {
			String actionCommand = e.getActionCommand();
			boolean isEnginStarted = controller.isEnginStarted(actionCommand);
			if (isEnginStarted) {
				controller.stopEngin(actionCommand);
			} else {
				controller.startEngin(actionCommand);
			}
		}
	}

}
