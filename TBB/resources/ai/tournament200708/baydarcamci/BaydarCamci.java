package tournament200708.baydarcamci;

import java.util.ArrayList;
import java.util.Vector;

import tournament200708.InterfaceArtificialIntelligence;



/**
 * Classe impl�mentant un comportement non-violent : le robot
 * ne pose jamais de bombe, et essaie juste de s'�loigner le plus
 * possible des dangers existants, i.e. par ordre de dangerosit�
 * croissante : le feu, les bombes, les autres joueurs. 
 */
public class BaydarCamci extends InterfaceArtificialIntelligence
{
		private static final long serialVersionUID = 1L;
   
   
    /** derni�re mouvement du personnage */
    private Integer lastMove;
    
    /** mouvement precedent du personnage */
    private Integer lastPreMove;
    
    int thePutBomb[] = {-1,-1}; //yol acmak icin bomba biraktiginda eski pozisyonuna geri don
    int runAwayStep = 0; 
    public int bombPower =0 ;
    
    public ArrayList<int[]> path = new ArrayList<int[]>();
    
    
    /**
	 * Constructeur.
	 */
	public BaydarCamci()
	{	
		super("BaydarCamc");	
	}
	
	public Integer call() throws Exception
	{	
		
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		int dangerPos[] = getClosestBlockPosition(x,y,AI_BLOCK_FIRE);
		if(dangerPos[0]+1 != x || dangerPos[0]-1 != 0 || dangerPos[1]+1 != y || dangerPos[1]-1 != y )
			{
			dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
			bombPower =getBombPowerAt(dangerPos[0],dangerPos[1]);
			}
		if( dangerPos[0]!=-1) 
			return onDanger(x,y,dangerPos[0],dangerPos[1],bombPower);
		if(dangerPos[0] == -1)
		{
			result= moveNow(x,y);
		}
		
	return result;	
	}	
	
	/**
	 * Controle la mouvement du personnage par l'algorithm A*.
	 * il construire une path pour aller au milieu de la zone.
	 * @param x : la position x de personnage
	 * @param y : la position y de personnage
	 * @return la mouvement possible
	 */
	public Integer moveNow(int x, int y)
	{
		
		AStar as = new AStar(getZoneMatrix(), 500);
		int[] aim = {getZoneMatrixDimX()/2, getZoneMatrixDimY()/2};
		as.findPath(getOwnPosition(),aim , path);
		//on ne peut pas initialiser le path d'algorithm A*, alors:
		
		return dummyMethod(x,y);
		
	}
	
	
	public Integer onDanger(int x, int y, int bombX, int bombY,int bombPower)
	{
		if(x==bombX && bombY< y)
		{if(bombY+bombPower > y)
			{
			return downBomb(x,y);
			}
		}
		if(x==bombX && bombY>y)
		{if(bombY-bombPower < y)
			{
			return upBomb(x,y);
			}
		}
		if(bombY==y && bombX>x )
		{if(bombX-bombPower < x)
			{
			return leftBomb(x,y);
			}
		}
		if( bombY== y && bombX < x) 
		{
			if(bombX+bombPower > x )
			{
				return rightBomb(x,y);
				
			}
		}
 		else if(x==bombX && y==bombY)
 		{
 			return onBomb(x,y);
 		}
 	return  getPutBombPosition(x,y);
 	}
	

		
	
	/**
	 * Parmi les blocs dont le type correspond � la valeur blockType
	 * pass�e en param�tre, cette m�thode cherche lequel est le plus proche
	 * du point de coordonn�es (x,y) pass�es en param�tres. Le r�sultat
	 * prend la forme d'un tableau des deux coordon�es du bloc le plus proche.
	 * Le tableau est contient des -1 s'il n'y a aucun bloc du bon type dans la zone de jeu.
	 * @param x	position de r�f�rence
	 * @param y	position de r�f�rence
	 * @param blockType	le type du bloc recherch�
	 * @return	les coordonn�es du bloc le plus proche
	 */
	private int[] getClosestBlockPosition(int x, int y, int blockType)
	{	int minDistance = Integer.MAX_VALUE;
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
	 * Calcule et renvoie la distance de Manhattan 
	 * (cf. : http://fr.wikipedia.org/wiki/Distance_%28math%C3%A9matiques%29)
	 * entre le point de coordonn�es (x1,y1) et celui de coordonn�es (x2,y2). 
	 * @param x1	position du premier point
	 * @param y1	position du premier point
	 * @param x2	position du second point
	 * @param y2	position du second point
	 * @return	la distance de Manhattan entre ces deux points
	 */
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}



	/**
	 * Indique si le d�placement dont le code a �t� pass� en param�tre 
	 * est possible pour un personnage situ� en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le d�placement � �tudier
	 * @return	vrai si ce d�placement est possible
	 */
	public boolean isMovePossible(int x, int y, int move)
	{	boolean result;
		// calcum
		switch(move)
		{	case InterfaceArtificialIntelligence.AI_ACTION_GO_UP:
				result = y>0 && !isObstacle(x,y-1);
				break;
			case InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN:
				result = y<(getZoneMatrixDimY()-1) && !isObstacle(x,y+1);
				break;
			case InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT:
				result = x>0 && !isObstacle(x-1,y);
				break;
			case InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT:
				result = x<(getZoneMatrixDimX()-1) && !isObstacle(x+1,y);
				break;
			default:
				result = false;
				break;
		}
		return result;
	}

	/**
	 * Indique si la case situ�e � la position pass�e en param�tre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position � �tudier
	 * @param y	position � �tudier
	 * @return	vrai si la case contient un obstacle
	 */
	private boolean isObstacle(int x, int y)
	{	int[][] matrix = getZoneMatrix();
		boolean result = false;
		// bombes
		result = result || matrix[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_BOMB;
		// feu
		result = result || matrix[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_FIRE;
		// murs
		result = result || matrix[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_WALL_HARD;
		result = result || matrix[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_WALL_SOFT;
		// on ne sait pas quoi
		result = result || matrix[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_UNKNOWN;
		// shrink
		result = result || (x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
		return result;
	}




	/**
	 * Renvoie la liste de tous les d�placements possibles
	 * pour un personnage situ� � la position (x,y)
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @return	la liste des d�placements possibles
	 */
	public Vector<Integer> getPossibleMoves(int x, int y)
	{	Vector<Integer> result = new Vector<Integer>();
		for(int move=AI_ACTION_GO_UP;move<=AI_ACTION_GO_RIGHT;move++)
			if(isMovePossible(x,y,move))
				result.add(move);
		return result;
	}

	public Integer dummyMethod(int x , int y)
	{
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		//les locations du personnage au debut du jeu
		if(x<=8 && y<=7)
			result= solUstKose(x,y);
		if(x>8 && y<=7)
			result= sagUstKose(x,y);
		if(x<=8 && y>=7)
			result= solAltKose(x,y);
		if(x>8 && y>=7)
			result= sagAltKose(x,y);
		if(x==8 && y==7 )
			result = merkez(x,y);
		return result;
		
		
	}
	
	/**
	 * moi et la bombe et dans le meme cas
	 * @param x, mon x
	 * @param y, mon y
	 * @return mouvement
	 */
	public Integer onBomb(int x, int y)
	{
		if(getBombPosition() == InterfaceArtificialIntelligence.AI_DIR_DOWN)
		{
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		    else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT)==true)	
			    return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		    else 
				return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		   
		}
		if(getBombPosition() == InterfaceArtificialIntelligence.AI_DIR_UP)
		{	if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN) == true)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		    else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT)==true)	
			    return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		    else 
				return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		    
		}	
		if(getBombPosition() == InterfaceArtificialIntelligence.AI_DIR_LEFT)
		{	if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT) == true)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		    else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP)==true)	
			    return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		    else
				return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		    
		}
		if(getBombPosition() == InterfaceArtificialIntelligence.AI_DIR_RIGHT)
		{	if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT) == true)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		    else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP)==true)	
			    return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		    else
				return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		}	
		if(getBombPosition()== InterfaceArtificialIntelligence.AI_DIR_NONE)
		{	
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP) == true)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT)==true)
					return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN)==true)
					return  InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT)==true)	
				    return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		}
		
		return  InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
			
	
	
		
	}
	

	/**
	 * 
	 * @param x mon x 
	 * @param y mon y
	 * @return la mouvement 
	 */
	public Integer solUstKose(int x, int y)
	{
		Integer result = solUstKoseHareketleri(x,y); //rutin durumsa zigzaglar cizerek merkeze ulasmaya calis
		result = Move(x,y,result);
		lastPreMove = lastMove;
		lastMove= result;
		
		return result;
	}
	/**
	 * 
	 * @param x mon x 
	 * @param y mon y
	 * @return la mouvement 
	 */
	public Integer sagUstKose(int x, int y)
	{
		Integer result = sagUstKoseHareketleri(x,y); //rutin durumsa zigzaglar cizerek merkeze ulasmaya calis
		return Move(x,y,result);
		
	}
	/**
	 * 
	 * @param x mon x 
	 * @param y mon y
	 * @return la mouvement 
	 */
	public Integer solAltKose(int x, int y)
	{
		Integer result = solAltKoseHareketleri(x,y); //rutin durumsa zigzaglar cizerek merkeze ulasmaya calis
		return Move(x,y,result);
		
	}
	/**
	 * 
	 * @param x mon x 
	 * @param y mon y
	 * @return la mouvement 
	 */
	public Integer sagAltKose(int x, int y)
	{
		Integer result = sagAltKoseHareketleri(x,y); //rutin durumsa zigzaglar cizerek merkeze ulasmaya calis
		return Move(x,y,result);
		
	}
	/**
	 * 
	 * @param x mon x 
	 * @param y mon y
	 * @param result mouvement deja obtenu 
	 * @return la mouvement 
	 */
	public Integer Move(int x, int y, Integer result)
	{

		lastMove = result;
		
		//s'il y une danger?
		int dangerPos[] = getClosestBlockPosition(x,y,AI_BLOCK_FIRE);
		if( dangerPos[0] == -1)
			dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
		
		if(lastMove== InterfaceArtificialIntelligence.AI_ACTION_PUT_BOMB)
		{
			if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_UP)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
			if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
			if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
			if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT)
				return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
			lastMove= InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
			lastPreMove= InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		}
		
		if(dangerPos[0]== -1)
		{	
			dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
			bombPower = getBombPowerAt(dangerPos[0],dangerPos[1]);
		}
		
		if(dangerPos[0] == -1 && dangerPos[0]==-1 && result ==InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING)
		{   
			//aucune danger. putBomb:
			thePutBomb = getOwnPosition();
			lastPreMove = lastMove;
			result= InterfaceArtificialIntelligence.AI_ACTION_PUT_BOMB;
			
			
		}
		
		else if(result ==InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING)
		{
			//il y une danger. choisir votre deplacement:
			int bombDir = bombaNerde(x,y,dangerPos[0],dangerPos[1]);
			if(bombDir==5)
				return onBomb(x,y);
			if(bombDir == 6)
				return rightBomb(x,y);
			if(bombDir==4)
				return leftBomb(x,y);
			if(bombDir == 2)
				return downBomb(x,y);
			if(bombDir == 8)
				return upBomb(x,y);
		}
		return result;
	}
	
	//moi est au milieu de la zone.
	public Integer merkez(int x, int y)
	{
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(getTimeBeforeShrink() == -1) 
		{
			// on d�termine les d�placements possibles
			Vector<Integer> possibleMoves = getPossibleMoves(x,y);
			// on teste s'il est possible d'effectuer le m�me d�placement que pr�c�demment
			if(possibleMoves.contains(lastMove))
				result = lastMove;
			// sinon : soit on se d�place, soit on pose une bombe
			else if(possibleMoves.size()>0)
			{	// on peut poser une bombe si on est � la fois dans un cul de sac 
				// (1 seul d�placement possible) et sur une case vide
				if(possibleMoves.size()<2 && getZoneMatrix()[x][y]==InterfaceArtificialIntelligence.AI_BLOCK_EMPTY)
					possibleMoves.add(InterfaceArtificialIntelligence.AI_ACTION_PUT_BOMB);
				// on d�termine al�atoirement l'action qui va �tre effectu�e
				int index;
				do
				{	index = (int)(Math.random()*(possibleMoves.size()));			
				}
				while(index==possibleMoves.size());
				result = possibleMoves.get(index);
			}
			lastMove = result;
			return result;
		}
			
		return result;
	}
	
	//la mouvement du personnage s'il commence au jeu au haut gauche de la zone
	public Integer solUstKoseHareketleri(int x, int y)
	{
		
	    Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(thePutBomb[0] == -1 )
		{
			if(x==7 && y==7)
				return result = InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		}
		else 
		{
			if(x !=thePutBomb[0] || y != thePutBomb[1])
			{
				return getPutBombPosition(x,y);
			}
			thePutBomb[0]= -1;
		}
		return result;
	}
	
	//la mouvement du personnage s'il commence au jeu au haut droit de la zone
	public Integer sagUstKoseHareketleri(int x, int y)
	{
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(thePutBomb[0] == -1 )
		{
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		}
		/*else
		{
			if(x !=thePutBomb[0] || y != thePutBomb[1])
			{
				return getPutBombPosition(x,y);
			}
			thePutBomb[0]= -1;
		}*/
		return result;
	}
	
	//la mouvement du personnage s'il commence au jeu au bas gauche de la zone
	public Integer solAltKoseHareketleri(int x, int y)
	{
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(thePutBomb[0] == -1 )
		{
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		} 
		else
		{
			if(x !=thePutBomb[0] || y != thePutBomb[1])
			{
				return getPutBombPosition(x,y);
			}
			thePutBomb[0]= -1;
		}
		return result;
	}
	
	//la mouvement du personnage s'il commence au jeu au bas droit de la zone
	public Integer sagAltKoseHareketleri(int x, int y)
	{
		Integer result = InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(thePutBomb[0] == -1 )
		{
			if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
			else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP))
				result = InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		}
		else
		{
			if(x !=thePutBomb[0] || y != thePutBomb[1])
			{
				return getPutBombPosition(x,y);
			}
			thePutBomb[0]= -1;
		}
		return result;
	}
	
	
	//la position du bombe par rapport a moi
	public int bombaNerde(int x, int y, int bombX, int bombY)
	{
		//bomba sagimdaysa 6 solumdaysa 4 yukardaysa 8 asagidaysa 2 altimdaysa 5  
	
		if(x<bombX && y== bombY)
			return 6;
		if(x>bombX && y== bombY)
			return 4;
		if(x==bombX && y>bombY)
			return 8;
		if(x==bombX && y<bombY)
			return 2;
		if(x==bombX && y==bombY)
			return 5;
	
				
		 	
		return solUstKoseHareketleri(x,y);
	}
	
	/**
	 * bombe situe au droite
	 * @param x mon x
	 * @param y mon y
	 * @return
	 */
	public Integer rightBomb(int x,int y)
	{
		if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		else
			return  InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
	}
	/**
	 * bombe se situe au gauche
	 * @param x, mon x
	 * @param y, mon y
	 * @return
	 */
	public Integer leftBomb(int x,int y)
	{
		if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_UP))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		else
			return  InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
	}
	
	/**
	 * bombe est au dessous de moi
	 * @param x mon x
	 * @param y mon y
	 * @return
	 */
	public Integer downBomb(int x,int y)
	{
		if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		else
			return  InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
	}
	/**
	 * bombe est au dessus de moi
	 * @param x mon x
	 * @param y mon y
	 * @return
	 */
	public Integer upBomb(int x,int y)
	{
		if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		else if(isMovePossible(x,y,InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT))
			return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		else
			return  InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
	}
	



		
	

	//se deplace au case de bombe
	public Integer getPutBombPosition(int x, int y)
	{
		if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN)
			return InterfaceArtificialIntelligence.AI_ACTION_GO_UP;
		if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_UP)
			return InterfaceArtificialIntelligence.AI_ACTION_GO_DOWN;
		if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT)
			return InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT;
		if(lastPreMove == InterfaceArtificialIntelligence.AI_ACTION_GO_RIGHT)
			return InterfaceArtificialIntelligence.AI_ACTION_GO_LEFT;
		return InterfaceArtificialIntelligence.AI_ACTION_DO_NOTHING;
		
	}
	
	

	
	
		
		
	

	}




