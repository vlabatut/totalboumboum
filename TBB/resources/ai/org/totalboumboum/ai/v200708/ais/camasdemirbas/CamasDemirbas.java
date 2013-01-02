package org.totalboumboum.ai.v200708.ais.camasdemirbas;

import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Gökhan Çamaş
 * @author İrem Demirbaş
 *
 */
@SuppressWarnings("deprecation")
public class CamasDemirbas extends ArtificialIntelligence
{	/** */
	public static final long serialVersionUID = 1L;
	/** si le mouvement suivant est AI_ACTION_PUT_BOMB, il est true */
	boolean bombFlag;
	/** il a le mouvement dernier */
	Integer previousMove;
	/** il nous donne le mouvent suivant s'il est necessaire */
	Integer nextMove;
	/** il nous donne les coordonnes apres le mouvement qu'il est en train de realiser */
	int[] newPosition = {-1,-1};
	/** Chemin suivi */
	private Path path;
	/** */
	private AStarPathFinder finder;
	/** */
	private GameMap map;
	/** */
	private static final int MIDDLEX = 9;
	/** */
	private static final int MIDDLEY = 7;
	/** */
	private Vector<Integer> pathMoves;
	/** */
	private boolean pathFlag;
	/** */
	private int pathIndex ;
	/** */
	private final static int MAXSEARCHDISTANCE = 500;
	/** */
	private final static int RUNINTERVAL = 5;
	/** */
	private int x ;
	/** */
	private int y ;
	
	

	/**
	 * Constructeur.
	 */
	public CamasDemirbas()
	{	super("CamasDmrbs");
		bombFlag=false;
		pathFlag=false;
		pathIndex=0;
		previousMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		nextMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		pathMoves = new Vector<Integer>();
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	@Override
	public Integer processAction() throws Exception
	{
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	
	x = getOwnPosition()[0];
	y = getOwnPosition()[1];
	int x2=newPosition[0];
	int y2=newPosition[1];
	int bomb[] = getClosestBlockPosition(x, y, ArtificialIntelligence.AI_BLOCK_BOMB);
	if(newPosition[0] !=-1 && (x!=x2 || y!=y2)) {
		result = previousMove;
	}
	else if(bombFlag) {
		result = nextMove;
		bombFlag=false;			
	}
	else {
		if(pathFlag) {
			if(pathIndex<pathMoves.size()) {
				result = pathMoves.get(pathIndex);
				pathIndex++;
				previousMove = result;
				newPosition = positionAfterMove(x, y, result);
				return result;
			}
			else {
				pathFlag=false;
				pathIndex=0;
				pathMoves.clear();
				path=null;
			}
			
		}
		if(bomb[0] != -1 ) {
			result = checkAndRunAwayFromBomb(bomb[0],bomb[1]);
		}
		else {
			Thread.sleep(10);
			if(x==MIDDLEX && y==MIDDLEY) {
				result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;					
			}
			else {
				int[] bonus = getClosestBlockPosition(x, y, ArtificialIntelligence.AI_BLOCK_ITEM_FIRE);
				map = new GameMap(getZoneMatrix());
				finder = new AStarPathFinder(map,MAXSEARCHDISTANCE,false,false);
				if(bonus[0]!=-1) {
					path = finder.findPath(x, y, bonus[0], bonus[1]);
				}
				else {
					path=finder.findPath(x, y, MIDDLEX, MIDDLEY);
				}
				
				if(path!=null) {
					loadMoves(path, pathMoves);
					pathFlag=true;
					pathIndex++;
					result=pathMoves.get(0);
					path=null;
				}			
				else {
					Vector<Integer> possibleMoves = getPossibleMoves(x,y);
					Vector<Integer> possibleMoves2 = new Vector<Integer>();
					Vector<Integer> possibleMoves3 = new Vector<Integer>();
					possibleMoves.remove(inverse(previousMove));
					int tempSize = possibleMoves.size();
					for(int i=0; i<tempSize; i++) {
						int[] temp = positionAfterMove(x, y, possibleMoves.get(i));
						int tempX = temp[0];
						int tempY = temp[1];
						Vector<Integer> tempPossibleMoves = getPossibleMoves(tempX,tempY);
						tempPossibleMoves.remove(inverse(possibleMoves.get(i)));
						if(tempPossibleMoves.size()!=0) {
							possibleMoves2.add(possibleMoves.get(i));
						}
						else {
							possibleMoves3.add(possibleMoves.get(i));
						}
					}
					possibleMoves2.remove(inverse(previousMove));
					
					if(possibleMoves2.size()==0) {
						result = getShortestMoveToMiddle(x,y,possibleMoves3);
						nextMove = ArtificialIntelligence.AI_ACTION_PUT_BOMB;
						bombFlag = true;
					}
					else {
						result = getShortestMoveToMiddle(x,y,possibleMoves2);
						if(result == inverse(previousMove)) {
							result = ArtificialIntelligence.AI_ACTION_PUT_BOMB;					
						}
					}				
				}
				}
			}											
		}							
	previousMove = result;
	newPosition = positionAfterMove(x, y, result);
		}
	return result;
}

	/**
	 * 
	 * @param bX
	 * @param bY
	 * @return
	 * 		?
	 * @throws InterruptedException
	 */
	private Integer  checkAndRunAwayFromBomb(int bX, int bY) throws InterruptedException {
		if(isRiskFromBomb(x,y,bX,bY)) {
			map = new GameMap(getZoneMatrix() );
			finder = new AStarPathFinder(map,MAXSEARCHDISTANCE,false,false);
			path = findRunAwayPathFromBomb(x,y,bX,bY);
			if(path!=null) {
				loadMoves(path, pathMoves);
				pathFlag=true;
				pathIndex++;
				Thread.sleep(180);
				return pathMoves.get(0);
			}
			else {
				return ArtificialIntelligence.AI_ACTION_DO_NOTHING;
			}							
		}
		else {
			return ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		}
		
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param bX
	 * @param bY
	 * @return
	 * 		?
	 */
	private Path findRunAwayPathFromBomb(int x, int y, int bX, int bY) {
		for(int index=0;index<=2*RUNINTERVAL;index++) {
			if(path!=null)
				break;
			for(int index2=0; index2<=2*RUNINTERVAL;index2++) {	
					if(isAvailable(x,y-index2,bX,bY)) {
						path = finder.findPath(x, y, x, y-index2);
						if(path!=null) 
							break;
					}
					
					if(isAvailable(x,y+index2,bX,bY)) {
						path = finder.findPath(x, y, x, y+index2);
						if(path!=null) 
							break;
					}
					if(isAvailable(x-index,y-index2,bX,bY)) {
						path = finder.findPath(x, y, x-index, y-index2);
						if(path!=null) 
							break;
					}
				
					if(isAvailable(x-index,y+index2,bX,bY)) {
						path = finder.findPath(x, y, x-index, y+index2);
						if(path!=null) 
							break;
					}
				
					if(isAvailable(x+index,y-index2,bX,bY)) {
						path = finder.findPath(x, y, x+index, y-index2);
						if(path!=null) 
							break;
					}
				
					if(isAvailable(x+index,y+index2,bX,bY)) {
						path = finder.findPath(x, y, x+index, y+index2);
						if(path!=null) 
							break;
					}
			}
		}
			return path;
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param bX
	 * @param bY
	 * @return
	 * 		?
	 */
	private boolean isAvailable(int x, int y, int bX, int bY) {
		try {
			int state = getZoneMatrix()[x][y];
			if(state ==0 || state == 5 || state == 6 ) {
				if(isRiskFromBomb(x,y,bX,bY)) 
					return false;
				else
					return true;
				}
			else 
				return false;
			
		} catch (Exception e) {
			return false;
		}						
	}
		
	/**
	 * 
	 * @param path
	 * @param pathMoves
	 */
	private void loadMoves(Path path, Vector<Integer> pathMoves) {
		int [] position = {-1,-1};
		int [] nextPosition = {-1,-1};
		for(int i=0; i<path.getLength()-1;i++) {
			position[0] = path.getX(i);
			position[1] = path.getY(i);
			nextPosition[0] = path.getX(i+1);
			nextPosition[1] = path.getY(i+1);
			pathMoves.add(getMove(position,nextPosition));
		}
	}

	/**
	 * 
	 * @param position
	 * @param nextPosition
	 * @return
	 * 		?
	 */
	private Integer getMove(int[] position, int[] nextPosition) {
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(position[0]>nextPosition[0])
			result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
		else if(position[0]<nextPosition[0])
			result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if(position[1]>nextPosition[1])
			result = ArtificialIntelligence.AI_ACTION_GO_UP;
		else if(position[1]<nextPosition[1])
			result = ArtificialIntelligence.AI_ACTION_GO_DOWN;			
		return result;
	}

	/**
	 * *
	 * @param x
	 * @param y
	 * @param bX
	 * @param bY
	 * @return
	 * 		?
	 */
	private boolean isRiskFromBomb(int x, int y, int bX, int bY) {
		int fire = getBombPowerAt(bX, bY);
		if((x==bX&&y<=bY+fire&&y>=bY-fire)||(y==bY&&x<=bX+fire&&x>=bX-fire))
			return true;
		else		
			return false;
	}

	/**
	 * @param x
	 * @param y
	 * @param blockType
	 * @return
	 * 		?
	 */
	private int[] getClosestBlockPosition(int x, int y, int blockType){
		int minDistance = 6;
		int result[] = {-1,-1}; 
		int[][] matrix = getZoneMatrix();
		for(int i=0;i<getZoneMatrixDimX();i++)
			for(int j=0;j<getZoneMatrixDimY();j++)
				if(matrix[i][j] == blockType)
				{	int tempDistance = distance(x,y,i,j); 	
					if(tempDistance<minDistance)
					{	minDistance = tempDistance;
						result[0] = i;
						result[1] = j;
					}
				}
		return result;
	}
	
	/**
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 * 		?
	 */
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param move
	 * @return
	 * 		?
	 */
	private boolean isMovePossible(int x, int y, int move)
	{	boolean result;
		// calcum
		switch(move)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				result = y>0 && !isObstacle(x,y-1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result = y<(getZoneMatrixDimY()-1) && !isObstacle(x,y+1);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result = x>0 && !isObstacle(x-1,y);
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result = x<(getZoneMatrixDimX()-1) && !isObstacle(x+1,y);
				break;
			default:
				result = false;
				break;
		}
		return result;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 * 		?
	 */
	private boolean isObstacle(int x, int y)
	{	int[][] matrix = getZoneMatrix();
		boolean result = false;
		// bombes
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
		// murs
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result || matrix[x][y]==ArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		result = result || (x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
		return result;
	}
	
	/**
	 * @param x
	 * @param y
	 * @return
	 * 		?
	 */
	private Vector<Integer> getPossibleMoves(int x, int y)
	{	Vector<Integer> result = new Vector<Integer>();
		for(int move=ArtificialIntelligence.AI_ACTION_GO_UP;move<=ArtificialIntelligence.AI_ACTION_GO_RIGHT;move++)
			if(isMovePossible(x,y,move))
				result.add(move);
		return result;
	}
	
	/**
	 * @param x
	 * @param y
	 * @param move
	 * @return
	 * 		?
	 */
	private int[] positionAfterMove(int x, int y, int move) {
		int[] result= {-1,-1};
		switch(move)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				result [0]=x;
				result [1]=y-1;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result [0]=x;
				result [1]=y+1;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result [0]=x-1;
				result [1]=y;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result [0]=x+1;
				result [1]=y;
				break;
			default:
				result[0] = -1;
				result[1] = -1;
				break;
		}		
		return result;		
	}
	
	/**
	 * @param x
	 * @param y
	 * @param possibleMoves
	 * @return
	 * 		?
	 */
	private Integer getShortestMoveToMiddle(int x,int y, Vector<Integer> possibleMoves) {
		int temp = Integer.MAX_VALUE;
		int tempX;
		int tempY;
		int result=0;
		for(int i=0; i<possibleMoves.size(); i++) {
			tempX = positionAfterMove(x, y, possibleMoves.get(i))[0];
			tempY = positionAfterMove(x, y, possibleMoves.get(i))[1];
			if(distance(tempX, tempY, 7, 7)<temp) {
				temp = distance(tempX, tempY, 9, 7);
				result = possibleMoves.get(i);
			}
		}
		
		return result;
	}	
	/**
	 * @param integer
	 * @return
	 * 		?
	 */
	private Integer inverse(Integer integer) {
		Integer result = 0;
		switch(integer)
		{	case ArtificialIntelligence.AI_ACTION_GO_UP:
				result= ArtificialIntelligence.AI_ACTION_GO_DOWN;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_DOWN:
				result= ArtificialIntelligence.AI_ACTION_GO_UP;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_LEFT:
				result= ArtificialIntelligence.AI_ACTION_GO_RIGHT;
				break;
			case ArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result= ArtificialIntelligence.AI_ACTION_GO_LEFT;
				break;
			default:
				result = 0;
				break;
		}		
		return result;		
	}
}
