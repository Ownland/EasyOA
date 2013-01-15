package cn.edu.nju.software.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Log;

public class FileUtils {  
	
	public static void saveFile(ByteArrayInputStream file, String address) {
		FileOutputStream fos = null;
		try {

			File myFile = new File(address);
			// 如果是目录，就创建
			if (myFile.isDirectory()) {
				myFile.mkdirs();
			} else {
				// 如果文件所在目录不存在，就创建
				File pf = myFile.getParentFile();
				if (!pf.exists())
					pf.mkdirs();
			}

			fos = new FileOutputStream(address);
			byte[] buffer = new byte[8192];
			int count = 0;
			while ((count = file.read(buffer)) >= 0) {
				fos.write(buffer, 0, count);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.v("tag", e.toString());
		} finally {
			try {
				if (fos != null) {
					fos.flush();
					fos.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}  