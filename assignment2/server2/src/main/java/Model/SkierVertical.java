package Model;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 15:32
 **/
public class SkierVertical {
    private String seasonId;
    private int vertical;

    public SkierVertical(String seasonId, int vertical) throws RuntimeException {
        setSeasonId(seasonId);
        this.vertical = vertical;
    }

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) throws RuntimeException {
        try {
            Integer.parseInt(seasonId);
        } catch (Exception e) {
            throw new RuntimeException("Invalid seasonId!");
        }
        this.seasonId = seasonId;
    }

    public int getVertical() {
        return vertical;
    }

    public void setVertical(int vertical) {
        this.vertical = vertical;
    }
}
