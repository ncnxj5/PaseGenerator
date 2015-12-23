package gui;


import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class MyFrame extends JFrame {
	//private static final long serialVersionUID = 1L;
	
	//private final static MyFrame MY_FRAME = new MyFrame();
	//public final static int FRAME_WIDTH = 1000;// 固定窗口的宽
	//public final static int FRAME_HEIGHT = 600;// 固定窗口的高
	// 用于�?测显示器的大�?
	Toolkit toolkit = Toolkit.getDefaultToolkit();
	Dimension screen = toolkit.getScreenSize();
	/*
	public static HotBLService hotBLService;
	public static HotBLService getHotBl(){
		return hotBLService;
	}*/
	// 当前界面的JPanel
	private JPanel currentPanel;
	//private JScrollPane scrollPane;

	public MyFrame(int height,int width) {// 构造器
		this.getIni(height,width);
	}

	// 初始化的方法
	private void getIni(int height,int width) {
		
		//int x_bounds = (screen.width - FRAME_WIDTH) / 2;
		//int y_bounds = (screen.height - FRAME_HEIGHT) / 2 - 12;
		int x_bounds = width;
		int y_bounds = height;

		this.setSize( width, height);// 设置窗口位置和大小
		this.setLocation(x_bounds, y_bounds);
		this.setLayout(null);// 关闭布局管理器
		//this.setUndecorated(true);//使窗口无边框
		this.setBackground(null);//设置背景为空
		this.setResizable(false);//固定窗口大小
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//初始化一个最原始的jpanel
		//currentPanel=new JPanel(null);
		currentPanel=new Player(null,this);

		currentPanel.setVisible(true);
//		StartThread startThread=new StartThread();
//		startThread.start_thread();
		this.add(currentPanel);
		
		// 重画线程
				new Thread("重画线程") {
					public void run() {
						while (true) {
							repaint();
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}.start();
				
				this.setVisible(true);//设置窗口可见
	}
	//传�?�窗口控制的方法
	/*
	public static MyFrame getFrame(){
		return MY_FRAME;
	}*/
	
	//转换当前窗口的方�?
	public void changePanel(Player thePanel){
		thePanel.setLayout(null);
		this.add(thePanel);
		thePanel.setVisible(true);
		
		this.remove(currentPanel);
		currentPanel=thePanel;
		currentPanel.updateUI();	
	}


	//public Player getCurrentPanel() {
	public JPanel getCurrentPanel() {
		return currentPanel;
	}
	
}
