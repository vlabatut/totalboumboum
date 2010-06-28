package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2.zone;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiZone;

/**
*
* @author Abdullah Tirtil
* @author Mert Tomruk
*
*/
public class TimedBomb {
	
	private AiZone zone;
	private AiBomb bomb;
	private long releaseTime;
	private long time;
	private ZoneEnum danger;
	
	public TimedBomb(AiZone zone,AiBomb bomb,long releaseTime, long time)
	{
		this.zone = zone;
		this.bomb = bomb;
		this.releaseTime = releaseTime;
		this.time = time;
		
		this.danger = ZoneEnum.FEUPOSSIBLE_PASDANGEREUX;
		if(getRemainingTime() < 2000)
			this.danger = ZoneEnum.FEUPOSSIBLE_PEUDANGEREUX;
		if(getRemainingTime() < 1500)
			this.danger = ZoneEnum.FEUPOSSIBLE_DANGEREUX;
		if(getRemainingTime() < 500)
			this.danger = ZoneEnum.FEUPOSSIBLE_TRESDANGEREUX;		
	}
	
	public ZoneEnum getDanger() {
		return danger;
	}

	public void setDanger(ZoneEnum danger) {
		this.danger = danger;
	}

	public long getRemainingTime()
	{
		if(this.bomb.isWorking())
			return this.bomb.getNormalDuration() - this.time + this.releaseTime;
		else
			return -1;
	}
	
	public boolean equals(Object timedBomb)
	{
		if(!TimedBomb.class.isInstance(timedBomb))
			return false;
		else
		{
			boolean condition = this.bomb.equals((((TimedBomb)timedBomb).getBomb()));
			condition = condition && this.zone.equals((((TimedBomb)timedBomb).getZone()));
			if(condition)
				return true;
			else
				return false;
		}
	}

	public AiZone getZone() {
		return zone;
	}

	public void setZone(AiZone zone) {
		this.zone = zone;
	}

	public AiBomb getBomb() {
		return bomb;
	}

	public void setBomb(AiBomb bomb) {
		this.bomb = bomb;
	}

	public long getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}
		
	public String toString()
	{
		String result = "";
		result += "( "+ this.bomb.getCol() + "," + this.bomb.getLine() + ") ";
		result += "Time Remaining:" + this.getRemainingTime();
		result += "/ Release Time:" + this.releaseTime;
		return result;
	}

	public void setTime(long time) {
		this.time = time;
		this.danger = ZoneEnum.FEUPOSSIBLE_PASDANGEREUX;
		if(getRemainingTime() < 2000)
			this.danger = ZoneEnum.FEUPOSSIBLE_PEUDANGEREUX;
		if(getRemainingTime() < 1500)
			this.danger = ZoneEnum.FEUPOSSIBLE_DANGEREUX;
		if(getRemainingTime() < 500)
			this.danger = ZoneEnum.FEUPOSSIBLE_TRESDANGEREUX;
	}
	
	
}
