package dao.utils;

import java.util.List;

import bean.Playlist;
import bean.Song;

public class StringUtils {
	
	/**
	 * ������ַ���
	 * �����Ϊ���򷵻�ԭֵ�����Ϊ�գ��򷵻�����ֵ
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
