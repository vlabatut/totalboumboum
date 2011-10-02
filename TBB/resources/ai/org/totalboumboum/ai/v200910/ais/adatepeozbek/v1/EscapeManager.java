package org.totalboumboum.ai.v200910.ais.adatepeozbek.v1;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;
import org.totalboumboum.ai.v200910.adapter.path.astar.Astar;
import org.totalboumboum.ai.v200910.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v200910.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Trouve toutes les cases secures avec SafetyManager et calcule en utilisant l'algorithme A*
 * le chemin le plus court possible
 * 
 * @version 1
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
public class EscapeManager
{
	/*
	 * Initalise costMatrix, heuristicCalculator, astar et les cases secures dans possibleDest
	 */
	public EscapeManager(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		zone = ai.getZone();
		
		double costMatrix[][] = new double[zone.getHeight()][zone.getWidth()];
		costCalculator = new MatrixCostCalculator(costMatrix);
		heuristicCalculator = new BasicHeuristicCalculator();
		astar = new Astar(ai,ai.getOwnHero(),costCalculator,heuristicCalculator);
		
		possibleDest = ai.getSafetyManager().findSafeTiles();
		updatePath();
	}
	
	private AdatepeOzbek ai;
	
	private AiZone zone;	
	
	// La variable qui contient les cases secures
	private List<AiTile> possibleDest;

	// La route que va suivre notre caractère
	private AiPath path;
	
	/*
	 * Calcule le chemin le plus court
	 */
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		path = astar.processShortestPath(ai.getCurrentTile(),possibleDest);
	}
	
	/*
	 * Calcule si le chemin calculé est accessible
	 */
	private boolean checkPathValidity() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		boolean result = true;
		Iterator<AiTile> it = path.getTiles().iterator();
		while(it.hasNext() && result)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			AiTile tile = it.next();
			result = tile.isCrossableBy(ai.getOwnHero());			
		}
		return result;
	}

	private Astar astar;
	
	private HeuristicCalculator heuristicCalculator;
	
	private MatrixCostCalculator costCalculator;

	/*
	 * Prend les valeurs de la matrice et les passe à costCalculator
	 */
	private void updateCostCalculator() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		 
		double safetyMatrix[][] = ai.getSafetyManager().getMatrix();
		for(int line=0;line<zone.getHeight();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				double cost = -safetyMatrix[line][col];
				costCalculator.setCost(line,col,cost);
			}
		}
	}
	
	/*
	 * Retourne la direction à suivre calculée par les méthodes précedentes
	 */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		
		updateCostCalculator();
	
		Direction result = Direction.NONE;
		
		// La route peut être vide, c'est pourquoi on reContrôle
		if(path.isEmpty() || !checkPathValidity())
			updatePath();
		
		/* On prend toujours le premier élement de path array car avec notre algorithme
		 * On recalcule toujours la route à suivre dans chaque itération, alors notre
		 * caractère est capable de changer sa décision à chaque itération.
		 */
		
		AiTile tile = path.getTile(0);
		
		if(tile!=null)
			result = zone.getDirection(ai.getOwnHero(),tile);	
		
		return result;
	}

}
