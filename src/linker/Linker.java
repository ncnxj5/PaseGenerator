package linker;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
/*==================*
 *| mblue 2015/12/18|
 *==================*/
public class Linker {
	//the head&tail part
	/* !!![warning] that the include path should change to fit the computer environment *
	 * !!![warning] that the include path should change to fit the computer environment *
	 * !!![warning] that the include path should change to fit the computer environment */
	static String head=new String(".386 \r\n.model flat, stdcall\r\n\r\ninclude  e:\\masm32\\include\\msvcrt.inc\r\nincludelib e:\\masm32\\lib\\msvcrt.lib\r\n");
	static String code=new String("");
	static String tail=new String("\r\nend main\r\n");
	static public String console=new String("");
	/*============================================================================*
	 * Step1:
	 * 		Setup MASM
	 * ---------------------------------------------------------------------------*
	 * Step2:
	 * 		Add the Environmental Variable path "\masm32\bin" e.g."E:\masm32\bin"
	 * 
	 *============================================================================*/
	private static void writefile(String datas,String codes,String filename)
	{
		BufferedWriter writer=null;
		BufferedReader reader=null;
		try {
			//attempt to out write the .asm out
			   writer=new BufferedWriter(new  FileWriter(filename+".asm"));
			} 
		catch (IOException e1) {
			  e1.printStackTrace();
			  System.out.println("[Linker]:>Oooops! File Opening Broken =w=");
			 }
		 try {
			  reader=new BufferedReader(new  FileReader("syshead.lib"));
			  head="";
			  while((head=reader.readLine())!=null)
				  writer.append(head+"\r\n");
			  //write the header
			  
			  writer.append(datas);
			  //write the user data
			  reader=new BufferedReader(new  FileReader("syscode.lib"));
			  code="";
			  while((code=reader.readLine())!=null)
				  writer.append(code+"\r\n");
			  //write the contents;
			  writer.append(codes);
			  
			  //write the tail
			  reader=new BufferedReader(new  FileReader("systail.lib"));
			  tail="";
			  while((tail=reader.readLine())!=null)
				  writer.append(tail+"\r\n");
			  
			  writer.close();
			   System.out.println("[Linker]:>Write .ASM OUT Finished! :)");
			 } catch (IOException e) {
			  e.printStackTrace();
			  System.out.println("[Linker]:>Oooops! My Pencil Broken -w=");
			 }
	}
	private static String executeCommond(String cmd) {  
		        String ret = "";  
		        try {
		        	//get the CMD process;
		            Process p = Runtime.getRuntime().exec(cmd);  
		            InputStreamReader ins = new InputStreamReader(p.getInputStream());  
		            LineNumberReader input = new LineNumberReader(ins);  
		            String line;  
		            while ((line = input.readLine()) != null) {  
		                System.out.println(line);  
		                ret += line +"\r\n";  
		            }  
		        } catch (IOException e) {  
		            e.printStackTrace();  
		        }  
		        return ret;  
		    }  
	private static void objCreater(String filename)
	{
		try {
			//use the MASM /bin ml.exe to create OBJ file
			console+=executeCommond("ml /c /coff "+filename+".asm");
			console+="[Linker]:>Write .OBJ OUT Finished!";
		System.out.println("[Linker]:>Write .OBJ OUT Finished!");
		} 
		catch (Exception e) {  
            e.printStackTrace();
            System.out.println("[Linker]:>Oooops! Obj Broken -w=");
            console+="[Linker]:>Oooops! Obj BOOMooooooOOOOOOOOOOooooooOoooooooOO! -w=";
        }  
	}
	private static void objLinker(String filename)
	{
		try {
			//use the MASM /bin link.exe to create OBJ file
			// case that using /subsystem:console
			// but not /subsystem:windows! that will only can used by xxx.exe>out.txt
			console+=executeCommond("link /subsystem:console "+filename+".obj");
			console+="[Linker]:>Write .EXE OUT Finished! -O-";
		System.out.println("[Linker]:>Write .EXE OUT Finished! -O-");
		} 
		catch (Exception e) {  
            e.printStackTrace();
            System.out.println("[Linker]:>Oooops! EXE Broken -w=");
        }  
	}
	private static void funcCaller(String filename)
	{
		try {
			//use the MASM /bin link.exe to create OBJ file
			// case that using /subsystem:console
			// but not /subsystem:windows! that will only can used by xxx.exe>out.txt
			console+=executeCommond("cmd /c  start "+filename+".exe");
			console+="[Linker]:>Run "+filename+".exe";
		System.out.println("[Linker]:>Run "+filename+".exe");
		} 
		catch (Exception e) {  
            e.printStackTrace();
            System.out.println("[Linker]:>Cannot Run "+filename+".exe -w=");
            console+="[Linker]:>Cannot Run "+filename+".exe -w=";
        }  
	}
	public static void LinkEntrance(String data,String code,String name)
	{
		writefile(data,code,name);
		//write ASM file
		objCreater(name);
		//create OBJ file
		objLinker(name);
		//create EXE file
		funcCaller(name);
	}
	public static void LinkEntrancefile(String datafile,String codefile,String name)
	{
		try {
			String temp=new String("");
			String data=new String("");
			String code=new String("");
			
			BufferedReader reader=null;
			reader=new BufferedReader(new  FileReader(datafile));
			while((temp=reader.readLine())!=null)
				data+=temp+"\r\n";
			reader=new BufferedReader(new  FileReader(codefile));
			while((temp=reader.readLine())!=null)
				code+=temp+"\r\n";
		
			Linker.LinkEntrance(data,code,name);
			//System.out.println("END TEST");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void setHead(String header_i)
	{
		//change head if you want...
		head=header_i;
	}
	public static void setTail(String tail_i)
	{
		//change tail if you want... orz
		tail=tail_i;
	}
}
