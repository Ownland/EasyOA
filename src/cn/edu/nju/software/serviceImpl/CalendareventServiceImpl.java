package cn.edu.nju.software.serviceImpl;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.edu.nju.software.model.Calendarevent;
import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.service.ICalendareventService;
import cn.edu.nju.software.serviceConfig.ClientServiceResource;
import cn.edu.nju.software.serviceConfig.DecodeHelper;
import cn.edu.nju.software.serviceConfig.Tools;
import cn.edu.nju.software.serviceConfig._FakeX509TrustManager;

public class CalendareventServiceImpl implements ICalendareventService{
	
	private String methodName;
	private SoapObject request;

	private SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	private HttpsTransportSE ht=new HttpsTransportSE(ClientServiceResource.ip,
			ClientServiceResource.port,ClientServiceResource.bootspace+
			ClientServiceResource.calendareventService,ClientServiceResource.outoftime);
	
	@Override
	public List<Calendarevent> getCalendareventList(Map<String, String> version_map,
			int owner_id) {
		// TODO Auto-generated method stub
		_FakeX509TrustManager.allowAllSSL();
	
		String id_version_str = Tools.convert_object_to_string(version_map);
		
		this.methodName = "getCalendareventList";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		request.addProperty("arg0", id_version_str);
		request.addProperty("arg1", new Integer(owner_id).toString());
		this.envelope.bodyOut = request;
		List<Calendarevent> res = null;
		try {
			ht.call(null, this.envelope);
			SoapObject soapObject = (SoapObject) envelope.bodyIn;
			ObjectInputStream in = DecodeHelper.getObjectInputStream(soapObject, "etCalendareventListResult=");
			res = (List<Calendarevent>)in.readObject();
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
	public String updateCalendarevent(Calendarevent new_calendarevent) {
		// TODO Auto-generated method stub
		return null;
	}

}
