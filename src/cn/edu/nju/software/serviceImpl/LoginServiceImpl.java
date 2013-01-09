package cn.edu.nju.software.serviceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.edu.nju.software.service.ILoginService;
import cn.edu.nju.software.serviceConfig.ClientServiceResource;
import cn.edu.nju.software.serviceConfig._FakeX509TrustManager;

public class LoginServiceImpl implements ILoginService{
	private String methodName;
	private SoapObject request;

	private SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	private HttpsTransportSE ht=new HttpsTransportSE(ClientServiceResource.ip,
			ClientServiceResource.port,ClientServiceResource.bootspace+
			ClientServiceResource.loginService,ClientServiceResource.outoftime);
	 
	@Override
	public Map<String, Object> login(String username, String password) {
		// TODO Auto-generated method stub
		_FakeX509TrustManager.allowAllSSL();
		this.methodName = "login";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		request.addProperty("arg0", username);
		request.addProperty("arg1", password);
		this.envelope.bodyOut = request;
		Map <String,Object> res = null;
			try {
				ht.call(null, this.envelope);
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				System.out.println("soap Object " + soapObject.toString());
				ObjectInputStream in = DecodeHelper.getObjectInputStream(soapObject, "loginResult=");
				res = (Map<String,Object>)in.readObject();
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
