package org.totalboumboum.ai.v201112.ais.sakaryasar.v3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
@SuppressWarnings("deprecation")
public class Tools extends AiAbstractHandler<SakarYasar>{

	protected Tools(SakarYasar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	protected void update() throws StopRequestException
	{	ai.checkInterruption();
		
		currentTile = ownHero.getTile();
	}
	
	public AiHero ownHero = null;
	public AiZone zone = null;
	public AiTile currentTile = null;
	
	public double upDelay;
	public double downDelay;
	public double leftDelay;
	public double rightDelay;
	
	/**
	 * method for controlling if a tile is in danger or not
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isTileInDanger(AiTile tile) throws StopRequestException{		
		ai.checkInterruption();
		
		boolean result = false;
		
		upDelay= checkSide(tile, Direction.UP);
		downDelay = checkSide(tile, Direction.DOWN);
		leftDelay = checkSide(tile, Direction.LEFT);
		rightDelay = checkSide(tile, Direction.RIGHT);
		
		if(upDelay+downDelay+leftDelay+rightDelay > 0){
			result = true;
		}
		return result;
	}

	/**
	 * method returns delay of bomb on the d side
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	public double checkSide(AiTile tile,Direction d) throws StopRequestException{
		ai.checkInterruption();

		double result = 0;
		boolean cont = true;
		
		AiTile temp=tile;
		List<AiBomb> bombs;
		int step=0;
		AiBomb btemp=null;
		
		HashMap<AiBomb,Long> bombsWithDelay = zone.getDelaysByBombs();
		while(cont)
		{
			ai.checkInterruption();

			bombs=temp.getBombs();
			if(bombs.isEmpty()){
				if(!temp.getBlocks().isEmpty()){
					cont = false;
				}
			}
			else{
				btemp=bombs.get(0);
				if(btemp.getRange() < step){
					cont = false;
				}
				else{
					result = bombsWithDelay.get(btemp);
				}
			}
			temp=temp.getNeighbor(d);
			step++;
		}
		return result;
	}
	
	public boolean isTilePassable(AiTile tile ) throws StopRequestException{
		ai.checkInterruption();
		boolean result = true;
		double 	currentSpeed = ownHero.getWalkingSpeed();
		double time = 1.5*(1000*tile.getSize()/currentSpeed);
		
		if(isTileInDanger(tile)){
			Double delays[]= new Double[4];
			delays[0]=upDelay;
			delays[1]=downDelay;
			delays[2]=leftDelay;
			delays[3]=rightDelay;
			Arrays.sort(delays);
			
			if(delays[3]<time){
				result=false;
			}
		}
		if(!tile.getFires().isEmpty()){
			result = false;
		}
		return result;
	}
}
