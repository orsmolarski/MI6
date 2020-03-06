package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentAvailableEvent implements Event {
    private List<String> SerialAgents;
    private int time;

    public AgentAvailableEvent(List<String> serial, int duration) {
        this.SerialAgents = serial;
        this.time=duration;
    }

    public List<String> getAgensSerials() {
        synchronized (this) {
            return SerialAgents;
        }
    }

    public int getTime() {
        return time;
    }
}
