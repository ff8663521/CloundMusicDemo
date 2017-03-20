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
 * ��������ҳ����action
 * @author wxf
 *
 */
public class DetailsSong {
	// ����ģ��ͻ���
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
		// �����ҳ
		int allCount = songDao.Count();
		int rows = 100;
		int page = 0;
		if (allCount % rows == 0) {
			page = allCount / rows;
		} else {
			page = allCount / rows + 1;
		}
		
		System.out.println("�ܹ���"+page +"ҳ");
		for (int i = 1; i <=page; i++) {
			List<Song> list =songDao.getAllSongByPage(i, rows);
			
			int countNum = 0;
			for (Song song : list) {
				countNum++;
				getSongDetails(song);
				
				//ÿ10����Ϣ1�룬��֤�Ǳ������汻��IP
				if(countNum%20 == 0){
					TimeUnit.SECONDS.sleep(1);
				}
			}
			
			System.out.println("==========================�� "+ i +"ҳ���Ѿ����" );
		}
		
//		Song song = new Song();
//		
//		song.setId(429450375);
//		song.setName("���շ���");
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

		// ������Ӧ
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get, context);
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
		
		//System.out.println(response.getStatusLine().getStatusCode());
		
		// Ԫ��ת��Ϊdom ��
		Document doc = Jsoup.parse(content);

		// ��ȡ���֣�ר����Ϣ
		List<Element> list_s = doc.select("p.des a");
		if(list_s.size() == 0){
			System.out.println("**********************���׸�δ��ץȡ************************");
			System.out.println(song);
			System.out.println("**********************���׸�δ��ץȡ************************");
			return;
		}
		// ���ø�����Ϣ
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

		// ����ר����Ϣ
		// �������ֵ���ץȡ����ץȡlist���һ������
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

		// ����ϳ�����ͳ����ֱ��ץȡ�������ƣ����ݿ���ר���ֶδ�ȡ
		List<Element> list_other = doc.select("p.des span");
		String singerNames = list_other.get(0).attr("title");
		song.setSingerName(singerNames);
		song.setAlbum(album);
		song.setArtist(artist);

		/*
		 * ���������۲���post�ύ��ajax���أ����м��ܲ�����ͨ����ҳץȡ��������properties���ظ�ʹ��
		 * http://music.163.com/weapi/v1/resource/comments/R_SO_4_+ 429450375+
		 * ?csrf_token=acdaa8f6cc56df6199017275d1797bf9
		 */
		ResourceBundle resource = ResourceBundle.getBundle("config/taken");
		String params = resource.getString("params");
		String encSecKey = resource.getString("encSecKey");
		String csrf_token = resource.getString("csrf_token");

		// ����ajax����
		String ajaxurl_base = "http://music.163.com/weapi/v1/resource/comments/R_SO_4_";
		StringBuilder sbd = new StringBuilder(ajaxurl_base);
		sbd.append(song.getId()).append("?csrf_token=").append(csrf_token);
		String ajaxurl = sbd.toString();
		// ajax post ����
		HttpPost post = new HttpPost(ajaxurl);
		// ��΢����һ��
		post.setHeader("User-Agent",
				UA.getUA());
		post.setHeader("Referer", song.getLink());
		post.setHeader("Connection", "keep-alive");
		// ��װ����
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("params", params));
		nvps.add(new BasicNameValuePair("encSecKey", encSecKey));
		// ��������
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
		
		// ��Json����format����ȡ��������Ϣ
		CommentsJson coms = JSON.parseObject(content1, CommentsJson.class);
		song.setNum(coms.getTotal());

		// ��ȡ��������
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
		 * �����漰���ţ����飬�ɰ汾��mysql utf8 Ϊ3�ֽڣ��ᱨ�� ����� content�ֶε��ַ���Ϊ uft8mb4 ���ַ���Ϊ
		 * 4�ֽ�
		 */
		commentDao.batchSave(list);

	}
}
