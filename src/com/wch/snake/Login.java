package com.wch.snake;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.mysql.cj.util.StringUtils;

/**
 * @author CH W
 * @description	游戏登录窗口
 * @date 2019年12月26日 上午10:24:14
 * @version 1.0
 */
public class Login extends JFrame {
	private static final long serialVersionUID = -1285781375669118425L;
	
	private static final int login_width = 400, login_height = 300;		//	登录窗口尺寸
	private int account_max_len = 10, pwd_max_len = 15, account_min_len = 3, pwd_min_len = 6;		//	账号、密码长度限制
	ImageIcon bg_img = new ImageIcon("image/login_bg.jpg");		//	背景图
	private JLabel bg_label = new JLabel(bg_img);		//	窗口背景label
	private JLabel account_lb = new JLabel("账号：");		//	账号标题
	private JTextField account_tf = new JTextField(10);		//	账号输入框
	private JLabel account_warn = new JLabel();		//	账号输入异常提示
	private JLabel pwd_lb = new JLabel("密码：");		//	密码标题
	private JPasswordField pwd_pf = new JPasswordField(10);		//	密码输入框
	private JLabel pwd_warn = new JLabel();		//	密码输入异常提示
	private JCheckBox pwd_cb = new JCheckBox("记住密码", false);		//	是否记住密码
	private JButton login_bt = new JButton("登录");		//	登录按钮
	private boolean show_login_btn = false;		//	登录按钮隐藏、显示（账号、密码长度校验通过才显示登录按钮）

	public static void main(String[] args) {
		Login login = new Login("贪吃蛇登录");
		login.addListener(login);
	}

	public Login(String windowTitle) {
		bg_label.setBounds(0, 0, bg_img.getIconWidth(), bg_img.getIconHeight());
		this.getLayeredPane().add(bg_label, new Integer(Integer.MIN_VALUE));
		/**
		 * --设置内容面板透明度
		 */
		JPanel panel = (JPanel)this.getContentPane();
		panel.setOpaque(false);
		panel.setLayout(null);
		/**
		 * --窗口内组件位置及尺寸
		 */
		account_warn.setBounds(140, 40, 260, 30);
		account_warn.setForeground(Color.RED);
		account_lb.setBounds(100, 70, 50, 30);
		account_tf.setBounds(140, 70, 150, 30);
		pwd_warn.setBounds(140, 100, 150, 30);
		pwd_warn.setForeground(Color.RED);
		pwd_lb.setBounds(100, 130, 50, 30);
		pwd_pf.setBounds(140, 130, 150, 30);
		pwd_cb.setBounds(140, 160, 80, 30);
		pwd_cb.setContentAreaFilled(false);
		login_bt.setBounds(160, 195, 80, 30);
		login_bt.setVisible(false);
		/**
		 * --添加所有组件
		 */
		panel.add(account_lb);
		panel.add(account_tf);
		panel.add(account_warn);
		panel.add(pwd_lb);
		panel.add(pwd_pf);
		panel.add(pwd_warn);
		panel.add(pwd_cb);
		panel.add(login_bt);
		/**
		 * --获取屏幕分辨率
		 */
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int login_x = (screenSize.width - login_width) / 2;
		int login_y = (screenSize.height - login_height) / 2;
		/**
		 * --设置登录窗口位置及尺寸
		 */
		this.setTitle(windowTitle);
		this.setBounds(login_x, login_y, login_width, login_height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
	}
	/**
	 * --登录界面的各种监听
	 * @param login
	 */
	public void addListener(Login login) {
		account_tf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				login.inputMaxLenVerify(account_tf.getText(), null);
			}
			@Override
			public void removeUpdate(DocumentEvent e) {}
			@Override
			public void changedUpdate(DocumentEvent e) {}
		});
		account_tf.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				String account = account_tf.getText();
				String password = String.valueOf(pwd_pf.getPassword());
				login.focusLostVerifyShowLoginBtn(account, password);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
		pwd_pf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				login.inputMaxLenVerify(null, String.valueOf(pwd_pf.getPassword()));
			}
			@Override
			public void changedUpdate(DocumentEvent e) {}
			@Override
			public void removeUpdate(DocumentEvent e) {}
		});
		pwd_pf.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				String account = account_tf.getText();
				String password = String.valueOf(pwd_pf.getPassword());
				login.focusLostVerifyShowLoginBtn(account, password);
			}
			@Override
			public void focusGained(FocusEvent e) {}
		});
		login_bt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String account = account_tf.getText();
				String password = String.valueOf(pwd_pf.getPassword());
				boolean isExit = new OperationDB().queryAccountIsExit(account, password);
				if(isExit) {
					new SnakeWindow();
					if(pwd_cb.isSelected()) {
						try {
							String canonicalPath = new File("").getCanonicalPath();
							String folder = canonicalPath.substring(0, canonicalPath.lastIndexOf("\\")+1);
							List<String> account_list = Utils.readAccountInfo(new File(folder + "game_account_info"));
							if(account_list!=null && account_list.size()>0) {
								if(account_list.contains(account+"="+password)) {
									account_list.remove(account+"="+password);
								}
							}
							account_list.add(account+"="+password);
							Utils.writeFile(account_list, "game_account_info", folder);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					dispose();
				}else {
					JOptionPane.showMessageDialog(login, "账号或密码错误");
				}
			}
		});
	}
	/**
	 * --数据最大长度校验
	 * @param account
	 * @param password
	 */
	private void inputMaxLenVerify(String account, String password) {
		if(!StringUtils.isNullOrEmpty(account)) {
			if(account.length()>account_max_len) {
				new Thread((Runnable)() ->{
					account_tf.setText(account.substring(0, account_max_len));
					account_tf.setCaretPosition(account_max_len);
				}).start();
				account_warn.setText("账号限制"+account_max_len+"个字符以内");
			}else {
				if(account.length()<account_max_len) {
					account_warn.setText(null);
				}
			}
		}
		if(!StringUtils.isNullOrEmpty(password)) {
			if(password.length()>pwd_max_len) {
				new Thread((Runnable)() ->{
					pwd_pf.setText(password.substring(0, pwd_max_len));
					pwd_pf.setCaretPosition(pwd_max_len);
				}).start();
				pwd_warn.setText("密码限制"+pwd_max_len+"个字符以内");
			}else {
				if(password.length()<pwd_max_len) {
					pwd_warn.setText(null);
					account_warn.setText(null);
				}
			}
		}
	}
	/**
	 * --失去焦点校验数据并显示登录按钮
	 * @param account
	 * @param password
	 */
	private void focusLostVerifyShowLoginBtn(String account, String password) {
		if(account.length()<account_min_len || password.length()<pwd_min_len) {
			show_login_btn = false;
			account_warn.setText("账号、密码分别限制"+account_min_len+"、"+pwd_min_len+"个字符以上");
		}else {
			show_login_btn = true;
			account_warn.setText(null);
		}
		login_bt.setVisible(show_login_btn);
	}
	
}
