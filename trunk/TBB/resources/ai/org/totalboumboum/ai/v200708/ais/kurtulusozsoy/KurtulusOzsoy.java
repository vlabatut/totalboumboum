package org.totalboumboum.ai.v200708.ais.kurtulusozsoy;

import java.util.Random;
import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Mehmet Kurtuluş
 * @author Yalcin Özsoy
 *
 */
@SuppressWarnings("deprecation")
public class KurtulusOzsoy extends ArtificialIntelligence{
	

	
	
		private static final long serialVersionUID = 1L;
		/** le dernier déplacement effectué */
		private Integer lastMove;
		
		
		
		private int midX;
		private int midY;
		int dist;
		int posit[] = {-1,-1};
		
		/**
		 * Constructeur.
		 */
		
		public KurtulusOzsoy()
		{	super("KurtlsOzsy");
			lastMove = ArtificialIntelligence.AI_ACTION_GO_LEFT;
			
		}
		
		/** indicateur de première invocation (pour la compatibilité */
		private boolean firstTime = true;

		public Integer call() throws Exception
		{	Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		if(firstTime)
			firstTime = false;
		else
		{	
			int x = getOwnPosition()[0];
			int y = getOwnPosition()[1];
			posit=getClosestPlayerPosition(x, y);
			dist=distance(x,y,posit[0],posit[1]);
			if(dist==1)
			{   
				int temp[] = getDirectionPreferences(x, y, posit[0],posit[1]);
			// on sélectionne une direction possible
			int i=0;
			while(i<temp.length && result==ArtificialIntelligence.AI_ACTION_DO_NOTHING)
			{	if(isMovePossible(x, y, temp[i]))
			          result = temp[i];
			          
				else
					i++;
			}
				
				
			}
			if(dist==0)
			{
				result=AI_ACTION_PUT_BOMB;
				
			}
			int dangerPos[] = getClosestBlockPosition(x,y,AI_BLOCK_FIRE);
			// si aucun feu, on détermine où est la bombe la plus proche
			if(dangerPos[0]==-1)
				dangerPos = getClosestBlockPosition(x,y,AI_BLOCK_BOMB);
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
			}		
			else{
			if(getTimeBeforeShrink()<7000){
				 midX=getZoneMatrixDimX()/2;
					midY=getZoneMatrixDimY()/2;
			return	result=goMiddle(x, y);
			}	
			else{	// on détermine les déplacements 
				
				
				
				result=getBonus(x, y, getClosestBonusPosition(x, y, AI_BLOCK_ITEM_FIRE)[0], getClosestBonusPosition(x, y, AI_BLOCK_ITEM_FIRE)[1]);
				if(result==AI_ACTION_DO_NOTHING)
				 {
				  result=getBonus(x, y, getClosestBonusPosition(x, y, AI_BLOCK_ITEM_BOMB)[0], getClosestBonusPosition(x, y, AI_BLOCK_ITEM_BOMB)[1]);
				 }
				 if(result==AI_ACTION_DO_NOTHING)
				 {
				  result=move(x, y);
				 }
				}
			
		
			}	
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
			result = result ||
	 matrix[x][y]==ArtificialIntelligence.AI_BLOCK_BOMB;
			// feu
			result = result ||
	 matrix[x][y]==ArtificialIntelligence.AI_BLOCK_FIRE;
			// murs
			result = result ||
	 matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_HARD;
			result = result ||
	 matrix[x][y]==ArtificialIntelligence.AI_BLOCK_WALL_SOFT;
			// on ne sait pas quoi
			result = result ||
	 matrix[x][y]==ArtificialIntelligence.AI_BLOCK_UNKNOWN;
			// shrink
			result = result || (x==getNextShrinkPosition()[0] &&
	 y==getNextShrinkPosition()[1]);
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
		 * Renvoie la liste de tous les déplacements possibles
		 * pour un personnage situé à la position (x,y)
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
		private int distance(int x1,int y1,int x2,int y2)
		{	int result = 0;
			result = result + Math.abs(x1-x2);
			result = result + Math.abs(y1-y2);
			return result;
		}
	private int[] getDirectionPreferences(int x, int y, int dangerX, int dangerY)
		//On decide vers quelle direction notre personnage va avancer (ou reculer)
		{ if((Math.abs(x-dangerX)==0 &&(Math.abs(y-dangerY)<=getBombPowerAt(dangerX,dangerY)))||( Math.abs(y-dangerY)==0 &&(Math.abs(x-dangerX)<=getBombPowerAt( dangerX,  dangerY))))
				{	
			int result[] = new int[4];
			int indexUp, indexDn, indexLt, indexRt;
			if(Math.abs(y-dangerY)!=0){
				indexLt=0;
				indexRt=1;
				if(y-dangerY<0){
					indexUp=2;
					indexDn=3;
				}
				else{
					indexDn=2;
					indexUp=3;
				}
			} 
			else if(y-dangerY==0 &&x-dangerX==0){
				if(isMovePossible(x, y,AI_ACTION_GO_UP)&&(isMovePossible(x, y-1,AI_ACTION_GO_UP)||isMovePossible(x, y-1,AI_ACTION_GO_LEFT) || isMovePossible(x, y-1,AI_ACTION_GO_RIGHT))){
					indexUp=0;
					indexDn=1;
					indexLt=2;
					indexRt=3;
				}
				else if(isMovePossible(x, y,AI_ACTION_GO_DOWN)&&(isMovePossible(x, y+1,AI_ACTION_GO_DOWN)||isMovePossible(x, y+1,AI_ACTION_GO_LEFT) || isMovePossible(x, y+1,AI_ACTION_GO_RIGHT))){
				indexDn=0;
				indexUp=1;
				indexLt=2;
				indexRt=3;
				}
				else if(isMovePossible(x, y,AI_ACTION_GO_RIGHT)&&(isMovePossible(x+1, y,AI_ACTION_GO_DOWN)||isMovePossible(x+1, y,AI_ACTION_GO_UP) || isMovePossible(x+1, y,AI_ACTION_GO_RIGHT))){
					indexRt=0;
					indexDn=2;
					indexUp=3;
					indexLt=1;
				}
				else{
					indexLt=0;
					indexDn=3;
					indexUp=2;
					indexRt=1;
				}
			}
			else{
				indexUp=0;
			    indexDn=1;
			
		
				if(x-dangerX<0){
					indexLt=2;
					indexRt=3;
				}
				else{
					indexLt=3;
					indexRt=2;
				}
			}
			result[indexUp] = new
	        Integer(ArtificialIntelligence.AI_ACTION_GO_UP);
			result[indexDn] = new
	        Integer(ArtificialIntelligence.AI_ACTION_GO_DOWN);
			result[indexLt] = new
	        Integer(ArtificialIntelligence.AI_ACTION_GO_LEFT);
			result[indexRt] = new
	        Integer(ArtificialIntelligence.AI_ACTION_GO_RIGHT);
			return result;
		}
		else {int result[] = new int[1];
			if(Math.abs(y-dangerY)<4 && Math.abs(x-dangerX)<4){		
			result[0] =AI_ACTION_DO_NOTHING;
			return result;}
			else{
				result[0] =move(x,y);
				return result;
			}
		}
		
	}
	
	private int goMiddle(int x,int y)
	{
		//   ---zone map---
		//        1 2
		//        3 4
		//on controle si on se trouve dans la zone numero 1 ou bien 2
		if(x==midX)
		{
		
			if(y<midY)
			{
				if(isObstacle(x, y+1))
				{
				if(isObstacle(x+1, y))
					return AI_ACTION_PUT_BOMB;
				else
					return AI_ACTION_GO_RIGHT;
				}
				else 
					return AI_ACTION_GO_DOWN;
		   }
			else
			{
				if(isObstacle(x, y-1))
				{
				if(isObstacle(x-1, y))
					return AI_ACTION_PUT_BOMB;
				else
					return AI_ACTION_GO_LEFT;
				}
				else 
					return AI_ACTION_GO_UP;
				
			}
		}
		else if(y==midY)
		{
			if(x<midX)
			{
				if(isObstacle(x+1, y))
				{
				if(isObstacle(x, y-1))
					return AI_ACTION_PUT_BOMB;
				else
					return AI_ACTION_GO_UP;
				}
				else
					return AI_ACTION_GO_RIGHT;
		   }
			else
			{
				if(isObstacle(x-1, y))
				{
				if(isObstacle(x, y+1))
					return AI_ACTION_PUT_BOMB;
				else
					return AI_ACTION_GO_DOWN;
				}

				else
					return AI_ACTION_GO_LEFT;
			}
			
			
			
			
		}
		
		else if(y<midY){
			if(x<midX){
				return getBonus(x, y, x+1, y+1);
			}
		    else{
		    	return getBonus(x, y,x-1 ,y+1);
			}	
				
		}	
		else
		{
			if(x<midX){
			return getBonus(x, y, x+1, y-1);
		}
	    else{
	    	return getBonus(x, y,x-1 ,y-1);
		}			
			
		}
	}

 private int move(int x, int y)
 {
	 int result=AI_ACTION_DO_NOTHING;
		Vector<Integer> possibleMoves = getPossibleMoves(x,y);
		// on teste s'il est possible d'effectuer le même déplacement que précédemment
		if(possibleMoves.contains(lastMove))
		{	result = lastMove;
		// sinon : soit on se déplace, soit on pose une bombe
		}
		else if(possibleMoves.size()>0)
		{	// on peut poser une bombe si on est à la fois dans un cul de sac 
			// (1 seul déplacement possible) et sur une case vide
			result=AI_ACTION_DO_NOTHING;
			if(possibleMoves.size()<2 && getZoneMatrix()[x][y]==ArtificialIntelligence.AI_BLOCK_EMPTY)
				possibleMoves.add(ArtificialIntelligence.AI_ACTION_PUT_BOMB);
		
			Random rand = new Random();
			// on détermine aléatoirement l'action qui va être effectuée
			int index;
			do
			{	index = (int)( rand.nextInt(possibleMoves.size()));			
			}
			while(index==possibleMoves.size());
			result = possibleMoves.get(index);
		}
		if(lastMove!=result)
		{
			return AI_ACTION_PUT_BOMB;
		}
		lastMove = result;

 return result;
 }
 
 private int[] getClosestBonusPosition(int x, int y, int AI_BLOCK_ITEM_BONUS)
	{	int minDistance = Integer.MAX_VALUE;
		int result[] = {-1,-1}; 
		int[][] matrix = getZoneMatrix();
		for(int i=0;i<getZoneMatrixDimX();i++)
			for(int j=0;j<getZoneMatrixDimY();j++)
				if(matrix[i][j] == AI_BLOCK_ITEM_BONUS)
				{	int tempDistance = distance(x,y,i,j); 	
					if(tempDistance<minDistance)
					{	minDistance = tempDistance;
						result[0] = i;
						result[1] = j;
					}
				}
		return result;
	}
 
 
 private int getBonus(int x, int y, int bonusX, int bonusY)
 {	int result;
 
  if(Math.abs(x-bonusX)<2&&Math.abs(y-bonusY)<2)
	{
	 if(x==bonusX){
		 if(y<bonusY){
			 result=AI_ACTION_GO_DOWN;
			 
		 }
		 else{
			 result=AI_ACTION_GO_UP;
		 }
	 }
	 else if(y==bonusY)
	 {
		 
		 if(x<bonusX){
			 result=AI_ACTION_GO_RIGHT;
		 }
		 else{
			 result=AI_ACTION_GO_LEFT;
		 }
	 } 
	else if(y<bonusY){
			 if(x<bonusX)
			 {
				 if(isObstacle(bonusX,bonusY-1)&&isObstacle(bonusX-1, bonusY))
				 {
					 result=AI_ACTION_PUT_BOMB;
				 }
				 else if(isObstacle(bonusX,bonusY-1))
					 result=AI_ACTION_GO_DOWN;
				 else
				result=AI_ACTION_GO_RIGHT;
			 }
			 else 
			 { if(isObstacle(bonusX,bonusY-1)&&isObstacle(bonusX+1, bonusY))
			 {
				 result=AI_ACTION_PUT_BOMB;
			 }
			 else if(isObstacle(bonusX,bonusY-1))
				 result=AI_ACTION_GO_DOWN;
			 else
			result=AI_ACTION_GO_LEFT;
				 
			 }
		}
	else{
		 if(x<bonusX)
		 {
			 if(isObstacle(bonusX-1,bonusY)&&isObstacle(bonusX, bonusY+1))
			 {
				 result=AI_ACTION_PUT_BOMB;
			 }
			 else if(isObstacle(bonusX-1,bonusY))
				 result=AI_ACTION_GO_RIGHT;
			 else
			result=AI_ACTION_GO_UP;
		 }
		 else 
		 { if(isObstacle(bonusX+1,bonusY)&&isObstacle(bonusX, bonusY+1))
		 {
			 result=AI_ACTION_PUT_BOMB;
		 }
		 else if(isObstacle(bonusX+1,bonusY))
			 result=AI_ACTION_GO_LEFT;
		 else
		result=AI_ACTION_GO_UP;
			 
		
	   } 		 		 
	 }
 return result;		
}
  else if(Math.abs(x-bonusX)<3&&Math.abs(y-bonusY)<3)
  {
	if(y==bonusY)
	{
		if(x<bonusX)
		{
		  if(isObstacle(bonusX-1, bonusY))
		  {
			  result=AI_ACTION_PUT_BOMB;
		  }
		  else
		  {
			  result=AI_ACTION_GO_RIGHT;
		  }
		}
		else
		{
			if(isObstacle(bonusX+1, bonusY))
			  {
				  result=AI_ACTION_PUT_BOMB;
			  }
			  else
			  {
				  result=AI_ACTION_GO_LEFT;
			  }
		}
	}
		else if(x==bonusX)
		{
			if(y<bonusY)
			{
			  if(isObstacle(bonusX, bonusY-1))
			  {
				  result=AI_ACTION_PUT_BOMB;
			  }
			  else
			  {
				  result=AI_ACTION_GO_DOWN;
			  }
			}
			else
			{
				if(isObstacle(bonusX, bonusY+1))
				  {
					  result=AI_ACTION_PUT_BOMB;
				  }
				  else
				  {
					  result=AI_ACTION_GO_UP;
				  }
			
		}
	}
		else if(y<bonusY)
			{
				if(x<bonusX)
				{
					if(isObstacle(bonusX-1, bonusY-1))
						result=AI_ACTION_PUT_BOMB;
					else 
						result=getBonus(x, y, bonusX-1, bonusY-1);
				}
				else{
					if(isObstacle(bonusX+1, bonusY-1))
						result=AI_ACTION_PUT_BOMB;
					else 
						result=getBonus(x, y, bonusX+1, bonusY-1);
				}
			}
			else{
				if(x<bonusX)
				{
					if(isObstacle(bonusX-1, bonusY+1))
						result=AI_ACTION_PUT_BOMB;
					else 
						result=getBonus(x, y, bonusX-1, bonusY+1);
				}
				else{
					if(isObstacle(bonusX+1, bonusY+1))
						result=AI_ACTION_PUT_BOMB;
					else 
						result=getBonus(x, y, bonusX+1, bonusY+1);
				}
			}
	
	return result;	  
  }
  
	else{
		return AI_ACTION_DO_NOTHING;
		}
	 	
}
 
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

 
 
}
