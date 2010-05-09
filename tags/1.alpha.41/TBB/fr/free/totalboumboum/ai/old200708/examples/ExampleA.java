package fr.free.totalboumboum.ai.old200708.examples;

import fr.free.totalboumboum.ai.old200708.ArtificialIntelligence;

/**
 * Classe implémentant un comportement non-violent : le robot
 * ne pose jamais de bombe, et essaie juste de s'éloigner le plus
 * possible des dangers existants, i.e. par ordre de dangerosité
 * croissante : le feu, les bombes, les autres joueurs. 
 */
public class ExampleA extends ArtificialIntelligence
{	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur.
	 */
	public ExampleA()
	{	super("Panique");	
	}

	public Integer call() throws Exception
	{	Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		// position du personnage
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		// on détermine où est le feu le plus proche
		int dangerPos[] = getClosestBlockPosition(x,y,AI_BLOCK_FIRE);
		// si aucun feu, on détermine où est la bombe la plus proche
		if(dangerPos[0]==-1)
			dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
		// si aucune bombe, on détermine où est le joueur le plus proche
		if(dangerPos[0]==-1)
			dangerPos = getClosestPlayerPosition(x,y);
		// s'il y a au moins un danger : 
		if(dangerPos[0]!=-1)
		{	// on calcule l'ordre des directions
			int temp[] = getDirectionPreferences(x, y, dangerPos[0], dangerPos[1]);
			// on sélectionne une direction possible
			int i=0;
			while(i<temp.length && result==ArtificialIntelligence.AI_ACTION_DO_NOTHING)
			{	if(isMovePossible(x, y, temp[i]))
					result = temp[i];
				else
					i++;
			}
			// si aucun déplacement n'est possible : on place une bombe
			if(result==ArtificialIntelligence.AI_ACTION_DO_NOTHING)
				result = ArtificialIntelligence.AI_ACTION_PUT_BOMB;
		}		
		//
		return result;
	}
	
	/**
	 * Parmi les blocs dont le type correspond à la valeur blockType
	 * passée en paramètre, cette méthode cherche lequel est le plus proche
	 * du point de coordonnées (x,y) passées en paramètres. Le résultat
	 * prend la forme d'un tableau des deux coordonées du bloc le plus proche.
	 * Le tableau est contient des -1 s'il n'y a aucun bloc du bon type dans la zone de jeu.
	 * @param x	position de référence
	 * @param y	position de référence
	 * @param blockType	le type du bloc recherché
	 * @return	les coordonnées du bloc le plus proche
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
	 * entre le point de coordonnées (x1,y1) et celui de coordonnées (x2,y2). 
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
	 * Détermine un ordre de préférence sur toutes les directions possibles :
	 * plus la direction permet de s'éloigner du danger,
	 * plus elle est préférée.
	 * @param x	position du personnage
	 * @param y	position du personnage
	 * @param dangerX	position du danger
	 * @param dangerY	position du danger
	 * @return	liste des directions ordonnée par préférence 
	 */
	private int[] getDirectionPreferences(int x, int y, int dangerX, int dangerY)
	{	int result[] = new int[4];
		int indexUp, indexDn, indexLt, indexRt;
		if(Math.abs(x-dangerX)>Math.abs(y-dangerY))
		{	if(y<dangerY)
			{	indexUp = 0;
				indexDn = 3;
			}
			else
			{	indexUp = 3;
				indexDn = 0;
			}
			if(x<dangerX)
			{	indexLt = 1;
				indexRt = 2;
			}
			else
			{	indexLt = 2;
				indexRt = 1;
			}
		}
		else
		{	if(x<dangerX)
			{	indexLt = 0;
				indexRt = 3;
			}
			else
			{	indexLt = 3;
				indexRt = 0;
			}
			if(y<dangerY)
			{	indexUp = 1;
				indexDn = 2;
			}
			else
			{	indexUp = 2;
				indexDn = 1;
			}
		}
		result[indexUp] = new Integer(ArtificialIntelligence.AI_ACTION_GO_UP);
		result[indexDn] = new Integer(ArtificialIntelligence.AI_ACTION_GO_DOWN);
		result[indexLt] = new Integer(ArtificialIntelligence.AI_ACTION_GO_LEFT);
		result[indexRt] = new Integer(ArtificialIntelligence.AI_ACTION_GO_RIGHT);
		return result;
	}
	
	/**
	 * Renvoie la position du personnage le plus proche de la
	 * position passée en paramètres. 
	 * @param x	position de référence
	 * @param y	position de référence
	 * @return	position du joueur le plus proche
	 */
	private int[] getClosestPlayerPosition(int x, int y)
	{	int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		for(int i=0;i<getPlayerCount();i++)
		{	int pos[] = getPlayerPosition(i);
			int temp = distance(x,y,pos[0],pos[1]);
			if(temp<minDistance)
			{	result[0] = pos[0];
				result[1] = pos[1];
				minDistance = temp;
			}
		}
		return result;
	}

	/**
	 * Indique si le déplacement dont le code a été passé en paramètre 
	 * est possible pour un personnage situé en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le déplacement à étudier
	 * @return	vrai si ce déplacement est possible
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
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
	 * @return	vrai si la case contient un obstacle
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
}
