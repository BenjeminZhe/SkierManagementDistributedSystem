import Model.Event;
import Model.SyncList;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>hw1client</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-06 01:26
 **/
public class Consumer {
    private static String IPAddress;
    private static BlockingQueue<Event> buffer;
    private static AtomicInteger successCallCount;
    private static AtomicInteger failCallCount;
    private static Integer numThreadsBefore;
    private static Integer numThreadsAfter;
    private static SyncList allRecords;
    private static final Integer phaseOneCount = 1000;
    private static final Integer totalCount = 200000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("*********************************************************");
        System.out.println("Consumer starts...");
        System.out.println("*********************************************************");

        //Change this address every time you start the server on ec2
        IPAddress = "34.213.40.199:8080";
        //IPAddress = "myalb-799446580.us-west-2.elb.amazonaws.com:8080";
        //IPAddress = "localhost:8080";
        buffer = new LinkedBlockingQueue<>();
        successCallCount = new AtomicInteger(0);
        failCallCount = new AtomicInteger(0);
        numThreadsBefore = 100;
        numThreadsAfter = 300;
        allRecords = new SyncList();
        CountDownLatch latch1 = new CountDownLatch(numThreadsBefore);
        CountDownLatch latch2 = new CountDownLatch(numThreadsAfter);
        long start = System.currentTimeMillis();
        Producer producer = new Producer(numThreadsAfter, totalCount, buffer);
        Thread producerThread = new Thread(producer);
        producerThread.start();

        for (int i = 0; i < numThreadsBefore; i++) {
            ConsumerRunnable cm1 = new ConsumerRunnable(phaseOneCount, true, IPAddress, buffer, successCallCount, failCallCount, latch1, allRecords);
            Thread thread1 = new Thread(cm1);
            thread1.start();
        }
        latch1.await();

        for (int j = 0; j < numThreadsAfter; j++) {
            ConsumerRunnable cm2 = new ConsumerRunnable(phaseOneCount, false, IPAddress, buffer, successCallCount, failCallCount, latch2, allRecords);
            Thread thread2 = new Thread(cm2);
            thread2.start();
        }
        latch2.await();

        long end = System.currentTimeMillis();
        long wallTime = end - start;

        try {
            CSVWriter.outputCSVFile(allRecords.getRecords(), "C:\\Users\\benje\\Desktop\\neu\\CS6650\\assignment1\\output\\", "records.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StatisticsProcessor ap = new StatisticsProcessor(allRecords.getRecords());
        double meanTime = ap.meanResponse();
        double medianTime = ap.medianResponse();
        double per99 = ap.get99Percent();
        double maxLan = ap.getMaxResponse();
        double minLan = ap.getMinResponse();

        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests:" + successCallCount.get());
        System.out.println("Number of failed requests:" + failCallCount.get());
        System.out.println("Total wall time:" + wallTime);
        System.out.println("Mean response time (in milliseconds):" + meanTime);
        System.out.println("Median response time (in milliseconds):" + medianTime);
        System.out.println("P99 (99th percentile) response time (in milliseconds):" + per99);
        System.out.println( "Throughput:" + (int)((successCallCount.get() + failCallCount.get()) /
            (double)(wallTime / 1000) )+ " requests/second");
        System.out.println("Max response time (in milliseconds):" + maxLan);
        System.out.println("Min response time (in milliseconds):" + minLan);
    }
}
