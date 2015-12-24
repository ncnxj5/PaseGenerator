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
}
