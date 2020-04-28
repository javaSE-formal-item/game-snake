package com.wch.snake;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * @author CH W
 * @description	游戏面板
 * @date 2019年12月10日 下午5:39:34
 * @version 1.0
 */
public class SnakePanel extends JPanel implements KeyListener, ActionListener {
	private static final long serialVersionUID = 1921019502483519295L;
	
	//	本地使用
	ImageIcon sn_bg = new ImageIcon("image/sn_bg.jpg");
	ImageIcon sn_top = new ImageIcon("image/sn_top.png");
	ImageIcon sn_body = new ImageIcon("image/sn_body.png");
	ImageIcon food = new ImageIcon("image/food.png");
	//	打jar包使用
//	ImageIcon sn_bg = new ImageIcon(this.getClass().getClassLoader().getResource("image/sn_bg.jpg"));
//	ImageIcon sn_top = new ImageIcon(this.getClass().getClassLoader().getResource("image/sn_top.png"));
//	ImageIcon sn_body = new ImageIcon(this.getClass().getClassLoader().getResource("image/sn_body.png"));
//	ImageIcon food = new ImageIcon(this.getClass().getClassLoader().getResource("image/food.png"));
	
	boolean isDialog = false;	//	当前是否弹窗状态
	boolean isStarted = false;		//	游戏是否开始
	boolean isFailed = false;		//	游戏是否结束
	int[] sn_top_x = new int[1184];		//	蛇的坐标
	int[] sn_top_y = new int[736];
	int sn_len = 0;		//	当前蛇的长度
	int sn_max_len = 3;		//	游戏最大蛇长度
	int score = 0;		//	分数
	char direction = 'U';	//	当前蛇头方向
	int foodx = 0;		//	食物坐标
	int foody = 0;
	int speed = 1;
	
	int delay = 1000;
	Timer timer = new Timer(delay, this);
	Random random = new Random();
	int panel_width = 0;		//	内容面板宽
	int panel_height = 0;		//	内容面板高
	int wall_sizex = 0;
	int wall_sizey = 0;
	
	/**
	 * --初始化参数
	 */
	public void init() {
		isStarted = false;
		isFailed = false;
		sn_top_x[0] = 400;
		sn_top_y[0] = 304;
		sn_top_x[1] = 400;
		sn_top_y[1] = 320;
		sn_top_x[2] = 400;
		sn_top_y[2] = 336;
		sn_len = 3;
		score = 0;
		direction = 'U';
		speed = 1;
		delay = 1000;
	}
	/**
	 * --设置食物随机出现的坐标点
	 * @param panel_width
	 * @param panel_height
	 */
	public void setFoodPoint() {
		this.panel_width = this.getWidth();
		this.panel_height = this.getHeight();
		wall_sizex = (this.panel_width - 208 - 16) / 16;
		wall_sizey = (this.panel_height - 16) / 16;
		foodx = (random.nextInt((this.panel_width - 208 - 32) / 16) + 1) * 16;
		foody = (random.nextInt((this.panel_height - 16) / 16) + 1) * 16;
		for(int i=0; i<sn_len; i++) {
			if(foodx==sn_top_x[i] && foody==sn_top_y[i]) {
				this.setFoodPoint();
			}
		}
	}
	/**
	 * --构造函数
	 */
	public SnakePanel() {
		this.setFocusable(true);
		this.addKeyListener(this);
		init();
		timer.start();
	}
	/**
	 * --绘制游戏界面
	 */
	@Override
	public void paint(Graphics g) {
		timer.setDelay(delay);
		super.paint(g);
		/**
		 * --画游戏界面图片、图标
		 */
		sn_top.paintIcon(this, g, sn_top_x[0], sn_top_y[0]);
		for(int i=1; i<sn_len; i++) {
			sn_body.paintIcon(this, g, sn_top_x[i], sn_top_y[i]);
		}
		food.paintIcon(this, g, foodx, foody);
		/**
		 * --绘制游戏边框
		 */
		Graphics2D gs_broder = (Graphics2D)g;
		gs_broder.setStroke(new BasicStroke(1.0f));
		gs_broder.setColor(Color.WHITE);
		gs_broder.drawRect(16, 16, this.panel_width-208-32, this.panel_height-32);
		/**
		 * --绘制右边游戏说明及游戏成绩
		 */
		Graphics2D gs_right = (Graphics2D)g;
		gs_right.setColor(Color.GRAY);
		gs_right.fillRect(panel_width-208, 0, 208, panel_height);
		if(!isStarted) {
			g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
			g.setColor(Color.WHITE);
			g.drawString("按空格键开始/暂停游戏", panel_width/4, this.panel_height/2);
		}
		g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		g.setColor(Color.BLACK);
		g.drawString("当前蛇长："+sn_len, panel_width-180, 50);
		g.drawString("最大蛇长："+sn_max_len, panel_width-180, 100);
		g.drawString("分数："+score, panel_width-180, 150);
		g.drawString("移动速度："+speed, panel_width-180, 200);
		
		if(isFailed && !isDialog) {
			isDialog = true;
			JOptionPane.showMessageDialog(this, "Game Over，点确定重玩", "游戏结束", JOptionPane.CANCEL_OPTION);
			init();
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isStarted && !isFailed) {
			/**
			 * --移动蛇身
			 */
			for(int i=sn_len; i>0; i--) {
				sn_top_x[i] = sn_top_x[i - 1];
				sn_top_y[i] = sn_top_y[i - 1];
			}
			/**
			 * --移动蛇头
			 */
			switch (direction) {
				case 'U':
					sn_top_y[0] = sn_top_y[0] - 16;
					break;
				case 'D':
					sn_top_y[0] = sn_top_y[0] + 16;
					break;
				case 'L':
					sn_top_x[0] = sn_top_x[0] - 16;
					break;
				case 'R':
					sn_top_x[0] = sn_top_x[0] + 16;
					break;
			}
			/**
			 * --蛇撞墙，游戏结束
			 */
			if(sn_top_x[0]>(panel_width-208-32) || sn_top_x[0]<16 || sn_top_y[0]>(panel_height-32) || sn_top_y[0]<16) {
				isFailed = true;
			}
			/**
			 * --蛇头撞到蛇身，游戏结束
			 */
			for(int i=1; i<sn_len; i++) {
				if(sn_top_x[0]==sn_top_x[i] && sn_top_y[0]==sn_top_y[i]) {
					isFailed = true;
				}
			}
			/**
			 * --吃到食物
			 */
			if(sn_top_x[0]==foodx && sn_top_y[0]==foody) {
				score = score + 10;
				sn_len++;
				sn_max_len = sn_len > sn_max_len ? sn_len : sn_max_len;
				delay = delay - 20;
				speed = (1000 - delay) / 20 + 1;
				this.setFoodPoint();
			}
		}
		/**
		 * --游戏未结束才继续刷新画面
		 */
		if(!isFailed) {
			repaint();
		}
	}
	/**
	 * --触发按键
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		if(keyCode==KeyEvent.VK_SPACE) {
			isDialog = false;
			isStarted = isStarted ? false : true;
		}
		if(isStarted) {
			if(keyCode==KeyEvent.VK_UP && direction!='D') {
				direction = 'U';
			}else if(keyCode==KeyEvent.VK_DOWN && direction!='U') {
				direction = 'D';
			}else if(keyCode==KeyEvent.VK_LEFT && direction!='R') {
				direction = 'L';
			}else if(keyCode==KeyEvent.VK_RIGHT && direction!='L') {
				direction = 'R';
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
