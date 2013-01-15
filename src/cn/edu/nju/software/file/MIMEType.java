package cn.edu.nju.software.file;

import java.io.File;
import java.util.HashMap;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class MIMEType {
	// �����ļ�����׺��MIMEType�Ķ������
	static HashMap<String, String> map = new HashMap<String, String>(64, 1.0f);

	public static final HashMap<String, String> MimeTypeTab() {
		map.put(".3gp", "video/3gpp");
		map.put(".apk", "application/vnd.android.package-archive");
		map.put(".asf", "video/x-ms-asf");
		map.put(".avi", "video/x-msvideo");
		map.put(".bin", "application/octet-stream");
		map.put(".bmp", "image/bmp");
		map.put(".c", "text/plain");
		map.put(".class", "application/octet-stream");
		map.put(".conf", "text/plain");
		map.put(".cpp", "text/plain");
		map.put(".doc", "application/msword");
		map.put(".docx", "application/msword");
		map.put(".exe", "application/octet-stream");
		map.put(".gif", "image/gif");
		map.put(".gtar", "application/x-gtar");
		map.put(".gz", "application/x-gzip");
		map.put(".h", "text/plain");
		map.put(".htm", "text/html");
		map.put(".html", "text/html");
		map.put(".jar", "application/java-archive");
		map.put(".java", "text/plain");
		map.put(".jpeg", "image/jpeg");
		map.put(".jpg", "image/jpeg");
		map.put(".js", "application/x-javascript");
		map.put(".log", "text/plain");
		map.put(".m3u", "audio/x-mpegurl");
		map.put(".m4a", "audio/mp4a-latm");
		map.put(".m4b", "audio/mp4a-latm");
		map.put(".m4p", "audio/mp4a-latm");
		map.put(".m4u", "video/vnd.mpegurl");
		map.put(".m4v", "video/x-m4v");
		map.put(".mov", "video/quicktime");
		map.put(".mp2", "audio/x-mpeg");
		map.put(".mp3", "audio/x-mpeg");
		map.put(".mp4", "video/mp4");
		map.put(".mpc", "application/vnd.mpohun.certificate");
		map.put(".mpe", "video/mpeg");
		map.put(".mpeg", "video/mpeg");
		map.put(".mpg", "video/mpeg");
		map.put(".mpg4", "video/mp4");
		map.put(".mpga", "audio/mpeg");
		map.put(".msg", "application/vnd.ms-outlook");
		map.put(".ogg", "audio/ogg");
		map.put(".pdf", "application/pdf");
		map.put(".png", "image/png");
		map.put(".pps", "application/vnd.ms-powerpoint");
		map.put(".ppt", "application/vnd.ms-powerpoint");
		map.put(".pptx", "application/vnd.ms-powerpoint");
		map.put(".prop", "text/plain");
		map.put(".rar", "application/x-rar-compressed");
		map.put(".rc", "text/plain");
		map.put(".rmvb", "audio/x-pn-realaudio");
		map.put(".rtf", "application/rtf");
		map.put(".sh", "text/plain");
		map.put(".tar", "application/x-tar");
		map.put(".tgz", "application/x-compressed");
		map.put(".txt", "text/plain");
		map.put(".wav", "audio/x-wav");
		map.put(".wma", "audio/x-ms-wma");
		map.put(".wmv", "audio/x-ms-wmv");
		map.put(".wps", "application/vnd.ms-works");
		map.put(".xml", "text/plain");
		map.put(".xls", "application/vnd.ms-excel");
		map.put(".xlsx", "application/vnd.ms-excel");
		map.put(".z", "application/x-compress");
		map.put(".zip", "application/zip");
		map.put("", "*/*");
		return map;
	}

	/**
	 * �����ļ���׺����ö�Ӧ��MIME���͡�
	 * 
	 * @param file
	 */
	public static String getMIMEType(String file) {
		String type = "*/*";
		String fName = file;
		// ��ȡ��׺��ǰ�ķָ���"."��fName�е�λ�á�
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0) {
			return type;
		}
		/* ��ȡ�ļ��ĺ�׺�� */
		String end = fName.substring(dotIndex, fName.length()).toLowerCase();
		if (end.equals(""))
			return type;
		// ��MIME���ļ����͵�ƥ������ҵ���Ӧ��MIME���͡�
		for (int i = 0; i < map.size(); i++) {
			if (map.containsKey(end))
				type = map.get(end);
		}
		return type;
	}

	/**
	 * ����MIME����������Ӧ��������ļ�
	 * 
	 * @param file
	 */
	public static void openFile(String file, String dir, Activity activity) {
		// ��ʼ��MIME���Ͷ����б�
		MIMEType.MimeTypeTab();
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// ����intent��Action����
		intent.setAction(Intent.ACTION_VIEW);
		// ��ȡ�ļ�file��MIME����
		String type = MIMEType.getMIMEType(file);
		// ����intent��data��Type���ԡ�
		file = dir + "/" + file;
		File f = new File(file);
		intent.setDataAndType(Uri.fromFile(f), type);
		// ��ת
		activity.startActivity(intent);
	}
}
