package demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class HttpClentTest {
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		System.out.println("获取表单元素");
		
		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet get = new HttpGet("http://www.zhaopin.com");
		CloseableHttpResponse response = client.execute(get);   
		HttpEntity entity = response.getEntity(); 
		String content = EntityUtils.toString(entity); 
		EntityUtils.consume(entity);  
		Document doc = Jsoup.parse(content); 
		
		Element from = doc.getElementById("loginForm");
		Elements e =from.getAllElements();
		Iterator<Element> i = e.iterator();
		while(i.hasNext()){
			Element ele =i.next();
			System.out.println(ele.val());
		}
		
		System.out.println("开始登录");
		
		HttpPost post = new HttpPost("https://passport.zhaopin.com/account/login");  
		
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("Password", "12345678"));
		nvps.add(new BasicNameValuePair("RememberMe", "true"));
		nvps.add(new BasicNameValuePair("errUrl", "https://passport.zhaopin.com/account/login"));
		nvps.add(new BasicNameValuePair("int_count", "999"));
		nvps.add(new BasicNameValuePair("loginname", "15600813381"));
		nvps.add(new BasicNameValuePair("requestFrom", "portal"));
		
		post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8")); 
		
		content = EntityUtils.toString(entity);
		
		System.out.println();
		
		
	}
	
	public void login1() throws ClientProtocolException, IOException{
		
		//构建一个Client;4.x 使用。3.x 使用 defaultHttpClent
		CloseableHttpClient client = HttpClients.createDefault();
		
		HttpPost post = new HttpPost("http://passport.zhaopin.com/account/login");
		
		List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		nvps.add(new BasicNameValuePair("LoginName", "ff8663521@163.com"));
		nvps.add(new BasicNameValuePair("Password", "breakout0536"));
		nvps.add(new BasicNameValuePair("RememberMe", "true"));
		nvps.add(new BasicNameValuePair("RememberMe", "flase"));
		nvps.add(new BasicNameValuePair("bkurl", ""));
		
		post.setEntity(new UrlEncodedFormEntity(nvps));
		
		CloseableHttpResponse response = client.execute(post);
		
		 try {	
			 	
			 	Header[] header =  response.getAllHeaders();
			 	for (int i = 0; i < header.length; i++) {
					System.out.print(header[i].getName()+":");
					System.out.println(header[i].getValue());
				}
			 	
		        System.out.println(response.getStatusLine());
			 	
			 	//关流
		        HttpEntity entity = response.getEntity();
				EntityUtils.consume(entity);
		    } finally {
				response.close();
		    }
	}
	
	
	
	
}
