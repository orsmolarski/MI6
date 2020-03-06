package bgu.spl.mics.application.messages;

import bgu.spl.mics.Broadcast;

public class TickBroadcast implements Broadcast {
    private int passedTime;

    public TickBroadcast(int time)
    {
        this.passedTime=time;
    }

    public int getPassedTime() {
        return passedTime;
    }
}
