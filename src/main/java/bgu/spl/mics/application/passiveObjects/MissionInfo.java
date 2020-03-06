package bgu.spl.mics.application.passiveObjects;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Passive data-object representing information about a mission.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class MissionInfo {
	@SerializedName("name")
	private String name;
	private List<String> serialAgentsNumbers;
	private String gadget;
	private int timeExpired;
	private int timeIssued;
	private int duration;

	public MissionInfo(String name, List<String> serialAgentsNumbers,String gadget,int timeExpired,int timeIssued,int duration){
		this.name = name;
		this.serialAgentsNumbers = serialAgentsNumbers;
		this.gadget = gadget;
		this.timeExpired = timeExpired;
		this.timeIssued = timeIssued;
		this.duration = duration;
	}


    /**
     * Sets the name of the mission.
     */
    public void setMissionName(String missionName) { name = missionName;}

	/**
     * Retrieves the name of the mission.
     */
	public String getMissionName() {return name;}

    /**
     * Sets the serial agent number.
     */
    public void setSerialAgentsNumbers(List<String> serialAgentsNumbers) {
		this.serialAgentsNumbers = serialAgentsNumbers;
    }

	/**
     * Retrieves the serial agent number.
     */
	public List<String> getSerialAgentsNumbers() { return serialAgentsNumbers;}

    /**
     * Sets the gadget name.
     */
    public void setGadget(String gadget) {
		this.gadget = gadget;
    }

	/**
     * Retrieves the gadget name.
     */
	public String getGadget() {return gadget;}

    /**
     * Sets the time the mission was issued in time ticks.
     */
    public void setTimeIssued(int timeIssued) {
		this.timeIssued = timeIssued;
    }

	/**
     * Retrieves the time the mission was issued in time ticks.
     */
	public int getTimeIssued() {
		return timeIssued;
	}

    /**
     * Sets the time that if it that time passed the mission should be aborted.
     */
    public void setTimeExpired(int timeExpired) {
		this.timeExpired = timeExpired;
    }

	/**
     * Retrieves the time that if it that time passed the mission should be aborted.
     */
	public int getTimeExpired() {
		return timeExpired;
	}

    /**
     * Sets the duration of the mission in time-ticks.
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

	/**
	 * Retrieves the duration of the mission in time-ticks.
	 */
	public int getDuration() {
		return duration;
	}


}
