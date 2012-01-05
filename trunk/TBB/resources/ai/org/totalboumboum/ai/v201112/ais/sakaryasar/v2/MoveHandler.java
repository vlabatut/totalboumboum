package org.totalboumboum.ai.v201112.ais.sakaryasar.v2;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Cahide Sakar
 * @author Abdurrahman Yaşar
 */
/**
 * @author fortuna
 *
 */
public class MoveHandler extends AiMoveHandler<SakarYasar> {
	protected AiZone zone = null;
	protected AiHero ownHero = null;
	protected Astar astarPath = null;
	protected AiTile heroTile = null;
	protected AiTile oldDestination = null;
	
	protected MoveHandler(SakarYasar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		this.zone = ai.getZone();
		this.ownHero = zone.getOwnHero();

		CostCalculator cc = new TimeCostCalculator(ai, ownHero);
		cc.setOpponentCost(1000);
		HeuristicCalculator hc = new TimeHeuristicCalculator(ai, ownHero);
		SuccessorCalculator sc = new TimePartialSuccessorCalculator(ai,
				TimePartialSuccessorCalculator.MODE_NOTREE);
		astarPath = new Astar(ai, ownHero, cc, hc, sc);

		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();
		AiLocation start = new AiLocation(ownHero);

		oldDestination = ownHero.getTile();
		
		refreshDestination();
		
		try {
			this.currentPath = astarPath.processShortestPath(start,
					this.currentDestination);
		} catch (LimitReachedException e) {
		}

		Direction d = Direction.NONE;
		AiLocation first=null;
		if(this.currentPath!=null){
			if (this.currentPath.getLocations().size() > 1) {
				first = this.currentPath.getLocations().get(1);
				d = ai.getZone().getDirection(ownHero.getPosX(), ownHero.getPosY(),first.getPosX(), first.getPosY());
				
				if(isInDanger(first.getTile())){
					d=Direction.NONE;
				}
			}
		}

		return d;
	}
	//////////////////////////////////////////////////////////////////
	// Own Methods 		//////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	/**
	 * Setter method for current destination.
	 * @param tile
	 * @throws StopRequestException
	 */
	public void setCurrentDestination(AiTile tile) throws StopRequestException{
		ai.checkInterruption();

		this.currentDestination = tile;
	}
	/**
	 * Method for testing if the given tile is in danger or not
	 * @param tile
	 * @return
	 * @throws StopRequestException
	 */
	private boolean isInDanger(AiTile tile) throws StopRequestException{
		ai.checkInterruption();

		double left,right,up,down;
		boolean result =false;
		double tileSize = ownHero.getTile().getSize();
		double t=0;
		
		if(!tile.getFires().isEmpty()){
			result= true;	
		}
		else {
			left = checkSide(tile, Direction.LEFT);
			right = checkSide(tile, Direction.RIGHT);
			up = checkSide(tile, Direction.UP);
			down = checkSide(tile, Direction.DOWN);
		
			t=(tileSize/ownHero.getWalkingSpeed())*1000;
			if(up>0 && t>up){
				result =true;
			}
			if(down>0 && t>down){
				result =true;
			}
			if(left>0 && t>left){
				result =true;
			}
			if(right>0 && t>right){
				result =true;
			}
		}
		
		return result;
	}
	/**
	 * this method tests the side d which is given by parameter while there is not any obstacle or bomb 
	 * @param tile
	 * @param d
	 * @return
	 * @throws StopRequestException
	 */
	private double checkSide(AiTile tile,Direction d) throws StopRequestException{
		ai.checkInterruption();

		AiTile temp=tile;
		double result = 0;
		boolean cont = true;
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
	
	/**
	 * methode for changing destination if it is necessary
	 * @throws StopRequestException
	 */
	private void refreshDestination() throws StopRequestException {
		ai.checkInterruption();
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		heroTile = ownHero.getTile();
		int i=0;
		HashMap<AiTile, Float> hm = ai.getUtilityHandler().getUtilitiesByTile();
		HashMap<Float, List<AiTile>> hm2 = ai.getUtilityHandler().getUtilitiesByValue();
		Float max = Collections.max(hm.values());

		if(!hm2.get(max).contains(this.currentDestination)  ){
			this.currentDestination = hm2.get(max).get(0);
		}
		if(this.currentDestination == ownHero.getTile() || oldDestination == ownHero.getTile()){
			i++;
		}
		if(i>2 ){
			this.currentDestination = hm2.get(max).get(((int)(100*Math.random()) % hm2.get(max).size() ));
			i=0;
		}

	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();
	}
}
