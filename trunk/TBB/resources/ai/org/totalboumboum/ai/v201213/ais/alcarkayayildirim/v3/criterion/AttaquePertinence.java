package org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.criterion;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.alcarkayayildirim.v3.AlcarKayaYildirim;

/**
 * The criteria that will evaluate the tile for attack suitability.
 * 
 * @author Ekin Alçar
 * @author Ulaş Kaya
 * @author Yağmur Yıldırım
 */
public class AttaquePertinence extends AiUtilityCriterionBoolean<AlcarKayaYildirim>
{	/** Nom de ce critère */
	public static final String NAME = "ATTAQUE_PERTINENCE";
		
//	/**
//	 * (Range - CLOSING_LIMIT) determines the tile's state.
//	 */
//	public static final int CLOSING_LIMIT = 3;
	/**
	 * Crée un nouveau critère binaire.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AttaquePertinence(AlcarKayaYildirim ai) throws StopRequestException
	{	super(ai,NAME);
		ai.checkInterruption();
		this.ai1 = ai;
	}
	
	
	/** */
	protected AlcarKayaYildirim ai1;
    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	ai1.checkInterruption();
		AiZone zone = ai1.getZone();
		AiHero ownHero=zone.getOwnHero();
		boolean result = false;
	
		List<AiHero> heroes = ai.getZone().getHeroes();
		Iterator<AiHero> itHeroes = heroes.iterator();
		AiHero temp;
		
		while(itHeroes.hasNext() && !result){
			ai.checkInterruption();
			temp = itHeroes.next();
			if(temp != ownHero && (temp.getTile().getCol() == tile.getCol() || temp.getTile().getRow() == tile.getRow()))
				result = true;
		}
//	boolean result = false;
//
//	for ( AiHero currentEnemy : this.ai1.getZone().getRemainingOpponents() )
//	{
//		ai1.checkInterruption();
//
//		if ( this.ai1.getDangerousTilesOnBombPut( tile, ownHero.getBombRange() - CLOSING_LIMIT ).contains( currentEnemy.getTile() ) ) result = true;
//	}
	
		return result;
	}
}
