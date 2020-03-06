package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Callback;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.ResultMoneyPenny;
import bgu.spl.mics.application.passiveObjects.Squad;
import java.util.LinkedList;
import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
    private int uniqueNum;
    private Squad squad;
    private MessageBrokerImpl MB;


    public Moneypenny(int uniqeN) {
        super("MoneyPenny");
        squad = Squad.getInstance();
        MB = MessageBrokerImpl.getInstance();
        uniqueNum = uniqeN;
    }

    @Override
    protected void initialize() {
        if (uniqueNum % 2 == 0) {
            Callback<AgentAvailableEvent> callback = new Callback<AgentAvailableEvent>() {
                @Override
                public void call(AgentAvailableEvent c) {
                    synchronized (squad) {
                        List<String> names = squad.getAgentsNames(c.getAgensSerials());
                        squad.getAgents(c.getAgensSerials());
                        complete(c, new ResultMoneyPenny(uniqueNum, names));
                    }
                }
            };

            Callback<SendAgentEvent> callback3 = new Callback<SendAgentEvent>() {
                @Override
                public void call(SendAgentEvent c) {
                    squad.sendAgents(c.getSerials(), c.getTime());
                    complete(c, true);
                }
            };
            subscribeEvent(SendAgentEvent.class, callback3);
            subscribeEvent(AgentAvailableEvent.class, callback);

        } else {
            Callback<ReleaseAgentsEvent> callback1 = new Callback<ReleaseAgentsEvent>() {
                @Override
                public void call(ReleaseAgentsEvent c) {
                    synchronized (squad) {
                        squad.releaseAgents(c.getAgentsToRelease());
                        complete(c, true);

                    }
                }
            };
            subscribeEvent(ReleaseAgentsEvent.class, callback1);
        }
        Callback<TerminateBroadcast3> callback2 = new Callback<TerminateBroadcast3>() {
            @Override
            public void call(TerminateBroadcast3 c) {
                terminate();
            }
        };


        subscribeBroadcast(TerminateBroadcast3.class, callback2);
    }
}
