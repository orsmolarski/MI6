package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

public class MissionReceivedEvent implements Event {
    private MissionInfo missionInfo;

    public MissionReceivedEvent(MissionInfo misInfo)
    {
        synchronized (this){
            this.missionInfo=misInfo;
        }
    }

    public MissionInfo getMissionInfo() {
        return missionInfo;
    }
}
