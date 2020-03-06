package bgu.spl.mics;
import bgu.spl.mics.application.messages.TerminateBroadcast1;
import bgu.spl.mics.application.messages.TerminateBroadcast2;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Q;
import java.util.Map;
import java.util.concurrent.*;

/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {
    private static class SingletonHolder {
        private static MessageBrokerImpl instance = new MessageBrokerImpl();
    }

    private volatile ConcurrentHashMap<Subscriber, LinkedBlockingQueue<Message>> CHMSubsciber;
    private volatile ConcurrentHashMap<Class<? extends Event>,ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>> CHMEvents;
    private volatile ConcurrentHashMap<Class<? extends Broadcast>, ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>> CHMBroadcasts;
    private volatile ConcurrentHashMap<Event, Future> EventFutureCHM;

    private MessageBrokerImpl() {
        CHMSubsciber = new ConcurrentHashMap<>();
        CHMEvents = new ConcurrentHashMap<>();
        CHMBroadcasts = new ConcurrentHashMap<>();
        EventFutureCHM = new ConcurrentHashMap<>();

    }

    /**
     * Retrieves the single instance of this class.
     */
    public static MessageBrokerImpl getInstance() {
        return SingletonHolder.instance;
    }

    @Override
    public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber m) {
        synchronized (this) {
            if (!CHMEvents.containsKey(type)) {
                ConcurrentLinkedQueue<LinkedBlockingQueue<Message>> RoundRobinQ = new ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>();
                CHMEvents.put(type, RoundRobinQ);
            }
            if (!CHMEvents.get(type).contains(CHMSubsciber.get(m))) {
                    CHMEvents.get(type).add(CHMSubsciber.get(m));
            }
        }
    }

    @Override
    public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber m) {
        synchronized (this) {
            //checking if CHMBroadcast contain a Queue for this type of event, if not, initializing a new one and inserting
            // subscriber m msgQueue into the matching Queue in CHMBroadcast under this type.
            if (!CHMBroadcasts.containsKey(type)) {
                ConcurrentLinkedQueue<LinkedBlockingQueue<Message>> BroadcastTypeQ = new ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>();
                CHMBroadcasts.put(type, BroadcastTypeQ);
            }
            if (!CHMBroadcasts.get(type).contains(CHMSubsciber.get(m))) {
                    CHMBroadcasts.get(type).add(CHMSubsciber.get(m));

            }
        }
    }

    @Override
    public <T> void complete(Event<T> e, T result) {
        Future<T> ans = EventFutureCHM.get(e);
        ans.resolve(result);
    }

    @Override
    public void sendBroadcast(Broadcast b) {
        synchronized (this) {
            if (b.getClass() == TerminateBroadcast1.class) {
                CHMSubsciber.get(Q.getInstance()).clear();
                try {
                    CHMSubsciber.get(Q.getInstance()).put(b);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("ERROR Inserting TerminateBroad into Q's msg Q");
                }
            }
            for (LinkedBlockingQueue<Message> messages : CHMBroadcasts.get(b.getClass())) {
                try {
                    messages.put(b);
                } catch (InterruptedException e) {
                    System.out.println("ERROR Inserting Broadcast in to certain Q");
                }
            }
        }
    }


    @Override
    public <T> Future<T> sendEvent(Event<T> e) {
        synchronized (this) {
            if (CHMEvents.containsKey(e.getClass())) {
                ConcurrentLinkedQueue<LinkedBlockingQueue<Message>> curr = CHMEvents.get(e.getClass());
            if (!curr.isEmpty()) {
                    Future<T> result = new Future<>();
                    EventFutureCHM.put(e, result);
                    LinkedBlockingQueue<Message> tempToSend = curr.poll();
                    try {
                        tempToSend.put(e);
                    } catch (InterruptedException ex) {
                        System.out.println("ERROR SENDING EVENT(adding event to msqQ)");
                        ex.printStackTrace();
                    }
                    curr.add(tempToSend);
                    return result;
                }
            }
            return null;
        }
    }

    @Override
    public void register(Subscriber m) {
        LinkedBlockingQueue<Message> Q = new LinkedBlockingQueue<>();
        CHMSubsciber.put(m, Q);
    }

    @Override
    public synchronized void unregister(Subscriber m) {
            if (CHMSubsciber.containsKey(m)) {
                LinkedBlockingQueue Q = CHMSubsciber.get(m);
                for (Map.Entry<Class<? extends Broadcast>, ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>> entry : CHMBroadcasts.entrySet()
                ) {
                    if (entry.getValue().contains(Q)) {
                        entry.getValue().remove(Q);
                    }
                }
                for (Map.Entry<Class<? extends Event>, ConcurrentLinkedQueue<LinkedBlockingQueue<Message>>> entry : CHMEvents.entrySet()
                ) {
                    if (entry.getValue().contains(Q)) {
                        entry.getValue().remove(Q);
                    }
                }
                CHMSubsciber.remove(m);
            }
    }


    public Message awaitMessage(Subscriber m) throws InterruptedException {
        return CHMSubsciber.get(m).take();

    }


}
