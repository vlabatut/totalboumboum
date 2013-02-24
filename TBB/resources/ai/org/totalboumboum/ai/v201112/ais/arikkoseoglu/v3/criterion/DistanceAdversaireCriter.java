package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.arikkoseoglu.v3.ArikKoseoglu;

/**
 * Cette classe est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
@SuppressWarnings("deprecation")
public class DistanceAdversaireCriter extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE_ADVERSAIRE";
	/** */
	public static final int DISTANCE_LIMIT = 15;
	/**
	 * Crée un nouveau critère binaire.
	 * @param ai 
	 * 		?
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public DistanceAdversaireCriter(ArikKoseoglu ai) throws StopRequestException
	{	// init nom
		super(NAME,0,DISTANCE_LIMIT);
		ai.checkInterruption();
		// init agent
		this.ai = ai;
	}
	
	/**
	 * 
	 * @param t1
	 * 		description manquante !
	 * @param t2
	 * 		description manquante !
	 * @return 
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	protected int getDistance(AiTile t1,AiTile t2) throws StopRequestException
	{	ai.checkInterruption();
		int distance = Math.abs(t1.getCol() - t2.getCol())+ Math.abs(t1.getRow() - t2.getRow()) ;
	
		return distance;
	}
	
    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** */
	protected ArikKoseoglu ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	ai.checkInterruption();
		AiZone gameArea = ai.getZone();
		List<AiHero> heros = gameArea.getHeroes();
		int distance = DISTANCE_LIMIT;
		heros.remove(gameArea.getOwnHero());
		if(heros!= null){
			for(int i = 0 ; i< heros.size();i++){
				ai.checkInterruption();
				int tileDis = getDistance(tile, heros.get(i).getTile());
				
				if( tileDis< distance){
					distance = tileDis;
				}
			}
		}
		return distance;
	}
}
