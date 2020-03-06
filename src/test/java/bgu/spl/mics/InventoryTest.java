package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    Inventory Inv ;

    @BeforeEach
    public void setUp(){
        Inv = new Inventory();
    }


    @Test
    public void loadTest(){
        String [] gadgetslist = {"Pistol","Rifle"};
        Inv.load(gadgetslist);
        //List<String> CurrGadgets = Inv.getGadgets();
        //Assertions.assertArrayEquals(CurrGadgets.toArray(), gadgetslist);
    }

    @Test
    public void getItemTest(){
        String [] gadgetslist = {"Pistol","Rifle"};
        Inv.load(gadgetslist);
        Assertions.assertTrue(Inv.getItem("Pistol"));
        Assertions.assertFalse(Inv.getItem("Piu-Piu"));
    }

    @Test
    public void getInstanceTest(){
        //Inventory is a Singleton and therefor there should be only one instance
        Inv = Inventory.getInstance();
        Assertions.assertTrue(Inv == Inventory.getInstance());
    }


    @AfterEach
    public void tearDown(){
        Inv = null;
    }
}