package org.totalboumboum.ai.v200910.ais.bektasmazilyah.v2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * classe principale de l'IA, qui définit son comportement.
 * n'hésitez pas à décomposer le traitement en plusieurs classes,
 * plus votre programme est modulaire et plus il sera facile à
 * débugger, modifier, relire, comprendre, etc.
 * 
 * @version 2
 * 
 * @author Erdem Bektas
 * @author Nedim Mazilyah
 *
 */
public class BektasMazilyah extends ArtificialIntelligence
{	
	private AiZone zone;
	// le personnage dirig� par cette IA
	private AiHero hero;
	//la prochaine action que l'IA veut r�aliser
	private AiAction action;
	// la dernière case par laquelle on est passé 
	@SuppressWarnings("unused")
	private AiTile previousTile;
	// la case occup�e actuellement par le personnage
	private AiTile currentTile;
	// la case sur laquelle on veut aller 
	private AiTile nextTile;
	//La Zone
	private int width;
	private int height;
	
	/** VARIABLES DE STRATEGIE **/
	private boolean defence=true;
	//private boolean bonus=false;
	//private boolean attack=false;
	
    /*** DEFENCE CONSTANTS ***/
	private final int BLOCK_INDEST=-4;
	private final int BLOCK_DEST=-3;
	private final int FIRE=-2;
	private final int BOMB=-1;
	private final int SAFE=0;
	private final int NON_VISITED=0;
	private final int VISITED=1;
	private final int CONSTANT_RUN_AWAY=10;
	
	private final int INITIAL = -2;
	
	//les tailles accesibles de notre hero
	int[][] matrixAvailable= new int[height][width];	
	
	//MATRICE SERVANT A DEFENCE STRATEGIE
	int matriceDefence[][][] = new int[height][width][2];
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
	
		initMonia();
		//on init la matrice avec tous zero
		initMatrice();
		//on mis a jours la matrice
		updateMatrice();
		
		defence=!isSafe(matriceDefence,currentTile.getLine(),currentTile.getCol());
		
		if(defence)
			action = findSafePlace(matriceDefence, height, width, zone);
		else
			action = ramasserDesBonus();
		
		return action;
	}
	

	private AiAction ramasserDesBonus() {
		return null;
	}


	private void initMonia() throws StopRequestException
	{
		zone = getPercepts();
		hero = zone.getOwnHero();
		action = new AiAction(AiActionName.NONE);
		width = zone.getWidth();
		height = zone.getHeight();
		currentTile = hero.getTile();
		
	}
	
	private void initMatrice() throws StopRequestException
	{
		for (int i=0; i<height; i++)
		{
			checkInterruption();
			for (int j=0; j<width; j++)
			{
				checkInterruption();
				matriceDefence[i][j][0]=SAFE;
				matriceDefence[i][j][1]=NON_VISITED;
			}
		}
		
		int[][] matrixBonus= new int[height][width];
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				matrixBonus[i][j]=matrixAvailable[i][j];
			}
   		}
		
		int[][] matrixAvailable= new int[height][width];	
		for (int i = 0; i < height; i++) {
			checkInterruption();
			for (int j = 0; j < width; j++) {
				checkInterruption();
				matrixAvailable[i][j]=INITIAL;
			}
		}
		
	}
	
	private void updateMatrice() throws StopRequestException
	{
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
		Collection<AiBomb> bombs = zone.getBombs();
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
				if(bombLine+i<height){
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
				if(bombCol+i<width-1){
				
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
		}
	}

	
	
	private boolean isSafe(int[][][] matriceDefence2, int line, int col) throws StopRequestException {
		checkInterruption();
		return matriceDefence[line][col][0]==0;
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
			Collection<AiTile> tiles=getClearNeighbors(ownHero.getTile());
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
						Collection<AiTile> tiles1=getClearNeighbors(ownTile);
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

		if(currentTile!=nextTile && dirOldValue<1000){
			result = new AiAction(AiActionName.MOVE,direction);
		}
		return result;
		
	}

	private List<AiTile> getClearNeighbors(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		// liste des cases autour de la case de r�f�rence
		List<AiTile> neighbors = tile.getNeighbors();
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

	public Collection<AiTile> getNeighborTiles(AiTile tile)
	{	Collection<AiTile> result = new ArrayList<AiTile>();
		List<Direction> directions = Direction.getPrimaryValues();
		Iterator<Direction> d = directions.iterator();
		while(d.hasNext())
		{	Direction dir = d.next();
			AiTile neighbor = getNeighborTile(tile, dir);
			result.add(neighbor);
		}
		result = Collections.unmodifiableCollection(result);
		return result;
	}


	public AiTile getNeighborTile(AiTile tile, Direction direction)
	{	AiTile result = null;
		int c,col=tile.getCol();
		int l,line=tile.getLine();
		Direction p[] = direction.getPrimaries(); 
		//
		if(p[0]==Direction.LEFT)
			c = (col+width-1)%width;
		else if(p[0]==Direction.RIGHT)
			c = (col+1)%width;
		else
			c = col;
		//
		if(p[1]==Direction.UP)
			l = (line+height-1)%height;
		else if(p[1]==Direction.DOWN)
			l = (line+1)%height;
		else
			l = line;
		//
		result = getTile(l,c);
		return result;
	}
	
	public AiTile getTile(int line, int col)
	{	
		return matrix[line][col];
	}
	
	/** matrice représentant la zone et tous les sprites qu'elle contient */
	private AiTile[][] matrix;
	/** niveau représent� par cette classe */

	private boolean isClear(AiTile tile, int[][][] matriceDefence) throws StopRequestException
	{	
		checkInterruption(); //APPEL OBLIGATOIRE
		return matriceDefence[tile.getLine()][tile.getCol()][0]>=0;
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
	
		// liste des cases autour de la case de r�f�rence
		Collection<AiTile> neighbors = getNeighborTiles(tile);
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
	
	private boolean isVisited(int[][][] matriceDefence, int line, int col) throws StopRequestException{
		checkInterruption();
		return matriceDefence[line][col][1]==VISITED;
	}

	private void markVisited(int[][][] matriceDefence, int line, int col) throws StopRequestException{
		checkInterruption();
		matriceDefence[line][col][1]=VISITED;
	}

	//Si on met un bombe, est-ce qu'on peut fuire
	@SuppressWarnings("unused")
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
			if(bombLine+i<height){
				
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
			if(bombCol+i<width-1){
				
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
}