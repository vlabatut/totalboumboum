package org.totalboumboum.ai.v201314.ais.saylamsonmez.v3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiSuddenDeathEvent;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * 
 * @author Berrenur Saylam
 * @author Kübra Sönmez
 */
public class Agent extends ArtificialIntelligence {
	/** zone */
	AiZone zone;
	/** our hero */
	AiHero ourHero;
	/** our current tile */
	AiTile ourTile;
	/** la liste des cases accessibles dans la zone */
	public ArrayList<AiTile> reachableTiles;
	/** le temps de faire le control pour mort subite */
	private static final long SUDDEN_DEATH_CONTROL_TIME = 3500;

	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent() {
		checkInterruption();

		// active/désactive la sortie texte
		//verbose = true;
	}

	@Override
	protected void initOthers() {
		checkInterruption();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// ///////////////////////////////////////////////////////////////
	// PERCEPTS /////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() {
		checkInterruption();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	@Override
	protected void updatePercepts() {
		checkInterruption();

		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
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

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
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

		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
		// les chemins et destinations courants
		moveHandler.updateOutput();
		// les preferences courantes
		preferenceHandler.updateOutput();

		// cf. la Javadoc dans ArtificialIntelligence pour une description de la
		// méthode
	}

	// ///////////////////////////////////////////////////////////////
	// OUR METHODES /////////////////////////////////////
	// ///////////////////////////////////////////////////////////////

	/**
	 * Methode pour calculer les cases accessibles.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	private void fillAccessibleTilesBy(AiTile tile) throws StopRequestException {
		checkInterruption();
		zone= getZone();
		ourHero = zone.getOwnHero();
		if (tile.isCrossableBy(ourHero)) {
			reachableTiles.add(tile);
			if (tile.getNeighbor(Direction.UP).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile.getNeighbor(Direction.UP)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.UP));
			if (tile.getNeighbor(Direction.DOWN).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.DOWN)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.DOWN));
			if (tile.getNeighbor(Direction.LEFT).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.LEFT)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.LEFT));
			if (tile.getNeighbor(Direction.RIGHT).isCrossableBy(ourHero)
					&& !reachableTiles.contains(tile
							.getNeighbor(Direction.RIGHT)))
				fillAccessibleTilesBy(tile.getNeighbor(Direction.RIGHT));
		}
	}

	/**
	 * Methode pour envoyer les cases accessibles.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return La liste des cases accessibles.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected ArrayList<AiTile> getReachableTiles(AiTile tile)
			throws StopRequestException {
		checkInterruption();
		reachableTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy(tile);

		return reachableTiles;
	}

	/**
	 * Methode pour calculer les cases secures.
	 * 
	 * @return Cette méthode retourne la case secure.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public AiTile getSecureTile() throws StopRequestException {
		checkInterruption();
		AiTile secureTile = null;
		ourHero = zone.getOwnHero();
		Map<AiTile, Integer> pref = preferenceHandler.getPreferencesByTile();
		List<AiTile> rTiles = getReachableTiles(ourHero.getTile());
		AiTile tempTile = null;
		int minPreference = Integer.MAX_VALUE;

		int preference = 0;
		for (Entry<AiTile, Integer> entry : pref.entrySet()) {
			checkInterruption();
			tempTile = entry.getKey();

			if (rTiles.contains(tempTile)) {
				print("TEMPTILE " + tempTile);
				preference = entry.getValue();
				if (minPreference >= preference) {
					minPreference = preference;
				}
			}
		}
		List<AiTile> tiles = new ArrayList<AiTile>();
		for (Entry<AiTile, Integer> entry : pref.entrySet()) {
			checkInterruption();
			preference = entry.getValue();
			tempTile = entry.getKey();
			if (rTiles.contains(tempTile))
				if (preference == minPreference) {
					tiles.add(tempTile);
					print("TILES SECURE " + tiles);
					secureTile = tempTile;
				}
		}
		if (tiles.contains(ourHero.getTile()))
			secureTile = ourHero.getTile();

		return secureTile;
	}

	/**
	 * Methode pour pouvoir calculer les cases dangereux.
	 * 
	 * @return La liste des cases qui ont une potentielle d'être dangereux.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles()
			throws StopRequestException {
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();

		for (AiBomb currentBomb : zone.getBombs()) {
			checkInterruption();
			dangerousTiles.add(currentBomb.getTile());
			for (AiTile currentTile : currentBomb.getBlast()) {
				checkInterruption();
				dangerousTiles.add(currentTile);
			}
		}
		for (AiFire currentFire : zone.getFires()) {
			checkInterruption();
			dangerousTiles.add(currentFire.getTile());
		}

		return dangerousTiles;
	}

	/**
	 * Methode pour calculer l'effet mort subit.
	 * 
	 * @return Retourne la liste des cases qui ont l'effet mort subit.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public ArrayList<AiTile> getSuddenDeathTiles() throws StopRequestException {
		checkInterruption();
		ArrayList<AiTile> result = new ArrayList<AiTile>();

		if (!zone.getAllSuddenDeathEvents().isEmpty()) {
			for (AiSuddenDeathEvent suddenDeath : zone
					.getAllSuddenDeathEvents()) {
				checkInterruption();
				if (suddenDeath.getTime() < zone.getTotalTime()
						+ SUDDEN_DEATH_CONTROL_TIME) {

					for (AiTile tile : suddenDeath.getTiles()) {

						checkInterruption();
						result.add(tile);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Méthode pour calculer la distance de la bloque. Par rapport à cette
	 * valeur, on décide dans le mode handler est-ce que notre bombe a assez
	 * portée pour réaliser la stratégie d'attaque.
	 * 
	 * @param hero
	 *            Les agents dans la zone à part de nous.
	 * 
	 * @return Cette méthode retourne la distance de bloque.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public int getBlockSize(AiHero hero) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		ourHero = zone.getOwnHero();

		ArrayList<AiTile> currentDangerousTiles = getCurrentDangerousTiles();
		int i = 1;
		ourTile = ourHero.getTile();

		AiTile tileHero = hero.getTile();
		checkInterruption();
		if (tileHero != null) {

			while (tileHero.getNeighbor(Direction.DOWN).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				checkInterruption();
				tileHero = tileHero.getNeighbor(Direction.DOWN);
				if (result == true) {
					if ((!tileHero.getNeighbor(Direction.LEFT).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.LEFT)))
							&& (!tileHero.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.RIGHT)))) {
						result = true;
						i++;
					} else {
						i = 1;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.UP).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				checkInterruption();
				tileHero = tileHero.getNeighbor(Direction.UP);
				if (result == true) {
					if ((!tileHero.getNeighbor(Direction.LEFT).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.LEFT)))
							&& (!tileHero.getNeighbor(Direction.RIGHT)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.RIGHT)))) {
						result = true;
						i++;
					} else {
						i = 1;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.LEFT).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				checkInterruption();
				tileHero = tileHero.getNeighbor(Direction.LEFT);
				if (result == true) {
					if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.UP)))
							&& (!tileHero.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.DOWN)))) {
						result = true;

					} else {
						i = 1;
						break;
					}
				}

			}

			tileHero = hero.getTile();
			while (tileHero.getNeighbor(Direction.RIGHT).isCrossableBy(hero,
					false, false, false, false, true, true)) {
				checkInterruption();
				tileHero = tileHero.getNeighbor(Direction.RIGHT);
				if (result == true) {
					if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
							hero, false, false, false, false, true, true) || currentDangerousTiles
							.contains(tileHero.getNeighbor(Direction.UP)))
							&& (!tileHero.getNeighbor(Direction.DOWN)
									.isCrossableBy(hero, false, false, false,
											false, true, true) || currentDangerousTiles
									.contains(tileHero
											.getNeighbor(Direction.DOWN)))) {
						result = true;
						i++;
					}

					else {
						i = 1;
						break;
					}
				}

			}
		}
		print("Aralık sayısı " + i);
		return i;
	}

	/**
	 * Methode pour aider les items cachees. S'il y a plusieurs murs
	 * destructibles autours d'une case, alors il y a plus de chances pour
	 * trouver un item cachée.
	 * 
	 * @param sourceTile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Le nombre de murs destructibles autour de la case.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public int getNbMurDetruitofTile(AiTile sourceTile)
			throws StopRequestException {
		checkInterruption();
		int result = 0;
		AiTile tileUp = sourceTile.getNeighbor(Direction.UP);
		AiTile tileDown = sourceTile.getNeighbor(Direction.DOWN);
		AiTile tileLeft = sourceTile.getNeighbor(Direction.LEFT);
		AiTile tileRight = sourceTile.getNeighbor(Direction.RIGHT);
		int i = 1;
		int bombRange = zone.getOwnHero().getBombRange();
		// obstacles sont pour terminer les recherce de murs quand on se
		// rencontre des murs.
		boolean[] obstacle = { true, true, true, true, true };

		while (obstacle[4] && (i <= bombRange)) {
			checkInterruption();

			List<AiBlock> blocks;

			if (obstacle[0]) {
				blocks = tileUp.getBlocks();
				if (!tileUp.getItems().isEmpty())
					obstacle[0] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[0] = false;
					}
				}

				tileUp = tileUp.getNeighbor(Direction.UP);
			}
			if (obstacle[1]) {
				blocks = tileDown.getBlocks();
				if (!tileDown.getItems().isEmpty())
					obstacle[1] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[1] = false;
					}
				}
				tileDown = tileDown.getNeighbor(Direction.DOWN);
			}
			if (obstacle[2]) {
				blocks = tileLeft.getBlocks();
				// kontrolun amacı eğer source tile'ın sağında solunda üstünde
				// altında item varsa oraya bomba koyma
				if (!tileLeft.getItems().isEmpty())
					obstacle[2] = false;
				// bu kontolde ise block var mı yok mu ona bakılıyor.
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[2] = false;
					}
				}
				tileLeft = tileLeft.getNeighbor(Direction.LEFT);
			}
			if (obstacle[3]) {
				blocks = tileRight.getBlocks();
				if (!tileRight.getItems().isEmpty())
					obstacle[3] = false;
				else if (!blocks.isEmpty()) {
					for (AiBlock block : blocks) {
						checkInterruption();
						if (block.isDestructible())
							result++;
						obstacle[3] = false;
					}
				}
				tileRight = tileRight.getNeighbor(Direction.RIGHT);
			}
			if ((!obstacle[0]) && (!obstacle[1])
					&& (!obstacle[2] && (!obstacle[3])))
				obstacle[4] = false;

			i++;

		}
		if (result > 4)
			result = 4;

		return result;
	}

	/**
	 * Méthode pour tester s'il y a un critère de bloque.
	 * 
	 * @param tile
	 *            Case pour commencer regarder à partir de.
	 * 
	 * @return Cette méthode returne une valeur booléan, est-ce que la situation
	 *         d'etre bloque assure.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public Boolean isBlockingEnemy(AiTile tile) throws StopRequestException {
		checkInterruption();
		boolean result = true;
		ourHero = zone.getOwnHero();

		ArrayList<AiTile> currentDangerousTiles = getCurrentDangerousTiles();
		int bombRange = ourHero.getBombRange();
		int i = 0;
		ourTile = ourHero.getTile();
		AiSimZone simZone = new AiSimZone(zone);
		AiBomb myBomb = simZone.createBomb(tile, simZone.getOwnHero());
		if (!simZone.getRemainingOpponents().isEmpty()) {
			Enemy e = new Enemy(this);
			AiHero hero = e.selectEnemy();
			AiTile tileHero = hero.getTile();
			i = 0;
			if (myBomb.getBlast().contains(tileHero)) {
				while (tileHero.getNeighbor(Direction.DOWN).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.DOWN).equals(
								myBomb.getTile())) {
					checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.DOWN);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero, false, false, false,
										false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.LEFT)))
								&& (!tileHero.getNeighbor(Direction.RIGHT)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.RIGHT)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}
				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.UP).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.UP).equals(
								myBomb.getTile())) {
					checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.UP);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.LEFT)
								.isCrossableBy(hero, false, false, false,
										false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.LEFT)))
								&& (!tileHero.getNeighbor(Direction.RIGHT)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.RIGHT)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}

				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.LEFT).isCrossableBy(hero,
						false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.LEFT).equals(
								myBomb.getTile())) {
					checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.LEFT);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
								hero, false, false, false, false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.UP)))
								&& (!tileHero.getNeighbor(Direction.DOWN)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.DOWN)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;
						else
							result = false;
					}
					i++;
				}
				i = 0;
				tileHero = hero.getTile();
				while (tileHero.getNeighbor(Direction.RIGHT).isCrossableBy(
						hero, false, false, false, false, true, true)
						&& i <= bombRange
						&& !tileHero.getNeighbor(Direction.RIGHT).equals(
								myBomb.getTile())) {
					checkInterruption();
					tileHero = tileHero.getNeighbor(Direction.RIGHT);
					if (result == true) {
						if ((!tileHero.getNeighbor(Direction.UP).isCrossableBy(
								hero, false, false, false, false, true, true) || currentDangerousTiles
								.contains(tileHero.getNeighbor(Direction.UP)))
								&& (!tileHero.getNeighbor(Direction.DOWN)
										.isCrossableBy(hero, false, false,
												false, false, true, true) || currentDangerousTiles
										.contains(tileHero
												.getNeighbor(Direction.DOWN)))
								&& myBomb.getBlast().contains(tileHero))
							result = true;

						else
							result = false;
					}
					i++;
				}
			} else
				result = false;

		}
		return result;
	}

	/**
	 * Méthode pour pouvoir decider à un item bonus ou pas.
	 * 
	 * @param item
	 *            L'item à controler.
	 * 
	 * @return Cette méthode returne boolean, est-ce que l'item est bonus ou pas
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	public boolean isBonus(AiItem item) throws StopRequestException {
		checkInterruption();
		AiItemType type = item.getType();
		if (type.equals(AiItemType.EXTRA_BOMB)
				|| type.equals(AiItemType.EXTRA_FLAME)
				|| type.equals(AiItemType.EXTRA_SPEED)
				|| type.equals(AiItemType.GOLDEN_BOMB)
				|| type.equals(AiItemType.GOLDEN_FLAME)
				|| type.equals(AiItemType.GOLDEN_SPEED)
				|| type.equals(AiItemType.RANDOM_EXTRA))
			return true;
		return false;
	}

	/**
	 * Methode pour envoyer l'ennemie qui est choisi.
	 * 
	 * @return L'ennemie ce qui est choisi.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/*
	public AiHero SelectEnemy() throws StopRequestException {
		checkInterruption();
		AiHero enemy = null;
		enemy = HighPointEnemy();
		if (enemy == null)
			enemy = NearestEnemy();
		return enemy;
	}*/

	/**
	 * Cette méthode nous permet de calculer le plus proche ennemie.
	 * 
	 * @return Le plus proche l'ennemie.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/*
	public AiHero NearestEnemy() throws StopRequestException {
		checkInterruption();
		AiHero enemy = null;
		int distance = 1000;
		int distanceBetweEnenemy = 0;
		for (AiHero hero : this.getZone().getRemainingOpponents()) {
			checkInterruption();
			distanceBetweEnenemy = zone.getTileDistance(ourHero.getTile(),
					hero.getTile());
			if (distance > distanceBetweEnenemy) {
				distance = distanceBetweEnenemy;
				enemy = hero;
			}
		}
		return enemy;
	}*/

	/**
	 * Methode pour decider à quel ennemie on va choisir. Si dans la zone il y a
	 * un ennemie qui a max point, alors on le choisit.
	 * 
	 * @return Retourne comme l'ennemie qui a le plus point.
	 * 
	 * @throws StopRequestException
	 *             Au cas où le moteur demande la terminaison de l'agent.
	 */
	/*
	public AiHero HighPointEnemy() {
		checkInterruption();
		AiHero enemy = null;
		int maxPoint = Integer.MIN_VALUE;
		for (AiHero hero : zone.getRemainingOpponents()) {
			checkInterruption();
			if (hero.getMatchRank() != 0) {
				if (maxPoint < hero.getMatchRank()) {
					maxPoint = hero.getMatchRank();
					enemy = hero;
				}
			}
		}
		return enemy;

	}*/

}
