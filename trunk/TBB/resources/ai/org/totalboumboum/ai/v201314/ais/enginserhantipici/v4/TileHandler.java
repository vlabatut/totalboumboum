package org.totalboumboum.ai.v201314.ais.enginserhantipici.v4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;

/**
 * This class is a collection of common methods about tiles
 * 
 * 
 * @author Gözde Engin
* @author Barış Serhan
* @author Garip Tipici
*/
public class TileHandler extends AiAbstractHandler<Agent>{

	/**
	 * construct a handler for agent to pass a parameter
	 
	 * @param ai	
	 * 		the agent who is managed by this class
	 */
	public TileHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();
	}
	
	/**
	 * This method detects if the tile is in danger or not
	 * @param tile AiTile
	 * @return result boolean
	 */
	public boolean isInDanger(AiTile tile){
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		boolean result;
		List<AiTile> dangertiles = new ArrayList<AiTile>();
		Map<Long, List<AiBomb>> allbombs = zone.getBombsByDelays();
		for(long l : allbombs.keySet()){
			ai.checkInterruption();
			for(AiBomb bomb : allbombs.get(l)){
				ai.checkInterruption();
				if(l < 1700){
					dangertiles.addAll(bomb.getBlast());
				}
			}
		}
		
		if(dangertiles.contains(tile)){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	/**
	 * This method detects if the tile is in danger or not
	 * to do this, it needs two parameters
	 * it returns true if there is bomb blast(by delays) on destination and the time require is less then the limit
	 * @param tile destination tile
	 * @param limit time necessary in miliseconds
	 * @return true if there is danger
	 */
	public boolean isInDangerTest(AiTile tile, long limit){
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		boolean result;
		List<AiTile> dangertiles = new ArrayList<AiTile>();
		Map<Long, List<AiBomb>> allbombs = zone.getBombsByDelays();
		for(long l : allbombs.keySet()){
			ai.checkInterruption();
				if(l < limit){
					for(AiBomb bomb : allbombs.get(l)){
						ai.checkInterruption();
						dangertiles.addAll(bomb.getBlast());
					}
			}
		}
		
		if(dangertiles.contains(tile)){
			result = true;
		}else{
			result = false;
		}
		return result;
	}
	
	
	/**
	 * This method returns: 3 if a tile is not in danger tile of a bomb
	 * 					  : 2 if a tile is in a danger tile of a bomb that will explode in 2 or more seconds
	 * 					  : 1 if a tile is in a danger tile of a bomb that will explode in 2 or less seconds
	 * @param tile that will be analyzed for danger
	 * @return Degree of danger.
	 */
	public int DangerDegree(AiTile tile){
		ai.checkInterruption();
		int result = 3;
		Map<Long, List<AiBomb>> bombs= ai.getZone().getBombsByDelays();
		bombs = new HashMap<Long, List<AiBomb>>(bombs);
		int bombCounter = 0;
		for(Long delay : bombs.keySet()){
			ai.checkInterruption();
			for(AiBomb bomb : bombs.get(delay)){
				ai.checkInterruption();
				if(bomb.getBlast().contains(tile)){
					bombCounter++;
					if(delay < 2000 || bombCounter > 1 || !tile.getFires().isEmpty()){
						result = 1;
						break;
					}
					else{
						result = 2;
						break;
					}
				}
			}
			if(result == 1){
				break;
			}
		}
		return result;
		
	}
	
	
	
	/**
	 * This method returns true if our hero can reach before the rival's hero, else returns false.
	 * @param ownhero : Our hero
	 * @param rival   : Our rival's hero
	 * @param target  : Tile that heros want to go
	 * @return boolean
	 */
	public boolean isATileCompetible(AiHero ownhero, AiHero rival, AiTile target){
		ai.checkInterruption();
		boolean result;
		double distancerival, mydistance;
		AiLocation ownLocation = new AiLocation(ownhero);
		AiLocation rivalLocation = new AiLocation(rival);
		
		double mytime, rivaltime;
		
		distancerival = ai.getZone().getPixelDistance(rivalLocation, target);
		mydistance =ai. getZone().getPixelDistance(ownLocation, target);
		
		mytime = mydistance / ownhero.getWalkingSpeed();
		rivaltime = distancerival / rival.getWalkingSpeed();
		
		if(mytime < rivaltime) 
			result = true;
		else 
			result = false;
		
		
		return result;
	}
}
