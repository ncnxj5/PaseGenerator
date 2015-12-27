package gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.filechooser.FileNameExtensionFilter;

import TreeGenerator.MyNewGrammar;
import TreeGenerator.MyNewGrammarTreeConstants;
import TreeGenerator.Node;
import TreeGenerator.ParseException;
import TreeGenerator.SimpleNode;
import linker.Utils;
import mytree.MyTree;
import mytree.ExpNode;

public class MyGUI {
	
	public static MyFrame mJFrame = null;
	public static Player jPlayer = null;
	public static MyJPanel treeJpanel= null;
	public static MyJPanel streamJpanel= null;
	public static MyJPanel consoleJpanel= null;
	public static MyJPanel codeJpanel= null;
	public static JTextArea codeText = null;
	public static JTextArea consoleText = null;
	public static JTextArea streamText = null;
	public static BufferedInputStream inputStream = null;
	
	class FileChooseAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
			JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "txt", "bom");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(new JPanel());
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		       System.out.println("你打开的文件是: " +
		            chooser.getSelectedFile().getName());
		       codeFile = chooser.getSelectedFile();
		    } 
		    else
		    	return;
		    Reader reader = null;
		    StringBuffer codeString = new StringBuffer();
	        try {
	            reader = new InputStreamReader(new FileInputStream(codeFile));
	            int tempchar;
	            while ((tempchar = reader.read()) != -1) {
	                if (((char) tempchar) != '\r') {
	                    codeString.append((char)tempchar);
	                }
	            }
	            reader.close();
	            codeText.setText(codeString.toString());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}

	class CompilerAction implements ActionListener{
		public void actionPerformed(ActionEvent event){
		    InputStream inputStream = Utils.getStringStream(codeText.getText()+"@BOOM!");
			MyNewGrammar myNewGrammar = new MyNewGrammar(inputStream,null);
			try {
				myNewGrammar.compilerCall();
			} 
			catch (Exception e) {
				consoleText.append("\r\n"+e.getMessage());
			}
		}
	}
	
	public File codeFile = null;
	
	public MyTree myTree = null;
	public static ExpNode oriRoot = null;
	public int cntTreeHir = 0;
	public static double minLocX = 0;
	public static double minSize = 1;
	
	public static GraphicTree graphicTree;
	public SimpleNode mRoot = null;
	public static ArrayList<ArrayList<ExpNode>>hiyNodes = null;
	public ArrayList<Integer>hiyLayerWidth = new ArrayList<Integer>();
	public String hiyLineString[] = null;
	public String hiyConnectString[] = null;
	public String hiyBrankString[] = null;
	public int[] lineX;
	public int[] lineY;
	public static ArrayList<ExpNode>rootList = new ArrayList<ExpNode>();
	public static ArrayList<ArrayList<String>> streamList = new ArrayList<ArrayList<String>>();
	
	public MyGUI(){
		oriRoot = new ExpNode(0, "BOOMROOT",null,"");
	}
	public static void addRoot(SimpleNode root){
		ExpNode cntRootExpNode = SimpleNode2ExpNode(root);
		System.out.println(cntRootExpNode.content+" at here");
		oriRoot.expChildren.add(cntRootExpNode);
		rootList.add(cntRootExpNode);
	}
	
	public static void addStream(ArrayList<String> stream){
		ArrayList<String> cntStirngs = new ArrayList<String>();
		for(int i =0;i<stream.size();i++){
			cntStirngs.add(stream.get(i));
			System.out.println(cntStirngs);
		}
		MyGUI.streamList.add(cntStirngs);
	}
	
	double rootAlign = 0;
	
	public static void makeGraphicTree(){
		try{
			setNodeAlign(graphicTree.root,0,1);
			//LET a AS 2+3!
			drawTree();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public static ExpNode getTree(){
		return MyGUI.oriRoot;
	}
	
	public static void setNodeAlign(ExpNode node,double leftAlign,double parentRevised){
		for(int i =0;i<node.expChildren.size();i++){
			int cntHierarchy = node.expChildren.get(i).hierarchy;
			int parentWidth = 1;
			if(cntHierarchy>0)
				parentWidth = graphicTree.hiyWidth.get(cntHierarchy-1);
			int cntInterWidth = graphicTree.hiyWidth.get(cntHierarchy);
			double revised = parentRevised;
			if(parentWidth<cntInterWidth)
				revised*=(0.5*0.6);
			if(parentWidth>cntInterWidth)
				revised*=(2.0*1.3);
			//if(revised<parentRevised)
			//System.out.println("revised "+revised+" par "+parentWidth+" cnt "+cntInterWidth);
			if(node.expChildren.get(i)!=null){
				ExpNode cntNode = node.expChildren.get(i);
				if(node.expChildren.size()>1){
					if(node.expChildren.size()%2==1){
						if(i<node.expChildren.size()/2)
							cntNode.locationX = leftAlign - (double)(node.expChildren.size()/2-i)*revised;
						else
							cntNode.locationX = leftAlign + (double)(i - (node.expChildren.size()/2))*revised;
					}
					else{
						if(i<node.expChildren.size()/2)
							cntNode.locationX = leftAlign - (double)(node.expChildren.size()/2-i)*revised;
						else
							cntNode.locationX = leftAlign + (double)(i - (node.expChildren.size()/2-1))*revised;
					}
				}
				else
					cntNode.locationX = leftAlign;
				if(cntNode.locationX<0 && cntNode.locationX<minLocX)
					minLocX = cntNode.locationX;
				if(revised<minSize)
					minSize = revised;
				setNodeAlign(cntNode,cntNode.locationX,revised);
			}
		}
	}
	
	public void drawGUI(){
		MyGUI.mJFrame = new MyFrame(700,1400);
		mJFrame.setTitle("BOOM IT");
		mJFrame.setLocation(10,10);
		mJFrame.setLayout(null);

          

		MyGUI.jPlayer = (Player) (mJFrame).getCurrentPanel();

		
		JButton comButton = new JButton("Compile & Run"); 
		comButton.setLocation(260, 380);
		comButton.setBounds(565, 210, 150, 30);
		

		comButton.setContentAreaFilled(false);
		comButton.setForeground(new java.awt.Color(0xc8c8c8));
		
		comButton.addActionListener(new CompilerAction());
	    MyGUI.jPlayer.add(comButton);

		JButton importButton = new JButton("choose file"); 
		importButton.setLocation(240, 380);
		importButton.setBounds(20, 210, 150, 30);
		
		importButton.setContentAreaFilled(false);
		importButton.setForeground(new java.awt.Color(0xc8c8c8));
		
		importButton.addActionListener(new FileChooseAction());
	    MyGUI.jPlayer.add(importButton);
	    
	    drawConsolePanel();
	    drawCodePanel();
		drawStreamPanel();
		drawTreePanel();
		//drawTree();
	}
	
	public void drawConsolePanel(){
		MyGUI.consoleJpanel=new MyJPanel();
		JScrollPane jjp_streamaddGrammar=new JScrollPane(MyGUI.consoleJpanel);
		
		jjp_streamaddGrammar.setBounds(20, 20, 700, 175);
		jjp_streamaddGrammar.getVerticalScrollBar().setUnitIncrement(20);
		jjp_streamaddGrammar.getHorizontalScrollBar().setUnitIncrement(20);
		
		//ui
		jjp_streamaddGrammar.getHorizontalScrollBar().setFont(null);
		jjp_streamaddGrammar.getHorizontalScrollBar().setForeground(new java.awt.Color(0x1e1e1e));
		jjp_streamaddGrammar.getHorizontalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		jjp_streamaddGrammar.getVerticalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		
		MyGUI.consoleJpanel.setPreferredSize(new Dimension(1000,6000));
		MyGUI.consoleJpanel.setLayout(null);
		consoleText = new JTextArea("");
		
		consoleText.setForeground(new java.awt.Color(0xc8c8c8));
		consoleText.setBackground(new java.awt.Color(0x1e1e1e));
		Font x = new Font("Dialog",0,15);    
		consoleText.setFont(x);
		//consoleText.setOpaque(false);
		
		consoleText.setBounds(0, 0, 6000,6000);
		MyGUI.consoleJpanel.add(consoleText);

		MyGUI.jPlayer.add(jjp_streamaddGrammar);
		MyGUI.jPlayer.updateUI();
	}
	public void drawCodePanel(){
		MyGUI.codeJpanel=new MyJPanel();
		JScrollPane jjp_addGrammar=new JScrollPane(MyGUI.codeJpanel);
		jjp_addGrammar.setBounds(20, 250, 700, 400);
		jjp_addGrammar.getVerticalScrollBar().setUnitIncrement(20);
		
		jjp_addGrammar.getVerticalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		jjp_addGrammar.getHorizontalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		
		/*
		actionlistener{
			private static final long serialversionuid = 1l;
		jtextarea textarea = new jtextarea();
		jmenu formatmenu = new jmenu("格式");//格式菜单//menuitem
		}*/
		
		MyGUI.codeJpanel.setPreferredSize(new Dimension(20*30,20*100));
		MyGUI.codeJpanel.setLayout(null);
		
		codeText = new JTextArea("/*here is your code*/");
		
		codeText.setForeground(new java.awt.Color(0xc8c8c8));
		codeText.setBackground(new java.awt.Color(0x1e1e1e));
		Font x = new Font("Dialog",0,18);    
		codeText.setFont(x);
		
		codeText.setBounds(0, 0, 700,2000);
		MyGUI.codeJpanel.add(codeText);
		MyGUI.jPlayer.add(jjp_addGrammar);
		MyGUI.jPlayer.updateUI();
	}
	
	public void drawStreamPanel(){
		MyGUI.streamJpanel=new MyJPanel();
		JScrollPane jjp_streamaddGrammar=new JScrollPane(MyGUI.streamJpanel);
		jjp_streamaddGrammar.setBounds(750, 20, 620, 210);
		jjp_streamaddGrammar.getVerticalScrollBar().setUnitIncrement(20);

		jjp_streamaddGrammar.getVerticalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		jjp_streamaddGrammar.getHorizontalScrollBar().setBackground(new java.awt.Color(0x3e3e42));
		
		MyGUI.streamJpanel.setPreferredSize(new Dimension(640,300));
		MyGUI.streamJpanel.setLayout(null);
		
		streamText = new JTextArea("");
		streamText.setBounds(0, 0, 640,300);
		
		streamText.setForeground(new java.awt.Color(0xc8c8c8));
		streamText.setBackground(new java.awt.Color(0x1e1e1e));
		
		Font x = new Font("Dialog",0,15);
		streamText.setFont(x);
		
		
		MyGUI.streamJpanel.add(streamText);
		MyGUI.jPlayer.add(jjp_streamaddGrammar);
		MyGUI.jPlayer.updateUI();
	}

	public void drawTreePanel(){
		MyGUI.treeJpanel=new MyJPanel();
		JScrollPane jjp_addGrammar=new JScrollPane(MyGUI.treeJpanel);
		jjp_addGrammar.setBounds(750, 250, 620, 400);
		jjp_addGrammar.getVerticalScrollBar().setUnitIncrement(20);
		MyGUI.treeJpanel.setBackground(java.awt.Color.WHITE);
		
		MyGUI.treeJpanel.setPreferredSize(new Dimension(20*100*10,20*100));
		MyGUI.treeJpanel.setLayout(null);
		
		MyGUI.jPlayer.add(jjp_addGrammar);
		MyGUI.jPlayer.updateUI();
	}
	/*
	 * After set nodes, call function will draw a tree
	 */
	public static void drawTree(){
		//int row =graphicTree.height;
		//int col =graphicTree.maxWidth;
		//MyFrame mJFrame = new MyFrame(row*100,col*100*4);
		//Player jPlayer = (Player) (this.mJFrame).getCurrentPanel();
		
		//this.jPlayer.updateUI();
		treeJpanel.removeAll();
		treeJpanel.removeLines();
		MyGUI.jPlayer.updateUI();
		drawSteam(streamJpanel);
		drawAST(treeJpanel,(int) (5.0/minSize),50);
	}
	public static void drawSteam(MyJPanel streamjpanel){
		String streamString ="STEAM IS: \n";
		for(int i = 0; i<streamList.size();i++){
			for(int j=0;j<streamList.get(i).size();j++){
				streamString+=(" "+streamList.get(i).get(j));
			}
			streamString+="\n  ";
		}
		streamText.setText(streamString);
	}
	
	public static void drawAST(MyJPanel jpanel,int borderWidth, int moveY){
		//System.out.println("yes ok");
		for(int i =0;i<hiyNodes.size();i++){
			for(int j=0;j<hiyNodes.get(i).size();j++){
				ExpNode cntNode = hiyNodes.get(i).get(j);
				Label label = new Label(cntNode.content);
				jpanel.add(label);
				label.setBounds((int)((cntNode.locationX-minLocX)*borderWidth+100),moveY+i*50, 50, 20);
				//label.setBounds((int)(cntNode.locationX*(double)graphicTree.maxWidth/cntInterWidth*50+col*100),moveY+i*50, 50, 20);
				for(int index = 0;index<cntNode.expChildren.size();index++){
					jpanel.addLine(
							(int)((cntNode.locationX-minLocX)*borderWidth+100),moveY+i*50, 
							(int)((cntNode.expChildren.get(index).locationX-minLocX)*borderWidth+100),moveY+(i+1)*50);
				}
			}
		}
	}
	
	public static ExpNode SimpleNode2ExpNode(SimpleNode simpleRoot){
		ExpNode rootNode = new ExpNode(1, "",oriRoot,"");
		shiftOutput(rootNode, simpleRoot, 1, null);
		if(rootNode.expChildren!=null){
			printNode(rootNode);
		}
		return rootNode;
	}
	
	public static void printNode(ExpNode node){
		if(node.expChildren!=null){
			for(int i=0;i<node.expChildren.size();i++){
				printNode(node.expChildren.get(i));
			}
		}
	}
	public static void shiftOutput(ExpNode expRoot, SimpleNode node,int cntHierarchy,ExpNode parentNode){
		try {
			ExpNode cntNode = new ExpNode(cntHierarchy, MyNewGrammarTreeConstants.jjtNodeName[node.id],parentNode,node.m_Text);
			if(parentNode!=null)
				parentNode.expChildren.add(cntNode);
			else{
				expRoot.content = MyNewGrammarTreeConstants.jjtNodeName[node.id];
				cntNode = expRoot;
			}
			
			Node[] children = node.children;
			if (children != null) {
			      for (int i = 0; i < children.length; ++i){
				      SimpleNode n = (SimpleNode)children[i];
				      if (n != null) {
				    	  shiftOutput(expRoot,n,cntHierarchy+1,cntNode);
				      }
			      }
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//
	public static void outputTree(){
		graphicTree = new GraphicTree();
		graphicTree.initGraphicTree();
		hiyNodes = new ArrayList<ArrayList<ExpNode>>();
		graphicTree.root = oriRoot;
		setGraphicHiy(oriRoot,0);
		makeGraphicTree();
	}
	
	public static void setGraphicHiy(ExpNode node,int cntHierarchy){
		//LET a AS 1!
		//FileWriter fileWriter;
		try {
			if(hiyNodes.size()<=cntHierarchy){
				hiyNodes.add(cntHierarchy, new ArrayList<ExpNode>());
			}
			hiyNodes.get(cntHierarchy).add(node);
			
			if(graphicTree.height<cntHierarchy){
				graphicTree.height=cntHierarchy;
				graphicTree.hiyWidth.add(cntHierarchy, 1);
			}
			else{
				int hiyWidth = graphicTree.hiyWidth.get(cntHierarchy)+1;
				graphicTree.hiyWidth.set(cntHierarchy,hiyWidth);
				if(hiyWidth>graphicTree.maxWidth)
					graphicTree.maxWidth=hiyWidth;
			}
			ArrayList<ExpNode> children = node.expChildren;
			if (children != null) {
				//System.out.println("here in "+children.size());
			      for (int i = 0; i < children.size(); ++i){
			    	  ExpNode n = (ExpNode)children.get(i);
			    	  //System.out.println(n.content);
				      if (n != null) {
				    	  //System.out.println("here ininLET a AS 1!@BOOM!");
				    	  setGraphicHiy(n,cntHierarchy+1);
				      }
			      }
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	public static void clearAll(){
		oriRoot = new ExpNode(0, "BOOMROOT",null,"");
		rootList = new ArrayList<ExpNode>();
		streamList = new ArrayList<ArrayList<String>>();
	}
}
