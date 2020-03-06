package bgu.spl.mics.application;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.passiveObjects.*;
import bgu.spl.mics.application.publishers.TimeService;
import bgu.spl.mics.application.subscribers.Intelligence;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.application.subscribers.Q;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/** This is the Main class of the application. You should parse the input file,
 * create the different instances of the objects, and run the system.
 * In the end, you should output serialized objects.
 */
public class MI6Runner {
    public static void main(String[] args) {
        try {
            Input input = JsonInputReader.getInputFromJson(args[0]);
            List<Thread> runnableList =new LinkedList<Thread>();

            //Inventory initialization
            Inventory inventory = Inventory.getInstance();
            inventory.load(input.getInventory());

            //Squad initialization
            Squad squad = Squad.getInstance();
            squad.load(input.getSquad());

            //Message Broker initialization
            MessageBrokerImpl messageBroker = MessageBrokerImpl.getInstance();

            //Q initialization
            runnableList.add(new Thread(Q.getInstance()));

            //M initialization
            for(int i=1; i<=input.getServices().getM(); i++){
                runnableList.add(new Thread(new M(i)));
            }

            //MoneyPenny initialization
            for (int i=1; i <= input.getServices().getMoneypenny(); i++){
                runnableList.add(new Thread(new Moneypenny(i)));
            }

            //Intelligence initial
            Intelligence[] intelligencesArr = input.getServices().getIntelligence();
            for (Intelligence x: intelligencesArr) {
                runnableList.add(new Thread(new Intelligence(x.getMissionList())));
            }

            //TimeService initialization
            runnableList.add(new Thread(new TimeService(input.getServices().getTime())));

            synchronized (runnableList){
                for (Thread x:runnableList) {
                   x.start();
                }
            }

            for (Thread x: runnableList) {
                try {
                    x.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        Diary.getInstance().printToFile(args[2]);
        Inventory.getInstance().printToFile(args[1]);

    }
}
