package org.totalboumboum.ai.v201011.ais.isbirkoyas.v5;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

public class Securite {
	IsbirKoyas ai = new IsbirKoyas();
	public boolean secure;
	public AiPath nextMove2 = null;

	public Securite(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;
	}

	/**
	 * Cette méthode Contrôle si la case cible est vide ou pas et elle retourne
	 * un type bool�enne (danger). Elle prend 2 arguments la zone du jeu et la
	 * case cible.
	 * 
	 * @param AiTile
	 *            tile
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return poserBombe
	 */
	public boolean plein(AiZone gameZone, AiTile tile)
			throws StopRequestException {
		ai.checkInterruption();
		boolean plein = false;
		// s'il y a une bombe ou un feu dans le tile cible alors il y a danger
		if (!(tile.getBombs().isEmpty()) || !(tile.getFires().isEmpty())) {
			plein = true;
		}
		// s'il on est au portee du'une bombe alors il y a danger
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bomb = iteratorBombs.next();
			List<AiTile> tiles = bomb.getBlast();
			if (tiles.contains(tile)) {
				plein = true;
				break;
			}
		}
		return plein;
	}

	/**
	 * Cette méthode fait le Contrôle de sécurité du posage de la bombe de notre
	 * IA c'est-a-dire Contrôle si notre IA pose sa bombe ou pas. Elle prend un
	 * seul argument,la zone du jeu et elle retourne une liste.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return casSurs
	 */
	public List<AiTile> posageControle(AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();

		int[][] matriceBombe = new int[height][width];

		for (int i = 0; i < gameZone.getHeight(); i++) {

			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matriceBombe[i][j] = 0;
			}
		}

		List<AiTile> casSurs = new ArrayList<AiTile>();
		Collection<AiTile> blasts = ai.ourHero.getBombPrototype().getBlast();
		Iterator<AiTile> iteratorBlasts = blasts.iterator();
		while (iteratorBlasts.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile blast = iteratorBlasts.next();
			matriceBombe[blast.getLine()][blast.getCol()] = -1;

			List<AiTile> voisins = blast.getNeighbors();
			for (int i = 0; i < voisins.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (matriceBombe[voisins.get(i).getLine()][voisins.get(i)
						.getCol()] != -1)
					matriceBombe[voisins.get(i).getLine()][voisins.get(i)
							.getCol()] = 1;
			}
		}
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (plein(gameZone, gameZone.getTile(i, j))
						|| !gameZone.getTile(i, j).getBlocks().isEmpty())
					matriceBombe[i][j] = -1;
			}
		}
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (matriceBombe[i][j] == 1)
					casSurs.add(gameZone.getTile(i, j));
			}
		}

		return casSurs;
	}

	/**
	 * Cette méthode fait le Contrôle de sécurité du posage de la bombe par
	 * l�adversaire autour d�IA c�est-a-dire Contrôle si notre IA tombe en
	 * danger par la bombe de l�adversaire. Elle prend deux arguments la zone du
	 * jeu et une bombe du jeu. Elle retourne une liste.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param bombe
	 * @throws StopRequestException
	 * @return casSurs2
	 */

	public List<AiTile> bombeControle(AiZone gameZone, AiBomb bombe)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// Notre hero dans cette zone
		ai.ourHero = gameZone.getOwnHero();
		List<AiTile> casSurs2 = new ArrayList<AiTile>();
		// la matrice de collecte
		int[][] matriceBombe2 = new int[height][width];
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matriceBombe2[i][j] = 0;
			}
		}

		Collection<AiTile> blasts = bombe.getBlast();
		Iterator<AiTile> iteratorBlasts = blasts.iterator();
		while (iteratorBlasts.hasNext()) {

			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile blast = iteratorBlasts.next();
			matriceBombe2[blast.getLine()][blast.getCol()] = -1;
			List<AiTile> voisins = blast.getNeighbors();
			for (int i = 0; i < voisins.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (matriceBombe2[voisins.get(i).getLine()][voisins.get(i)
						.getCol()] != -1)
					matriceBombe2[voisins.get(i).getLine()][voisins.get(i)
							.getCol()] = 1;
			}
		}
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (matriceBombe2[i][j] == 1) {
					casSurs2.add(gameZone.getTile(i, j));
				}
			}
		}

		return casSurs2;
	}

	/**
	 * Cette méthode fait le Contrôle de sécurité de la prochaine case de notre
	 * IA. Elle prend deux arguments, la zone du jeu et un chemin (une séquence
	 * des cases).
	 * 
	 * @param ourHero
	 * @param gameZone
	 *            la zone du jeu
	 * @param nextMove
	 * @throws StopRequestException
	 * @return secure
	 */

	public boolean nextMoveSecurite(AiHero ourHero, AiZone gameZone,
			AiPath nextMove) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		secure = true;
		// La liste des tiles des bombes
		List<AiTile> bombeTileListe = new ArrayList<AiTile>();
		// La liste des bombes
		List<AiBomb> bombeListe = new ArrayList<AiBomb>();
		// La liste des blasts des bombes
		List<AiTile> bombeBlasts = new ArrayList<AiTile>();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombes.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bombe = iteratorBombs.next();
			bombeTileListe.add(bombe.getTile());
			bombeListe.add(bombe);
			bombeBlasts.addAll(bombe.getBlast());
			// si un feu va apparaitre à la case suivante (une bombe va exploser
			// jusqu'a notre arrivee)
			if (bombeBlasts.contains(ai.nextMove.getFirstTile())) {
				long temps = bombe.getNormalDuration() - bombe.getTime();
				if (temps < Double.MAX_VALUE) {
					secure = false;
					return secure;
				}
			}
		}

		// si une bombe va exploser avant notre arrivee, donc cette case nest
		// pas sure.
		if (!nextMove.isEmpty()) {
			for (int i = 0; i < bombeListe.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				if (nextMove.getFirstTile().getNeighbors()
						.contains(bombeTileListe.get(i))) {

					AiBomb bombe = bombeListe.get(i);
					long temps = bombe.getNormalDuration() - bombe.getTime();
					if (temps < Double.MAX_VALUE) {
						secure = false;
						return secure;
					}
				}
			}
			// s'il y a un feu cette case nest pas sure.

			if (!nextMove.getFirstTile().getFires().isEmpty()) {
				secure = false;
				return secure;
			}

		}
		if (ai.print)
			System.out.println("SECURE:" + secure);
		return secure;
	}

}