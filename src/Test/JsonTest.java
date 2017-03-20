package Test;

import java.util.List;
import java.util.ResourceBundle;

import com.alibaba.fastjson.JSON;

import fromatbean.CommentJson;
import fromatbean.CommentsJson;
import fromatbean.UserJson;

public class JsonTest {
	static String data ;
	
	static{
		ResourceBundle resource = ResourceBundle.getBundle("Test/CommentsJson");
		data = resource.getString("data");
	}
	
	public static void CommentsTest(){
		CommentsJson coms =JSON.parseObject(data, CommentsJson.class);
		CommentTest(coms.getHotComments());
	}
	
	public static void CommentTest(String s){
		List<CommentJson> list = JSON.parseArray(s, CommentJson.class); 
		for (CommentJson commentJson : list) {
			userTest(commentJson.getUser());
		}
		
	}
	
	public static void userTest(String s){
		UserJson user = JSON.parseObject(s,UserJson.class);
		System.out.println(user);
	}
	
	public static void main(String[] args) {
		CommentsTest();
	}
}
