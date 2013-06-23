package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v1;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

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
 * @version 1
 * 
 * @author Cansin Aldanmaz
 * @author Yalcin Yenigun
 *
 */
public class ZoneFormee {
	
//	private AiTile currentTile;	
//	private AiTile nextTile;	
//	private AiTile previousTile;
	@SuppressWarnings("unused")
	private AiZone zone;	
	@SuppressWarnings("unused")
	private AiHero ownPlayer;	
	@SuppressWarnings("unused")
	private Collection<AiHero> rivals;	
	private Collection<AiItem> bonus;	
	private Collection<AiBomb> bombes;	
	private Collection<AiBlock> blocks;	
	private Collection<AiFire> fires;
	
	private int DESTRUCTIBLEMUR = -2;	
	private int INDESTRUCTIBLEMUR = -1;	
//	private int SAFETILE = 0;	
	private int FIRE = 5000;	
	private int BOMBE = 2000;	
	private int BONUS = 1;	
	private int MALUS = 2;
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
		this.zone = zone;
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
				//matrixSafe[i][j] = EMPTY;
				matrixDanger[i][j] = EMPTY;
			}
		}				
		fillDangerMatrix();
	}
	
	/** methode pour rempir la matrice de danger */
	public void fillDangerMatrix(){
		
		putFires(matrixDanger);
		
		fillDangerLevels(matrixDanger);
		
		//mettre les murs
		putWalls(matrixDanger);
		
		//mettre les bonus
		//putBonusMalus(matrixDanger);
		
		//mettre les feux		
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
			else if(block.isDestructible())
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
		double time;
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
			
			time = bombe.getNormalDuration() - bombe.getTime();
			
			if(leftBlockList.isEmpty()){
				for(int r = 1; r <= bombe.getRange(); r++){
					if (x - r > 0){
						matrix[x - r][y] = (bombe.getRange() - r) / time;
						if(matrix[x - r][y] == BOMBE)
							matrix[x - r][y] = BOMBE;
						else if((matrix[x - r][y] == FIRE))
							matrix[x - r][y] = FIRE;
					}
					else
						break;
				}
			}
			
			if(rightBlockList.isEmpty()){
				for(int r = 1; r <= bombe.getRange(); r++){
					if (x + r < width){
						matrix[x + r][y] = (bombe.getRange() - r) / time;
						if(matrix[x + r][y] == BOMBE)
							matrix[x + r][y] = BOMBE;
						else if((matrix[x + r][y] == FIRE))
							matrix[x + r][y] = FIRE;
					}
					else
						break;
				}
			}
			
			if(downBlockList.isEmpty()){
				for(int r = 1; r <= bombe.getRange(); r++){
					if (y - r > 0){
						matrix[x][y - r] = (bombe.getRange() - r) / time;
						if(matrix[x][y - r] == BOMBE)
							matrix[x][y - r] = BOMBE;
						else if((matrix[x][y - r] == FIRE))
							matrix[x][y - r] = FIRE;
					}
					else
						break;
				}
			}
			
			if(upBlockList.isEmpty()){
				for(int r = 1; r <= bombe.getRange(); r++){
					if (y + r < heigh){
						matrix[x][y + r] = (bombe.getRange() - r) / time;
						if(matrix[x][y + r] == BOMBE)
							matrix[x][y + r] = BOMBE;
						else if((matrix[x][y + r] == FIRE))
							matrix[x][y + r] = FIRE;
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

	
}
