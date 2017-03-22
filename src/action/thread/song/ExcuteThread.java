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
				//ÿץȡ20�׸裬����һ��
				if(count % 20 ==0){
					barrier.await();
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("�쳣�жϣ�" );
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
			System.out.println("�쳣�жϣ�" );
		}
	}
	
	public void doWork() throws InterruptedException {
		//�������߳�ÿץȡ10�׸���Ϣ1��
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
