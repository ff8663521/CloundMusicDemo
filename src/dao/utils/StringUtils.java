package dao.utils;

import java.util.List;

import bean.Playlist;
import bean.Song;

public class StringUtils {
	
	/**
	 * 处理空字符串
	 * 如果不为空则返回原值，如果为空，则返回期望值
	 * @param oldstr
	 * @param newstr
	 * @return
	 */
	
	public static String replaceNUll(String oldstr,String newstr){
		
		oldstr = oldstr.trim();
		
		if("".equals(oldstr)|| null == oldstr){
			return newstr;
		}
		
		return oldstr;
	}
	
}
