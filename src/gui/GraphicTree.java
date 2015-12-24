package gui;

import java.util.ArrayList;

import mytree.ExpNode;

class GraphicTree{
	public ArrayList<Integer>hiyWidth = new ArrayList<Integer>();
	public int height;
	public int maxWidth;
	public ExpNode root;

	public void initGraphicTree(){
		this.height = -1;
		this.maxWidth = -1;
		this.root = null;
	}	
}