package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionInteger;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * Cette classe représente est un simple exemple de 
 * critère entier. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin. Notez que les bornes
 * du domaine de définition sont spécifiées dans
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class CriterionTemps extends AiUtilityCriterionInteger
{	/** Nom de ce critère */
	public static final String NAME = "SECOND";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionTemps(UnluYildirim ai) throws StopRequestException
	{	// init nom + bornes du domaine de définition
		super(NAME,1,3);
		
		// init agent
		this.ai = ai;
	}

    /////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE	/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected UnluYildirim ai;

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public Integer processValue(AiTile tile) throws StopRequestException
	{	
		int result = 0;
	
    AiZone zone = ai.getZone(); ;
	List<AiHero> heros ;
	List<AiHero> heros_tile; 
	
	AiHero ownHero = zone.getOwnHero();
	AiTile tile_hero =  ownHero.getTile();
	
	heros_tile=tile.getHeroes(); //les heros qui se trouve dans le tile spécifié
	heros=zone.getRemainingOpponents(); // tous les heros dans la zone
	
	int i=0,index=0;
	int distance =0;
	float temps=0 ;
	float temps_proche=0;
	for(i=0;i<heros.size();i++) 
	{
		
		distance= zone.getTileDistance(tile_hero, tile);
		temps=(float) (distance/(ownHero.getWalkingSpeed()));
		if (temps < temps_proche)
		{
			temps_proche=temps;
			index=i;
		}
		
	}
	if(heros_tile.contains(heros.get(index))) // regarde si le tile contient l'adversaire plus proche dans la zone
	{
		result=1;
	}
	
	
	return result;
}
}
