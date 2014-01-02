package org.totalboumboum.ai.v201314.ais.oraliosmanoglu.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * il y a les methodes pour calculer les cases(safe, danger, traversable..) dans
 * cette classe
 * 
 * @author Selen Oralı
 * @author Arman Osmanoğlu
 * 
 */
public class TileHandler {
	/** l'agent que cette classe doit gérer. */
	private Agent ai;
	/** la liste pour les cases secure. (ne sont pas blasts,bombes,fires..) */
	public List<AiTile> safeTiles;
	/** notre agent */
	public AiHero ownHero;
	/** integer variable pour porte bombe de notre agent */
	public int blastSize;

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre. calculer les
	 * cases safe,danger,le plus proche adversaire..
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 */
	public TileHandler(Agent ai) {
		ai.checkInterruption();
		this.ai = ai;
		this.ownHero = ai.getZone().getOwnHero();
		this.safeTiles = new ArrayList<AiTile>();
		this.blastSize = ownHero.getBombRangeLimit();
	}

	/**
	 * cette methode nous renvoie la liste qui sont dans la blast, dans la
	 * bombe, dans fires
	 * 
	 * @return ArrayLişt de AiTile
	 */
	public ArrayList<AiTile> dangerTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> dangerTiles = new ArrayList<AiTile>();

		for (AiBomb bombs : ai.getZone().getBombs()) {
			ai.checkInterruption();
			dangerTiles.add(bombs.getTile());
			for (AiTile bombBlast : bombs.getBlast()) {
				ai.checkInterruption();
				dangerTiles.add(bombBlast);
			}
		}
		for (AiFire fires : ai.getZone().getFires()) {
			ai.checkInterruption();
			dangerTiles.add(fires.getTile());

		}
		return dangerTiles;
	}

	/**
	 * cette methode renvoie la liste de AiTile qui sont dans la liste des
	 * selection des cases, mais ne sont pas dans la liste de dangerListe.
	 * 
	 * @return set de AiTile
	 */
	public Set<AiTile> safeTiles() {
		ai.checkInterruption();
		Set<AiTile> accesible = ai.acces;
		accesible.removeAll(ai.dangerTiles);
		accesible.removeAll(ai.getZone().getOwnHero().getBombPrototype()
				.getBlast());
		return accesible;
	}

	/**
	 * cette methode calcule que est-ce qu'il y a de l'adversaire qui est
	 * traversable directement par nous. s'il en a, on retourne vrai.
	 * 
	 * @return boolean value
	 */
	public boolean isEnnemyAccesible() {
		ai.checkInterruption();
		AiZone zone = ai.getZone();
		Set<AiTile> accesible = ai.acces;

		for (AiHero enemyheroes : zone.getRemainingOpponents()) {
			ai.checkInterruption();
			if (accesible.contains(enemyheroes.getTile())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Calcule la plus proche adversaire
	 * 
	 * @return AiHero closest ennemy
	 */
	public AiHero getClosestEnnemy() {
		ai.checkInterruption();
		int tmpDistance = 100;
		AiHero result = ai.getZone().getOwnHero();
		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			int myDistance = ai.getZone().getTileDistance(
					ai.getZone().getOwnHero().getTile(), ennemy.getTile());
			if (tmpDistance > myDistance) {
				tmpDistance = myDistance;
				result = ennemy;
			}
		}
		return result;
	}

	/**
	 * si le mur destructible est dans la portee de notre bombe, on retourne
	 * true.
	 * 
	 * @return boolean value
	 */
	public boolean wallbomb() {
		ai.checkInterruption();
		AiBomb bomb = ai.getZone().getOwnHero().getBombPrototype();
		List<AiTile> ownBlast = bomb.getBlast();
		for (AiBlock destWall : ai.getZone().getDestructibleBlocks()) {
			ai.checkInterruption();
			if (ownBlast.contains(destWall.getTile()))
				return true;
		}

		return false;
	}

	/**
	 * s'il ya 4 mur autour de l'adversaire, on retourne true.
	 * 
	 * @param hero
	 *            adversaire
	 * @return boolean value
	 */
	public boolean fourWall(AiHero hero) {
		ai.checkInterruption();
		int counter = 4;
		for (Direction direction : Direction.getPrimaryValues()) {
			ai.checkInterruption();
			if (!hero.getTile().getNeighbor(direction).getBlocks().isEmpty())
				counter--;
		}
		if (counter == 0)
			return true;

		return false;
	}

}
