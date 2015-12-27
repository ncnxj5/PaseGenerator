package gui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Player extends JPanel {
//public class Player extends JScrollPane {
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
	//private JButton searchJButton = new JButton();
	
	// 切换到球队界面按钮
	//private JButton changeToTeamPanel = new JButton();
	// 切换到比赛界面按钮
	//private JButton changeToMatchPanel = new JButton();
	// 切换到比赛界面按钮
		//private JButton changeToStatPanel = new JButton();
		private double leftX = 100.0;
		private double topY =500.0 ;
		private double W = 50.0;
		private double H = 50.0;
		private double MovLen = 5.0;
		private Line2D line =new Line2D.Double(leftX,topY,W,H);

		public ArrayList<LinePosition>lines;
		public int y1= 0;
		public int x2= 0;
		public int y2= 0;
		
	class LinePosition{
		int x1;
		int y1;
		int x2;
		int y2;
		public LinePosition(int x1,int y1,int x2,int y2){
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
	}
	//public Player(JScrollPane lastJPanel, Frame current)  {
	public Player(JPanel lastJPanel, Frame current)  {
		setVisible(true);
		setLayout(null);
		this.currentFrame = current;
		this.lastJPanel = lastJPanel;
		this.currentJpanel = lastJPanel;
//		try {
//			playerBLService=new PlayerBLImp();
//		} catch (DatabaseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		this.setBackground(new java.awt.Color(0x2d2d2d));
		lines = new ArrayList<LinePosition>(); 
		initialize();

	}

	private Frame getContentPane() {
		// TODO Auto-generated method stub
		return null;
	}

	private void initialize() {
		//这里可以就是初始化这个jpanel
		this.setSize(1400, 760);
		//this.getGraphics().drawLine(0, 0,100, 100);
  	
	}

	public void addLine(int x1,int y1,int x2,int y2){
		LinePosition cntLine = new LinePosition(x1,y1,x2,y2);
		lines.add(cntLine);
		//this.paintComponent(g);
		//this.paintComponent(g,cntLine);
	}
	public void drawLines(Graphics g){
		this.paintComponent(g);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
//		ImageIcon backOfoneTextForTongj3= new ImageIcon("1.jpg");
//		g.drawImage(backOfoneTextForTongj3.getImage(), 0, 0, null);
		g2 = (Graphics2D)g;
		//Line2D cntLine =new Line2D.Double(leftX,topY,W,H);
		//g2.draw(cntLine);
		for(int i=0;i<lines.size();i++){
			LinePosition cntLinePosition = lines.get(i);
			Line2D cntLine =new Line2D.Double(
					cntLinePosition.x1,cntLinePosition.y1,
					cntLinePosition.x2,cntLinePosition.y2);
			g2.draw(cntLine);
		}
	}
	
}

