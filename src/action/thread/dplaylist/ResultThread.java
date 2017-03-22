package action.thread.dplaylist;

import java.util.concurrent.CountDownLatch;

//���ͨ���߳�
public class ResultThread implements Runnable {

	private final CountDownLatch latch;

	private final int page;
	
	public ResultThread(CountDownLatch latch ,int page) {
		this.latch = latch;
		this.page = page;
	}

	@Override
	public void run() {
		try {
			// ������ǰ�̣߳�ֱ�� latch Ϊ 0
			latch.await();
			System.out.println("��"+page+"ҳ������ץȡ����");
		} catch (InterruptedException ex) {
			ex.printStackTrace();
			System.out.println("�쳣�жϣ�latch: " + latch.getCount());
		}
	}

}
