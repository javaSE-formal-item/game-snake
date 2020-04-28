package com.wch.snake;

import java.awt.Color;

import javax.swing.JFrame;

/**
 * @author CH W
 * @description	贪吃蛇游戏窗口
 * @date 2019年12月10日 下午3:07:45
 * @version 1.0
 */
public class SnakeWindow extends JFrame {
	private static final long serialVersionUID = 7714637909406998675L;

	public SnakeWindow() {
		this.setTitle("贪吃蛇");
		this.setBounds(700, 240, 1206, 781);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		SnakePanel snakePanel = new SnakePanel();
		snakePanel.setBackground(Color.black);
		this.add(snakePanel);
		
		this.setVisible(true);
		snakePanel.setFoodPoint();
	}

}
