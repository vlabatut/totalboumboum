package org.totalboumboum.ai.v200809.ais.tirtiltomruk.v2c.zone;

import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;

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
	/** */
	ArtificialIntelligence ai;
	
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
	 * @param ai
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public ZoneEnum getDanger() throws StopRequestException {
		ai.checkInterruption();
		return danger;
	}

	/**
	 * 
	 * @param danger
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setDanger(ZoneEnum danger) throws StopRequestException {
		ai.checkInterruption();
		this.danger = danger;
	}

	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public long getRemainingTime() throws StopRequestException
	{	ai.checkInterruption();
	
		if(this.bomb.isWorking())
			return this.bomb.getNormalDuration() - this.time + this.releaseTime;
		else
			return -1;
	}
	
	@Override
	public boolean equals(Object timedBomb)
	{	try {
		ai.checkInterruption();
	} catch (StopRequestException e) {
		// 
		//e.printStackTrace();
		throw new RuntimeException();
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
				//e.printStackTrace();
				throw new RuntimeException();
			}
			try {
				condition = condition && this.zone.equals((((TimedBomb)timedBomb).getZone()));
			} catch (StopRequestException e) {
				// 
				//e.printStackTrace();
				throw new RuntimeException();
			}
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
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiZone getZone() throws StopRequestException {
		ai.checkInterruption();
		return zone;
	}

	/**
	 * 
	 * @param zone
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setZone(AiZone zone) throws StopRequestException {
		ai.checkInterruption();
		this.zone = zone;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiBomb getBomb() throws StopRequestException {
		ai.checkInterruption();
		return bomb;
	}
	/**
	 * 
	 * @param bomb
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setBomb(AiBomb bomb) throws StopRequestException {
		ai.checkInterruption();
		this.bomb = bomb;
	}
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public long getReleaseTime() throws StopRequestException {
		ai.checkInterruption();
		return releaseTime;
	}
	/**
	 * 
	 * @param releaseTime
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setReleaseTime(long releaseTime) throws StopRequestException {
		ai.checkInterruption();
		this.releaseTime = releaseTime;
	}
		
	@Override
	public String toString()
	{
		String result = "";
		result += "( "+ this.bomb.getCol() + "," + this.bomb.getLine() + ") ";
		try {
			result += "Time Remaining:" + this.getRemainingTime();
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		result += "/ Release Time:" + this.releaseTime;
		return result;
	}

	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
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
