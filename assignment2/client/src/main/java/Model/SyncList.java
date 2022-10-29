package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>client-part2</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-07 16:27
 **/
public class SyncList {
    private List<OutputRecord> records;

    public SyncList() {
        this.records = new ArrayList<>();
    }

    public synchronized void addNewRecords(List<OutputRecord> newRecords) {
        this.records.addAll(newRecords);
    }

    public List<OutputRecord> getRecords() {
        return records;
    }
}
