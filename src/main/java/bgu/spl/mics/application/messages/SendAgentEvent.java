package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class SendAgentEvent implements Event {
    private List<String> serials;
    private int time;

    public SendAgentEvent(List<String> s, int Time){
        this.serials=s;
        time = Time;
    }

    public List<String> getSerials() {
        return serials;
    }

    public int getTime() {
        return time;
    }



}
