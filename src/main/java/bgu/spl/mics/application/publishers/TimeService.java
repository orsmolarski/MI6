package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadcast1;
import bgu.spl.mics.application.messages.TerminateBroadcast2;
import bgu.spl.mics.application.messages.TerminateBroadcast3;
import bgu.spl.mics.application.messages.TickBroadcast;
import org.junit.rules.Timeout;

import java.util.Timer;
import java.util.TimerTask;


/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
    private MessageBrokerImpl MB;
    private int duration;
    private int count;
    private Timer timer;

    public TimeService(int duration) {
        super("TimeService");
        this.duration = duration;
        count = 0;
        timer = new Timer("Timer");
        MB = MB.getInstance();
    }

    private TimeService() {
        super("TimeService");
    }

    @Override
    protected void initialize() {
        TimerTask repeatedTask = new TimerTask() {
            public void run() {
                if (count < duration) {
                    //TickBroadcast tick = new TickBroadcast(count);
                    MB.sendBroadcast(new TickBroadcast(count));
                    count++;
                } else {
                    timer.cancel();
                    timer.purge();
                    MB.sendBroadcast(new TerminateBroadcast1());
                    MB.sendBroadcast(new TerminateBroadcast2());
                    MB.sendBroadcast(new TerminateBroadcast3());

                }
            }
        };
        timer.schedule(repeatedTask, 100, 100);

    }

    @Override
    public void run() {
        initialize();
    }

}
