package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 15:33
 **/
public class SeasonsList {
    List<String> arr;

    public SeasonsList() {
        arr = new ArrayList<>();
    }

    public void addSeasons(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            throw new RuntimeException("Not valid parameter!");
        }
        arr.add(str);
    }
}
