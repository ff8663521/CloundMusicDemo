package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.alibaba.fastjson.JSON;

import bean.Album;
import bean.Artist;
import bean.Comment;
import bean.Playlist;
import bean.Song;
import dao.IAlbumDao;
import dao.IArtistDao;
import dao.ICommentDao;
import dao.ISongDao;
import dao.impl.AlbumDao;
import dao.impl.ArtistDao;
import dao.impl.CommentDao;
import dao.impl.SongDao;
import dao.utils.UA;
import fromatbean.CommentsJson;

/**
 * 歌曲详情页处理action
 * @author wxf
 *
 */
public class DetailsSong {
	// 创建模拟客户端
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = new HttpClientContext();

	private static ISongDao songDao;
	private static IAlbumDao albumDao;
	private static IArtistDao artistDao;
	private static ICommentDao commentDao;

	static {
		songDao = new SongDao();
		albumDao = new AlbumDao();
		artistDao = new ArtistDao();
		commentDao = new CommentDao();
	}

	public static void main(String[] args) throws InterruptedException {
		// 计算分页
		int allCount = songDao.Count();
		int rows = 100;
		int page = 0;
		if (allCount % rows == 0) {
			page = allCount / rows;
		} else {
			page = allCount / rows + 1;
		}
		
		System.out.println("总共："+page +"页");
		for (int i = 1; i <=page; i++) {
			List<Song> list =songDao.getAllSongByPage(i, rows);
			
			int countNum = 0;
			for (Song song : list) {
				countNum++;
				getSongDetails(song);
				
				//每10次休息1秒，保证非暴力爬虫被封IP
				if(countNum%20 == 0){
					TimeUnit.SECONDS.sleep(1);
				}
			}
			
			System.out.println("==========================第 "+ i +"页，已经完成" );
		}
		
//		Song song = new Song();
//		
//		song.setId(429450375);
//		song.setName("来日方长");
//		song.setLink("http://music.163.com/song?id=429450375");
//		song.setNum(44610);
//		
//		
//		getSongDetails(song);
	}

	public static void getSongDetails(Song song) {
		HttpGet get = new HttpGet(song.getLink());
		get.setHeader("User-Agent",
				UA.getUA());
		get.setHeader("Referer", "http://music.163.com/");
		get.addHeader("Connection", "keep-alive");

		// 接收响应
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get, context);
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
		
		//System.out.println(response.getStatusLine().getStatusCode());
		
		// 元素转换为dom 树
		Document doc = Jsoup.parse(content);

		// 获取歌手，专辑信息
		List<Element> list_s = doc.select("p.des a");
		if(list_s.size() == 0){
			System.out.println("**********************这首歌未能抓取************************");
			System.out.println(song);
			System.out.println("**********************这首歌未能抓取************************");
			return;
		}
		// 配置歌手信息
		Element element_artist = list_s.get(0);
		// id
		String id_s;
		try {
			id_s = element_artist.attr("href").substring(11);
		} catch (Exception ex) {
			id_s = "0";
		}
		Integer id = Integer.parseInt(id_s);
		Artist artist = new Artist();
		artist.setId(id);
		artist.setName(element_artist.text());

		// 配置专辑信息
		// 避免多歌手导致抓取错误，抓取list最后一组数据
		Element element_album = list_s.get(list_s.size() - 1);
		// id
		String id_s1;
		try {
			id_s1 = element_album.attr("href").substring(10);
		} catch (Exception ex) {
			id_s1 = "0";
		}
		Integer id_album = Integer.parseInt(id_s1);
		Album album = new Album();
		album.setId(id_album);
		album.setName(element_album.text());
		album.setArtist(artist);

		// 避免合唱导致统计误差，直接抓取歌手名称，数据库中专门字段存取
		List<Element> list_other = doc.select("p.des span");
		String singerNames = list_other.get(0).attr("title");
		song.setSingerName(singerNames);
		song.setAlbum(album);
		song.setArtist(artist);

		/*
		 * 网易云评论采用post提交，ajax加载，并有加密参数，通过网页抓取，保存如properties，重复使用
		 * http://music.163.com/weapi/v1/resource/comments/R_SO_4_+ 429450375+
		 * ?csrf_token=acdaa8f6cc56df6199017275d1797bf9
		 */
		ResourceBundle resource = ResourceBundle.getBundle("config/taken");
		String params = resource.getString("params");
		String encSecKey = resource.getString("encSecKey");
		String csrf_token = resource.getString("csrf_token");

		// 发送ajax请求
		String ajaxurl_base = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_";
		StringBuilder sbd = new StringBuilder(ajaxurl_base);
		sbd.append(song.getId()).append("?csrf_token=").append(csrf_token);
		String ajaxurl = sbd.toString();
		// ajax post 请求
		HttpPost post = new HttpPost(ajaxurl);
		// 稍微遮掩一下
		post.setHeader("User-Agent",
				UA.getUA());
		post.setHeader("Referer", song.getLink());
		post.setHeader("Connection", "keep-alive");
		// 封装参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("params", params));
		nvps.add(new BasicNameValuePair("encSecKey", encSecKey));
		// 发送请求
		CloseableHttpResponse response1 = null;
		String content1 = null;
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
			response1 = client.execute(post, context);
			HttpEntity entity1 = response1.getEntity();
			content1 = EntityUtils.toString(entity1);
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// 对Json进行format，获取总评论信息
		CommentsJson coms = JSON.parseObject(content1, CommentsJson.class);
		song.setNum(coms.getTotal());

		// 获取热门评论
		List<Comment> list_comment = JSON.parseArray(coms.getHotComments(), Comment.class);

		saveAll(artist, album, song, list_comment);
	}

	public static void saveAll(Artist artist, Album album, Song song, List<Comment> list) {

		artistDao.save(artist);
		albumDao.save(album);
		songDao.save(song);
		for (Comment comment : list) {
			comment.setSong(song);
		}
		/**
		 * 评论涉及符号，表情，旧版本的mysql utf8 为3字节，会报错 需更改 content字段的字符集为 uft8mb4 此字符集为
		 * 4字节
		 */
		commentDao.batchSave(list);

	}
}
