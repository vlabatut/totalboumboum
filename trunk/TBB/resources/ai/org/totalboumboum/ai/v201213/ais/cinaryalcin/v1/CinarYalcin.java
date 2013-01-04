package org.totalboumboum.ai.v201213.ais.cinaryalcin.v1;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v201213.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.data.AiBlock;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMode;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * 
 *
 * @author Bekir Cınar
 * @author Deniz Yalçın
 */
public class CinarYalcin extends ArtificialIntelligence
{
	
	/** */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;
	
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public CinarYalcin()
	{	// active/désactive la sortie texte
		verbose = false;
	}
	
	
	
	/**
	 * 
	 */
	
	@Override
	protected void initOthers() throws StopRequestException
	{	checkInterruption();
		
		// TODO à compléter si vous voulez créer des objets 
		// particuliers pour réaliser votre traitement, et qui
		// ne sont ni des gestionnaires (initialisés dans initHandlers)
		// ni des percepts (initialisés dans initPercepts).
		// Par exemple, ici on surcharge initOthers() pour initialiser
		// verbose, qui est la variable controlant la sortie 
		// texte de l'agent (true -> debug, false -> pas de sortie)
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts() throws StopRequestException
	{	checkInterruption();
	
		// TODO à compléter si vous voulez créer des objets 
		// particuliers pour réaliser votre traitement.
		// Ils peuvent être stockés dans cette classe ou dans
		// un gestionnaire quelconque. 
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	checkInterruption();
		
		// TODO à compléter si vous avez des objets 
		// à mettre à jour à chaque itération, e.g.
		// des objets créés par la méthode initPercepts().
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
	protected UtilityHandler utilityHandler;
	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers() throws StopRequestException
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		utilityHandler = new UtilityHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		// TODO à compléter si vous utilisez d'autres gestionnaires
		// (bien sûr ils doivent aussi être déclarés ci-dessus)
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<CinarYalcin> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<CinarYalcin> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<CinarYalcin> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<CinarYalcin> getMoveHandler() throws StopRequestException
	{	checkInterruption();
		return moveHandler;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	checkInterruption();

		// TODO à compléter si vous voulez modifier l'affichage
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les utilités courantes
			utilityHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	

	/**
	 * @return get current mode
	 */
	public AiMode getMode() {
		AiMode mode=modeHandler.getMode();
		return mode;
	}
	
	/**
	 * @return get current dangerous tiles in all zone
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone=getZone();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		for ( AiBomb currentBomb : zone.getBombs() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				this.checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : zone.getFires() )
		{
			this.checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		return dangerousTiles;
	}
	
	/**
	 * @param aitile 
	 * @return return true if selected wall in danger
	 * @throws StopRequestException  */
	public boolean getWallInDanger(AiTile aitile) throws StopRequestException {
		this.checkInterruption();

		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		boolean result = false;
		if (!aitile.getBlocks().isEmpty()) {
			for (AiBlock currentBlock : aitile.getBlocks()) {
				this.checkInterruption();
				if (currentBlock.isDestructible()) {
					for (AiBomb currentBomb : zone.getBombs()) {
						this.checkInterruption();
						if (currentBomb.getBlast().contains(aitile)) 
							result = true;
					}
				}
			}
		}
		return result;
	}
	
	
	/**
	 * @param aitile
	 * @param direction
	 * @return return true if there isn't any block in given tile's direction specified
	 * @throws StopRequestException
	 */
	public boolean controlOfBlocks(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;

		if (aitile.getNeighbor(direction).getBlocks().isEmpty()) {
			if (aitile.getNeighbor(direction).getBombs().isEmpty()) {
				result = true;

			}
		}
		return result;

	}
	
	
	/**
	 * @param aitile
	 * @param direction
	 * @return returns 1 if there is an enemy in your hero's neighbors
	 * @throws StopRequestException
	 */
	public boolean controlEnemy(AiTile aitile, Direction direction)
			throws StopRequestException {
		this.checkInterruption();

		boolean result = false;

		if (getNearestEnemy().getTile().equals(aitile.getNeighbor(direction))) {
			result = true;
		}
		return result;
	}
	

	/**
	 * @param aitile
	 * @param direction
	 * @param i
	 * @return returns true if your hero's bomb can reach an enemy
	 * @throws StopRequestException
	 */
	public boolean getAnEnemyInMyRange(AiTile aitile, Direction direction, int i)
			throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		int currentrange = ourhero.getBombRange();

		if (!zone.getRemainingOpponents().isEmpty()) {

			while (i < currentrange) {
				this.checkInterruption();
				if (controlOfBlocks(aitile, direction) == true
						&& aitile.getNeighbor(direction).getItems().isEmpty())
				{
					if (controlEnemy(aitile, direction) == true) {
						result = true;
					} 
					else {
						i++;
						result = getAnEnemyInMyRange(
								aitile.getNeighbor(direction), direction, i);
					}
				}

				else result = false;			
			}
		}
		return result;
	}
	

	/**
	 * @param hero
	 * @param tile
	 * @param simulate
	 * @return finds all secure tiles for given hero
	 * @throws StopRequestException
	 */
	public List<AiTile> getSafeTiles(AiHero hero, AiTile tile, boolean simulate)
			throws StopRequestException {
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		AiSimZone simzone = new AiSimZone(zone);
		ArrayList<AiTile> list = new ArrayList<AiTile>();
		ArrayList<AiTile> safe = new ArrayList<AiTile>();
		ArrayList<AiTile> blastflamelist = new ArrayList<AiTile>();

		list.add(hero.getTile());

		for (AiBomb b : simzone.getBombs()) {
			this.checkInterruption();

			if (b.getBlast().contains(hero.getTile()))
				continue;

			for (AiTile t : b.getBlast()) {
				this.checkInterruption();
				blastflamelist.add(t);
			}
		}

		for (AiFire aifire : simzone.getFires()) {
			this.checkInterruption();
			blastflamelist.add(aifire.getTile());
		}
		while (!list.isEmpty()) {
			this.checkInterruption();
			ArrayList<AiTile> list1 = new ArrayList<AiTile>();
			for (AiTile aitile : list) {
				this.checkInterruption();
				for (Direction direction : Direction.getPrimaryValues()) {
					this.checkInterruption();
					AiTile neighbor = aitile.getNeighbor(direction);

					if (neighbor.isCrossableBy(hero)
							&& !safe.contains(neighbor)) {
						if (!blastflamelist.contains(neighbor)) {
							list1.add(neighbor);
						}
					}

				}
				safe.add(aitile);
			}
			for (AiTile aitile : safe) {
				this.checkInterruption();
				if (list.contains(aitile)) {
					list.remove(aitile);
				}
			}
			for (AiTile aitile : list1) {
				this.checkInterruption();
				list.add(aitile);
			}
		}
		// simulates our bomb and removes blast range from the safe tiles.
		if (simulate) {
			AiBomb b = simzone.createBomb(tile, simzone.getOwnHero());
			safe.removeAll(b.getBlast());
		}
		// checks if we have chain reaction possibilities and removes those
		// tiles.
		/*
		 * ArrayList<AiTile> list4 = new ArrayList<AiTile>();
		 * if(!safe.isEmpty()) { for (AiTile aiTile : safe)
		 * {this.checkInterruption(); if(getDanger2(aiTile)) {
		 * list4.add(aiTile); } } } safe.removeAll(list4);
		 */
		return safe;
	}
	

	/**
	 * @return finds your agent's nearest opponent
	 * @throws StopRequestException
	 */
	public AiHero getNearestEnemy() throws StopRequestException {
		this.checkInterruption();
		int enemydist = 10000;
		AiHero nearestEnemy = null;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		for (AiHero enemy : zone.getRemainingOpponents()) {
			this.checkInterruption();
			if (enemy.hasEnded())
				continue;
			int dist = this.getDist(enemy.getTile(), ourtile);
			if (dist < enemydist) {
				nearestEnemy = enemy;
				enemydist = dist;
			}
		}
		return nearestEnemy;
	}
	
	
	/**
	 * @param aitile
	 * @param aitile1
	 * @return get distance between aitile and aitile1 with Manhattan algorithm.
	 * @throws StopRequestException
	 */
	public int getDist(AiTile aitile, AiTile aitile1)
			throws StopRequestException {
		this.checkInterruption();
		int distance = Math.abs(aitile.getCol() - aitile1.getCol())
				+ Math.abs(aitile.getRow() - aitile1.getRow());
		return distance;
	}
	
	/**
	 * @param tile
	 * @return j
	 * @throws StopRequestException
	 */
	public Boolean simBlock(AiTile tile) throws StopRequestException {
		this.checkInterruption();
		boolean result = false;
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		if (!zone.getRemainingOpponents().isEmpty()) {
			if (getSafeTiles(getNearestEnemy(), tile, true).isEmpty()) {
				return result = true;
			}
		}
		return result;
	}
}
