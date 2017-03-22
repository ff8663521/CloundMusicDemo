package action.thread.song;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import action.DetailsSong;
import bean.Song;

public class ExcuteThread implements Runnable {
	
	private LinkedBlockingQueue<Song> queue;
	
	private static CyclicBarrier barrier;
	
	private int count ;
	
	public ExcuteThread(LinkedBlockingQueue<Song> queue,CyclicBarrier barrier){
		this.queue = queue;
		this.barrier = barrier;
		count = 0;
	}

	@Override
	public void run() {
		try {
			while (!Thread.interrupted()) {
				doWork();
				//每抓取20首歌，报告一次
				if(count % 20 ==0){
					barrier.await();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("异常中断！" );
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			System.out.println("异常中断！" );
		}
	}
	
	public void doWork() throws InterruptedException {
		//计数；线程每抓取10首歌休息1秒
		count ++;
		
		if (count%10 == 0) {
			TimeUnit.SECONDS.sleep(1);
		}
		
		Song song =queue.poll();
		
		if(song == null ){
			return;
		}
		
		DetailsSong.getSongDetails(song);
		
	}
}
