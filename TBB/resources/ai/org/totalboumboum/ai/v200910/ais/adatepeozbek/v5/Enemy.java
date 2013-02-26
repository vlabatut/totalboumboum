package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5;

import org.totalboumboum.ai.v200910.adapter.data.AiHero;

/**
 * 
 * @version 5
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
	
	/**
	 * 
	 * @param hero
	 * 		Description manquante !
	 * @param type
	 * 		Description manquante !
	 */
	public Enemy(AiHero hero, EnemyTypes type)
	{
		_hero = hero;
		_type = type;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public AiHero getHero()
	{
		return _hero;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public EnemyTypes getType()
	{
		return _type;		
	}
	
	/**
	 * 
	 * @param hero
	 * 		Description manquante !
	 */
	public void updateHero(AiHero hero)
	{
		_hero = hero;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getPosedBombs()
	{
		return _posedBombs;		
	}
	
	/**
	 * 
	 * @param type
	 * 		Description manquante !
	 */
	public void setType(EnemyTypes type)
	{
		_type = type;		
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public int getSameTileCount()
	{
		return _sameTileCount;		
	}
	
	/**
	 * 
	 * @param ct
	 * 		Description manquante !
	 */
	public void increasePosedBombs(int ct)
	{
		_posedBombs += ct;		
	}
	
	/**
	 * 
	 */
	public void increaseSameTileCount()
	{
		_sameTileCount += 1;		
	}
	
	/**
	 * 
	 * @param time
	 * 		Description manquante !
	 */
	public void setLastCheck(long time)
	{
		_lastCheck = time;
	}
	
	/**
	 * 
	 * @return
	 * 		Description manquante !
	 */
	public long getLastCheck()
	{
		return _lastCheck;
	}
	
	@Override
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		result.append(" - sameTile.: "+ getSameTileCount());
		result.append(" - posedBombs.: "+ getPosedBombs());
		result.append(" - totalTime.: "+ getLastCheck());
		return result.toString();
	}
}