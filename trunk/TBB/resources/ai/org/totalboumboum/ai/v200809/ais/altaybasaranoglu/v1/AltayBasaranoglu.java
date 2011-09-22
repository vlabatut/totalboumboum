package org.totalboumboum.ai.v200809.ais.altaybasaranoglu.v1;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ismail Gökhan Altay
 * @author Ertuğrul Basaranoğlu
 *
 */
public class AltayBasaranoglu extends ArtificialIntelligence
{
	/** la case occupée actuellement par le personnage*/
	private AiTile currentTile;
	/** la case sur laquelle on veut aller */
	private AiTile nextTile = null;
	/** la dernière case par laquelle on est passé */ 
//	private AiTile previousTile = null;
	
	
	///////////DEFENCE CONSTANTS//////////////////////
	private final int BLOCK_INDEST=-4;
	private final int BLOCK_DEST=-3;
	private final int FIRE=-2;
	private final int BOMB=-1;
	private final int SAFE=0;
	private final int NON_VISITED=0;
	private final int VISITED=1;
	private final int CONSTANT_RUN_AWAY=10;
	//tehlikeli ise 1 den buyuk degerler
	
	
	//VARIABLES DE STRATEGIE
	//private boolean ATTACK=false;
	private boolean DEFENCE=true;
	
	//LA ZONE
	private int col;
	private int line;

	//LES VARIABLES D'ATTACK
	//private boolean attackCompleted=false;
	//private boolean targetGone=false;
	private boolean bombDroped=false;
	
	public AiAction processAction() throws StopRequestException
	{	checkInterruption();		
		AiZone zone = getPercepts();
		col=zone.getWidth();
		line=zone.getHeight();
		///////////MATRICE SERVANT A DEFENCE STRATEGIE///////////////////
		int matriceDefence[][][] = new int[line][col][2];
		//on init la matrice avec tous zero
		//remplir la matrice
		for (int i=0; i<line; i++){
			checkInterruption();
			for (int j=0; j<col; j++){
				checkInterruption();
				matriceDefence[i][j][0]=SAFE;
				matriceDefence[i][j][1]=NON_VISITED;
			}
		}
		
		//LES MURS
		Collection<AiBlock> blocks=zone.getBlocks();
		Iterator<AiBlock> itAiBlock=blocks.iterator();
		while(itAiBlock.hasNext()){
			checkInterruption();
			AiBlock block=itAiBlock.next();
			if(block.isDestructible())
				matriceDefence[block.getLine()][block.getCol()][0]=BLOCK_DEST;
			else
				matriceDefence[block.getLine()][block.getCol()][0]=BLOCK_INDEST;
		}
		
		//LES FEUX
		Collection<AiFire> fires=zone.getFires();
		Iterator<AiFire> itAiFire= fires.iterator();
		while(itAiFire.hasNext()){
			checkInterruption();
			AiFire fire = itAiFire.next();
			matriceDefence[fire.getLine()][fire.getCol()][0]=FIRE;
		}
		/*
		//LES ITEMS
		Collection<AiItem> items=getPercepts().getItems();
		Iterator<AiItem> itAiItem=items.iterator();
		while(itAiItem.hasNext()){
			AiItem item=itAiItem.next();
			matriceDefence[item.getLine()][item.getCol()]=ITEM;
		}
		*/
		//LES BOMBS
		Collection<AiBomb> bombs=zone.getBombs();
		Iterator<AiBomb> itAiBomb=bombs.iterator();
		int bombRange;
		int bombCol;
		int bombLine;
		while(itAiBomb.hasNext()){
			checkInterruption();
			AiBomb bomb=itAiBomb.next();
			bombRange=bomb.getRange();
			bombCol=bomb.getCol();
			bombLine=bomb.getLine();
			matriceDefence[bombLine][bombCol][0]=BOMB;
			//REMPLIR MATRICE EN +y
			for(int i = 1; i<=bombRange; i++){
				checkInterruption();
				if(bombLine+i<line){
					//TANT QU'ON NE DEBORDE PAS
					if(matriceDefence[bombLine + i][bombCol][0]<-2){
						//cest mur, danger n'est plus aux tailles suivantes. on ne change pas les valeurs
						//donc on termine l'iteration dans ce sense
						i=bombRange+1;
					}
					else if(matriceDefence[bombLine + i][bombCol][0]>=-2&&
						matriceDefence[bombLine + i][bombCol][0]<0){
						//feu ou bombe; on l'ignore ca, et on continue mettre les valeurs 
						;//on ne fait rien
					}
					else{
						//INCREMENTER LES VALEURS
						matriceDefence[bombLine + i][bombCol][0]= matriceDefence[bombLine + i][bombCol][0]+bombRange-i+1;
					}
				}
			}
			//-y
			for(int i = 1; i<=bombRange; i++){
				checkInterruption();
				if(bombLine-i>=0){
	
					if(matriceDefence[bombLine - i][bombCol][0]<-2){

						i=bombRange+1;
					}
					else if(matriceDefence[bombLine - i][bombCol][0]>=-2&&
						matriceDefence[bombLine - i][bombCol][0]<0){
		
						;
					}
					else{
					
						matriceDefence[bombLine - i][bombCol][0]= matriceDefence[bombLine - i][bombCol][0]+bombRange-i+1;
					}
				}
			}
			// +x
			for(int i = 1; i<=bombRange; i++){
				checkInterruption();
				if(bombCol+i<col-1){
				
					if(matriceDefence[bombLine][bombCol+i][0]<-2){
					
						i=bombRange+1;
					}
					else if(matriceDefence[bombLine][bombCol+i][0]>=-2&&
						matriceDefence[bombLine][bombCol+i][0]<0){
						
						;
					}
					else{
						
						matriceDefence[bombLine][bombCol+i][0]=matriceDefence[bombLine][bombCol+i][0]+bombRange-i+1;
					}
				}
			}
			//remplir au sens -x
			for(int i = 1; i<=bombRange; i++){
				checkInterruption();
				if(bombCol-i>=0){
				
					if(matriceDefence[bombLine][bombCol-i][0]<-2){
						//cest mur, danger n'est plus aux tailles suivantes. on ne change pas les valeurs
						//donc on termine l'iteration dans ce sense
						i=bombRange+1;
					}
					else if(matriceDefence[bombLine][bombCol-i][0]>=-2&&
						matriceDefence[bombLine][bombCol-i][0]<0){
						//feu ou bombe; on l'ignore ca, et on continue mettre les valeurs 
						;//on ne fait rien
					}
					else{
						
						matriceDefence[bombLine][bombCol-i][0]=matriceDefence[bombLine][bombCol-i][0]+bombRange-i+1;
					}
				}
			}
		}//////on a rempli la matrice
		
		//int i;	
		AiHero ownHero = zone.getOwnHero();
		AiAction result = new AiAction(AiActionName.NONE);
		currentTile=ownHero.getTile();
		
		DEFENCE=!isSafe(matriceDefence,currentTile.getLine(),currentTile.getCol());
		//ATTACK=checkAttack(matriceDefence, zone);
		
			if(checkAttack(matriceDefence, zone)&&!bombDroped){
				result=attack(matriceDefence, zone);
				bombDroped=true;
			}
			else if(DEFENCE){
			result=findSafePlace(matriceDefence, line, col, zone);	
		}
		else{
			result=attack(matriceDefence, zone);
		bombDroped=false;
		}
			//ATTACK=true;
		
		return result;
	}

	private boolean checkAttack(int[][][] matriceDefence, AiZone zone) throws StopRequestException {
		checkInterruption();
		boolean result=false;
		
			int heroLine, heroCol, myLine=currentTile.getLine(), myCol=currentTile.getCol();
			AiHero myOwnHero=zone.getOwnHero();
			Collection<AiHero> heroes= zone.getHeroes();
			List<AiHero> newHeroes=new ArrayList<AiHero>();
			Iterator<AiHero> itHeroes=heroes.iterator();
			AiHero hero;
			while(itHeroes.hasNext()){
				hero = itHeroes.next();
				if(!hero.equals(myOwnHero))
				newHeroes.add(hero);
			}
			itHeroes=newHeroes.iterator();
			while(itHeroes.hasNext()){
				hero=itHeroes.next();
				heroLine=hero.getLine();
				heroCol=hero.getCol();
				if(((heroLine==myLine&&Math.abs(heroCol-myCol)<7)||(heroCol==myCol&&Math.abs(heroLine-myLine)<7))
						){//&&noObstacle(matriceDefence, hero, myOwnHero)){
					result=true;
				}
			}
		
		return result;
	}

	@SuppressWarnings("unused")
	private boolean noObstacle(int[][][] matriceDefence, AiHero hero, AiHero myOwnHero) {
		boolean result=true;
		int heroLine= hero.getLine();
		int heroCol= hero.getCol();
		int myLine=myOwnHero.getLine();
		int myCol=myOwnHero.getCol();
		if(heroLine==myLine){
			for(int i=Math.min(heroCol, myCol); i<=Math.max(heroCol, myCol); i++)
				if (matriceDefence[heroLine][i][0]<-2)
					result=false;
		}
		else if(heroCol==myCol){
			for(int i=Math.min(heroLine, myLine); i<=Math.max(heroLine, myLine); i++)
				if (matriceDefence[i][heroCol][0]<-2)
					result=false;
		}
		return result;
	}

	private AiAction findSafePlace(int[][][] matriceDefence, int line, int col, AiZone zone) throws StopRequestException{
		checkInterruption();
		AiAction result= new AiAction(AiActionName.NONE);
		
		//on demenage notre hero au sens moins dangeroux
		AiHero ownHero=zone.getOwnHero();
		AiTile tile;
		AiTile tempTile=ownHero.getTile();
		AiTile ownTile=ownHero.getTile();
		Direction dirNew=Direction.NONE,tempDir= Direction.NONE;
		int dirOldValue=2000, dirNewValue;
		int heroLine=ownHero.getLine();
	    int heroCol=ownHero.getCol();
		if (!isSafe(matriceDefence,heroLine,heroCol)){
			Collection<AiTile> tiles=zone.getNeighborTiles(ownHero.getTile());
			Iterator<AiTile> itAiTile= tiles.iterator();
			while(itAiTile.hasNext()){
				tile=itAiTile.next();
				if(isClear(tile, matriceDefence)){
					dirNew=zone.getDirection(ownTile, tile);

						dirNewValue=calculateCost(tile,ownTile, matriceDefence, zone);


					if (dirNewValue<dirOldValue){
						tempDir=dirNew;
						dirOldValue=dirNewValue;
					}

					//s'il a deux choix de meme cout, il va comparer les destinations et il va choisir plus libre
					///*
					else if(dirOldValue==dirNewValue&&dirNewValue==0&&!tempDir.equals(dirNew)){
						//daha acik alani sec
						Collection<AiTile> tiles1=zone.getNeighborTiles(zone.getNeighborTile(ownTile, dirNew));
						Iterator<AiTile> itAiTiles1=tiles1.iterator();
						AiTile tile1;
						while (itAiTiles1.hasNext()) {
							tile1=itAiTiles1.next();
							if(!tile1.equals(ownTile)&&matriceDefence[tile1.getLine()][tile1.getCol()][0]==0)
								tempDir=dirNew;							
						}
					}//*/
				}
			}

		}
		
		

		tempTile=ownTile.getNeighbor(tempDir);
		nextTile=tempTile;

		Direction direction = zone.getDirection(currentTile,nextTile);

		if(currentTile!=nextTile&&dirOldValue<1000){
			result = new AiAction(AiActionName.MOVE,direction);
		}
		return result;
		
	}
	
	private int calculateCost(AiTile aiTile, AiTile previousTile, int[][][] matriceDefence, AiZone zone) throws StopRequestException{
		checkInterruption();
		int result=1000, temp=0;
		if (isSafe(matriceDefence,aiTile.getLine(),aiTile.getCol()))
			return 0;//pas de danger
		else{	
			markVisited(matriceDefence,aiTile.getLine(),aiTile.getCol());

			Collection<AiTile> tiles=getClearNeighbors(aiTile, matriceDefence);//les voisins
			Iterator<AiTile> itAiTile=tiles.iterator();
			Collection<AiTile> processTiles = new LinkedList<AiTile>();
			AiTile tempTile;
			while(itAiTile.hasNext()){
				checkInterruption();
				tempTile=itAiTile.next();
				if(	!tempTile.equals(previousTile)&&!isVisited(matriceDefence, tempTile.getLine(), tempTile.getCol()))

					processTiles.add(tempTile);
			}
			
			if(processTiles.isEmpty())
				return 1000;
			else{
				Iterator<AiTile> itProcessTiles=processTiles.iterator();
				while(itProcessTiles.hasNext()){
					checkInterruption();
					tempTile=itProcessTiles.next();
					if(!tempTile.equals(previousTile) && isClear(tempTile, matriceDefence)){

						temp=matriceDefence[aiTile.getLine()][aiTile.getCol()][0]+calculateCost(tempTile, aiTile, matriceDefence, zone);//recursive calismiyor
						
						if(temp<result)
							result=temp;
					}
				}
				return result;
			}
		}
		
		
		
	}
	
	private Collection<AiTile> getClearNeighbors(AiTile tile, int[][][] matriceDefence) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while(it.hasNext())
		{	checkInterruption(); //APPEL OBLIGATOIRE
		
			AiTile t = it.next();
			if(isClear(t, matriceDefence))
				result.add(t);
		}
		return result;
	}
	
	private boolean isClear(AiTile tile, int[][][] matriceDefence) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		return matriceDefence[tile.getLine()][tile.getCol()][0]>=0;
		
	}
	
	private boolean isVisited(int[][][] matriceDefence, int line, int col) throws StopRequestException{
		checkInterruption();
		return matriceDefence[line][col][1]==VISITED;
	}

	private void markVisited(int[][][] matriceDefence, int line, int col) throws StopRequestException{
		checkInterruption();
		matriceDefence[line][col][1]=VISITED;
	}
	
	private boolean isSafe(int matriceDefence[][][], int line, int col) throws StopRequestException{
		checkInterruption();
		return matriceDefence[line][col][0]==0;
	}
	
	private AiAction attack(int[][][] matriceDefence, AiZone zone) throws StopRequestException{
		checkInterruption();
		AiAction result=new AiAction(AiActionName.NONE);
		boolean safety= checkSafety(matriceDefence, zone);
		//if(!targetGone){
		//	result=goTarget(matriceDefence, zone);
		//}
		//else if(targetGone&&!attackCompleted){
			
			if(safety){
				result=new AiAction(AiActionName.DROP_BOMB);
			//	attackCompleted=true;
			//	targetGone=false;
			}

		//}	

		return result;
	}
	//Si on met un bombe, est-ce qu'on peut fuire
	private boolean checkSafety(int[][][] matriceDefence, AiZone zone) throws StopRequestException {
		checkInterruption();
		boolean result=false;
		int tempMatrice[][][]=matriceDefence.clone();

		int bombRange=5;
		int bombCol=currentTile.getCol();
		int bombLine=currentTile.getLine();
		tempMatrice[bombLine][bombCol][0]=BOMB;
		//imitation, n'est pas reel mais s'il y a un bombe
		//+y
		for(int i = 1; i<=bombRange; i++){
			checkInterruption();
			if(bombLine+i<line){
				
				if(tempMatrice[bombLine + i][bombCol][0]<-2){
				
					i=bombRange+1;
				}
				else if(tempMatrice[bombLine + i][bombCol][0]>=-2&&
					tempMatrice[bombLine + i][bombCol][0]<0){
					
					;
				}
				else{
					
					tempMatrice[bombLine + i][bombCol][0]= tempMatrice[bombLine + i][bombCol][0]+bombRange-i+1;
				}
			}
		}
		//-y 
		for(int i = 1; i<=bombRange; i++){
			checkInterruption();
			if(bombLine-i>=0){
				
				if(tempMatrice[bombLine - i][bombCol][0]<-2){
			
					i=bombRange+1;
				}
				else if(tempMatrice[bombLine - i][bombCol][0]>=-2&&
					tempMatrice[bombLine - i][bombCol][0]<0){
				
					;
				}
				else{
			
					tempMatrice[bombLine - i][bombCol][0]= tempMatrice[bombLine - i][bombCol][0]+bombRange-i+1;
				}
			}
		}
		//+x
		for(int i = 1; i<=bombRange; i++){
			checkInterruption();
			if(bombCol+i<col-1){
				
				if(tempMatrice[bombLine][bombCol+i][0]<-2){
					
					i=bombRange+1;
				}
				else if(tempMatrice[bombLine][bombCol+i][0]>=-2&&
					tempMatrice[bombLine][bombCol+i][0]<0){
				
					;
				}
				else{
					
					tempMatrice[bombLine][bombCol+i][0]=tempMatrice[bombLine][bombCol+i][0]+bombRange-i+1;
				}
			}
		}
		//-x
		for(int i = 1; i<=bombRange; i++){
			checkInterruption();
			if(bombCol-i>=0){
			
				if(matriceDefence[bombLine][bombCol-i][0]<-2){
					//cest mur, danger n'est plus aux tailles suivantes. on ne change pas les valeurs
					//donc on termine l'iteration dans ce sense
					i=bombRange+1;
				}
				else if(matriceDefence[bombLine][bombCol-i][0]>=-2&&
					matriceDefence[bombLine][bombCol-i][0]<0){
					//feu ou bombe; on l'ignore ca, et on continue mettre les valeurs 
					;//on ne fait rien
				}
				else{
					
					matriceDefence[bombLine][bombCol-i][0]=matriceDefence[bombLine][bombCol-i][0]+bombRange-i+1;
				}
			}
		}
		
		AiTile tile;
		AiTile ownTile=currentTile;
		int dirOldValue=2000, dirNewValue;
		Collection<AiTile> tiles=getClearNeighbors(currentTile, tempMatrice);
		Iterator<AiTile> itAiTile= tiles.iterator();
		while(itAiTile.hasNext()){
			tile=itAiTile.next();
			if(isClear(tile, tempMatrice)){
					dirNewValue=calculateCost(tile,ownTile, tempMatrice, zone);
				if (dirNewValue<dirOldValue){
					dirOldValue=dirNewValue;
				}
			}
		}
		if(dirOldValue<CONSTANT_RUN_AWAY)// ***************************************************
			result=true;
		return result;
	}

	//n'est pas fini
	/*private AiAction goTarget(int[][][] matriceDefence, AiZone zone) throws StopRequestException{
		checkInterruption();
		AiAction result= new AiAction(AiActionName.NONE);

		int heroLine=4, heroCol=3, myLine=currentTile.getLine(), myCol=currentTile.getCol();
		AiTile tile;
		AiHero myOwnHero=zone.getOwnHero();
		Collection<AiHero> heroes= zone.getHeroes();

		Iterator<AiHero> itHeroes=heroes.iterator();
		AiHero hero=itHeroes.next();
		while(itHeroes.hasNext()&&hero.equals(myOwnHero)){
			hero=itHeroes.next();
			heroLine=hero.getLine();
			heroCol=hero.getCol();
		}
		if(heroLine!=myLine && heroCol!=myCol){
			if(heroLine-myLine<0){
				tile=currentTile.getNeighbor(Direction.UP);
				if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
					result=new AiAction(AiActionName.MOVE, Direction.UP);
				else{
					if(heroCol-myCol<0){
						tile=currentTile.getNeighbor(Direction.LEFT);
						if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
							result=new AiAction(AiActionName.MOVE, Direction.LEFT);
					}
					else if(heroCol-myCol>0){
						tile=currentTile.getNeighbor(Direction.RIGHT);
						if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
							result=new AiAction(AiActionName.MOVE, Direction.RIGHT);
					}
				}
			}
			else if(heroLine-myLine>0){
				tile=(currentTile.getNeighbor(Direction.DOWN));
				if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
					result=new AiAction(AiActionName.MOVE, Direction.DOWN);
				else{
					if(heroCol-myCol<0){
						tile=currentTile.getNeighbor(Direction.LEFT);
						if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
							result=new AiAction(AiActionName.MOVE, Direction.LEFT);
					}
					else if(heroCol-myCol>0){
						tile=currentTile.getNeighbor(Direction.RIGHT);
						if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
							result=new AiAction(AiActionName.MOVE, Direction.RIGHT);
					}
				}
			}
		}else if(myLine==heroLine||myCol==heroCol){
			if(myLine==heroLine&&heroLine%2==1){
				if(heroCol-myCol>4){
					tile=currentTile.getNeighbor(Direction.RIGHT);
					if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
						result=new AiAction(AiActionName.MOVE, Direction.RIGHT);
				}
				else if(myCol-heroCol>4){
					tile=currentTile.getNeighbor(Direction.LEFT);
					if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
						result=new AiAction(AiActionName.MOVE, Direction.LEFT);
				}
				else
					result=attack(matriceDefence, zone);
			}
			else if(myCol==heroCol&&heroCol%2==0){
				if(heroLine-myLine>4){
					tile=currentTile.getNeighbor(Direction.DOWN);
					if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
						result=new AiAction(AiActionName.MOVE, Direction.DOWN);
				}
				else if(myLine-heroLine>4){
					tile=currentTile.getNeighbor(Direction.UP);
					if(isSafe(matriceDefence, tile.getLine(), tile.getCol()))
						result=new AiAction(AiActionName.MOVE, Direction.UP);
				}
				else
					result=attack(matriceDefence, zone);
			}
		}
		//if(isTarget(matriceDefence, currentTile, zone))
		//	targetGone=true;
		return result;
	}
//n'est pas fini
	private boolean isTarget(int[][][] matriceDefence, AiTile tile, AiZone zone) {
		boolean result = false;
		int heroLine, heroCol, myLine=currentTile.getLine(), myCol=currentTile.getCol();
		Collection<AiHero> heroes= zone.getHeroes();
		Iterator<AiHero> itHeroes=heroes.iterator();
		AiHero hero;
		while(itHeroes.hasNext()){
			hero=itHeroes.next();
			heroLine=hero.getLine();
			heroCol=hero.getCol();
			if(	((heroLine==myLine&&Math.abs(heroCol-myCol)<3)||(heroCol==myCol&&Math.abs(heroLine-myLine)<3))){
				result=true;
			}
		}
		return result;
	}*/
}
