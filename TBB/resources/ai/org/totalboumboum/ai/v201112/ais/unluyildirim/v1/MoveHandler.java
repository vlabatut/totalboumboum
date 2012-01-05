package org.totalboumboum.ai.v201112.ais.unluyildirim.v1;

/*
 * La fonction que  j'ai écrit dans ce class nous aide de s'enfuire par les bombes et leurs feux.
 * On ne peut pas completer tous les fonction ,donc notre version 1 ne marche pas exactement comme on veut.
 * 
 * 
 */
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Merve Ünlü
 * @author Gülay Yıldırım
 */
public class MoveHandler extends AiMoveHandler<UnluYildirim>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	protected MoveHandler(UnluYildirim ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "unused", "static-access", "null" })
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
	    
	    AiZone zone;
	    zone=ai.getZone();
	    AiHero ownHero; 
	    ownHero=zone.getOwnHero();
	    AiTile tile_hero;
	    List<AiTile> tile_next ;
	    List<AiBomb> zone_bomb = zone.getBombs();
	    Direction direction_next = null;
	    int i,col,row;
	    
	  
	    
	    col=ownHero.getCol();
	    row=ownHero.getRow();
	    
	    AiMode mode ;
	    AiModeHandler<UnluYildirim> mode_handler = ai.getModeHandler();
	    AiBombHandler<UnluYildirim> bomb_handler=ai.getBombHandler();
	    
	    tile_hero=zone.getTile(row, col);
	    
		List<AiTile> tile_blast=null;
	    tile_next=tile_hero.getNeighbors();
	    int j;
		for(i=0;i<zone_bomb.size();i++)
	    {
	    	tile_blast=zone_bomb.get(i).getBlast();
	        if(tile_blast.contains(tile_hero))
	        {
	        	AiTile tile_bomb=zone_bomb.get(i).getTile();
	        	AiTile tile_voisin1,tile_voisin2;
	        	if(tile_hero.getCol()== zone_bomb.get(i).getCol())
	        	{
	        		
					direction_next=zone.getDirection(tile_hero,tile_bomb);
	        		tile_voisin1=tile_hero.getNeighbor(Direction.LEFT);
        			tile_voisin2=tile_hero.getNeighbor(Direction.RIGHT);
        			if(tile_voisin1.isCrossableBy(ownHero))
	        		   return Direction.LEFT;
        			else {
        				if(tile_voisin2.isCrossableBy(ownHero))
        					{return Direction.RIGHT ; }
        				else 
        				{
        					return direction_next.getOpposite();
        				}
        					
        			}
	        	}
	        	else {
	        		if(tile_hero.getRow()== zone_bomb.get(i).getRow())
	        		{
	        			direction_next=zone.getDirection(tile_hero,tile_bomb);
		        		tile_voisin1=tile_hero.getNeighbor(Direction.UP);
	        			tile_voisin2=tile_hero.getNeighbor(Direction.DOWN);
	        			if(tile_voisin1.isCrossableBy(ownHero))
		        		   return Direction.UP;
	        			else {
	        				if(tile_voisin2.isCrossableBy(ownHero))
	        					{return Direction.DOWN; }
	        				else 
	        				{
	        					return direction_next.getOpposite();
	        				}
	        					
	        			}
	        		}
	        	}
	    	  
	    
	    	}
	        else {
	        	
	        	if(mode_handler.getMode()==AiMode.COLLECTING)
	        	{
	        	
	        		
	        		return direction_next.UP;
	        	}
	        	else {
	        		return direction_next.DOWN;
	        	}
	        }
	        
	    }
		
	    
	   /* AiTile tile ;
	    List<AiBomb> tile_bomb;
	    
	    tile=zone.getTile(row, j);
	    tile_bomb=tile.getBombs();
	    
	    for(i=0;i<tile_bomb.size();i++)
	   {
		   if(!(tile_bomb.contains(tile_next)))
	   }*/
	    
	    

	    return Direction.NONE;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
