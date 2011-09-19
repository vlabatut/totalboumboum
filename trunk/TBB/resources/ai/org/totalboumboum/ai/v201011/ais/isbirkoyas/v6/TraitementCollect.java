package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

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

	public int DESTRUCTIBLEINIT = 200;
	public final int BONUS = 1000;

	public TraitementCollect(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;

	}

	/**
	 * Cette méthode initialise la matrice collecte avec les '0'. Elle prend 2
	 * arguments, la zone du jeu et une matrice de type Int.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void initialiseMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matrice[i][j] = 0;
			}
		}
	}

	/**
	 * Cette méthode remplie la matrice collecte. Elle donne des valeurs aux
	 * cases des bonus en fonction du temps et en fonction de la distance. Elle
	 * prend 2 arguments, la zone du jeu et une matrice de type Int.
	 * 
	 * @param matrice
	 *            La Matrice de collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */

	public void collectMatrice(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
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
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiItem item = iteratorItems.next();
			matrice[item.getLine()][item.getCol()] = ai.bonusInit;
			plusProcheBombe(gameZone, item, matrice);
			plusProcheHero(matrice, gameZone, item);
			if (block.isDestructible()) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile neighbour = iteratorNeighbours.next();
				if (neighbour.getBlocks().isEmpty()
						&& neighbour.getBombs().isEmpty())
					matrice[neighbour.getLine()][neighbour.getCol()] = DESTRUCTIBLEINIT;
			}
		}
	}

	/**
	 * Cette méthode permet l'affichage de la matrice collecte en utilisant
	 * l'API fournie. Elle prend 2 arguments, la zone du jeu et une matrice de
	 * type Int.
	 * 
	 * @param matrice
	 *            la matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void affiche(int[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				System.out.print(matrice[i][j] + "\t");
			}
			System.out.println();
		}

		System.out.println("-----------------");
	}

	/**
	 * Cette méthode calcule la dur�e n�cessaire pour que notre IA arrive à la
	 * case du bonus cible. Elle prend deux arguments, la zone du jeu et les
	 * items du jeu.
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
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		AiTile bonusPos = bonus.getTile();
		AiTile MonPos = ai.ourHero.getTile();
		int distance = gameZone.getTileDistance(bonusPos, MonPos);
		double Vitesse = ai.ourHero.getWalkingSpeed();
		double T = distance / Vitesse;
		return T;
	}

	/**
	 * Cette méthode trouve la case du plus proche bombe au bonus et elle
	 * initialise cette case avec une valeur. Cette méthode contr�le les 4 cases
	 * voisines. Elle prend 3 arguments, la zone du jeu, les items du jeu et une
	 * matrice de type Int.
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
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		gameZone = ai.getPercepts();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombes = bombes.iterator();
		AiBomb tempbombe = null;
		int distance;
		double T, T2;
		while (iteratorBombes.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
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
					ai.bonusInit = BONUS / 2;
			}

		}
		matrice[bonus.getLine()][bonus.getCol()] = ai.bonusInit;

	}

	/**
	 * Cette méthode calcule le temps d'aller aux bonus du plus proche
	 * adversaire et elle initialise cette case avec une valeur. Elle prend 3
	 * arguments, la zone du jeu, les items du jeu et une matrice de type Int.
	 * 
	 * @param bonus
	 *            item bonus
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void plusProcheHero(int[][] matrice, AiZone gameZone, AiItem bonus)
			throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		int compte = 1;
		int tempDis = 0;
		List<AiHero> heros = gameZone.getRemainingHeroes();
		heros.remove(ai.ourHero);
		AiTile bonusPos = bonus.getTile();
		if (matrice[bonus.getLine()][bonus.getCol()] != -1) {
			for (int i = 0; i < heros.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiHero hero = heros.get(i);
				tempDis = gameZone.getTileDistance(bonusPos, hero.getTile());
				double tempVitesse = hero.getWalkingSpeed();
				double tempT = tempDis / tempVitesse
						- dureeAllerBonus(gameZone, bonus);
				if (tempT < 0)
					compte = compte + 1;
			}

			// compte=compte*10;
			ai.bonusInit = (ai.bonusInit / compte) - tempDis;
			matrice[bonus.getLine()][bonus.getCol()] = ai.bonusInit;
		}
	}

}
