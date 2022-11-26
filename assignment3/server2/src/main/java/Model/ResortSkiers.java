package Model;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 15:33
 **/
public class ResortSkiers {
    private String name;
    private int numSkiers;

    public ResortSkiers(String name, int numSkiers) {
        this.name = name;
        this.numSkiers = numSkiers;
    }

    public String getName() {
        return name;
    }

    public int getNumSkiers() {
        return numSkiers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumSkiers(int numSkiers) {
        this.numSkiers = numSkiers;
    }
}
