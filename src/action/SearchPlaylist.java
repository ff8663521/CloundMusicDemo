package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import bean.Playlist;
import dao.IPlaylistDao;
import dao.impl.PlaylistDao;
import dao.utils.StringUtils;

public class SearchPlaylist {
	// ����ģ��ͻ���
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = new HttpClientContext();  
	
	private static IPlaylistDao playlistDao;
	
	static {
		playlistDao = new PlaylistDao();;
	}
	
	public static void main(String[] args) {
		// Ŀ���ַ
		//��ȡȫ���赥
		String listURL = "http://music.163.com/discover/playlist/?cat=%E6%AC%A7%E7%BE%8E&order=hot";

		HttpGet get = new HttpGet(listURL);
		// ������Ӧ
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get,context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ��Ӧ����
		HttpEntity entity = response.getEntity();
		String content = null;
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		// �رջ�ȡcontent ��
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Ԫ��ת��Ϊdom ��
		Document doc = Jsoup.parse(content);
		//��ȡ��ҳ��
		List<Element> list_page = doc.select("a.zpgi");
		String page_s = list_page.get(list_page.size() - 1).text();
		int page = Integer.parseInt(page_s);
		//��ҳurl
		String url ="http://music.163.com/discover/playlist/?order=hot&cat=%E6%AC%A7%E7%BE%8E&limit=35&offset=";
		
		System.out.println("�ܹ���"+page +"ҳ");
		for (int i = 0; i < page; i++) {
			String item_url = url+(i*35);
			System.out.println(item_url);
			getPlaylist(item_url);
			System.out.println("�� "+ i +"ҳ���Ѿ����" );
			
		}
	}

	public static void getPlaylist(String url) {
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		get.setHeader("Referer", "http://music.163.com/");
		get.addHeader("Connection","keep-alive");

		// ������Ӧ
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get,context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// ��Ӧ����
		HttpEntity entity = response.getEntity();
		String content = null;
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		// �رջ�ȡcontent ��
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Ԫ��ת��Ϊdom ��
		Document doc = Jsoup.parse(content);
		// ��ȡ�赥
		List<Element> list_a = doc.select("a.msk");
		// ��ȡ�赥���Ŵ���
		List<Element> list_b = doc.select("span.nb");
		List<Playlist> playlist = new ArrayList<Playlist>();

		 for (int i = 0; i < list_a.size(); i++) {
			 Playlist play = new Playlist();
			 Element e1 = list_a.get(i);
			 Element e2 = list_b.get(i);
			
			 String link =StringUtils.replaceNUll(e1.attr("href"), "");
			 String id_s;
			 try {
				  id_s = link.substring(13);
			} catch (Exception e) {
				id_s ="0";
			}
			 Integer id = Integer.parseInt(id_s);
			 String title =StringUtils.replaceNUll(e1.attr("title"), "����") ;
			
			 play.setId(id);
			 play.setName(title);
			 play.setLink("http://music.163.com"+link);
			
			 String num_s = e2.text();
			 Integer num = 0;
			 try {
				 num = Integer.parseInt(num_s.replaceAll("��","0000"));
			} catch (Exception e) {
				
			}
			 play.setNum(num);
			
			 playlist.add(play);
		
		 }
		 //����赥�����ݿ�
		 playlistDao.batchSave(playlist);
		 
	}

}
