package org.totalboumboum.ai.v200910.ais.enhoskarapazar.v5;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;

public class PathManagement {

	
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
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
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
		double normalized[] = zone.normalizePosition(x, y);
		xDest = normalized[0];
		yDest = normalized[1];
		tileDest = zone.getTile(xDest,yDest);
		path = astar.processShortestPath(ai.getCurrentTile(),tileDest);
		if(path.getLength()>0)	
		path.removeTile(0);
	}


	/////////////////////////////////////////////////////////////////
	// PATH			/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** le chemin � suivre */
	private AiPath path;
	
	
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