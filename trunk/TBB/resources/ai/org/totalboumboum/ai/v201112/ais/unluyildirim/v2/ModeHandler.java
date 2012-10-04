package org.totalboumboum.ai.v201112.ais.unluyildirim.v2;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiItem;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;

/**.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
@SuppressWarnings("deprecation")
public class ModeHandler extends AiModeHandler<UnluYildirim>
{	
	
	AiZone zone = null;
	AiHero myhero = null ;
	/** */
	public static int NUMBERRANGE =3 ;
	/** */
	public static int NUMBERBOMB =3;
	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ModeHandler(UnluYildirim ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		
		verbose = false ;
		
	}

    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	ai.checkInterruption();
		
	
	int hero_bomb;
	int hero_range;
	
	zone = ai.getZone();//prend la zone current
	myhero = zone.getOwnHero(); // notre hero 
	hero_bomb=myhero.getBombNumberMax(); // le nombre des bombes de notre hero
	hero_range=myhero.getBombRange(); // La portée de chaque bombe que l'agent peux posser. 
	if(hero_range>=NUMBERRANGE && hero_bomb>=NUMBERBOMB) //si la portée et le nombre de bombe sont plus petit que 3 , l'agent a assez items
		return true;
		
		return false;
	}
	
	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	ai.checkInterruption();
	
	List<AiItem> zone_item;
	int zone_cache;
	
	zone=ai.getZone();
    zone_cache=zone.getHiddenItemsCount();// le nombre des items caches dans la zone 
    zone_item=zone.getItems(); // tous les items dans la zone 
   
     
    if(zone_cache>0 || zone_item.size()>0) // s'il n'y a pas des items caches et des items ouvets dans la zone ,l'agent ne peux pas ramasser 
    	return true; 
    
	return false;


	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	protected void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		//  à compléter, si vous voulez afficher quelque chose
	}
}
