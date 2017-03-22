package action.thread.playlist;

import java.util.concurrent.CountDownLatch;

//���ͨ���߳�
public class ResultThread implements Runnable {

	private final CountDownLatch latch;
	

	public ResultThread(CountDownLatch latch) {
		this.latch = latch;
	}

	@Override
	public void run() {
		try {
			// ������ǰ�̣߳�ֱ�� latch Ϊ 0
			latch.await();
			System.out.println("�赥ץȡ����");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.out.println("�쳣�жϣ�latch: " + latch.getCount());
		}
	}

}
