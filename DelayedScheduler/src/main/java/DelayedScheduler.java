import java.util.Comparator;
import java.util.PriorityQueue;

public class DelayedScheduler extends Thread {
    private PriorityQueue<DelayedThread> priorityQueue;
    private Boolean terminated;

    public DelayedScheduler() {
        priorityQueue = new PriorityQueue<>(Comparator.comparingLong(w -> w.delayTime));
        terminated = false;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (priorityQueue) {
                if (priorityQueue.isEmpty() && terminated) return;
                if (priorityQueue.isEmpty()) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                    }
                }
                if (!priorityQueue.isEmpty()) {
                    priorityQueue.poll().run();
                }
            }
        }
    }

    public void delayedRun(Runnable runnable, Long delayTime) {
        synchronized (priorityQueue) {
            priorityQueue.add(new DelayedThread(runnable, delayTime));
            this.notifyAll();
        }
    }

    public void terminate() {
        synchronized (terminated) {
            this.terminated = true;
        }
    }

    private class DelayedThread extends Thread {
        Thread thread;
        Long delayTime;
        Long creationTime;

        DelayedThread(Runnable runnable, Long delayTime) {
            this.thread = new Thread(runnable);
            this.delayTime = delayTime;
            this.creationTime = System.currentTimeMillis();
        }

        @Override
        public void run() {
            Long realDelayTime = delayTime - (System.currentTimeMillis() - creationTime);
            if (realDelayTime > 0)
                try {
                    this.sleep(realDelayTime);
                } catch (InterruptedException e) {
                }
            thread.start();
        }
    }
}
