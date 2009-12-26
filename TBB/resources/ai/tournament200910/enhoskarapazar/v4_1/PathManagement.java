package tournament200910.enhoskarapazar.v4_1;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.ai.adapter200910.communication.AiOutput;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.BasicCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.CostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;

public class PathManagement {

	/** interrupteur permettant d'afficher la trace du traitement */
	private boolean verbose = false;
	
	/**
	 * cr�e un PathManager charg� d'amener le personnage � la position (x,y)
	 * exprim�e en pixels
	 */
	public PathManagement(EnhosKarapazar ai, double x, double y) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		init(ai);
		setDestination(x,y);
	}
	
	/**
	 * cr�e un PathManager charg� d'amener le personnage au centre de la case
	 * pass�e en param�tre
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
		astar = new Astar(ai.getOwnHero(),costCalculator,heuristicCalculator);
		//updatePrev();
	}
	
	/////////////////////////////////////////////////////////////////
	// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** l'IA concern�e par ce gestionnaire de chemin */
	private EnhosKarapazar ai;
	/** zone de jeu */
	private AiZone zone;
	
	/////////////////////////////////////////////////////////////////
	// DESTINATION	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si le personnage est arriv� � destination */
	private boolean arrived;
	/** la case de destination s�lectionn�e */
	private AiTile tileDest;
	/** l'abscisse de destination */
	private double xDest;
	/** l'ordonn�e de destination */
	private double yDest;
	
	/**
	 * modifie la case de destination du personnage,
	 * place les coordonn�es de destination au centre de cette case,
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
	 * modifie les coordonn�es de destination,
	 * met � jour automatiquement la case correspondante,
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
	 * d�termine si le personnage est arriv� aux coordonn�es de destination
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			AiHero ownHero = ai.getOwnHero();
			double xCurrent = ownHero.getPosX();
			double yCurrent = ownHero.getPosY();
			arrived = zone.hasSamePixelPosition(xCurrent,yCurrent,xDest,yDest);
			// cas particulier : oscillation autour du point d'arriv�e
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
	/** abscisse pr�c�dente */
	private double xPrev;
	/** ordonn�e pr�c�dente */
	private double yPrev;	
	
	/**
	 * met � jour la position pr�c�dente du personnage,
	 * exprim�e en pixels
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
	/** le chemin � suivre */
	private AiPath path;
	
	/**
	 * v�rifie que le personnage est bien sur le chemin pr�-calcul�,
	 * en supprimant si besoin les cases inutiles.
	 * Si le personnage n'est plus sur le chemin, alors le chemin
	 * est vide apr�s l'ex�cution de cette m�thode.
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
	 * est toujours s�r et si aucun obstacle n'est apparu
	 * depuis la derni�re it�ration
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
	/** classe impl�mentant l'algorithme A* */
	private Astar astar;
	/** classe impl�mentant la fonction heuristique */
	private HeuristicCalculator heuristicCalculator;
	/** classe impl�mentant la fonction de co�t */
	private CostCalculator costCalculator;

	/////////////////////////////////////////////////////////////////
	// PROCESS					/////////////////////////////////////
	/////////////////////////////////////////////////////////////////	
	/** 
	 * calcule la prochaine direction pour aller vers la destination 
	 *(ou renvoie Direction.NONE si aucun d�placement n'est n�cessaire)
	 * */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		Direction result = Direction.NONE;
		if(!hasArrived())
		{	// on v�rifie que le joueur est toujours sur le chemin
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
		
		// mise � jour de la position pr�c�dente
		updatePrev();
		// mise � jour de la sortie
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
	 * met � jour la sortie graphique de l'IA en fonction du
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
	public  ArrayList<AiTile> getPathList()
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
