package mytree;

import java.util.ArrayList;

import STgenerator.Generator;
import STgenerator.SymbolTable.vsItem;
import linker.Linker;

public class MyTree {
	public final int DWORD_LENGTH = 4;
	
	public int whileflag = 1;
	public int outFlag = 1;
	public int flag = 2;
	public int cntOrder = 0;
	public int floatOrder = 0;
	public int labelOrder = 0;
	public ExpNode root = null;
	public ArrayList<String>threeAddCodes = null;
	public ArrayList<String>dataCodes = null;
	public Generator tableManager = null;
	public MyTree() {
		threeAddCodes = new ArrayList<String>(); 
		dataCodes = new ArrayList<String>(); 
	}
	public void setRoot(ExpNode root){
		this.root = root;
	}
	public void setTableManager(Generator tableManager){
		this.tableManager = tableManager;
	}
	
	public void tree2code(){
		//System.out.println("root children "+root.expChildren.size());
		if(outFlag==0){
			threeAddCodes.add(".data");
			for(String key:tableManager.root.vars.keySet()){
				threeAddCodes.add(dataDefCode(key,tableManager.root.vars.get(key).type,tableManager.root.vars.get(key).isArray));
			}
			threeAddCodes.add(".code");
		}
		else if(outFlag==1){
			for(String key:tableManager.root.vars.keySet()){
				dataCodes.add(dataDefCode(key,tableManager.root.vars.get(key).type,tableManager.root.vars.get(key).isArray));
			}
		}
		//System.out.println("local size "+tableManager.root.vars.size());
		bodyStm(root,"");
	}
	public String getDataCodesString(){
		String resString = "";
		String whiteLine = "\n    ";
		for(int i=0;i<dataCodes.size();i++){
			resString+=(dataCodes.get(i)+whiteLine);
		}
		return resString;
	}

	public String getCodeCodesString(){
		String resString = "";
		String whiteLine = "\n    ";
		for(int i=0;i<threeAddCodes.size();i++){
			resString+=(threeAddCodes.get(i)+whiteLine);
		}
		return resString;
	}
	public void outputCodes(){
		System.out.println("here out "+threeAddCodes.size());
		System.out.println("BEGIN");
		tableManager.root.print();
		for(int i =0;i<threeAddCodes.size();i++){
			if(threeAddCodes.get(i).charAt(0)!='J'&&threeAddCodes.get(i).charAt(0)!='.' &&threeAddCodes.get(i).charAt(threeAddCodes.get(i).length()-1)!='c')
				System.out.println("    "+threeAddCodes.get(i));
			else
				System.out.println(threeAddCodes.get(i));
		}
		Linker.LinkEntrance(getDataCodesString(), getCodeCodesString(), "flag");
	}
	public void LDR(ExpNode root){
		//LET a AS 1+2+3+6!@BOOM!
		if(root ==null){
			return;
		}
		else{
			for(int i=0;i<root.expChildren.size();i++){
				if(root.expChildren.get(i).content.equals("expre")){
					expressLRD(root.expChildren.get(i));
				}
				else{
					threeAddCodes.add("others"+root.expChildren.get(i).content+"??");
					LDR(root.expChildren.get(i));
				}
			}
		}
	}
	
	public void bodyStm(ExpNode bodyNode,String condLabel){
		//System.out.println("here in body stm and children is "+bodyNode.expChildren.size());
		for(int i = 0;i<bodyNode.expChildren.size();i++){
			if(bodyNode.expChildren.get(i).content.equals("LET")){
				letStm(bodyNode.expChildren.get(i));
			}
			else if(bodyNode.expChildren.get(i).content.equals("BRANCH")){
				branchStm(bodyNode.expChildren.get(i));
			}
			else if(bodyNode.expChildren.get(i).content.equals("LOOP")){
				//System.out.println("loop next");
				loopStm(bodyNode.expChildren.get(i));
			}
			else if(bodyNode.expChildren.get(i).content.equals("RET")){
				retStm(bodyNode.expChildren.get(i));
			}
			else if(bodyNode.expChildren.get(i).content.equals("BOOM")){
				boomStm(bodyNode.expChildren.get(i),condLabel);
			}
			else if(bodyNode.expChildren.get(i).content.equals("FOREACH")){
				threeAddCodes.add("foreach remaining");
			}
			else if(bodyNode.expChildren.get(i).content.equals("CALL")){
				callStm(bodyNode.expChildren.get(i),"");
			}
			else if(bodyNode.expChildren.get(i).content.equals("ROOT")){
				bodyStm(bodyNode.expChildren.get(i),"");
			}
			else if(bodyNode.expChildren.get(i).content.equals("DEF")){
				if(judgeDef(bodyNode.expChildren.get(i)))
					defStm(bodyNode.expChildren.get(i));
				else{
					//simpleDef(bodyNode.expChildren.get(i));
				}
			}
			else{
				threeAddCodes.add("magic "+bodyNode.expChildren.get(i).content);
			}
		}
	}
	
	public boolean judgeDef(ExpNode defNode){
		String type = defNode.expChildren.get(defNode.expChildren.size()-1).content;
		return (type.equals("BODY"));
	}
	public void simpleDef(ExpNode defNode){
		//threeAddCodes.add("LOCAL "+defNode.expChildren.get(0).mText+":DWORD");
		threeAddCodes.add("should not define here");
	}
	public void defStm(ExpNode defNode){
		int nodeOrder = 0;
		String funcName = defNode.expChildren.get(nodeOrder).mText;
		nodeOrder++;
		ArrayList<String> params = new ArrayList<String>();
		if(defNode.expChildren.get(nodeOrder).content.equals("PARAS")){
			params = getFuncParams(defNode.expChildren.get(nodeOrder));
			System.out.println("out params is "+params.size());
			nodeOrder++;
		}
		//System.out.println("here paras is "+params.size());
		threeAddCodes.add(funcBegCode(funcName,params));
		//skip type node of function
		nodeOrder++;
		//local var of func
		System.out.println("here get function var list "+funcName+" "+ tableManager.getAllvar(funcName).size());
		for(int i =0;i<tableManager.getAllvar(funcName).size();i++){
			vsItem cntItem = (vsItem)(tableManager.getAllvar(funcName).get(i));
			//System.out.println("hujiajunshaji "+cntItem.type);
			if(cntItem.isFormal==0)
				threeAddCodes.add(varDefCode(cntItem.name,cntItem.type,cntItem.isArray));
		}
		//body of function
		bodyStm(defNode.expChildren.get(nodeOrder), "");
		threeAddCodes.add(funcEndCode(funcName));
		
	}
	public ArrayList<String>getFuncParams(ExpNode paramsNode){
		System.out.println("here paras is "+paramsNode.expChildren.size());
		ArrayList<String>params = new ArrayList<String>();
		for(int i=0;i<paramsNode.expChildren.size();i++){
			if(paramsNode.expChildren.get(i).expChildren.size()==2)
				params.add(paramsNode.expChildren.get(i).expChildren.get(1).mText);
			else if(paramsNode.expChildren.get(i).expChildren.size()==3)
				params.add(paramsNode.expChildren.get(i).expChildren.get(2).mText);
		}
		return params;
	}
	public String expressStm(ExpNode expRoot){
		return expressLRD(expRoot);
		//return expressCodes;
	}

	public void letStm(ExpNode letNode){
		int arrayInitFlag = 0;
		String expVal = "";
		if(letNode.expChildren.get(1).content.equals("expre")){
			expVal = expressLRD(letNode.expChildren.get(1));
		}
		else if(letNode.expChildren.get(1).content.equals("BULK")){
			arrayInitFlag = 1;
			arrayInit(letNode.expChildren.get(0),letNode.expChildren.get(1));
		}
		else threeAddCodes.add("let expre boom");
		if(arrayInitFlag==0){
			if(letNode.expChildren.get(0).content.equals("VAR")){
				//WARNING hard cord
				if(letNode.expChildren.get(1).mText.equals("FLOAT")){
					System.out.println(letNode.expChildren.get(1).mText+"hahahflaot");
					threeAddCodes.add(f_letValCode(varLRD(letNode.expChildren.get(0),""),expVal));
				}
				else{
					System.out.println(letNode.expChildren.get(1).mText+"hahah");
					threeAddCodes.add(letValCode(varLRD(letNode.expChildren.get(0),""),expVal));
				}
				//threeAddCodes.add("mov "+varLRD(letNode.expChildren.get(0),"")+","+judgeReg(expVal));;
			}
			else threeAddCodes.add("let var boom");
		}
	}
	/*整型数组情况
	 *	考虑mov a[const] b是否可用
	 *		可用即  以index 变量->变量
	 *		反之     取寄存器为中间值
	 *	
	 */
	public void arrayInit(ExpNode arrayNode,ExpNode termNode){
		String arrayName = arrayNode.expChildren.get(0).mText;
		int cntReg = 0;
		if(tableManager.getVar("", arrayName).type.equals("NUM")){
			for(int i =0;i<termNode.expChildren.size();i++){
				cntReg = cntOrder;
				threeAddCodes.add(get1AddCode("",termNode.expChildren.get(i).mText));
				threeAddCodes.add(letValCode(arrayName+"["+i+"*4]",cntReg+""));
			}
		}
		else{
			for(int i =0;i<termNode.expChildren.size();i++){
				cntReg = floatOrder;
				threeAddCodes.add(f_get1AddCode("",termNode.expChildren.get(i).mText));
				threeAddCodes.add(f_letValCode(arrayName+"["+i+"*8]",cntReg+""));
				//threeAddCodes.add("float!");
			}
		}
	}
	
	public void branchStm(ExpNode branchNode){
		//<header>
		String stackReg = "";
		if(whileflag == 0){
			stackReg = cntOrder+"";
			threeAddCodes.add(pushCode());
		}
		//<condition>
		//REMAINING put result in eax
		String expVal = "";
		if(branchNode.expChildren.get(0).content.equals("expre")){
			expVal = expressLRD(branchNode.expChildren.get(0));
		}
		else threeAddCodes.add("branch expre boom");
		threeAddCodes.add(regMovCode(expVal));
		
		int finalLabel = labelOrder;
		if(whileflag==0){
			//WARNING here add labelOrder 
			labelOrder++;
			threeAddCodes.add(JudgeTmpCode(finalLabel+""));
		}
		else if(whileflag==1){
			threeAddCodes.add(branchBegCode());
		}
		//<content>
		if(branchNode.expChildren.get(1).content.equals("BODY")){
			bodyStm(branchNode.expChildren.get(1),finalLabel+"");
		}
		else threeAddCodes.add("branch body boom");
		if(whileflag==0){
			threeAddCodes.add(labelCode(finalLabel+""));
			threeAddCodes.add(popCode(stackReg));
		}
		else if(whileflag==1){
			threeAddCodes.add(branchEndCode());
		}
	}
	public void loopStm(ExpNode loopNode){
		//<header>
		int condLabel = 0;
		String stackReg = "";
		if(whileflag==0){
			stackReg = cntOrder+"";
			threeAddCodes.add(pushCode());
			condLabel = labelOrder;
			threeAddCodes.add(labelCode());
		}
		else if(whileflag==1){
			;
		}
		//<condition>
		String BegExpVal = "";
		if(loopNode.expChildren.get(0).content.equals("expre")){
			BegExpVal = expressLRD(loopNode.expChildren.get(0));	
		}
		else threeAddCodes.add("loop expre boom");
		
		//move result to eax
		threeAddCodes.add(regMovCode(BegExpVal));
		
		int endLabel = labelOrder; 
		if(whileflag==0){
			//WARNING here add labelOrder
			labelOrder++;
			threeAddCodes.add(JudgeTmpCode(endLabel+""));
		}
		else if(whileflag==1){
			threeAddCodes.add(loopBegCode());
		}
		//<content>
		if(loopNode.expChildren.get(1).content.equals("BODY")){
			bodyStm(loopNode.expChildren.get(1),endLabel+"");
		}
		else threeAddCodes.add("loop body boom");
		
		//<condition>
		String judgeExpVal = "";
		if(loopNode.expChildren.get(0).content.equals("expre"))
			judgeExpVal = expressLRD(loopNode.expChildren.get(0));
		//move result to eax
		threeAddCodes.add(regMovCode(judgeExpVal));
		
		if(whileflag==0){
			threeAddCodes.add(getJmpCode("jmp",condLabel+""));
			threeAddCodes.add(labelCode(endLabel+""));
			threeAddCodes.add(popCode(stackReg));
		}
		else if(whileflag==1){
			threeAddCodes.add(loopEndCode());
		}
	}
	public void callStm(ExpNode callNode,String parentVal){
		String funcName ="";
		if(callNode.expChildren.get(0).content.equals("SYMBOL")){
			funcName = callNode.expChildren.get(0).mText;
		}
		else threeAddCodes.add("call boom");
		String argvString = "";
		for(int i =1; i<callNode.expChildren.size();i++){
			if(!callNode.expChildren.get(i).content.equals("term")) threeAddCodes.add("call term boom");
			//store var to argv list
			//REMAIN differ float reigster and normal register
			String cntVal = termLRD(callNode.expChildren.get(i));
			//argv is array
			if(tableManager.getFuncInfo(funcName).parasIsarray.get(i-1)!=0){
				if(tableManager.getVar("", cntVal).isFormal!=0){
					argvString+=","+cntVal;
				}
				else{
					argvString+=",addr "+cntVal;
				}
			}
			else{
				threeAddCodes.add(letValCode("_NUM"+(i-1),cntVal));
				argvString+=","+"_NUM"+(i-1);	
			}
		}
		threeAddCodes.add(callCode(funcName,argvString));
		//WARNING hard code
		//judge whether the caller should has return value
		if(!parentVal.equals(""))
			//threeAddCodes.add("t"+parentVal+"="+"eax");
			threeAddCodes.add(get1AddCodeSP(parentVal,0+""));
	}
	
	public void retStm(ExpNode retNode){
		String resVal = "";
		if(retNode.expChildren.get(0).content.equals("term")){
			resVal = termLRD(retNode.expChildren.get(0));
		}
		threeAddCodes.add(regMovCode(resVal));
		threeAddCodes.add(retStmCode());
	}
	
	public void boomStm(ExpNode boomNode,String boomLabel){
		threeAddCodes.add(getJmpCode("jmp",boomLabel));
	}
	
	public String expressLRD(ExpNode expNode){
		int resVal = 0;
		String condResString = "";
		if(expNode.mText.equals("NUM")){
			resVal = cntOrder;
			for(int i = 0;i<expNode.expChildren.size();i++){
				if(expNode.expChildren.get(i).content.equals("COND")){
					condResString = conditionLRD(expNode.expChildren.get(i));
					if(condResString.equals("")){
						//here execute expre
					}
					else{
						//System.out.println("after lower hiy res is "+condResString);
						//succOrder = cntOrder;
						//threeAddCodes.add(get1AddCode("",condResString));
					}
				}
				else if(expNode.expChildren.get(i).content.equals("AND")){
					condResString = conditionLRD(expNode.expChildren.get(i+1)); 
					i++;
					threeAddCodes.add(get3AddCode("and",resVal,condResString));
				}
				else if(expNode.expChildren.get(i).content.equals("NOT")){
					threeAddCodes.add(get2AddCode("not"));
				}
				else if(expNode.expChildren.get(i).content.equals("OR")){
					condResString = conditionLRD(expNode.expChildren.get(i+1)); 
					i++;
					threeAddCodes.add(get3AddCode("or",resVal,condResString));
				}
				else{
					threeAddCodes.add("express fail");
				}
			}
		}
		else{
			resVal = floatOrder;
			for(int i = 0;i<expNode.expChildren.size();i++){
				if(expNode.expChildren.get(i).content.equals("COND")){
					condResString = conditionLRD(expNode.expChildren.get(i));
					if(condResString.equals("")){
						//here execute expre
					}
					else{
						//System.out.println("after lower hiy res is "+condResString);
						//succOrder = cntOrder;
						//threeAddCodes.add(get1AddCode("",condResString));
					}
				}
				else if(expNode.expChildren.get(i).content.equals("AND")){
					condResString = conditionLRD(expNode.expChildren.get(i+1)); 
					i++;
					threeAddCodes.add(get3AddCode("and",resVal,condResString));
				}
				else if(expNode.expChildren.get(i).content.equals("NOT")){
					threeAddCodes.add(get2AddCode("not"));
				}
				else if(expNode.expChildren.get(i).content.equals("OR")){
					condResString = conditionLRD(expNode.expChildren.get(i+1)); 
					i++;
					threeAddCodes.add(get3AddCode("or",resVal,condResString));
				}
				else{
					threeAddCodes.add("express fail");
				}
			}
		}
		return resVal+"";
	}
	public String conditionLRD(ExpNode condNode){
		int floatFlag = 0;
		int resVal = cntOrder;
		String leftString = "";
		String rightString = "";
		for(int i = 0;i<condNode.expChildren.size();i++){
			if(condNode.expChildren.get(i).content.equals("term")){
				leftString = termLRD(condNode.expChildren.get(i));
				if(condNode.expChildren.get(i).mText.equals("FLOAT"))
					floatFlag=1;
				if(leftString.equals("")){
				}
				else{
				}
			}
			else if(condNode.expChildren.get(i).content.equals("LESS")){
				rightString = termLRD(condNode.expChildren.get(i+1)); 
				i++;
				//threeAddCodes.add(get3AddCode("sub",resVal,termResString));
				//threeAddCodes.add(get3AddCode("sub",Integer.parseInt(rightString),""+resVal));
				//move result register to eax
				//threeAddCodes.add(regMovCode(resVal+"",rightString));
				if(floatFlag==0)
					compareCode(leftString,rightString,"LESS",resVal+"");
				else
					f_compareCode(leftString,rightString,"LESS",resVal+"");
			}
			else if(condNode.expChildren.get(i).content.equals("LARGER")){
				rightString = termLRD(condNode.expChildren.get(i+1)); 
				i++;
				compareCode(leftString,rightString,"LARGER",resVal+"");
				//threeAddCodes.add(get3AddCode("sub",resVal,rightString));
			}
			else if(condNode.expChildren.get(i).content.equals("EQUAL")){
				rightString = termLRD(condNode.expChildren.get(i+1)); 
				i++;
				compareCode(leftString,rightString,"EQUAL",resVal+"");
				//threeAddCodes.add(get3AddCode("sub",resVal,rightString));
			}
			else if(condNode.expChildren.get(i).content.equals("LE")){
				rightString = termLRD(condNode.expChildren.get(i+1)); 
				i++;
				////threeAddCodes.add(get3AddCode("sub",resVal,termResString));
				//threeAddCodes.add(get3AddCode("sub",Integer.parseInt(rightString),""+resVal));
				//move result register to eax
				//threeAddCodes.add(regMovCode(resVal+"",rightString));
				compareCode(leftString,rightString,"LE",resVal+"");
			}
			else if(condNode.expChildren.get(i).content.equals("BE")){
				rightString = termLRD(condNode.expChildren.get(i+1)); 
				i++;
				compareCode(leftString,rightString,"BE",resVal+"");
				//threeAddCodes.add(get3AddCode("sub",resVal,rightString));
			}
			else{
				threeAddCodes.add("cond fail");
			}
		}
		return resVal+"";
	}
	public String termLRD(ExpNode termNode){
		int resVal = 0;
		String subtermResString = "";
		if(termNode.mText.equals("NUM")||termNode.mText.equals("CHAR")){
			resVal = cntOrder;
			for(int i = 0;i<termNode.expChildren.size();i++){
				//String cntContent = termNode.expChildren.get(i).content;
				if(termNode.expChildren.get(i).content.equals("subterm")){
					subtermResString = subtermLRD(termNode.expChildren.get(i),resVal+""); 
					if(subtermResString.equals("")){
						//here execute expre
					}
					else{
						//succOrder = cntOrder;
						//threeAddCodes.add(get1AddCode("",subtermResString));
						if((tableManager.getVar("", subtermResString)!=null)
								&&(tableManager.getVar("", subtermResString).isArray!=0))
							return subtermResString;
					}
				}
				else if(termNode.expChildren.get(i).content.equals("PLUS")){
					subtermResString = subtermLRD(termNode.expChildren.get(i+1),resVal+""); 
					i++;
					threeAddCodes.add(get3AddCode("add",resVal,subtermResString));
				}	
				else if(termNode.expChildren.get(i).content.equals("MINUS")){
					subtermResString = subtermLRD(termNode.expChildren.get(i+1),resVal+""); 
					i++;
					threeAddCodes.add(get3AddCode("sub",resVal,subtermResString));
				}
				else{
					threeAddCodes.add("term fail "+termNode.expChildren.get(i).content);
				}
			}
		}
		else{
			resVal = floatOrder;
			for(int i = 0;i<termNode.expChildren.size();i++){
				//String cntContent = termNode.expChildren.get(i).content;
				if(termNode.expChildren.get(i).content.equals("subterm")){
					subtermResString = subtermLRD(termNode.expChildren.get(i),resVal+""); 
					if(subtermResString.equals("")){
						//here execute expre
					}
					else{
						//succOrder = cntOrder;
						//threeAddCodes.add(get1AddCode("",subtermResString));
						if((tableManager.getVar("", subtermResString)!=null)
								&&(tableManager.getVar("", subtermResString).isArray!=0))
							return subtermResString;
					}
				}
				else if(termNode.expChildren.get(i).content.equals("PLUS")){
					subtermResString = subtermLRD(termNode.expChildren.get(i+1),resVal+""); 
					
					int cntVal = floatOrder;
					if(termNode.expChildren.get(i+1).mText.equals("NUM")){
						//call type shift
						String shiftRes = "";
						//num2Float();
						threeAddCodes.add(f_get1AddCode("",shiftRes));
					}
					i++; 
					//the code should let result to the resVal
					threeAddCodes.add(f_get3AddCode("add",resVal,cntVal+""));
				}	
				else if(termNode.expChildren.get(i).content.equals("MINUS")){
					subtermResString = subtermLRD(termNode.expChildren.get(i+1),resVal+"");
					int cntVal = floatOrder;
					if(termNode.expChildren.get(i+1).mText.equals("NUM")){
						//call type shift
						String shiftRes = "";
						threeAddCodes.add(f_get1AddCode("",shiftRes));
					}
					i++;
					//the code should let result to the resVal
					threeAddCodes.add(f_get3AddCode("sub",resVal,cntVal+""));
				}
				else{
					threeAddCodes.add("term fail float"+termNode.expChildren.get(i).content);
				}
			}
		}
		//System.out.println("term call "+termCallTimes);
		return resVal+"";
	}

	public String subtermLRD(ExpNode subtermNode,String parntVal){
		int resVal = 0;
		String atomResString = "";
		if(subtermNode.mText.equals("NUM")){
			resVal = cntOrder;
			for(int i = 0;i<subtermNode.expChildren.size();i++){
				if(subtermNode.expChildren.get(i).content.equals("atom")){
					atomResString=atomLRD(subtermNode.expChildren.get(i),resVal+""); 
					if(atomResString.charAt(atomResString.length()-1)==']'){
						resVal = cntOrder;
						threeAddCodes.add(get1AddCodeArray(parntVal, atomResString));
					}
					else if(atomResString.equals("call")){
						;
					}
					else{
						if(tableManager.getVar("", atomResString)!=null){
							if(tableManager.getVar("", atomResString).isArray==0)
								threeAddCodes.add(get1AddCode("",atomResString));
							else
								return atomResString;
						}
						else
							threeAddCodes.add(get1AddCode("",atomResString));
					}
				}
				else if(subtermNode.expChildren.get(i).content.equals("MULTIPLY")){
					int cntVal = cntOrder;
					atomResString=atomLRD(subtermNode.expChildren.get(i+1),resVal+"");
					threeAddCodes.add(get1AddCode("",atomResString));
					i++;
					threeAddCodes.add(get3AddCode("imul",resVal,cntVal+""));
				}
				else if(subtermNode.expChildren.get(i).content.equals("DIVIDE")){
					int cntVal = cntOrder;
					atomResString=atomLRD(subtermNode.expChildren.get(i+1),resVal+"");
					threeAddCodes.add(get1AddCode("",atomResString));
					i++;
					threeAddCodes.add(get3AddCode("idiv",resVal,cntVal+""));
				}
			}
		}
		else{
			resVal = floatOrder;
			for(int i = 0;i<subtermNode.expChildren.size();i++){
				if(subtermNode.expChildren.get(i).content.equals("atom")){
					atomResString=atomLRD(subtermNode.expChildren.get(i),resVal+""); 
					if(atomResString.charAt(atomResString.length()-1)==']'){
						resVal = cntOrder;
						threeAddCodes.add(get1AddCodeArray(parntVal, atomResString));
					}
					else if(atomResString.equals("call")){
						;
					}
					else{
						if(tableManager.getVar("", atomResString)!=null){
							if(tableManager.getVar("", atomResString).isArray==0)
								threeAddCodes.add(f_get1AddCode("",atomResString));
							else
								return atomResString;
						}
						else
							threeAddCodes.add(f_get1AddCode("",atomResString));
					}
				}
				else if(subtermNode.expChildren.get(i).content.equals("MULTIPLY")){
					int cntVal = floatOrder;
					atomResString=atomLRD(subtermNode.expChildren.get(i+1),resVal+"");
					if(subtermNode.expChildren.get(i+1).mText.equals("NUM")){
						//call type shift
						String shiftRes = "";
						threeAddCodes.add(f_get1AddCode("",shiftRes));
					} 
					else
						threeAddCodes.add(f_get1AddCode("",atomResString));
					i++;
					//the code should let result to the resVal
					threeAddCodes.add(f_get3AddCode("imul",resVal,cntVal+""));
				}
				else if(subtermNode.expChildren.get(i).content.equals("DIVIDE")){
					int cntVal = floatOrder;
					atomResString=atomLRD(subtermNode.expChildren.get(i+1),resVal+"");
					if(subtermNode.expChildren.get(i+1).mText.equals("NUM")){
						//call type shift
						String shiftRes = "";
						threeAddCodes.add(f_get1AddCode("",shiftRes));
					} 
					else
						threeAddCodes.add(f_get1AddCode("",atomResString));
					i++;
					//the code should let result to the resVal
					threeAddCodes.add(f_get3AddCode("idiv",resVal,cntVal+""));;
				}
			}
		}
		return resVal+"";
	}
	public String atomLRD(ExpNode atomNode,String parntVal){
		String contentString = atomNode.expChildren.get(0).content;
		if(contentString.equals("CHARACTER")||contentString.equals("NUM")||contentString.equals("FLOAT")){
			return atomNode.expChildren.get(0).mText;
		}
		else if(contentString.equals("VAR")){
			return varLRD(atomNode.expChildren.get(0),parntVal);
		}
		else if(contentString.equals("expre")){
			return "t"+expressStm(atomNode.expChildren.get(0));
		}
		else if(contentString.equals("CALL")){
			//WARNING hard code and disconsideration
			callStm(atomNode.expChildren.get(0),parntVal);
			return "call";
		}
		else
			return "atomLRD boom";
	}
	
	public String varLRD(ExpNode varNode,String parntVal){
		//WARNING var node only has one child?
		//String resVal = parntVal;
		String varContentString = "";
		if(varNode.expChildren.size()!=1){
			varContentString+=varNode.expChildren.get(0).mText;
			String termVal = termLRD(varNode.expChildren.get(1).expChildren.get(0));
			varContentString+="[t";
			varContentString+=termVal;
			varContentString+="]";
		}
		else{
			for(int i = 0;i<varNode.expChildren.size();i++){
				String contentString = varNode.expChildren.get(i).content; 
				if(contentString.equals("SYMBOL")){
					varContentString+=varNode.expChildren.get(i).mText;
				}
				else if(contentString.equals("ARRAY")){
					//WARNING hard code to mark register
					String termVal = termLRD(varNode.expChildren.get(i).expChildren.get(0));
					varContentString+="[t";
					varContentString+=termVal;
					varContentString+="]";
					threeAddCodes.add(get1AddCodeSP("", termVal));
				}
				else{
					varContentString+= "varLRD fail";
				}
			}
		}
		//System.out.println("here var res is "+varContentString);
		return varContentString;
	}
	
	public String funcBegCode(String funcName, ArrayList<String>params){
		String resString = funcName+" proc ";
		if(params.size()!=0 && params!=null){
			//System.out.println("funcbeg "+params.size());
			for(int i=0;i<params.size()-1;i++)
				resString+=(params.get(i)+",");
			resString+=params.get(params.size()-1);
		}
		return resString;
	}
	public String dataDefCode(String valName, String type, int isArray){
		String typeString = "";
		String typeInit = "0";
		if(type.equals("NUM")||type.equals("CHAR"))
			typeString = "DWORD";
		else if(type.equals("FLOAT")){
			typeString = "REAL8";
			typeInit = "0.0";
		}
		else
			typeString = "shitType";
		String resString = "";
		if(isArray==0){
			resString = (valName+" "+typeString+" "+typeInit);
		}
		else if(!valName.equals("global_float_const")){
			resString = (valName+" "+typeString+" ");
			for(int i=0;i<isArray-1;i++){
				resString+="0,";
			}
			resString+="0";
		} 
		else if(tableManager.constFloat.size()!=0){
			String cntConst = "0.0";
			resString = (valName+" "+typeString+" ");
			for(int i=0;i<tableManager.constFloat.size();i++){
				if(!tableManager.constFloat.get(i).equals("")){
					cntConst = tableManager.constFloat.get(i);
				}
				resString+=(cntConst+",");
			}
		} 
		return resString;
	}
	
	public String varDefCode(String valName, String type, int isArray){
		String typeString = "";
		if(type.equals("NUM")||type.equals("CHAR"))
			typeString = "DWORD";
		else if(type.equals("FLOAT"))
			typeString = "REAL8";
		else
			typeString = "shitType";
		String resString = "";
		if(isArray==0){
			resString = ("LOCAL "+valName+":"+typeString);
		}
		else if(isArray!=0){
			resString = ("LOCAL "+valName+"["+isArray*DWORD_LENGTH+"]:"+typeString);
		} 
		else {
			return "var Def boom";
		}
		return resString;
	}
	
	public String funcEndCode(String funcName){
		String resString = funcName+" endp";
		return resString;
	}
	public String regGeteax(String resVal){
		String resString = "t"+resVal+"=eax";
		String asmString = "mov t"+resVal+" eax";
		String regString = "mov "+judgeReg(Integer.parseInt(resVal))+",eax";
		//cntOrder++;
		//return resString;
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	public String regMovCode(String resVal){
		String resString = "eax= t"+resVal;
		String asmString = "mov eax t"+resVal;
		String regString = "mov eax,"+judgeReg(Integer.parseInt(resVal));
		//cntOrder++;
		//return resString;
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	public String regMovVar(String resVal,String srcVal){
		String resString = resVal+"="+srcVal;
		String asmString = "mov t"+resVal+srcVal;
		String regString = "mov "+judgeReg(resVal)+","+(srcVal);
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}

	public String regMovCode(String resVal,String srcVal){
		String resString = resVal+"= t"+srcVal;
		String asmString = "mov t"+resVal+" t"+srcVal;
		String regString = "mov "+judgeReg(resVal)+","+judgeReg(srcVal);
		//cntOrder++;
		//return resString;
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	public String pushCode(String register){
		String regString = "";
		regString = "push "+judgeReg(register);
		return regString;
	}
	
	public String pushCode(){
		String resString = "push t"+cntOrder;
		cntOrder++;
		return resString;
	}
	public String popCode(String regOrder){
		String resString = "pop "+judgeReg(regOrder);
		return resString;
	}
	public String callCode(String funcName,String argvString){
		return "invoke "+funcName+argvString;
	}
	public String branchBegCode(){
		return ".IF eax>0";
	}
	public String branchEndCode(){
		return ".ENDIF";
	}
	public String loopBegCode(){
		return ".WHILE eax>0";
	}
	public String loopEndCode(){
		return ".ENDW";
	}
	public String labelCode(){
		String resString = "J"+labelOrder+":";
		labelOrder++;
		return resString;
	}
	public String labelCode(String label){
		String resString = "J"+label+":";
		return resString;
	}
	
	public String JudgeTmpCode(String endLabel){
		String movStm = get1AddCode("",0+"");
		String cmpStm = get2AddCode("cmp",1);
		String jmpStm = getJmpCode("jge", endLabel);
		return movStm+"\n    "+cmpStm+"\n    "+jmpStm;
	}
	
	public String retStmCode(){
		String resString = "ret";
		return resString;
	}
	
	public String getJmpCode(String symbol,String jmpLabel){
		String resString = symbol+" J"+jmpLabel;
		return resString;
	}

	public String f_get1AddCode(String unusedsymbol,String value){
		//REMAINING here only evaluation
		String whiteLine = "\n    "; 
		String resString =  "tf"+cntOrder+"="+" "+value;
		String asmString = "mov "+"tf"+cntOrder+" "+value;
		if(value.charAt(0)=='t')
			value=judgeReg(value.substring(1));
		String regInit = "finit";
		String regPush = "fld "+""+value;
		if(value.matches("[\\d]+")){
			regPush = "fld "+"global_float_const["+value+"*8"+"]";			
		}
		String regPop  = "fstp "+"f_"+judgeReg(floatOrder);
		floatOrder+=1;
		
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regInit+whiteLine+regPush+whiteLine+regPop;
		else
			return resString;
	}
	public String num2Float(String value){
		return "invoke num2float,"+value;
	}
	
	public String get1AddCode(String unusedsymbol,String value){
		//REMAINING here only evaluation
		String resString =  "t"+cntOrder+"="+" "+value;
		String asmString = "mov "+"t"+cntOrder+" "+value;
		if(value.charAt(0)=='t')
			value=judgeReg(value.substring(1));
		String regString = "mov "+judgeReg(cntOrder)+","+value;
		cntOrder+=1;
		
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	//WARNING register evaluation
	public String letValCode(String resVal, String srcReg){
		int arrayFlag = 0;
		String arrayOrderNum = "";
		String srcVar = "";
		
		if(resVal.contains("t")&&resVal.contains("[")&&resVal.contains("]")){
			int kuohao = resVal.indexOf('[');
			int kuohao2 = resVal.indexOf(']');
			arrayOrderNum = resVal.substring(kuohao+2,kuohao2);
			System.out.println("arrayOrder "+arrayOrderNum);
			srcVar = resVal.substring(0,kuohao);
			arrayFlag = 1;
		}
		String resString = resVal+"="+" t"+srcReg;
		String asmString = "mov "+resVal+" t"+srcReg;
		String regString = "";
		String arrayLocString = "";
		
		if(arrayFlag == 0)
			regString = "mov "+resVal+","+judgeReg(srcReg);
		else{
			//if local
			System.out.println("null before srcVar is "+srcVar);
			if(tableManager.getVar("", srcVar).isFormal==0)
				regString = "mov "+srcVar+"["+judgeReg(arrayOrderNum)+"]"+","+judgeReg(srcReg);
			else{
				int arrayReg = cntOrder;
				arrayLocString = get1AddCode("",resVal.substring(0,resVal.indexOf('[')));
				regString = "mov "+"DWORD "+"ptr"+"["+judgeReg(arrayReg)+"+"+judgeReg(arrayOrderNum)+"*4"+"]"+","+judgeReg(srcReg);
				arrayLocString = arrayLocString+"\n    ";
			}
		}
		
		if(flag==1)
			return (arrayLocString+asmString);
		else if(flag==2)
			return (arrayLocString+regString);
		else
			return resString;
	}
	
	public String f_letValCode(String resVal, String srcReg){
		int arrayFlag = 0;
		String whiteLine = "\n    ";
		String arrayOrderNum = "";
		String srcVar = "";
		
		if(resVal.contains("t")&&resVal.contains("[")&&resVal.contains("]")){
			int kuohao = resVal.indexOf('[');
			int kuohao2 = resVal.indexOf(']');
			arrayOrderNum = resVal.substring(kuohao+2,kuohao2);
			System.out.println("arrayOrder "+arrayOrderNum);
			srcVar = resVal.substring(0,kuohao);
			arrayFlag = 1;
		}
		String resString = resVal+"="+" t"+srcReg;
		String asmString = "mov "+resVal+" t"+srcReg;
		String regString = "";
		String arrayLocString = "";
		
		if(arrayFlag == 0){
			//regString = "mov "+resVal+","+judgeReg(srcReg);
			regString = "finit"+whiteLine+"fld "+"f_"+judgeReg(srcReg)+whiteLine+"fstp "+resVal;
		}
		else{
			//if local
			if(tableManager.getVar("", srcVar).isFormal==0){
				//regString = "mov "+srcVar+"["+judgeReg(arrayOrderNum)+"]"+","+"f_"+judgeReg(srcReg);
				//regString =
				String regInit = "finit";
				String regPush = "fld "+""+"f_"+judgeReg(srcReg);
				String regPop  = "fstp "+srcVar+"["+judgeReg(arrayOrderNum)+"*8"+"]";
				regString = regInit+"\n    "+regPush+"\n    "+regPop;
			}
			else{
				int arrayReg = cntOrder;
				arrayLocString = get1AddCode("",resVal.substring(0,resVal.indexOf('[')));
				regString = "mov "+"DWORD "+"ptr"+"["+judgeReg(arrayReg)+"+"+judgeReg(arrayOrderNum)+"*4"+"]"+","+judgeReg(srcReg);
				arrayLocString = arrayLocString+"\n    ";
			}
		}
		
		if(flag==1)
			return (arrayLocString+asmString);
		else if(flag==2)
			return (arrayLocString+regString);
		else
			return resString;
	}
	
	public String get1AddCodeSP(String resVal, String srcVal){
		String resString =  "t"+resVal+"="+" t"+srcVal;
		String asmString = "mov "+"t"+resVal+" t"+srcVal;
		String regString = "mov "+judgeReg(resVal)+","+judgeReg(srcVal);
		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	public String get1AddCodeArray(String resVal, String srcVal){
		
		int kuohao = srcVal.indexOf('[');
		int kuohao2 = srcVal.indexOf(']');
		String srcReg = srcVal.substring(kuohao+2,kuohao2);
		String srcVar = srcVal.substring(0,kuohao);
		String resString =  "t"+resVal+"="+" "+srcVal;
		String asmString = resString;
		String regString = "mov "+judgeReg(resVal)+","+srcVar+"["+judgeReg(srcReg)+"]";
		
		String arrayLocString = "";
		//if local
		if(tableManager.getVar("", srcVar).isFormal==0)
			regString = "mov "+judgeReg(resVal)+","+srcVar+"["+judgeReg(srcReg)+"]";
		else{
			int arrayReg = cntOrder;
			arrayLocString = get1AddCode("",srcVal.substring(0,srcVal.indexOf('[')));
			regString = "mov "+judgeReg(resVal)+","+"DWORD "+"ptr"+"["+judgeReg(arrayReg)+"+"+judgeReg(srcReg)+"*4"+"]";
			arrayLocString = arrayLocString+"\n    ";
		}
		
		if(flag==1)
			return (arrayLocString+asmString);
		else if(flag==2)
			return (arrayLocString+regString);
		else
			return (resString);
	}
	
	public String get2AddCode(String symbol){
		String resString =  "t"+cntOrder+"="+" "+symbol+" "+"t"+(cntOrder+1);
		cntOrder+=2;
		return resString;
	}
	public String get2AddCode(String symbol, int state){
		String resString =  "t"+cntOrder+"="+" "+symbol+" "+"t"+(cntOrder-1);
		cntOrder+=1;
		return resString;
	}
	public String get3AddCode(String symbol){
		String resString =  "t"+cntOrder+"="+" "+"t"+(cntOrder+1)+" "+symbol+" "+"t"+(cntOrder+2);
		cntOrder+=3;
		return resString;
		
	}
	public String get3AddCode(String symbol, int resVal, String succVal){
		
		String resString =  "t"+resVal+"="+" "+symbol+" "+"t"+succVal;
		String asmString = symbol+" "+"t"+resVal+","+"t"+succVal;
		String regString = symbol+" "+judgeReg(resVal)+","+judgeReg(Integer.parseInt(succVal));

		if(flag==1)
			return asmString;
		else if(flag==2)
			return regString;
		else
			return resString;
	}
	public String f_get3AddCode(String symbol, int resVal, String succVal){
		String resString =  "ft"+resVal+"="+" "+symbol+" "+"ft"+succVal;
		String asmString = symbol+" "+"ft"+resVal+","+"ft"+succVal;
		String whiteLine = "\n    ";
		String regString = "invoke "+"f_"+symbol+" "+"f_"+judgeReg(resVal)+","+"f_"+judgeReg(Integer.parseInt(succVal));
		
		String setEaxInit = "finit";
		String setEaxPush = "fld "+"f_eax";
		String setEaxPop = "fstp "+"f_"+judgeReg(resVal);
		String setEaxString = setEaxInit+whiteLine+setEaxPush+whiteLine+setEaxPop;

		if(flag==1)
			return (asmString+"\n    "+setEaxString);
		else if(flag==2)
			return (regString+"\n    "+setEaxString);
		else
			return (resString+"\n    "+setEaxString);
	}
	
	public String get3AddCode(String symbol,String succ){
		String resString =  "t"+cntOrder+"="+"t"+(cntOrder+1)+symbol+" "+"t"+" "+(cntOrder+2);
		cntOrder+=3;
		return resString;
		
	}
	public String judgeReg(int order){
		if(order%5==0)
			return "esi";
		else if(order%5==1)
			return "ebx";
		else if(order%5==2)
			return "ecx";
		else if(order%5==3)
			return "edx";
		else if(order%5==4)
			return "edi";
		else {
			return "bloody crash";
		}
	}

	public String judgeReg(String orderString){
		int order = Integer.parseInt(orderString);
		if(order%5==0)
			return "esi";
		else if(order%5==1)
			return "ebx";
		else if(order%5==2)
			return "ecx";
		else if(order%5==3)
			return "edx";
		else if(order%5==4)
			return "edi";
		else {
			return "bloody crash";
		}
	}
	public String cmpAsmCode(String resVal,String srcVal){
		return "cmp "+judgeReg(resVal)+","+judgeReg(srcVal);
	}
	
	public void f_compareCode(String resValOri,String srcValOri,String symbol,String resReg){
		
		String resVal = "";
		String srcVal = "";
		if(symbol.equals("LESS")||symbol.equals("LE")){
			resVal = srcValOri;
			srcVal = resValOri;
		}
		else{
			resVal = resValOri;
			srcVal = srcValOri;
		}
		//WARNING hard code push ebx
		String regString = "";
		regString = "invoke "+"f_"+symbol+","+"f_"+judgeReg(resVal)+","+"f_"+judgeReg(srcVal);
		threeAddCodes.add(regString);
		threeAddCodes.add(regGeteax(resReg));
	}
	public void compareCode(String resValOri,String srcValOri,String symbol,String resReg){
		String resValReg = "1";
		String resVal = "";
		String srcVal = "";
		if(symbol.equals("LESS")||symbol.equals("LE")){
			resVal = srcValOri;
			srcVal = resValOri;
		}
		else{
			resVal = resValOri;
			srcVal = srcValOri;
		}
		if(srcVal.equals("1"))
			resValReg = "5";
		//WARNING hard code push ebx
		threeAddCodes.add(pushCode("2"));
		threeAddCodes.add(pushCode(resValReg));
		threeAddCodes.add(pushCode(srcVal));
		threeAddCodes.add(pushCode(resVal));
		threeAddCodes.add(popCode(resValReg));
		threeAddCodes.add(popCode("2"));
		
		threeAddCodes.add(cmpAsmCode(resValReg,"2"));
		threeAddCodes.add("mov eax,0");
		threeAddCodes.add(getCmpSymbol(symbol)+" @F");
		threeAddCodes.add("mov eax,1");
		threeAddCodes.add("@@:");
		threeAddCodes.add(popCode(resValReg));
		threeAddCodes.add(popCode("2"));	
		threeAddCodes.add(regGeteax(resReg));
	}
	
	public String getCmpSymbol(String symbol){
		if(symbol.equals("LARGER"))
			return "jle";
		else if(symbol.equals("BE"))
			return "jl";
		else if(symbol.equals("LESS"))
			return "jle";
		else if(symbol.equals("LE"))
			return "jl";
		else if(symbol.equals("EQUAL"))
			return "jne";
		return "";
	}
	
}

