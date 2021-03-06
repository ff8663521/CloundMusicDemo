package action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

import action.thread.dplaylist.ResultThread;
import action.thread.dplaylist.ExcuteThread;
import bean.Playlist;
import bean.Song;
import dao.IPlaylistDao;
import dao.ISongDao;
import dao.impl.PlaylistDao;
import dao.impl.SongDao;
import dao.utils.DuplicateRemovalUtils;
import dao.utils.StringUtils;
import redis.RedisUtils;

/**
 * 歌单详情页处理action
 * @author wxf
 *
 */
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
		
		//创建多线程管理
		ExecutorService exec = Executors.newCachedThreadPool();
		
		for (int i = 1; i <= page; i++) {
			List<Playlist> list =playlistDao.getAllPlaylistByPage(i, rows);
			
			// 创建计数
			CountDownLatch latch = new CountDownLatch(list.size());
			// 加入通报类
			exec.execute(new ResultThread(latch,i));
			
			for (int j = 0; j < list.size(); j++){
				try {
					//多线程速度过快，每5条线程间隔3秒创建一次,避免hibernate session 支撑不住
					if(i%5 == 0){
						TimeUnit.SECONDS.sleep(3);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//执行类
				exec.execute(new ExcuteThread(latch,list));
			}
			
			//当前list 全部分配完线程,执行类计数归零。
			ExcuteThread.resetCount();
			
			//计数变量
//			int countNum = 0;
//			for (Playlist pl : list) {
//				countNum++;
//				
//				getSongsByPlaylist(pl);
//				
//				//每10次休息三秒，保证非暴力爬虫被封IP
//				if(countNum%10 == 0){
//					TimeUnit.SECONDS.sleep(3);
//				}
//			}
//			
//			System.out.println("第 "+ i +"页，已经完成" );
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
		
//		//整理抓取歌曲id字符串，去重使用
//		StringBuilder sbd4s = new StringBuilder();
		
		Song song = null;
		for (Element e : list_s) {
			 String link =StringUtils.replaceNUll(e.attr("href"), "");
			 String id_s;
			 try {
				  id_s = link.substring(9);
			 } catch (Exception ex) {
				id_s ="0";
			 }
//			//记录ID，去重使用
//			 sbd4s.append(id_s);
//			 sbd4s.append(",");
			 
			 Integer id = Integer.parseInt(id_s);
			 String name =StringUtils.replaceNUll(e.text(), "无名") ;
			
			 song = new Song();
			 song.setId(id);
			 song.setName(name);
			 song.setLink("http://music.163.com"+link);
			 songlist.add(song);
		}
		
//		// 此次抓取的全部歌曲ID
//		String id_connect = sbd4s.append(0).toString();
//		// 获取数据库已存在的歌曲
//		List<Song> hasS = songDao.getPlaylistByIds(id_connect);
//		// 去重
//		songlist = DuplicateRemovalUtils.remove(songlist, hasS);
//		System.out.println("去重歌曲："+hasS.size()+"首,"+"剩余歌曲："+songlist.size()+"首");
		
		/**
		 * 通过redis song 记录表来查询
		 * 进行去重
		 */
		List<Song> forSave = RedisUtils.dealHavenSong(songlist);
		
		songDao.batchSave(forSave);
		
		//添加到redis 数据库
		RedisUtils.saveSongs(forSave);
	}
	
	
}
