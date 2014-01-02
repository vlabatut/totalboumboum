package org.totalboumboum.ai.v201314.ais.asilizeryuce.v2;

import java.util.ArrayList;
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
 * Classe principale de notre agent. on va completer cette classe pendant la
 * semestre
 * 
 * @author Emre Asıl
 * @author Tülin İzer
 * @author Miray Yüce
 */
public class Agent extends ArtificialIntelligence {
	
	/** cases accessibles, retourne une list de ces cases */
	public ArrayList<AiTile> accessibleTiles;
	
	/** on ajoute les cases qui contient d'ennemi dans les cases accessibles, si les cases
	 * accessibles ne contiennent pas d'ennemi
	 * c'est une liste et retourne une liste des cases accessibles avec des cases qui contiennent des ennemis
	 */
	public ArrayList<AiTile> selectedTiles;

	/**
	 * On utilise pour savoir quelle case est plus proche a un ennemi
	 * inaccessible
	 */
	public AiTile wallAccessibleTile;

	/** c'est la dernier direction selectionnee*/
	public Direction lastDirection;

	/** la case prochaine dans notre chemin*/
	public AiTile nextTile;
	
	/** si notre chemin est indirect, on pose cette question pour savoir 
	 * est-ce q'on doit mets dans prochaine case une bombe ou pas?*/
	public boolean nextTileHasBlock = false;
	
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
		
		this.accessibleTiles = new ArrayList<AiTile>();
		this.selectedTiles = new ArrayList<AiTile>();
		
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
		this.accessibleTiles.clear();
		this.selectedTiles.clear();
		
		fillAccessibleTilesBy(this.getZone().getOwnHero().getTile());
		fillSelectedTilesBy(this.getZone().getOwnHero().getTile());

		for (AiTile tile : enemyTiles()) {
			checkInterruption();
			if (!this.selectedTiles.contains(tile))
				selectedTiles.add(tile);
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

	/**
	 * Recursive method to fill a list of accessible tiles.
	 * 
	 * @param sourceTile The tile to start looking from. 
	 * If not crossable, list will not be populated.
	 */
	private void fillAccessibleTilesBy(AiTile sourceTile) {
		checkInterruption();
		AiHero hero = getZone().getOwnHero();
		if (sourceTile.isCrossableBy(hero)) {
			this.accessibleTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
					&& !this.accessibleTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}

	}
	
	/**
	 * Recursive method to fill a list of selected tiles.
	 * @param sourceTile The tile to start looking from.
	 */
	private void fillSelectedTilesBy(AiTile sourceTile) {
		checkInterruption();
		AiHero hero = getZone().getOwnHero();
		if (sourceTile.isCrossableBy(hero)) {
			this.selectedTiles.add(sourceTile);
			if (sourceTile.getNeighbor(Direction.UP).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.UP)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.UP));
			if (sourceTile.getNeighbor(Direction.DOWN).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.DOWN)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.DOWN));
			if (sourceTile.getNeighbor(Direction.LEFT).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.LEFT)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.LEFT));
			if (sourceTile.getNeighbor(Direction.RIGHT).isCrossableBy(hero)
					&& !this.selectedTiles.contains(sourceTile
							.getNeighbor(Direction.RIGHT)))
				fillSelectedTilesBy(sourceTile.getNeighbor(Direction.RIGHT));
		}

	}


	/**
	 * cette boolean method pour savoir si un mur est destructible ou indestructible
	 * @param aiTile ou on regarde
	 * @return une value boolean.si le mur est indestructible, il retourne true, false vice versa
	 */
	public boolean hasIndestructibleWall(AiTile aiTile) {
		checkInterruption();
		boolean result = false;
		for (AiBlock block : aiTile.getBlocks()) {
			checkInterruption();
			if (!block.isDestructible())
				result = true;
		}

		return result;
	}

	/**
	 * To get the accessible tiles from a given source tile. (TESTED, WORKS)
	 * 
	 * @param sourceTile Tile to start looking from.
	 * @return List of all tiles that accessible from a given source tile.
	 */
	public ArrayList<AiTile> getAccessibleTilesFrom(AiTile sourceTile) {
		this.checkInterruption();

		fillAccessibleTilesBy(sourceTile);

		return this.accessibleTiles;
	}


	/**
	 * pour calculer la distance entre deux cases
	 * 
	 * @param tile1 le tile depart
	 * @param tile2 le tile arrivé
	 * @return la distance entre deux cases
	 */
	public int simpleTileDistance(AiTile tile1, AiTile tile2) {
		checkInterruption();
		return Math.abs(tile1.getCol() - tile2.getCol())
				+ Math.abs(tile1.getRow() - tile2.getRow());
	}

	/**
	 * cette methode est pour savoir si un item est un malus ou pas
	 * 
	 * @param item item qu'on veut savoir s'il est un item malus
	 * @return true s'il ya un malus item, sinon false
	 */
	public boolean isMalus(AiItem item) {
		checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.ANTI_BOMB)
				|| type.equals(AiItemType.ANTI_FLAME)
				|| type.equals(AiItemType.ANTI_SPEED)
				|| type.equals(AiItemType.NO_BOMB)
				|| type.equals(AiItemType.NO_FLAME)
				|| type.equals(AiItemType.NO_SPEED)
				|| type.equals(AiItemType.RANDOM_NONE))
			return true;
		return false;
	}

	/**
	 * cette methode prend les cases qui contiennent de flamme ou de bombe, et
	 * les met dans une liste
	 * 
	 * @return dangerousTiles c'est un liste concernant les cases qui
	 *         contiennent de bombe ou de flamme
	 */
	public ArrayList<AiTile> dangerousTiles() {
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		
		for (AiBomb currentBomb : getZone().getBombs()) {
			checkInterruption();
			for (AiTile currentTile : currentBomb.getBlast()) {
				checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : getZone().getFires()) {
			checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}
		return dangerousTiles;
	}

	/**
	 * Cette methode nous donne la liste des ennemies vivant.
	 * 
	 * @return Cette methode nous donne la liste des ennemies vivant.
	 */
	public ArrayList<AiTile> enemyTiles() {
		checkInterruption();
		ArrayList<AiTile> enemyTiles = new ArrayList<AiTile>();
		for (AiHero hero : getZone().getRemainingOpponents()) {
			checkInterruption();
			enemyTiles.add(hero.getTile());
		}
		return enemyTiles;
	}

	/**
	 * on controle le colon selon la portee de notre bombe
	 * 
	 * @param tile qu'on veut savoir si on pose une bombe dans ce tile, est-ce
	 * que cette bombe peut tuer l'adversaire, ou elle peut menacer
	 * l'adversaire. on controle le colonne selon la portee de notre
	 * bombe
	 * @return 0 s'il ya une ennemi dans cette colonne et 1 s'il n'y a pas
	 * d'ennemi dans cette cologne.
	 */
	public int getEnemyInColumn(AiTile tile) {
		checkInterruption();
		AiHero hero = getZone().getOwnHero();
		int range = hero.getBombRange();
		if (range > 8)
			range = 8;
		int result = 1, block = 0;
		AiTile tile2 = tile;

		if (this.enemyTiles().contains(tile))
			return 2;

		for (int i = 0; i < range; i++) {
			checkInterruption();
			tile = tile.getNeighbor(Direction.UP);
			if (this.enemyTiles().contains(tile) && block == 0)
				return 0;
			if (!tile.getBlocks().isEmpty())
				block++;
		}
		block = 0;
		for (int i = 0; i < range; i++) {
			checkInterruption();
			tile2 = tile2.getNeighbor(Direction.DOWN);
			if (this.enemyTiles().contains(tile2) && block == 0)
				return 0;
			if (!tile2.getBlocks().isEmpty())
				block++;
		}

		return result;

	}
	
	

	/**
	 * on controle la ligne selon la portee de notre bombe
	 * 
	 * @param tile qu'on veut savoir si on pose une bombe dans ce tile, est-ce
	 * que cette bombe peut tuer l'adversaire, ou elle peut menacer
	 * l'adversaire.
	 * @return 0 s'il ya une ennemi dans cette ligne et 1 s'il n'y a pas
	 * d'ennemi dans cette ligne.
	 */
	public int getEnemyInRow(AiTile tile) {
		checkInterruption();
		AiHero hero = getZone().getOwnHero();
		int range = hero.getBombRange();
		if (range > 8)
			range = 8;
		int result = 1, block = 0;
		AiTile tile2 = tile;
		if (this.enemyTiles().contains(tile))
			return 2;

		for (int i = 0; i < range; i++) {
			checkInterruption();
			tile = tile.getNeighbor(Direction.LEFT);
			if (this.enemyTiles().contains(tile) && block == 0)
				return 0;
			if (!tile.getBlocks().isEmpty())
				block++;
		}

		block = 0;
		for (int i = 0; i < range; i++) {
			checkInterruption();
			tile2 = tile2.getNeighbor(Direction.RIGHT);
			if (this.enemyTiles().contains(tile2) && block == 0)
				return 0;
			if (!tile2.getBlocks().isEmpty())
				block++;
		}

		return result;
	}

}