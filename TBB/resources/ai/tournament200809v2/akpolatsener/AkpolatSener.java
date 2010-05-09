package tournament200809v2.akpolatsener;

import fr.free.totalboumboum.ai.adapter200809.AiAction;
import fr.free.totalboumboum.ai.adapter200809.AiActionName;
import fr.free.totalboumboum.ai.adapter200809.AiItem;
import fr.free.totalboumboum.ai.adapter200809.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200809.StopRequestException;
import java.util.ArrayList;
import fr.free.totalboumboum.ai.adapter200809.AiHero;
import fr.free.totalboumboum.ai.adapter200809.AiTile;
import fr.free.totalboumboum.ai.adapter200809.AiZone;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class AkpolatSener extends ArtificialIntelligence {

	/** les cases actuel, precedent et prochain */
	AiTile currentTile = null;
	AiTile previousTile = null;
	AiTile nextTile = null;

	/** zone du jeu */
	AiZone zone = null;

	/** les personnages de soi-meme et l'enemie */
	AiHero ownHero;

	/** direction du cas actuel vers le prochain */
	Direction direction;

	/** action de resultat à renvoyer */
	AiAction result = new AiAction(AiActionName.NONE);

	/** objet pour les operations sur les cases */
	TileControl control;

	/** objets de danger et de cible */
	Danger danger;
	Target target;

	/**
	 * methode obligatoire
	 * 
	 * @return action d'IA
	 */
	public AiAction processAction() throws StopRequestException {
		checkInterruption();

		control = new TileControl(this);
		ArrayList<AiTile> tiles;
		// initialisation des zone, personnage et case actuelle
		zone = getPercepts();
		ownHero = zone.getOwnHero();
		currentTile = ownHero.getTile();

		if (ownHero != null) {
			if (control.checkTile(currentTile, true, true, true, false, false,
					false, false)) {

				if ((direction = control.prisonBreak(currentTile))
						.isComposite())
					result = new AiAction(AiActionName.NONE);
				else
					result = new AiAction(AiActionName.MOVE, direction);
			} else {

				target = new Target(this);

				tiles = control.filterNeighbors(currentTile, false, true,
						true, true, false, false, false);

				if ((tiles.size() == 0))
					result = new AiAction(AiActionName.NONE);
				else {

					tiles = control.sortTiles(tiles, true);

					int i = 0;
					AiTile bestTile = null;

					while (tiles.size() > 0 && i < tiles.size()) {
						if (control.isInBombRange(tiles.get(i)))
							tiles.remove(i);
						else {
							i++;
							bestTile = tiles.get(0);
						}

					}

					if (bestTile == null)
						bestTile = currentTile;

					if (control.checkTile(bestTile, false, false, false, false,
							false, true, false)) {

						tiles = control.filterNeighbors(currentTile, false,
								false, false, true, false, true, false);

						Direction dir = zone.getDirection(currentTile, tiles
								.get(0));

						tiles = control.filterNeighbors(currentTile, false,
								false, false, true, false, false, false);

						if (tiles.size() > 0) {

							if (!control.checkDangerBehindCorner(currentTile,
									dir))
								result = new AiAction(AiActionName.DROP_BOMB);

						} else
							result = new AiAction(AiActionName.NONE);
					} else {
						direction = zone.getDirection(currentTile, bestTile);

						Object closestTarget = target.getClosestTarget();

						if (closestTarget instanceof AiItem) {
							if (control
									.getNeighborsCount(((AiItem) closestTarget)
											.getTile()) > 2) {
								if (((AiItem) closestTarget).getTile() == currentTile)
									result = new AiAction(AiActionName.PUNCH);
								else
									result = new AiAction(AiActionName.MOVE,
											direction);

							} else
								result = new AiAction(AiActionName.NONE);
						} else {
							if (target.getHypotenuseToTarget(currentTile) < 1) {

								tiles = control.filterNeighbors(currentTile,
										true, false, false, false, false,
										false, false);

								if (tiles.size() > 0)
									result = new AiAction(
											AiActionName.DROP_BOMB);
								else
									result = new AiAction(AiActionName.NONE);
							} else
								result = new AiAction(AiActionName.MOVE,
										direction);
						}
					}
				}

			}

		} else
			result = new AiAction(AiActionName.NONE);

		return result;
	}

}
