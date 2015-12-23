package shihu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
	
	class ExpNode {
		public int hierarchy;
		public String content;
		public ArrayList<ExpNode>expChildren = null;
		public ExpNode(int hierarchy, String content) {
			this.hierarchy = hierarchy;
			this.content = content;
			this.expChildren = new ArrayList<ExpNode>();
		}
	}
	
	public GraphicTree graphicTree = null;
	public SimpleNode mRoot = null;
	public String hiyLineString[] = null;
	public String hiyConnectString[] = null;
	public String hiyBrankString[] = null;
	public MyGUI(SimpleNode root){
		this.mRoot = root;
	}
	public void setRoot(SimpleNode root){
		try {
			FileWriter fileWriter = new FileWriter(new File("tree.txt"));
			fileWriter.write("");
			fileWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mRoot = root;
	}
	public void makeGraphicTree(){
		try{
			hiyLineString = new String[graphicTree.height+1];
			hiyConnectString = new String[graphicTree.height+1];
			hiyBrankString = new String[graphicTree.height+1];
			for(int i=0;i<graphicTree.height+1;i++){
				hiyLineString[i]=new String("");
				hiyConnectString[i]=new String("");
				hiyBrankString[i]=new String("");
			}
			printNode(graphicTree.root,0);
			//LET a AS 2+3!
			for(int i =0;i<graphicTree.height+1;i++){
				String prefixString ="";
				if(graphicTree.maxWidth*10/2>hiyLineString[i].length())
					for(int j = 0;j<graphicTree.maxWidth*10/2-4;j++){
						prefixString+=" ";
					}
				System.out.println(hiyLineString[i]);
				System.out.println(hiyConnectString[i]);
				if(hiyBrankString[i].length()>1)
					System.out.println(hiyBrankString[i]);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void printNode(ExpNode node,int leftAlign){
		String prefixString = "";
		if(hiyLineString[node.hierarchy].length()<leftAlign*10)
			for(int i = 0;i<leftAlign*10-hiyLineString[node.hierarchy].length();i++)
				prefixString+=" ";
		hiyLineString[node.hierarchy]+=(prefixString+node.content);

		prefixString="";
		for(int i = hiyConnectString[node.hierarchy].length();i<hiyLineString[node.hierarchy].length()-node.content.length()+1;i++)
			prefixString+=" ";
		if(node.expChildren.size()>0){
			hiyConnectString[node.hierarchy]+=(prefixString+"|");
			
			if(node.expChildren.size()>1){
				String brankLineString = "";
				prefixString="";
				for(int i=0;i<node.expChildren.size()*5;i++)
					brankLineString +="-";
				for(int i = 0;i<hiyConnectString[node.hierarchy].length()-1;i++)
					prefixString+=" ";
				hiyBrankString[node.hierarchy]+=(prefixString+"|"+brankLineString);
			}
		}
		
		for(int i =0;i<node.expChildren.size();i++){
			if(node.expChildren.get(i)!=null){
				ExpNode cntNode = node.expChildren.get(i);
				printNode(cntNode,i+leftAlign);
			}
		}
	}
	public void outputTree(){
		graphicTree = new GraphicTree();
		outputNode(mRoot,"",0,null);
		makeGraphicTree();
		
	}
	public void outputNode(SimpleNode node,String prefix,int cntHierarchy,ExpNode parentNode){
		//LET a AS 1!
		//FileWriter fileWriter;
		try {
			//fileWriter = new FileWriter(new File("tree.txt"),true);
			//String outputString = prefix+MyNewGrammarTreeConstants.jjtNodeName[node.id]+'\n';
			ExpNode cntNode = new ExpNode(cntHierarchy, MyNewGrammarTreeConstants.jjtNodeName[node.id]);
			if(parentNode!=null)
				parentNode.expChildren.add(cntNode);
			if(graphicTree.height==-1)
				graphicTree.root = cntNode;
			
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
			Node[] children = node.children;
			if (children != null) {
			      for (int i = 0; i < children.length; ++i){
				      SimpleNode n = (SimpleNode)children[i];
				      if (n != null) {
				      	  outputNode(n,prefix+"-",cntHierarchy+1,cntNode);
				      	  
				      }
			      }
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
