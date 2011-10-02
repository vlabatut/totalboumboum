package org.totalboumboum.ai.v200708.ais.kilicpala;

import java.util.Vector;

import org.totalboumboum.ai.v200708.adapter.ArtificialIntelligence;

/**
 * 
 * @author Uğur Çağlar Kılıç
 * @author Ibrahim Yağızhan Pala
 *
 */
@SuppressWarnings("deprecation")
public class KilicPala extends ArtificialIntelligence
{	private static final long serialVersionUID = 1L;
	/** le dernier déplacement effectué */
	private Integer lastMove;

	/**
	 * Constructeur.
	 */
	public KilicPala()
	{	super("KilicPala");	 
	    lastMove = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
	  
	}
	
	/** indicateur de première invocation (pour la compatibilité */
	private boolean firstTime = true;

	public synchronized Integer call() throws Exception
	{	Integer result = ArtificialIntelligence.AI_ACTION_DO_NOTHING;
		
	if(firstTime)
		firstTime = false;
	else
	{	
		int matrix[][]=getZoneMatrix();
		int x = getOwnPosition()[0];
		int y = getOwnPosition()[1];
		
		int posBomb[]=getClosestBlockPosition(x, y, AI_BLOCK_BOMB);
		int posPlayer[]=getClosestPlayerPosition(x, y);
		Vector<Integer> possibleMoves = getPossibleMoves(x,y);
		int xShrink=getNextShrinkPosition()[0];
		int yShrink=getNextShrinkPosition()[1];
		int distShrink=distance(x, y, xShrink, yShrink);
		
		//si shrink est bien commence
		if((getTimeBeforeShrink()<=1000) && (distShrink <5)){
			
			//si shrink en haut et a gauche
			if((getNextShrinkPosition()[0]<=getZoneMatrixDimX()/2) && (getNextShrinkPosition()[1]<=getZoneMatrixDimY()/2)){
				try
				{
					if(isNextPosMoreFar(1, 1, x, y, possibleMoves.get(0))
							&& possibleMoves.get(0)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(0);
					else if(isNextPosMoreFar(1, 1, x, y, possibleMoves.get(1))
							&& possibleMoves.get(1)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(1);
					else if(isNextPosMoreFar(1, 1, x, y, possibleMoves.get(2))
							&& possibleMoves.get(2)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(2);
					else if(isNextPosMoreFar(1, 1, x, y, possibleMoves.get(3))
							&& possibleMoves.get(3)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(3);
					else{
						int index;
						do
						{	index = (int)(Math.random()*(possibleMoves.size()));			
						}
						while(index==possibleMoves.size());
						result = possibleMoves.get(index);
					}
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			//si shrink est en haut et en droite
			else if((getNextShrinkPosition()[0]>=getZoneMatrixDimX()/2) && (getNextShrinkPosition()[1]<=getZoneMatrixDimY()/2)){
				try
				{
					if(isNextPosMoreFar(getZoneMatrixDimX(), 1, x, y, possibleMoves.get(0))
							&& possibleMoves.get(0)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(0);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), 1, x, y, possibleMoves.get(1))
							&& possibleMoves.get(1)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(1);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), 1, x, y, possibleMoves.get(2))
							&& possibleMoves.get(2)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(2);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), 1, x, y, possibleMoves.get(3))
							&& possibleMoves.get(3)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(3);
					else{
						int index;
						do
						{	index = (int)(Math.random()*(possibleMoves.size()));			
						}
						while(index==possibleMoves.size());
						result = possibleMoves.get(index);
					}
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			//si shrink est en bas et a droite
			else if((getNextShrinkPosition()[0]>=getZoneMatrixDimX()/2) && (getNextShrinkPosition()[1] >= getZoneMatrixDimY()/2)){
				try
				{
					if(isNextPosMoreFar(getZoneMatrixDimX(), getZoneMatrixDimY(), x, y, possibleMoves.get(0))
							&& possibleMoves.get(0)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(0);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), getZoneMatrixDimY(), x, y, possibleMoves.get(1))
							&& possibleMoves.get(1)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(1);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), getZoneMatrixDimY(), x, y, possibleMoves.get(2))
							&& possibleMoves.get(2)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(2);
					else if(isNextPosMoreFar(getZoneMatrixDimX(), getZoneMatrixDimY(), x, y, possibleMoves.get(3))
							&& possibleMoves.get(3)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(3);
					else{
						int index;
						do
						{	index = (int)(Math.random()*(possibleMoves.size()));			
						}
						while(index==possibleMoves.size());
						result = possibleMoves.get(index);
					}
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			//si shrink est en bas et a gauche
			else if((getNextShrinkPosition()[0]<=getZoneMatrixDimX()/2) && (getNextShrinkPosition()[1]>=getZoneMatrixDimY()/2)){
				try
				{
					if(isNextPosMoreFar(1, getZoneMatrixDimY(), x, y, possibleMoves.get(0))
							&& possibleMoves.get(0)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(0);
					else if(isNextPosMoreFar(1, getZoneMatrixDimY(), x, y, possibleMoves.get(1))
							&& possibleMoves.get(1)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(1);
					else if(isNextPosMoreFar(1, getZoneMatrixDimY(), x, y, possibleMoves.get(2))
							&& possibleMoves.get(2)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(2);
					else if(isNextPosMoreFar(1, getZoneMatrixDimY(), x, y, possibleMoves.get(3))
							&& possibleMoves.get(3)==whichMoveMakesMeLeastFar(xShrink, yShrink, x, y))
						result=possibleMoves.get(3);
					else{
						int index;
						do
						{	index = (int)(Math.random()*(possibleMoves.size()));			
						}
						while(index==possibleMoves.size());
						result = possibleMoves.get(index);
					}
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			//comportement au hasard
			else{
				int index;
				do
				{	index = (int)(Math.random()*(possibleMoves.size()));			
				}
				while(index==possibleMoves.size());
				result = possibleMoves.get(index);	
			}
		}
		//si shrink n'a pas commence encore et si je suis sur la meme case avec un atre personnage
		else if(posPlayer[0]==x && posPlayer[1]==y && lastMove!=AI_ACTION_PUT_BOMB && amIcloseToBomb(x, y)>8){
			int indexPlayer=getPlayerIndex(x, y);
			if(isPlayerAlive(indexPlayer))
				result=AI_ACTION_PUT_BOMB;
			else if(amInearSoftWall(x, y))
				result=AI_ACTION_PUT_BOMB;
			
			else if(possibleMoves.contains(lastMove))
				result=lastMove;
			//comportement au hasard
			else{
				int index;
				do
				{	index = (int)(Math.random()*(possibleMoves.size()));			
				}
				while(index==possibleMoves.size());
				result = possibleMoves.get(index);
				
			}
			
			 
		}
	    // si je suis proche d'une bombe
		else if(amIcloseToBomb(x, y)<8 || !amIonASafeCase(posBomb[0], posBomb[1], x, y)){
			
			
			if(matrix[x][y]==4){
				int flag=0;
				
				try{/*
				  for(int i=0;i<possibleMoves.size();i++){
					   if(doYouHaveEmptySideNeighbor(x, y, possibleMoves.get(i))){
						   result=possibleMoves.get(i);
						   break;
					   } 
					   else if(doYouHaveEmptyNeighbor(x, y, possibleMoves.get(i))){
						   result=possibleMoves.get(i);
						   break;
					   }
					   else
						   result=lastMove;
				   }*/
					for(int i=0;i<possibleMoves.size();i++){
						if(possibleMoves.get(i)==AI_ACTION_GO_UP){
							if(!isThereAnyWallBetween(x, y-4, x, y))
							{
								result=possibleMoves.get(i);
								
								flag=1;
								
							}
							else if(doYouHaveEmptySideNeighbor(x, y, possibleMoves.get(i)))
								result=possibleMoves.get(i);
							else if(doYouHaveEmptyNeighbor(x, y, possibleMoves.get(i))){
								result=possibleMoves.get(i);
								
							}
							else
								result=lastMove;
						}
						else if(possibleMoves.get(i)==AI_ACTION_GO_DOWN){
							if(!isThereAnyWallBetween(x, y+4, x, y))
							{
								result=possibleMoves.get(i);
							
								flag=1;
							}
							else if(doYouHaveEmptySideNeighbor(x, y, possibleMoves.get(i)))
								result=possibleMoves.get(i);
							else if(doYouHaveEmptyNeighbor(x, y, possibleMoves.get(i))){
								result=possibleMoves.get(i);
								
							}
							else
								result=lastMove;
						}
						else if(possibleMoves.get(i)==AI_ACTION_GO_LEFT){
							if(!isThereAnyWallBetween(x-4, y, x, y))
							{
								result=possibleMoves.get(i);
								
								flag=1;
							}
							else if(doYouHaveEmptySideNeighbor(x, y, possibleMoves.get(i)))
								result=possibleMoves.get(i);
							else if(doYouHaveEmptyNeighbor(x, y, possibleMoves.get(i))){
								result=possibleMoves.get(i);
								
							}
							else
								result=lastMove;
						}
						else if(possibleMoves.get(i)==AI_ACTION_GO_RIGHT){
							if(!isThereAnyWallBetween(x+4, y, x, y))
							{
								result=possibleMoves.get(i);
								
								flag=1;
							}
							else if(doYouHaveEmptySideNeighbor(x, y, possibleMoves.get(i)))
								result=possibleMoves.get(i);
							else if(doYouHaveEmptyNeighbor(x, y, possibleMoves.get(i))){
								result=possibleMoves.get(i);
								
							}
							else
								result=lastMove;
						}
						if(flag==1)
							break;
					}
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			//si je suis proche de deux bombes
			else if(howManyBombsAreThere(x, y)>1){
				if(amIonASafeCase(posBomb[0], posBomb[1], x, y))
					result=AI_ACTION_DO_NOTHING;
				else try{
					if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(0)))
						result=possibleMoves.get(0);
					else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(1)))
						result=possibleMoves.get(1);
					else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(2)))
						result=possibleMoves.get(2);
					else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(3)))
						result=possibleMoves.get(3);
					else{
						int index;
						do
						{	index = (int)(Math.random()*(possibleMoves.size()));			
						}
						while(index==possibleMoves.size());
						result = possibleMoves.get(index);
					}	
				}
				catch (Exception e) {
					//  handle exception
				}
			}
			else try
			{
				
				if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(0)) 
						&& possibleMoves.get(0)==whichMoveMakesMeLeastFar(posBomb[0],posBomb[1], x, y))
					result=possibleMoves.get(0);
				else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(1))
						&& possibleMoves.get(1)==whichMoveMakesMeLeastFar(posBomb [0],posBomb[1], x, y))
					result=possibleMoves.get(1);
				else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(2))
						&& possibleMoves.get(2)==whichMoveMakesMeLeastFar(posBomb[0],posBomb[1], x, y))
					result=possibleMoves.get(2);
				else if(isNextPosMoreFar(posBomb[0], posBomb[1], x, y, possibleMoves.get(3))
						&& possibleMoves.get(3)==whichMoveMakesMeLeastFar(posBomb[0],posBomb[1], x, y))
					result=possibleMoves.get(3);
				else{
					int index;
					do
					{	index = (int)(Math.random()*(possibleMoves.size()));			
					}
					while(index==possibleMoves.size());
					result = possibleMoves.get(index);
				}
			}
			catch (Exception e) {
				//  handle exception
			}
		}
		//si je suis a cote de softwall
		else if(amInearSoftWall(x, y)  )
			result=AI_ACTION_PUT_BOMB;
				
		else if(possibleMoves.contains(lastMove))
			result=lastMove;
		else if(possibleMoves.size()>0){
			
				
			
			int index;
			do
			{	index = (int)(Math.random()*(possibleMoves.size()));			
			}
			while(index==possibleMoves.size());
			result = possibleMoves.get(index);	
		}
		lastMove = result;
	}
		return result;	
	}
	
	/**
	 * 
	 * Indique si la case située à la position passée en paramètre
	 * constitue un obstacle pour un personnage : bombe, feu, mur.
	 * @param x	position à étudier
	 * @param y	position à étudier
	 * @return	vrai si la case contient un obstacle
	 * 
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
		if(getTimeBeforeShrink()<=0)
		result = result || (x==getNextShrinkPosition()[0] && y==getNextShrinkPosition()[1]);
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
	
	private int amIcloseToBomb(int x,int y){
		//x = getOwnPosition()[0];
	    //y = getOwnPosition()[1];
	    int pos[]=getClosestBlockPosition(x, y, AI_BLOCK_BOMB);
	    if(pos[0] != -1 || pos[1]!= -1){
	    	int dist=distance(x, y, pos[0],pos[1]);
		    return dist;
	    }
	    else
	    	return Integer.MAX_VALUE;	
	}
	private int distance(int x1,int y1,int x2,int y2)
	{	int result = 0;
		result = result + Math.abs(x1-x2);
		result = result + Math.abs(y1-y2);
		return result;
	}
	private double vraiDistance(int x1,int y1,int x2,int y2){
		double resulttemp=0;
		double result;
		resulttemp=Math.pow(Math.abs(x1-x2),2)+Math.pow(Math.abs(y2-y1), 2);
		result=Math.sqrt(resulttemp);
		return result;
	}
	
	private boolean amInearSoftWall(int x,int y){
		boolean result=false;
		int posSoftWall[]=getClosestBlockPosition(x, y, AI_BLOCK_WALL_SOFT);
		if(distance(x,y,posSoftWall[0],posSoftWall[1])==1)
			result=true;
		
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
	
	
	private boolean doYouHaveEmptyNeighbor(int x,int y,int move){
		boolean result=false;
		int xNext;
		int yNext;
		int matrix[][]=getZoneMatrix();
		if(move==AI_ACTION_GO_UP){
			xNext=x;
			yNext=y-1;
			if(matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0 || matrix[xNext-1][yNext]==0 || matrix[xNext+1][yNext]==0)
				result=true;
		}
		if(move==AI_ACTION_GO_DOWN){
			xNext=x;
			yNext=y+1;
			if(matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0 || matrix[xNext-1][yNext]==0 || matrix[xNext+1][yNext]==0)
				result=true;
		}
		if(move==AI_ACTION_GO_LEFT){
			xNext=x-1;
			yNext=y;
			if(matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0 || matrix[xNext-1][yNext]==0 || matrix[xNext+1][yNext]==0)
				result=true;
		}
		if(move==AI_ACTION_GO_RIGHT){
			xNext=x+1;
			yNext=y;
			if(matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0 || matrix[xNext-1][yNext]==0 || matrix[xNext+1][yNext]==0)
				result=true;
		}

		return result;
	}
	
	private  int getPlayerIndex(int xPlayer,int yPlayer){
		int result=-1;
		for(int i=0;i<getPlayerCount();i++){
			if(getPlayerPosition(i)[0]==xPlayer  && getPlayerPosition(i)[1]==yPlayer){
				return i;
			}
				
		}
		
		return result;
	}	
	
	
	private boolean isNextPosMoreFar(int x1,int y1,int x2,int y2,int move){
		boolean result=false;
		double dist=vraiDistance(x1, y1, x2, y2);
		double distNext;
		switch(move){
		
		case AI_ACTION_GO_UP:
			distNext=vraiDistance(x1, y1, x2, y2-1);
			if(distNext > dist)
				result=true;
			break;
		case AI_ACTION_GO_DOWN:
			distNext=vraiDistance(x1, y1, x2, y2+1);
			if(distNext > dist)
				result=true;
			break;
		case AI_ACTION_GO_LEFT:
			distNext=vraiDistance(x1, y1, x2-1, y2);
			if(distNext > dist)
				result=true;
			break;
		case AI_ACTION_GO_RIGHT:
			distNext=vraiDistance(x1, y1, x2+1, y2);
			if(distNext > dist)
				result=true;
			break;		
		}
	
		return result;
	}

	
	private int  whichMoveMakesMeLeastFar(int x1,int y1,int x2 ,int y2){
	    
		double vraiDist=Integer.MAX_VALUE;
		int flag=0;
		for(int i=AI_ACTION_GO_UP;i<=AI_ACTION_GO_RIGHT;i++){
			if(i==AI_ACTION_GO_UP && isMovePossible(x2, y2, AI_ACTION_GO_UP) && isNextPosMoreFar(x1, y1, x2, y2, AI_ACTION_GO_UP)){
				if(vraiDistance(x1, y1, x2, y2-1) < vraiDist){
					vraiDist=vraiDistance(x1, y1, x2, y2-1);
					flag=i;
				}	
			}
			if(i==AI_ACTION_GO_DOWN && isMovePossible(x2, y2, AI_ACTION_GO_DOWN) && isNextPosMoreFar(x1, y1, x2, y2, AI_ACTION_GO_DOWN)){
				if(vraiDistance(x1, y1, x2, y2+1) < vraiDist){
					vraiDist=vraiDistance(x1, y1, x2, y2+1);
					flag=i;
				}	
			}
			if(i==AI_ACTION_GO_LEFT && isMovePossible(x2, y2, AI_ACTION_GO_LEFT) && isNextPosMoreFar(x1, y1, x2, y2, AI_ACTION_GO_LEFT)){
				if(vraiDistance(x1, y1, x2-1, y2) < vraiDist){
					vraiDist=vraiDistance(x1, y1, x2-1, y2);
					flag=i;
				}	
			}
			if(i==AI_ACTION_GO_RIGHT && isMovePossible(x2, y2, AI_ACTION_GO_RIGHT) && isNextPosMoreFar(x1, y1, x2, y2, AI_ACTION_GO_RIGHT)){
				if(vraiDistance(x1, y1, x2+1, y2) < vraiDist){
					vraiDist=vraiDistance(x1, y1, x2+1, y2);
					flag=i;
				}	
			}
		}
		return flag;
	}
	
	private boolean amIonASafeCase(int x1,int y1,int x2,int y2){
		boolean result=true;
		if((x1==x2 || y1==y2) && getBombPowerAt(x1, y1)+2 >= distance(x1, y1, x2, y2) ){
			result=result && isThereAnyWallBetween(x1, y1, x2, y2);
		}
		else
			result=true;		
		return result;
	}
	private boolean isThereAnyWallBetween(int x1,int y1,int xPlayer,int yPlayer){
		boolean result=false;
		if(xPlayer==x1){
			if(yPlayer < y1){
				int count=y1-yPlayer;
				for(int i=1;i<count;i++){
					if(doYouHaveWallNeighbor(xPlayer, yPlayer+i, AI_ACTION_GO_DOWN)){
						result=true;
						break;
					}
				}
				
			}
			if(yPlayer > y1){
				int count=yPlayer-y1;
				for(int i=1;i<count;i++){
					if(doYouHaveWallNeighbor(xPlayer, yPlayer-i, AI_ACTION_GO_UP)){
						result=true;
						break;
					}
				}
				
			}
		}
		else if(yPlayer==y1){
			if(xPlayer<x1){
				int count=x1-xPlayer;
				for(int i=1;i<count;i++){
					if(doYouHaveWallNeighbor(xPlayer+i, yPlayer, AI_ACTION_GO_RIGHT)){
						result=true;
						break;
					}
				}
			}
			if(xPlayer>x1){
				int count=xPlayer-x1;
				for(int i=1;i<count;i++){
					if(doYouHaveWallNeighbor(xPlayer-i, yPlayer, AI_ACTION_GO_LEFT)){
						result=true;
						break;
					}
				}
			}
		}
		return result;
	}
	private boolean doYouHaveWallNeighbor(int x,int y,int move){
		boolean result=false;
		int xNext;
		int yNext;
		int matrix[][]=getZoneMatrix();
		if(move==AI_ACTION_GO_UP){
			xNext=x;
			yNext=y-1;
			if(matrix[xNext][yNext]==AI_BLOCK_WALL_HARD || matrix[xNext][yNext]==AI_BLOCK_WALL_SOFT )
				result=true;
		}
		if(move==AI_ACTION_GO_DOWN){
			xNext=x;
			yNext=y+1;
			if(matrix[xNext][yNext]==AI_BLOCK_WALL_HARD || matrix[xNext][yNext]==AI_BLOCK_WALL_SOFT )
				result=true;
		}
		if(move==AI_ACTION_GO_LEFT){
			xNext=x-1;
			yNext=y;
			if(matrix[xNext][yNext]==AI_BLOCK_WALL_HARD || matrix[xNext][yNext]==AI_BLOCK_WALL_SOFT )
				result=true;
		}
		if(move==AI_ACTION_GO_RIGHT){
			xNext=x+1;
			yNext=y;
			if(matrix[xNext][yNext]==AI_BLOCK_WALL_HARD || matrix[xNext][yNext]==AI_BLOCK_WALL_SOFT )
				result=true;
		}

		return result;
	}
	private int howManyBombsAreThere(int x,int y){
		int result=0;
		
		int matrix[][]=getZoneMatrix();
		
		for(int i=x-2;i<=x+2;i++)
			for(int j=y-2;j<=y+2;j++){
				try{
				if(matrix[i][j]==AI_BLOCK_BOMB)
					result++;
				}
				catch (Exception e) {
					//  handle exception
				}
			}
		
		return result;
		
	}
	private boolean doYouHaveEmptySideNeighbor(int x,int y,int move){
		boolean result=false;
		int xNext;
		int yNext;
		int matrix[][]=getZoneMatrix();
		if(move==AI_ACTION_GO_UP){
			xNext=x;
			yNext=y-1;
			if((matrix[xNext+1][yNext]==0 || matrix[xNext-1][yNext]==0) && (matrix[xNext][yNext]==0) )
				result=true;
		}
		if(move==AI_ACTION_GO_DOWN){
			xNext=x;
			yNext=y+1;
			if((matrix[xNext+1][yNext]==0 || matrix[xNext-1][yNext]==0) && (matrix[xNext][yNext]==0) )
				result=true;
		}
		if(move==AI_ACTION_GO_LEFT){
			xNext=x-1;
			yNext=y;
			if((matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0) && (matrix[xNext][yNext]==0))
				result=true;
		}
		if(move==AI_ACTION_GO_RIGHT){
			xNext=x+1;
			yNext=y;
			if((matrix[xNext][yNext+1]==0 || matrix[xNext][yNext-1]==0) && (matrix[xNext][yNext]==0))
				result=true;
		}

		return result;
	}
	
}
