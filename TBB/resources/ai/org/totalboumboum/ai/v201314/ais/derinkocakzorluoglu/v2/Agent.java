package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
//import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiBomb;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe principale de l'agent. Cf. la
 * documentation de {@link ArtificialIntelligence} pour plus de détails.
 *
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
public class Agent extends ArtificialIntelligence
{
	/** Liste de tile accessible */
	public ArrayList<AiTile> acces;
	/** Temprorary liste pour des tiles accessibles
	 * On va utiliser ArrayList acces pour notre agent , et on va utiliser ArrayListe accesTemp quand 
	 * on fait des calculs sur des adversaires */
	public ArrayList<AiTile> accesTemp;
	/** Liste de tile murs destructibles qui sont accessibles */
	public ArrayList<AiBlock> accesWall;
	/**Temprory liste de murs destructibles qui sont accessibles. 
	On va utiliser ArrayList accesWall pour notre agent , et on va utiliser ArrayListe accesWallTemp quand 
	 * on fait des calculs sur des adversaires */
	public ArrayList<AiBlock> accesWallTemp;
	/** C'est un map dans laquelle on peut trouver le valeurs numerique associé aux dangers des tiles accessible.
	 * 0 quand il n'y a pas du danger dans cette tile
	 * 1 quand il y a un bombe dans cette tile 
	 * 2 quand il y a un bombe qui va exploiter dans un temps tres proche ou il y a un feu dans cette tile  */
	public Map<AiTile,Integer> dangerMap;
	
	/**C'est un liste de tous les adversaires dans cette episode */
	public List<AiHero> heros;
	
	/**C'est un Arraylist de tous les adversaires dans cette episode pour faire des calculs  */
	public ArrayList<AiHero> herolist;
	
	/** C'est un flag pour controler si l'adversaire est l'accessible ou pas . Si c'est faux il n'y a pas de l'ennemie accessible. */
	public Boolean control=false;
	/**
	 * On utilise Time pour les calculs lié a l'explosion du bombe.
	 */
	public long Time=10000;
	
	/** Quand l'ennemie est inaccessible c'est le case ou il y a un mur destructible qui est plus proche a l'ennemie pour que
	 * on va vers lui !!!Attention murtile n'est pas complet , on essaie encore de lui ameliorer.*/
	public AiTile murtile=null;
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	checkInterruption();
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
		
	}
	
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void initPercepts()
	{	checkInterruption();
	

	this.acces =new ArrayList <AiTile>();
	this.accesWall=new ArrayList<AiBlock>();
	this.accesTemp =new ArrayList <AiTile>();
	this.accesWallTemp=new ArrayList<AiBlock>();
	this.dangerMap=new HashMap<AiTile,Integer>();
	this.herolist=new ArrayList<AiHero>();
	this.heros=getZone().getRemainingOpponents();

		
	}
	
	@Override
	protected void updatePercepts()
	{	
		checkInterruption();
	
		// active/désactive la sortie texte
		verbose = false;
		modeHandler.verbose = false;
		preferenceHandler.verbose = false;
		bombHandler.verbose = false;
		moveHandler.verbose = false;		
		
		this.acces.clear();
		this.accesWall.clear();
		this.accesTemp.clear();
		this.accesWallTemp.clear();		
		fillAcces(getZone().getOwnHero().getTile(),getZone().getOwnHero());
		this.acces.addAll(this.accesTemp);
		this.accesWall.addAll(this.accesWallTemp);
		dangerMap();
		for(AiHero hero:this.heros){
			checkInterruption();
			if(hero.hasEnded()){
			control=false;
			}
		}
		if(!control){
			for(AiHero hero:heros){
				checkInterruption();
				if(!acces.contains(hero.getTile())){
					this.herolist.add(hero);
				}
				else control=true;
			}
		}
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

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput()
	{	checkInterruption();
	
		// vous pouvez changer la taille du texte affiché, si nécessaire
		// attention: il s'agit d'un coefficient multiplicateur
	//	AiOutput output = getOutput();
	//	output.setTextSize(2);
		
		// ici, par défaut on affiche :
			// les chemins et destinations courants
			moveHandler.updateOutput();
			// les preferences courantes
			preferenceHandler.updateOutput();
			
	
	}
	
	/**
	 * Recursive method to fill a list of accessible tiles.
	 * @param Hero
	 *  		  Pour determiner laquelle hero on va utiliser. (Nous ou l'adversaire)
	 * @param sourceTile
	 *            The tile to start looking from. If not crossable, list will
	 *            not be populated.
	 * @throws StopRequestException
	 *             If the engine demands the termination of the agent.
	 */
	public void fillAcces(AiTile sourceTile, AiHero Hero) throws StopRequestException {
		checkInterruption();
		if(!sourceTile.getBlocks().isEmpty()){
			for(AiBlock Wall:sourceTile.getBlocks()){
				checkInterruption();
				if(Wall.isDestructible())		
					this.accesWallTemp.add(Wall);
			}
		}
		
		if ( sourceTile.isCrossableBy(Hero ) )
		{
			this.accesTemp.add(sourceTile);
			if ( sourceTile.getNeighbor( Direction.UP ).isCrossableBy( Hero ) && !this.accesTemp.contains( sourceTile.getNeighbor( Direction.UP ) ) )
				fillAcces( sourceTile.getNeighbor( Direction.UP ),Hero );
			if ( sourceTile.getNeighbor( Direction.DOWN ).isCrossableBy( Hero ) && !this.accesTemp.contains( sourceTile.getNeighbor( Direction.DOWN ) ) )
				fillAcces( sourceTile.getNeighbor( Direction.DOWN ),Hero );
			if ( sourceTile.getNeighbor( Direction.LEFT ).isCrossableBy( Hero ) && !this.accesTemp.contains( sourceTile.getNeighbor( Direction.LEFT ) ) ) 
				fillAcces( sourceTile.getNeighbor( Direction.LEFT ),Hero );
			if ( sourceTile.getNeighbor( Direction.RIGHT ).isCrossableBy( Hero ) && !this.accesTemp.contains( sourceTile.getNeighbor( Direction.RIGHT ) ) )
				fillAcces( sourceTile.getNeighbor( Direction.RIGHT ),Hero );	
		}
	}
	/**Represant l'ennemi plus proche 
	 * @return il renvoie l'adversaire */

	public AiHero nearestEnemy(){
		checkInterruption();
	int min=50;
	AiHero nearestEnemy=null;
	for(AiHero hero:this.heros){
		checkInterruption();
		if(getZone().getTileDistance(getZone().getOwnHero().getTile(), hero.getTile())<min)
			{
			min=getZone().getTileDistance(getZone().getOwnHero().getTile(), hero.getTile());
			nearestEnemy=hero;
			}
	}
	
	return nearestEnemy;
}
	
	
	/** C'est un map dans laquelle on peut trouver le valeurs numerique associé aux dangers des tiles accessible.*/
	 
    public void dangerMap()
    {
    	checkInterruption();
    	this.dangerMap.clear();
    	for(AiTile tile:acces){
    		checkInterruption();
    		this.dangerMap.put(tile,dangerAllTile(tile)); 
    	}    	
    }
    
    /** @return 0 quand il n'y a pas du danger dans cette tile
	 * 1 quand il y a un bombe dans cette tile 
	 * 2 quand il y a un bombe qui va exploiter dans un temps tres proche ou il y a un feu dans cette tile  
     * @param Tile des tiles accessible
      */
   
    public int dangerAllTile(AiTile Tile)
    {
    	checkInterruption();
    	int result=0;
    	List<AiTile> Tiles = new ArrayList<AiTile>();
    	Map<AiBomb,Long> bombtime=getZone().getDelaysByBombs();		
		if(!Tile.getFires().isEmpty()){
			result=2;
		}		
		for(AiBomb Bomba:bombtime.keySet())
		{
			checkInterruption();
			Tiles.addAll(Bomba.getBlast());
			if(Tiles.contains(Tile)){
				if(bombtime.get(Bomba)<800){
					result=2;
					Time=bombtime.get(Bomba);
				}
				else if (Time>bombtime.get(Bomba))
					result=1;
			}
		}	
		Time=10000;
		return result;		
}
    
    
    /**
     * @param Tile Un tile acccessible 
     * @return Integer valeur (0,1,2)
     */
    public int getDangerValue(AiTile Tile)
    {
    	checkInterruption();
    	int a=this.dangerMap.get(Tile);
    	return a;
    }
    
    /** Icı on va calculer le case plus proche a l'ennemie inaccessible
     * !!!ATTENTION : Il n'est pas complet !!!
     * @return murtile
     */
    /*
    public AiTile BestWallMur()
    {  	
    	int distance=100;
    	
    	if(!this.control) //sil il n'y a pas un ennemie accessible 
    		{
    		if(!this.accesWall.isEmpty()){
    		for(AiBlock mur:this.accesWall){
    			if(distance>getZone().getTileDistance(mur.getTile(),nearestEnemy().getTile())){
    				distance=getZone().getTileDistance(mur.getTile(),nearestEnemy().getTile());
    				this.murtile=null;
    				this.murtile=mur.getTile();
    			}
    		}    			
    	}
    	}
    	return this.murtile;
    }
*/
	/**
	 * 
	 * @return 
	 * 		The murtile.
	 */
/*	public AiTile getMurtile() {
		return murtile;
	} */
}
