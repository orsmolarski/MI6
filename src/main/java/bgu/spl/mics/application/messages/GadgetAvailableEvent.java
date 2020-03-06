package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class GadgetAvailableEvent implements Event {
    private String gadget;

    public GadgetAvailableEvent(String g) {
        this.gadget = g;
    }

    public String getGadget() {
        synchronized (this) {
            return this.gadget;
        }
    }
}
