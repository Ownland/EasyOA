package cn.edu.nju.software.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import cn.edu.nju.software.model.Contact;
import cn.edu.nju.software.model.Document;
import cn.edu.nju.software.service.IDocumentService;
import cn.edu.nju.software.serviceConfig.ClientServiceResource;
import cn.edu.nju.software.serviceConfig.DecodeHelper;
import cn.edu.nju.software.serviceConfig._FakeX509TrustManager;

public class DocumentServiceImpl implements IDocumentService{
	
	private String methodName;
	private SoapObject request;
	
	private SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	private HttpsTransportSE ht=new HttpsTransportSE(ClientServiceResource.ip,
			ClientServiceResource.port,ClientServiceResource.bootspace+
			ClientServiceResource.documetService,ClientServiceResource.outoftime);
	
	@Override
	public List<Document> getDocumentList() {
		// TODO Auto-generated method stub
		_FakeX509TrustManager.allowAllSSL();
		this.methodName = "getDocumentList";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		this.envelope.bodyOut = request;
		List<Document> res = null;
		
			try {
				ht.call(null, this.envelope);
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				ObjectInputStream in = DecodeHelper.getObjectInputStream(soapObject, "getDocumentListResult=");
				res = (List<Document>)in.readObject();
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
	public ByteArrayInputStream getDocumentContent(int id) {
		_FakeX509TrustManager.allowAllSSL();
		this.methodName = "getDocumentContent";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		// TODO Auto-generated method stub
		this.envelope.bodyOut = request;
		request.addProperty("arg0", new Integer(id).toString());
		ByteArrayInputStream res = null;
		
			try {
				ht.call(null, this.envelope);
				if (envelope.getResponse() != null){
					SoapObject soapObject = (SoapObject) envelope.bodyIn;
					res = DecodeHelper.getByteStream(soapObject, "getDocumentContentResult=");
				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return res;
	}
	
}
