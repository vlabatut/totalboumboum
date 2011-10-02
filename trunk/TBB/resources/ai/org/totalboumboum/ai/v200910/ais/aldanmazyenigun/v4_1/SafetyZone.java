package org.totalboumboum.ai.v200910.ais.aldanmazyenigun.v4_1;

import java.util.Collection;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBlock;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiFire;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiItem;
import org.totalboumboum.ai.v200910.adapter.data.AiStateName;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * 
 * @version 4.1
 * 
 * @author Cansın Aldanmaz
 * @author Yalçın Yenigün
 *
 */
@SuppressWarnings("deprecation")
public class SafetyZone {

	AldanmazYenigun ai;

	AiZone zone;

	private double matrix[][];
	
	private double attackMatrix[][];

	private double SAFE = 0;

	private double FIRE = 10000;

	private double BLOCKDEST = 80000;
	private double BLOCKINDEST =81111;
	

	private double BOMB = 11;
	private double BONUS =-1;
	
	private double HERO = -2;
	

	public SafetyZone(AldanmazYenigun ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
		zone = ai.getPercepts();
		matrix = new double[zone.getHeight()][zone.getWidth()];
		attackMatrix = new double[zone.getHeight()][zone.getWidth()];
		fillSafetyMatrix();
		fillAttackMatrix();
	}

	private void fillSafetyMatrix() throws StopRequestException {
		ai.checkInterruption();

		// init matrix
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matrix[line][col] = SAFE;
			}
		}

		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				AiTile tile = zone.getTile(line, col);
				Collection<AiFire> fires = tile.getFires();
				Collection<AiBomb> bombs = tile.getBombs();
				Collection<AiBlock> blocks = tile.getBlocks();
				Collection<AiItem> items= tile.getItems();

				// s'il y a du feu : valeur zéro (il ne reste pas de temps avant
				// l'explosion)
				if (!fires.isEmpty()) {
					matrix[line][col] = FIRE;
				}
				// s'il y a un mur en train de brûler : pareil
				/*
				 * else if(!heros.isEmpty()) matrix[line][col]=1;
				 */
			else if (!blocks.isEmpty()) {
					AiBlock block = blocks.iterator().next();
					if (block.getState().getName() == AiStateName.BURNING)
						matrix[line][col] = FIRE;
					else if(block.isDestructible())
						matrix[line][col] = BLOCKDEST;
					else
						matrix[line][col] =BLOCKINDEST;
				} 
				else if (bombs.size() > 0) {
					AiBomb bomb = bombs.iterator().next();
					AiTile tempTileRight = bomb.getTile();
					AiTile tempTileLeft = bomb.getTile();
					AiTile tempTileUp = bomb.getTile();
					AiTile tempTileDown = bomb.getTile();

					tempTileRight = tempTileRight.getNeighbor(Direction.RIGHT);
					Collection<AiBlock> neighBlocksRight = tempTileRight
							.getBlocks();

					tempTileLeft = tempTileLeft.getNeighbor(Direction.LEFT);
					Collection<AiBlock> neighBlocksLeft = tempTileLeft
							.getBlocks();

					tempTileUp = tempTileUp.getNeighbor(Direction.UP);
					Collection<AiBlock> neighBlocksUp = tempTileUp.getBlocks();

					tempTileDown = tempTileDown.getNeighbor(Direction.DOWN);
					Collection<AiBlock> neighBlocksDown = tempTileDown
							.getBlocks();

					double time = bomb.getNormalDuration() - bomb.getTime();
					//System.out.println(time);
					boolean normal = true;
					if (time < 0) {
						time = Math.abs(time);
						normal = false;
					}
					matrix[line][col] = BOMB;
					for (int i = 1; i <= bomb.getRange(); i++) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE

						if (col + i < zone.getWidth()) {
							if (neighBlocksRight.isEmpty()) {
								if (!bomb.isWorking() && time <= 500 &&(matrix[line][col + i] == 0||matrix[line][col + i] == 10000))
									matrix[line][col + i] = 0;
								else if (!bomb.isWorking() && time <= 1200 && matrix[line][col+i]<=10)
									matrix[line][col + i] = 10;
								else if (!bomb.isWorking() && time <= 2000 && matrix[line][col+i]<=70)
									matrix[line][col + i] = 70;
								else if (!bomb.isWorking() && time > 2000)
									matrix[line][col + i] = 190;
								if (bomb.isWorking()
										&& matrix[line][col + i] < (10000) / (time + 1)) {
									if (normal)
										matrix[line][col + i] = (10000) / (time + 1);
									else
										matrix[line][col + i] = 1000;
								}
							}
						}

						if (col - i >= 0) {
							if (neighBlocksLeft.isEmpty()) {
								if (!bomb.isWorking() && time <= 500 && (matrix[line][col - i] == 0|| matrix[line][col - i] == 10000))
									matrix[line][col - i] = 0;
								else if (!bomb.isWorking() && time <= 1200 && matrix[line][col-i]<=10)
									matrix[line][col - i] = 10;
								else if (!bomb.isWorking() && time <= 2000 && matrix[line][col-i]<=70)
									matrix[line][col - i] = 70;
								else if (!bomb.isWorking() && time > 2000)
									matrix[line][col - i] = 190;
								if (bomb.isWorking()
										&& matrix[line][col - i] < (10000) / (time + 1)) {
									if (normal)
										matrix[line][col - i] = (10000) / (time + 1);
									else
										matrix[line][col - i] = 1000;
								}
							}
						}

						if (line - i >= 0) {
							if (neighBlocksUp.isEmpty()) {
								if (!bomb.isWorking() && time <= 500 && (matrix[line - i][col] == 0 || matrix[line - i][col] == 10000))
									matrix[line - i][col] = 0;
								else if (!bomb.isWorking() && time <= 1200 && matrix[line-i][col]<=10)
									matrix[line - i][col] = 10;
								else if (!bomb.isWorking() && time <= 2000 && matrix[line-i][col]<=70)
									matrix[line - i][col] = 70;
								else if (!bomb.isWorking() && time > 2000)
									matrix[line - i][col] =190;
								if (bomb.isWorking()&& matrix[line - i][col] < (10000) / (time + 1))
								{
									if(normal)
										matrix[line-i][col] = (10000)	/ (time + 1);
										else
										matrix[line-i][col]=1000;
										}
								}
							}
						

						if (line + i < zone.getHeight()) {
							if (neighBlocksDown.isEmpty()) {
								if (!bomb.isWorking() && time <= 500 && (matrix[line + i][col] == 0|| matrix[line + i][col] == 10000))
									matrix[line + i][col] = 0;
								else if (!bomb.isWorking() && time <= 1200 && matrix[line+i][col]<=10)
									matrix[line + i][col] = 10;
								else if (!bomb.isWorking() && time <= 2000 && matrix[line+i][col]<=70)
									matrix[line + i][col] = 70;
								else if (!bomb.isWorking() && time > 2000)
									matrix[line + i][col] = 190;
								if (bomb.isWorking()
										&& matrix[line + i][col] < (10000) / (time + 1))
								{
									if(normal)
										matrix[line+i][col] = (10000)	/ (time + 1);
										else
										matrix[line+i][col]=1000;
										}
								}
									
									}
						}

					}
					else if (!items.isEmpty()) {
					AiItem bonus = items.iterator().next();
						if (bonus.getState().getName() == AiStateName.BURNING)
							matrix[line][col] = FIRE;
						else if(matrix[line][col] == SAFE)
							matrix[line][col] = BONUS;}
				}
			}
		}
	private void fillAttackMatrix() throws StopRequestException {
		ai.checkInterruption();

		// init matrix
		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				attackMatrix[line][col] = SAFE;
			}
		}

		for (int line = 0; line < zone.getHeight(); line++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE

			for (int col = 0; col < zone.getWidth(); col++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE

				AiTile tile = zone.getTile(line, col);
				Collection<AiFire> fires = tile.getFires();
				Collection<AiBomb> bombs = tile.getBombs();
				Collection<AiBlock> blocks = tile.getBlocks();
				Collection<AiHero> heros = tile.getHeroes();
				AiHero ownHero = zone.getOwnHero();

				// s'il y a du feu : valeur zéro (il ne reste pas de temps avant
				// l'explosion)
				if (!fires.isEmpty()) {
					attackMatrix[line][col] = FIRE;
				}
				else if (!blocks.isEmpty()) {
					AiBlock block = blocks.iterator().next();
					if (block.getState().getName() == AiStateName.BURNING)
						attackMatrix[line][col] = FIRE;
					else if(block.isDestructible())
						attackMatrix[line][col] = BLOCKDEST;
					else
						attackMatrix[line][col] =BLOCKINDEST;
				}
				// s'il y a un mur en train de brûler : pareil
				/*
				 * else if(!heros.isEmpty()) matrix[line][col]=1;
				 */
				
				else if (bombs.size() > 0) {
					AiBomb bomb = bombs.iterator().next();
					AiTile tempTileRight = bomb.getTile();
					AiTile tempTileLeft = bomb.getTile();
					AiTile tempTileUp = bomb.getTile();
					AiTile tempTileDown = bomb.getTile();
					
					tempTileRight = tempTileRight.getNeighbor(Direction.RIGHT);
					Collection<AiBlock> neighBlocksRight = tempTileRight
							.getBlocks();

					tempTileLeft = tempTileLeft.getNeighbor(Direction.LEFT);
					Collection<AiBlock> neighBlocksLeft = tempTileLeft
							.getBlocks();

					tempTileUp = tempTileUp.getNeighbor(Direction.UP);
					Collection<AiBlock> neighBlocksUp = tempTileUp.getBlocks();

					tempTileDown = tempTileDown.getNeighbor(Direction.DOWN);
					Collection<AiBlock> neighBlocksDown = tempTileDown
							.getBlocks();

					attackMatrix[line][col] = BOMB;
					for (int i = 1; i <= bomb.getRange(); i++) {
						ai.checkInterruption(); // APPEL OBLIGATOIRE

						if (col + i < zone.getWidth()) {
							if (neighBlocksRight.isEmpty()) {
										attackMatrix[line][col + i] = FIRE;
								}
						}
						if (col - i >= 0) {
							if (neighBlocksLeft.isEmpty()) {
									attackMatrix[line][col - i] = FIRE;
							}
						}
						if (line + i < zone.getHeight()) {
							if (neighBlocksDown.isEmpty()) {
									attackMatrix[line+i][col] = FIRE;
							}
						}
						if (line - i >= 0) {
							if (neighBlocksUp.isEmpty()) {
									attackMatrix[line-i][col] = FIRE;
							}
						}
					}
	
				}
				if(!ownHero.hasEnded()){
					if(ownHero.getTile() == tile){
						AiTile tempTileRight = ownHero.getTile();
						AiTile tempTileLeft = ownHero.getTile();
						AiTile tempTileUp = ownHero.getTile();
						AiTile tempTileDown = ownHero.getTile();

						tempTileRight = tempTileRight.getNeighbor(Direction.RIGHT);
						Collection<AiBlock> neighBlocksRight = tempTileRight
								.getBlocks();

						tempTileLeft = tempTileLeft.getNeighbor(Direction.LEFT);
						Collection<AiBlock> neighBlocksLeft = tempTileLeft
								.getBlocks();

						tempTileUp = tempTileUp.getNeighbor(Direction.UP);
						Collection<AiBlock> neighBlocksUp = tempTileUp.getBlocks();

						tempTileDown = tempTileDown.getNeighbor(Direction.DOWN);
						Collection<AiBlock> neighBlocksDown = tempTileDown
								.getBlocks();

						attackMatrix[line][col] = BOMB;
						for (int i = 1; i <= ownHero.getBombRange(); i++) {
							ai.checkInterruption(); // APPEL OBLIGATOIRE

							if (col + i < zone.getWidth()) {
								if (neighBlocksRight.isEmpty()) {
											attackMatrix[line][col + i] = FIRE;
									}
							}
							if (col - i >= 0) {
								if (neighBlocksLeft.isEmpty()) {
										attackMatrix[line][col - i] = FIRE;
								}
							}
							if (line + i < zone.getHeight()) {
								if (neighBlocksDown.isEmpty()) {
										attackMatrix[line+i][col] = FIRE;
								}
							}
							if (line - i >= 0) {
								if (neighBlocksUp.isEmpty()) {
										attackMatrix[line-i][col] = FIRE;
								}
							}
						}
					}
				}
				if(!heros.isEmpty())
				{
					AiHero hero = heros.iterator().next();
					if (hero!=ownHero)
						attackMatrix[line][col] = HERO;
				}
			}
		}
	}
			
	public double[][] getMatrix() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return matrix;
	}
	
	public double[][] getAttackMatrix() throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		return attackMatrix;
	}

	public double getCaseLevel(int line, int col) {
		return matrix[line][col];
	}

	public double getDangerLevel(AiTile tile) throws StopRequestException {
		ai.checkInterruption();
		int line = tile.getLine();
		int col = tile.getCol();
		double result = matrix[line][col];
		return result;
	}

	public boolean isBonus(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;
		
		if (matrix[y1][x1] == BONUS)
			resultat = true;
		
		return resultat;
	}

	public boolean isSafe(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;

		if (matrix[y1][x1] == SAFE || isBonus(x1,y1))
			resultat = true;
		return resultat;
	}
	
	public boolean abstractIsSafe(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;

		if (attackMatrix[y1][x1] == SAFE || attackMatrix[y1][x1] == HERO)
			resultat = true;
		return resultat;
	}
	
	public boolean isHero(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;

		if (attackMatrix[y1][x1] == HERO)
			resultat = true;
		return resultat;
	}
	
	public boolean iswall(int x1, int y1) throws StopRequestException {
		ai.checkInterruption();
		boolean resultat = false;
		AiTile tile = zone.getTile(y1, x1);
		Collection<AiBlock> blocks = tile.getBlocks();
		if(!blocks.isEmpty()){
			AiBlock block = blocks.iterator().next();
			if (matrix[y1][x1] == BLOCKDEST && block.isDestructible())
				resultat = true;
		}
		return resultat;

	}
}