package ds.project1task3;
/*
 * @author Yi Guo
 * Andrew ID: yiguo
 *
 * This file is the Player class that is used to store all statistics and bios related to a player.
 *
 */

public class Player {
    // baseball related statistics
    String AB, H, HR, R, RBI, SB;
    String WAR, BA, OBP, SLG, OPS;
    // bio info
    String image;
    String team;
    String birth;
    String position;
    String name;

    /**
     * Below are the getters of the variables.
     *
     * @return string of the variable values
     */
    public String getName() {
        return name;
    }

    public String getAB() {
        return AB;
    }

    public String getH() {
        return H;
    }

    public String getHR() {
        return HR;
    }

    public String getR() {
        return R;
    }

    public String getRBI() {
        return RBI;
    }

    public String getSB() {
        return SB;
    }

    public String getWAR() {
        return WAR;
    }

    public String getBA() {
        return BA;
    }

    public String getOBP() {
        return OBP;
    }

    public String getSLG() {
        return SLG;
    }

    public String getOPS() {
        return OPS;
    }

    public String getImage() {
        return image;
    }

    public String getTeam() {
        return team;
    }

    public String getBirth() {
        return birth;
    }

    public String getPosition() {
        return position;
    }


}
