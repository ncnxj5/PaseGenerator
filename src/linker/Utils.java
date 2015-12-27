package linker;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class Utils {
	public static InputStream getStringStream(String sInputString){
		if (sInputString != null && !sInputString.trim().equals("")){
			try{
				ByteArrayInputStream tInputStringStream = new ByteArrayInputStream(sInputString.getBytes());
				return tInputStringStream;
				}
			catch (Exception ex){
					ex.printStackTrace();
					}
			}
		return null;
	}
	public static int getLineCount(String s,char c){

		int index= 0;
		int i = 0;
		while((index=s.indexOf(c+"",index))!=-1){
			i++;
		}
		return i;
	}
}
