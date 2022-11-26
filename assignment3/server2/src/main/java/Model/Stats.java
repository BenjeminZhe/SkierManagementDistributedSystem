package Model;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 03:14
 **/
public class Stats {
    private String URL;
    private String operation;
    private int mean;
    private int max;

    public Stats(String URL, String operation, int mean, int max) {
        this.URL = URL;
        this.operation = operation;
        this.mean = mean;
        this.max = max;
    }
    public Stats() {

    }

    public String getURL() {
        return URL;
    }

    public String getOperation() {
        return operation;
    }

    public int getMean() {
        return mean;
    }

    public int getMax() {
        return max;
    }
}
