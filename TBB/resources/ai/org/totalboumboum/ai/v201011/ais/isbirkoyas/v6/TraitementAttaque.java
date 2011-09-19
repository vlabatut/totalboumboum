package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

import java.util.Collection;
import java.util.Iterator;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

public class TraitementAttaque {

	IsbirKoyas ai = new IsbirKoyas();
	double BOUGER = 100;
	double DISTANCE = 1000;

	public TraitementAttaque(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;

	}

	/**
	 * Cette méthode initialise la matrice attaque avec les '0'. Elle prend 2
	 * arguments, la zone du jeu et une matrice de type double.
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void initialiseMatrice(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {

				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matrice[i][j] = 0;
			}
		}
	}

	/**
	 * Cette méthode remplie la matrice attaque. .Elle donne des valeurs aux
	 * cases des bombes, des adversaires en fonction du temps et de la vitesse.
	 * Elle prend 2 arguments, la zone du jeu et une matrice de type double
	 * 
	 * @param matrice
	 *            La Matrice de attaque
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void attaqueMatrice(double[][] matrice, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		gameZone = ai.getPercepts();

		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bomb = iteratorBombs.next();
			matrice[bomb.getLine()][bomb.getCol()] = ai.ATTAQUE;
		}
		Collection<AiBlock> blocks = gameZone.getBlocks();
		Iterator<AiBlock> iteratorBlocks = blocks.iterator();
		while (iteratorBlocks.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBlock block = iteratorBlocks.next();
			matrice[block.getLine()][block.getCol()] = ai.ATTAQUE;
		}

		Collection<AiHero> heros = gameZone.getRemainingHeroes();
		heros.remove(ai.ourHero);
		Iterator<AiHero> iteratorHeros = heros.iterator();
		while (iteratorHeros.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiHero hero = iteratorHeros.next();
			int x = hero.getLine();
			int y = hero.getCol();
			double m = matrice[x - 1][y - 1] + matrice[x][y - 1]
					+ matrice[x + 1][y + 1] + matrice[x + 1][y]
					+ matrice[x + 1][y - 1] + matrice[x][y + 1]
					+ matrice[x - 1][y + 1] + matrice[x - 1][y];
			int distance = gameZone.getTileDistance(hero.getTile(),
					ai.ourHero.getTile());
			double valeur;
			if ((int) (hero.getCurrentSpeed()) == 0)
				valeur = m + BOUGER;
			else if (distance == 0)
				valeur = m + 1 / hero.getCurrentSpeed() + DISTANCE;
			else
				valeur = m + 1 / hero.getCurrentSpeed() + DISTANCE / distance;
			matrice[x][y] = valeur;
			int x1 = x, x2 = x, x3 = x, x4 = x;
			int y1 = y, y2 = y, y3 = y, y4 = y;

			// controle le bas
			if (matrice[x1 + 1][y1 + 1] == 1 && matrice[x1 + 1][y1 - 1] == 1
					&& matrice[x1 + 1][y1] != 1) {

				matrice[x1 + 2][y1] = valeur + 2;
				x1 = x1 + 1;
			}
			// controle le haut
			if (matrice[x2 - 1][y2 + 1] == 1 && matrice[x2 - 1][y2 - 1] == 1
					&& matrice[x2 - 1][y2] != 1) {

				matrice[x2 - 2][y2] = valeur + 2;
				x2 = x2 - 1;
			}
			// controle le droite
			if (matrice[x3 + 1][y3 + 1] == 1 && matrice[x3 - 1][y3 + 1] == 1
					&& matrice[x3][y3 + 1] != 1) {

				matrice[x3][y3 + 2] = valeur + 2;
				y3 = y3 + 1;
			}
			// controle la gauche
			if (matrice[x4 + 1][y4 - 1] == 1 && matrice[x4 - 1][y4 - 1] == 1
					&& matrice[x4][y4 - 1] != 1) {

				matrice[x4][y4 - 2] = valeur + 2;
				y4 = y4 - 1;
			}
		}

	}

	/**
	 * Cette méthode permet l'affichage de la matrice attaque en utilisant l'API
	 * fournie. Elle prend 2 arguments, la zone du jeu et une matrice de type
	 * double.
	 * 
	 * @param matrice
	 *            la matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	public void affiche(double[][] matrice, AiZone gameZone)
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

}
