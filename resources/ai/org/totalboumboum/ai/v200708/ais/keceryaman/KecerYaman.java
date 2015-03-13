package org.totalboumboum.ai.v200708.ais.keceryaman;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Serkan Keçer
 * @author Onur Yaman
 *
 */
@SuppressWarnings("deprecation")
public class KecerYaman extends ArtificialIntelligence {
	/********************************************************************************/
	public static final long serialVersionUID = 1L;
	
	/** les points des objets differents */
	private int VAL_HARD = AI_BLOCK_WALL_HARD;
	/** */
	private int VAL_BOMB = 500;
	/** */
	private int VAL_FLAME = VAL_BOMB;
//	private int VAL_SHRINK = VAL_BOMB;
//	private int VAL_SHRINK_CST = 7;
	/** */
	private int VAL_SOFT = 200;
	/** */
	private int VAL_PLAYER = -1000;
	/** */
	private int VAL_PLAYER_CST = 7;
	/** */
	private int VAL_EMPTY = 0;
	/** */
	private int VAL_BONUS = -10;
	/** */
	private int VAL_UNKNOWN = 50000;
	/** */
	private int VAL_HEURISTIC = 3;
	/** */
	private int[][] AREA = {{7,7},{8,7},{7,8},{8,8}};
	/** */
	private int[] GROUP_POINTS = {0,0,0,0};
	/** algorithm pour trouver le mieux chemin 	 */
	private AStar bestPath;
	/** */
	private List<Node> path = new ArrayList<Node>();
	/** */
	private Node currentNode;
	/** */
	private Node nextNode;
	
	/** temporary variables */
	private boolean started = false;
	/** */
	private boolean bombPlanted = false;
	/** */
	private int action;
	
	/********************************************************************************/
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{	action = AI_ACTION_DO_NOTHING;
		
		if(firstTime)
		{	firstTime = false;
			AStar.init(getZoneMatrixDimY(),getZoneMatrixDimX());
		}
		else
		{	
		// initialization of our map that is used during 
		// calculations
		Map.init( getZoneMatrix() );
		addPlayersToMap();
		addFlamesToMap();
		// own position
		int ownPosX = getOwnPosition()[0];
		int ownPosY = getOwnPosition()[1];

		if ( !started ){
			Node startNode = new Node(ownPosX,ownPosY,null,0,0);
			int[] target = {9,7};
			bestPath = new AStar(startNode,target);
			updatePath(startNode);
			started = true;	
		}

		if ( getTimeBeforeShrink() < 1000 ){
			AStar.target[0] = 9;
			AStar.target[1] = 7;
		}
		
		else if ( bombPlanted() ){
			updatePath(new Node(currentNode.getX(),currentNode.getY(),null,0,0));
		}
		
		else if ( !currentNode.equals(nextNode) ){
			if ( !currentNode.equals(ownPosX, ownPosY) ){
				currentNode = nextNode;
				if ( path.size() > 0 ){
					nextNode = path.get( (path.size()-1) );
					path.remove( (path.size()-1) );
				}
			}
		
		}else {
			updatePath(new Node(currentNode.getX(),currentNode.getY(),null,0,0));
		}
		action = calculateAction(currentNode, nextNode);
		}
		
		return action;
	}
	
	/********************************************************************************/
	/**
	 * Constructor
	 */
	public KecerYaman() {
		super("KecerYaman");
	}
	/*******************************************************************************/
	/**
	 * Other methods used during calculations
	 */
	
	/**
	 * Adds player positions to the map
	 *  Do not add dead players' positions
	 */
	private void addPlayersToMap (){
		int playerCount = getPlayerCount();
		int[] ownPosition = getOwnPosition();
		int i = 0;
		int x,y;
		
		while ( i < playerCount ){
			// coordinates of the player #i
			x = getPlayerPosition(i)[0];
			y = getPlayerPosition(i)[1];
			
			if ( x == ownPosition[0] && y == ownPosition[1] ){
				i++;
				continue;
			}
			else if ( getBombPowerAt(x, y) != -1 ){
				i++;
				continue;
			}
			else if ( !isPlayerAlive(i) )
			{
				i++;
				continue;
			}
			else{
				Map.setValue(x, y, VAL_PLAYER);
			}
			i++;
		}
	}
	
	/**
	 * Adds flames to the map before the bomb blows up
	 */
	private void addFlamesToMap (){
		int bombPower;
		int value;
		
		// for each square in the map (except the outside boundries)
//		for ( int y = 1 ; y <= 13 ; y ++ ){
		for ( int y = 0 ; y < getZoneMatrixDimY() ; y ++ ){ //adjustment
//			for ( int x = 1 ; x <=15 ; x++ ){
			for ( int x = 0 ; x <getZoneMatrixDimX() ; x++ ){ //adjustment
				value = Map.getValue(x, y);
				if ( value == AI_BLOCK_BOMB || value == VAL_PLAYER ){
					bombPower = getBombPowerAt(x, y);
					if ( bombPower != -1 ){
						// top
						addFlamesToDirection(x, y, bombPower, 0, -1);
						// right
						addFlamesToDirection(x, y, bombPower, 1, 0);
						// bottom
						addFlamesToDirection(x, y, bombPower, 0, 1);
						// left
						addFlamesToDirection(x, y, bombPower, -1, 0);
					}
					// self
					if ( value == AI_BLOCK_BOMB ){
						Map.setValue(x, y, VAL_BOMB);
					}
				}else if ( value == AI_BLOCK_FIRE ){
					Map.setValue(x, y, VAL_FLAME);
				}else if ( value == AI_BLOCK_WALL_SOFT ){
					Map.setValue(x, y, VAL_SOFT);
				}else if ( value == AI_BLOCK_EMPTY ){
					Map.setValue(x, y, VAL_EMPTY);
				}else if ( value == AI_BLOCK_ITEM_BOMB || value == AI_BLOCK_ITEM_FIRE ){
					Map.setValue(x, y, VAL_BONUS);
				}else if ( value == AI_BLOCK_UNKNOWN){
					Map.setValue(x, y, VAL_UNKNOWN);
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @param bombPower
	 * 		Description manquante !
	 * @param wX
	 * 		Description manquante !
	 * @param wY
	 * 		Description manquante !
	 */
	private void addFlamesToDirection ( int x , int y , int bombPower , int wX , int wY ){
		boolean blocked = false;
		int i = 0;
		int value;
		
		while ( i < bombPower && !blocked ){
			// boundry check
//			if ( x > 0 && x < 17 ) x += wX;
			if ( x > 0 && x < getZoneMatrixDimX() ) x += wX; //adjustment
//			if ( y > 0 && y < 15 ) y += wY;
			if ( y > 0 && y < getZoneMatrixDimY() ) y += wY; //adjustment
			
			// does anything blocks the flames?
			value = Map.getValue(x, y);
			if ( value == AI_BLOCK_WALL_HARD || value == AI_BLOCK_WALL_SOFT || value == AI_BLOCK_BOMB ){
				Map.setValue(x, y, VAL_HARD);
				blocked = true;
			}
			else{
				Map.setValue(x, y, VAL_FLAME);
			}
			
			// increment i
			i++;
		}
	}
	
	/**
	 * 
	 * @param current
	 * 		Description manquante !
	 * @param next
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private int calculateAction ( Node current , Node next ){
		int currentX = current.getX();
		int nextX = next.getX();
		int currentY = current.getY();
		int nextY = next.getY();
		int action = AI_ACTION_DO_NOTHING;
		
		if ( currentX == nextX ){
			if ( currentY < nextY ) action = AI_ACTION_GO_DOWN; 
			else if ( currentY == nextY ) action = AI_ACTION_DO_NOTHING;
			else action = AI_ACTION_GO_UP;
		}else if ( currentX < nextX ){
			if ( currentY == nextY ) action = AI_ACTION_GO_RIGHT;
		}else if ( currentX > nextX ){
			if ( currentY == nextY ) action = AI_ACTION_GO_LEFT;
		}
		
		setBombNotPlanted();
		
		if ( Map.getValue(nextX, nextY) == VAL_SOFT || Map.getValue(nextX, nextY) < (VAL_PLAYER + 100) ){
			if ( !isInDanger(currentX,currentY) ){
				action = AI_ACTION_PUT_BOMB;
				setBombPlanted();
				VAL_SOFT = 1000;
			}
		}
		
		return action;
	}

	/**
	 * 
	 * @return
	 * 		?
	 */
	private boolean bombPlanted (){
		boolean temp = bombPlanted;
		setBombNotPlanted();
		return temp;
	}
	
	/**
	 * 
	 */
	private void setBombPlanted (){
		bombPlanted = true;
	}
	
	/**
	 * 
	 */
	private void setBombNotPlanted (){
		bombPlanted = false;
	}
	
	/**
	 * 
	 * @param node
	 * 		Description manquante !
	 */
	private void updatePath ( Node node ){
		path.clear();
		bestPath.setTargetNotAdded();
		findTarget();
		path = bestPath.bestPath(node);
		currentNode = node;
		if ( path.size() > 0 ){
			nextNode = path.get( (path.size()-1) );
			path.remove( (path.size()-1) );
		}else{
			nextNode = currentNode;
		}
	}

	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private boolean isInDanger ( int x , int y ){
		if ( Map.getValue((x-1), y) == VAL_BOMB || 
			 Map.getValue((x-1), y) == VAL_FLAME ||
			 Map.getValue((x+1), y) == VAL_BOMB || 
			 Map.getValue((x+1), y) == VAL_FLAME ||
			 Map.getValue(x, (y-1)) == VAL_BOMB || 
			 Map.getValue(x, (y-1)) == VAL_FLAME ||
			 Map.getValue(x, (y+1)) == VAL_BOMB || 
			 Map.getValue(x, (y+1)) == VAL_FLAME || 
			 Map.getValue(x, y) == VAL_FLAME || 
			 Map.getValue(x, y) == VAL_BOMB
			 ){
			return true;
		}
		
		return false;
	}

	/**
	 * Find target
	 */
	private void findTarget (){
		int[] min = {9,7};
		int ownX = getOwnPosition()[0];
		int ownY = getOwnPosition()[1];
		if ( getTimeBeforeShrink() > 1000 && !isInDanger(ownX,ownY) ){
			min[0] = ownX;
			min[1] = ownY;
		}

		/**
		 * group points
		 */
		else if ( getTimeBeforeShrink() > 1000 ){	
			if ( bombPlanted() ){
//				for ( int y = 1 ; y <= 13 ; y++ ){
				for ( int y = 0 ; y < getZoneMatrixDimY() ; y++ ){ //adjustment
//					for ( int x = 1 ; x <= 15 ; x++ ){
					for ( int x = 0 ; x < getZoneMatrixDimX() ; x++ ){ //adjustment
						if ( Map.getValue(x, y) <= VAL_EMPTY ){
							min[0] = x;
							min[1] = y;
							break;
						}
					}
				}
			}else{
//				for ( int y = 1 ; y <= 13 ; y++ ){
				for ( int y = 0 ; y < getZoneMatrixDimY() ; y++ ){ //adjustment
//					for ( int x = 1 ; x <= 15 ; x++ ){
					for ( int x = 0 ; x < getZoneMatrixDimX() ; x++ ){ //adjustment
						if ( Map.getValue(x, y) != VAL_HARD ){
							updateGroupPoint ( x , y );
						}
					}
				}
				
//				for ( int y = 1 ; y <= 13 ; y++ ){
				for ( int y = 0 ; y < getZoneMatrixDimY() ; y++ ){ //adjustment
//					for ( int x = 1 ; x <= 15 ; x++ ){
					for ( int x = 0 ; x < getZoneMatrixDimX() ; x++ ){ //adjustment
						if ( Map.getValue(x, y) != VAL_HARD ){
							updatePositionPoint ( x , y );
						}
					}
				}
				
//				for ( int y = 1 ; y <= 13 ; y++ ){
				for ( int y = 0 ; y < getZoneMatrixDimY() ; y++ ){ //adjustment
//					for ( int x = 1 ; x <= 15 ; x++ ){
					for ( int x = 0 ; x < getZoneMatrixDimX() ; x++ ){ //adjustment
						if ( Map.getValue(x, y) != VAL_HARD ){
							if ( Map.getValue(x,y) < Map.getValue(min[0], min[1])){
								min[0] = x;
								min[1] = y;
							}					
						}
					}
				}
			}
		}
		
		AStar.target[0] = (int) min[0];
		AStar.target[1] = (int) min[1];
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	private void updateGroupPoint ( int x , int y ){
		int value = Map.getValue(x, y);
		
		if ( value == VAL_PLAYER ){
			value += VAL_PLAYER_CST;
		}
		
		// 1st area
		if ( x <= AREA[0][0] && y <= AREA[0][1] ){
			GROUP_POINTS[0] += value;
		}
		// 2nd area
		else if ( x <= AREA[1][0] && y <= AREA[0][1] ){
			GROUP_POINTS[1] += value;
		}
		// 3rd area
		else if ( x <= AREA[2][0] && y <= AREA[0][1] ){
			GROUP_POINTS[2] += value;
		}
		// 4th area
		else if ( x <= AREA[3][0] && y <= AREA[0][1] ){
			GROUP_POINTS[3] += value;
		}
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 * @return
	 * 		Description manquante !
	 */
	private int heuristic ( int x , int y  ){
		int tx = getOwnPosition()[0];
		int ty = getOwnPosition()[1];
		
		return VAL_HEURISTIC * ( Math.abs( tx - x ) + Math.abs( ty - y ) );
	}
	
	/**
	 * 
	 * @param x
	 * 		Description manquante !
	 * @param y
	 * 		Description manquante !
	 */
	private void updatePositionPoint ( int x , int y ){
		int heuristic = heuristic(x,y);
		Map.setValue(x,y,Map.getValue(x,y) + heuristic);
	}
}
