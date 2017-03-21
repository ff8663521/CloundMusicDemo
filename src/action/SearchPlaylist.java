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
import dao.utils.DuplicateRemovalUtils;
import dao.utils.StringUtils;

/**
 * 歌单列表页处理action
 * @author wxf
 *
 */
public class SearchPlaylist {
	// 创建模拟客户端
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = new HttpClientContext();  
	
	private static IPlaylistDao playlistDao;
	
	static {
		playlistDao = new PlaylistDao();;
	}
	
	public static void main(String[] args) {
		// 目标地址
		//获取全部歌单
		String listURL = "http://music.163.com/discover/playlist/?cat=%E6%B5%81%E8%A1%8C&order=hot";
		
		HttpGet get = new HttpGet(listURL);
		// 接收响应
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get,context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 响应解析
		HttpEntity entity = response.getEntity();
		String content = null;
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		// 关闭获取content 流
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 元素转换为dom 树
		Document doc = Jsoup.parse(content);
		//获取总页数
		List<Element> list_page = doc.select("a.zpgi");
		String page_s = list_page.get(list_page.size() - 1).text();
		int page = Integer.parseInt(page_s);
		//分页url
		String url ="http://music.163.com/discover/playlist/?order=hot&cat=%E6%B5%81%E8%A1%8C&limit=35&offset=";
		
		System.out.println("总共："+page +"页");
		for (int i = 0; i < page; i++) {
			String item_url = url+(i*35);
			System.out.println(item_url);
			getPlaylist(item_url);
			System.out.println("第 "+ i +"页，已经完成" );
			
		}
	}

	public static void getPlaylist(String url) {
		HttpGet get = new HttpGet(url);
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		get.setHeader("Referer", "http://music.163.com/");
		get.addHeader("Connection","keep-alive");

		// 接收响应
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get,context);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 响应解析
		HttpEntity entity = response.getEntity();
		String content = null;
		try {
			content = EntityUtils.toString(entity);
		} catch (ParseException | IOException e) {
			e.printStackTrace();
		}
		
		// 关闭获取content 流
		try {
			EntityUtils.consume(entity);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 元素转换为dom 树
		Document doc = Jsoup.parse(content);
		// 获取歌单
		List<Element> list_a = doc.select("a.msk");
		// 获取歌单播放次数
		List<Element> list_b = doc.select("span.nb");
		List<Playlist> playlist = new ArrayList<Playlist>();
		
		//整理抓取歌单id字符串，去重使用
		StringBuilder sbd = new StringBuilder();
		
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
			 //记录ID，去重使用
			 sbd.append(id_s);
			 sbd.append(",");
			 
			 Integer id = Integer.parseInt(id_s);
			 String title =StringUtils.replaceNUll(e1.attr("title"), "无名") ;
			
			 play.setId(id);
			 play.setName(title);
			 play.setLink("http://music.163.com"+link);
			
			 String num_s = e2.text();
			 Integer num = 0;
			 try {
				 num = Integer.parseInt(num_s.replaceAll("万","0000"));
			} catch (Exception e) {
				
			}
			 play.setNum(num);
			
			 playlist.add(play);
		
		 }
		 
		 //此次抓取的全部歌单ID
		 String id_connect=sbd.append(0).toString();
		 //获取数据库已存在的歌单
		 List<Playlist> hasPL = playlistDao.getPlaylistByIds(id_connect);
		 //去重
		 playlist = DuplicateRemovalUtils.remove(playlist, hasPL);
		 
		 //保存歌单至数据库
		 playlistDao.batchSave(playlist);
		 
	}

}
