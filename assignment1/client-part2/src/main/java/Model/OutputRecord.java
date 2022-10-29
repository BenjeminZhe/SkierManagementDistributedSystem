package Model;

/**
 * <h3>client-part2</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-07 15:33
 **/
public class OutputRecord implements Comparable<OutputRecord> {
    private long startTime;
    private long latency;
    private int responseCode;

    public OutputRecord(long startTime, long latency, int responseCode) {
        this.startTime = startTime;
        this.latency = latency;
        this.responseCode = responseCode;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    @Override
    public int compareTo(OutputRecord o) {
        if (this.getLatency() - o.getLatency() > 0) {
            return 1;
        } else if (this.getLatency() - o.getLatency() == 0) {
            return 0;
        }
        return -1;
    }

    public String toString() {
        return this.getStartTime() + ",POST," + this.getLatency() + "," + this.getResponseCode();
    }
}
