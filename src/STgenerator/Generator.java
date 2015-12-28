package STgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import STgenerator.SymbolTable.fsItem;
import STgenerator.SymbolTable.objItem;
import STgenerator.SymbolTable.vsItem;
import TreeGenerator.*;


public class Generator {
	
	public SymbolTable root;
	public ArrayList<String> constFloat = new ArrayList<String>();
	
	private SimpleNode ini_foreach_count = new SimpleNode(10);
	private SimpleNode inc_foreach_count = new SimpleNode(10);
	private SimpleNode foreach_count_expre = new SimpleNode(22);
	private SimpleNode array_for_each = new SimpleNode(16);
	private Integer current_body = new Integer(0);
	
	private String initiate_for_each(){
		
		root.AddEvar("foreach_count");
		root.vars.get("foreach_count").isArray = 0;
		root.vars.get("foreach_count").type = "NUM";
		root.vars.get("foreach_count").loc = -1;
		
		//create: LET count AS 0!
		Node[] children = new Node[2];
		SimpleNode var = new SimpleNode(39);
		var.jjtSetParent(ini_foreach_count);
		SimpleNode expre = new SimpleNode(22);
		expre.jjtSetParent(ini_foreach_count);
		//expre.m_Text = "NUM";
		children[0] = var;
		children[1] = expre;
		ini_foreach_count.children = children;
		
		SimpleNode symbol = new SimpleNode(3);
		symbol.m_Text = "foreach_count";
		var.jjtAddChild(symbol, 0);
		symbol.jjtSetParent(var);
		
		SimpleNode cond = new SimpleNode(26);
		//cond.m_Text = "NUM";
		expre.jjtAddChild(cond, 0);
		cond.jjtSetParent(expre);
		
		SimpleNode term = new SimpleNode(32);
		//term.m_Text = "NUM";
		cond.jjtAddChild(term, 0);
		term.jjtSetParent(cond);
		
		SimpleNode subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		term.jjtAddChild(subterm, 0);
		subterm.jjtSetParent(term);
		
		SimpleNode atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		subterm.jjtAddChild(atom, 0);
		atom.jjtSetParent(subterm);
		
		SimpleNode num = new SimpleNode(13);
		num.m_Text = "0";
		atom.jjtAddChild(num, 0);
		num.jjtSetParent(atom);
		
		
		//create: LET count AS count+1
		children = new Node[2];
		var = new SimpleNode(39);
		var.jjtSetParent(inc_foreach_count);
		expre = new SimpleNode(22);
		expre.jjtSetParent(inc_foreach_count);
		//expre.m_Text = "NUM";
		children[0] = var;
		children[1] = expre;
		inc_foreach_count.children = children;
		
		symbol = new SimpleNode(3);
		symbol.m_Text = "foreach_count";
		var.jjtAddChild(symbol, 0);
		symbol.jjtSetParent(var);
		
		cond = new SimpleNode(26);
		//cond.m_Text = "NUM";
		expre.jjtAddChild(cond, 0);
		cond.jjtSetParent(expre);
		
		term = new SimpleNode(32);
		//term.m_Text = "NUM";
		cond.jjtAddChild(term, 0);
		term.jjtSetParent(cond);
		
		subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		term.jjtAddChild(subterm, 0);
		subterm.jjtSetParent(term);
		
		atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		subterm.jjtAddChild(atom, 0);
		atom.jjtSetParent(subterm);
		
		var = new SimpleNode(39);
		var.jjtSetParent(atom);
		atom.jjtAddChild(var, 0);
		
		symbol = new SimpleNode(3);
		symbol.m_Text = "foreach_count";
		symbol.jjtSetParent(var);
		var.jjtAddChild(symbol, 0);
		
		SimpleNode plus = new SimpleNode(33);
		//plus.m_Text = "NUM";
		plus.jjtSetParent(term);
		term.jjtAddChild(plus, 1);
		
		subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		subterm.jjtSetParent(term);
		term.jjtAddChild(subterm, 2);
		
		atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		atom.jjtSetParent(subterm);
		subterm.jjtAddChild(atom, 0);
		
		num = new SimpleNode(13);
		num.m_Text = "1";
		num.jjtSetParent(atom);
		atom.jjtAddChild(num, 0);
		
		//create: count<1
		cond = new SimpleNode(26);
		//cond.m_Text = "NUM";
		cond.jjtSetParent(foreach_count_expre);
		foreach_count_expre.jjtAddChild(cond, 0);
		
		term = new SimpleNode(32);
		//term.m_Text = "NUM";
		term.jjtSetParent(cond);
		cond.jjtAddChild(term, 0);
		
		subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		subterm.jjtSetParent(term);
		term.jjtAddChild(subterm, 0);
		
		atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		atom.jjtSetParent(subterm);
		subterm.jjtAddChild(atom, 0);
		
		var = new SimpleNode(39);
		var.jjtSetParent(atom);
		atom.jjtAddChild(var, 0);
		
		symbol = new SimpleNode(3);
		symbol.m_Text = "foreach_count";
		symbol.jjtSetParent(var);
		var.jjtAddChild(symbol, 0);
		
		SimpleNode less = new SimpleNode(27);
		//less.m_Text = "NUM";
		less.jjtSetParent(cond);
		cond.jjtAddChild(less, 1);
		
		term = new SimpleNode(32);
		//term.m_Text = "NUM";
		term.jjtSetParent(cond);
		cond.jjtAddChild(term, 2);
		
		subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		subterm.jjtSetParent(term);
		term.jjtAddChild(subterm, 0);
		
		atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		atom.jjtSetParent(subterm);
		subterm.jjtAddChild(atom, 0);
		
		num = new SimpleNode(13);
		num.m_Text = "";
		num.jjtSetParent(atom);
		atom.jjtAddChild(num, 0);
		
		ini_foreach_count.dump("");
		inc_foreach_count.dump("");
		foreach_count_expre.dump("");
		
		//create ARRAY node
		term = new SimpleNode(32);
		//term.m_Text = "NUM";
		term.jjtSetParent(array_for_each);
		array_for_each.jjtAddChild(term, 0);
		
		subterm = new SimpleNode(35);
		//subterm.m_Text = "NUM";
		subterm.jjtSetParent(term);
		term.jjtAddChild(subterm, 0);
		
		atom = new SimpleNode(38);
		//atom.m_Text = "NUM";
		atom.jjtSetParent(subterm);
		subterm.jjtAddChild(atom, 0);
		
		var = new SimpleNode(39);
		var.jjtSetParent(atom);
		atom.jjtAddChild(var, 0);
		
		symbol = new SimpleNode(3);
		symbol.m_Text = "foreach_count";
		symbol.jjtSetParent(var);
		var.jjtAddChild(symbol, 0);
		
		return "1";
	}
	
	//this func must be call right after root initiated
	private String CreateInline(){
		
		initiate_for_each();
		
		//create inline func
		root.AddEfunc("inputnum");
		root.funcs.get("inputnum").type = "NUM";
		
		root.AddEfunc("inputcha");
		root.funcs.get("inputcha").type = "CHAR";
		
		root.AddEfunc("inputflo");
		root.funcs.get("inputflo").type = "FLOAT";
		
		root.AddEfunc("printnum");
		root.funcs.get("printnum").type = "NUM";
		root.funcs.get("printnum").addPara("n1", "NUM", 0);
		
		root.AddEfunc("printcha");
		root.funcs.get("printcha").type = "NUM";
		root.funcs.get("printcha").addPara("c1", "CHAR", 0);
		
		root.AddEfunc("printflo");
		root.funcs.get("printflo").type = "NUM";
		root.funcs.get("printflo").addPara("f1", "FLOAT", 0);
		
		root.AddEfunc("printspace");
		root.funcs.get("printspace").type = "NUM";
		
		root.AddEfunc("fadd");
		root.funcs.get("fadd").type = "NUM";
		root.funcs.get("fadd").addPara("f1", "FLOAT", 0);
		root.funcs.get("fadd").addPara("f2", "FLOAT", 0);
		
		root.AddEfunc("fsub");
		root.funcs.get("fsub").type = "NUM";
		root.funcs.get("fsub").addPara("f1", "FLOAT", 0);
		root.funcs.get("fsub").addPara("f2", "FLOAT", 0);
		
		root.AddEfunc("fsub");
		root.funcs.get("fsub").type = "NUM";
		root.funcs.get("fsub").addPara("f1", "FLOAT", 0);
		root.funcs.get("fsub").addPara("f2", "FLOAT", 0);

		root.AddEfunc("fimul");
		root.funcs.get("fimul").type = "NUM";
		root.funcs.get("fimul").addPara("f1", "FLOAT", 0);
		root.funcs.get("fimul").addPara("f2", "FLOAT", 0);
		
		root.AddEfunc("fidiv");
		root.funcs.get("fidiv").type = "NUM";
		root.funcs.get("fidiv").addPara("f1", "FLOAT", 0);
		root.funcs.get("fidiv").addPara("f2", "FLOAT", 0);
		
		return "1";
	}
	
	//this should search tree alone and must initialize constFloat
	//plus turn char to num here
	private String addConstFloat(SimpleNode startNode){
		
		if(MyNewGrammarTreeConstants.jjtNodeName[startNode.id].equals("FLOAT") && !startNode.m_Text.equals("")){
			String tmp = startNode.m_Text;
			startNode.m_Text = ((Integer)constFloat.size()).toString();
			constFloat.add(tmp);
		}
		
		if(MyNewGrammarTreeConstants.jjtNodeName[startNode.id].equals("CHAR") && !startNode.m_Text.equals("")){
			String tmp = startNode.m_Text;
			Integer midValue = (int) tmp.charAt(1);
			startNode.m_Text = midValue.toString();
		}
		
		for(int i=0; i<startNode.jjtGetNumChildren(); ++i){
			addConstFloat((SimpleNode)startNode.jjtGetChild(i));
		}
		
		root.AddEvar("global_float_const");
		root.vars.get("global_float_const").isArray = 1;
		root.vars.get("global_float_const").type = "FLOAT";
		root.vars.get("global_float_const").loc = -1;
		
		return "1";
	}
	
	public vsItem locateVar(String name, SymbolTable st){
		
		vsItem result = null;
		SymbolTable tmp = st;
		
		
		while(!tmp.body.equals(0)){
			String target1 = name+"_"+tmp.body.toString();
			String target2 = name;
			if(tmp.vars.containsKey(target1)){
				result = tmp.vars.get(target1);
				break;
			}else{
				if(tmp.vars.containsKey(target2)){
					result = tmp.vars.get(target2);
					break;
				}else{
					tmp = tmp.parent;
				}
			}
		}
		
		String target1 = name+"_"+tmp.body.toString();
		String target2 = name;
		if(tmp.vars.containsKey(target1)){
			result = tmp.vars.get(target1);
		}else{
			if(tmp.vars.containsKey(target2)){
				result = tmp.vars.get(target2);
			}else{
				tmp = tmp.parent;
			}
		}
		
		return result;
	}
	
	private String turnForeachToLoop(SimpleNode body, String pix, String array){
		
		for(Integer i=0; i<body.jjtGetNumChildren(); ++i){
			SimpleNode tmp = (SimpleNode)body.jjtGetChild(i);
			if(tmp.m_Text.equals(pix)){
				tmp.m_Text = array;
				SimpleNode arrayCopy = new SimpleNode(array_for_each);
				body.jjtAddChild(arrayCopy, i+1);
				arrayCopy.jjtSetParent(body);
			}else{
				turnForeachToLoop(tmp, pix, array);
			}
		}
		
		return "1";
	}
	
	private String turnComCall(SimpleNode comCall, SymbolTable st){
		
		for(Integer i=1; i<comCall.jjtGetNumChildren(); ++i){
			SimpleNode tmp = (SimpleNode)comCall.jjtGetChild(i);
			if(MyNewGrammarTreeConstants.jjtNodeName[tmp.id].equals("term")){
				tmp = (SimpleNode)comCall.jjtGetChild(i-2);
				String objType;
				if(root.objs.containsKey(tmp.m_Text)){
					objType = tmp.m_Text; 
				}else{
					objType = locateVar(tmp.m_Text, st).type;
				}
				tmp.m_Text = objType+"_"+((SimpleNode)comCall.jjtGetChild(i-1)).m_Text;
				Node[] newchildren = new Node[comCall.jjtGetNumChildren()-i+1];
				newchildren[0] = tmp;
				int p = 1;
				for(int j=i.intValue(); j<comCall.jjtGetNumChildren(); ++j){
					newchildren[p] = comCall.jjtGetChild(j);
					p++;
				}
				comCall.children = newchildren;
				return "1";
			}
		}
		
		Integer i = comCall.jjtGetNumChildren()-1;
		SimpleNode tmp = (SimpleNode)comCall.jjtGetChild(i);
		tmp = (SimpleNode)comCall.jjtGetChild(i-1);
		String objType;
		if(root.objs.containsKey(tmp.m_Text)){
			objType = tmp.m_Text; 
		}else{
			objType = locateVar(tmp.m_Text, st).type;
		}
		tmp.m_Text = objType+"_"+((SimpleNode)comCall.jjtGetChild(i)).m_Text;
		Node[] newchildren = new Node[comCall.jjtGetNumChildren()-i+1];
		newchildren[0] = tmp;
		comCall.children = newchildren;
		
		return "1";
	}
	
	public SymbolTable generate(SimpleNode root, Integer base, SymbolTable papa, ArrayList<vsItem> FormalVar) throws Exception{
		
		Integer tmp_body = new Integer(current_body.intValue());
		SymbolTable result = new SymbolTable(base, tmp_body);
		result.parent = papa;
		
		if(FormalVar!=null){
			for(int i=0; i<FormalVar.size(); ++i){
				result.AddEvar(FormalVar.get(i).name);
				vsItem tmp = (vsItem)result.vars.get(FormalVar.get(i).name);
				tmp.type = FormalVar.get(i).type;
				tmp.isArray = FormalVar.get(i).isArray;
				tmp.loc = -1;
				tmp.isFormal = 1;
			}
		}
		
		//define initial correct basic type
		ArrayList<String> allType = new ArrayList();
		allType.add("NUM"); allType.add("CHAR"); allType.add("FLOAT");
		ArrayList tmp = null;
		
		Integer current_child = 0;
		Integer current_loc = base;
		while(current_child < root.jjtGetNumChildren()){
			String accept;
			SimpleNode t = (SimpleNode)(root.jjtGetChild(current_child));
			SimpleNode tc;
			switch(MyNewGrammarTreeConstants.jjtNodeName[t.id]){
			case "DEF": 
				tc = (SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1));
				if((!MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("OBJ")) && (!MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("BODY"))){			
					tc = (SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1));
					if(MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("SYMBOL")){
						
						if(t.jjtGetNumChildren()>2){
							Exception e = new Exception(((SimpleNode)t.jjtGetChild(0)).m_Text+":Can not define object array");
							throw e;
							//throw fault(can not be obj array)
						}
						if(!this.root.objs.containsKey(tc.m_Text)){
							Exception e = new Exception(((SimpleNode)t.jjtGetChild(0)).m_Text+":not defined boject");
							throw e;
							//throw fault(not defined obj)
						}
						
						vsItem var;
						unpackedObj pack = unpackObj(((SimpleNode)t.jjtGetChild(0)).m_Text, tc.m_Text);
						for(String key:pack.eleVars.keySet()){
							String name = key + "_" +result.body.toString();
							result.AddEvar(name);
							var = result.vars.get(name);
							if(!pack.isEleArray.get(key).equals(0)){
								var.type = pack.eleVars.get(key);
								var.loc = current_loc;
								var.isArray = pack.isEleArray.get(key);
								current_loc += 4*var.isArray;
							}else{
								var.type = pack.eleVars.get(key);
								var.isArray = 0;
								var.loc = current_loc;
								current_loc += 4;
							}
						}
						
						tc = (SimpleNode)(t.jjtGetChild(0));
						tc.m_Text = tc.m_Text+"_"+result.body.toString();
						if(result.vars.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic variable");
							throw e;
							//throw fault(duplicate var name)
						}
						
						result.AddEvar(tc.m_Text);
						var = (vsItem)(result.vars.get(tc.m_Text));
						var.type = ((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1))).m_Text;
						var.loc = -1;
						
					}else{
						tc = (SimpleNode)(t.jjtGetChild(0));
						tc.m_Text = tc.m_Text+"_"+result.body.toString();
						if(result.vars.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic variable");
							throw e;
							//throw fault(duplicate var name)
						}
						
						result.AddEvar(tc.m_Text);
						vsItem var = (vsItem)(result.vars.get(tc.m_Text));
						if(t.jjtGetNumChildren()>2){
							var.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-2))).id];
							var.loc = current_loc;
							SimpleNode term = (SimpleNode)((SimpleNode)(t.jjtGetChild(2))).jjtGetChild(0);
							var.isArray = compute_term(term);
							current_loc += 4*compute_term(term);
						}else{
							var.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1))).id];
							var.isArray = 0;
							var.loc = current_loc;
							current_loc += 4;
						}
					}
				}else{
					Exception e = new Exception(((SimpleNode)t.jjtGetChild(0)).m_Text+":Function and object can only be defined globally");
					throw e;
					//throw fault(func and obj definition can only be global)
				}
				break;
				
				
			case "CALL":
				if(t.jjtGetNumChildren()>1 && !MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)t.jjtGetChild(1)).id].equals("term")){
					turnComCall(t, result);
				}
				emCall(t,allType,result);
				break;
			case "RET":
				emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result, 0);
				break;
			case "LET":
				emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result, 1);
				if(MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(1))).id].equals("BULK")){
					emBulk((SimpleNode)(t.jjtGetChild(1)), result);
				}else{
					emExpre((SimpleNode)(t.jjtGetChild(1)), allType, result, 0);
				}

				break;
			case "LOOP":
			case "BRANCH":
				System.out.println("test");
				emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result, 0);
				tc = (SimpleNode)(t.jjtGetChild(1));
				current_body += 1;
				tc.m_Text = current_body.toString();
				result.children.add(generate(tc, current_loc, result, tmp));		   
				break;
			case "FOREACH":
				if(locateVar(((SimpleNode)t.jjtGetChild(1)).m_Text, result)==null){
					Exception e = new Exception(((SimpleNode)t.jjtGetChild(1)).m_Text+":not defined variable");
					throw e;
				}
				
				turnForeachToLoop((SimpleNode)t.jjtGetChild(2), ((SimpleNode)t.jjtGetChild(0)).m_Text, ((SimpleNode)t.jjtGetChild(1)).m_Text);
				
				Node[] newchildren = new Node[root.jjtGetNumChildren()+1];
				for(int i=0; i<current_child; ++i){
					newchildren[i] = root.jjtGetChild(i);
				}
				SimpleNode iniCopy = new SimpleNode(ini_foreach_count); 
				newchildren[current_child] = iniCopy;
				iniCopy.jjtSetParent(root);
				for(int i=current_child+1; i<root.jjtGetNumChildren()+1; ++i){
					newchildren[i] = root.jjtGetChild(i-1);
				}
				root.children = newchildren;
				
				SimpleNode foreachExpre = new SimpleNode(foreach_count_expre);
				String size = locateVar(((SimpleNode)t.jjtGetChild(1)).m_Text, result).isArray.toString();
				SimpleNode pix = foreachExpre;
				while(!MyNewGrammarTreeConstants.jjtNodeName[pix.id].equals("NUM")){
					pix = (SimpleNode)pix.jjtGetChild(pix.jjtGetNumChildren()-1);
				}
				pix.m_Text = size;
				
				SimpleNode forBody = (SimpleNode)t.jjtGetChild(2);
				
				SimpleNode incCopy = new SimpleNode(inc_foreach_count);
				forBody.jjtAddChild(incCopy, forBody.jjtGetNumChildren());
				incCopy.jjtSetParent(forBody);
				
				newchildren = new Node[2];
				newchildren[0] = foreachExpre;
				newchildren[1] = forBody;
				t.children = newchildren;
				foreachExpre.jjtSetParent(t);
				forBody.jjtSetParent(t);
				t.id = 5;
						
				break;
			}
			current_child += 1;
		}
		return result;
	}
	
	public SymbolTable generateRoot(SimpleNode root) throws Exception{
		
		SymbolTable result = new SymbolTable(0, 0);   //initiate body number of root as 0
		this.root = result;
		current_body = 0;
		
		CreateInline();
		
		Integer current_child = 0;
		Integer current_loc = 0;
		
		ArrayList<SimpleNode> bodys = new ArrayList();
		ArrayList<String> funcName = new ArrayList();
		
		while(current_child < root.jjtGetNumChildren()){
			SimpleNode t = (SimpleNode)(root.jjtGetChild(current_child));
			SimpleNode tc;
			Integer tmp_body = new Integer(current_body.intValue());
			if(MyNewGrammarTreeConstants.jjtNodeName[t.id].equals("DEF")){			
				tc = (SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1));
				if((!MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("OBJ")) && (!MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("BODY"))){			
					tc = (SimpleNode)(t.jjtGetChild(1));
					if(MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("SYMBOL")){
						
						if(t.jjtGetNumChildren()>2){
							Exception e = new Exception(((SimpleNode)t.jjtGetChild(0)).m_Text+":Can not define object array");
							throw e;
							//throw fault(can not be obj array)
						}
						if(!this.root.objs.containsKey(tc.m_Text)){
							Exception e = new Exception(((SimpleNode)t.jjtGetChild(0)).m_Text+":not defined boject");
							throw e;
							//throw fault(not defined obj)
						}
						
						vsItem var;
						unpackedObj pack = unpackObj(((SimpleNode)t.jjtGetChild(0)).m_Text, tc.m_Text);
						for(String key:pack.eleVars.keySet()){
							String name = key + "_" +result.body.toString();
							result.AddEvar(name);
							var = result.vars.get(name);
							if(!pack.isEleArray.get(key).equals(0)){
								var.type = pack.eleVars.get(key);
								var.loc = current_loc;
								var.isArray = pack.isEleArray.get(key);
								current_loc += 4*var.isArray;
							}else{
								var.type = pack.eleVars.get(key);
								var.isArray = 0;
								var.loc = current_loc;
								current_loc += 4;
							}
						}
						
						tc = (SimpleNode)(t.jjtGetChild(0));
						tc.m_Text = tc.m_Text+"_"+result.body.toString();
						if(result.vars.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic variable");
							throw e;
							//throw fault(duplicate var name)
						}
						
						result.AddEvar(tc.m_Text);
						var = (vsItem)(result.vars.get(tc.m_Text));
						var.type = ((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1))).m_Text;
						var.loc = -1;
						
					}else{
						tc = (SimpleNode)(t.jjtGetChild(0));
						tc.m_Text = tc.m_Text+"_"+result.body.toString();
						if(result.vars.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic variable");
							throw e;
							//throw fault(duplicate var name)
						}
						
						result.AddEvar(tc.m_Text);
						vsItem var = (vsItem)(result.vars.get(tc.m_Text));
						if(t.jjtGetNumChildren()>2){
							var.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-2))).id];
							var.loc = current_loc;
							SimpleNode term = (SimpleNode)((SimpleNode)(t.jjtGetChild(2))).jjtGetChild(0);
							var.isArray = compute_term(term);
							current_loc += 4*compute_term(term);
						}else{
							var.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1))).id];
							var.isArray = 0;
							var.loc = current_loc;
							current_loc += 4;
						}
					}
					
				}else{
					if(MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("BODY")){
						tmp_body ++;
						tc = (SimpleNode)(t.jjtGetChild(0));
						if(result.funcs.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic function");
							throw e;
							//throw fault(duplic func)
						}
						result.AddEfunc(tc.m_Text);
						fsItem func = (fsItem)(result.funcs.get(tc.m_Text));
						funcName.add(tc.m_Text);
						tc = (SimpleNode)(t.jjtGetChild(1));
						if(MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("PARAS")){
							for(int i=0; i<tc.jjtGetNumChildren(); ++i){
								SimpleNode tmp = (SimpleNode)(tc.jjtGetChild(i));
								if(MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(tmp.jjtGetChild(1))).id].equals("ARRAY")){
									func.addPara(((SimpleNode)(tmp.jjtGetChild(2))).m_Text, MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(tmp.jjtGetChild(0))).id], 1);
								}else{
									func.addPara(((SimpleNode)(tmp.jjtGetChild(1))).m_Text, MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(tmp.jjtGetChild(0))).id], 0);
								}
							}			
							func.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(2))).id];					
						}else{						
							func.type = MyNewGrammarTreeConstants.jjtNodeName[tc.id];
						}
				 		tc = (SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1));					
						bodys.add(tc);
					}else{
						tc = (SimpleNode)(t.jjtGetChild(0));
						if(result.objs.containsKey(tc.m_Text)){
							Exception e = new Exception(tc.m_Text+":Duplic object");
							throw e;
							//throw fault(duplic obj)
						}
						result.AddEobj(tc.m_Text);
						objItem obj = (objItem)(result.objs.get(tc.m_Text));
						tc = (SimpleNode)t.jjtGetChild(1);
						if(MyNewGrammarTreeConstants.jjtNodeName[tc.id].equals("DEFS")){
							for(int i=0; i<tc.jjtGetNumChildren(); ++i){
								SimpleNode defTmp = (SimpleNode)tc.jjtGetChild(i);
								if(!MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)defTmp.jjtGetChild(defTmp.jjtGetNumChildren()-1)).id].equals("BODY")){
									if(defTmp.jjtGetNumChildren()>2){
										String type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)defTmp.jjtGetChild(1)).id];
										if(type.equals("NUM") || type.equals("CHAR") || type.equals("FLOAT")){
											obj.addVar(((SimpleNode)defTmp.jjtGetChild(0)).m_Text, type, compute_term((SimpleNode)((SimpleNode)defTmp.jjtGetChild(2))));
										}else{
											Exception e = new Exception(((SimpleNode)defTmp.jjtGetChild(0)).m_Text+":Can not define object array");
											throw e;
											//throw fault(no obj array)
										}
									}else{
										String type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)defTmp.jjtGetChild(1)).id];
										if(type.equals("NUM") || type.equals("CHAR") || type.equals("FLOAT")){
											obj.addVar(((SimpleNode)defTmp.jjtGetChild(0)).m_Text, type, 0);
										}else{
											type = ((SimpleNode)defTmp.jjtGetChild(1)).m_Text;
											if(!this.root.objs.containsKey(type)){
												Exception e = new Exception(((SimpleNode)defTmp.jjtGetChild(1)).m_Text+":Not defined object");
												throw e;
												//throw fault(not defined obj)
											}
										}
										
									}
								}else{
									if(!MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)defTmp.jjtGetChild(defTmp.jjtGetNumChildren()-1)).id].equals("OBJ")){
										obj.eleFunc.put(((SimpleNode)defTmp.jjtGetChild(0)).m_Text, defTmp);
										SimpleNode funcdef = new SimpleNode(defTmp);
										funcdef.parent = root;
										((SimpleNode)funcdef.jjtGetChild(0)).m_Text = ((SimpleNode)t.jjtGetChild(0)).m_Text+"_"+((SimpleNode)funcdef.jjtGetChild(0)).m_Text;
										root.jjtAddChild(funcdef, root.jjtGetNumChildren());
									}else{
										Exception e = new Exception(((SimpleNode)defTmp.jjtGetChild(0)).m_Text+":Can not define object in object");
										throw e;
										//throw fault(no obj in obj)
									}
								}
							}
						}
					}
				}			
			}else{
				Exception e = new Exception(MyNewGrammarTreeConstants.jjtNodeName[t.id]+"...:Can only DEF in global");
				throw e;
				//throw fault(only definition in global)
			}
			current_child++;
		}
		
		//to build temp variable
		Integer maxVars = new Integer(2);
		
		for(int i=0; i<bodys.size(); ++i){
			current_body += 1;
			bodys.get(i).m_Text = current_body.toString();
			ArrayList<vsItem> FormalVar = new ArrayList<vsItem>(); 
			
			fsItem fitem = (fsItem)result.funcs.get(funcName.get(i));
			fitem.body = current_body.intValue();
			if(fitem.getNumpara() > maxVars){
				maxVars = fitem.getNumpara();
			}
			for(int j=0; j<fitem.getNumpara(); ++j){		
				ArrayList para = fitem.getPara(j);
				vsItem tmp = result.new vsItem();
				FormalVar.add(tmp);
				FormalVar.get(j).name = (String)para.get(0);
				FormalVar.get(j).type = (String)para.get(1);
				FormalVar.get(j).isArray = (Integer)para.get(2);
			}
			
			//shit
			int procFlag = 1;
			SymbolTable funcTable = null;
			if(procFlag == 0){
				funcTable = generate((SimpleNode)(bodys.get(i)), 0, result, null);
			}
			else{
				funcTable = generate((SimpleNode)(bodys.get(i)), 0, result, FormalVar);
			}
			//SymbolTable funcTable = generate((SimpleNode)(bodys.get(i)), 0, result, FormalVar);
			result.children.add(funcTable);
		}
		
		for(Integer i=0; i<maxVars; ++i){
			this.root.AddEvar("_NUM"+i.toString());
			vsItem tmp = this.root.vars.get("_NUM"+i.toString());
			tmp.type = "NUM";
			tmp.isArray = 0;
			tmp.loc = -1;
			
			this.root.AddEvar("_FLOAT"+i.toString());
			tmp = this.root.vars.get("_FLOAT"+i.toString());
			tmp.type = "FLOAT";
			tmp.isArray = 0;
			tmp.loc = -1;
		}
		
		root.m_Text = "0";
		
		for(int i=0; i<bodys.size(); ++i){
			String funcType = this.root.funcs.get(funcName.get(i)).type;
			semanticCheckbyFunc(bodys.get(i), funcType);
			
		}
		
		addConstFloat(root);
		return result;
	}
	
	private class unpackedObj{
		
		public Map<String, String> eleVars = new HashMap<String, String>();
		public Map<String, Integer> isEleArray = new HashMap<String, Integer>();  
		
	}
	
	public unpackedObj unpackObj(String prefix, String objName){
		
		prefix = prefix + "_";
		objItem obitem = root.objs.get(objName);
		unpackedObj result = new unpackedObj();
		for(String key: obitem.eleVars.keySet()){
			String uName = prefix+key;
			String type = obitem.eleVars.get(key);
			Integer isArray = obitem.isEleArray.get(key);
			if(type.equals("NUM") || type.equals("FLOAT") || type.equals("CHAR")){
				result.eleVars.put(uName, type);
				result.isEleArray.put(uName, isArray);
			}else{
				unpackedObj child = unpackObj(uName, type);
				for(String key2: child.eleVars.keySet()){
					result.eleVars.put(key2, child.eleVars.get(key2));
					result.isEleArray.put(key2, child.isEleArray.get(key2));
				}
			}
		}
		
		return result;
		
	}
	
	public String emBulk(SimpleNode bulk, SymbolTable st) throws Exception{
		
		String result = "1";
		
		for(int i=0; i<bulk.jjtGetNumChildren(); ++i){
			SimpleNode t = (SimpleNode)bulk.jjtGetChild(i);
			if(MyNewGrammarTreeConstants.jjtNodeName[t.id].equals("SYMBOL")){
				
				vsItem vitem = locateVar(t.m_Text, st);
				
				if(vitem == null){
					Exception e = new Exception(t.m_Text+":Not defined variable");
					throw e;
				}
				
				t.m_Text = vitem.name;
			}
		}
		
		return result;
	}
	
	public String emExpre(SimpleNode expre, ArrayList<String> type, SymbolTable st, Integer mustVar) throws Exception{ /*return "1" present ok, return symbol with
																		  postfix 2 present not deifined symbol, with 
																		  postfix 0 present not match type*/
		
		//deal with simple call node
		if(MyNewGrammarTreeConstants.jjtNodeName[expre.id].equals("CALL")){
			if(mustVar.equals(1)){
				Exception e = new Exception(((SimpleNode)expre.jjtGetChild(0)).m_Text+":First parameter of LET must be a variable");
				throw e;
				//throw fault(var violate) 
			}
			String tmp = emCall(expre, type, st);
			return tmp;
		}
		
		//deal with var with pointer child(may be a call)
		if(MyNewGrammarTreeConstants.jjtNodeName[expre.id].equals("VAR") && MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)expre.jjtGetChild(expre.jjtGetNumChildren()-1)).id].equals("pointer")){
			String newVar = ((SimpleNode)expre.jjtGetChild(0)).m_Text;
			for(int i=1; i<expre.jjtGetNumChildren(); ++i){
				SimpleNode ele = (SimpleNode)((SimpleNode)expre.jjtGetChild(i)).jjtGetChild(0);
				if(MyNewGrammarTreeConstants.jjtNodeName[ele.id].equals("CALL")){
					String newfun;
					if(root.objs.containsKey(((SimpleNode)expre.jjtGetChild(i-1)).m_Text)){
						newfun = ((SimpleNode)expre.jjtGetChild(i-1)).m_Text+"_"+((SimpleNode)ele.jjtGetChild(0)).m_Text;
					}else{
						if(locateVar(((SimpleNode)expre.jjtGetChild(i-1)).m_Text, st)==null){
							Exception e = new Exception(((SimpleNode)expre.jjtGetChild(i-1)).m_Text+":undefined symbol");
							throw e;
						}
						newfun = locateVar(((SimpleNode)expre.jjtGetChild(i-1)).m_Text, st).type+"_"+((SimpleNode)ele.jjtGetChild(0)).m_Text;
					}
					SimpleNode preAtom = (SimpleNode)expre.parent;
					SimpleNode preSub = (SimpleNode)preAtom.parent;
					((SimpleNode)ele.jjtGetChild(0)).m_Text = newfun;
					Node[] newchildren = new Node[1];
					newchildren[0] =  ele;
					preAtom.children = newchildren;
					ele.parent = preAtom;
					return emExpre(preAtom, type, st, mustVar);
				}else{
					newVar = newVar+"_"+ele.m_Text;
				}
			}
			((SimpleNode)expre.jjtGetChild(0)).m_Text = newVar;
			Node[] newchildren;
			if(((SimpleNode)expre.jjtGetChild(expre.jjtGetNumChildren()-1)).jjtGetNumChildren()>1){
				System.out.println("shit");
				newchildren = new Node[2];
				newchildren[0] = (SimpleNode)expre.jjtGetChild(0);
				newchildren[1] = ((SimpleNode)expre.jjtGetChild(expre.jjtGetNumChildren()-1)).jjtGetChild(1);
				((SimpleNode)((SimpleNode)expre.jjtGetChild(expre.jjtGetNumChildren()-1)).jjtGetChild(1)).parent = expre;
			}else{
				newchildren = new Node[1];
				newchildren[0] = (SimpleNode)expre.jjtGetChild(0);
			}
			expre.children = newchildren;
		}
		
		//check this node if terminate
		if(!expre.m_Text.equals("")){
			
			String etype;
			if(MyNewGrammarTreeConstants.jjtNodeName[expre.id].equals("SYMBOL")){
				vsItem vitem = locateVar(expre.m_Text, st);
				
				if(vitem == null){
					Exception e = new Exception(expre.m_Text+":Not defined variable");
					throw e;
				}
				
				etype = vitem.type;
				expre.m_Text = vitem.name;
				
			}else{
				etype = MyNewGrammarTreeConstants.jjtNodeName[expre.id];
			}
			
			for(int i=0; i<type.size(); ++i){      //works of checking type done here
				
				if(etype.equals(type.get(i))){
					return "1";
				}
				
			}
			
			Exception e = new Exception(expre.m_Text+":Not match type");
			throw e;
		}
		
		//check children if non-terminate
		Integer i = 0;
		while(i<expre.jjtGetNumChildren()){
			emExpre((SimpleNode)(expre.jjtGetChild(i)), type, st, mustVar); 
			++i;
		}
		
		return "1";
	}
	
	public String emCall(SimpleNode nodeCall, ArrayList<String> type, SymbolTable st) throws Exception{
	
		String accept;
		
		if(this.root.funcs.containsKey(((SimpleNode)(nodeCall.jjtGetChild(0))).m_Text)){
			for(int i=1; i<nodeCall.jjtGetNumChildren(); ++i){
				emExpre((SimpleNode)(nodeCall.jjtGetChild(i)), type, st, 0);
			}
		}else{
			Exception e = new Exception(((SimpleNode)(nodeCall.jjtGetChild(0))).m_Text+":No match function");
			throw e;
			//throw fault(no match function)
		}
		
		return "1";
	}
	
	public Integer compute_term(SimpleNode term){
		//Modify this to compute a real term
		SimpleNode pix = term;
		while(!MyNewGrammarTreeConstants.jjtNodeName[pix.id].equals("NUM")){
			pix = (SimpleNode)(pix.jjtGetChild(0));
		}
		return Integer.parseInt(pix.m_Text);
	}
	
/* func below must be called after generateRoot done*/	
	
	public fsItem getFuncInfo(String name){
		fsItem result = null;
		result = root.funcs.get(name);
		return result;
	}
	
	public ArrayList getAllvar(String funcName){
		
		ArrayList result = new ArrayList();
		
		if(root.funcs.containsKey(funcName)){
			Integer funcbody = root.funcs.get(funcName).body;
			for(int i=0; i<root.children.size(); ++i){
				if(((SymbolTable)(root.children.get(i))).body.equals(funcbody)){
					result = getAllvarBybody(((SymbolTable)(root.children.get(i))));
					break;
				}
			}
		}
		
		return result;
		
	}
	
	private ArrayList getAllvarBybody(SymbolTable st){
		
		ArrayList result = new ArrayList();
		
		for(String key:st.vars.keySet()){
			result.add(st.vars.get(key));
		}
		for(int i=0; i<st.children.size(); ++i){
			ArrayList tmp = getAllvarBybody((SymbolTable)(st.children.get(i)));
			for(int j=0; j<tmp.size(); ++j){
				result.add(tmp.get(j));
			}
		}
		
		return result;
	}
	
	public vsItem getVar(String funcName, String varName){
		
		vsItem result = null;
		
		if(funcName.equals("")){
			result = getVar(root, varName);
		}else{
			if(root.funcs.containsKey(funcName)){
				Integer funcBody = root.funcs.get(funcName).body;
				for(int i=0; i<root.children.size(); ++i){
					if(((SymbolTable)root.children.get(i)).body.equals(funcBody)){
						result = getVar((SymbolTable)root.children.get(i), varName);
						break;
					}
				}
			}
		}
		
		return result;
	}
	
	public vsItem getVar(SymbolTable r, String name){
		
		vsItem var = null;
		
		if(r.vars.containsKey(name)){
			return r.vars.get(name);
		}
		for(int i=0; i<r.children.size(); ++i){
			SymbolTable tmp = (SymbolTable)(r.children.get(i));
			var = getVar(tmp, name); 
			if(var!=null){
				return var;
			}
		}
			
		return var;
	}

	public String mtExpre(SimpleNode expre, SymbolTable st) throws Exception{
		String result = "NUM";
		if(expre.jjtGetNumChildren()>1){			
			for(int i=0; i<expre.jjtGetNumChildren(); ++i){
				if((i%2)==0){
					String type = mtCond((SimpleNode)expre.jjtGetChild(i), st);
					if(type.equals("CHAR")){
						type = "NUM";
					}
					((SimpleNode)expre.jjtGetChild(i)).m_Text = type;
				}else{
					((SimpleNode)expre.jjtGetChild(i)).m_Text = "NUM";
				}
			}
		}else{
			result = mtCond((SimpleNode)expre.jjtGetChild(0), st);
		}
		
		expre.m_Text = result;
		return result;
	}
	
	public String mtCond(SimpleNode cond, SymbolTable st) throws Exception{
		
		String result = "NUM";
		if(cond.jjtGetNumChildren()>1){
			mtTerm((SimpleNode)cond.jjtGetChild(0), st);
			mtTerm((SimpleNode)cond.jjtGetChild(2), st);
			((SimpleNode)cond.jjtGetChild(1)).m_Text = "NUM";
		}else{
			result = mtTerm((SimpleNode)cond.jjtGetChild(0), st);
		}
		
		cond.m_Text = result;
		return result;
	}
	
	public String mtTerm(SimpleNode term, SymbolTable st) throws Exception{
		
		String result = "NUM";
		
		SimpleNode sub = (SimpleNode)term.jjtGetChild(0);
		String preType = mtSub(sub,st);
		if(preType.equals("CHAR")){
			preType = "NUM";
		}
		result = preType;
		
		for(int i=1; i<term.jjtGetNumChildren(); ++i){
			if((i%2)==0){
				sub = (SimpleNode)term.jjtGetChild(i);
				String currentType = mtSub(sub, st);
				if(currentType.equals("CHAR")){
					currentType = "NUM";
				}
				if(preType.equals("NUM") && currentType.equals("NUM")){
					((SimpleNode)term.jjtGetChild(i-1)).m_Text = "NUM";
				}else{
					((SimpleNode)term.jjtGetChild(i-1)).m_Text = "FLOAT";
					result = "FLOAT";
				}
			} 
		}
		
		term.m_Text = result;
		return result;
	}
	
	public String mtSub(SimpleNode subterm, SymbolTable st) throws Exception{
		
		String result = "NUM";
		
		SimpleNode atom = (SimpleNode)subterm.jjtGetChild(0);
		String preType = mtAtom(atom, st);
		if(preType.equals("CHAR")){
			preType = "NUM";
		}
		result = preType;
		
		for(int i=1; i<subterm.jjtGetNumChildren(); ++i){
			if((i%2)==0){
				atom = (SimpleNode)subterm.jjtGetChild(i);
				String currentType = mtAtom(atom, st);
				if(currentType.equals("CHAR")){
					currentType = "NUM";
				}
				if(preType.equals("NUM") && currentType.equals("NUM")){
					((SimpleNode)subterm.jjtGetChild(i-1)).m_Text = "NUM";
				}else{
					((SimpleNode)subterm.jjtGetChild(i-1)).m_Text = "FLOAT";
					result = "FLOAT";
				}
			} 
		}
		
		subterm.m_Text = result;
		return result;
	}
	
	public String mtAtom(SimpleNode atom, SymbolTable st) throws Exception{
		
		String result = "NUM";
		
		SimpleNode child = (SimpleNode)atom.jjtGetChild(0);
		switch(MyNewGrammarTreeConstants.jjtNodeName[child.id]){
		case "FLOAT": 
			result = "FLOAT";
			break;
		case "VAR":
			if(mtVar(child, st).equals("FLOAT")){
				result = "FLOAT";
			}
			break;
		case "CALL":
			if(mtCall(child, st).equals("FLOAT")){
				result = "FLOAT";
			}
			break;
		case "expre":
			if(mtExpre(child, st).equals("FLOAT")){
				result = "FLOAT";
			}
			break;
		}
		
		atom.m_Text = result;
		return result;
	}
	
	public String mtVar(SimpleNode var, SymbolTable st) throws Exception{
		
		String result = "NUM";
		
		SimpleNode symbol = (SimpleNode)var.jjtGetChild(0);
		vsItem vitem = locateVar(symbol.m_Text, st);
		
		if(vitem.type.equals("FLOAT")){
			result = "FLOAT";
		}
		
		var.m_Text = result;
		
		if(var.jjtGetNumChildren()>1){
			SimpleNode tmp = (SimpleNode)var.jjtGetChild(1);
			tmp = (SimpleNode)tmp.jjtGetChild(0);
			mtTerm(tmp, st);
		}
		
		return result;
		
	}
	
	public String mtCall(SimpleNode call, SymbolTable st) throws Exception{
		
		String result = "NUM";
		
		SimpleNode funcName = (SimpleNode)call.jjtGetChild(0);
		fsItem fitem = getFuncInfo(funcName.m_Text);
	
		if(!fitem.getNumpara().equals(call.jjtGetNumChildren()-1)){
			Exception e = new Exception(((SimpleNode)call.jjtGetChild(0)).m_Text+":Number of parameters not match");
			throw e;
			//throw fault(number of parameters is not match)
		}
		
		for(int i=0; i<fitem.getNumpara(); ++i){
			String type = mtTerm((SimpleNode)call.jjtGetChild(i+1), st);
			String target = "NUM";
			if(((String)fitem.getPara(i).get(1)).equals("FLOAT")){
				target = "FLOAT";
			}
			if(!type.equals(target)){
				Exception e = new Exception(((SimpleNode)call.jjtGetChild(0)).m_Text+":Type of parameter not match");
				throw e;
				//throw fault(not match parameter type)
			}
		}
		
		if(fitem.type.equals("FLOAT")){
			result = "FLOAT";
		}
		
		call.m_Text = result;
		return result;
	}
	
	public String mtBulk(SimpleNode bulk, SymbolTable st, String acType) throws Exception{
		
		String result = "1";
		
		for(int i=0; i<bulk.jjtGetNumChildren(); ++i){
			SimpleNode t = (SimpleNode)bulk.jjtGetChild(i);
			String eleType = MyNewGrammarTreeConstants.jjtNodeName[t.id];
			if(MyNewGrammarTreeConstants.jjtNodeName[t.id].equals("SYMBOL")){
				eleType = locateVar(t.m_Text, st).type;
			}
			
			if(acType.equals("CHAR")){
				acType = "NUM";
			}
			if(eleType.equals("CHAR")){
				eleType = "NUM";
			}
			
			if(!eleType.equals(acType)){
				Exception e = new Exception(t.m_Text+":Not match array type");
				throw e;
				//throw fault(not match array type)
			}
		}
		
		return result;
	}
	
	private Integer checkBoom(SimpleNode BoomNode){
		
		SimpleNode pix = BoomNode;
		
		if(!MyNewGrammarTreeConstants.jjtNodeName[pix.id].equals("BOOM")){
			return -1;
		}
		
		while(!MyNewGrammarTreeConstants.jjtNodeName[pix.id].equals("ROOT")){
			if(MyNewGrammarTreeConstants.jjtNodeName[pix.id].equals("BODY")){
				Integer body = Integer.parseInt(pix.m_Text);
				for(String key:root.funcs.keySet()){
					if(root.funcs.get(key).body.equals(body)){
						return 0;
					}
				}
				return 1;
			}
			pix = (SimpleNode)pix.jjtGetParent();
		}
		
		return 0;
	} 
	
	public SymbolTable findTablebyBody(SymbolTable st, Integer body){
		 
		 SymbolTable result = null;
		 
		 if(st.body.equals(body)){
			 return st;
		 }
		 
		 for(int i=0; i<st.children.size(); ++i){
			 result = findTablebyBody((SymbolTable)st.children.get(i), body);
			 if(result!=null){
				 return result;
			 }
		 }
		 
		 return result;
	}
	
	
	private String semanticCheckbyFunc(SimpleNode funcBodyNode, String acType) throws Exception{
		
		String funcType = semanticCheckbyBody(funcBodyNode);
		
		if(funcType.equals("")){
			Exception e = new Exception(((SimpleNode)((SimpleNode)funcBodyNode.parent).jjtGetChild(0)).m_Text+":No ret");
			throw e;
			//throw fault(no ret) 
		}else{
			
			if(funcType.equals("CHAR")){
				funcType = "NUM";
			}
			if(acType.equals("CHAR")){
				acType = "NUM";
			}
			
			if(!funcType.equals(acType)){
				Exception e = new Exception(((SimpleNode)((SimpleNode)funcBodyNode.parent).jjtGetChild(0)).m_Text+":Ret not match funciton type");
				throw e;
				//throw fault(ret not match func type)
			}
		}
		
		return "1";
	}
	
	//semantic check except duplic and not define fault
	private String semanticCheckbyBody(SimpleNode BodyNode) throws Exception{
		
		String result = "";
		SymbolTable st = findTablebyBody(root, Integer.parseInt(BodyNode.m_Text));
		
		Integer current_child = 0;
		while(current_child < BodyNode.jjtGetNumChildren()){
			String expType, acType, bodyType;
			SimpleNode t = (SimpleNode)(BodyNode.jjtGetChild(current_child));
			SimpleNode tc;
			switch(MyNewGrammarTreeConstants.jjtNodeName[t.id]){
			case "CALL":
				mtCall(t, st);
				break;
			case "RET":
				expType = mtTerm((SimpleNode)t.jjtGetChild(0), st);
				result = expType; 
				break;
			case "LET":
				acType = mtVar((SimpleNode)t.jjtGetChild(0), st);
				if(MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)t.jjtGetChild(1)).id].equals("BULK")){
					mtBulk((SimpleNode)t.jjtGetChild(1), st, acType);
				}else{
					expType = mtExpre((SimpleNode)t.jjtGetChild(1), st);
					if(acType.equals("CHAR")){
						acType = "NUM";
					}
					if(!expType.equals(acType)){
						Exception e = new Exception("LET "+((SimpleNode)((SimpleNode)t.jjtGetChild(0)).jjtGetChild(0)).m_Text+"...:Expre not match variable type");
						throw e;
						//throw fault(expre type not match);
					}
				}
				break;
			case "LOOP":
			case "BRANCH":
				expType = mtExpre((SimpleNode)(t.jjtGetChild(0)), st);
				acType = "NUM";
				if(!expType.equals(acType)){
					SimpleNode first = t;
					while(first.m_Text.equals("")){
						first = (SimpleNode)first.jjtGetChild(0);
					}
					Exception e = new Exception(MyNewGrammarTreeConstants.jjtNodeName[t.id]+"("+first.m_Text+"...):Type of expre in branch/loop must be NUM");
					throw e;
					//throw fault(expre type not match);
				}
				bodyType = semanticCheckbyBody((SimpleNode)t.jjtGetChild(1)); 
				if(!bodyType.equals("")){
					result = bodyType; 
				}
				break;
			case "BOOM":
				Integer ac = checkBoom(t);
				if(!ac.equals(1)){
					Exception e = new Exception("BOOM must within LOOP or Branch");
					throw e;
					//throw fault(BOOM fault)
				}
				break;
			/*
			case "FOREACH": 
				expType = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)t.jjtGetChild(0)).id];
				acType = getVar(st, ((SimpleNode)t.jjtGetChild(0)).m_Text).type;
				if(acType.equals("CHAR")){
					acType = "NUM";
				}
				if(!expType.equals(acType)){
					SimpleNode first = (SimpleNode)t.jjtGetChild(0);
					Exception e = new Exception("FOREACH "+MyNewGrammarTreeConstants.jjtNodeName[first.id]+"...):Type of foreach not match");
					throw e;
					//throw fault(expre type not match);
				}
				bodyType = semanticCheckbyBody((SimpleNode)t.jjtGetChild(2));
				if(!bodyType.equals("")){
					result = bodyType;
				}
				break;
				*/
			}
			current_child += 1;
		}
		return result;
	}
	
}
