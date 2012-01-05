package org.totalboumboum.ai.v201112.ais.coskunozdemir.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent. Cf. la documentation de
 * {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Doruk Coşkun
 * @author Utku Özdemir
 */
public class MoveHandler extends AiMoveHandler<CoskunOzdemir> {
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai
	 *            l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected MoveHandler(CoskunOzdemir ai) throws StopRequestException {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

		// TODO à compléter
	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException {
		ai.checkInterruption();
		Direction result = Direction.NONE;
		// -------------------------------- if I am in
		// danger------------------------------------------------------

		if (this.ai.isInDanger()) {
			try {
				AiPath thePath = this.ai.shortestTo(this.ai.closestSafeTile());
				result = this.ai.getZone().getDirection(
						this.ai.getMyCurrentTile(),
						thePath.getLocation(1).getTile());
			} catch (LimitReachedException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		} else {
			this.ai.setUtilityMap(this.ai.getUtilityHandler()
					.getUtilitiesByTile());
			AiTile biggestTile = this.ai.getTileWithBiggestUtility();
			//System.out.println("My Tile : " + this.ai.getMyCurrentTile());
			//System.out.println("Biggest Tile : " + biggestTile);
			if (biggestTile.equals(this.ai.getMyCurrentTile())) {
				result = Direction.NONE;
			} else {
				AiPath thePath;
				try {
					thePath = this.ai.shortestTo(biggestTile);
					result = this.ai.getZone().getDirection(
							this.ai.getMyCurrentTile(),
							thePath.getLocation(1).getTile());
				} catch (LimitReachedException e) {
					//e.printStackTrace();
				}

			}
		}

		// -------------------------------- //if I am in
		// danger------------------------------------------------------

		if (!this.ai.getCurrentDangerousTiles().contains(
				this.ai.getMyCurrentTile())
				&& this.ai.getCurrentDangerousTiles().contains(
						this.ai.getMyCurrentTile().getNeighbor(result)))
			result = Direction.NONE;

		return result;
		// find the closest destructible wall
		// int myColumn = this.ai.getZone().getOwnHero().getCol();
		// int myRow = this.ai.getZone().getOwnHero().getRow();
		// int distance = 100;
		// AiBlock aimedBlock = null;
		// for ( AiBlock currentBlock :
		// this.ai.getZone().getDestructibleBlocks() )
		// {
		// ai.checkInterruption();
		// int newDistance = this.ai.getZone().getTileDistance( myRow, myColumn,
		// currentBlock.getRow(), currentBlock.getCol() );
		// if ( newDistance < distance )
		// {
		// distance = newDistance;
		// aimedBlock = currentBlock;
		// }
		// }
		// System.out.println( "My Block : " + myRow + "," + myColumn +
		// " Aimed Block : " + aimedBlock.getRow() + "," + aimedBlock.getCol()
		// );
		// if ( this.ai.getZone().getTileDistance( myRow, myColumn,
		// aimedBlock.getRow(), aimedBlock.getCol() ) <= 1 )
		// {
		// // I have reached the closest destructible wall
		// result = Direction.NONE;
		// }
		// else
		// {
		// // I need to go to the closest destructible wall
		// result = this.ai.getZone().getDirection( new AiLocation(
		// this.ai.getZone().getOwnHero().getTile() ) , new AiLocation(
		// aimedBlock.getTile() ) );
		// }
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException {
		ai.checkInterruption();

		// ici on se contente de faire le traitement par défaut
		//super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
