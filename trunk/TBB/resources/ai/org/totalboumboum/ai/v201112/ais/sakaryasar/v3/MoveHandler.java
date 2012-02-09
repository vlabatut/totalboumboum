package org.totalboumboum.ai.v201112.ais.sakaryasar.v3;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMode;
import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
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
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<SakarYasar> {
	protected AiZone zone = null;
	protected AiHero ownHero = null;
	protected Astar astarPath = null;
	protected AiTile heroTile = null;
	
	protected MoveHandler(SakarYasar ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		this.zone = ai.getZone();
		this.ownHero = zone.getOwnHero();

		CostCalculator cc = new TimeCostCalculator(ai, ownHero);
		cc.setOpponentCost(1000);
		HeuristicCalculator hc = new TimeHeuristicCalculator(ai, ownHero);
		SuccessorCalculator sc = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astarPath = new Astar(ai, ownHero, cc, hc, sc);

		verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	public boolean waitNextTerm = false;
	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();

		heroTile = ownHero.getTile();
		
		refreshDestination();
		if (currentDestination != null) {
						
			AiLocation start = new AiLocation(ownHero);
			try {
				this.currentPath = astarPath.processShortestPath(start,
						this.currentDestination);
			} catch (LimitReachedException e) {
				e.printStackTrace();
			}
	
		}


		refreshDirection();

		return currentDirection;
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
	private boolean isPassable(AiTile tile) throws StopRequestException{
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
		Float destUtility = ai.utilityHandler.getUtilitiesByTile().get(currentDestination);
		Float heroUtility = ai.utilityHandler.getUtilitiesByTile().get(heroTile);
		
		oldDestination=currentDestination;
		
		if((currentDestination == heroTile && ownHero.getBombNumberCurrent() == 0) ||  
				destUtility ==null || 
				destUtility==0 ){
			currentDestination=findDestination();
		}
		else if(currentDestination == heroTile && heroUtility == 0){
			currentDestination=findDestination();
		}
		else if(currentDestination == heroTile && heroUtility == null){
			currentDestination=findDestination();
		}
		else if(currentDestination == heroTile && heroUtility >=11 && ai.modeHandler.getMode()==AiMode.ATTACKING){
			currentDestination=findDestination();
		}
		else if(heroUtility==0 && currentDestination == heroTile){
			currentDestination=findDestination();	
		}
		else if(currentDirection==Direction.NONE && heroUtility==0){
			currentDestination=findDestination();	
		}
		else if( heroUtility==null){
			if(currentDirection==Direction.NONE){
				currentDestination=findDestination();
			}
		}
		else if(!heroTile.getBombs().isEmpty()){
			if(!ai.bombHandler.isThereEnemyDanger()){
				AiTile temp = currentDestination;
				Float v1 = (float) 6.0;
				Float v2 = (float) 5.0;
				Float v3 = (float) 4.0;

				if( (temp=findDestinationInEnemyDanger(v2))!=null){
					currentDestination=temp;
				}
				else if( (temp=findDestinationInEnemyDanger(v3))!=null){
					currentDestination=temp;
				}
				else if( (temp=findDestinationInEnemyDanger(v1))!=null){
					currentDestination=temp;
				}
			}	
		}
		else if(checkPathForBombs(currentPath)){
			AiTile temp = currentDestination;
			Float v1 = (float) 6.0;
			Float v2 = (float) 5.0;
			Float v3 = (float) 4.0;

			if( (temp=findDestinationInEnemyDanger(v2))!=null){
				currentDestination=temp;
			}
			else if( (temp=findDestinationInEnemyDanger(v3))!=null){
				currentDestination=temp;
			}
			else if( (temp=findDestinationInEnemyDanger(v1))!=null){
				currentDestination=temp;
			}
		}	
	}
	
	/**
	 * check path if is there any tile that is dangerous
	 * @return
	 * @throws StopRequestException
	 */
	public boolean isThereAnyUnsafeTileOnPath() throws StopRequestException{
		ai.checkInterruption();
		boolean result = false;
		
		AiPath temp = currentPath;
		int size = temp.getLength();
		int i =0;
		while(i<size && !result){
			ai.checkInterruption();
			if(ai.utilityHandler.getUtilitiesByTile().get(temp.getLocation(i).getTile()) ==0){
				result = true;
			}
			i++;
		}
		
		return result;
	}
	/**
	 * find best destination from current position of our hero.
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile findDestination() throws StopRequestException{
		ai.checkInterruption();

		AiTile result=null;

		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		heroTile = ownHero.getTile();
		
		HashMap<AiTile, Float> hm = ai.getUtilityHandler().getUtilitiesByTile();
		HashMap<Float, List<AiTile>> hm2 = ai.getUtilityHandler().getUtilitiesByValue();
		Float max = Collections.max(hm.values());
		
		List <AiTile> maxTiles = hm2.get(max);
		int maxSize = maxTiles.size();
		
		if(max>0){
			int i=0;
			AiTile temp;
			do{
				ai.checkInterruption();
				temp = maxTiles.get(i);
				i++;
			}while(i<maxSize && temp == heroTile);
			if(temp != heroTile){
				result = temp;
			}
			else{
				result = null;
			}
		}
		if(result == null){
			int tilesSize = hm2.values().size();
			if(max < 2){
				result = null;
			}
			else{
				AiTile temp = null;
				int i=0;
				Float keys = max-1;
				do{
					ai.checkInterruption();
					List<AiTile> tiles = hm2.get(keys);
					int j = 0;
					if (tiles != null) {
						do {
							ai.checkInterruption();
							temp = tiles.get(j);
							++j;
						} while (j < maxSize && temp == heroTile);
						if (temp != heroTile) {
							result = temp;
						} else {
							result = null;
						}
					}
					--keys;
					++i;
				}while(temp!=heroTile && i<tilesSize && result == null && keys > 1);
			}
		}
		return result;
	}
	/**
	 * method for finding a destination whose utility is 'value'
	 * @param value
	 * @return
	 * @throws StopRequestException
	 */
	private AiTile findDestinationInEnemyDanger(Float value) throws StopRequestException{
		ai.checkInterruption();

		AiTile result=null;

		zone = ai.getZone();
		ownHero = zone.getOwnHero();
		heroTile = ownHero.getTile();

		HashMap<Float, List<AiTile>> hm2 = ai.getUtilityHandler().getUtilitiesByValue();


		AiTile temp = null;
		List<AiTile> tiles = hm2.get(value);

		int j = 0;

		if (tiles != null) {
			int tilesSize = tiles.size();
			do {
				ai.checkInterruption();
				temp = tiles.get(j);
				++j;
			} while (j < tilesSize && temp == heroTile);
			if (temp != heroTile) {
				result = temp;
			} else {
				result = null;
			}
		}

		return result;
	}
	/**
	 * method who checks path for seeing if there is an enemy on the road.
	 * @param path
	 * @return
	 * @throws StopRequestException
	 */
	public boolean checkPathForEnemmies(AiPath path) throws StopRequestException{
		ai.checkInterruption();
		
		boolean result = false;
		Direction dt = Direction.NONE;
		if (path != null) {
			int size = path.getLength();
			int i = 1;
			boolean cont = true;
			if (size > 1) {
				dt = zone.getDirection(path.getLocation(0), path.getLocation(1));
				while (i < size && !result && cont) {
					ai.checkInterruption();
					List<AiHero> h = path.getLocation(i).getTile().getHeroes();
					if (!h.isEmpty()) {
						if (!h.contains(ownHero)) {
							result = true;
						}
					}

					if (dt != zone.getDirection(path.getLocation(i - 1),
							path.getLocation(i))) {
						cont = false;
					}
					i++;
				}
			}
		}
		return result;
	}
	/**
	 * method for controlling bombs on the path
	 * @param path
	 * @return
	 * @throws StopRequestException
	 */
	public boolean checkPathForBombs(AiPath path) throws StopRequestException{
		ai.checkInterruption();
		
		boolean result = false;
		Direction dt = Direction.NONE;
		if (path != null) {
			int size = path.getLength();
			int i = 1;
			boolean cont = true;
			if (size > 1) {
				dt = zone.getDirection(path.getLocation(0), path.getLocation(1));
				while (i < size && !result && cont) {
					ai.checkInterruption();
					List<AiBomb> ab = path.getLocation(i).getTile().getBombs();
					if(!ab.isEmpty()){
						result = true;
					}
					if (dt != zone.getDirection(path.getLocation(i - 1),
							path.getLocation(i))) {
						cont = false;
					}
					i++;
				}
			}
		}
		return result;
	}
	
	protected AiTile oldDestination = null;
	private Direction lastDirections[] = new Direction[4];
	private int step =0;
	/**
	 * method for updating direction in some special cases this method returns Direction.none
	 * @throws StopRequestException
	 */
	private void refreshDirection() throws StopRequestException{
		ai.checkInterruption();
		
		Direction d = Direction.NONE;
		AiLocation first=null;
		AiPath p=currentPath;
		
		if (this.currentPath != null && currentDestination != null) {
			if (p.getLocations().size() > 1) {
				first = p.getLocations().get(1);
				d = ai.getZone().getDirection(ownHero.getPosX(),
						ownHero.getPosY(), first.getPosX(), first.getPosY());

				if (isPassable(first.getTile()) ) {
					d = Direction.NONE;
				}

			}
		}
		
		if(waitNextTerm ){
			d=Direction.NONE;
			waitNextTerm=false;
		}
		//for repeated actions(method naive)
		if(step<4){
			lastDirections[step] = d;
			++step;
		}
		else{
			boolean checkSimilarity = (lastDirections[0].equals(lastDirections[2])) &&
										(lastDirections[1].equals(lastDirections[3]));
			
			if(checkSimilarity && d == lastDirections[0]){
				d=lastDirections[1];
			}
			step = 0;
		}
		currentDirection=d;
	}
	
	//////////////////////////////////////////////////////////////////
	// OUTPUT  			 /////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////	
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		super.updateOutput();
	}
}
