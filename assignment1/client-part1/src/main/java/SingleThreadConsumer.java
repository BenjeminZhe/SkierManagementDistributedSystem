import Model.Event;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>hw1client</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-06 21:04
 **/
public class SingleThreadConsumer {
    private static String IPAddress;
    public static Integer count = 1000;
    private static BlockingQueue<Event> buffer;
    private static AtomicInteger successCallCount;
    private static AtomicInteger failCallCount;

    public static void main(String[] args) throws InterruptedException {
        //Change this address every time you start the server on ec2
        IPAddress = "35.88.243.84:8080";
        buffer = new LinkedBlockingQueue<>();
        successCallCount = new AtomicInteger(0);
        failCallCount = new AtomicInteger(0);

        System.out.println("*********************************************************");
        System.out.println("Consumer starts...");
        System.out.println("*********************************************************");
        long start = System.currentTimeMillis();
        Producer producer = new Producer(0, count, buffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        CountDownLatch latch = new CountDownLatch(1);
        ConsumerRunnable cm = new ConsumerRunnable(count, true, IPAddress, buffer, successCallCount, failCallCount, latch);
        Thread thread = new Thread(cm);
        thread.start();
        latch.await();

        long end = System.currentTimeMillis();
        long wallTime = end - start;

        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests :" + successCallCount.get());
        System.out.println("Number of failed requests :" + failCallCount.get());
        System.out.println("Total wall time: " + wallTime);
        System.out.println( "Throughput: " + (int)((successCallCount.get() + failCallCount.get()) /
            (double)(wallTime / 1000) )+ " requests/second");
    }
}
