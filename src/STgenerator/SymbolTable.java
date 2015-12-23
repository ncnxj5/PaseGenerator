package STgenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
	
	public class vsItem{
		public String name = "";
		public String type = "";
		public Integer loc = new Integer(0);
		public Integer isArray = new Integer(0);
		public Integer isFormal = new Integer(0);
		public vsItem(){}
	}
	
	public class fsItem{
		public String name = "";
		public String type = "";
		public ArrayList<String> parasName = new ArrayList();
		public ArrayList<String> parasType = new ArrayList();
		public ArrayList<Integer> parasIsarray = new ArrayList();
		public Integer body = new Integer(-1);
		
		public void addPara(String name, String type, Integer isArray){
			parasName.add(name);
			parasType.add(type);
			parasIsarray.add(isArray);
		}
		
		public Integer getNumpara(){
			return parasName.size();
		}
		
		public ArrayList getPara(Integer num){
			
			ArrayList result = new ArrayList();
			
			result.add(parasName.get(num));
			result.add(parasType.get(num));
			result.add(parasIsarray.get(num));
			
			return result;
		}
	}
	
	public void AddEvar(String name){
		vars.put(name, new vsItem());
		vars.get(name).name = name;
	}
	
	public void AddEfunc(String name){
		funcs.put(name, new fsItem());
		funcs.get(name).name = name;
	}
	
	public Integer base;
	public Map<String, vsItem> vars = new HashMap<String, vsItem>();
	public Map<String, fsItem> funcs = new HashMap<String, fsItem>();
	public ArrayList children = new ArrayList();
	public Integer body;
	public SymbolTable parent;
	
	public SymbolTable(Integer b, Integer body_num){
		base = b;
		body = body_num;
	}
	
	public void print(){
		if(parent != null){
			System.out.println("body="+body.toString()+"; parent="+parent.body+"; base="+base.toString());
		}else{
			System.out.println("body="+body.toString()+"; base="+base.toString());
		}
		
		System.out.println("vars:");
		for(String key:vars.keySet()){
			vsItem tmp = vars.get(key);
			System.out.println(tmp.name+" "+tmp.type+" "+tmp.isArray+" "+tmp.isFormal);
		}
		System.out.println("funcs:");
		for(String key: funcs.keySet()){
			fsItem tmp = funcs.get(key);
			System.out.print(tmp.name+" "+tmp.type+" "+tmp.body);
			for(int i=0; i<tmp.getNumpara(); ++i){
				ArrayList para = tmp.getPara(i);
				System.out.print("("+(String)para.get(0)+" "+(String)para.get(1)+" "+(Integer)para.get(2)+")");
			}
			System.out.println();
		}
		for(Integer i=0; i<children.size(); ++i){
			System.out.println("child:"+((SymbolTable)(children.get(i))).body.toString());
		}
		System.out.println("");
		for(int i=0; i<children.size(); ++i){
			((SymbolTable)(children.get(i))).print();
		}
	}
	
}
