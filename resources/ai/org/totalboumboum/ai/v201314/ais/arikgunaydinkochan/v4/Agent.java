package org.totalboumboum.ai.v201314.ais.arikgunaydinkochan.v4;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimBomb;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimHero;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 *
 * @author İsmail Arık
 * @author Tuğba Günaydın
 * @author Çağdaş Kochan
 */
@SuppressWarnings("deprecation")
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
	 * Cette est un flag , on utilise dans MoveHandler
	 * */
	boolean change=true;
	
	/**
	 * On utilise dans MoveHandler 
	 * Cette tile est controlé par "change"
	 * */
	AiTile tile = null;
	
	/**Avec la, on prende notre currentpath qui est en MoveHandler. 
	 * */
	AiPath currentPath =null;

	
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
	 * @return true , s'il ya un agent qui est accessible par notre agent  .
	 * Sinon return false 
	 */
	public boolean accessibleHero(){
		checkInterruption();
		boolean accessibleHero =false;
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(this.getZone().getRemainingOpponents());
	//	
		if(!heroes.isEmpty()){
			for(AiHero hero : heroes){
				checkInterruption();
				if(accessibleHero==false){
					AiTile heroTile =hero.getTile();
					if(accessibleTiles.contains(heroTile)){
								accessibleHero=true;
					}
				}
			}
			if(accessibleHero){
				return true;
			}
		}	
	//
		return false;
	}
	
	/**
	 * @return true , s'il ya un agent qui peut etre dangereous pour notre agent .
	 *  Sinon return false.
	 */
	public boolean dangereousHero(){
		checkInterruption();
		AiTile myTile = this.getZone().getOwnHero().getTile();
		
		ArrayList<AiHero> heroes = new ArrayList<AiHero>();
		heroes.addAll(this.getZone().getRemainingOpponents());
		
		boolean dangereousHero=false ;
		
		if(!heroes.isEmpty()){
			for(AiHero hero : heroes){
				checkInterruption();
				if(dangereousHero==false){
					AiTile heroTile =hero.getTile();
					if(accessibleTiles.contains(heroTile)){
						if(this.getZone().getTileDistance(myTile,heroTile)<=3)
							dangereousHero=true;
					}
				}	
			}
			if(dangereousHero)
				return true;
		} 
		
		return false;
	}
		
	/**
	 * s'il y a ,dans accessibleTile, des itemVisible qui sont meme type avec items donnants.
	 * @param itemType est AiItemType
	 * @return s'il y a un item visible true .sinon false
	 */
	public boolean itemVisible(AiItemType itemType){
		checkInterruption();
		AiZone zone = this.getZone();	
		AiTile myTile = zone.getOwnHero().getTile();
		for (AiItem item : zone.getItems()) {
			checkInterruption();
			if (item.getType().equals(itemType))
				if(item.getTile()==myTile)
					return true;
		}
		return false;
	}
	
	/**
	 * Dans le zone, on controle des items qui sont hiddents.
	 * @param itemType est AiItemType
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
	 * @return La liste of tiles qui peut etre dangereous
	 */
	public  ArrayList<AiTile> dangereousTiles() {
		checkInterruption();
		AiZone zone=this.getZone();
		ArrayList<AiTile> dangerousTile = new ArrayList<AiTile>();
		AiTile tile=null;
		
		//fires
		if(!zone.getFires().isEmpty()){
				for (AiFire fire : zone.getFires()) {
						checkInterruption();
						tile=fire.getTile();
						if(!dangerousTile.contains(tile))
								dangerousTile.add(tile);
				}
		}
		//blast
		if(!zone.getBombs().isEmpty()){
				for (AiBomb bomb : zone.getBombs()) {
						checkInterruption();
						tile =bomb.getTile();
						if(!dangerousTile.contains(tile))
								dangerousTile.add(tile);
						for (AiTile tileBomb : bomb.getBlast()) {
								checkInterruption();
								if(!dangerousTile.contains(tileBomb))
										dangerousTile.add(tileBomb);
						}
				}
		}
		/*
		//Malus
		if(!zone.getItems().isEmpty()){
				for (AiItem item: zone.getItems()) {
						checkInterruption();		
						if(!dangerousTiles.contains(item.getTile()))		
								if(isMalus(item))
										dangerousTiles.add(item.getTile());
				}
		}
		*/
		//heros contagious
		if(!zone.getRemainingOpponents().isEmpty()){
				for(AiHero hero:zone.getRemainingOpponents()){
						checkInterruption();
						if(hero.isContagious()){	
								tile=hero.getTile();
								if(!dangerousTile.contains(tile))
										dangerousTile.add(tile);
						}
				}	
		}
		return dangerousTile;
	}

	/**
	 * On controle des malus qui peut donner nous quelque desavantages.
	 * @param item is AiItem
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
	
	/**
	 * On fait une similation de bombe. On controle de trouver une case securite apres poser une bombe.
	 * @return true s'il ya un case securite. 
	 * Sinon la valeur retourne false.
	 */
	public boolean simulationSecuriteZone(){
		checkInterruption();
		ArrayList<AiTile> accessible = accessibleTiles;
		accessible.removeAll(dangereousTiles());
		
		AiSimZone simZone 	= new AiSimZone(getZone());
		AiSimHero simMyHero = simZone.getOwnHero();
		AiSimBomb simBomb 	= simZone.createBomb(simMyHero.getTile(), simMyHero);
		
		List<AiTile> simBlastTile = simBomb.getBlast();
		accessible.removeAll(simBlastTile);
		
		if(!accessible.isEmpty())
					return true;
		else
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




