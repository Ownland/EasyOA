package cn.edu.nju.software.serviceConfig;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.kobjects.base64.Base64;
import org.ksoap2.serialization.SoapObject;



public class DecodeHelper {

	public static ObjectInputStream getObjectInputStream(SoapObject soapObject, String outname){
		String[] output = soapObject.toString().split(outname);
		output = output[1].split(";");
		byte[] buffer = null;
		try {
			buffer = Base64.decode(output[0]);
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
		return in;
	}
	
	public static boolean getBooleanOut(SoapObject soapObject, String outname){
		 String[] account = soapObject.toString().split(outname);
		 account = account[1].split(";");
		 boolean bool = Boolean.parseBoolean(account[0]);
		 
		 return bool;
	}
	
	public static ByteArrayInputStream getByteStream(SoapObject soapObject, String outname){
		String[] output = soapObject.toString().split(outname);
		output = output[1].split(";");
		byte[] buffer = null;
		try {
			buffer = Base64.decode(output[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ByteArrayInputStream baos = new ByteArrayInputStream(buffer);
		return baos;
	}
	
}
