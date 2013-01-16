package cn.edu.nju.software.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import cn.edu.nju.software.model.Document;

public interface IDocumentService {
	public List<Document> getDocumentList();
	public ByteArrayInputStream getDocumentContent(int id);
}
