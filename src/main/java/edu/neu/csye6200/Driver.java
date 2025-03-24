package edu.neu.csye6200;

import javax.swing.*;

/**
 * 
 * @author Ruchika Sharma
 * 
 */

public class Driver {
	public static void main(String[] args) {
		System.out.println("============Main Execution Start===================\n\n");

         //Add your code in between these two print statements
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Login(); // 创建并显示登录界面
			}
		});

		System.out.println("\n\n============Main Execution End===================");
	}

}
