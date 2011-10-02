package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v3;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiItemType;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 3
 * 
 * @author Cansın Aldanmaz
 * @author Yalçın Yenigün
 *
 */
@SuppressWarnings("deprecation")
public class ZoneFormee {
		
//	private AiTile currentTile;	
//	private AiTile nextTile;	
//	private AiTile previousTile;
	@SuppressWarnings("unused")
	private AiZone aiZone;	
	@SuppressWarnings("unused")
	private AiHero ownPlayer;	
	@SuppressWarnings("unused")
	private Collection<AiHero> rivals;	
	private Collection<AiItem> bonus;	
	private Collection<AiBomb> bombes;	
	private Collection<AiBlock> blocks;	
	private Collection<AiFire> fires;
	
	private int DESTRUCTIBLEMUR = 10000;	
	private int INDESTRUCTIBLEMUR = 20000;	
    private int SAFETILE = 0;	
	//private double FIRE = Double.MAX_VALUE;	
	private double FIRE = 50000;	
	private int BOMBE = 2000;	
	private double BONUS = 0.0001;	
	private double MALUS = 0.0002;
	private int EMPTY = 0;
//	private int ADVERSAIRE = 4;
//	private int DANGER = 100;
	
	private int width;
	
	private int heigh;
	
	private int x;
	
	private int y;
	
	/**represente si on est en danger ou pas*/
//	private boolean danger = Boolean.FALSE;
	
	/**
	 * si on est en danger on va utiliser cette matrice.
	 * il contient les niveux de danger par rapport aux valeurs numeriques
	 */
	private double matrixDanger[][];
	
	/**
	 * si on n'est pas en danger on va utiliser cette matrice
	 * il contient les places des bonus et des adversaires.
	 */
	//private double matrixSafe[][];
		
	/** constructeur de la classe ZoneFormee */
	public ZoneFormee(AiZone zone) {
		this.aiZone = zone;
		this.ownPlayer = zone.getOwnHero();

		this.rivals = zone.getHeroes();
		this.bombes = zone.getBombs();
		this.blocks = zone.getBlocks();
		this.bonus = zone.getItems();
		this.fires = zone.getFires();
		this.width = zone.getWidth();
		this.heigh = zone.getHeight();
		fillZone();
	}
	
	/** methode qui rempit la zone de jeu avec les valeurs adaptées */
	public void fillZone(){		
		
		matrixDanger = new double[width][heigh];
		int i,j;
		
		//initialisation des matrices.
		for (i = 0; i < width; i++) {
			for (j = 0; j < heigh; j++) {
				//System.out.println(width);
				//matrixSafe[i][j] = EMPTY;
				matrixDanger[i][j] = EMPTY;
			}
		}				
		fillDangerMatrix();
	}
	
	/** methode pour rempir la matrice de danger */
	public void fillDangerMatrix(){
		
		//mettre les murs
		putWalls(matrixDanger);
		//mettre les feux
		putFires(matrixDanger);
		
		fillDangerLevels(matrixDanger);
		
		//mettre les bonus
		putBonusMalus(matrixDanger);
				
	}
	
	/*
    methode pour remplir la matrice sur 
	public void fillSafeMatrix(){		
		//mettre les murs
		putWalls(matrixSafe);
		
		//mettre les feux
		putFires(matrixSafe);
		
		//mettre les bonus
		putBonusMalus(matrixSafe);	
		
		
	}
	*/
	
	/** methode qui met les valeurs pour les bonus et les malus dans la matrice */
	public void putBonusMalus(double matrix[][]){
		
		Iterator<AiItem> iterItem = bonus.iterator();
		AiItem item;

		while (iterItem.hasNext()) {
			item = iterItem.next();
			x = item.getCol();
			y = item.getLine();			
			if (matrix[x][y] != FIRE && item.getType() != AiItemType.MALUS)
				matrix[x][y] = BONUS;
			else if (matrix[x][y] != FIRE && item.getType() == AiItemType.MALUS)
				matrix[x][y] = MALUS;
		}
	}
	
	/** methode qui met les valeurs pour les murs dans la matrice */
	public void putWalls(double matrix[][]){
		Iterator<AiBlock> iterBlock = blocks.iterator();
		AiBlock block;

		//mettre les murs dans notre zone formée
		while (iterBlock.hasNext()) {
			block = iterBlock.next();
			x = block.getCol();
			y = block.getLine();
			if (!block.isDestructible())
				matrix[x][y] = INDESTRUCTIBLEMUR;
			else 
				matrix[x][y] = DESTRUCTIBLEMUR;
		}
	}
	
	/** méthode pour mettre les valeurs pour les feux dans la matrice */
	public void putFires(double matrix[][]){
		Iterator<AiFire> iterFire = fires.iterator();
		AiFire fire;

		while (iterFire.hasNext()) {
			fire = iterFire.next();
			x = fire.getCol();
			y = fire.getLine();
			matrix[x][y] = FIRE;
		}
	}
	

	
	public void fillDangerLevels(double matrix[][] ){
		AiTile bombeTile;
		AiBomb bombe;
		List<AiBlock> rightBlockList;
		List<AiBlock> leftBlockList;
		List<AiBlock> downBlockList;
		List<AiBlock> upBlockList;
	
		Iterator<AiBomb> iterBomb = bombes.iterator();
		while(iterBomb.hasNext()){
			bombe = iterBomb.next();
			x = bombe.getCol();
			y = bombe.getLine();
			bombeTile = bombe.getTile();	
			
			rightBlockList = bombeTile.getNeighbor(Direction.RIGHT).getBlocks();
			leftBlockList = bombeTile.getNeighbor(Direction.LEFT).getBlocks();
			downBlockList = bombeTile.getNeighbor(Direction.DOWN).getBlocks();
			upBlockList = bombeTile.getNeighbor(Direction.UP).getBlocks();
			
			double time = bombe.getNormalDuration() - bombe.getTime();

			//System.out.println(time);			
			if(leftBlockList.isEmpty()){
				for(int r = 0; r <= bombe.getRange(); r++){
					if (x - r >= 0){
						if(time < 0)
							matrix[x - r][y] = FIRE;
						else
						{
						if(bombe.getRange() - r != 0)
							{
							if(matrix[x - r][y]<50000 && matrix[x - r][y]>0)
								matrix[x - r][y]=matrix[x - r][y]+((bombe.getRange() - r)*1000) / (time + 1);
							else	
							matrix[x - r][y] = ((bombe.getRange() - r)*1000) /  Math.pow(10, (time+1));						
							}
						if(matrix[x - r][y] == BOMBE)
							matrix[x - r][y] = BOMBE;
						else if((matrix[x - r][y] == FIRE))
							matrix[x - r][y] = FIRE;
							
					}
					}
					else
						break;
				}
			}
			
			if(rightBlockList.isEmpty()){
				for(int r = 0; r <= bombe.getRange(); r++){
					if (x + r < width){
						if(time < 0)
							matrix[x+r][y]=FIRE;
						else{
							if(bombe.getRange() - r != 0)
							{
							if(matrix[x + r][y]<50000 && matrix[x + r][y]>0)
								matrix[x + r][y]=matrix[x + r][y]+((bombe.getRange() - r)*1000) / Math.pow(10, (time+1));
							else	
							matrix[x + r][y] = ((bombe.getRange() - r)*1000) / (time + 1);						
							}
						if(matrix[x + r][y] == BOMBE)
							matrix[x + r][y] = BOMBE;
						else if((matrix[x + r][y] == FIRE))
							matrix[x + r][y] = FIRE;							
					}
					}
					else
						break;
				}
			}

			if(upBlockList.isEmpty()){
				for(int r = 0; r <= bombe.getRange(); r++){
					if (y - r >= 0){
						if(time < 0)
							matrix[x][y-r] = FIRE;
						else{
							if(bombe.getRange() - r != 0)
							{
							if(matrix[x][y-r]<50000 && matrix[x][y-r]>0)
								matrix[x][y-r]=matrix[x][y-r]+((bombe.getRange() - r)*1000) /  Math.pow(10, (time+1));
							else	
							matrix[x ][y- r] = ((bombe.getRange() - r)*1000) / (time + 1);						
							}
						if(matrix[x][y - r] == BOMBE)
							matrix[x][y - r] = BOMBE;
						else if((matrix[x][y - r] == FIRE))
							matrix[x][y - r] = FIRE;
						}
					}
					else
						break;
				}
			}
			
			if(downBlockList.isEmpty()){
				for(int r = 0; r <= bombe.getRange(); r++){
					if (y + r < heigh){
						if(time<0)
							matrix[x][y + r] = FIRE;
						else {
							if(bombe.getRange() - r != 0)
							{
							if(matrix[x][y+r]<50000 && matrix[x][y+r]>0)
								matrix[x][y+r]=matrix[x][y+r]+((bombe.getRange() - r)*1000) /  Math.pow(10, (time+1));
							else	
							matrix[x][y+r] = ((bombe.getRange() - r)*1000) / (time + 1);						
							}
						if(matrix[x][y + r] == BOMBE)
							matrix[x][y + r] = BOMBE;
						else if((matrix[x][y + r] == FIRE))
							matrix[x][y + r] = FIRE;
						}
						
							
					}
					else
						break;
				}
			}
		}		
	}
	
	public double[][] returnMatrix() {
		return matrixDanger;
	}
	
	public boolean isSafe(int x1, int y1) {
		boolean resultat = false;

		if (matrixDanger[x1][y1] == EMPTY || matrixDanger[x1][y1] == SAFETILE)
			resultat = true;
		return resultat;
	}
	
	public boolean isThereSafeTiles(int x1, int y1){
		boolean resultat = false;

		if (matrixDanger[x1][y1] < 50000  && matrixDanger[x1][y1] > 0)
			resultat = true;
		return resultat;
	}
	
	public boolean isBonus(int x1, int y1) {
		boolean resultat = false;

		if (matrixDanger[x1][y1] == BONUS)
			resultat = true;
		return resultat;
	}
	
	/*
	public boolean isWall(int x1, int y1) {
		boolean resultat = false;

		if (matrixDanger[x1][y1] == DESTRUCTIBLEMUR)
			resultat = true;
		return resultat;
	}
	*/
	
	public double getDangerLevel(AiTile tile) throws StopRequestException
	{	
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrixDanger[line][col];
		return result;		
	}
	
	public double getSafe() throws StopRequestException{
		return EMPTY;
	}
	
	public void updateZone() throws StopRequestException{
		fillDangerMatrix();
	}
}
