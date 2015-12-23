package mytree;

import java.util.ArrayList;


public class ExpNode {
	public int hierarchy;
	public String content;
	public String mText;
	public double locationX;
	public double locationY;
	int belowWidth = 0;
	public ExpNode paretNode = null;
	public ArrayList<ExpNode>expChildren = null;
	public ExpNode(int hierarchy, String content,ExpNode parent,String mText) {
		this.hierarchy = hierarchy;
		this.paretNode = parent;
		this.mText = mText;
		this.content = content;
		this.expChildren = new ArrayList<ExpNode>();
		this.locationX = 0;
		this.locationY = 0;
	}
}