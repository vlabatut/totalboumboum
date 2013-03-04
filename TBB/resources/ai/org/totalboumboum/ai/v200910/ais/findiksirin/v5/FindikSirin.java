package org.totalboumboum.ai.v200910.ais.findiksirin.v5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiBomb;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * @author Ali Fındık
 * @author Göknur Şırın
 */
@SuppressWarnings("deprecation")
public class FindikSirin extends ArtificialIntelligence
{		
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de notre IA */
	@Override
	public AiAction processAction() throws StopRequestException
	{	
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		
		
		if(ownHero == null)
			init();
		// si le personnage n'est pas elimine
		if(!ownHero.hasEnded())
		{	// on met a jour la position de l'ia dans la zone
			updateLocation();
			setOurBombs();
			Direction moveDir = Direction.NONE;
//			AiTile destinationTile=currentTile.getNeighbor(Direction.LEFT);
//			System.out.println(zone.getPixelDistance(currentTile.getPosX(), currentTile.getPosY(), destinationTile.getPosX(), destinationTile.getPosY()));
//			if(!ourBombs.isEmpty()){
//				System.out.println(ourBombs.iterator().next().getBombs().iterator().next().getExplosionDuration());
//			}
			// on met a jour le safetyManager
			safetyManager.update();
			
			if(dropBomb){
				dropBomb=false;		
			}
			
			//t'as un chemin pour fuir?
			if(escapeManager!=null)
			{
				//t'es arrive à la fin de chemin de fuir?
				if(escapeManager.hasArrived())
				{
					//initialiser le chemin de fuir
					escapeManager=null;
				}
				//tu n'es pas arrive à la fin de chemin de fuir?
				else
				{
					//continuer sur le chemin de fuir
					moveDir = escapeManager.update();
				}
			}
			
			else if(safetyManager.hasNoWhereToGo(currentTile) && isSafe(currentTile))
			{
				escapeManager = new EscapeManager(this);
				moveDir=Direction.NONE;
			}
			
			//t'as pas de chemin de fuir et t'es en danger?
			else if(!safetyManager.isSafe(currentTile))
			{
				attackManager=null;
				bonusManager=null;
				escapeManager = new EscapeManager(this);
				moveDir = escapeManager.update();
			}
			
			//t'as pas de chemin de fuir et tu n'es pas en danger?
			else
			{
				//t'as un chemin d'attaque?
				if(attackManager!=null)
				{
					//t'es arrive au chemin d'attaque?
					if(attackManager.hasArrived())
					{
						dropBomb=true;
						attackManager=null;
						posTarget=null;
						posWalkable=null;
						
					}
					//tu n'es pas arrive à la fin de chemin d'attaque?
					else
					{
						if(attackManager.bomb2Pos==currentTile){
							dropBomb=true;
						}
						else{
							moveDir= attackManager.update();	
						}
					}
				}
				//t'as pas de chemin d'attaque?
				else
				{
					//t'as assez bombes?
					if((ownHero.getBombNumber()>2 || noWall) && hasTargetAttackable() && ourBombs.size()<4 )
					{
						//trouver un autre chemin d'attaque
						attackManager = new AttackManager(this,posTarget,posWalkable);
						dropBomb=true;
						attackManager.update();
//						System.out.println(attackManager.bomb1Pos.toString());
//						System.out.println(attackManager.bomb2Pos.toString());
//						System.out.println(attackManager.bomb3Pos.toString());
					}
					//t'as pas d'assez bombes?
					else
					{
						//t'as un chemin de bonus?
						if(bonusManager!=null)
						{							
							//t'es arrive au chemin de bonus?
							if(bonusManager.hasArrived())
							{
								if(bonusManager.isOnBonusDestruction() && ourBombs.isEmpty() && !bonusManager.isBonusOver()){
									dropBomb=true;
									//bombPos=currentTile;
									bonusManager=null;
								}
								else if(bonusManager.isBonusOver()){
								noWall=true;	
								}
								else
								bonusManager=null;
							}
							//tu n'es pas arrive au chemin de bonus?
							else
							{
								if(bonusManager.isPathSecure()){
								moveDir = bonusManager.update();
								}
								else
								{
									bonusManager=null;
								}
							}
						}
						//t'as aucun chemin de bonus?
						else
						{
							if(ourBombs.isEmpty()){
								bonusManager = new BonusManager(this);
								moveDir = bonusManager.update();
							}
						}
					}
				}
			}
		
			if (dropBomb){
				if(areYouSure()){
				result=new AiAction(AiActionName.DROP_BOMB);
				}
				else{
					result=new AiAction(AiActionName.NONE);
					dropBomb=false;
				}
				
			}
			else
				result=new AiAction(AiActionName.MOVE,moveDir);
		}
		return result;
	}
	

	/** initialisation
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void init() throws StopRequestException
	{	checkInterruption();

		zone = getPercepts();
		ownHero = zone.getOwnHero();
		updateLocation();
		safetyManager = new SafetyManager(this);
		//setOurBombs();
	}

	
	/** notre personnage et son acces */
	private AiHero ownHero = null;
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiHero getOwnHero() throws StopRequestException
	{	checkInterruption();
		return ownHero;
	}

	/** la zone de jeu */
	private AiZone zone = null;
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiZone getZone() throws StopRequestException
	{	checkInterruption();	
		return zone;
	}
	
	/** LES DEFINITIONS */ 
	private EscapeManager escapeManager = null;
	/** */
	private SafetyManager safetyManager = null;
	/** */
	private BonusManager bonusManager = null;
	/** */
	private AttackManager attackManager = null;
	
	/** */
	private boolean dropBomb=false;
	/** */
	private List<AiTile> ourBombs=new ArrayList<AiTile>();
	//private AiTile bombPos=null;
	/** */
	private boolean noWall=false;
	
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean areYouSure() throws StopRequestException{
		checkInterruption();
		List <AiTile> safeTiles = safetyManager.findSafeTiles(currentTile);
		Astar astar;
		HeuristicCalculator heuristicCalculator;
		MatrixCostCalculator costCalculator;
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(this,ownHero,costCalculator,heuristicCalculator);
		AiPath path=astar.processShortestPath(currentTile, safeTiles);
		AiTile safest= path.getLastTile();
		return safetyManager.isReachable(safest);
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public void setOurBombs() throws StopRequestException{
		checkInterruption();
		AiTile tile=null;
		for(int line=0;line<zone.getHeight();line++)
		{	checkInterruption();
			for(int col=0;col<zone.getWidth();col++)
			{	checkInterruption();
				tile = zone.getTile(line,col);
				if(!tile.getBombs().isEmpty() && !ourBombs.contains(tile) && tile.getBombs().iterator().next().getColor()==PredefinedColor.WHITE)
				{	
					ourBombs.add(tile);
				}
				else
				{
					ourBombs.remove(tile);
				}
			}
		}
	}
	/*
	private boolean isOurBonusBombExplosed() throws StopRequestException{
		checkInterruption();
		boolean resultat=true;
		if(bombPos!=null){
			List <AiBomb> b= bombPos.getBombs();
			if(b.isEmpty())
			{
				bombPos=null;
				resultat=true;
			}
			else
			{
				resultat=false;
			}
		}
		return resultat;
	}
	*/
	/** */
	public Direction posTarget=null;
	/** */
	public Direction posWalkable=null;
	
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private boolean hasTargetAttackable() throws StopRequestException{
		checkInterruption();
		if(!currentTile.getNeighbor(Direction.LEFT).getNeighbor(Direction.LEFT).getHeroes().isEmpty() &&
				currentTile.getNeighbor(Direction.LEFT).isCrossableBy(ownHero) &&
				safetyManager.isSafe(currentTile.getNeighbor(Direction.LEFT))
				){
			posTarget=Direction.LEFT;
			if(currentTile.getNeighbor(Direction.UP).isCrossableBy(ownHero))
				posWalkable=Direction.UP;
			else if(currentTile.getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				posWalkable=Direction.DOWN;
			else posWalkable=null;
		}
		if(!currentTile.getNeighbor(Direction.RIGHT).getNeighbor(Direction.RIGHT).getHeroes().isEmpty() &&
				currentTile.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero) &&
				safetyManager.isSafe(currentTile.getNeighbor(Direction.RIGHT))
				){
			posTarget=Direction.RIGHT;
			if(currentTile.getNeighbor(Direction.UP).isCrossableBy(ownHero))
				posWalkable=Direction.UP;
			else if(currentTile.getNeighbor(Direction.DOWN).isCrossableBy(ownHero))
				posWalkable=Direction.DOWN;
			else posWalkable=null;
		}
		if(!currentTile.getNeighbor(Direction.DOWN).getNeighbor(Direction.DOWN).getHeroes().isEmpty() &&
				currentTile.getNeighbor(Direction.DOWN).isCrossableBy(ownHero) &&
				safetyManager.isSafe(currentTile.getNeighbor(Direction.DOWN))
				){
			posTarget=Direction.DOWN;
			if(currentTile.getNeighbor(Direction.LEFT).isCrossableBy(ownHero))
				posWalkable=Direction.LEFT;
			else if(currentTile.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero))
				posWalkable=Direction.RIGHT;
			else posWalkable=null;
		}
		if(!currentTile.getNeighbor(Direction.UP).getNeighbor(Direction.UP).getHeroes().isEmpty() &&
				currentTile.getNeighbor(Direction.UP).isCrossableBy(ownHero) &&
				safetyManager.isSafe(currentTile.getNeighbor(Direction.UP))
				){
			posTarget=Direction.UP;
			if(currentTile.getNeighbor(Direction.LEFT).isCrossableBy(ownHero))
				posWalkable=Direction.LEFT;
			else if(currentTile.getNeighbor(Direction.RIGHT).isCrossableBy(ownHero))
				posWalkable=Direction.RIGHT;
			else posWalkable=null;
		}
		return ( (posTarget!=null) && (posWalkable!=null) );	
	}

	
//LES METHODES D'ACCES DES GESTIONNAIRES DE TRAITEMENT
	/**
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public SafetyManager getSafetyManager() throws StopRequestException
	{	checkInterruption();	
		return safetyManager;		
	}
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public EscapeManager getEscapeManager() throws StopRequestException
	{	checkInterruption();	
		return escapeManager;		
	}
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public BonusManager getBonusManager() throws StopRequestException
	{	checkInterruption();	
		return bonusManager;		
	}
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AttackManager getAttackManager() throws StopRequestException
	{	checkInterruption();	
		return attackManager;		
	}

//LES METHODES DE CONTROLE DE SECURITE
	/**
	 * @param tile 
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException 
	 * 		Description manquante !
	 */
	public double getSafetyLevel(AiTile tile) throws StopRequestException
	{	checkInterruption();	
		return safetyManager.getSafetyLevel(tile);		
	}	
	/**
	 * 
	 * @param tile
	 * 		Description manquante !
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public boolean isSafe(AiTile tile) throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
			
		return safetyManager.isSafe(tile);
	}
	
	
	/** CASE ET POSITION OCCUPE (current tile and position) */
	private AiTile currentTile = null;
	/** */
	private double currentX;
	/** */
	private double currentY;
	
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public AiTile getCurrentTile() throws StopRequestException
	{	checkInterruption();
		return currentTile;
	}	

	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double getCurrentX() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
	
		return currentX;
	}
	/**
	 * 
	 * @return 
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	public double getCurrentY() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE	
		return currentY;
	}

	/**
	 * 
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	private void updateLocation() throws StopRequestException
	{	checkInterruption();
		currentTile = ownHero.getTile();
		currentX = ownHero.getPosX();
		currentY = ownHero.getPosY();				
	}
	
	/**
	 * 
	 * @param currentTile
	 * 		Description manquante !
	 * @return ?
	 * 		Description manquante !
	 * @throws StopRequestException
	 * 		Description manquante !
	 */
	@SuppressWarnings("unused")
	private AiBomb getOurBomb(AiTile currentTile) throws StopRequestException{
		checkInterruption();
		AiBomb tempBomb=null;
		AiBomb resultat=null;
		List <AiBomb> bombs= zone.getBombs();
		Iterator <AiBomb> i = bombs.iterator();
		while(i.hasNext()){
			checkInterruption();
			tempBomb=i.next();
			if(tempBomb.getTile()==currentTile )
			{
				resultat=tempBomb;
			}
		}
		return resultat;
	}
	
}
