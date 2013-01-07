package org.totalboumboum.ai.v201213.ais.saglamseven.v4;

import java.util.ArrayList;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201213.adapter.agent.AiUtilityHandler;
import org.totalboumboum.ai.v201213.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiBomb;
import org.totalboumboum.ai.v201213.adapter.data.AiFire;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiItem;
import org.totalboumboum.ai.v201213.adapter.data.AiItemType;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 *
 * @author Esra Sağlam
 * @author Cihan Adil Seven
 */
public class SaglamSeven extends ArtificialIntelligence
{
	
//	/**
//	 * Instancie la classe principale de l'agent.
//	 */
//	public SaglamSeven()
//	{	// active/désactive la sortie texte
//		verbose = true;
//	}
//	
//	@Override
//	protected void initOthers() throws StopRequestException
//	{	checkInterruption();
//		
//		// TODO à compléter si vous voulez créer des objets 
//		// particuliers pour réaliser votre traitement, et qui
//		// ne sont ni des gestionnaires (initialisés dans initHandlers)
//		// ni des percepts (initialisés dans initPercepts).
//		// Par exemple, ici on surcharge initOthers() pour initialiser
//		// verbose, qui est la variable controlant la sortie 
//		// texte de l'agent (true -> debug, false -> pas de sortie)
//	
//		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
//	}
//	
//	/////////////////////////////////////////////////////////////////
//	// PERCEPTS			/////////////////////////////////////////////
//	/////////////////////////////////////////////////////////////////
//	@Override
//	protected void initPercepts() throws StopRequestException
//	{	checkInterruption();
//	
//		// TODO à compléter si vous voulez créer des objets 
//		// particuliers pour réaliser votre traitement.
//		// Ils peuvent être stockés dans cette classe ou dans
//		// un gestionnaire quelconque. 
//	
//		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
//	}
//	
//	@Override
//	protected void updatePercepts() throws StopRequestException
//	{	checkInterruption();
//		
//		// TODO à compléter si vous avez des objets 
//		// à mettre à jour à chaque itération, e.g.
//		// des objets créés par la méthode initPercepts().
//	
//		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
//	}
//	
//	/////////////////////////////////////////////////////////////////
//	// HANDLERS			/////////////////////////////////////////////
//	/////////////////////////////////////////////////////////////////
//	/** gestionnaire chargé de calculer le mode de l'agent */
//	protected ModeHandler modeHandler;
//	/** gestionnaire chargé de calculer les valeurs d'utilité de l'agent */
//	protected UtilityHandler utilityHandler;
//	/** gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
//	protected BombHandler bombHandler;
//	/** gestionnaire chargé de décidé de la direction de déplacement de l'agent */
//	protected MoveHandler moveHandler;
//	
//	@Override
//	protected void initHandlers() throws StopRequestException
//	{	checkInterruption();
//		
//		// création des gestionnaires standard (obligatoires)
//		modeHandler = new ModeHandler(this);
//		utilityHandler = new UtilityHandler(this);
//		bombHandler = new BombHandler(this);
//		moveHandler = new MoveHandler(this);
//		
//		// TODO à compléter si vous utilisez d'autres gestionnaires
//		// (bien sûr ils doivent aussi être déclarés ci-dessus)
//		
//		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
//	}
//
//	@Override
//	protected AiModeHandler<SaglamSeven> getModeHandler() throws StopRequestException
//	{	checkInterruption();
//		return modeHandler;
//	}
//
//	@Override
//	protected AiUtilityHandler<SaglamSeven> getUtilityHandler() throws StopRequestException
//	{	checkInterruption();
//		return utilityHandler;
//	}
//
//	@Override
//	protected AiBombHandler<SaglamSeven> getBombHandler() throws StopRequestException
//	{	checkInterruption();
//		return bombHandler;
//	}
//
//	@Override
//	protected AiMoveHandler<SaglamSeven> getMoveHandler() throws StopRequestException
//	{	checkInterruption();
//		return moveHandler;
//	}
//
//	/////////////////////////////////////////////////////////////////
//	// OUTPUT			/////////////////////////////////////////////
//	/////////////////////////////////////////////////////////////////
//	@Override
//	protected void updateOutput() throws StopRequestException
//	{	checkInterruption();
//
//		// TODO à compléter si vous voulez modifier l'affichage
//		// ici, par défaut on affiche :
//			// les chemins et destinations courants
//			moveHandler.updateOutput();
//			// les utilités courantes
//			utilityHandler.updateOutput();
//	
//		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
//	}
//	
//	public int calculDistance(AiTile tileA, AiTile tileB) throws StopRequestException{
//		this.checkInterruption();
//		int distance = Math.abs(tileA.getCol()-tileB.getCol())+ Math.abs(tileA.getRow()-tileB.getRow());
//		return distance;
//	}
//	
//	
//	/**
//	 * @param tile
//	 * 		prends tile
//	 * @return
//	 * @throws StopRequestException
//	 */
//	public AiTile plusProcheTile(AiTile tile) throws StopRequestException{
//		this.checkInterruption();
//		Set<AiTile> tileList = this.utilityHandler.selectTiles();
//		AiTile plusProcheTile = null;
//		float valeur = 50 ;
//		org.totalboumboum.ai.v201213.adapter.data.AiZone aiZone = getZone();
//		AiHero propreHero = aiZone.getOwnHero();
//		AiTile propreTile = propreHero.getTile();
//		
//		if(tile.equals(propreTile)){
//			tileList.remove(propreTile);
//		}
//		
//		for (AiTile currentTile : tileList){
//			this.checkInterruption();
//			if(calculDistance(tile,currentTile) < valeur){
//				valeur = calculDistance(tile,currentTile);
//				plusProcheTile = currentTile;
//			}
//		}
//		
//		return plusProcheTile;
//		
//		
//	}
//	
//	/**
//	 * @param item
//	 * 		Une item de jeu, bonus ou malus
//	 * @return
//	 * 		Un type de boolean, si l'item est bonus il renvoye true, si l'item est malus il renvoye false 
//	 */
//	public Boolean isBonusItem(AiItem item){
//		boolean bonus = true;
//		
//		if((AiItemType.ANTI_BOMB != null) || (AiItemType.ANTI_FLAME != null) 
//				|| (AiItemType.ANTI_SPEED != null) || (AiItemType.NO_BOMB != null) 
//				|| (AiItemType.NO_FLAME != null) || (AiItemType.NO_SPEED != null) 
//				|| (AiItemType.RANDOM_NONE != null))
//			bonus = false;
//		
//		return bonus;
//	}
//	

	
	
	
	//ESRA MAIN STARTS HERE
	
	/**
	 * Instancie la classe principale de l'agent.
	 */
	AiZone zone;
	/** */
	AiHero ourhero;
	/** */
	AiTile ourtile;
	/** */
	public AiTile objective = null;
	/**
	 * 
	 */
	public SaglamSeven()
	{	// active/désactive la sortie texte
		verbose = false;
	}
	/**
	 * 
	 */
	private final int RANGE_BOTTOM_LIMIT = 0;
	
	/**
	 * 
	 */
	private final int DISTANCE_BOTTOM_LIMIT	= 0;
	/**
	 * 
	 */
	private final int SAFETY_TILE_COUNT_LIMIT = 0;
	
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
	public ModeHandler modeHandler;
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
	protected AiModeHandler<SaglamSeven> getModeHandler() throws StopRequestException
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiUtilityHandler<SaglamSeven> getUtilityHandler() throws StopRequestException
	{	checkInterruption();
		return utilityHandler;
	}

	@Override
	protected AiBombHandler<SaglamSeven> getBombHandler() throws StopRequestException
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<SaglamSeven> getMoveHandler() throws StopRequestException
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
	 * @return ?? 
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getCurrentDangerousTiles() throws StopRequestException
	{
		checkInterruption();
		ArrayList<AiTile> dangerousTiles = new ArrayList<AiTile>();
		
		for ( AiBomb currentBomb : getZone().getBombs() )
		{
			checkInterruption();
			dangerousTiles.add( currentBomb.getTile() );
			for ( AiTile currentTile : currentBomb.getBlast() )
			{
				checkInterruption();
				dangerousTiles.add( currentTile );
			}
		}
		for ( AiFire currentFire : getZone().getFires() )
		{
			checkInterruption();
			dangerousTiles.add( currentFire.getTile() );
		}
		for(AiItem currentItem : getZone().getItems() ){
			checkInterruption();
			if(currentItem.getType().equals(AiItemType.NO_BOMB) ||
					currentItem.getType().equals(AiItemType.NO_FLAME) ||
					currentItem.getType().equals(AiItemType.NO_SPEED) ||
					currentItem.getType().equals(AiItemType.ANTI_BOMB) ||
					currentItem.getType().equals(AiItemType.ANTI_FLAME) ||
					currentItem.getType().equals(AiItemType.ANTI_SPEED) ||
					currentItem.getType().equals(AiItemType.RANDOM_NONE)){
				dangerousTiles.add(currentItem.getTile());
			}
		}
		
		return dangerousTiles;
	}

	/**
	 * @return this.getDangerousTilesOnBombPut(ourtile, ourhero.getBombRange())
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		return this.getDangerousTilesOnBombPut(ourtile, ourhero.getBombRange());
	}

	/**
	 * @param givenTile
	 * @param range
	 * @return dangerousTilesOnBombPut
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getDangerousTilesOnBombPut( AiTile givenTile, int range ) throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		ArrayList<AiTile> dangerousTilesOnBombPut = new ArrayList<AiTile>();
		if ( givenTile.isCrossableBy(ourhero) && ( range > RANGE_BOTTOM_LIMIT ) )
		{
			dangerousTilesOnBombPut.add( givenTile );

			AiTile currentTile = givenTile.getNeighbor( Direction.LEFT );
			int distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.LEFT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.RIGHT );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.RIGHT );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.UP );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.UP );
				distance--;
			}

			currentTile = givenTile.getNeighbor( Direction.DOWN );
			distance = range;
			while ( currentTile.isCrossableBy(ourhero) && ( distance > DISTANCE_BOTTOM_LIMIT ) )
			{
				this.checkInterruption();
				dangerousTilesOnBombPut.add( currentTile );
				if ( !currentTile.getItems().isEmpty() ) break;
				currentTile = currentTile.getNeighbor( Direction.DOWN );
				distance--;
			}
		}

		return dangerousTilesOnBombPut;
	}

	/**
	 * @return
	 * @throws StopRequestException
	 */
	/**
	 * @return this.accessibleTiles
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getAccessibleTiles() throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		this.accessibleTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy(ourtile);

		return this.accessibleTiles;
	}
	
	/**
	 * @param sourceTile
	 * @return this.accessibleTiles
	 * @throws StopRequestException
	 */
	public ArrayList<AiTile> getAccessibleTiles( AiTile sourceTile ) throws StopRequestException
	{
		this.checkInterruption();
		this.accessibleTiles = new ArrayList<AiTile>();
		fillAccessibleTilesBy( sourceTile );

		return this.accessibleTiles;
	}
	/**
	 * @param givenHero
	 * @return ( safeTileCount > SAFETY_TILE_COUNT_LIMIT )
	 * @throws StopRequestException
	 */
	protected boolean canReachSafety( AiHero givenHero ) throws StopRequestException
	{
		this.checkInterruption();
		
		ArrayList<AiTile> accessibleTiles = this.getAccessibleTiles( givenHero.getTile() );
		int safeTileCount = accessibleTiles.size();
		
		for ( AiTile currentTile : accessibleTiles )
		{
			this.checkInterruption();
			if ( this.getDangerousTilesOnBombPut( givenHero.getTile(), givenHero.getBombRange() ).contains( currentTile ) || 

this.getCurrentDangerousTiles().contains( currentTile ) ) safeTileCount--;
		}
		return ( safeTileCount > SAFETY_TILE_COUNT_LIMIT );
	}
	/**
	 * 
	 */
	private ArrayList<AiTile> accessibleTiles;
	/**
	 * @param sourceTile
	 * @throws StopRequestException
	 */
	private void fillAccessibleTilesBy( AiTile sourceTile ) throws StopRequestException
	{
		this.checkInterruption();
		zone = getZone();
		ourhero = zone.getOwnHero();
		ourtile = ourhero.getTile();
		if ( sourceTile.isCrossableBy( ourhero) )
		{
			this.accessibleTiles.add( sourceTile );
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( 

sourceTile.getNeighbor( Direction.UP ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.UP ) );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( 

sourceTile.getNeighbor( Direction.DOWN ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.DOWN ) );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( 

sourceTile.getNeighbor( Direction.LEFT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.LEFT ) );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy(ourhero) && !this.accessibleTiles.contains( 

sourceTile.getNeighbor( Direction.RIGHT ) ) ) fillAccessibleTilesBy( sourceTile.getNeighbor( Direction.RIGHT ) );
		}
	}
	
	/**
	 * 
	 * @param aiTile
	 * @param direction
	 * @return ?
	 * @throws StopRequestException
	 */
	public boolean destructibleMurCheck (AiTile aiTile, Direction direction) throws StopRequestException{
		this.checkInterruption();
		boolean result = false;
		
		for(org.totalboumboum.ai.v201213.adapter.data.AiBlock currentBlock : aiTile.getNeighbor(direction).getBlocks() ){
			this.checkInterruption();
			if(currentBlock.isDestructible() && aiTile.getNeighbor(direction).getItems().isEmpty() ){
				result = true;
				return result;
			}
		}
		return result;
	}
	
}
