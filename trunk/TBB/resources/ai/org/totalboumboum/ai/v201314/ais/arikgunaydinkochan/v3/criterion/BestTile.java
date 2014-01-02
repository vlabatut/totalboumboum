package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionBoolean;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3.Agent;

/**
 * Cette classe est distance critère 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class BestTile extends AiCriterionBoolean<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "BestTile";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public BestTile(Agent ai)
	{	super(ai,NAME);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Boolean processValue(AiTile tile)
	{	
		ai.checkInterruption();	
		
		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile mytile = myHero.getTile();
		
		ArrayList<AiTile> accessibleTiles=ai.accessibleTiles;
	
		//variables
		
		AiHero accessibleHero =null;
		int accessibleDistance =100;
		
		AiHero nonAccessibleHero =null;
		int nonAccessibleDistance =100;
		
		AiTile bestTile=null;

		int blockDistance =100;
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();	
		heroes.addAll(zone.getRemainingOpponents());
		
		if(!heroes.isEmpty()){
		//		
				for(AiHero hero : heroes){
						ai.checkInterruption();
						AiTile heroTile = hero.getTile();
						int d=zone.getTileDistance(mytile,heroTile);
				//
						if(accessibleTiles.contains(heroTile)){
									
								if(accessibleDistance>=d){
											accessibleHero=hero;
											accessibleDistance=d;
								}
						}		
						else if(accessibleDistance==100){
								
								if(nonAccessibleDistance>=d){
											nonAccessibleHero=hero;
											nonAccessibleDistance=d;
								}
						}
				}
		//
				if(accessibleDistance<100){
						bestTile=accessibleHero.getTile();
						if(bestTile.getNeighbors().contains(tile))
								return true;
						else
								return false;
				}
				else{
						ArrayList<AiBlock> blocks = new ArrayList<AiBlock>(); 
				//
						for(AiTile t : accessibleTiles){
								ai.checkInterruption();
								 
								for(AiTile neigbour : t.getNeighbors()){
									ai.checkInterruption();
										for(AiBlock b : neigbour.getBlocks()){
												
											ai.checkInterruption();
											blocks.add(b);									
										}
								}		
						}
				//
						for(AiBlock block : blocks){
							ai.checkInterruption();
								AiTile blockTile =block.getTile();
								int d=zone.getTileDistance(blockTile, nonAccessibleHero.getTile());
								if(block.isDestructible())
										if(d<blockDistance){
												blockDistance=d;
												bestTile=blockTile;
										}
						}
						//
						if(tile.getNeighbors().contains(bestTile))
								return true;
						else 
								return false;
				}
		
		}		
		else
				return false;
			
	}
}
