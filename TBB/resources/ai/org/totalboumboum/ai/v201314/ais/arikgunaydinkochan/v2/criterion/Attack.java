package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.criterion;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.Agent;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Cette classe est critère attack.
 * l'appel au constructeur ({@code super(nom,inf,sup)}).
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Attack extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Attack";
	
	/** La liste d'accessible tile*/
	public ArrayList<AiTile> accessible;
	/** La liste de control tile*/
	public ArrayList<AiTile> control;
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Attack(Agent ai)
	{	super(ai,NAME,0,1);
		ai.checkInterruption();
	}

    /////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Integer processValue(AiTile tile)
	{	
	ai.checkInterruption();
	
	this.accessible = new ArrayList<AiTile>();
	this.control = new ArrayList<AiTile>();
	
	ArrayList<AiTile> neighbors = new ArrayList<AiTile>();
	
	AiZone zone = ai.getZone();
	AiHero myhero = zone.getOwnHero();
	
	List<AiBomb> bombs = new ArrayList<AiBomb>();	  
	List<AiTile> blasttiles = new ArrayList<AiTile>();
	
	int zonesecurite=0;
	
	int danger=0;
	
	bombs = zone.getBombs();
	
	for( AiBomb item : bombs  ){
		ai.checkInterruption();
		blasttiles.addAll(item.getBlast()) ;	
	}
	
	for(AiTile item: blasttiles){
		ai.checkInterruption();
		if(item==tile)
			danger=1;
	}
	if(danger==1){
		return 1;
	}
	else {
		danger=0;
		
		neighbors.addAll(tile.getNeighbors());
		
		for(AiTile item:neighbors){
			ai.checkInterruption();
			if(item.isCrossableBy(myhero)){
				
				accessible.add(item);
				control.add(item); 
				
				//
				for(int i=1;i<=2;i++){
					ai.checkInterruption();
					
					for(AiTile item2 : control){
						ai.checkInterruption();					
					//	
						if(item2.getNeighbor(Direction.RIGHT).isCrossableBy(zone.getOwnHero())){
							//
							if(i==2){
										for(AiTile item3: blasttiles){
											ai.checkInterruption();
											if(item3==item2.getNeighbor(Direction.RIGHT))
												danger=1;
										}
										if(danger!=1)
											accessible.add(item2.getNeighbor(Direction.RIGHT));
							}
							//
							else	
									accessible.add(item2.getNeighbor(Direction.RIGHT));
							danger=0;
						}
					//
						if(item2.getNeighbor(Direction.LEFT).isCrossableBy(zone.getOwnHero())){
							//
							if(i==2){
								for(AiTile item3: blasttiles){
									ai.checkInterruption();
									if(item3==item2.getNeighbor(Direction.LEFT))
										danger=1;
								}
								if(danger!=1)
									accessible.add(item2.getNeighbor(Direction.LEFT));
							}
							//
							else	
								accessible.add(item2.getNeighbor(Direction.LEFT));
							danger=0;
						}
					//	
						if(item2.getNeighbor(Direction.UP).isCrossableBy(zone.getOwnHero())){
							//
							if(i==2){
								for(AiTile item3: blasttiles){
									ai.checkInterruption();
									if(item3==item2.getNeighbor(Direction.UP))
										danger=1;
								}
								if(danger!=1)
									accessible.add(item2.getNeighbor(Direction.UP));
							}
							//
							else	
								accessible.add(item2.getNeighbor(Direction.UP));
							danger=0;
						}
					//
						if(item2.getNeighbor(Direction.DOWN).isCrossableBy(zone.getOwnHero())){
							//
							if(i==2){
								for(AiTile item3: blasttiles){
									ai.checkInterruption();
									if(item3==item2.getNeighbor(Direction.DOWN))
										danger=1;
								}
								if(danger!=1)
									accessible.add(item2.getNeighbor(Direction.DOWN));	
							}
							//
							else	
								accessible.add(item2.getNeighbor(Direction.DOWN));
							
							danger=0;
						}
						
					}	
					control.addAll(accessible);
				}
				
				for(AiTile accestile : accessible){
					ai.checkInterruption();
					if(ai.getZone().getTileDistance(accestile,item)==2)
						if(accestile.getCol()!=item.getCol()&&accestile.getRow()!=item.getRow())
						zonesecurite=1;
				}
				
			if(zonesecurite==1)
				return 0;
			else
				return 1;
				
			}	
		
		}
			
	}
	

	return 0;
	}
	
	
}
