package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 16:01
 **/
public class ResortsList {
    private List<Resorts> resorts;

    public ResortsList() {
        resorts = new ArrayList<>();
    }

    public void addResorts(Resorts resort) {
        resorts.add(resort);
    }
}
