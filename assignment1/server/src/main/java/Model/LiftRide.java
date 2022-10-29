package Model;

/**
 * <h3>server</h3>
 *
 * @author Zhe Xi
 * @description <p></p>
 * @date 2022-10-04 02:40
 **/
public class LiftRide {
    public String liftID;
    public String time;

    public int resortId;
    public int seasonId;
    public int dayId;
    public int skierId;

    public LiftRide(String liftID, String time) {
        this.liftID = liftID;
        this.time = time;
    }

    public String getLiftID() {
        return liftID;
    }

    public void setLiftID(String liftID) {
        this.liftID = liftID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getResortId() {
        return resortId;
    }

    public void setResortId(int resortId) {
        this.resortId = resortId;
    }

    public int getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(int seasonId) {
        this.seasonId = seasonId;
    }

    public int getDayId() {
        return dayId;
    }

    public void setDayId(int dayId) {
        this.dayId = dayId;
    }

    public int getSkierId() {
        return skierId;
    }

    public void setSkierId(int skierId) {
        this.skierId = skierId;
    }

    @Override
    public String toString() {
        return "LiftRide{" +
                "liftID='" + liftID + '\'' +
                ", time='" + time + '\'' +
                ", resortId=" + resortId +
                ", seasonId=" + seasonId +
                ", dayId=" + dayId +
                ", skierId=" + skierId +
                '}';
    }
}
