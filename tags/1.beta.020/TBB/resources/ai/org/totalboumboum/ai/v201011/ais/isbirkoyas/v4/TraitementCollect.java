package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

public class TraitementCollect {
	IsbirKoyas ai = new IsbirKoyas();
	public boolean print = false;

	public TraitementCollect(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

	}

	/**
	 * Methode qui initialise la matrice collecte avec les '0'.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void initialiseMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();
				matrice[i][j] = 0;
			}
		}
	}

	/**
	 * Methode qui remplie la matrice collecte avec les bonus en fonction du
	 * temps
	 * 
	 * @param matrice
	 *            La Matrice de collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */

	public void collectMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();

		gameZone = ai.getPercepts();
		Collection<AiItem> items = gameZone.getItems();
		Iterator<AiItem> iteratorItems = items.iterator();
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		AiBlock block = iteratorBlocks.next();
		Collection<AiTile> neighbours = block.getTile().getNeighbors();
		Iterator<AiTile> iteratorNeighbours = neighbours.iterator();
		while (iteratorItems.hasNext() && iteratorBlocks.hasNext()
				&& iteratorNeighbours.hasNext()) {
			ai.checkInterruption();
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = ai.bonusInit;
			plusProcheBombe(gameZone, item, matrice);
			plusProcheHero(matrice, gameZone, item);
			ai.checkInterruption();

			if (block.isDestructible()) {
				ai.checkInterruption();
				AiTile neighbour = iteratorNeighbours.next();
				if (neighbour.getBlocks().isEmpty()
						&& neighbour.getBombs().isEmpty())
					matrice[neighbour.getLine()][neighbour.getCol()] = ai.DESTRUCTIBLEINIT;
			}
		}
	}

	/**
	 * Methode qui permet l'affichage de la matrice collecte en utilisant l'API
	 * fournie
	 * 
	 * @param matrice
	 *            la matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void affiche(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				System.out.print(matrice[i][j] + "\t");
			}
			System.out.println();
		}

		System.out.println("-----------------");
	}

	/**
	 * Methode qui calcule la duree necessaire pour aller a la case du bonus
	 * 
	 * @param bonus
	 *            item bonus
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return T
	 */
	public double dureeAllerBonus(AiZone gameZone, AiItem bonus)
			throws StopRequestException {
		ai.checkInterruption();
		AiTile bonusPos = bonus.getTile();
		AiTile MonPos = ai.ourHero.getTile();
		int distance = gameZone.getTileDistance(bonusPos, MonPos);
		double Vitesse = ai.ourHero.getWalkingSpeed();
		double T = distance / Vitesse;
		return T;
	}

	/**
	 * Methode qui trouve le plus proche bombe au bonus cette methode controle
	 * les 4 cases voisins
	 * 
	 * @param bonus
	 *            item bonus
	 * @param gameZone
	 *            la zone du jeu
	 * @param matrice
	 *            La Matrice de collecte
	 * @throws StopRequestException
	 */
	public void plusProcheBombe(AiZone gameZone, AiItem bonus, int[][] matrice)
			throws StopRequestException {
		ai.checkInterruption();

		gameZone = ai.getPercepts();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombes = bombes.iterator();
		AiBomb tempbombe = null;
		int distance;
		double T, T2;
		while (iteratorBombes.hasNext()) {
			ai.checkInterruption();
			tempbombe = iteratorBombes.next();
			AiTile bonusPos = bonus.getTile(), bombePos = tempbombe.getTile();
			Direction dir = gameZone.getDirection(bonusPos.getPosX(),
					bonusPos.getPosY(), bombePos.getPosX(), bombePos.getPosY());
			
			if (dir == Direction.UP || dir == Direction.DOWN
					|| dir == Direction.LEFT || dir == Direction.RIGHT) {
				distance = gameZone.getTileDistance(bonusPos, bombePos);
				
				T = tempbombe.getExplosionDuration()
						+ tempbombe.getFailureProbability()
						- tempbombe.getLatencyDuration();
				T2 = T - dureeAllerBonus(gameZone, bonus);
				

				if (T2 < 0)
					ai.bonusInit = -1;
				else
				// la distance d'effet va etre calculee, a completer
				if (distance < 2)
					ai.bonusInit = ai.BONUS / 2;
			}

		}
		matrice[bonus.getLine()][bonus.getCol()] = ai.bonusInit;

	}

	/**
	 * Methode qui calcule le temps d'aller au bonus du plus proche adversaire
	 * 
	 * @param bonus
	 *            item bonus
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void plusProcheHero(int[][] matrice, AiZone gameZone, AiItem bonus)
			throws StopRequestException {

		ai.checkInterruption();
		int compte = 1;
		List<AiHero> heros = gameZone.getRemainingHeroes();
		heros.remove(ai.ourHero);
		AiTile bonusPos = bonus.getTile();
		if (matrice[bonus.getLine()][bonus.getCol()] != -1) {
			ai.checkInterruption();
			for (int i = 0; i < heros.size(); i++) {
				ai.checkInterruption();
				AiHero hero = heros.get(i);
				int tempDis = gameZone
						.getTileDistance(bonusPos, hero.getTile());
				double tempVitesse = hero.getWalkingSpeed();
				double tempT = tempDis / tempVitesse
						- dureeAllerBonus(gameZone, bonus);
				// System.out.println("hero:"+hero);
				if (tempT < 0)
					compte = compte + 1;
			}
			ai.bonusInit = ai.bonusInit / compte;
			matrice[bonus.getLine()][bonus.getCol()] = ai.bonusInit;
		}
	}

}
