

package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;


/**
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
 
public class CalculationHelper {

	/**
	 * On definit un objet ai de la classe agent pour qu'on l'utilise dans des autres classes
	 */
	Agent ai;
	/**	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * @param ai
	 * l'agent que cette classe doit gérer.
	 */
	
	public CalculationHelper(Agent ai) {
			ai.checkInterruption();
			this.ai=ai;
		}
	
	/** C'est un map dans laquelle on peut trouver le valeurs numerique associé aux dangers des tiles accessible.*/
	 
    public void dangerMap()
    {
    	ai.checkInterruption();
    	ai.dangerMap.clear();
    	for(AiTile tile:ai.access){
    		ai.checkInterruption();
    		ai.dangerMap.put(tile,dangerAllTile(tile)); 
    	}    	
    }
    

    
    
    /** @return 0 quand il n'y a pas du danger dans cette tile
	 * 1 quand il y a un bombe dans cette tile 
	 * 2 quand il y a un bombe qui va exploiter dans un temps tres proche ou il y a un feu dans cette tile  
     * @param Tile des tiles accessible
      */
   
    public int dangerAllTile(AiTile Tile)
    {
    	ai.checkInterruption();
    	int result=0;
    	List<AiTile> Tiles = new ArrayList<AiTile>();
    	Map<AiBomb,Long> bombtime=ai.getZone().getDelaysByBombs();		
		if(!Tile.getFires().isEmpty()){
			result=2;
		}		
		for(AiBomb Bomba:bombtime.keySet())
		{
			ai.checkInterruption();
			Tiles.addAll(Bomba.getBlast());
			if(Tiles.contains(Tile)){
				if(bombtime.get(Bomba)<800){
					result=2;
					ai.Time=bombtime.get(Bomba);
				}
				else if (ai.Time>bombtime.get(Bomba))
					{ai.checkInterruption();
result=1;}
			}
		}	
		ai.Time=10000;
		return result;		
    }
    
    
    /**
     * @param Tile Un tile acccessible 
     * @return Integer valeur (0,1,2)
     */
    public int getDangerValue(AiTile Tile)
    {
    	ai.checkInterruption();
    	int a=ai.dangerMap.get(Tile);
    	return a;
    }
    
    
    /**
	 * fonction pour trouver l'ennemi plus proche
	 * 
	 * @return AiHero de l'ennemi plus proche	
	 */ 
    public AiHero getNearestEnemy()  {
		ai.checkInterruption();
	
		AiHero hero = ai.getZone().getOwnHero();
		AiHero target = null;
		
		int distance = 5000;
		int temp_distance=0;
		
		if (ai.enemyAccessible) {
			for (AiHero enemy : ai.getZone().getRemainingOpponents()) {
				ai.checkInterruption();
				if (((temp_distance=simpleTileDistance(hero.getTile(), enemy.getTile())) < distance)
						&& (ai.access.contains(enemy.getTile()))){
					distance=temp_distance;
					target = enemy;
				}
			}
		}
		else
		{
			for (AiHero enemy : ai.getZone().getRemainingOpponents()) {
				ai.checkInterruption();
				if (((temp_distance=simpleTileDistance(hero.getTile(), enemy.getTile())) < distance)){
					distance=temp_distance;
					target = enemy;
				}
			}
			
		}
		return target;
	}
	/**
	 * 
	 * Une methode simple pour determiner distance de deux cases
	 * 
	 * @param tile1
	 * 		description manquante !	
	 * @param tile2
	 * 		description manquante !	
	 * @return la distance entre deux cases
	 */
    public int simpleTileDistance(AiTile tile1,AiTile tile2)  
	{
		ai.checkInterruption();
		return Math.abs(tile1.getCol()-tile2.getCol())+Math.abs(tile1.getRow()-tile2.getRow());
	}
    
   
    
    /**
	 * Cherche les cases accessibles qui sont a cotes des murs destructibles et qui sont
	 *  les plus proches a un ennemi inaccesible
	 * 
	 * @return arrayList bestwall dans le map
     */
    public ArrayList<AiTile> nearAccesWall(){
    	ai.checkInterruption();
    	int d=1000;
    	ArrayList<AiTile> result=new ArrayList<AiTile>();
    	ArrayList<AiTile> murs=new ArrayList<AiTile>();
    	
    	AiTile til=null;
    // on l'utilise pour debug. Il y a un faut dans le TBB
    //	int a = ai.zone.getRemainingOpponents().size();
    	if(!ai.enemyAccessible && ai.zone.getRemainingOpponents().size()>0){
    	for(AiBlock mur:ai.accessWall){
			ai.checkInterruption();
			
			if(d>simpleTileDistance(ai.nearestEnemy.getTile(), mur.getTile())){
				d=simpleTileDistance(ai.nearestEnemy.getTile(), mur.getTile());
				til=mur.getTile();
			}
		}
    	for(AiBlock mur:ai.accessWall){
			ai.checkInterruption();
			
			if(d==simpleTileDistance(ai.nearestEnemy.getTile(), mur.getTile())){
				
				murs.add(mur.getTile());
			}
		}
    	}
    	if(til!=null){
    		for(AiTile ti:murs)
    		{	ai.checkInterruption();
    		for(AiTile tile:ti.getNeighbors()){
    			ai.checkInterruption();
    			if(ai.access.contains(tile)){
    				result.add(tile);
    			}}
    		}}
    	return result;
    }
	
}
