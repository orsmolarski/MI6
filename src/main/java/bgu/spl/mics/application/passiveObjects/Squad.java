package bgu.spl.mics.application.passiveObjects;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {
    private volatile Map<String, Agent> Agents;

    private static class SingletonHolder {
        private static Squad instance = new Squad();
    }


    /**
     * Retrieves the single instance of this class.
     */
    public static Squad getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * Initializes the squad. This method adds all the agents to the squad.
     * <p>
     *
     * @param agents Data structure containing all data necessary for initialization
     *               of the squad.
     */
    public void load(Agent[] agents) {
        Agents = new HashMap<String, Agent>();
        for (Agent agent : agents) {
            this.Agents.put(agent.getSerialNumber(), agent);
        }
    }

    /**
     * Releases agents.
     */
    public void releaseAgents(List<String> serials) {
        for (String agent : serials) {
            Agents.get(agent).release();
        }
        notifyAll();

    }

    /**
     * simulates executing a mission by calling sleep.
     *
     * @param time time ticks to sleep
     */
    public void sendAgents(List<String> serials, int time) {
        synchronized (Squad.getInstance()) {
            if (this.getAgents(serials)) {
                for (String agentSerial : serials)
                    this.Agents.get(agentSerial).acquire();
            }
            try {
                Thread.sleep(time * 100);
            } catch (InterruptedException e) {
                System.out.println("ERROR sendAgents");
            }
            releaseAgents(serials);
        }
    }

    /**
     * acquires an agent, i.e. holds the agent until the caller is done with it
     *
     * @param serials the serial numbers of the agents
     * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
     */
    public boolean getAgents(List<String> serials) {
        for (String agentSerial : serials) {
            if (!this.Agents.containsKey(agentSerial))
                return false;
        }
        for (String agentSerial : serials) {
            while (!this.Agents.get(agentSerial).isAvailable()) synchronized (this) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.out.println("ERROR getAgents wait");
                }
            }
        }

        return true;
    }

    /**
     * gets the agents names
     *
     * @param serials the serial numbers of the agents
     * @return a list of the names of the agents with the specified serials.
     */
    public List<String> getAgentsNames(List<String> serials) {
        List<String> agentList = new LinkedList<>();
        for (String agent : serials) {

            if (Agents.get(agent) != null) {
                agentList.add(Agents.get(agent).getName());
            }
        }
        return agentList;
    }
}
