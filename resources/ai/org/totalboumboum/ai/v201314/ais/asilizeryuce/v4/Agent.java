package org.totalboumboum.ai.v201314.ais.asilizeryuce.v4;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de notre agent. on va completer cette classe pendant la
 * semestre
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
@SuppressWarnings("deprecation")
public class Agent extends ArtificialIntelligence {

	/**
	 * On utilise pour savoir quelle case est plus proche a un ennemi
	 * inaccessible
	 */
	public AiTile wallAccessibleTile;

	/** c'est la dernier direction selectionnee */
	public Direction lastDirection;

	/** la case prochaine dans notre chemin */
	public AiTile nextTile;

	/** le chemin courant qu'on suive */
	public AiPath currentPath;
	
	/**
	 * cette variable nous donne la notion d'item. si cette valeur est 0, on n'a
	 * pas besoin de cet item, si c'est 1, c'est à dire l'item est une bombe et
	 * si c'est 2, c'est à dire l'item est une flamme
	 * */
	public int itemType = 0;

	/**
	 * Instancie la classe principale de l'agent.
	 */

	public Agent() {
		checkInterruption();

		// active/désactive la sortie texte
		verbose = false;
	}

	@Override
	protected void initOthers() {
		checkInterruption();

		this.lastDirection = null;
		this.nextTile = null;
		this.currentPath = null;
		/*
		 * si on veut créer des objets particuliers pour réaliser votre
		 * traitement, et qui ne sont ni des gestionnaires (initialisés dans
		 * initHandlers) ni des percepts (initialisés dans initPercepts).
		 */
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() {
		checkInterruption();

		/*
		 * si on veut créer des objets particuliers pour réaliser votre
		 * traitement. Ils peuvent être stockés dans cette classe ou dans un
		 * gestionnaire quelconque.
		 */
	}

	@Override
	protected void updatePercepts() {
		checkInterruption();
		/*
		 * des objets à mettre à jour à chaque itération, e.g. des objets créés
		 * par la méthode initPercepts().
		 */
		// active/désactive la sortie texte
		this.tileHandler.accessibleTiles.clear();
		this.tileHandler.selectedTiles.clear();
		this.tileHandler.reacheableTiles.clear();

		this.tileHandler.suddenDeathTiles.clear();
		this.tileHandler.suddenDeathTiles = tileHandler.getSuddenDeathTiles();

		tileHandler
				.fillAccessibleTilesBy(this.getZone().getOwnHero().getTile());
		tileHandler.fillSelectedTilesBy(this.getZone().getOwnHero().getTile());
		tileHandler.reacheableTiles.addAll(tileHandler.selectedTiles);

		if (enemyHandler.enemyTiles() != null)
			for (AiTile tile : enemyHandler.enemyTiles()) {
				checkInterruption();
				if (!this.tileHandler.selectedTiles.contains(tile))
					tileHandler.selectedTiles.add(tile);
			}

		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
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
	/** Gestionnaire chargé de nos adversaires */
	public EnemyHandler enemyHandler;
	/** Gestionnaire chargé des cases de la zone */
	public TileHandler tileHandler;
	/** Gestionnaire chargé des items de la zone */
	public ItemHandler itemHandler;

	@Override
	protected void initHandlers() {
		checkInterruption();

		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		itemHandler = new ItemHandler(this);
		tileHandler = new TileHandler(this);
		moveHandler = new MoveHandler(this);
		enemyHandler = new EnemyHandler(this);

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
	@Override
	protected void updateOutput() {
		checkInterruption();

		// pour modifier l'affichage
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

		bombHandler.updateOutput();
	}

	// public boolean triangle (AiTile tile) {
	// double tileCosts[][] = new
	// double[getZone().getHeight()][getZone().getWidth()];
	// // On initialise avec des zéros pour chaque case.
	// for (int r = 0; r < tileCosts.length; r++) {
	// for (int c = 0; c < tileCosts[0].length; c++) {
	// tileCosts[r][c] = 0;
	// }
	// }
	//
	// for (int r = 0; r < tileCosts.length; r++) {
	// for (int c = 0; c < tileCosts[0].length; c++) {
	// if (tileIsCorridor(zone.getTile(r, c)))
	// tileCosts[r][c] = AiTile.getSize() * 3;
	// }
	//
	// }
	//
	// }
}
