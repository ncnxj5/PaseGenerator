package gui;

import java.awt.Dimension;
import java.awt.Label;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import TreeGenerator.MyNewGrammarTreeConstants;
import TreeGenerator.Node;
import TreeGenerator.SimpleNode;
import mytree.MyTree;
import mytree.ExpNode;

public class MyGUI {
	
	class GraphicTree{
		public ArrayList<Integer>hiyWidth = new ArrayList<Integer>();
		public int height;
		public int maxWidth;
		public ExpNode root;

		public GraphicTree(){
			this.height = -1;
			this.maxWidth = -1;
			this.root = null;
		}	
	}
	
	public MyTree myTree = null;
	public ExpNode oriRoot = null;
	public int cntTreeHir = 0;
	public double minLocX = 0;
	public double minSize = 1;
	
	public GraphicTree graphicTree = null;
	public SimpleNode mRoot = null;
	public ArrayList<ArrayList<ExpNode>>hiyNodes = null;
	public ArrayList<Integer>hiyLayerWidth = new ArrayList<Integer>();
	public String hiyLineString[] = null;
	public String hiyConnectString[] = null;
	public String hiyBrankString[] = null;
	public int[] lineX;
	public int[] lineY;
	public ArrayList<ExpNode>rootList = new ArrayList<ExpNode>();
	public ArrayList<ArrayList<String>> streamList = new ArrayList<ArrayList<String>>();
	
	public MyGUI(){
		oriRoot = new ExpNode(0, "BOOMROOT",null,"");
	}
	public void addRoot(SimpleNode root){
		ExpNode cntRootExpNode = SimpleNode2ExpNode(root);
		System.out.println(cntRootExpNode.content+" at here");
		oriRoot.expChildren.add(cntRootExpNode);
		this.rootList.add(cntRootExpNode);
	}
	
	public void addStream(ArrayList<String> stream){
		ArrayList<String> cntStirngs = new ArrayList<String>();
		for(int i =0;i<stream.size();i++){
			cntStirngs.add(stream.get(i));
			System.out.println(cntStirngs);
		}
		this.streamList.add(cntStirngs);
	}
	
	double rootAlign = 0;
	
	public void makeGraphicTree(){
		try{
			setNodeAlign(graphicTree.root,0,1);
			//LET a AS 2+3!
			drawTree();
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public ExpNode getTree(){
		return this.oriRoot;
	}
	
	public void setNodeAlign(ExpNode node,double leftAlign,double parentRevised){
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
	/*
	 * After set nodes, call function will draw a tree
	 */
	public void drawTree(){
		int moveY = 50;
		int row =graphicTree.height;
		int col =graphicTree.maxWidth;
		System.out.println(row+ " "+col);
		//MyFrame mJFrame = new MyFrame(row*100,col*100*4);
		MyFrame mJFrame = new MyFrame(700,700);
		mJFrame.setLocation(200,200);
		
		mJFrame.setLayout(null);
		Player jPlayer = (Player) mJFrame.getCurrentPanel();

		MyJPanel streamjpanel=new MyJPanel();
		JScrollPane jjp_streamaddGrammar=new JScrollPane(streamjpanel);
		jjp_streamaddGrammar.setBounds(50, 50, 600, 200);
		jjp_streamaddGrammar.getVerticalScrollBar().setUnitIncrement(20);
		streamjpanel.setPreferredSize(new Dimension(10000,200));
		streamjpanel.setLayout(null);
		
		MyJPanel jpanel=new MyJPanel();
		JScrollPane jjp_addGrammar=new JScrollPane(jpanel);
		jjp_addGrammar.setBounds(50, 250, 600, 400);
		jjp_addGrammar.getVerticalScrollBar().setUnitIncrement(20);
		jpanel.setPreferredSize(new Dimension(col*100*10,row*100));
		jpanel.setLayout(null);

		jPlayer.add(jjp_addGrammar);
		jPlayer.add(jjp_streamaddGrammar);
		jPlayer.updateUI();
		
		String streamString ="STEAM IS: \n";
		for(int i = 0; i<this.streamList.size();i++){
			for(int j=0;j<this.streamList.get(i).size();j++){
				streamString+=(" "+streamList.get(i).get(j));
			}
			streamString+="\n  ";
		}
		JTextArea streamlabel = new JTextArea(streamString);
		streamjpanel.add(streamlabel);
		streamlabel.setBounds(10, 10, graphicTree.maxWidth*400,400);

		int borderWidth = (int) (20.0/minSize);
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
	
	public ExpNode SimpleNode2ExpNode(SimpleNode simpleRoot){
		ExpNode rootNode = new ExpNode(1, "",oriRoot,"");
		shiftOutput(rootNode, simpleRoot, 1, null);
		if(rootNode.expChildren!=null){
			printNode(rootNode);
		}
		return rootNode;
	}
	
	public void printNode(ExpNode node){
		if(node.expChildren!=null){
			for(int i=0;i<node.expChildren.size();i++){
				printNode(node.expChildren.get(i));
			}
		}
	}
	public void shiftOutput(ExpNode expRoot, SimpleNode node,int cntHierarchy,ExpNode parentNode){
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
	public void outputTree(){
		graphicTree = new GraphicTree();
		hiyNodes = new ArrayList<ArrayList<ExpNode>>();
		graphicTree.root = oriRoot;
		setGraphicHiy(oriRoot,0);
		makeGraphicTree();
	}
	
	public void setGraphicHiy(ExpNode node,int cntHierarchy){
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
}
