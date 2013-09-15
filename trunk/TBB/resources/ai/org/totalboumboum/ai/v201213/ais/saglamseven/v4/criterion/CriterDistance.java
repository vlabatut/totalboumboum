package org.totalboumboum.ai.v201213.ais.saglamseven.v4.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.ais.saglamseven.v4.SaglamSeven;

/**
 * Cette classe est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
@SuppressWarnings("deprecation")
public class CriterDistance extends AiUtilityCriterionInteger<SaglamSeven>
{	/** Nom de ce critère */
	public static final String NAME = "DISTANCE";
	
	/**
	 * 
	 */
	public static final int DISTANCE_LIMIT = 2;
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterDistance(SaglamSeven ai) throws StopRequestException
	{	super(ai,NAME,0,2);
		ai.checkInterruption();
	}
	
	/**
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
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
    
	ai.checkInterruption();
	AiZone gameArea = ai.getZone();
	List<AiHero> heros = new ArrayList<AiHero>(gameArea.getHeroes());
	int distance = DISTANCE_LIMIT;
	heros.remove(gameArea.getOwnHero());
	if(heros!= null){
		for(int i = 0 ; i< heros.size();i++){
			ai.checkInterruption();
			int tileDis = getDistance(tile, heros.get(i).getTile());
			
			if( tileDis<4)
				distance=0;
			else if(tileDis<8)
				distance=1;
			else 
				distance=2;
			
	
	
}}
	
	return distance;}}
	

