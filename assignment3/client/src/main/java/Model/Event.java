package Model;

/**
 * <h3>hw1client</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-05 22:51
 **/
public class Event {
    private int skierId;
    private int resortId;
    private int liftId;
    private String SeasonId;
    private String dayId;
    private int time;

    public Event(int skierId, int resortId, int liftId, String seasonId, String dayId, int time) {
        this.skierId = skierId;
        this.resortId = resortId;
        this.liftId = liftId;
        SeasonId = seasonId;
        this.dayId = dayId;
        this.time = time;
    }

    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public int getLiftId() {
        return liftId;
    }

    public void setLiftId(int liftId) {
        this.liftId = liftId;
    }

    public String getSeasonId() {
        return SeasonId;
    }

    public void setSeasonId(String seasonId) {
        SeasonId = seasonId;
    }

    public String getDayId() {
        return dayId;
    }

    public void setDayId(String dayId) {
        this.dayId = dayId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
