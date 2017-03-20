package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import bean.Song;
import dao.IPlaylistDao;
import dao.ISongDao;
import dao.impl.PlaylistDao;
import dao.impl.SongDao;
import dao.utils.StringUtils;

public class DetailsPlaylist {
	// 创建模拟客户端
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = new HttpClientContext();

	private static IPlaylistDao playlistDao;
	
	private static ISongDao songDao;
	
	static {
		playlistDao = new PlaylistDao();
		songDao = new SongDao();
	}

	public static void main(String[] args) throws InterruptedException {
		
		// 计算分页
		int allCount =playlistDao.Count();
		int rows = 50 ;
		int page = 0;
		if(allCount % rows == 0){
			page = allCount/rows;
		}else{
			page = allCount/rows+1;
		}
		
		System.out.println("总共："+page +"页");
		for (int i = 1; i <= page; i++) {
			List<Playlist> list =playlistDao.getAllPlaylistByPage(i, rows);
			//计数变量
			int countNum = 0;
			for (Playlist pl : list) {
				countNum++;
				
				getSongsByPlaylist(pl);
				
				//每10次休息三秒，保证非暴力爬虫被封IP
				if(countNum%10 == 0){
					TimeUnit.SECONDS.sleep(3);
				}
			}
			
			System.out.println("第 "+ i +"页，已经完成" );
		}
		
	}
	
	/**
	 * 歌单中歌曲获取并存储
	 * @param pl
	 */
	public static void getSongsByPlaylist(Playlist pl){
		HttpGet get = new HttpGet(pl.getLink());
		
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
		
		//补齐歌单剩余信息
		List<Element> list_ps = doc.select("a.u-tag");
		StringBuilder sbd = new StringBuilder("");
		for (Element element : list_ps) {
			sbd.append(element.text()).append(",");
		}
		String label = sbd.toString();
		
		List<Element> list_pc =doc.select("strong#play-count");
		String playCount = list_pc.get(0).text();
		Integer count = 0;
		 try {
			 count = Integer.parseInt(playCount);
		} catch (Exception e) {
		}
		 pl.setLabel(label);
		 pl.setNum(count);
		//更新到数据库
		playlistDao.update(pl);
		
		//歌曲列表
		List<Element> list_s = doc.select("ul.f-hide a");
		List<Song> songlist = new ArrayList<Song>();
		
		Song song = null;
		for (Element e : list_s) {
			 String link =StringUtils.replaceNUll(e.attr("href"), "");
			 String id_s;
			 try {
				  id_s = link.substring(9);
			 } catch (Exception ex) {
				id_s ="0";
			 }
			 Integer id = Integer.parseInt(id_s);
			 String name =StringUtils.replaceNUll(e.text(), "无名") ;
			
			 song = new Song();
			 song.setId(id);
			 song.setName(name);
			 song.setLink("http://music.163.com"+link);
			 songlist.add(song);
		}
		
		songDao.batchSave(songlist);
		System.out.println(pl.getNum() +"finish==============");
	}
	
	
}
