package cn.edu.nju.software.utils;

public class PathUtil {
	public String addPath(String src,int id){
		String des = src+"/" + id;
		return des;
	}
	
	public int getPathId(String src){
		String sub = src.substring(src.lastIndexOf("/")+1);
		int dex = Integer.parseInt(sub);
		return dex;
	}
	
	public String deletePath(String src){
		String des = src.substring(0, src.lastIndexOf("/"));
		return des;
	}
}
