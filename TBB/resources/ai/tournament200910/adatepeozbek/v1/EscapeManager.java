package tournament200910.adatepeozbek.v1;

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

/*
 * Trouve toutes les cases secures avec SafetyManager et calcule en utilisant l'algorithme A*
 * le chemin le plus court possible
 */
public class EscapeManager
{
	/*
	 * Initalise costMatrix, heuristicCalculator, astar et les cases secures dans possibleDest
	 */
	public EscapeManager(AdatepeOzbek ai) throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		this.ai = ai;
		zone = ai.getZone();
		
		double costMatrix[][] = new double[zone.getHeigh()][zone.getWidth()];
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

	// La route que va suivre notre caract�re
	private AiPath path;
	
	/*
	 * Calcule le chemin le plus court
	 */
	private void updatePath() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		path = astar.processShortestPath(ai.getCurrentTile(),possibleDest);
	}
	
	/*
	 * Calcule si le chemin calcul� est accessible
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
	 * Prend les valeurs de la matrice et les passe � costCalculator
	 */
	private void updateCostCalculator() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		 
		double safetyMatrix[][] = ai.getSafetyManager().getMatrix();
		for(int line=0;line<zone.getHeigh();line++)
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			for(int col=0;col<zone.getWidth();col++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				double cost = -safetyMatrix[line][col];
				costCalculator.setCost(line,col,cost);
			}
		}
	}
	
	/*
	 * Retourne la direction � suivre calcul�e par les m�thodes pr�cedentes
	 */
	public Direction update() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
		
		updateCostCalculator();
	
		Direction result = Direction.NONE;
		
		// La route peut �tre vide, c'est pourquoi on recontr�le
		if(path.isEmpty() || !checkPathValidity())
			updatePath();
		
		/* On prend toujours le premier �lement de path array car avec notre algorithme
		 * On recalcule toujours la route � suivre dans chaque it�ration, alors notre
		 * caract�re est capable de changer sa d�cision � chaque it�ration.
		 */
		
		AiTile tile = path.getTile(0);
		
		if(tile!=null)
			result = zone.getDirection(ai.getOwnHero(),tile);	
		
		return result;
	}

}
