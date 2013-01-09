package cn.edu.nju.software.serviceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.List;
import java.util.Map;


import org.ksoap2.SoapEnvelope;

import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.service.IContactService;
import cn.edu.nju.software.serviceConfig.ClientServiceResource;
import cn.edu.nju.software.serviceConfig._FakeX509TrustManager;



public class ContactServiceImpl implements IContactService{

	private String methodName;
	private SoapObject request;

	private SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	private HttpsTransportSE ht=new HttpsTransportSE(ClientServiceResource.ip,
			ClientServiceResource.port,ClientServiceResource.bootspace+
			ClientServiceResource.contactService,ClientServiceResource.outoftime);
	
	@Override
	public List<Contact> getContactList() {
		// TODO Auto-generated method stub
		_FakeX509TrustManager.allowAllSSL();
		this.methodName = "getContactList";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		this.envelope.bodyOut = request;
		List<Contact> res = null;
		
			try {
				ht.call(null, this.envelope);
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				ObjectInputStream in = DecodeHelper.getObjectInputStream(soapObject, "getContactListResult=");
				res = (List<Contact>)in.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return res;
	}

	@Override
	public Map<String, Object> changeInfo(Contact new_contact) {
		// TODO Auto-generated method stub
		_FakeX509TrustManager.allowAllSSL();
		this.methodName = "changeInfo";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		this.envelope.bodyOut = request;
		
		request.addProperty("arg0", new_contact.getId());
		request.addProperty("arg1", new_contact.getName());
		request.addProperty("arg2", new_contact.getNamePinyin());
		request.addProperty("arg3", new_contact.getPhone());
		request.addProperty("arg4", new_contact.getMobile());
		request.addProperty("arg5", new_contact.getEmail());
		request.addProperty("arg6", new_contact.getDepartment());
		request.addProperty("arg7", new_contact.getNickname());
		request.addProperty("arg8", new_contact.getAddress());
		request.addProperty("arg9", new_contact.getNote());
		
		Map<String, Object> res = null;
			try {
				ht.call(null, this.envelope);
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				ObjectInputStream in = DecodeHelper.getObjectInputStream(soapObject, "changeInfoResult=");
				res = (Map<String, Object>)in.readObject();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return res;
	}
	
}
