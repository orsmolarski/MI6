package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SquadTest {
    Squad tstSqd;
    @BeforeEach
    public void setUp(){
        Squad tstSqd= new Squad();
    }


    @Test
    /**
     * @pre: none
     * @post: this.tstSqd.isEmpty()==false
     */
    public void loadTest(){
        this.prepare();
        List<String> test=new LinkedList<>();
        test.add("001");
        test.add("002");
        test.add("003");
       // List<String> toCompare= new ArrayList(tstSqd.getMap().keySet());
       // assertTrue(test.equals(toCompare));
    }

    @Test
    public void getInstanceTest()
    {
        assertEquals(tstSqd,Squad.getInstance());
    }

    @Test
    public void releaseAgentTest(List<String> sNum)
    {
        for (int i = 0; i < sNum.size(); i++) {
            //assertFalse(tstSqd.getMap().containsKey(sNum.get(i)));
        }
    }

    @Test
    public void sendAgentsTest(){
        this.prepare();
        List<String> sendAway=new LinkedList<>();
        sendAway.add("001");
        tstSqd.sendAgents(sendAway, 1500);
       // assertFalse(tstSqd.getMap().containsKey("001"));
    }

    @Test
    public void getAgentsTest(List<String> serials)
    {
        this.prepare();
        List<String> getList= new LinkedList<>();
        getList.add("001");
        getList.add("002");
        assertTrue(tstSqd.getAgents(getList));
        assertFalse(tstSqd.getAgents(getList));
    }

    @Test
    public void getAgentsNames(List<String> sNum)
    {
        this.prepare();
        List<String> getList= new LinkedList<>();
        getList.add("001");
        getList.add("002");
        getList.add("003");
        List<String> checkList= new LinkedList<>();
        getList.add("one");
        getList.add("two");
        getList.add("three");
        List<String> output= tstSqd.getAgentsNames(getList);
        assertEquals(checkList, output);
        checkList.remove(0);
        assertNotEquals(checkList,output);
    }

    @AfterEach
    public void tearDown()
    {
        tstSqd=null;
    }

    public void prepare()
    {
        Agent one= new Agent();
        one.setName("one");
        one.setSerialNumber("001");
        Agent two= new Agent();
        one.setName("two");
        one.setSerialNumber("002");
        Agent three= new Agent();
        one.setName("three");
        one.setSerialNumber("003");
        Agent[] agntArr={one, two, three};
        this.tstSqd.load(agntArr);
    }
}