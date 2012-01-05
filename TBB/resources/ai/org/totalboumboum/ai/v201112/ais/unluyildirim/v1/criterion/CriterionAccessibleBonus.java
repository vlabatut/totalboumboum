package org.totalboumboum.ai.v201112.ais.unluyildirim.v1.criterion;

import org.totalboumboum.ai.v201112.adapter.agent.AiUtilityCriterionBoolean;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.ais.unluyildirim.v1.UnluYildirim;

/**
 * Cette classe représente est un simple exemple de 
 * critère binaire. Copiez-la, renommez-la, modifiez-la
 * pour l'adapter à vos besoin.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class CriterionAccessibleBonus extends AiUtilityCriterionBoolean
{	/** Nom de ce critère */
	public static final String NAME = "ACCESSIBLEBONUS";
	
	/**
	 * Ce critere regarde si le tile est accessible directement.
	 * Ça veut dire que l'on n'a pas besoin de détruire des murs 
	 * pour l'acceder.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	public CriterionAccessibleBonus(UnluYildirim ai) throws StopRequestException
	{	// init nom
		super(NAME);
		
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
	@SuppressWarnings("null")
	@Override
	public Boolean processValue(AiTile tile) throws StopRequestException
	{	boolean result = true;
	
		AiZone zone=ai.getZone(); //prend la zone 
		AiHero ownHero=zone.getOwnHero(); //prend notre Hero 
		Astar astar = null ; 
	    AiLocation startLocation = new AiLocation(ownHero.getTile()); //prend la place de notre hero comme le debut
	    AiPath path = new AiPath(); // nouveau chemin
	  
		
	    try {
			path=astar.processShortestPath(startLocation, tile); //path contient une chemin de debut au tile defini
		} catch (LimitReachedException e) {
		}
	    if(path.isEmpty()) // si le path est vide , ça veut dire le tile n'est pas accessible.
	    {
	    	result = false ; //il retourne false si le tile n'est pas accessible.
	    }
		
		
	
		return result;
	}
}
