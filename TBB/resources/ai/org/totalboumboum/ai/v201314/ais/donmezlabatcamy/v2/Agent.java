package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de notre agent.
 * 
 * @author Mustafa Dönmez
 * @author Charlotte Labat Camy
 */
public class Agent extends ArtificialIntelligence {
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();
	}

	/** */
	public AiTile endTile = null;

	/** */
	public int endTilePref = 100;

	/**
	 * Méthode permettant de faire une initialisation supplémentaire.
	 */
	@Override
	protected void initOthers() {
		checkInterruption();

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant d'initilaiser les percepts de l'agent c'est à dire les
	 * différents objets stockés en interne dans ces classes.
	 */
	@Override
	protected void initPercepts() {
		checkInterruption();

	}

	/**
	 * Méthode permettant de mettre à jour les percepts de l'agent c'est à dire
	 * les différents objets stockés en interne dans ces classes.
	 */
	@Override
	protected void updatePercepts() {
		checkInterruption();

		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = true;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
	}

	// ///////////////////////////////////////////////////////////////
	// HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;

	/**
	 * Cette méthode a pour but d'initialiser les gestionnaires.
	 */
	@Override
	protected void initHandlers() {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);

	}

	@Override
	protected AiModeHandler<Agent> getModeHandler() {
		checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler() {
		checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler() {
		checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler() {
		checkInterruption();
		return moveHandler;
	}

	// ///////////////////////////////////////////////////////////////
	// OUTPUT /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Méthode permettant de mettre à jour les sorties graphiques de l'agent.
	 */
	@Override
	protected void updateOutput() {
		checkInterruption();

		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

	}

	/**
	 * Cette méthode calcule les cases qui sont en danger
	 * 
	 * @return dangerTiles Listes des cases en danger
	 */
	public ArrayList<AiTile> getDangerousTiles() {
		this.checkInterruption();
		ArrayList<AiTile> dangerTiles = new ArrayList<AiTile>();

		for (AiBomb bomb : this.getZone().getBombs()) {
			this.checkInterruption();
			dangerTiles.add(bomb.getTile());

			for (AiTile tileBombBlast : bomb.getBlast()) {
				this.checkInterruption();
				dangerTiles.add(tileBombBlast);
			}
		}
		for (AiFire fires : this.getZone().getFires()) {
			this.checkInterruption();
			dangerTiles.add(fires.getTile());
		}

		return dangerTiles;

	}

	/**
	 * Calcule la période de pause total par rapport à une liste de pause donnée
	 * 
	 * @param pauses
	 *            Liste dont la pause total va être calculé
	 * @return result Le temps de pause total
	 */
	public long getTotalPauseTime(List<Long> pauses) {
		this.checkInterruption();
		long result = 0;
		if (pauses != null)
			for (int i = 0; i < pauses.size(); i++) {
				this.checkInterruption();
				result += pauses.get(i);
			}
		return result;

	}

	/**
	 * Calcule la liste des cases accessibles
	 * 
	 * @return accessibleTile La liste des cases accessibles
	 */
	public ArrayList<AiTile> getAccesibleTiles() {
		checkInterruption();
		ArrayList<AiTile> accesibleTile = new ArrayList<AiTile>();
		AiPreferenceHandler<Agent> preferenceHandler = this
				.getPreferenceHandler();
		Map<Integer, List<AiTile>> preferences = preferenceHandler
				.getPreferencesByValue();
		TreeSet<Integer> values = new TreeSet<Integer>(preferences.keySet());
		values.remove(Collections.max(preferences.keySet()));
		while (!values.isEmpty()) {
			checkInterruption();
			accesibleTile.addAll(preferences.get(values.first()));
			values.remove(values.first());
		}
		return accesibleTile;
	}

	/**
	 * Calcule s'il y a au moins un ennemi accessible
	 * 
	 * @return true si il y a au moins un ennemi dans la liste de nos cases
	 *         séléctionnées false sinon
	 */
	public boolean hasEnoughEnnemyAccesible() {
		checkInterruption();
		for (AiHero ennemy : this.getZone().getRemainingOpponents()) {
			checkInterruption();
			if (this.preferenceHandler.selectTiles().contains(ennemy.getTile()))
				return true;
		}
		return false;
	}

	/**
	 * Calcule les cases hors de danger
	 * 
	 * @return safeTiles La liste des cases hors de danger
	 */
	public ArrayList<AiTile> getSafeTiles() {
		this.checkInterruption();
		ArrayList<AiTile> safeTiles = this.getAccesibleTiles();
		safeTiles.removeAll(this.getDangerousTiles());
		return safeTiles;
	}

	/**
	 * Calcule la case hors de danger la plus proche de notre agent
	 * 
	 * @return result la case hors de danger la plus proche
	 */
	public AiTile getNearestSafeTiles() {
		this.checkInterruption();
		ArrayList<AiTile> safeTiles = this.getSafeTiles();
		int tmpDistance = 100;
		AiTile result = null;
		for (AiTile aiTile : safeTiles) {
			checkInterruption();
			int myDistance = this.getZone().getTileDistance(
					this.getZone().getOwnHero().getTile(), aiTile);
			if (tmpDistance > myDistance) {
				tmpDistance = myDistance;
				result = aiTile;
			}
		}
		return result;
	}

	/**
	 * Calcule si un item est un bonus ou pas
	 * 
	 * @return true si l'item est un bonus false sinon
	 */
	public boolean usefulItemsExistence() {
		checkInterruption();
		for (AiItem aiItem : this.getZone().getItems()) {
			checkInterruption();
			if (aiItem.getType() == AiItemType.EXTRA_BOMB
					|| aiItem.getType() == AiItemType.EXTRA_FLAME
					|| aiItem.getType() == AiItemType.EXTRA_SPEED
					|| aiItem.getType() == AiItemType.RANDOM_EXTRA
					|| aiItem.getType() == AiItemType.GOLDEN_BOMB
					|| aiItem.getType() == AiItemType.GOLDEN_FLAME
					|| aiItem.getType() == AiItemType.GOLDEN_SPEED) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Calcule la liste des bonus se trouvant sur la zone de jeu
	 * 
	 * @return usefulItemsList Liste des bonus
	 */
	public ArrayList<AiItem> usefulItemsList() {
		checkInterruption();
		ArrayList<AiItem> usefulItemsList = new ArrayList<AiItem>();
		for (AiItem aiItem : this.getZone().getItems()) {
			checkInterruption();
			if (aiItem.getType() == AiItemType.EXTRA_BOMB
					|| aiItem.getType() == AiItemType.EXTRA_FLAME
					|| aiItem.getType() == AiItemType.EXTRA_SPEED
					|| aiItem.getType() == AiItemType.RANDOM_EXTRA
					|| aiItem.getType() == AiItemType.GOLDEN_BOMB
					|| aiItem.getType() == AiItemType.GOLDEN_FLAME
					|| aiItem.getType() == AiItemType.GOLDEN_SPEED) {
				usefulItemsList.add(aiItem);
			}
		}

		return usefulItemsList;
	}

	/**
	 * Calcul l'accessibilité d'un bonus
	 * 
	 * @return true si un item est accessible false sinon
	 */
	public boolean usefulItemsAccesibility() {
		checkInterruption();
		if (this.usefulItemsExistence()) {
			checkInterruption();
			for (AiItem items : this.usefulItemsList()) {
				this.checkInterruption();
				if (this.preferenceHandler.selectTiles().contains(
						items.getTile())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Calculer de l'existence de voisinage des destructibles murs lors qu'il
	 * n'y a pas de l'advarsaire dans notre selection de case
	 * 
	 * @return true si voisinage d'une destructible murs dans la selection de
	 *         case sinon false
	 */
	public boolean accesibleDestWallExistence() {
		this.checkInterruption();
		if (!this.hasEnoughEnnemyAccesible()) {
			for (AiBlock destWall : this.getZone().getDestructibleBlocks()) {
				this.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					this.checkInterruption();
					if (this.preferenceHandler.selectTiles().contains(
							destWall.getTile().getNeighbor(direction))) {
						return true;
					}

				}

			}
		}
		return false;
	}

}
