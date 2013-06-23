package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class Securite {
	/** */
	IsbirKoyas ai = new IsbirKoyas();
	/** */
	public boolean secure;
	/** */
	public AiPath nextMove2 = null;

	/**
	 * 
	 * @param ai
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public Securite(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

	}

	/**
	 * Cette méthode Contrôle si la case cible est vide ou pas et elle retourne
	 * un type booléenne (danger). Elle prend 2 arguments la zone du jeu et la
	 * case cible.
	 * 
	 * @param tile
	 *            tile
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * 		description manquante !
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
				return plein;
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
	 * 		description manquante !
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
	 * làadversaire autour d'IA c'est-a-dire Contrôle si notre IA tombe en
	 * danger par la bombe de làadversaire. Elle prend deux arguments la zone du
	 * jeu et une bombe du jeu. Elle retourne une liste.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param bombe
	 * 		description manquante !
	 * @throws StopRequestException
	 * 		description manquante !
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

		List<AiTile> blastsTous = new ArrayList<AiTile>();
		List<AiBomb> bombes = gameZone.getBombs();
		if (!bombes.isEmpty()) {
			for (int i = 0; i < bombes.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiBomb bomb = bombes.get(i);
				blastsTous.addAll(bomb.getBlast());
			}
		}

		if (!blastsTous.isEmpty()) {
			for (int i = 0; i < blastsTous.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				matriceBombe2[blastsTous.get(i).getLine()][blastsTous.get(i)
						.getCol()] = -1;
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
	 * Cette méthode nous donne une liste de direction pour notre IA. Elle prend
	 * deux arguments.
	 * 
	 * @param dx2
	 * 		description manquante !
	 * @param dy2
	 * 		description manquante !
	 * @return directions2
	 * 
	 * @throws StopRequestException
	 * 		description manquante !
	 */
	public List<Direction> directionToute(int dx2, int dy2)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		List<Direction> directions2 = new ArrayList<Direction>();
		if (dx2 < 0 && dy2 == 0) {
			directions2.add(Direction.UP);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.UPRIGHT);
		} else if (dx2 < 0 && dy2 < 0) {
			directions2.add(Direction.UP);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.LEFT);

		} else if (dx2 == 0 && dy2 < 0) {
			directions2.add(Direction.LEFT);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.DOWNLEFT);
		} else if (dx2 > 0 && dy2 == 0) {
			directions2.add(Direction.DOWN);
			directions2.add(Direction.DOWNLEFT);
			directions2.add(Direction.DOWNRIGHT);
		} else if (dx2 > 0 && dy2 > 0) {
			directions2.add(Direction.DOWN);
			directions2.add(Direction.DOWNRIGHT);
			directions2.add(Direction.RIGHT);
		} else if (dx2 == 0 && dy2 > 0) {
			directions2.add(Direction.RIGHT);
			directions2.add(Direction.UPRIGHT);
			directions2.add(Direction.DOWNRIGHT);
		} else if (dx2 > 0 && dy2 < 0) {
			directions2.add(Direction.DOWN);
			directions2.add(Direction.LEFT);
			directions2.add(Direction.DOWNLEFT);
		} else if (dx2 < 0 && dy2 > 0) {
			directions2.add(Direction.UP);
			directions2.add(Direction.UPRIGHT);
			directions2.add(Direction.RIGHT);
		}

		return directions2;
	}
}