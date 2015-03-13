/**
 * 
 */
package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v4;

import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.agent.AiAbstractHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant la position des agents adversaires.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
@SuppressWarnings("deprecation")
public class HeroHandler extends AiAbstractHandler<Agent> {

	/**
	 * @param ai
	 *            agent concerné
	 */
	public HeroHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

	}

	/**
	 * Calcule s'il y a au moins un ennemi accessible
	 * 
	 * @return true si il y a au moins un ennemi dans la liste de nos cases séléctionnées false sinon
	 */
	public boolean hasEnoughEnnemyAccesible() {
		ai.checkInterruption();

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			if ( ai.selectTiles.contains(ennemy.getTile()) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calcule l'ennemi le plus proche.
	 * 
	 * @return result l'ennemi le plus proche
	 */
	public AiHero getClosestEnnemy() {
		ai.checkInterruption();

		int tmpDistance = 10000;

		AiHero result = null;

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			int myDistance = ai.getCG().nonCyclicTileDistance(ai.getZone().getOwnHero().getTile(), ennemy.getTile());

			if ( tmpDistance > myDistance ) {
				tmpDistance = myDistance;
				result = ennemy;
			}
		}
		return result;
	}

	/**
	 * Vérifie si l'ennemi le plus proche est dans la portée de la bombe posée par l'agent.
	 * 
	 * @return true si l'ennemi est dans la portée de la bombe false sinon
	 */
	public boolean ennemyInMyBombBlastWhenIPutBomb() {
		ai.checkInterruption();

		for (AiBomb myboms : ai.getZone().getBombsByColor(ai.getZone().getOwnHero().getColor())) {
			ai.checkInterruption();

			if ( myboms.getBlast().contains(ai.getClosestEnnemy) ) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calcule la case qui est la plus proche à un mur destructible se trouvant sur une 
     * case voisine d'un ennemi inaccessible.
	 * 
	 * @return getWallToReachEnnemy la case la plus proche à un mur destructible
	 */
	public AiTile getWallToReachEnnemy() {
		ai.checkInterruption();

		AiTile getWallToReachEnnemy = null;

		Set<AiTile> accesibleTiles = ai.selectTiles;

		int tmpDistance = 100;

		for (AiTile tile : accesibleTiles) {
			ai.checkInterruption();

			if ( ai.getClosestEnnemy != null ) {
				for (AiTile tileNeighbor : tile.getNeighbors()) {
					ai.checkInterruption();

					for (AiBlock tileBlock : tileNeighbor.getBlocks()) {
						ai.checkInterruption();

						if ( tileBlock.isDestructible() ) {
							for (AiTile neighbor : tileNeighbor.getNeighbors()) {
								ai.checkInterruption();

								if ( !accesibleTiles.contains(neighbor) ) {

									if ( ai.getCG().nonCyclicTileDistance(tile, ai.getClosestEnnemy.getTile()) < tmpDistance ) {
										tmpDistance = ai.getCG().nonCyclicTileDistance(tile, ai.getClosestEnnemy.getTile());
										getWallToReachEnnemy = tile;
									}
									if ( ai.getCG().nonCyclicTileDistance(tile, ai.getClosestEnnemy.getTile()) == tmpDistance ) {

										if ( getWallToReachEnnemy != null ) {

											if ( tile.getRow() > getWallToReachEnnemy.getRow() ) {
												getWallToReachEnnemy = tile;
											} else if ( (tile.getRow() == getWallToReachEnnemy.getRow())
													&& (tile.getCol() > getWallToReachEnnemy.getCol()) ) {
												getWallToReachEnnemy = tile;
											}
										}
									}
								}
							}
						}

					}
				}
			}
		}
		if ( getWallToReachEnnemy != null ) {

			for (AiBomb bomb : ai.getZone().getBombs()) {
				ai.checkInterruption();

				if ( accesibleTiles.contains(bomb.getTile().getNeighbor(Direction.UP))
						|| accesibleTiles.contains(bomb.getTile().getNeighbor(Direction.DOWN))
						|| accesibleTiles.contains(bomb.getTile().getNeighbor(Direction.LEFT))
						|| accesibleTiles.contains(bomb.getTile().getNeighbor(Direction.RIGHT)) ) {
					if ( ai.getCG().nonCyclicTileDistance(bomb.getTile(), ai.getClosestEnnemy.getTile()) < ai.getCG()
							.nonCyclicTileDistance(ai.getClosestEnnemy.getTile(), getWallToReachEnnemy) )
						getWallToReachEnnemy = bomb.getTile();
				}
			}
		}
		return getWallToReachEnnemy;
	}

	/**
	 * Calcule l'existence d'un ennemi dans la portée de la bombe si notre agent décide
     * de poser une bombe sur la case où il se trouve.
	 * 
	 * @return true si il y a un ennemi false sinon
	 * 
	 */
	public boolean ennemyInOurBlast() {
		ai.checkInterruption();

		for (AiHero hero : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			if ( ai.getZone().getOwnHero().getBombPrototype().getBlast().contains(hero.getTile()) )
				return true;
		}
		return false;
	}

	/**
	 * Vérifie si il existe un ennemi à une distance de moins de 5 case.
	 * 
	 * @param tile
	 *            case de l'agent
	 * @return true si l'agent est proche false sinon
	 */
	public boolean aroundEnnemy(AiTile tile) {
		ai.checkInterruption();

		for (AiHero ennemy : ai.getZone().getRemainingOpponents()) {
			ai.checkInterruption();

			if ( ai.getCG().nonCyclicTileDistance(tile, ennemy.getTile()) <= 5 )
				return true;
		}
		return false;
	}

}
