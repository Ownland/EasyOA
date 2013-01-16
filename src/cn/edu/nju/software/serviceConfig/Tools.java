package cn.edu.nju.software.serviceConfig;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.kobjects.base64.Base64;


public class Tools {
	
	public static Object convert_string_to_object(String s){
		byte[] buffer = null;
		try {
			buffer = Base64.decode(s);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(baos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Object o = null;
		try {
			o = in.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	public static String convert_object_to_string(Object o){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(baos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			out.writeObject(o);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String ret_string=new String(Base64.encode(baos.toByteArray()));
		
		return ret_string;
	}
}
