package testJar;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


public class Player extends JPanel {

	// 获取配置
	//GameConfig cfg = ConfigFactory.getGameConfig();

//	StatJTable statTable;


	/**
	 * 一些用户界面转换的panel，frame
	 */
	private JPanel nextJpanel;
	private JPanel currentJpanel;
	private Frame currentFrame;
	private JPanel lastJPanel;
	Graphics2D g2;
	/**
	 * 搜索用的小按钮
	 */
	private JButton searchJButton = new JButton();
	
	// 切换到球队界面按钮
	private JButton changeToTeamPanel = new JButton();
	// 切换到比赛界面按钮
	private JButton changeToMatchPanel = new JButton();
	// 切换到比赛界面按钮
		private JButton changeToStatPanel = new JButton();
		private double leftX = 100.0;
		private double topY =500.0 ;
		private double W = 50.0;
		private double H = 50.0;
		private double MovLen = 5.0;
		private Line2D line =new Line2D.Double(leftX,topY,W,H);


	public Player(JPanel lastJPanel, Frame current)  {
		setVisible(true);
		setLayout(null);
		this.currentFrame = current;
		this.lastJPanel = lastJPanel;
		this.currentJpanel = this;
//		try {
//			playerBLService=new PlayerBLImp();
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		initialize();

	}

	private void initialize() {
		//这里可以就是初始化这个jpanel
		this.setSize(1275, 760);
    //  this.getGraphics().drawLine(0, 0,100, 100);
  	
	}

	

	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
//		ImageIcon backOfoneTextForTongj3= new ImageIcon("1.jpg");
//
//		g.drawImage(backOfoneTextForTongj3.getImage(), 0, 0, null);
		g2 = (Graphics2D)g;
		g2.draw(line);
	}
}

