package STgenerator;

import java.util.ArrayList;

import STgenerator.SymbolTable.fsItem;
import STgenerator.SymbolTable.vsItem;
import TreeGenerator.*;


public class Generator {
	
	public SymbolTable root;
	public ArrayList<String> constFloat = new ArrayList<String>();
	
	private Integer current_body = new Integer(0);
	
	//this func must be call right after root initiated
	private String CreateInline(){
		
		//create inline func
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
		
		root.AddEfunc("inputnum");
		root.funcs.get("inputnum").type = "CHAR";
		root.AddEfunc("inputcha");
		root.funcs.get("inputcha").type = "NUM";
		root.AddEfunc("inputflo");
		root.funcs.get("inputflo").type = "FLOAT";
		
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
	private String addConstFloat(SimpleNode startNode){
		
		if(MyNewGrammarTreeConstants.jjtNodeName[startNode.id].equals("FLOAT")){
			String tmp = startNode.m_Text;
			startNode.m_Text = ((Integer)constFloat.size()).toString();
			constFloat.add(tmp);
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
	
	public SymbolTable generate(SimpleNode root, Integer base, SymbolTable papa, ArrayList<vsItem> FormalVar){
		
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
					tc = (SimpleNode)(t.jjtGetChild(0));
					tc.m_Text = tc.m_Text+"_"+result.body.toString();
					
					if(result.vars.containsKey(tc.m_Text)){
						//throw fault(duplicate var name)
					}
					
					result.AddEvar(tc.m_Text);
					vsItem var = (vsItem)(result.vars.get(tc.m_Text));
					var.type = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(t.jjtGetNumChildren()-1))).id];
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
				}else{
					//throw fault(func and obj definition can only be global)
				}
				break;
			case "CALL":
				accept = emCall(t,allType,result);
				if(!accept.equals("1")){
					//throw fault(not defined function or not defined var or not match paraType)
				}
				break;
			case "RET":
				accept = emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result);
				if(!accept.equals("1")){
					//throw fault(not define var or not match type var);
				}
				break;
			case "LET":
				accept = emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result);
				if(!accept.equals("1")){
					//throw fault(not define var or not match type var);
				}
				if(MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)(t.jjtGetChild(1))).id].equals("BULK")){
					accept = emBulk((SimpleNode)(t.jjtGetChild(1)), result);
				}else{
					accept = emExpre((SimpleNode)(t.jjtGetChild(1)), allType, result);
				}
				if(!accept.equals("1")){
					//throw fault(not define var or not match type var);
				}
				break;
			case "LOOP":
			case "BRANCH":
				accept = emExpre((SimpleNode)(t.jjtGetChild(0)), allType, result);
				if(!accept.equals("1")){
					//throw fault(not define var of not match type var);
				}
				tc = (SimpleNode)(t.jjtGetChild(1));
				current_body += 1;
				tc.m_Text = current_body.toString();
				result.children.add(generate(tc, current_loc, result, tmp));		   
				break;
			case "FOREACH": 
				tc = (SimpleNode)(t.jjtGetChild(2));
				current_body += 1;
				tc.m_Text = current_body.toString();
				result.children.add(generate(tc, current_loc, result, tmp));
				break;
			}
			current_child += 1;
		}
		return result;
	}
	
	public SymbolTable generateRoot(SimpleNode root){
		
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
					tc = (SimpleNode)(t.jjtGetChild(0));
					tc.m_Text = tc.m_Text+"_"+result.body.toString();
					if(result.vars.containsKey(tc.m_Text)){
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
				}else{
					//NOT consider object yet
					tmp_body ++;
					tc = (SimpleNode)(t.jjtGetChild(0));
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
				}			
			}else{
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
	
	public String emBulk(SimpleNode bulk, SymbolTable st){
		
		String result = "1";
		
		for(int i=0; i<bulk.jjtGetNumChildren(); ++i){
			SimpleNode t = (SimpleNode)bulk.jjtGetChild(i);
			if(MyNewGrammarTreeConstants.jjtNodeName[t.id].equals("SYMBOL")){
				
				vsItem vitem = locateVar(t.m_Text, st);
				
				if(vitem == null){
					return t.m_Text+"2";
				}
				
				t.m_Text = vitem.name;
			}
		}
		
		return result;
	}
	
	public String emExpre(SimpleNode expre, ArrayList<String> type, SymbolTable st){ /*return "1" present ok, return symbol with
																		  postfix 2 present not deifined symbol, with 
																		  postfix 0 present not match type*/
		
		//deal with call node
		if(MyNewGrammarTreeConstants.jjtNodeName[expre.id].equals("CALL")){
			String tmp = emCall(expre, type, st);
			return tmp;
		}
		
		//check this node if terminate
		if(!expre.m_Text.equals("")){
			
			String etype;
			if(MyNewGrammarTreeConstants.jjtNodeName[expre.id].equals("SYMBOL")){
				vsItem vitem = locateVar(expre.m_Text, st);
				
				if(vitem == null){
					return expre.m_Text+"2";
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
			return expre.m_Text+"0";
		}
		
		//check children if non-terminate
		Integer i = 0;
		while(i<expre.jjtGetNumChildren()){
			String accept = emExpre((SimpleNode)(expre.jjtGetChild(i)), type, st); 
			if(!accept.equals("1")){
				return accept;
			}
			++i;
		}
		
		return "1";
	}
	
	public String emCall(SimpleNode nodeCall, ArrayList<String> type, SymbolTable st){
	
		String accept;
		
		if(this.root.funcs.containsKey(((SimpleNode)(nodeCall.jjtGetChild(0))).m_Text)){
			for(int i=1; i<nodeCall.jjtGetNumChildren(); ++i){
				accept = emExpre((SimpleNode)(nodeCall.jjtGetChild(i)), type, st);
				if(!accept.equals("1")){
					return accept;
				}
			}
		}else{
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

	public String mtExpre(SimpleNode expre, SymbolTable st){
		
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
	
	public String mtCond(SimpleNode cond, SymbolTable st){
		
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
	
	public String mtTerm(SimpleNode term, SymbolTable st){
		
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
	
	public String mtSub(SimpleNode subterm, SymbolTable st){
		
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
	
	public String mtAtom(SimpleNode atom, SymbolTable st){
		
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
	
	public String mtVar(SimpleNode var, SymbolTable st){
		
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
	
	public String mtCall(SimpleNode call, SymbolTable st){
		
		String result = "NUM";
		
		SimpleNode funcName = (SimpleNode)call.jjtGetChild(0);
		fsItem fitem = getFuncInfo(funcName.m_Text);
		
		if(!fitem.getNumpara().equals(call.jjtGetNumChildren()-1)){
			//throw fault(number of parameters is not match)
		}
		
		for(int i=0; i<fitem.getNumpara(); ++i){
			String type = mtTerm((SimpleNode)call.jjtGetChild(i+1), st);
			String target = "NUM";
			if(((String)fitem.getPara(i).get(1)).equals("FLOAT")){
				target = "FLOAT";
			}
			if(!type.equals(target)){
				//throw fault(not match parameter type)
			}
		}
		
		if(fitem.type.equals("FLOAT")){
			result = "FLOAT";
		}
		
		call.m_Text = result;
		return result;
	}
	
	public String mtBulk(SimpleNode bulk, SymbolTable st, String acType){
		
		String result = "1";
		
		for(int i=0; i<bulk.jjtGetNumChildren(); ++i){
			SimpleNode t = (SimpleNode)bulk.jjtGetChild(i);
			String eleType = MyNewGrammarTreeConstants.jjtNodeName[t.id];
			if(MyNewGrammarTreeConstants.jjtNodeName[t.id].equals("SYMBOL")){
				eleType = locateVar(t.m_Text, st).type;
			}
			if(!eleType.equals(acType)){
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
	
	
	private String semanticCheckbyFunc(SimpleNode funcBodyNode, String acType){
		
		String funcType = semanticCheckbyBody(funcBodyNode);
		
		if(funcType.equals("")){
			//throw fault(no ret) 
		}else{
			if(!funcType.equals(acType)){
				//throw fault(ret not match func type)
			}
		}
		
		return "1";
	}
	
	//semantic check except duplic and not define fault
	private String semanticCheckbyBody(SimpleNode BodyNode){
		
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
					if(!expType.equals(acType)){
						//throw fault(expre type not match);
					}
				}
				break;
			case "LOOP":
			case "BRANCH":
				expType = mtExpre((SimpleNode)(t.jjtGetChild(0)), st);
				acType = "NUM";
				if(!expType.equals(acType)){
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
					//throw fault(BOOM fault)
				}
				break;
			case "FOREACH": 
				expType = MyNewGrammarTreeConstants.jjtNodeName[((SimpleNode)t.jjtGetChild(0)).id];
				acType = getVar(st, ((SimpleNode)t.jjtGetChild(0)).m_Text).type;
				if(acType.equals("CHAR")){
					acType = "NUM";
				}
				if(!expType.equals(acType)){
					//throw fault(expre type not match);
				}
				bodyType = semanticCheckbyBody((SimpleNode)t.jjtGetChild(2));
				if(!bodyType.equals("")){
					result = bodyType;
				}
				break;
			}
			current_child += 1;
		}
		return result;
	}
	
}
