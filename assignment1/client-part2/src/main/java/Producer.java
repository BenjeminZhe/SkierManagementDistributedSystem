import Model.Event;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <h3>hw1client</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-05 22:47
 **/
public class Producer implements Runnable {
    private static final int SKIER_ID_BEGIN = 1;
    private static final int SKIER_ID_END = 100000;
    private static final int RESORT_ID_BEGIN = 1;
    private static final int RESORT_ID_END = 10;
    private static final int LIFT_ID_BEGIN = 1;
    private static final int LIFT_ID_END = 40;
    private static final String SEASON_ID = "2022";
    private static final String DAY_ID = "1";
    private static final int TIME_BEGIN = 1;
    private static final int TIME_END = 360;
    //Use wrong id to denote the stop signal.
    private static final int WRONG_RESORT_ID = 20;

    private BlockingQueue<Event> eventBuffer;
    int numberOfStopSignals;
    int total_posts;

    public Producer(int numberOfStopSignals, int total_posts, BlockingQueue<Event> queue) {
        this.numberOfStopSignals = numberOfStopSignals;
        this.eventBuffer = queue;
        this.total_posts = total_posts;
    }

    @Override
    public void run() {
        int i = 0, j = 0;
        while (i < total_posts) {
            Integer curLiftId = ThreadLocalRandom.current().nextInt(LIFT_ID_BEGIN, LIFT_ID_END + 1);
            Integer curResortId = ThreadLocalRandom.current().nextInt(RESORT_ID_BEGIN, RESORT_ID_END + 1);
            Integer curSkierId = ThreadLocalRandom.current().nextInt(SKIER_ID_BEGIN, SKIER_ID_END + 1);
            Integer curTime = ThreadLocalRandom.current().nextInt(TIME_BEGIN, TIME_END + 1);
            Event curEvent = new Event(curSkierId, curResortId, curLiftId, SEASON_ID, DAY_ID, curTime);
            eventBuffer.offer(curEvent);
            i++;
        }

        while (j < numberOfStopSignals) {
            eventBuffer.offer(new Event(1, WRONG_RESORT_ID, 1, SEASON_ID, DAY_ID, 1));
            j++;
        }
    }
}
