package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2.zone;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiZone;

/**
*
* @author Abdullah Tırtıl
* @author Mert Tomruk
*
*/
@SuppressWarnings("deprecation")
public class TimedBomb {
	
	/** */
	private AiZone zone;
	/** */
	private AiBomb bomb;
	/** */
	private long releaseTime;
	/** */
	private long time;
	/** */
	private ZoneEnum danger;
	
	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 * @param bomb
	 * 		Description manquante !
	 * @param releaseTime
	 * 		Description manquante !
	 * @param time
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public ZoneEnum getDanger() {
		return danger;
	}
	/**
	 * 
	 * @param danger
	 * 		Description manquante !
	 */
	public void setDanger(ZoneEnum danger) {
		this.danger = danger;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public long getRemainingTime()
	{
		if(this.bomb.isWorking())
			return this.bomb.getNormalDuration() - this.time + this.releaseTime;
		else
			return -1;
	}
	
	@Override
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

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public AiZone getZone() {
		return zone;
	}
	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 */
	public void setZone(AiZone zone) {
		this.zone = zone;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public AiBomb getBomb() {
		return bomb;
	}
	/**
	 * 
	 * @param bomb
	 * 		Description manquante !
	 */
	public void setBomb(AiBomb bomb) {
		this.bomb = bomb;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public long getReleaseTime() {
		return releaseTime;
	}
	/**
	 * 
	 * @param releaseTime
	 * 		Description manquante !
	 */
	public void setReleaseTime(long releaseTime) {
		this.releaseTime = releaseTime;
	}
		
	@Override
	public String toString()
	{
		String result = "";
		result += "( "+ this.bomb.getCol() + "," + this.bomb.getLine() + ") ";
		result += "Time Remaining:" + this.getRemainingTime();
		result += "/ Release Time:" + this.releaseTime;
		return result;
	}
	
	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 */
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
