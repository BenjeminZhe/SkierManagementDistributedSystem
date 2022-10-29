import Model.OutputRecord;
import java.util.Collections;
import java.util.List;

/**
 * <h3>client-part2</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-07 15:44
 **/
public class StatisticsProcessor {
    private List<OutputRecord> records;

    public StatisticsProcessor(List<OutputRecord> records) {
        this.records = records;
        Collections.sort(this.records);
    }

    public double meanResponse() {
        long sum = 0;
        for (OutputRecord record : this.records) {
            sum += record.getLatency();
        }

        return (double) sum / (double)(this.records.size());
    }

    public double medianResponse() {
        if (this.records.size() % 2 == 1) {
            return records.get(records.size() / 2).getLatency();
        } else {
            return (records.get(records.size() / 2 - 1).getLatency() + records.get(records.size() / 2).getLatency()) / 2;
        }
    }

    public double get99Percent() {
        return records.get((int)Math.floor(records.size()*0.99)).getLatency();
    }

    public double getMinResponse() {
        return records.get(0).getLatency();
    }

    public double getMaxResponse() {
        return records.get(records.size() - 1).getLatency();
    }
}
