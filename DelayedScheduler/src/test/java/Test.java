public class Test {
    @org.junit.Test
    public void test() {
        DelayedScheduler delayedScheduler = new DelayedScheduler();
        delayedScheduler.start();
        delayedScheduler.delayedRun(new MyRunnable("1"), (long) 5000);
        delayedScheduler.delayedRun(new MyRunnable("2"), (long) 10000);
        delayedScheduler.terminate();
    }

    class MyRunnable implements Runnable {
        String id;

        MyRunnable(String id) {
            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("Thread " + id + " is running");
        }
    }

}
