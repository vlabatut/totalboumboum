package org.totalboumboum.ai.v201314.ais.asilizeryuce.v2;

import java.util.ArrayList;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

/**
 * Classe gérant l'action de déposer une bombe pour l'agent. Cf. la
 * documentation de {@link AiBombHandler} pour plus de détails.
 * 
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class BombHandler extends AiBombHandler<Agent> {

	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai l'agent que cette classe doit gérer.
	 */
	protected BombHandler(Agent ai) {
		super(ai);
		ai.checkInterruption();

		// on règle la sortie texte pour ce gestionnaire
		verbose = false;

	}

	// ///////////////////////////////////////////////////////////////
	// PROCESSING /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() {
		ai.checkInterruption();

		boolean result = false;
		boolean dontPutBombe = false;
		AiTile destination = ai.moveHandler.getCurrentDestination();
		AiTile currentTile = ai.getZone().getOwnHero().getTile();

		int currentTilePreference = ai.preferenceHandler.getPreferencesByTile()
				.get(currentTile);
		
		if (ai.nextTileHasBlock == true) {
			ai.nextTileHasBlock = false;
			return true;
		}

		if (ai.nextTile != null) {
			// If I put a bomb here, can it cause a dangerous situation?
			// for example, will I find myself in a corridor? 
			if (tileCanBeCorridor(ai.nextTile))
				dontPutBombe = true;
		}

		// if there is already a bomb in this tile return false
		if (currentTile.getBombs().isEmpty()) {
			// preference is 0 and tile is our destination
			if (currentTilePreference == 0 && currentTile.equals(destination)
					&& !dontPutBombe)
				result = true;

			else {
				// if any of the neighbors of tile tile has an ennemy in a corridor
				for (AiTile tile : currentTile.getNeighbors()) {
					ai.checkInterruption();
					if (ennemyInCorridor(tile) == true)
						return true;
				}
			}
		}

		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Met à jour la sortie graphique.
	 */
	protected void updateOutput() {
		ai.checkInterruption();

	}

	/**
	 * c'est une method qui construit une liste des cases qui contiennent des ennemis
	 * @return la liste des cases qui contiennent des ennemis
	 */
	public ArrayList<AiTile> getEnnemyTiles() {
		ai.checkInterruption();
		ArrayList<AiTile> ennemyTiles = new ArrayList<AiTile>();

		for (AiHero hero : ai.getZone().getRemainingOpponents())
		{
			ai.checkInterruption();
			ennemyTiles.add(hero.getTile());
		}

		return ennemyTiles;
	}

	/**
	 * on essaye de savoir si une case peut etre dans une formation d'un couloir
	 * @param aiTile qu'on veut savoir si cette case forme un couloir, c'est la case on examiner
	 * @return si cette case forme un coulior, cette methode retourne true, sinon false
	 */
	public boolean tileCanBeCorridor(AiTile aiTile) {
		ai.checkInterruption();
		int i = 0;

		for (AiTile tile : aiTile.getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBlocks().isEmpty())
				i++;
			if (!tile.getBombs().isEmpty())
				i++;
		}

		if (i > 1)
			return true;
		else
			return false;
	}

	/**
	 * cette methode essaye de trouver s'il ya un couloir, est-ce qu'il y a une ennemi dans lui.
	 * on aussi controle si notre agent dans cet couloir.
	 * @param aiTile est la case qu'on examine
	 * @return retourne true s'il ya une ennemi et s'il notre agent n'est pas dans cet couloir,
	 * retourne false sinon
	 */
	public boolean ennemyInCorridor(AiTile aiTile) {
		ai.checkInterruption();
		int i = 0;

		for (AiTile tile : aiTile.getNeighbors()) {
			ai.checkInterruption();
			if (!tile.getBlocks().isEmpty())
				i++;
			if (!tile.getBombs().isEmpty())
				i++;
		}

		if (i > 2
				&& (!aiTile.getHeroes().isEmpty() && !aiTile.getHeroes()
						.contains(ai.getZone().getOwnHero())))
			return true;
		else
			return false;
	}


}
