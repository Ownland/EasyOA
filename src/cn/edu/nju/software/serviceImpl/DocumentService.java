package cn.edu.nju.software.serviceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpsTransportSE;

import android.util.Log;

import cn.edu.nju.software.service.IDocumentService;
import cn.edu.nju.software.serviceConfig.ClientServiceResource;

public class DocumentService implements IDocumentService{

	private String methodName;
	private SoapObject request;
	private SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	private HttpsTransportSE ht=new HttpsTransportSE(ClientServiceResource.ip,
			ClientServiceResource.port,ClientServiceResource.bootspace+
			ClientServiceResource.documentService,ClientServiceResource.outoftime);


	@Override
	public ByteArrayInputStream getDocumentContent(String address) {
		// TODO Auto-generated method stub
		ByteArrayInputStream baos = null;
		this.methodName = "getDocumentContent";
		this.request = new SoapObject(ClientServiceResource.namespace, this.methodName);
		request.addProperty("arg0", address);
		this.envelope.bodyOut = request;
		try{
			ht.call(null, this.envelope);
			if (envelope.getResponse() != null){
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				baos = DecodeHelper.getByteStream(soapObject, "getDocumentContentResult=");
			}
		}catch(Exception e){
			
		}
		return baos;
	}

}
