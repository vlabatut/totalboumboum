/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe contenant les calculs générales utilisés dans plusieurs classes de gestion de l'agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class CalculGeneral {
	/** */
	private Agent ai;

	/** */
	private AiHero ownHero;
	/** */
	private AiZone zone;

	/**
	 * @param ai
	 *            Agent concerne
	 */
	public CalculGeneral(Agent ai) {
		ai.checkInterruption();

		this.ai = ai;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();

	}

	/**
	 * Calcule la période de pause total d'une liste de pause donnée
	 * 
	 * @param  pauses
	 *            Liste des pauses total à calculer
	 * @return result 
	 *			  Le temps de pause total
	 */
	public long getTotalPauseTime(List<Long> pauses) {
		ai.checkInterruption();

		long result = 0;

		if ( pauses != null )
			for (int i = 0; i < pauses.size(); i++) {
				ai.checkInterruption();
				result += pauses.get(i);
			}
		return result;

	}

	/**
	 * Calcule l'existence de murs destructible dans les cases voisines s'il n'existe pas
	 * d'adversaires dans notre liste de sélection de case
	 * 
	 * @return true 
	 * 			s'il existe un mur destructible dans au moins une des cases voisines
	 * 		false sinon 
	 */
	public boolean accesibleDestWallExistence() {
		ai.checkInterruption();

		if ( ai.getZone().getDestructibleBlocks().size() != 0 ) {
			if ( !ai.ennemyAccesibility ) {
				for (AiBlock destWall : ai.getZone().getDestructibleBlocks()) {
					ai.checkInterruption();
					for (Direction direction : Direction.getPrimaryValues()) {
						ai.checkInterruption();
						if ( ai.selectTiles.contains(destWall.getTile().getNeighbor(direction)) ) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	/**
	 * Calcule la distance entre deux cases en prenant comme unité de mesure les coordonnées des cases.
	 * 
	 * @param tile1
	 *            première case donnée
	 * @param tile2
	 *            deuxième case donnée
	 * @return la distance entre les deux cases
	 */
	public int nonCyclicTileDistance(AiTile tile1, AiTile tile2) {
		ai.checkInterruption();

		return Math.abs(tile1.getCol() - tile2.getCol()) + Math.abs(tile1.getRow() - tile2.getRow());
	}

	/**
	 * Calcule l'existence de murs destructibles se trouvant dans la portée de notre bombe
	 * 
	 * @return true s'il existe au moins un mur destructible dans la portée de notre bombe
	 * 		false sinon
	 */
	public boolean wallInOurBlast() {
		ai.checkInterruption();

		AiBomb bomb = ai.getZone().getOwnHero().getBombPrototype();

		List<AiTile> ownBlast = bomb.getBlast();

		for (AiBlock destWall : ai.getZone().getDestructibleBlocks()) {
			ai.checkInterruption();
			if ( ownBlast.contains(destWall.getTile()) )
				return true;
		}

		return false;
	}

	/**
	 * Calcule la possibilité de notre agent d'atteindre une case cible avant l'adversaire
	 * 
	 * @param tile
	 *          case cible
	 * @return true si l'agent sera capable d'atteindre la case avant l'adversaire
	 * 		false sinon
	 */
	public boolean concurrencePossibility(AiTile tile) {
		ai.checkInterruption();

		boolean result = false;

		int mydistance = this.nonCyclicTileDistance(ownHero.getTile(), tile);

		for (AiHero currentEnemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();
			if ( ((double) this.nonCyclicTileDistance(currentEnemy.getTile(), tile) / currentEnemy.getWalkingSpeed()) > ((double) mydistance / ai
					.getZone().getOwnHero().getWalkingSpeed()) )
				result = true;
		}

		return result;
	}

	/**
	 * Calcule le temps qui reste à l'agent pour réaliser une explosion en enchainement en supposant qu'il pose 
	 * une bombe sur la case concernée.
	 * 
	 * @param tile
	 *            case concernée
	 * @return result Le temps qui restera à l'agent si la réaction en chaine est possible 
	 * 			0 si une explosion en enchainement n'est pas réalisable
	 */
	public long chainReactionTime(AiTile tile) {
		ai.checkInterruption();

		long result = 10000000;

		Map<AiBomb, Long> bomb_time = ai.getZone().getDelaysByBombs();

		if ( ai.getDangerousTiles.contains(tile) ) {
			if ( !bomb_time.isEmpty() ) {
				for (AiBomb bomb : ai.getZone().getBombs()) {
					ai.checkInterruption();
					if ( bomb.getBlast().contains(tile) ) {
						long explosion_time = bomb_time.get(bomb) - bomb.getElapsedTime();
						if ( explosion_time < result )
							result = explosion_time;
					}
				}

			}
		} else
			result = 0;

		return result;
	}

	/**
	 * Calcule la durée de temps dans laquelle la case passée en paramètre va être dans la portée
	 * d'explosion de la bombe 
	 * 
	 * @param tile
	 *            case concernée
	 * @return result Le temps d'explosion de la bombe
	 */
	public long bombExplosionTime(AiTile tile) {
		ai.checkInterruption();

		long result = 0;

		if ( ai.getDangerousTiles.contains(tile) ) {
			result = chainReactionTime(tile);
		} else
			result = ai.getZone().getOwnHero().getBombDuration();

		return result;
	}

	/**
	 * Calcule le nombre de murs autour d'une case.
	 * 
	 * @param tile
	 *            case concernée
	 * @return 0 si il y a 1 ou 0 mur sur les cases voisines
	 * 		   1 si il y a 2 murs 
	 * 		   2 sinon
	 */
	public int zoneWall(AiTile tile) {
		ai.checkInterruption();

		int result = 0;

		for (AiTile neighbor : tile.getNeighbors()) {
			ai.checkInterruption();

			if ( !neighbor.getBlocks().isEmpty() && result < 3 ) {
				result++;
			}
		}

		if ( result == 1 || result == 0 )
			return 0;

		if ( result == 2 )
			return 1;
		return 2;
	}

	/**
	 * Calcule la posibilité de réaliser une réaction en chaine.
	 * 
	 * @return true si la réaction en chaine est possible 
	 * 		   false sinon
	 */
	public boolean chaineReactionPossibility() {
		ai.checkInterruption();

		int distance = 0;

		double minConstantToMakeStrategy = 1.34;

		if ( ownHero.getBombNumberCurrent() >= 1 && ownHero.getBombNumberMax() >= 3
				&& ((double) ownHero.getWalkingSpeed() > (double) (minConstantToMakeStrategy) * ai.getClosestEnnemy.getWalkingSpeed()) ) {

			for (AiBomb bomb : ai.getZone().getBombsByColor(ownHero.getColor())) {
				ai.checkInterruption();

				distance = ai.getZone().getTileDistance(ownHero.getTile(), bomb.getTile());

				if ( bomb.getBlast().contains(ownHero.getTile()) && distance == 2 && ownHero.getBombNumberCurrent() < 3 ) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Calcule la possibilitée d'éliminer l'adversaire en vérifiant si celui-ci se trouve dans la portée
	 * de la bombe qu'il a posé et si cette case est dans l'intersection de la portée de la bombe de l'agent.
	 * 
	 * @return true si cette stratégie est applicable
	 * 		   false sinon
	 */
	public boolean ennemyBombMe() {
		ai.checkInterruption();

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			for (AiBomb ennemyBomb : ai.getZone().getBombsByColor(ennemy.getColor())) {
				ai.checkInterruption();

				for (AiTile myPrtBlast : ai.getZone().getOwnHero().getBombPrototype().getBlast()) {
					ai.checkInterruption();

					if ( ennemyBomb.getBlast().contains(ennemy.getTile()) && ennemyBomb.getBlast().contains(myPrtBlast) )
						return true;
				}

			}
		}

		return false;
	}

	/**
	 * Calcule les directions perpendiculaires à celle passée en paramètre.
	 * 
	 * @param direction
	 *            direction concernée
	 * @return result la liste des directions perpendiculaires
	 */
	public ArrayList<Direction> getPerpendicularDirections(Direction direction) {
		this.ai.checkInterruption();

		ArrayList<Direction> result = new ArrayList<Direction>();

		if ( direction == Direction.UP || direction == Direction.DOWN ) {
			result.add(Direction.RIGHT);
			result.add(Direction.LEFT);
		} else if ( direction == Direction.LEFT || direction == Direction.RIGHT ) {
			result.add(Direction.UP);
			result.add(Direction.DOWN);
		}

		return result;
	}

	/**
	 * Vérifie si l'agent est dans la portée de sa première bombe posée
	 * 
	 * @return true si l'agent est dans la portée de cette bombe
	 * 		   false sinon
	 */
	public boolean amIINmyFirstBombBlast() {
		ai.checkInterruption();
		if ( !ai.getZone().getBombsByColor(ownHero.getColor()).isEmpty() ) {
			if ( ai.getZone().getBombsByColor(ownHero.getColor()).get(0).getBlast().contains(ownHero.getTile()) )
				return true;
		}
		return false;

	}

	/**
	 * Vérifie si il y a trop de bombe par rapport aux nombres d'adversaire sur la zone de jeu
	 * 
	 * @return true si il y a beaucoup de bombe
	 * 		   false sinon
	 */
	public boolean tooManyBombsControl() {
		ai.checkInterruption();

		if ( ai.getZone().getBombs().size() > ai.getZone().getRemainingHeroes().size() * 2 + 1 )
			return true;

		return false;
	}
}
