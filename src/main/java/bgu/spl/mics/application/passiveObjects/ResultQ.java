package bgu.spl.mics.application.passiveObjects;

public class ResultQ {
    private int timeQ;
    private boolean isExist;

    public ResultQ(int time, boolean ans)
    {
        this.timeQ=time;
        isExist=ans;
    }

    public int getTimeQ() {
        return timeQ;
    }

    public boolean getAns() {
        return isExist;
    }
}
