package bgu.spl.mics.application.passiveObjects;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

/**
 * That's where Q holds his gadget (e.g. an explosive pen was used in GoldenEye, a geiger counter in Dr. No, etc).
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add ONLY private fields and methods to this class as you see fit.
 */
public class Inventory {
    private List<String> gadgets;

    /**
     * Retrieves the single instance of this class.
     */

    private static class SingletonHolder {
        private static Inventory instance = new Inventory();
    }

    public static Inventory getInstance() {
        return Inventory.SingletonHolder.instance;
    }


    //method added for debugging
    public boolean isEmpty(){ return gadgets.isEmpty();}

    /**
     * Initializes the inventory. This method adds all the items given to the gadget
     * inventory.
     * <p>
     *
     * @param inventory Data structure containing all data necessary for initialization
     *                  of the inventory.
     */
    public void load(String[] inventory) {
        gadgets = new LinkedList<String>();
        for (String gadget : inventory)
            gadgets.add(gadget);

    }

    /**
     * acquires a gadget and returns 'true' if it exists.
     * <p>
     *
     * @param gadget Name of the gadget to check if available
     * @return ‘false’ if the gadget is missing, and ‘true’ otherwise
     */
    public boolean getItem(String gadget) {
        return gadgets.contains(gadget);
    }

    /**
     * <p>
     * Prints to a file name @filename a serialized object List<String> which is a
     * list of all the of the gadgeds.
     * This method is called by the main method in order to generate the output.
     */
    public void printToFile(String filename) {
        try { Writer writer = new FileWriter(filename);
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                gson.toJson(gadgets, writer);
                writer.flush();

        } catch (IOException IOE) {
            System.out.println("Error printing Inventory to JSON");
        }

    }

    public void remove(String toRemove) {
        gadgets.remove(toRemove);
    }
}
