package bgu.spl.mics.application.passiveObjects;

import java.util.List;

public class ResultMoneyPenny {
    private int uniqueNUm;
    private List<String> agentNames;

    public ResultMoneyPenny(int num, List<String> list)
    {
        this.uniqueNUm=num;
        this.agentNames=list;
    }

    public int getUniqueNUm() {
        return uniqueNUm;
    }

    public List<String> getAgentNames() {
        return agentNames;
    }
}
