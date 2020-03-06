package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Callback;
import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;
import bgu.spl.mics.application.passiveObjects.ResultMoneyPenny;
import bgu.spl.mics.application.passiveObjects.ResultQ;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {
    private int uniqueNum;
    private MessageBrokerImpl MB;
    private int currTime;

    public M(int num) {
        super("M");
        MB = MessageBrokerImpl.getInstance();
        uniqueNum = num;
        currTime = 0;
    }

    @Override
    protected void initialize() {
        Callback<MissionReceivedEvent> callback = new Callback<MissionReceivedEvent>() {

            @Override
            public synchronized void call(MissionReceivedEvent c) {
                Diary.getInstance().incrementTotal();
                int moneypennySerialNum = 0;
                List<String> agentsNames = null;
                int qtime = 0;
                Future<ResultMoneyPenny> futureAgents = getSimplePublisher().sendEvent(new AgentAvailableEvent(c.getMissionInfo().getSerialAgentsNumbers(), c.getMissionInfo().getTimeExpired()));
                if (futureAgents != null) {
                    ResultMoneyPenny resolvedAgents = futureAgents.get(c.getMissionInfo().getTimeExpired()-c.getMissionInfo().getTimeIssued(),TimeUnit.MILLISECONDS);
                    if (!resolvedAgents.getAgentNames().isEmpty()) {
                        moneypennySerialNum = resolvedAgents.getUniqueNUm();
                        agentsNames = resolvedAgents.getAgentNames();

                        //sending GadgetsAvailableEvent
                        Future<ResultQ> futureGadget = getSimplePublisher().sendEvent(new GadgetAvailableEvent(c.getMissionInfo().getGadget()));
                        if (futureGadget != null) {
                            ResultQ resolvedGadget = futureGadget.get(c.getMissionInfo().getTimeExpired()-c.getMissionInfo().getTimeIssued(),TimeUnit.MILLISECONDS);
                            if (resolvedGadget.getAns()) {
                                qtime = resolvedGadget.getTimeQ();

                                //checking if expiry time has passed
                                if (c.getMissionInfo().getTimeExpired() <= currTime) {
                                    ////if so - releasing agents:
                                    Future<Boolean> futureReleaseAgents = getSimplePublisher().sendEvent(new ReleaseAgentsEvent(c.getMissionInfo().getSerialAgentsNumbers()));
                                    if (futureReleaseAgents == null) {
                                        System.out.println("Error releasing agents");
                                    }
                                } else {
                                    Future<Boolean> futureSendAgents = getSimplePublisher().sendEvent(new SendAgentEvent(c.getMissionInfo().getSerialAgentsNumbers(), c.getMissionInfo().getDuration()));
                                    Report currReport = new Report(c.getMissionInfo().getMissionName(), uniqueNum, moneypennySerialNum, c.getMissionInfo().getSerialAgentsNumbers(), agentsNames, c.getMissionInfo().getGadget(), c.getMissionInfo().getTimeIssued(), currTime, qtime);
                                    Diary.getInstance().addReport(currReport);
                                }
                            }
                        }
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

        Callback<TickBroadcast> callbackTime = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                currTime = c.getPassedTime();
            }
        };

        subscribeBroadcast(TickBroadcast.class, callbackTime);
        subscribeEvent(MissionReceivedEvent.class, callback);
        subscribeBroadcast(TerminateBroadcast2.class, callback2);
    }

}
