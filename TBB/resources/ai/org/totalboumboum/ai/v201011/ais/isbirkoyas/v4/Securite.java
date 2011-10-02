package org.totalboumboum.ai.v201011.ais.isbirkoyas.v4;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiFire;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;

/**
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class Securite {
	IsbirKoyas ai = new IsbirKoyas();
	public boolean print = false;
	public boolean secure;
	public AiPath nextMove2 = null;

	public Securite(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption();
		this.ai = ai;

	}

	/**
	 * Methode qui controle si une case est vide ou pas
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
		// s'il y a une bombe ou un bonus dans le tile cible alors il y a danger
		if (!(tile.getBombs().isEmpty()) || !(tile.getFires().isEmpty())) {
			ai.plein = true;
		}
		// s'il on est au portee du'une bombe alors il y a danger
		Collection<AiBomb> bombs = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombs.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiBomb bomb = iteratorBombs.next();
			List<AiTile> tiles = bomb.getBlast();
			if (tiles.contains(tile)) {
				ai.plein = true;
				break;
			}
		}
		return ai.plein;
	}

	/**
	 * methode qui calcule si notre IA peut passer sans tomber a la portee d'une
	 * bombe
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param AiTile
	 *            tile
	 * @return resultat
	 */
	public boolean controle(AiTile tile, AiZone gameZone)
			throws StopRequestException {
		ai.checkInterruption();

		boolean resultat = false;

		if (tile.isCrossableBy(ai.ourHero))
			resultat = true;

		return resultat;

	}

	/**
	 * Methode qui retourne les cases qui ont des dangers comme bombes,flammes
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return dangerPoints
	 */
	public List<AiTile> casDanger(AiZone gameZone) throws StopRequestException {
		List<AiTile> dangerPoints = new ArrayList<AiTile>();
		List<AiTile> dangerPointsBlast = new ArrayList<AiTile>();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombes.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiBomb bombe = iteratorBombs.next();
			dangerPointsBlast = bombe.getBlast();
			dangerPoints.add(bombe.getTile());
		}

		Collection<AiFire> fires = gameZone.getFires();
		Iterator<AiFire> iteratorFires = fires.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiFire fire = iteratorFires.next();
			dangerPoints.add(fire.getTile());
		}

		for (int i = 0; i < dangerPointsBlast.size(); i++)
			dangerPoints.add(dangerPointsBlast.get(i));

		return dangerPoints;

	}

	/**
	 * Methode qui controle si notre IA pose sa bombe ou pas
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 * @return casSurs
	 */
	public List<AiTile> PosageControle(AiZone gameZone)
			throws StopRequestException {
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// Notre hero dans cette zone
		ai.ourHero = gameZone.getOwnHero();
		// la matrice de collecte
		int[][] matriceBombe = new int[height][width];
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();
				matriceBombe[i][j] = 0;
			}
		}
		List<AiTile> casSurs = new ArrayList<AiTile>();
		Collection<AiTile> blasts = ai.ourHero.getBombPrototype().getBlast();
		Iterator<AiTile> iteratorBlasts = blasts.iterator();
		while (iteratorBlasts.hasNext()) {
			AiTile blast = iteratorBlasts.next();
			matriceBombe[blast.getLine()][blast.getCol()] = -1;
			List<AiTile> voisins = blast.getNeighbors();
			for (int i = 0; i < voisins.size(); i++)
				if (matriceBombe[voisins.get(i).getLine()][voisins.get(i)
						.getCol()] != -1)
					matriceBombe[voisins.get(i).getLine()][voisins.get(i)
							.getCol()] = 1;
		}

		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				if (matriceBombe[i][j] == 1) {
					if (plein(gameZone, gameZone.getTile(i, j))
							|| !gameZone.getTile(i, j).getBlocks().isEmpty())
						matriceBombe[i][j] = -1;
					else
						casSurs.add(gameZone.getTile(i, j));
				}
			}
		}

		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				if (print)
					System.out.print(matriceBombe[i][j] + "\t");
			}
			if (print)
				System.out.println();
		}

		return casSurs;
	}

	/**
	 * Methode qui controle si l'adversaire a poser une bombe autour de notre IA
	 * et estce que il y a une bombe au premier tile de son nextMove
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param bombe
	 * @throws StopRequestException
	 * @return casSurs2
	 */

	public List<AiTile> PosageControle2(AiZone gameZone, AiBomb bombe)
			throws StopRequestException {
		int width = gameZone.getWidth();
		// la largeur de la zone
		int height = gameZone.getHeight();
		// Notre hero dans cette zone
		ai.ourHero = gameZone.getOwnHero();
		List<AiTile> casSurs2 = new ArrayList<AiTile>();
		// la matrice de collecte
		int[][] matriceBombe2 = new int[height][width];
		for (int i = 0; i < gameZone.getHeight(); i++) {
			ai.checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) {
				ai.checkInterruption();
				matriceBombe2[i][j] = 0;
			}
		}

		Collection<AiTile> blasts = bombe.getBlast();
		Iterator<AiTile> iteratorBlasts = blasts.iterator();
		while (iteratorBlasts.hasNext()) {
			AiTile blast = iteratorBlasts.next();
			matriceBombe2[blast.getLine()][blast.getCol()] = -1;
			List<AiTile> voisins = blast.getNeighbors();
			for (int i = 0; i < voisins.size(); i++)
				if (matriceBombe2[voisins.get(i).getLine()][voisins.get(i)
						.getCol()] != -1)
					matriceBombe2[voisins.get(i).getLine()][voisins.get(i)
							.getCol()] = 1;
		}
		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				if (matriceBombe2[i][j] == 1) {
					casSurs2.add(gameZone.getTile(i, j));
				}
			}
		}

		for (int i = 0; i < gameZone.getHeight(); i++) {
			for (int j = 0; j < gameZone.getWidth(); j++) {
				if (print)
					System.out.print(matriceBombe2[i][j] + "\t");
			}
			if (print)
				System.out.println();
		}
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++");

		return casSurs2;
	}
	/**
	 * Methode qui controle si le nextMove de notre IA est une case sans danger
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
		secure = true;

		AiPath path = new AiPath();

		List<AiTile> bombeTileListe = new ArrayList<AiTile>();
		List<AiBomb> bombeListe = new ArrayList<AiBomb>();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombs = bombes.iterator();
		while (iteratorBombs.hasNext()) {
			ai.checkInterruption();
			AiBomb bombe = iteratorBombs.next();
			bombeTileListe.add(bombe.getTile());
			bombeListe.add(bombe);

		}
		// si une bombe va exploser avant notre arrivee, donc on ne va pas là.
		if (!nextMove.isEmpty()) {
			AiTile tile = nextMove.getFirstTile();
			path.addTile(0, tile);
			long temps = path.getDuration(ourHero);

			for (int i = 0; i < bombeListe.size(); i++) {
				if (nextMove.getFirstTile().getNeighbors()
						.contains(bombeTileListe.get(i))) {
					long temps2 = bombeListe.get(i).getNormalDuration();// +bombeListe.get(i).getExplosionDuration();
					if (temps > temps2) {
						secure = false;
						break;
					}
				}
			}
		}
		// s'il y a un feu.

		if (nextMove.isEmpty())
			if (!nextMove.getFirstTile().getFires().isEmpty())
				secure = false;

		return secure;
	}

}