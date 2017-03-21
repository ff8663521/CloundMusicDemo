package dao.utils;

import java.util.List;

public class DuplicateRemovalUtils {
	
	public static List remove(List listold,List listnew){
		
		boolean result = listold.removeAll(listnew);
		
		if(!result){
			System.out.println("»•÷ÿ ß∞‹");
		}
		
		return listold;
	}
}
