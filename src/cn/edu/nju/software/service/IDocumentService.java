package cn.edu.nju.software.service;

import java.io.ByteArrayInputStream;
import java.util.List;


public interface IDocumentService {
	
	public ByteArrayInputStream getDocumentContent(String address);
	
}
