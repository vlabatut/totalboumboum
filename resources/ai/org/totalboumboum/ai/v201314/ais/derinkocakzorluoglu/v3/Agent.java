package org.totalboumboum.ai.v201314.ais.derinkocakzorluoglu.v3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.AiBombHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiModeHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201314.adapter.agent.AiPreferenceHandler;
import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiBlock;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;

/**
 * Classe principale de votre agent, que vous devez compléter.
 * Cf. la documentation de {@link ArtificialIntelligence} pour plus de détails.
 *
 * @author Emre Derin
 * @author Oktay Koçak
 * @author Emin Can Zorluoğlu
 */
@SuppressWarnings("deprecation")
public class Agent extends ArtificialIntelligence
{
	/** Liste de tile accessible */
	public ArrayList<AiTile> access;
	
	/** C'est un map dans laquelle on peut trouver le valeurs numerique associé aux dangers des tiles accessible.
	 * 0 quand il n'y a pas du danger dans cette tile.
	 * 1 quand il y a un bombe qui menace cette tile.
	 * 2 quand il y a un bombe qui va exploiter dans un temps tres proche ou il y a un feu dans cette tile  */
	public Map<AiTile,Integer> dangerMap;	
	/**
	 * Une liste qui va retourner les BestWalls (voir critere BestWall)
	 */
	public ArrayList<AiTile> bestWallTile;
	/** Liste de tile murs destructibles qui sont a cote des cases accessibles. */
	public ArrayList<AiBlock> accessWall;
	/** C'est un flag pour controler si l'adversaire est l'accessible ou pas .
	 *  Si c'est faux il n'y a pas de l'ennemie accessible. */
	public boolean enemyAccessible;	
	/**
	 * On utilise Time pour les calculs lié a l'explosion du bombe.
	 */
	public long Time=10000;
	/** On cree un objet zone qui appartient a la classe Aizone */
	public AiZone zone;
	/**On cree un objet  myHero qui appartient a la classe AiHero */
	public AiHero myHero;
	/***On cree un objet objectAcces qui appartient a la classe AccessibleTilesFinder*/
	public AccessibleTilesFinder objectAcces=new AccessibleTilesFinder(this);
	/**On cree un objet objectCalcul qui appartient a la classe CalculationHelper */
	public CalculationHelper objectCalcul=new CalculationHelper(this);
	/**Represant l'ennemi plus proche 
	 * null s'in n'existe pas dans le case accessible */
	public AiHero nearestEnemy;
	/**
	 * Instancie la classe principale de l'agent.
	 */
	public Agent()
	{	checkInterruption();
	}
	
	@Override
	protected void initOthers()
	{	checkInterruption();
		
		this.access =new ArrayList <AiTile>();
		this.dangerMap=new HashMap<AiTile,Integer>();
		this.accessWall=new ArrayList<AiBlock>();
		this.zone=getZone();
		this.myHero=zone.getOwnHero();
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
		
		this.access.clear();
		this.accessWall.clear();		
		objectAcces.fillAccess(this.myHero.getTile(), this.myHero);
		objectAcces.fillAccessWall();
		objectCalcul.dangerMap();
		this.enemyAccessible=objectAcces.isEnemiesAccessible();
		this.nearestEnemy=objectCalcul.getNearestEnemy();
		if(!this.enemyAccessible&& zone.getRemainingOpponents().size()>0)
			this.bestWallTile=objectCalcul.nearAccesWall();
		for(AiHero hero:zone.getRemainingOpponents()){
			checkInterruption();
			if(hero.hasEnded()){
				this.enemyAccessible=objectAcces.isEnemiesAccessible();
			}
		}
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
	/**Gestionnaire chargé de décidé quelles sont les Tiles Accessibles  */
	protected AccessibleTilesFinder accessibleTilesFinder;
	/**Gestionnaire chargé pour aider aux Calculations  */
	protected CalculationHelper calculationHelper;
	
	@Override
	protected void initHandlers()
	{	checkInterruption();
		
		// création des gestionnaires standard (obligatoires)
		modeHandler = new ModeHandler(this);
		preferenceHandler = new PreferenceHandler(this);
		bombHandler = new BombHandler(this);
		moveHandler = new MoveHandler(this);
		accessibleTilesFinder=new AccessibleTilesFinder(this);
		calculationHelper=new CalculationHelper(this);

		
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
