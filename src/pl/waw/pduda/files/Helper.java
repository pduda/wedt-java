package pl.waw.pduda.files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public class Helper 
{
	public static void saveObject(String filePath,Serializable obj)
    {
		FileOutputStream fos = null;
		ObjectOutputStream out = null;
		try 
		{
			fos = new FileOutputStream(filePath);
			out = new ObjectOutputStream(fos);
			out.writeObject(obj);
			out.close();
		} 
		catch (IOException ex) 
		{
			ex.printStackTrace();
		}
    }
    public static Serializable readObject(String filePath)
    {
    	Object obj=null;
		FileInputStream fis = null;
		ObjectInputStream in = null;
		try {
			fis = new FileInputStream(filePath);
			in = new ObjectInputStream(fis);
			obj = in.readObject();
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		
		return (Serializable) obj;
    }
}
