package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadcast2;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

import java.util.*;


/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {
    private List<MissionInfo> missions;
    private static MessageBrokerImpl MB = MessageBrokerImpl.getInstance();

    public Intelligence(List<MissionInfo> list) {
        super("Intelligence");
        missions = list;
    }

    public void setMissionList(List<MissionInfo> missions) {
        this.missions = missions;
    }

    public List<MissionInfo> getMissionList() {
        return missions;
    }

    @Override
    protected void initialize() {
        Callback<TickBroadcast> callback = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                synchronized (this) {
                    if (!missions.isEmpty()) {
                        for (int i = 0; i < missions.size(); i++) {
                            MissionInfo x = missions.get(i);
                            if (x.getTimeIssued() == c.getPassedTime()) {
                                MB.sendEvent(new MissionReceivedEvent(missions.get(i)));
                            }
                        }
                        for (int i = 0; i < missions.size(); i++) {
                            MissionInfo x = missions.get(i);
                            if (x.getTimeIssued() == c.getPassedTime()) {
                                missions.remove(i);
                            }
                        }
//
                    }
                }
            }
        };
        Callback<TerminateBroadcast2> callback2 = new Callback<TerminateBroadcast2>() {
            @Override
            public void call(TerminateBroadcast2 c) {
                terminate();
            }
        };
        subscribeBroadcast(TickBroadcast.class, callback);
        subscribeBroadcast(TerminateBroadcast2.class, callback2);
    }

}






