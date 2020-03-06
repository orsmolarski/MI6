package bgu.spl.mics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;

public class FutureTest {

    Future<Object> futureEvent;

    @BeforeEach
    public void setUp(){
        futureEvent= new Future<>();
    }

    @Test
    public void test(){
        //TODO: change this test and add more tests :)
        fail("Not a good test");
    }

    @Test
    public void isDoneTest(){
        assertFalse(futureEvent.isDone());
        futureEvent.resolve(5);
        assertTrue(futureEvent.isDone());
    }

    @Test
    public void resolveTest(Integer result){
        assertNotEquals(futureEvent.get(), 78);
        futureEvent.resolve(5);
        assertEquals(futureEvent.get(), 5);

    }

    @Test
    public void getTest()
    {
        assertEquals(futureEvent.get(), null);
        futureEvent.resolve(true);
        assertEquals(futureEvent.get(), true);
    }
    @AfterEach
    public void tearDown()
    {
        futureEvent=null;
    }
}