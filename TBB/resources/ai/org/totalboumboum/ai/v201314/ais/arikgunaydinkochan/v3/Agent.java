package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v3;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiFire;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiItem;
import org.totalboumboum.ai.v201314.adapter.data.AiItemType;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;



/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 *
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
public class Agent extends ArtificialIntelligence
{
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	checkInterruption();
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	
		
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts()
	{	checkInterruption();
		
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// HANDLERS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Gestionnaire chargé de calculer le mode de l'agent */
	protected ModeHandler modeHandler;
	/** Gestionnaire chargé de calculer les valeurs de préférence de l'agent */
	protected PreferenceHandler preferenceHandler;
	/** Gestionnaire chargé de décider si l'agent doit poser une bombe ou pas */
	protected BombHandler bombHandler;
	/** Gestionnaire chargé de décidé de la direction de déplacement de l'agent */
	protected MoveHandler moveHandler;
	
	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}

	@Override
	protected AiModeHandler<Agent> getModeHandler()
	{	checkInterruption();
		return modeHandler;
	}

	@Override
	protected AiPreferenceHandler<Agent> getPreferenceHandler()
	{	checkInterruption();
		return preferenceHandler;
	}

	@Override
	protected AiBombHandler<Agent> getBombHandler()
	{	checkInterruption();
		return bombHandler;
	}

	@Override
	protected AiMoveHandler<Agent> getMoveHandler()
	{	checkInterruption();
		return moveHandler;
	}

	// Notre code commence
	
	 /** 
	 * S'il n ' y a pas un adversaire , on utilise cette variable
	 * */
	boolean change2=true;
	
	/** S'il y a au moins un adversire , on utilise cette variable
	 * */
	boolean change1=true;
	
	/** 
	 * S'il y a au moins un adversire , on utilise cette variable
	 * */
	AiTile tile1 = null;
	
	/** 
	 *  S'il n ' y a pas un adversaire , on utilise cette variable
	 * */
	AiTile tile2 = null;
	

	
	/** La liste de tile accessible
	 */
	public ArrayList<AiTile> accessibleTiles;
	 
	/**
	 * List of tiles is accessible by ownHero. 
	 */
	public void accessibleTile(){
		checkInterruption();
		AiZone zone = this.getZone();
		AiHero myHero = zone.getOwnHero();
		AiTile mytile = myHero.getTile();

		accessibleTiles = new ArrayList<AiTile>();
		
		Queue<AiTile> tiles=new LinkedList<AiTile>();
		tiles.add(mytile);
		accessibleTiles.add(mytile);
		while(tiles.size()>0)
		{
			checkInterruption();
			AiTile t=tiles.poll();
					
			for(AiTile neighbours: t.getNeighbors()){
		 		checkInterruption();
		 		AiTile current = neighbours;			 	
		 		if(current.isCrossableBy(myHero)){
		 			if(!accessibleTiles.contains(current)){
		 					accessibleTiles.add(current);
		 					tiles.add(current);
		 			}
		 		}
		 	}
		}
	}
	/**
	 * @param itemType is type of item
	 * @return s'il y a un item visible true .sinon false
	 */
	public boolean itemVisible(AiItemType itemType){
		checkInterruption();
		AiZone zone = this.getZone();	
		
		for (AiItem item : zone.getItems()) {
			checkInterruption();
			if (item.getType().equals(itemType))
				return true;
		}
		return false;
	}
	
	/**
	 * @param itemType is type of item
	 * @return true s'il y a un item hidden , sinon false
	 */
	public boolean itemHidden(AiItemType itemType){
		checkInterruption();
		AiZone zone = this.getZone();
		Integer result = zone.getHiddenItemsCount(itemType);
		if(result>0){	
			return true;
		}
		return false;
	}
	
	/**
	 * @return List of tile est dangereous
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
	 * @param item is item dans la tile
	 * @return true s'item est malus , sinon false
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
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
		AiOutput output = getOutput();
		output.setTextSize(2);

		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
	
		// cf. la Javadoc dans ArtificialIntelligence pour une description de la méthode
	}	
}




