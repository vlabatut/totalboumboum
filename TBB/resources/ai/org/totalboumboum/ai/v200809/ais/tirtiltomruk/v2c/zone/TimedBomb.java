package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	ArtificialIntelligence ai;
	
	public TimedBomb(AiZone zone,AiBomb bomb,long releaseTime, long time, ArtificialIntelligence ai) throws StopRequestException
	{	ai.checkInterruption();
		this.ai = ai;
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
	
	public ZoneEnum getDanger() throws StopRequestException {
		ai.checkInterruption();
		return danger;
	}

	public void setDanger(ZoneEnum danger) throws StopRequestException {
		ai.checkInterruption();
		this.danger = danger;
	}

	public long getRemainingTime() throws StopRequestException
	{	ai.checkInterruption();
	
		if(this.bomb.isWorking())
			return this.bomb.getNormalDuration() - this.time + this.releaseTime;
		else
			return -1;
	}
	
	public boolean equals(Object timedBomb)
	{	try {
		ai.checkInterruption();
	} catch (StopRequestException e) {
		// 
		e.printStackTrace();
	}
	
		if(!TimedBomb.class.isInstance(timedBomb))
			return false;
		else
		{
			boolean condition = false;
			try {
				condition = this.bomb.equals((((TimedBomb)timedBomb).getBomb()));
			} catch (StopRequestException e) {
				// 
				e.printStackTrace();
			}
			try {
				condition = condition && this.zone.equals((((TimedBomb)timedBomb).getZone()));
			} catch (StopRequestException e) {
				// 
				e.printStackTrace();
			}
			if(condition)
				return true;
			else
				return false;
		}
	}

	public AiZone getZone() throws StopRequestException {
		ai.checkInterruption();
		return zone;
	}

	public void setZone(AiZone zone) throws StopRequestException {
		ai.checkInterruption();
		this.zone = zone;
	}

	public AiBomb getBomb() throws StopRequestException {
		ai.checkInterruption();
		return bomb;
	}

	public void setBomb(AiBomb bomb) throws StopRequestException {
		ai.checkInterruption();
		this.bomb = bomb;
	}

	public long getReleaseTime() throws StopRequestException {
		ai.checkInterruption();
		return releaseTime;
	}

	public void setReleaseTime(long releaseTime) throws StopRequestException {
		ai.checkInterruption();
		this.releaseTime = releaseTime;
	}
		
	public String toString()
	{
		String result = "";
		result += "( "+ this.bomb.getCol() + "," + this.bomb.getLine() + ") ";
		try {
			result += "Time Remaining:" + this.getRemainingTime();
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		result += "/ Release Time:" + this.releaseTime;
		return result;
	}

	public void setTime(long time) throws StopRequestException {
		ai.checkInterruption();
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
