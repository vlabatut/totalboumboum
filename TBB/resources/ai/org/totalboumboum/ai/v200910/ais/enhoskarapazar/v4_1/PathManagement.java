package org.totalboumboum.ai.v200910.ais.enhoskarapazar.v4_1;

import java.awt.Color;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.AiOutput;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Sadettin Enhoş
 * @author Ali Can Karapazar
 */
public class PathManagement {

	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;
	
	/**
	 * crée un PathManager chargé d'amener le personnage à la position (x,y)
	 * exprimée en pixels
	 */
	public PathManagement(EnhosKarapazar ai, double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(x,y);
	}
	
	/**
	 * crée un PathManager chargé d'amener le personnage au centre de la case
	 * passée en paramètre
	 */
	public PathManagement(EnhosKarapazar ai, AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(destination);
	}
	
	/**
	 * initialise ce PathManager
	 */
	private void init(EnhosKarapazar ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		this.ai = ai;
		zone = ai.getZone();
		costCalculator = new BasicCostCalculator();
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		//updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concernée par ce gestionnaire de chemin */
	private EnhosKarapazar ai;
	/** zone de jeu */
	private AiZone zone;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arrivé à destination */
	private boolean arrived;
	/** la case de destination sélectionnée */
	private AiTile tileDest;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonnée de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonnées de destination au centre de cette case,
	 * et recalcule le chemin.
	 */
	public void setDestination(AiTile destination) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		arrived = false;
		tileDest = destination;
		xDest = tileDest.getPosX();
		yDest = tileDest.getPosY();
		path = astar.processShortestPath(ai.getCurrentTile(),destination);
		if(path.getLength()>0)
			path.removeTile(0);
	}

	/**
	 * modifie les coordonnées de destination,
	 * met à jour automatiquement la case correspondante,
	 * et recalcule le chemin.
	 */
	public void setDestination(double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		arrived = false;
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
		tileDest = zone.getTile(xDest,yDest);
		path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
		if(path.getLength()>0)	
		path.removeTile(0);
	}

	/**
	 * détermine si le personnage est arrivé aux coordonnées de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			AiHero ownHero = ai.getOwnHero();
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arrivée
			if(!arrived && path.getLength()==1)
			{	Direction prevDir = zone.getDirection(xPrev,yPrev,xDest,yDest);
				Direction currentDir = zone.getDirection(xCurrent,yCurrent,xDest,yDest);
				arrived = prevDir.getOpposite()==currentDir;
			}
		
		return arrived;
	}

	/////////////////////////////////////////////////////////////////
	// PREVIOUS LOCATION	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** abscisse précédente */
	private double xPrev;
	/** ordonnée précédente */
	private double yPrev;	
	
	/**
	 * met à jour la position précédente du personnage,
	 * exprimée en pixels
	 */
	private void updatePrev() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiHero hero = ai.getOwnHero();
		xPrev = hero.getPosX();
		yPrev = hero.getPosY();		
	}

	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin à suivre */
	private AiPath path;
	
	/**
	 * vérifie que le personnage est bien sur le chemin pré-calculé,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide après l'exécution de cette méthode.
	 */
	private void checkIsOnPath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		AiTile currentTile = ai.getCurrentTile();
		while(!path.isEmpty() && path.getTile(0)!=currentTile)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path.removeTile(0);
		}
	}
	
	
	/** 
	 * teste si le chemin est toujours valide, i.e. s'il
	 * est toujours sûr et si aucun obstacle n'est apparu
	 * depuis la dernière itération
	 */
	public boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile tile = it.next();
			if (tile.isCrossableBy(ai.getOwnHero()) && estCaseSure(tile.getLine(), tile.getCol())) 
					result=true;
			else
				result=false;
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// A STAR					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** classe implémentant l'algorithme A* */
	private Astar astar;
	/** classe implémentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe implémentant la fonction de coût */
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochaine direction pour aller vers la destination 
	 *(ou renvoie Direction.NONE si aucun déplacement n'est nécessaire)
	 * */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on vérifie que le joueur est toujours sur le chemin
			checkIsOnPath();
			// si le chemin est vide ou invalide, on le recalcule
			if(path.isEmpty() || !checkPathValidity())
				path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
			if(checkPathValidity())
			{	// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
				if(path.getLength()>1)
				{	AiTile tile = path.getTile(1);
					result = zone.getDirection(ai.getOwnHero(),tile);	
				}
				// sinon, s'il ne reste qu'une seule case, on va au centre
				else if(path.getLength()>0)
				{	AiHero ownHero = ai.getOwnHero();
					double x1 = ownHero.getPosX();
					double y1 = ownHero.getPosY();
					result = zone.getDirection(x1,y1,xDest,yDest);
				}
			}
		}
		
		// mise à jour de la position précédente
		updatePrev();
		// mise à jour de la sortie
		updateOutput();
		
		if(verbose)
		{	System.out.println(">>>>>>>>>> PATH MANAGER <<<<<<<<<<");
			System.out.println("path: "+path);
			System.out.println("direction: "+result);
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * met à jour la sortie graphique de l'IA en fonction du
	 * chemin courant
	 */
	private void updateOutput() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(path!=null && !path.isEmpty())	
		{	AiOutput output = ai.getOutput();
			Color color = Color.BLACK;
			output.addPath(path, color);
		}
	}
	
	
	public int getLength() throws StopRequestException
	{	
		return path.getLength();
	}
	public  List<AiTile> getPathList()
	{
		
		return path.getTiles();
	}
	
	public boolean isWalkable() throws StopRequestException
	{ 
		if(path.getLength()>0)
			return true;
		else
			return false;
	}
	public void printPath()
	{
		System.out.println(path.getTiles().toString());
	}
	
	private boolean estCaseSure(int line, int col) throws StopRequestException
	{
		
		ai.checkInterruption();
		boolean tempresult =true;
		DangerZone dZone=new DangerZone(zone, ai);
		if( dZone.getEnum(line, col)!=ZoneEnum.FEU && dZone.getEnum(line, col)!=ZoneEnum.FLAMMES && dZone.getEnum(line, col)!=ZoneEnum.BOMBE)
			tempresult=false;
		dZone=null;
		return tempresult;
	}
	
	
}
