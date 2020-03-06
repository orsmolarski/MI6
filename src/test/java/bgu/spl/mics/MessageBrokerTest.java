package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import bgu.spl.mics.application.subscribers.*;


import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    MessageBroker MB = MessageBrokerImpl.getInstance();

    @BeforeEach
    public void setUp(){ }

    @Test
    public void test(){

    }

    @Test
    void getInstance() {
        //MessageBroker is a Singleton and therefor there should be only one instance
        MB = MessageBrokerImpl.getInstance();
        Assertions.assertTrue(MB == MessageBrokerImpl.getInstance());
    }

    @Test
    void subscribeEvent() {
        Subscriber S1 = new Q();
        MB.register(S1);
        GadgetAvailableEvent A = new GadgetAvailableEvent("gogo");

        try {
            //MB.subscribeEvent(GadgetAvailableEvent.class, S1);
        } catch (Exception e) {

        }
    }


    @Test
    void complete() {
        Subscriber temp = new Q();
        Event curr = new Event(){};
        GadgetAvailableEvent A = new GadgetAvailableEvent("mami");
        try {
            MB.complete(curr, A);
            fail("complete method failed");
        }catch (Exception e){

        }
    }


    @Test
    void sendEvent() {
        Subscriber S1 = new Moneypenny(1);
        Subscriber S2 = new Q();
        MB.register(S1);
        MB.register(S2);
        GadgetAvailableEvent A = new GadgetAvailableEvent("kim k");
        try{
            MB.sendEvent(A);
            fail("sendEvent failed");
        }catch (Exception e){

        }

    }

    @Test
    void register() {
        Subscriber temp;
        temp = new Q();
        try{
            MB.register(temp);
            fail("register failed");
        }catch(Exception e){

        }
    }

    @Test
    void unregister() {
        Subscriber temp;
        temp = new Q();
        MB.register(temp);
        MB.unregister(temp);
        try{
            MB.unregister(temp);
            fail("unregister failed");
        }catch(Exception e){

        }

    }

    @Test
    void awaitMessage() {
        Subscriber S1 = new Q();
        MB.register(S1);
        try{MB.awaitMessage(S1);
            fail("should have thrown exception");
        } catch (Exception e){

        }

        GadgetAvailableEvent A = new GadgetAvailableEvent("Kanye The messiah");
        //MB.subscribeEvent(A.getClass(),S1);
        MB.sendEvent(A);
        Message Es = null;
        try{ Es = MB.awaitMessage(S1);}catch (Exception e){};
        assertEquals(A,Es);


    }

    @AfterEach
    public void tearDown(){ MB = null;}
}