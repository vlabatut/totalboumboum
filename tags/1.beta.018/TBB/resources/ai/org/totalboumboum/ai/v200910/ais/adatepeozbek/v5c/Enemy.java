package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5c;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;

/**
 * 
 * @version 5.c
 * 
 * @author Can Adatape
 * @author Sena Ozbek
 *
 */
public class Enemy
{
	private AiHero _hero = null;
	private EnemyTypes _type = null;
	private int _posedBombs = 0;
	private int _sameTileCount = 0;
	private long _lastCheck = 0;
	ArtificialIntelligence ownAi;
	
	public Enemy(AiHero hero, EnemyTypes type,ArtificialIntelligence ownAi) throws StopRequestException
	{	ownAi.checkInterruption();
		this.ownAi = ownAi;
		_hero = hero;
		_type = type;
	}
	
	public AiHero getHero() throws StopRequestException
	{	ownAi.checkInterruption();
		return _hero;
	}
	
	public EnemyTypes getType() throws StopRequestException
	{	ownAi.checkInterruption();
		return _type;		
	}
	
	public void updateHero(AiHero hero) throws StopRequestException
	{	ownAi.checkInterruption();
		_hero = hero;
	}
	
	public int getPosedBombs() throws StopRequestException
	{	ownAi.checkInterruption();
		return _posedBombs;		
	}
	
	public void setType(EnemyTypes type) throws StopRequestException
	{	ownAi.checkInterruption();
		_type = type;		
	}
	
	public int getSameTileCount() throws StopRequestException
	{	ownAi.checkInterruption();
		return _sameTileCount;		
	}
	
	public void increasePosedBombs(int ct) throws StopRequestException
	{	ownAi.checkInterruption();
		_posedBombs += ct;		
	}
	
	public void increaseSameTileCount() throws StopRequestException
	{	ownAi.checkInterruption();
		_sameTileCount += 1;		
	}
	
	public void setLastCheck(long time) throws StopRequestException
	{	ownAi.checkInterruption();
		_lastCheck = time;
	}
	
	public long getLastCheck() throws StopRequestException
	{	ownAi.checkInterruption();
		return _lastCheck;
	}
	
	public String toString()
	{
		StringBuffer result = new StringBuffer();
		try {
			result.append(" - sameTile.: "+ getSameTileCount());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		try {
			result.append(" - posedBombs.: "+ getPosedBombs());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		try {
			result.append(" - totalTime.: "+ getLastCheck());
		} catch (StopRequestException e) {
			// 
			e.printStackTrace();
		}
		return result.toString();
	}
}