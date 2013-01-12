package cn.edu.nju.software.file;

import java.util.ArrayList;
import java.util.List;
import cn.edu.nju.software.model.Document;
import cn.edu.nju.software.ui.R;
import edu.emory.mathcs.backport.java.util.Collections;

public class FileOperator {
	public static ArrayList<Document> fileList;

	public static ArrayList<Document> fileList(
			List<Document> documents, int parentId) {
		fileList = new ArrayList<Document>();
		List<Document> dir = new ArrayList<Document>();
		List<Document> file = new ArrayList<Document>();
		for (int i = 0; i < documents.size(); i++) {
			if (documents.get(i).getParentId() == parentId) {
				if (documents.get(i).getType().value() == 1)
					file.add(documents.get(i));
				else
					dir.add(documents.get(i));
			}
		}
		Collections.sort(dir);
		for (Document fdir : dir)
			fileList.add(fdir);
		Collections.sort(file);
		for (Document dfile : file)
			fileList.add(dfile);

		// int i = 0;
		// int total = fileList.size();

		List<Integer> fileIcon = new ArrayList<Integer>();
		for (int i = 0; i < fileList.size(); i++) {
			String end = fileList.get(i).getTitle()
					.substring(fileList.get(i).getTitle().lastIndexOf(".") + 1)
					.toLowerCase();
			if (fileList.get(i).getType().value() == 2)
				fileIcon.add(R.drawable.directory);
			else if (end.equals("txt") || end.equals("java")
					|| end.equals("xml") || end.equals("html")
					|| end.equals("css") || end.equals("properties")) {
				fileIcon.add(R.drawable.text);
			} else if (end.equals("doc") || end.equals("docx")) {
				fileIcon.add(R.drawable.docx_win);
			} else if (end.equals("pdf")) {
				fileIcon.add(R.drawable.pdf);
			} else if (end.equals("mp3")) {
				fileIcon.add(R.drawable.mp3);
			} else if (end.equals("jpeg")) {
				fileIcon.add(R.drawable.jpeg);
			} else if (end.equals("png")) {
				fileIcon.add(R.drawable.png);
			} else if (end.equals("bmp")) {
				fileIcon.add(R.drawable.bmp);
			} else if (end.equals("mp3")) {
				fileIcon.add(R.drawable.mp3);
			} else if (end.equals("xls") || end.equals("xlsx")) {
				fileIcon.add(R.drawable.xlsx_win);
			} else if (end.equals("ppt") || end.equals("pptx")) {
				fileIcon.add(R.drawable.pptx_win);
			} else {
				fileIcon.add(R.drawable.unknow);
			}
		}

		
		for (int i = 0; i < fileList.size(); i++) {
			fileList.get(i).setResource(fileIcon.get(i));
		}
		return fileList;

	}

}
