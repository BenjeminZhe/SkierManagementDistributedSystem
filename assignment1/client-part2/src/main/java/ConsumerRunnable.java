import static java.net.HttpURLConnection.HTTP_CREATED;
import static java.net.HttpURLConnection.HTTP_OK;

import Model.Event;
import Model.OutputRecord;
import Model.SyncList;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <h3>hw1client</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-06 01:27
 **/
public class ConsumerRunnable implements Runnable {
    private int count;
    private int MAX_RETRY = 5;
    private boolean phaseOneOrNot;
    private String ipAddress;
    Queue<Event> buffer;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;
    CountDownLatch latch;
    SyncList recordHolder;

    public ConsumerRunnable(int count, boolean phaseOneORNot, String ipAddress, Queue<Event> buffer,
        AtomicInteger successCallCount, AtomicInteger failCallCount, CountDownLatch latch, SyncList recordHolder) {
        this.count = count;
        this.phaseOneOrNot = phaseOneORNot;
        this.ipAddress = ipAddress;
        this.buffer = buffer;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;
        this.latch = latch;
        this.recordHolder = recordHolder;
    }


    @Override
    public void run() {
        String url = "http://" + this.ipAddress + "/server_war/";
        SkiersApi api = new SkiersApi();
        List<OutputRecord> records = new ArrayList<>();
        api.getApiClient().setBasePath(url);
        int succCount = 0;
        int failCount = 0;
        if (this.phaseOneOrNot) {
            int i = 0;
            while (i < count) {
                Event curEvent = buffer.poll();
                if (callPostWithEvent(api, curEvent, records)) {
                    succCount += 1;
                } else {
                    failCount += 1;
                }
                i++;
            }
        } else {
            while (true) {
                Event curEvent = buffer.poll();
                if (!checkEvent(curEvent)) {
                    break;
                }

                if (callPostWithEvent(api, curEvent, records)) {
                    succCount += 1;
                } else {
                    failCount += 1;
                }
            }
        }

        this.successCallCount.getAndAdd(succCount);
        this.failCallCount.getAndAdd(failCount);
        this.recordHolder.addNewRecords(records);
        this.latch.countDown();
    }

    private boolean callPostWithEvent(SkiersApi api, Event curEvent, List<OutputRecord> records) {
        int retry = 0;
        while (retry < MAX_RETRY) {
            try {
                long start = System.currentTimeMillis();
                LiftRide curLiftRide = new LiftRide();
                curLiftRide.setLiftID(curEvent.getLiftId());
                curLiftRide.setTime(curEvent.getTime());
                ApiResponse<Void> res = api.writeNewLiftRideWithHttpInfo(curLiftRide, curEvent.getResortId(), curEvent.getSeasonId(), curEvent.getDayId(), curEvent.getSkierId());
                if (res.getStatusCode() == HTTP_OK || res.getStatusCode() == HTTP_CREATED) {
                    long end = System.currentTimeMillis();
                    System.out.println(end - start);
                    records.add(new OutputRecord(start, end - start, res.getStatusCode()));
                    return true;
                }

                if (res.getStatusCode() >= 400) {
                    retry++;
                }
            } catch (ApiException e) {
                retry++;
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkEvent(Event curEvent) {
        if (curEvent.getResortId() > 10) {
            return false;
        }
        return true;
    }
}
