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
	// ����ģ��ͻ���
	private static CloseableHttpClient client = HttpClients.createDefault();
	private static HttpClientContext context = new HttpClientContext();

	private static IPlaylistDao playlistDao;
	
	private static ISongDao songDao;
	
	static {
		playlistDao = new PlaylistDao();
		songDao = new SongDao();
	}

	public static void main(String[] args) throws InterruptedException {
		
		// �����ҳ
		int allCount =playlistDao.Count();
		int rows = 50 ;
		int page = 0;
		if(allCount % rows == 0){
			page = allCount/rows;
		}else{
			page = allCount/rows+1;
		}
		
		System.out.println("�ܹ���"+page +"ҳ");
		for (int i = 1; i <= page; i++) {
			List<Playlist> list =playlistDao.getAllPlaylistByPage(i, rows);
			//��������
			int countNum = 0;
			for (Playlist pl : list) {
				countNum++;
				
				getSongsByPlaylist(pl);
				
				//ÿ10����Ϣ���룬��֤�Ǳ������汻��IP
				if(countNum%10 == 0){
					TimeUnit.SECONDS.sleep(3);
				}
			}
			
			System.out.println("�� "+ i +"ҳ���Ѿ����" );
		}
		
	}
	
	/**
	 * �赥�и�����ȡ���洢
	 * @param pl
	 */
	public static void getSongsByPlaylist(Playlist pl){
		HttpGet get = new HttpGet(pl.getLink());
		
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
		
		//����赥ʣ����Ϣ
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
		//���µ����ݿ�
		playlistDao.update(pl);
		
		//�����б�
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
			 String name =StringUtils.replaceNUll(e.text(), "����") ;
			
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
