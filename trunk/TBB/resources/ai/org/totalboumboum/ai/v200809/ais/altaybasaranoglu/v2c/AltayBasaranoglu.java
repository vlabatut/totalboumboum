package org.totalboumboum.ai.v200809.ais.altaybasaranoglu.v2c;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200809.adapter.AiAction;
import org.totalboumboum.ai.v200809.adapter.AiActionName;
import org.totalboumboum.ai.v200809.adapter.AiBlock;
import org.totalboumboum.ai.v200809.adapter.AiBomb;
import org.totalboumboum.ai.v200809.adapter.AiFire;
import org.totalboumboum.ai.v200809.adapter.AiHero;
import org.totalboumboum.ai.v200809.adapter.AiItem;
import org.totalboumboum.ai.v200809.adapter.AiTile;
import org.totalboumboum.ai.v200809.adapter.AiZone;
import org.totalboumboum.ai.v200809.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200809.adapter.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @author Ismail Gokhan Altay
 * @author Ertugrul Basaranoglu
 *
 */
public class AltayBasaranoglu extends ArtificialIntelligence {
	/** la case occup�e actuellement par le personnage */
	private AiTile currentTile;


	//***************DEFENCE CONSTANTS****************\\
	private static final int HERO = -6;
	private final int BLOCK_INDEST = -5;
	private final int BLOCK_DEST = -4;
	private final int FIRE = -3;
	private final int BOMB = -2;
	private final int ITEM=-1;
	private final int SAFE = 0;
	private int CONSTANT_RUN_AWAY=100;
	private final int DEFENCE_CONSTANT=200;
	// Si'il ya danger dans care, matrice prendra une valeur superieur a 0
	
	//**********CONSTANTS POUR FONCTIONS RECURSIVES********************\\
	private final int NON_VISITED = 0;
	private final int VISITED = 1;

	// VARIABLES DE STRATEGIE
	private boolean DEFENCE = true;
	private AiTile tilePrev=null;
	// LA ZONE
	private int col;
	private int line;

	// LES VARIABLES D'ATTACK
	
	//MATRICE A MAINTENIR DES TEMPS LES BOMBS SONT POSE
	private boolean initMatrixBombTimes = false;
	private static final int NON_BOMB = -1;


	
	private long matrixBombTimes[][][];
	
	private final int AVAILABLE = 0;
	private final int NON_AVAILABLE = -1;
	private final int INITIAL = -2;
	private final int DANGER_THRESHOLD=1000;
	
	
	private final int BONUS_STEP=64;
	
	public AiAction processAction() throws StopRequestException
	{
		checkInterruption();
		
		AiZone zone = getPercepts();
		line = zone.getHeight();
		col = zone.getWidth();
		AiHero ownHero= zone.getOwnHero();
		currentTile = ownHero.getTile();
		//**************MATRICE SERVANT A MAINTENIR LES TEMPS DE BOMBS POSES**************\\
		//on prend la temps pour chaque iteration
		Date date = new Date();
		
		if(!initMatrixBombTimes){
			//c'est a dire que, c'est le premier appel de fonction
			initMatrixBombTimes();
			initMatrixBombTimes=true;
		}
		//maintenant on remplira les temps des bombes posees a la matrice
		long tempMatrixBombTimes[][][]= new long[line][col][2];
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				tempMatrixBombTimes[i][j][0] = NON_BOMB;
				tempMatrixBombTimes[i][j][1] = NON_VISITED;
			}
		}
		Collection<AiBomb> bombs = zone.getBombs();
		Iterator<AiBomb> itAiBomb = bombs.iterator();
		int bombLine;
		int bombCol;	
		AiBomb bomb;
		while (itAiBomb.hasNext()) {
			checkInterruption();
			bomb = itAiBomb.next();
			bombLine = bomb.getLine();		
			bombCol = bomb.getCol();
			if(matrixBombTimes[bombLine][bombCol][0]==-1){
				//c'est a dire, c'est la premier iteration apres bomb est pose
				tempMatrixBombTimes[bombLine][bombCol][0]=date.getTime();
			}
			else if(matrixBombTimes[bombLine][bombCol][0]>=0){
				//bomb deja existait, donc on a donne la valeur precedent
				tempMatrixBombTimes[bombLine][bombCol][0]=matrixBombTimes[bombLine][bombCol][0];
			}
			//si un bomb est exploit qui existait, il ne sera pas dans collection, donc 
			//tempMatrixBombTimes restera -1
		}
		// on a nouvelle matrice
		matrixBombTimes=tempMatrixBombTimes;


		
		//***************MATRICE SERVANT A STRATEGIE DE DEFENCE ****************/
		int matriceDefence[][][] = new int[line][col][2];
		// on init la matrice avec tous zero
		
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matriceDefence[i][j][0] = SAFE;
				matriceDefence[i][j][1] = NON_VISITED;
			}
		}
		// on remplira la matrice
		// LES MURS
		Collection<AiBlock> blocks = zone.getBlocks();
		Iterator<AiBlock> itAiBlock = blocks.iterator();
		while (itAiBlock.hasNext()) {
			checkInterruption();
			AiBlock block = itAiBlock.next();
			if (block.isDestructible())
				matriceDefence[block.getLine()][block.getCol()][0] = BLOCK_DEST;
			else
				matriceDefence[block.getLine()][block.getCol()][0] = BLOCK_INDEST;
		}
		  //LES ITEMS 
		Collection<AiItem> items=zone.getItems();
		Iterator<AiItem> itAiItem=items.iterator();
		while(itAiItem.hasNext()){ 
			checkInterruption();
			AiItem item=itAiItem.next();
			matriceDefence[item.getLine()][item.getCol()][0]=ITEM; 
		}
		// LES FEUX
		Collection<AiFire> fires = zone.getFires();
		Iterator<AiFire> itAiFire = fires.iterator();
		while (itAiFire.hasNext()) {
			checkInterruption();
			AiFire fire = itAiFire.next();
			matriceDefence[fire.getLine()][fire.getCol()][0] = FIRE;
		}

		 
		// LES BOMBS
		bombs = zone.getBombs();
		itAiBomb = bombs.iterator();
		while (itAiBomb.hasNext()) {
			checkInterruption();
			bomb = itAiBomb.next();
			bombCol = bomb.getCol();
			bombLine = bomb.getLine();
			matriceDefence[bombLine][bombCol][0] = BOMB;
		}
		
		
		//on revalue les valeurs de matrixBombTimes
		bombs = zone.getBombs();
		itAiBomb = bombs.iterator();
		while (itAiBomb.hasNext()) {
			checkInterruption();
			bomb = itAiBomb.next();
			initVisit();
			matrixBombTimes[bomb.getLine()][bomb.getCol()][0]=getRealTimes(matriceDefence, bomb, zone);
		}
		/*
		System.out.println(date.getTime());
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				System.out.print(matrixBombing[i][j]+" ,");
			}
	 		System.out.print("\n");
	    }
		System.out.print("\n");*/
		
		int dangerConstants[][] = new int[line][col];
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				if(matrixBombTimes[i][j][0]!=-1){
					if((date.getTime()-matrixBombTimes[i][j][0])>3500)
						dangerConstants[i][j]=3500;
					else
						dangerConstants[i][j]=(int) (date.getTime()-matrixBombTimes[i][j][0])+1;
					}
				
				//+1 parce que on ne veux pas qu'il commence avec 0
				else
					dangerConstants[i][j]=-1;
			}	 		
	    }
		
	
		bombs = zone.getBombs();
		itAiBomb = bombs.iterator();
		int bombRange;
		while (itAiBomb.hasNext()) {
			checkInterruption();
			bomb = itAiBomb.next();
			bombRange = Math.min(15, bomb.getRange());
			bombCol = bomb.getCol();
			bombLine = bomb.getLine();
			// REMPLIR MATRICE EN +y
			for (int i = 1; i <= bombRange; i++) {
				checkInterruption();
				if (bombLine + i < line) {
					// tant qu'on ne deborde pas
					if (matriceDefence[bombLine + i][bombCol][0] < FIRE) {
						// cest mur,  danger n'est plus aux tailles suivantes. on
						// ne change pas les valeurs
						// donc on termine l'iteration dans ce sense
						i = bombRange + 1;
					} else if (matriceDefence[bombLine + i][bombCol][0] >= FIRE
							&& matriceDefence[bombLine + i][bombCol][0] <= BOMB) {
						// feu ou bombe; on ignore ca, et on continue mettre
						// les valeurs
						;// on ne fait rien
					} else {
						// on met le taux de danger dans le matrice, si danger deja existe et superieur, on le changera pas
						matriceDefence[bombLine + i][bombCol][0] = Math.max(matriceDefence[bombLine+ i][bombCol][0], (bombRange - i + 1)*dangerConstants[bombLine][bombCol]);
					}
				}
			}
			// -y
			for (int i = 1; i <= bombRange; i++) {
				checkInterruption();
				if (bombLine - i >= 0) {

					if (matriceDefence[bombLine - i][bombCol][0] < FIRE ) {

						i = bombRange + 1;
					} else if (matriceDefence[bombLine - i][bombCol][0] >= FIRE
							&& matriceDefence[bombLine - i][bombCol][0] <= BOMB) {

						;
					} else {

						matriceDefence[bombLine - i][bombCol][0] = Math.max(matriceDefence[bombLine- i][bombCol][0], (bombRange - i + 1)*dangerConstants[bombLine][bombCol]);
					}
				}
			}
			// +x
			for (int i = 1; i <= bombRange; i++) {
				checkInterruption();
				if (bombCol + i < col ) {

					if (matriceDefence[bombLine][bombCol + i][0] < FIRE) {

						i = bombRange + 1;
					} else if (matriceDefence[bombLine][bombCol + i][0] >= FIRE
							&& matriceDefence[bombLine][bombCol + i][0] <= BOMB) {

						;
					} else {

						matriceDefence[bombLine][bombCol + i][0] = Math.max(matriceDefence[bombLine][bombCol+ i][0], (bombRange - i + 1)*dangerConstants[bombLine][bombCol]);
					}
				}
			}
			// -x
			for (int i = 1; i <= bombRange; i++) {
				checkInterruption();
				if (bombCol - i >= 0) {

					if (matriceDefence[bombLine][bombCol - i][0] < FIRE) {
						// cest mur, danger n'est plus aux tailles suivantes. on
						// ne change pas les valeurs
						// donc on termine l'iteration dans ce sense
						i = bombRange + 1;
					} else if (matriceDefence[bombLine][bombCol - i][0] >= FIRE
							&& matriceDefence[bombLine][bombCol - i][0] <= BOMB) {
						// feu ou bombe; on ignore ca, et on continue mettre
						// les valeurs
						;// on ne fait rien
					} else {

						matriceDefence[bombLine][bombCol - i][0] = Math.max(matriceDefence[bombLine][bombCol- i][0], (bombRange - i + 1)*dangerConstants[bombLine][bombCol]);
					}
				}
			}
		}//on a rempli la matrice de defence avec taux de danger
		
		int[][] matrixAvailable= new int[line][col];	
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matrixAvailable[i][j]=INITIAL;
			}
		}
		//On rempli la matrice qui nous dire les tailles accesibles de notre hero
		AiTile tile = ownHero.getTile();
		fillMatrixAvailable(tile, matriceDefence, matrixAvailable);
		
		int[][] matrixBonus= new int[line][col];
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matrixBonus[i][j]=matrixAvailable[i][j];
			}
   		}
		int[][] heroMatrix= new int[line][col];	
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				heroMatrix[i][j]=INITIAL;
			}
		}
		//LesHeros
		Collection<AiHero> heroes= zone.getHeroes();
		Iterator<AiHero> itAiHeroes= heroes.iterator();
		while (itAiHeroes.hasNext()) {
			checkInterruption();
			AiHero hero = (AiHero) itAiHeroes.next();
			if((hero!=ownHero)&&matrixAvailable[hero.getLine()][hero.getCol()]==AVAILABLE)
				heroMatrix[hero.getLine()][hero.getCol()]=HERO;
		}
	/*	for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				System.out.print(heroMatrix[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();*/
		//On rempli la matrice de bonus, les tailles evalue par la distance aux bonus	
		items=zone.getItems();
		Iterator<AiItem> itItems=items.iterator();
		AiItem item;
		while(itItems.hasNext()){
			checkInterruption();
			item=itItems.next();
		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
			AiTile itemTile = item.getTile();
			fillMatrix(matrixBonus, BONUS_STEP, itemTile);
			
		}
		
	
		
		//
///////////////////////////////////////////////MatriceD'attack
			
			int[][] matrixAttack= new int[line][col];
			bombRange=ownHero.getBombRange();
			for (int wallLine = 0; wallLine < line; wallLine++) {
				checkInterruption();
				for (int wallCol = 0; wallCol < col; wallCol++) {
					checkInterruption();
					matrixAttack[wallLine][wallCol]=matrixAvailable[wallLine][wallCol];
					if ((matriceDefence[wallLine][wallCol][0]==BLOCK_DEST&&matrixAvailable[wallLine][wallCol]==NON_AVAILABLE)){
						matrixAttack[wallLine][wallCol]=BLOCK_DEST;
					}else if((matrixAvailable[wallLine][wallCol]==AVAILABLE&&heroMatrix[wallLine][wallCol]==HERO)){
						matrixAttack[wallLine][wallCol]=HERO;
					}
				}
			}
			bombRange=3;
			for (int wallLine = 0; wallLine < line; wallLine++) {
				checkInterruption();
				for (int wallCol = 0; wallCol < col; wallCol++) {
					checkInterruption();
					if (matrixAttack[wallLine][wallCol]==BLOCK_DEST){
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallLine + i < line) {
								// tant qu'on ne deborde pas
								if (matrixAttack[wallLine + i][wallCol] < AVAILABLE) {
									i = bombRange + 1; 
								} else if(matrixAttack[wallLine + i][wallCol]>=AVAILABLE){
									matrixAttack[wallLine + i][wallCol] = matrixAttack[wallLine + i][wallCol]+1;
								}
							}
						}
						// -y
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallLine - i >= 0) {

								if (matrixAttack[wallLine - i][wallCol]< AVAILABLE ) {

									i = bombRange + 1;
								} else if(matrixAttack[wallLine - i][wallCol]>=AVAILABLE){
									matrixAttack[wallLine - i][wallCol] = matrixAttack[wallLine - i][wallCol]+1;
								}
							}
						}
						// +x
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallCol + i < col ) {
								if (matrixAttack[wallLine][wallCol + i] < AVAILABLE) {
									i = bombRange + 1;
								} else if(matrixAttack[wallLine][wallCol+i]>=AVAILABLE){
									matrixAttack[wallLine][wallCol+i] = matrixAttack[wallLine][wallCol+i]+1;
								}
							}
						}
						// -x
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallCol - i >= 0) {

								if (matrixAttack[wallLine][wallCol - i] < AVAILABLE) {
									i = bombRange + 1;
								} else if(matrixAttack[wallLine][wallCol-i]>=AVAILABLE){
									matrixAttack[wallLine][wallCol-i] = matrixAttack[wallLine][wallCol-i]+1;
								}
							}
						}
					}
					else if (matrixAttack[wallLine][wallCol]==HERO){
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallLine + i < line) {
								// tant qu'on ne deborde pas
								if (matrixAttack[wallLine + i][wallCol] < AVAILABLE) {
									i = bombRange + 1; 
								} else if(matrixAttack[wallLine + i][wallCol]>=AVAILABLE){
									matrixAttack[wallLine + i][wallCol] = matrixAttack[wallLine + i][wallCol]+2;
								}
							}
						}
						// -y
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallLine - i >= 0) {

								if (matrixAttack[wallLine - i][wallCol]< AVAILABLE ) {

									i = bombRange + 1;
								} else if(matrixAttack[wallLine - i][wallCol]>=AVAILABLE){
									matrixAttack[wallLine - i][wallCol] = matrixAttack[wallLine - i][wallCol]+2;
								}
							}
						}
						// +x
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallCol + i < col ) {
								if (matrixAttack[wallLine][wallCol + i] < AVAILABLE) {
									i = bombRange + 1;
								} else if(matrixAttack[wallLine][wallCol+i]>=AVAILABLE){
									matrixAttack[wallLine][wallCol+i] = matrixAttack[wallLine][wallCol+i]+2;
								}
							}
						}
						// -x
						for (int i = 1; i <= bombRange; i++) {
							checkInterruption();
							if (wallCol - i >= 0) {

								if (matrixAttack[wallLine][wallCol - i] < AVAILABLE) {
									i = bombRange + 1;
								} else if(matrixAttack[wallLine][wallCol-i]>=AVAILABLE){
									matrixAttack[wallLine][wallCol-i] = matrixAttack[wallLine][wallCol-i]+2;
								}
							}
						}
					}
				}
			}
			AiTile tileUp =currentTile.getNeighbor(Direction.UP); 
			AiTile tileDown =currentTile.getNeighbor(Direction.DOWN);
			AiTile tileRight =currentTile.getNeighbor(Direction.RIGHT);
			AiTile tileLeft =currentTile.getNeighbor(Direction.LEFT);
			if(!checkSafety(matriceDefence,zone,tileUp))
				matrixAttack[tileUp.getLine()][tileUp.getCol()]=0;
			if(!checkSafety(matriceDefence,zone,tileDown))
				matrixAttack[tileDown.getLine()][tileDown.getCol()]=0;
			if(!checkSafety(matriceDefence,zone,tileRight))
				matrixAttack[tileRight.getLine()][tileRight.getCol()]=0;
			if(!checkSafety(matriceDefence,zone,tileLeft))
				matrixAttack[tileLeft.getLine()][tileLeft.getCol()]=0;
			boolean safe=checkSafety(matriceDefence,zone,currentTile);
			if(!safe)
				matrixAttack[currentTile.getLine()][currentTile.getCol()]=0;
		
		int matrixBombing[][] = new int[line][col];
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matrixBombing[i][j]=matrixAvailable[i][j];
			}
   		}
		
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				int k =(int) Math.pow(2, matrixAttack[i][j]+3);
				if(matrixAttack[i][j]>0)
					fillMatrix(matrixBombing, k, zone.getTile(i, j));
			}
   		}

	
		boolean possible;
		possible=checkSafety(matriceDefence, zone, currentTile);
		if(!possible)
		matrixBombing[currentTile.getLine()][currentTile.getCol()]=0;	
		
		AiAction result = new AiAction(AiActionName.NONE);
		
		DEFENCE = ((matriceDefence[currentTile.getLine()][currentTile.getCol()][0]>DEFENCE_CONSTANT)
				|| (matriceDefence[currentTile.getLine()][currentTile.getCol()][0]==BOMB));
	
		if (DEFENCE) {
			result = findSafePlace(matriceDefence, matrixBonus, line, col, zone);
		} 
		else if(matrixBonus[currentTile.getLine()][currentTile.getCol()]>0){
			result=moveToHigherValue(matrixBonus, currentTile);
		}
		else {
			result=moveToHigherValueAttack(zone, matriceDefence, matrixBombing, matrixBonus, currentTile);
		}
		
		if(tilePrev==null || !(tilePrev.getLine()==currentTile.getLine()&&tilePrev.getCol()==currentTile.getCol()))
		tilePrev=currentTile;
		return result;
	}
	
	private AiAction moveToHigherValue(int[][] matrixBonus, AiTile tile) throws StopRequestException{
		checkInterruption();
		AiAction result= new AiAction(AiActionName.NONE);
		Direction dir=Direction.NONE;
		AiTile tileUp =tile.getNeighbor(Direction.UP); 
		int up=matrixBonus[tileUp.getLine()][tileUp.getCol()];
		AiTile tileDown =tile.getNeighbor(Direction.DOWN);
		int down=matrixBonus[tileDown.getLine()][tileDown.getCol()];
		AiTile tileRight =tile.getNeighbor(Direction.RIGHT);
		int right=matrixBonus[tileRight.getLine()][tileRight.getCol()];
		AiTile tileLeft =tile.getNeighbor(Direction.LEFT);
		int left=matrixBonus[tileLeft.getLine()][tileLeft.getCol()];
		int max =0;
		max=Math.max(max, up);
		max=Math.max(max, down);
		max=Math.max(max, left);
		max=Math.max(max, right);
		if(max==up)dir=Direction.UP;
		else if(max==down)dir=Direction.DOWN;
		else if(max==right)dir=Direction.RIGHT;
		else if(max==left)dir=Direction.LEFT;
		result =  new AiAction(AiActionName.MOVE,dir);
		return result;	
	}
	private AiAction moveToHigherValueAttack(AiZone zone, int[][][] matriceDefence, int[][] matrixBombing, int matrixBonus[][], AiTile tile) throws StopRequestException{
		checkInterruption();
		int up, right, left, down, now;
		AiAction result= new AiAction(AiActionName.NONE);
		AiTile tileUp =tile.getNeighbor(Direction.UP); 
		AiTile tileDown =tile.getNeighbor(Direction.DOWN);
		AiTile tileRight =tile.getNeighbor(Direction.RIGHT);
		AiTile tileLeft =tile.getNeighbor(Direction.LEFT);
		

		
		now=matrixBombing[tile.getLine()][tile.getCol()];
		up=matrixBombing[tileUp.getLine()][tileUp.getCol()];
		down=matrixBombing[tileDown.getLine()][tileDown.getCol()];
		left=matrixBombing[tileLeft.getLine()][tileLeft.getCol()];
		right=matrixBombing[tileRight.getLine()][tileRight.getCol()];
	
		int max =0;
			max=Math.max(max, up);
			max=Math.max(max, down);
			max=Math.max(max, left);
			max=Math.max(max, right);
			max=Math.max(max, now);
			boolean safe=checkSafety(matriceDefence,zone,tile);
		if(safe&&(max==now)){
				result = new AiAction(AiActionName.DROP_BOMB);
		}
		else if(!safe&&max==now){
			if(matrixBonus[tile.getLine()][tile.getCol()]>0)
				result = moveToHigherValue(matrixBonus, tile);
			else
				result = new AiAction(AiActionName.NONE);
			//available tarafa git
		}
		else if(max!=now){
			if(max==down&&max>0)
				result=new AiAction(AiActionName.MOVE, Direction.DOWN);
			else if(max==right&&max>0)
				result=new AiAction(AiActionName.MOVE, Direction.RIGHT);
			else if(max==left&&max>0)
				result=new AiAction(AiActionName.MOVE, Direction.LEFT);	
			else if(max==up&&max>0)
				result=new AiAction(AiActionName.MOVE, Direction.UP);
		}
		return result;	
	}
	private void initVisit() throws StopRequestException {
		checkInterruption();
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matrixBombTimes[i][j][1] = NON_VISITED;
			}
		}
	}
	
	//ca marche avec l'algorithme qu'on a appris en FIT 2 pour painter une cadre
	private void fillMatrixAvailable(AiTile tile, int[][][] matrixDefence, int[][] matrixAvailable) throws StopRequestException{
		checkInterruption();
		int tileLine = tile.getLine();
		int tileCol= tile.getCol();
		if(matrixAvailable[tileLine][tileCol]==INITIAL){
			if((matrixDefence[tileLine][tileCol][0]>=ITEM && matrixDefence[tileLine][tileCol][0]<DANGER_THRESHOLD)
					||(tileLine==currentTile.getLine()&&tileCol==currentTile.getCol())){
				matrixAvailable[tileLine][tileCol]=AVAILABLE;
				fillMatrixAvailable(tile.getNeighbor(Direction.UP), matrixDefence, matrixAvailable);
				fillMatrixAvailable(tile.getNeighbor(Direction.DOWN), matrixDefence, matrixAvailable);
				fillMatrixAvailable(tile.getNeighbor(Direction.RIGHT), matrixDefence, matrixAvailable);
				fillMatrixAvailable(tile.getNeighbor(Direction.LEFT),  matrixDefence, matrixAvailable);
			}
			else if(matrixDefence[tileLine][tileCol][0]<=BOMB||matrixDefence[tileLine][tileCol][0]>=DANGER_THRESHOLD)
				matrixAvailable[tileLine][tileCol]=NON_AVAILABLE;
			
		}
	}
	
	private void fillMatrix(int[][] matrix, int valeur, AiTile tile) throws StopRequestException{
		checkInterruption();
			int tileLine = tile.getLine();
			int tileCol= tile.getCol();
			if(matrix[tileLine][tileCol]>=AVAILABLE&&matrix[tileLine][tileCol]<valeur){
				matrix[tileLine][tileCol]=valeur;
				AiTile tileUp = tile.getNeighbor(Direction.UP);
				if(matrix[tileUp.getLine()][tileUp.getCol()]<valeur/2 && matrix[tileUp.getLine()][tileUp.getCol()]>=AVAILABLE)
					fillMatrix(matrix, valeur/2, tileUp);
				AiTile tileDown=tile.getNeighbor(Direction.DOWN);
				if(matrix[tileDown.getLine()][tileDown.getCol()]<valeur/2 && matrix[tileDown.getLine()][tileDown.getCol()]>=AVAILABLE)
					fillMatrix(matrix, valeur/2, tileDown);
				AiTile tileLeft=tile.getNeighbor(Direction.LEFT);
				if(matrix[tileLeft.getLine()][tileLeft.getCol()]<valeur/2 && matrix[tileLeft.getLine()][tileLeft.getCol()]>=AVAILABLE)
					fillMatrix(matrix, valeur/2, tileLeft);
				AiTile tileRight=tile.getNeighbor(Direction.RIGHT);
				if(matrix[tileRight.getLine()][tileRight.getCol()]<valeur/2 && matrix[tileRight.getLine()][tileRight.getCol()]>=AVAILABLE)
					fillMatrix(matrix, valeur/2, tileRight);
			}
			
	}
	


	private void initMatrixBombTimes() throws StopRequestException {
		checkInterruption();
		matrixBombTimes= new long[line][col][2];	
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				matrixBombTimes[i][j][0] = NON_BOMB;
				matrixBombTimes[i][j][1] = NON_VISITED;
			}
		}
		
	}
	

	
		


	
	/**
	 * @param matriceDefence matrice a regarder pour decision
	 * @param bomb1 le premier bomb
	 * @param bomb2 le seconde bomb
	 * @return true si l'un des bombs exploit l'autre 
	 * @throws StopRequestException 
	 */
	private boolean noObstacle(int[][][] matriceDefence, AiBomb bomb1,AiBomb bomb2) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		int bomb1Line = bomb1.getLine();
		int bomb1Col = bomb1.getCol();
		int bomb2Line = bomb2.getLine();
		int bomb2Col = bomb2.getCol();
		int bombRange = Math.max(bomb1.getRange(), bomb2.getRange());
		//si les lignes et les couloumn sont differentes, c'est deja obstacle
		if(bomb1Line!=bomb2Line && bomb1Col!=bomb2Col){
			result =false;
		}
		//si'ls sont sur meme lignes
		else if (bomb1Line == bomb2Line) {
			if(bombRange<Math.abs(bomb1Col-bomb2Col))
				result = false;
			else{
				checkInterruption();
				for (int i = Math.min(bomb1Col, bomb2Col) + 1 ; i < Math.max(bomb1Col, bomb2Col); i++)
				{	checkInterruption();
					if (matriceDefence[bomb1Line][i][0] < SAFE && matriceDefence[bomb1Line][i][0] != FIRE)
						result = false;
				}
			}
		} 
		else if (bomb1Col == bomb2Col) {
			if(bombRange<Math.abs(bomb1Line-bomb2Line))
				result = false;
			else{
				for (int i = Math.min(bomb1Line, bomb2Line) + 1 ; i < Math.max(bomb1Line,bomb2Line); i++)
				{	checkInterruption();
					if (matriceDefence[i][bomb1Col][0] < SAFE && matriceDefence[i][bomb1Col][0] != FIRE)
						result = false;
				}
			}
		}	
		return result;
	}

	private AiAction findSafePlace(int[][][] matriceDefence, int[][] matrixBonus, int line, int col,
			AiZone zone) throws StopRequestException {
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);

		// on demenage notre hero au sens moins dangereux
		AiHero ownHero = zone.getOwnHero();
		AiTile tile;
		AiTile tempTile = ownHero.getTile();
		AiTile ownTile = ownHero.getTile();
		Direction dirNew = Direction.NONE, tempDir = Direction.NONE;
		int dirOldValue = 1000000, dirNewValue=1000000;
		int heroLine = ownHero.getLine();
		int heroCol = ownHero.getCol();
		if (!isSafe(matriceDefence, heroLine, heroCol)) {
			Collection<AiTile> tiles = getClearNeighbors(ownHero.getTile(), matriceDefence);
			Iterator<AiTile> itAiTile = tiles.iterator();
			while (itAiTile.hasNext()) {
				checkInterruption();
				tile = itAiTile.next();			
				dirNew = zone.getDirection(ownTile, tile);

				dirNewValue = calculateCost(tile, ownTile, matriceDefence,zone);

				if (dirNewValue < dirOldValue) {
					tempDir = dirNew;
					dirOldValue = dirNewValue;
				}
					// s'il a deux choix de meme cout, il va comparer les
					// destinations et il va choisir plus libre ou qui a bonus
					// /*&& 
				else if (dirOldValue == dirNewValue &&(dirNewValue == SAFE||dirNewValue==ITEM)&& dirOldValue!=1000000) {
						// choisir direction ou il y a des bonus
					AiTile tileBonus = zone.getNeighborTile(ownTile, dirNew);
					AiTile tileRef = zone.getNeighborTile(ownTile, tempDir);
					if(matrixBonus[tileBonus.getLine()][tileBonus.getCol()]>matrixBonus[tileRef.getLine()][tileRef.getCol()]){
							tempDir = dirNew;
					}
					else if (dirNewValue == SAFE) {
						// daha acik alani sec
						Collection<AiTile> tiles1 = zone.getNeighborTiles(zone.getNeighborTile(ownTile, dirNew));
						Iterator<AiTile> itAiTiles1 = tiles1.iterator();
						AiTile tile1;
						while (itAiTiles1.hasNext()) {
							checkInterruption();
							tile1 = itAiTiles1.next();
							if (!tile1.equals(ownTile)
									&& (matriceDefence[tile1.getLine()][tile1.getCol()][0] == SAFE 
									||matriceDefence[tile1.getLine()][tile1.getCol()][0] == ITEM))
								tempDir = dirNew;
						}
					}
				}
				
			}

		}

		tempTile = ownTile.getNeighbor(tempDir);

		Direction direction = zone.getDirection(currentTile, tempTile);
        //600000 est possible parce que hero peur fuire au plus 10 care qui fera au plus 525000
		//si plus que ca, il ne fait pas sens de fuire
		if (currentTile != tempTile && dirOldValue < 1000000) {
			result = new AiAction(AiActionName.MOVE, direction);
		}//sinon, on va aller au care moins dangeroux, si un des bombes exploit et hero peut fuire
		else{
			int min = 1000000;
			if(matriceDefence[heroLine][heroCol][0]>=ITEM)
				min=matriceDefence[heroLine][heroCol][0];
			Collection<AiTile> tiles = zone.getNeighborTiles(ownHero.getTile());
			Iterator<AiTile> itAiTile = tiles.iterator();
			Direction dirLastChance=Direction.NONE;
			while (itAiTile.hasNext()) {
				checkInterruption();
				tile = itAiTile.next();
				if (matriceDefence[tile.getLine()][tile.getCol()][0]>=ITEM&&
						matriceDefence[tile.getLine()][tile.getCol()][0]<min) {
					dirLastChance = zone.getDirection(ownTile, tile);
					min = matriceDefence[tile.getLine()][tile.getCol()][0];
				}
			}
			result = new AiAction(AiActionName.MOVE, dirLastChance);
		}
		return result;

	}
	private long getRealTimes(int[][][] matriceDefence, AiBomb bomb, AiZone zone) throws StopRequestException{
		checkInterruption();
		markVisited(matrixBombTimes, bomb.getLine(), bomb.getCol());
		long result = matrixBombTimes[bomb.getLine()][bomb.getCol()][0];
		//premierement, on truvera les bomb qui exploit le bomb sujet
		Collection<AiBomb> bombs= zone.getBombs();
		Iterator<AiBomb> itBombs = bombs.iterator();
		AiBomb bombToTest;
		Collection<AiBomb> processBombs = new LinkedList<AiBomb>();
		while(itBombs.hasNext()){
			checkInterruption();
			bombToTest= itBombs.next();
			/**
			 * bomb n'est pas le meme qu'il va tester
			 * il n'ya aucun obstacle(l'un exploit l'autre)
			 * bomb n'est pas visite pendant recursion
			 */
			if(!bombToTest.equals(bomb) 
					&& !isVisited(matrixBombTimes, bombToTest.getLine(), bombToTest.getCol())
				    && noObstacle(matriceDefence, bomb, bombToTest))
					processBombs.add(bombToTest);	
		}
		
		if (!processBombs.isEmpty()){
			Iterator<AiBomb> itProcessBombs = processBombs.iterator();
			while (itProcessBombs.hasNext()) {
				checkInterruption();
				bombToTest=itProcessBombs.next();
				result = Math.min(result, getRealTimes(matriceDefence, bombToTest, zone));
			}
		}
			///gerekeni yaz


		return result;

	}

	private boolean isVisited(long[][][] matrixBombTimes, int line, int col) throws StopRequestException {
		//  Auto-generated method stub
		checkInterruption();
		return matrixBombTimes[line][col][1] == VISITED;
	}

	private boolean isVisited(int[][][] matriceDefence, int line, int col)
			throws StopRequestException {
		checkInterruption();
		return matriceDefence[line][col][1] == VISITED;
	}

	private void markVisited(long[][][] matrixBombTimes, int line, int col) throws StopRequestException {
		//  Auto-generated method stub
		checkInterruption();
		matrixBombTimes[line][col][1] = VISITED;
		
	}

	private void markVisited(int[][][] matriceDefence, int line, int col)
			throws StopRequestException {
		checkInterruption();
		matriceDefence[line][col][1] = VISITED;
	}

	private int calculateCost(AiTile aiTile, AiTile previousTile,
			int[][][] matriceDefence, AiZone zone) throws StopRequestException {
		checkInterruption();
		//Une care peut avoir le taux de danger au plus 52500 (prenons bomb range 15 et bombe en panne 3500ms)
		int result = 1000000, temp = 0;//un million
		if (matriceDefence[ aiTile.getLine()][aiTile.getCol()][0]==SAFE)
			result = 0;// pas de danger
		else if(matriceDefence[ aiTile.getLine()][aiTile.getCol()][0]==ITEM)
			result=-20;
		else {
			markVisited(matriceDefence, aiTile.getLine(), aiTile.getCol());

			Collection<AiTile> tiles = getClearNeighbors(aiTile,matriceDefence);// les voisins
			Iterator<AiTile> itAiTile = tiles.iterator();
			Collection<AiTile> processTiles = new LinkedList<AiTile>();
			AiTile tempTile;
			while (itAiTile.hasNext()) {
				checkInterruption();
				tempTile = itAiTile.next();
				if (!tempTile.equals(previousTile)
						&& !isVisited(matriceDefence, tempTile.getLine(),tempTile.getCol()))

					processTiles.add(tempTile);
			}

			if (processTiles.isEmpty())
				result = 1000000;
			else {
				Iterator<AiTile> itProcessTiles = processTiles.iterator();
				while (itProcessTiles.hasNext()) {
					checkInterruption();
					tempTile = itProcessTiles.next();
					if (!tempTile.equals(previousTile)
							&& isClear(tempTile, matriceDefence)) {

						temp = matriceDefence[aiTile.getLine()][aiTile.getCol()][0]
								+ calculateCost(tempTile, aiTile,
										matriceDefence, zone);

						if (temp < result)
							result = temp;
					}
				}

			}
		}

		return result;

	}

	private Collection<AiTile> getClearNeighbors(AiTile tile,
			int[][][] matriceDefence) throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE

		// liste des cases autour de la case de référence
		Collection<AiTile> neighbors = getPercepts().getNeighborTiles(tile);
		// on garde les cases sans bloc ni bombe ni feu
		List<AiTile> result = new ArrayList<AiTile>();
		Iterator<AiTile> it = neighbors.iterator();
		while (it.hasNext()) {
			checkInterruption(); // APPEL OBLIGATOIRE

			AiTile t = it.next();
			if (isClear(t, matriceDefence))
				result.add(t);
		}
		return result;
	}

	private boolean isClear(AiTile tile, int[][][] matriceDefence)
			throws StopRequestException {
		checkInterruption(); // APPEL OBLIGATOIRE
		return matriceDefence[tile.getLine()][tile.getCol()][0] >= ITEM;

	}

	/**
	 * @param matriceDefence
	 * @param line ligne de taille de requete
	 * @param col coloumn de taille de requete
	 * @return true s'il y'a pas de danger au taille ou c'est un bonus
	 * @throws StopRequestException
	 */
	private boolean isSafe(int matriceDefence[][][], int line, int col)
			throws StopRequestException {
		checkInterruption();
		return matriceDefence[line][col][0] == SAFE || matriceDefence[line][col][0] == ITEM;
	}


	// Si 
	/**
	 * @param matriceDefence matrice de defence
	 * @param zone le table de jeu
	 * @return la result de teste si on met un bombe, est-ce qu'on peut fuire
	 * @throws StopRequestException
	 */
	private boolean checkSafety(int[][][] matriceDefence, AiZone zone, AiTile tile)
			throws StopRequestException {
		checkInterruption();
		boolean result = false;
		int tempMatrice[][][] = new int[line][col][2];
		for (int i = 0; i < line; i++) {
			checkInterruption();
			for (int j = 0; j < col; j++) {
				checkInterruption();
				tempMatrice[i][j][0]=matriceDefence[i][j][0];
				tempMatrice[i][j][1]=NON_VISITED;
			}
	 		
	    }

		int bombRange = zone.getOwnHero().getBombRange();
		int bombCol = tile.getCol();
		int bombLine = tile.getLine();
		tempMatrice[bombLine][bombCol][0] = 10;
		// imitation, n'est pas reel mais s'il y a un bombe
		// +y
		for (int i = 1; i <= bombRange; i++) {
			checkInterruption();
			if (bombLine + i < line) {

				if (tempMatrice[bombLine + i][bombCol][0] < FIRE) {

					i = bombRange + 1;
				} else if (tempMatrice[bombLine + i][bombCol][0] >= FIRE
						&& tempMatrice[bombLine + i][bombCol][0] <= BOMB) {

					;
				} else {

					tempMatrice[bombLine + i][bombCol][0] = Math.max(tempMatrice[bombLine+ i][bombCol][0], bombRange - i + 1);
				}
			}
		}
		// -y
		for (int i = 1; i <= bombRange; i++) {
			checkInterruption();
			if (bombLine - i >= 0) {

				if (tempMatrice[bombLine - i][bombCol][0] < FIRE) {

					i = bombRange + 1;
				} else if (tempMatrice[bombLine - i][bombCol][0] >= FIRE
						&& tempMatrice[bombLine - i][bombCol][0] <= BOMB) {

					;
				} else {

					tempMatrice[bombLine - i][bombCol][0] = Math.max(tempMatrice[bombLine- i][bombCol][0], bombRange - i + 1);
				}
			}
		}
		// +x
		for (int i = 1; i <= bombRange; i++) {
			checkInterruption();
			if (bombCol + i <col) {

				if (tempMatrice[bombLine][bombCol + i][0] < FIRE) {

					i = bombRange + 1;
				} else if (tempMatrice[bombLine][bombCol + i][0] >= FIRE
						&& tempMatrice[bombLine][bombCol + i][0] <= BOMB) {

					;
				} else {

					tempMatrice[bombLine][bombCol + i][0] = Math.max(tempMatrice[bombLine][bombCol+ i][0], bombRange - i + 1);
				}
			}
		}
		// -x
		for (int i = 1; i <= bombRange; i++) {
			checkInterruption();
			if (bombCol - i >= 0) {

				if (tempMatrice[bombLine][bombCol - i][0] < FIRE) {
					// cest mur, danger n'est plus aux tailles suivantes. on ne
					// change pas les valeurs
					// donc on termine l'iteration dans ce sense
					i = bombRange + 1;
				} else if (tempMatrice[bombLine][bombCol - i][0] >= FIRE
						&& tempMatrice[bombLine][bombCol - i][0] <= BOMB) {
					// feu ou bombe; on l'ignore ca, et on continue mettre les
					// valeurs
					;// on ne fait rien
				} else {

					tempMatrice[bombLine][bombCol - i][0] = Math.max(tempMatrice[bombLine][bombCol- i][0], bombRange - i + 1);
				}
			}
		}

		AiTile tileTemp;
		int dirOldValue = 1000000, dirNewValue;
		Collection<AiTile> tiles = getClearNeighbors(tile, tempMatrice);
		Iterator<AiTile> itAiTile = tiles.iterator();
		while (itAiTile.hasNext()) {
			checkInterruption();
			tileTemp = itAiTile.next();
			dirNewValue = calculateCost(tileTemp, tile, tempMatrice, zone);
			if (dirNewValue < dirOldValue) {
				dirOldValue = dirNewValue;
			}

		}
		if (dirOldValue < CONSTANT_RUN_AWAY){// ***************************************************
			result = true;
			
		}
		return result;
	}

}
