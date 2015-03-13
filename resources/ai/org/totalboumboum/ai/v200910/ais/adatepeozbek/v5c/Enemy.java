package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;

/**
 * 
 * @version 5.c
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
public class Enemy
{
	/** */
	private AiHero _hero = null;
	/** */
	private EnemyTypes _type = null;
	/** */
	private int _posedBombs = 0;
	/** */
	private int _sameTileCount = 0;
	/** */
	private long _lastCheck = 0;
	/** */
	ArtificialIntelligence ownAi;
	
	/**
	 * 
	 * @param hero
	 * 		Description manquante !
	 * @param type
	 * 		Description manquante !
	 * @param ownAi
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public Enemy(AiHero hero, EnemyTypes type,ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		this.ownAi = ownAi;
		_hero = hero;
		_type = type;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiHero getHero() throws StopRequestException
	{	ownAi.checkInterruption();
		return _hero;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public EnemyTypes getType() throws StopRequestException
	{	ownAi.checkInterruption();
		return _type;		
	}
	
	/**
	 * 
	 * @param hero
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void updateHero(AiHero hero) throws StopRequestException
	{	ownAi.checkInterruption();
		_hero = hero;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public int getPosedBombs() throws StopRequestException
	{	ownAi.checkInterruption();
		return _posedBombs;		
	}
	
	/**
	 * 
	 * @param type
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setType(EnemyTypes type) throws StopRequestException
	{	ownAi.checkInterruption();
		_type = type;		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public int getSameTileCount() throws StopRequestException
	{	ownAi.checkInterruption();
		return _sameTileCount;		
	}
	
	/**
	 * 
	 * @param ct
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void increasePosedBombs(int ct) throws StopRequestException
	{	ownAi.checkInterruption();
		_posedBombs += ct;		
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void increaseSameTileCount() throws StopRequestException
	{	ownAi.checkInterruption();
		_sameTileCount += 1;		
	}
	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setLastCheck(long time) throws StopRequestException
	{	ownAi.checkInterruption();
		_lastCheck = time;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public long getLastCheck() throws StopRequestException
	{	ownAi.checkInterruption();
		return _lastCheck;
	}
	
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		try {
			result.append(" - sameTile.: "+ getSameTileCount());
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		try {
			result.append(" - posedBombs.: "+ getPosedBombs());
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		try {
			result.append(" - totalTime.: "+ getLastCheck());
		} catch (StopRequestException e) {
			// 
			//e.printStackTrace();
			throw new RuntimeException();
		}
		return result.toString();
	}
}