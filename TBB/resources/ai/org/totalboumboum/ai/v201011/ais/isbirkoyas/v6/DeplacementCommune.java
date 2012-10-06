package org.totalboumboum.ai.v201011.ais.isbirkoyas.v6;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.totalboumboum.ai.v201011.adapter.data.AiStateName;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**constructeur
 * 
 *
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings("deprecation")
public class DeplacementCommune {

	IsbirKoyas ai = new IsbirKoyas();
	// la direction de la case d'attaque
	Direction dir2 = Direction.NONE;

	/**
	 * 
	 * @param ai
	 * @throws StopRequestException
	 */
	public DeplacementCommune(IsbirKoyas ai) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		this.ai = ai;
	}

	/**
	 * 
	 * Cette méthode calcule le chemin le plus court que notre IA peut suivre et
	 * elle retourne le plus court chemin (shortestPath). Elle prend 3
	 * arguments, notre IA, une liste des items du jeu et AiTile (représente une
	 * case du jeu, avec tous les spirites qu'elle contient).
	 * 
	 * @param ownHero
	 *            l'hero sollicite par notre AI
	 * @param startPoint
	 *            la position de notre hero
	 * @param endPoints
	 *            les cases cibles ou le hero peut aller
	 * @return le chemin le plus court a parcourir
	 * @throws StopRequestException
	 */
	public AiPath cheminLePlusCourt(AiHero ownHero, AiTile startPoint,
			List<AiTile> endPoints) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// le chemin le plus court possible
		AiPath shortestPath = null;
		// L'objet pour implementer l'algo A*
		Astar astar;
		// Calcul du cout par la classe de l'API
		CostCalculator cost = new BasicCostCalculator();
		// Calcul de l'heuristic par la classe de l'API
		HeuristicCalculator heuristic = new BasicHeuristicCalculator();
		astar = new Astar(ai, ai.ourHero, cost, heuristic);
		if (ai.print)
			System.out.println("shortestpath");
		try {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			shortestPath = astar.processShortestPath(startPoint, endPoints);
		} catch (LimitReachedException e) { // 
		}
		return shortestPath;
	}

	/**
	 * Cette méthode forme une liste des murs qui peuvent se détruire c'est à
	 * dire elle Contrôle si la case est destructible ou indestructibles et il y
	 * a posséde du danger ou pas . Elle prend 2 arguments la zone du jeu et une
	 * action.
	 * 
	 * @param gameZone
	 *            la zone du jeu
	 * @param resultat
	 *            resultat
	 * @param endPoints2 
	 * @return endPoints
	 * @throws StopRequestException
	 */
	public List<AiTile> detruire(AiZone gameZone, AiAction resultat,
			List<AiTile> endPoints2) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		Securite securite = new Securite(ai);

		// Tous les blasts de toutes les bombes
		List<AiTile> blasts = new ArrayList<AiTile>();
		Collection<AiBomb> bombes = gameZone.getBombs();
		Iterator<AiBomb> iteratorBombes = bombes.iterator();
		while (iteratorBombes.hasNext()) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBomb bombe = iteratorBombes.next();
			blasts.addAll(bombe.getBlast());
		}

		List<AiBlock> blockInutile = new ArrayList<AiBlock>();

		// Les murs destructibles qui sont situe au suffle d'une bombe.
		for (int i = 0; i < blasts.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile blast = blasts.get(i);
			if (!blast.getBlocks().isEmpty()) {
				AiBlock block = blast.getBlocks().get(0);
				if (block.isDestructible())
					blockInutile.add(block);
			}
		}

		// La liste des blocks destructibles
		List<AiBlock> blocks = gameZone.getDestructibleBlocks();

		// On supprime les blocks passifs (car ils sont au suffle d'une ou
		// plusieurs bombe(s).)
		// On ne posera pas une bombe la.
		for (int i = 0; i < blockInutile.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			if (blocks.contains(blockInutile.get(i)))
				blocks.remove(blockInutile.get(i));
		}

		// De plus on supprime les murs qui sont en train de bruler
		for (int i = 0; i < blocks.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiBlock block = blocks.get(i);
			if (block.getState().getName() == AiStateName.BURNING)
				blocks.remove(block);
		}

		// La liste des voisins des murs destructibles qui ont ete trouve
		// ci-dessus.
		List<AiTile> voisins = new ArrayList<AiTile>();
		for (int i = 0; i < blocks.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile block = blocks.get(i).getTile();
			voisins.addAll(block.getNeighbors());

		}

		// La liste des voisins qui ne sont pas pleins
		List<AiTile> voisines = new ArrayList<AiTile>();
		for (int i = 0; i < voisins.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			AiTile voisine = voisins.get(i);
			if (!securite.plein(gameZone, voisine)
					&& voisine.getBlocks().isEmpty())
				voisines.add(voisine);
		}

		// Creation de nouvelle zone pour controler les chemins (minimisation de
		// calcul)
		int gauche = 0;
		int droit = 0;
		int haut = 0;
		int bas = 0;
		nouvelleZone(gauche, droit, haut, bas, gameZone);

		// Les voisines qui sont dans notre zone que nous avons calcule
		List<AiTile> voisinesZone = new ArrayList<AiTile>();
		for (int i = 0; i < voisines.size(); i++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			int xVoisin = voisines.get(i).getLine();
			int yVoisin = voisines.get(i).getCol();
			if (yVoisin <= droit && yVoisin >= gauche && xVoisin <= bas
					&& xVoisin >= haut) {
				voisinesZone.add(voisines.get(i));
			}
		}

		// si on a des zones dans notre nouvelle zone
		if (!voisinesZone.isEmpty()) {
			voisines = voisinesZone;
			if (ai.print) {
				System.out.println("<<<<<<<<<<<NOUVELLE ZONE>>>>>>>>>>>>");
				System.out.println("DETRUIRE: Voisins:" + voisines);
			}
		} else {
			if (ai.print) {
				System.out
						.println("NouvelleZone ne contient pas de case pour detruire");
			}
		}

		if (!endPoints2.isEmpty() ) {
			// la liste des voisins des mur destructibles qui
			// sont a la meme direction d'une case d'attaque.

			List<AiTile> voisinsAvantageTous = new ArrayList<AiTile>();

			for (int i = 0; i < voisines.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile voisin = voisines.get(i);
				int dx = (voisin.getLine()) - (ai.ourHero.getLine());
				int dy = (voisin.getCol()) - (ai.ourHero.getCol());

				// la direction de la case de detruit
				Direction dir = Direction.NONE;
				dir = direction(dir, dx, dy);

				if (dir == Direction.NONE)
					voisinsAvantageTous.add(voisin);
				for (int j = 0; j < endPoints2.size(); j++) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					AiTile point = endPoints2.get(j);
					int dx2 = point.getLine() - ai.ourHero.getLine();
					int dy2 = point.getCol() - ai.ourHero.getCol();

					List<Direction> directions2 = new ArrayList<Direction>();
					// UP,DOWN,RIGHT,LEFT,UPRIGHT DOWNLEFT etc.
					directions2 = directionToute(dx2, dy2);

					if (directions2.contains(dir)) {
						if (!voisinsAvantageTous.contains(voisin)) {
							voisinsAvantageTous.add(voisin);
						}
					}
				}
			}
			if (!voisinsAvantageTous.isEmpty())
				voisines = voisinsAvantageTous;
			if (ai.print) {
				System.out.println("DETRUIRE: Voisins (avantage):" + voisines);
			}

			// les distances des voisins au points
			int range = voisines.size() * endPoints2.size();
			int[] distances = new int[range];
			for (int i = 0; i < voisines.size(); i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile voisin = voisines.get(i);
				for (int j = 0; j < endPoints2.size(); j++) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					AiTile point = endPoints2.get(j);
					int distance = gameZone.getTileDistance(voisin, point);
					distances[i + endPoints2.size() - 1] = distance;
				}

			}
			// l'ordre par les distances
			if (voisines.size() > 1)
				ordre(voisines, distances);

			// on trouve la plus proche case "detruit" à la case "attaque".
			// Cette case est accessible
			int i = 0;
			AiPath path = null;
			List<AiTile> tile = new ArrayList<AiTile>();
			List<AiTile> tile2 = new ArrayList<AiTile>();
			while (i < voisines.size()) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				tile = new ArrayList<AiTile>();
				tile.add(voisines.get(i));
				path = cheminLePlusCourt(ai.ourHero, ai.ourHero.getTile(), tile);
				if (!path.isEmpty()) {
					tile2.add(voisines.get(i));
					break;
				} else
					i++;
			}
			voisines = tile2;
		}
		return voisines;
	}

	/**
	 * Cette méthode permet à décider la nouvelle action. Elle prend un seul
	 * argument qui est un chemin (une séquence des cases).
	 * 
	 * @param nextMove
	 * 
	 * @return la nouvelle action de notre hero
	 * 
	 * @throws StopRequestException
	 */
	public AiAction newAction(AiPath nextMove) throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE

		// les cases suivant pour le deplacement.
		List<AiTile> tiles = nextMove.getTiles();
		// deplacement sur l'abcisse
		int dx;
		// deplacement sur l'ordonne
		double dy;

		dx = (tiles.get(0).getLine()) - (this.ai.ourHero.getLine());
		// calcul de deplacement sur l'ordonne par rapport a la position de
		// l'hero et la premiere
		// case du chemin le plus court.
		dy = (tiles.get(0).getCol()) - (this.ai.ourHero.getCol());

		// Determine la direction ou le hero va se deplacer.
		if (dx < 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.UP);
		} else if (dx < 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
		} else if (dx == 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.LEFT);
		} else if (dx > 0 && dy == 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWN);
		} else if (dx > 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
		} else if (dx == 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.RIGHT);
		} else if (dx > 0 && dy < 0) {
			return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
		} else if (dx < 0 && dy > 0) {
			return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
		} else {
			return new AiAction(AiActionName.NONE);
		}
	}

	/**
	 * Cette méthode nous donne une nouvelle zone pour pour minimiser le nombre
	 * des calculs.
	 * 
	 * @param gauche
	 * @param droit
	 * @param haut
	 * @param bas
	 * @param gameZone 
	 * 
	 * @throws StopRequestException
	 */
	public void nouvelleZone(int gauche, int droit, int haut, int bas,
			AiZone gameZone) throws StopRequestException {

		ai.checkInterruption(); // APPEL OBLIGATOIRE
		// on a cree une nouvelle zone qui est plus petite, pour minimiser le
		// nombre des calculs
		// la largeur et la lonueur de la zone
		int height = gameZone.getHeight();
		int width = gameZone.getWidth();

		// la case de notre ia
		int x = ai.ourHero.getLine();
		int y = ai.ourHero.getCol();
		// les frontieres de notre zone qui nous interesse
		int i,j;
		if(height>12)
			i=5;
		else
			i=4;
		if(width>12)
			j=5;
		else
			j=4;
		
		int frontireHeight = height / i;
		int frontiereWidth = width / j;

		gauche = y - frontiereWidth;
		droit = y + frontiereWidth;
		haut = x - frontireHeight;
		bas = x + frontireHeight;

		if (gauche < 0) {
			droit = droit - gauche + 1;
			gauche = 0;
		}
		if (droit < 0) {
			gauche = gauche - droit + 1;
			droit = y;
		}
		if (haut < 0) {
			bas = bas - haut + 1;
			haut = 0;
		}
		if (bas < 0) {
			haut = haut - bas + 1;
			bas = x;
		}
	}

	/**
	 * Cette méthode nous donne une liste de direction pour notre IA. Elle prend
	 * deux arguments.
	 * 
	 * @param dx2
	 * @param dy2
	 * @return directions2
	 * 
	 * @throws StopRequestException
	 */
	public List<Direction> directionToute(int dx2, int dy2)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		List<Direction> directions2 = new ArrayList<Direction>();
		if (dx2 < 0 && dy2 == 0) {
			dir2 = Direction.UP;
			directions2.add(Direction.UP);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.UPRIGHT);
		} else if (dx2 < 0 && dy2 < 0) {
			dir2 = Direction.UPLEFT;
			directions2.add(Direction.UP);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.LEFT);

		} else if (dx2 == 0 && dy2 < 0) {
			dir2 = Direction.LEFT;
			directions2.add(Direction.LEFT);
			directions2.add(Direction.UPLEFT);
			directions2.add(Direction.DOWNLEFT);
		} else if (dx2 > 0 && dy2 == 0) {
			dir2 = Direction.DOWN;
			directions2.add(Direction.DOWN);
			directions2.add(Direction.DOWNLEFT);
			directions2.add(Direction.DOWNRIGHT);
		} else if (dx2 > 0 && dy2 > 0) {
			dir2 = Direction.DOWNRIGHT;
			directions2.add(Direction.DOWN);
			directions2.add(Direction.DOWNRIGHT);
			directions2.add(Direction.RIGHT);
		} else if (dx2 == 0 && dy2 > 0) {
			dir2 = Direction.RIGHT;
			directions2.add(Direction.RIGHT);
			directions2.add(Direction.UPRIGHT);
			directions2.add(Direction.DOWNRIGHT);
		} else if (dx2 > 0 && dy2 < 0) {
			dir2 = Direction.DOWNLEFT;
			directions2.add(Direction.DOWN);
			directions2.add(Direction.LEFT);
			directions2.add(Direction.DOWNLEFT);
		} else if (dx2 < 0 && dy2 > 0) {
			dir2 = Direction.UPRIGHT;
			directions2.add(Direction.UP);
			directions2.add(Direction.UPRIGHT);
			directions2.add(Direction.RIGHT);
		}

		return directions2;
	}

	/**
	 * Cette méthode nous donne la direction de notre IA. Elle prend 3
	 * arguments.
	 * 
	 * @param dx
	 * @param dy
	 * @param dir
	 * @return dir
	 * 
	 * @throws StopRequestException
	 */
	public Direction direction(Direction dir, int dx, int dy)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		if (dx < 0 && dy == 0) {
			dir = Direction.UP;
		} else if (dx < 0 && dy < 0) {
			dir = Direction.UPLEFT;
		} else if (dx == 0 && dy < 0) {
			dir = Direction.LEFT;
		} else if (dx > 0 && dy == 0) {
			dir = Direction.DOWN;
		} else if (dx > 0 && dy > 0) {
			dir = Direction.DOWNRIGHT;
		} else if (dx == 0 && dy > 0) {
			dir = Direction.RIGHT;
		} else if (dx > 0 && dy < 0) {
			dir = Direction.DOWNLEFT;
		} else if (dx < 0 && dy > 0) {
			dir = Direction.UPRIGHT;
		}
		return dir;
	}

	/**
	 * Cette méthode met en ordre la liste des direction de notre IA. Elle prend
	 * 2 arguments.
	 * 
	 * @param voisines
	 * 
	 * @param distances2
	 * 
	 * @throws StopRequestException
	 */
	public void ordre(List<AiTile> voisines, int[] distances2)
			throws StopRequestException {
		ai.checkInterruption(); // APPEL OBLIGATOIRE
		AiTile temp;
		for (int j = 0; j < voisines.size(); j++) {
			ai.checkInterruption(); // APPEL OBLIGATOIRE
			for (int i = 0; i < voisines.size() - 1; i++) {
				ai.checkInterruption(); // APPEL OBLIGATOIRE
				AiTile element = voisines.get(i);
				AiTile element2 = voisines.get(i + 1);
				int val = distances2[i];
				int val2 = distances2[i + 1];
				if (val > val2) {
					ai.checkInterruption(); // APPEL OBLIGATOIRE
					temp = element;
					voisines.set(i, element2);
					voisines.set(i + 1, temp);
					distances2[i] = val2;
					distances2[i + 1] = val;
				}
			}
		}

	}
}
