package org.totalboumboum.ai.v200708.ais.goksaykose;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Deniz Goksay
 * @author Ipek Kose
 *
 */
public class GoksayKose extends ArtificialIntelligence{
	private static final long serialVersionUID = 1L;
	/**true si la case est occup� par un bombe**/ 
	private boolean Flag;
	/**
	 * constructeur
	 */
	public GoksayKose() {
		super("GoksayKose");
		Flag=false;
			}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	public Integer call() throws Exception {
		
		Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	
		// position du personnage
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		
		// on détermine où est le feu le plus proche
		int dangerPos[] = getClosestBlockPosition(x,y,AI_BLOCK_FIRE);
		// si aucun feu, on détermine où est la bombe la plus proche
		if(dangerPos[0]==-1)
			dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
		if(dangerPos[0]==-1)
			
		// si aucune bombe, on détermine la prochaine deplacement
		{
			//on détermine la premiere position de IA
			//s'il est au coin gauche au dessus du zone
			if(x==1 && y==1)
				result= coinDessusGauche(x,y);
			
			//s'il est au coin droit au dessus du zone
			if(x==15 && y==1)
				result= coinDessusDroit(x,y);
			
			//s'il est au coin gauche au dessous du zone
			if(x==1 && y==13)
				result= coinDessousGauche(x,y);
			
			//s'il est au coin droit au dessous du zone
			if(x==15 && y==13)
				result= coinDessousDroit(x,y);
			
			}	
		}
		return result;
	}
	
	/**
	 * 
	 * @param x l'abscisse du IA au coin gauche au dessus
	 * @param y l'ordonné du IA au coin gauche au dessus
	 * @return le cas du mouvement qui sera effectué
	 */
	public Integer coinDessusGauche(int x, int y)
	{
		Integer result = mouvementCoinDessusGauche(x,y); 
		return result;
		
	}
	/**
	 * 
	 * @param x l'abscisse du IA au coin gauche au dessus
	 * @param y l'ordonné du IA au coin gauche au dessus
	 * @return le mouvement qui sera effectué si IA est au coin gauche au dessus
	 */

	public int mouvementCoinDessusGauche(int x, int y)
	{
		
			int result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
			Vector<Integer> vektor =getPossibleMoves(x, y);
			if(x==7){
				
				if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
						result = ArtificialIntelligence.AI_ACTION_GO_DOWN;
					else
						result=metBombeFuir(x,y);
				}

			else
			{
				if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
					result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
				else
					result=metBombeFuir(x,y);
		
			}
		
		return result;
	}
	
	
	/**
	 * 
	 * @param x l'abscisse du IA au coin droit au dessus
	 * @param y l'ordonné du IA au coin droit au dessus
	 * @return le cas du mouvement qui sera effectué
	 */
	public Integer coinDessusDroit(int x, int y)
	{
		Integer result = mouvementCoinDessusDroit(x,y); 
		return result;
		
	}
	/**
	 * 
	 * @param x l'abscisse du IA au coin droit au dessus
	 * @param y l'ordonné du IA au coin droit au dessus
	 * @return le mouvement qui sera effectué si IA est coin droit au dessus
	 */
	
	public int mouvementCoinDessusDroit(int x, int y)
	{
		int result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		Vector<Integer> vektor =getPossibleMoves(x, y);
		if(x==7){
			
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_DOWN))
					result = ArtificialIntelligence.AI_ACTION_GO_DOWN;
				else
					result=metBombeFuir(x,y);
			}
		else
		{
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
		    result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else 
		    result=metBombeFuir(x,y);
		
		}
		return result;
	}
	/**
	 * 
	 * @param x l'abscisse du IA au coin gauche au dessous
	 * @param y l'ordonné du IA au coin gauche au dessous
	 * @return le cas du mouvement qui sera effectué
	 */
	public Integer coinDessousGauche(int x, int y)
	{
		Integer result = mouvementCoinDessousGauche(x,y); 
		return result;
		
	}
	/**
	 * 
	 * @param x l'abscisse du IA au coin gauche au dessous
	 * @param y l'ordonné du IA au coin gauche au dessous
	 * @return le mouvement qui sera effectué si IA est coin gauche au dessous
	 */
	
	public int mouvementCoinDessousGauche(int x, int y)
	{
		int result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		Vector<Integer> vektor =getPossibleMoves(x, y);
		if(x==7){
			
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
					result = ArtificialIntelligence.AI_ACTION_GO_UP;
				else
					result=metBombeFuir(x,y);
			}
		else
		{
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_RIGHT))
		    result = ArtificialIntelligence.AI_ACTION_GO_RIGHT;
			else 
		    result=metBombeFuir(x,y);
		
		}
		return result;
	}
	
	/**
	 * 
	 * @param x l'abscisse du IA au coin droit au dessous
	 * @param y l'ordonné du IA au coin droit au dessous
	 * @return le cas du mouvement qui sera effectué
	 */
	public Integer coinDessousDroit(int x, int y)
	{
		Integer result = mouvementCoinDessousDroit(x,y); 
		return result;
		
	}
	/**
	 * 
	 * @param x l'abscisse du IA au coin droit au dessous
	 * @param y l'ordonné du IA au coin droit au dessous
	 * @return le mouvement qui sera effectué si IA est au coin droit au dessous
	 */
	
	public int mouvementCoinDessousDroit(int x, int y)
	{
		int result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		Vector<Integer> vektor =getPossibleMoves(x, y);
		if(x==7){
			
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_UP))
					result = ArtificialIntelligence.AI_ACTION_GO_UP;
				else
					result=metBombeFuir(x,y);
			}
		else
		{
			if(vektor.contains(ArtificialIntelligence.AI_ACTION_GO_LEFT))
		    result = ArtificialIntelligence.AI_ACTION_GO_LEFT;
			else 
		    result=metBombeFuir(x,y);
		
		}
		return result;
	}
	/**
	 * IA mets une bombe et s'enfuit du case
	 * @param x l'abscisse du IA
	 * @param y l'ordonné du IA
	 * @return l'action qui sera effectuer 
	 */
	private int metBombeFuir(int x,int y) {
    	
    	int action=ArtificialIntelligence.AI_ACTION_PUT_BOMB;
    	Flag=true;
    	if(Flag) {
			action = fuir(x, y);
			Flag=false;			
		}
		
    	return action;
    }
	
	/**
	 * IA  s'enfuit d'une bombe et flamme
	 * @param x l'abscisse du IA
	 * @param y l'ordonné du IA
	 * @return l'action qui sera effectué 
	 */
			private int fuir(int x,int y) {
				int result=ArtificialIntelligence.AI_ACTION_DO_NOTHING;
				int bomb[] = getClosestBlockPosition(x, y, ArtificialIntelligence.AI_BLOCK_BOMB);
				int portee=0;
			    //prend en parametre l'abscisse et l'ordonné du bombe et retourne la port�e du bombe
				portee = getBombPowerAt(bomb[0], bomb[1]);
				//si IA se trouve sur une case qui explosera par la bombe
				if(x<bomb[0]+portee||x>bomb[0]-portee&&y<bomb[1]+portee&&y>bomb[1]-portee) {
					int directions[] = getDirectionPreferences(x, y, bomb[0], bomb[1]);
					int i=0;
					while(i<directions.length && result==ArtificialIntelligence.AI_ACTION_DO_NOTHING)
					{	if(isMovePossible(x, y, directions[i]))
							result = directions[i];
						else
							i++;
					}
	    	    
			}
				 return result;
	}
	
			/**
			 * détermine un ordre de préférence sur toutes les directions possibles :
			 * plus la direction permet de s'�loigner du danger,
			 * plus elle est pr�f�r�e.
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
	 * Indique si la case situ�e à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à �tudier
	 * @param y	position à �tudier
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
    
	/**
	 * Renvoie la liste de tous les déplacements possibles
	 * pour un personnage situ� à la position (x,y)
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @return	la liste des déplacements possibles
	 */
	
	private Vector<Integer> getPossibleMoves(int x, int y)
	{	Vector<Integer> result = new Vector<Integer>();
		for(int move=AI_ACTION_GO_UP;move<=AI_ACTION_GO_RIGHT;move++)
			if(isMovePossible(x,y,move))
				result.add(move);
		return result;
	}
	
	/**
	 * Indique si le déplacement dont le code a été passé en paramètre 
	 * est possible pour un personnage situ� en (x,y).
	 * @param x	position du personnage
	 * @param y position du personnage
	 * @param move	le déplacement à �tudier
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
	 * Parmi les blocs dont le type correspond à la valeur blockType
	 * passée en paramètre, cette méthode cherche lequel est le plus proche
	 * du point de coordonnées (x,y) passées en paramètres. Le r�sultat
	 * prend la forme d'un tableau des deux coordon�es du bloc le plus proche.
	 * Le tableau est contient des -1 s'il n'y a aucun bloc du bon type dans la zone de jeu.
	 * @param x	position de référence
	 * @param y	position de référence
	 * @param blockType	le type du bloc recherch�
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
	
}
