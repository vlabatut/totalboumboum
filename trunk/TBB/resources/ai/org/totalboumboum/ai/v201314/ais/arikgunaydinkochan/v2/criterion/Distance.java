package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.criterion;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiCriterionInteger;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v2.Agent;

/**
 * Cette classe est distance critère 
 * 
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Distance extends AiCriterionInteger<Agent>
{	/** Nom de ce critère */
	public static final String NAME = "Distance";
	
	/**
	 * Crée un nouveau critère entier.
	 * 
	 * @param ai
	 * 		l'agent concerné. 
	 */
	public Distance(Agent ai)
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
		
		AiZone zone = ai.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile mytile = myHero.getTile();
		
		ArrayList<AiTile> accessibleTiles=ai.accessibleTiles;
	
	
		
		//int heroesNumber=0;
		int[][] distancetoheroes = new int[8][2];
		int smallBig =100;
		int i;
		int heroesAccessibleNumber=0;	// erişilebilen hero sayısı
		int heroesNonAccessibleNumber=0;// erişilemeyen hero sayısı
		for(i=0;i<8;i++){
			ai.checkInterruption();
			for(int j=0;j<2;j++){
				ai.checkInterruption();
				distancetoheroes[i][j]=10;
			}
		}
		
		
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();				 // tüm heroların listesi
		ArrayList<AiHero> heroesAccessible = new ArrayList<AiHero>();	 // erişilebilen heroların listesi
		ArrayList<AiHero> heroesNonAccessible = new ArrayList<AiHero>(); // erişilemeyen heroların sayısı
		
		AiTile heroNonAccessibleTile=null;
		AiTile heroAccessibleTile=null;
		AiTile destinationTile=null;

		heroes.addAll(ai.getZone().getRemainingOpponents());
		
		if(!heroes.isEmpty()){
			for(AiHero hero : heroes){
				ai.checkInterruption();
				AiTile heroTile = hero.getTile();
				if(accessibleTiles.contains(heroTile)){
					heroesAccessible.add(hero);
					distancetoheroes[heroesAccessibleNumber][0]=ai.getZone().getTileDistance(mytile,heroTile);
					heroesAccessibleNumber++;
				}		
				else	{
					heroesNonAccessible.add(hero);
					distancetoheroes[heroesNonAccessibleNumber][1]=ai.getZone().getTileDistance(mytile,heroTile);
					heroesNonAccessibleNumber++;
				}
			}

			if(heroesAccessible.isEmpty()){
					smallBig=100;				
					for(i=0;i<heroesNonAccessibleNumber;i++){			
						ai.checkInterruption();
						if(distancetoheroes[i][1]<=smallBig){
							heroNonAccessibleTile=heroesNonAccessible.get(i).getTile();
							smallBig=distancetoheroes[i][1];
						}		
					}				
					smallBig=100;			
					for(AiTile item : accessibleTiles){
						ai.checkInterruption();
						if(ai.getZone().getTileDistance(heroNonAccessibleTile,item)<=smallBig){
							smallBig=ai.getZone().getTileDistance(heroNonAccessibleTile,item);
							destinationTile=item;
						}
					}				
					if(destinationTile==tile){
						return 0;
					}
					else	
						return 1;					
			}

			else	{
					smallBig=100;
				
					for(i=0;i<heroesAccessibleNumber;i++){			
						ai.checkInterruption();
						if(distancetoheroes[i][0]<=smallBig){
							heroAccessibleTile=heroesAccessible.get(i).getTile();
							smallBig=distancetoheroes[i][0];
						}		
					}
					smallBig=100;
				
					for(AiTile item : accessibleTiles){
						ai.checkInterruption();
						if(ai.getZone().getTileDistance(heroAccessibleTile,item)<=smallBig){
							smallBig=ai.getZone().getTileDistance(heroAccessibleTile,item);
							destinationTile=item;
						}
					}
				
						if(destinationTile==tile){
							return 0;
						}
						else	
							return 1;
			
			}	
	
		}
		else return 1;
		
	}
}
