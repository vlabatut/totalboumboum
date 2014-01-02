package org.totalboumboum.ai.v201314.ais.donmezlabatcamy.v3;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;

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
	public AiTile endTile;
	/** */
	public int endTilePref;
	/** */
	private TileHandler tileHandler;
	/** */
	private ItemHandler itemHandler;
	/** */
	private HeroHandler heroHandler;
	/** */
	private CalculGeneral calculGeneral;
	/** */
	public Set<AiTile> selectTiles;
	/** */
	public boolean ennemyAccesibility;
	/** */
	public AiHero getClosestEnnemy;
	/** */
	public AiTile getWallToReachEnnemy;
	/** */
	public ArrayList<AiTile> getDangerousTiles;
	/** */
	public Set<AiTile> accesibleTiles;
	/** */
	public Set<AiTile> getSafeTiles;
	/** */
	public ArrayList<AiTile> getSecuretiles;
	/** */
	public AiTile getTilesWhichSurroundByThreeWall;
	/** */
	public ArrayList<AiTile> getDangerousTilesWhileBombExist;
	/** */
	public ArrayList<AiTile> dangerZoneForEnnemyBomb;
	/** */
	public Set<AiTile> getSafeTilesWithoutEnnemyBlast;

	/**
	 * Méthode permettant de faire une initialisation supplémentaire.
	 */
	@Override
	protected void initOthers() {
		checkInterruption();
		endTile = null;
		endTilePref = 100;
		ennemyAccesibility = false;
		getClosestEnnemy = null;
		getWallToReachEnnemy = null;
		getDangerousTiles = new ArrayList<AiTile>();
		getSecuretiles = new ArrayList<AiTile>();
		getSafeTiles = new TreeSet<AiTile>();
		accesibleTiles = new TreeSet<AiTile>();
		getSafeTilesWithoutEnnemyBlast = new TreeSet<AiTile>();
		getTilesWhichSurroundByThreeWall = null;
		dangerZoneForEnnemyBomb = new ArrayList<AiTile>();
		getDangerousTilesWhileBombExist = new ArrayList<AiTile>();

	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant d'initilaiser les percepts de l'agent c'est à dire les différents objets stockés en interne dans ces classes.
	 */
	@Override
	protected void initPercepts() {
		checkInterruption();

		selectTiles = new TreeSet<AiTile>();
		tileHandler = new TileHandler(this);
		itemHandler = new ItemHandler(this);
		heroHandler = new HeroHandler(this);
		calculGeneral = new CalculGeneral(this);

	}

	/**
	 * Méthode permettant de mettre à jour les percepts de l'agent c'est à dire les différents objets stockés en interne dans ces classes.
	 */
	@Override
	protected void updatePercepts() {
		checkInterruption();

		selectTiles.clear();
		selectTiles = this.preferenceHandler.selectTiles();

		accesibleTiles.clear();
		accesibleTiles = this.getTH().getAccesibleTiles();

		ennemyAccesibility = this.getHH().hasEnoughEnnemyAccesible();

		getClosestEnnemy = this.getHH().getClosestEnnemy();

		getWallToReachEnnemy = this.getHH().getWallToReachEnnemy();

		getTilesWhichSurroundByThreeWall = this.getTH().getTilesWhichSurroundByThreeWall();

		getSafeTiles.clear();
		getSafeTiles = this.getTH().getSafeTiles();

		getDangerousTiles.clear();
		getDangerousTiles = this.getTH().getDangerousTiles();

		getDangerousTilesWhileBombExist.clear();
		getDangerousTilesWhileBombExist = this.getTH().getDangerousTilesWhileBombExist(this.getZone().getOwnHero().getTile(),
				this.getZone().getOwnHero().getBombRange());

		getSecuretiles.clear();
		getSecuretiles = this.getTH().getSecuretiles();

		dangerZoneForEnnemyBomb.clear();
		dangerZoneForEnnemyBomb = this.getTH().dangerZoneForBomb();

		getSafeTilesWithoutEnnemyBlast.clear();
		getSafeTilesWithoutEnnemyBlast = this.getTH().getSafeTilesWithoutEnnemyBlast();

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
		output.setTextSize(3);
		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

	}
	
	
	// ///////////////////////////////////////////////////////////////
	// OUR HANDLERS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Gestionnaire chargé de calculer la position des agents adversaires
	 * 
	 * @return heroHandler
	 */
	public HeroHandler getHH() {
		checkInterruption();
		return heroHandler;
	}

	/**
	 * Gestionnaire chargé de calculer la position des items sur la zone de jeu
	 * 
	 * @return itemHandler
	 */
	public ItemHandler getIH() {
		checkInterruption();
		return itemHandler;
	}

	/**
	 * Gestionnaire chargé de catégoriser les types de cases sur la zone de jeu
	 * 
	 * @return tileHandler
	 */
	public TileHandler getTH() {
		checkInterruption();
		return tileHandler;
	}

	/**
	 * Gestionnaire chargé de réaliser des calculs générales
	 * 
	 * @return calculGeneral
	 */
	public CalculGeneral getCG() {
		checkInterruption();
		return calculGeneral;
	}
}
