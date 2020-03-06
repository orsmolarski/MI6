package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.ResultQ;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {
    Inventory inventory;
    private MessageBrokerImpl MB;
    private int QTime;

    private static class SingletonHolder {
        private static Q instance = new Q();
    }

    public static Q getInstance() {
        return Q.SingletonHolder.instance;
    }

    public Q() {
        super("Q");
        inventory = Inventory.getInstance();
        MB= MessageBrokerImpl.getInstance();
        QTime=0;
    }

    @Override
    protected void initialize() {

        Callback<GadgetAvailableEvent> callbackGadget = new Callback<GadgetAvailableEvent>() {
            @Override
            public void call(GadgetAvailableEvent c) {
                synchronized (this) {
                    if (inventory.getItem(c.getGadget())) {
                        inventory.remove(c.getGadget());
                        complete(c, new ResultQ(QTime, true));
                    } else {
                        complete(c, new ResultQ(QTime, false));
                    }
                }
            }
        };

        Callback<TickBroadcast> callbackTime = new Callback<TickBroadcast>() {
            @Override
            public void call(TickBroadcast c) {
                QTime=c.getPassedTime();
            }
        };
        Callback<TerminateBroadcast1> callback2 = new Callback<TerminateBroadcast1>() {
            @Override
            public void call(TerminateBroadcast1 c) {
                inventory = null;
                terminate();
            }
        };

        subscribeBroadcast(TickBroadcast.class, callbackTime);
        subscribeEvent(GadgetAvailableEvent.class, callbackGadget);
        subscribeBroadcast(TerminateBroadcast1.class, callback2 );
    }

}
