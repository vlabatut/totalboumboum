package org.totalboumboum.ai.v201011.ais.avcigungor.v5;

import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.LimitReachedException;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.MatrixCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.engine.content.feature.Direction;


public class WallController {

		
		private AiHero ourHero;
		public WallController(AvciGungor ai) throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			this.ai=ai;
			AiZone gameZone = ai.getPercepts();
			
			
			// init A*
			double costMatrix[][] = new double[gameZone.getHeight()][gameZone.getWidth()];
			costCalculator = new MatrixCostCalculator(costMatrix);
			heuristicCalculator = new BasicHeuristicCalculator();
			astar = new Astar(ai,ourHero,costCalculator,heuristicCalculator);
			
			// init destinations
			arrived = false;
			ourHero = gameZone.getOwnHero();
			possibleDest = ai.collectC.findWallTiles(ourHero.getTile());
			updatePath();
		}
		
		/////////////////////////////////////////////////////////////////
		// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/** l'IA concernée par ce gestionnaire de chemin */
		private AvciGungor ai;
		/** gameZone de jeu */
		private AiZone gameZone;	
		
		/////////////////////////////////////////////////////////////////
		// DESTINATION	/////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/** indique si le personnage est arrivé à destination */
		private boolean arrived;
		/** la case de destination sélectionn�e pour la fuite */
		private AiTile tileDest;
		/** destinations potentielles */
		private List<AiTile> possibleDest;
		
		/**
		 * détermine si le personnage est arrivé dans la case de destination.
		 * S'il n'y a pas de case de destination, on considère que le personnage
		 * est arrivé.
		 */
		public boolean hasArrived() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			if(!arrived)
			{	if(tileDest==null)
					arrived = true;
				else
				{	AiTile currentTile = ai.getActualTile();
					arrived = currentTile==tileDest;			
				}
			}		
			return arrived;
		}

		/////////////////////////////////////////////////////////////////
		// PATH			/////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/** le chemin à suivre */
		private AiPath path;
		
		private void updatePath() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			
				try {
					path = astar.processShortestPath(ourHero.getTile(),possibleDest);
				} catch (LimitReachedException e) {
					// 
					e.printStackTrace();
				}
			
			tileDest = path.getLastTile();
		}
		
		/**
		 * vérifie que le personnage est bien sur le chemin pr�-calculé,
		 * en supprimant si besoin les cases inutiles (car pr�cedant la case courante).
		 * Si le personnage n'est plus sur le chemin, alors le chemin
		 * est vide après l'exécution de cette méthode.
		 */
		private void checkIsOnPath() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			AiTile currentTile = ai.getActualTile();
			while(!path.isEmpty() && path.getTile(0)!=currentTile)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				path.removeTile(0);
			}
		}
		
		/** 
		 * teste si le chemin est toujours valide, i.e. si
		 * aucun obstacle n'est apparu depuis la dernière itération.
		 * Contrairement au PathManager, ici pour simplifier on ne teste
		 * que l'apparition de nouveaux obstacles (feu, bombes, murs), et non pas 
		 * les changement concernant la sûret� des cases. En d'autres termes,
		 * si une bombe apparait avant que le personnage d'ait atteint une
		 * case sure, elle ne sera pas prise en compte dans la trajectoire.
		 * 
		 */
		private boolean checkPathValidity() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			boolean result = true;
			Iterator<AiTile> it = path.getTiles().iterator();
			while(it.hasNext() && result)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				AiTile tile = it.next();
				result = tile.isCrossableBy(ourHero);			
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
		private MatrixCostCalculator costCalculator;

	/*	private void updateCostCalculator() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			// calcul de la matrice de coût : on prend l'opposé du niveau de sûret�
			// i.e. : plus le temps avant l'explosion est long, plus le coût est faible 
		//	double dangerMatrix[][] = ai.getgameZoneFormee().getMatrix();
			for(int line=0;line<gameZone.getHeight();line++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				for(int col=0;col<gameZone.getWidth();col++)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					double cost = ai.getCaseLevel(line, col);
					costCalculator.setCost(line,col,cost);
				}
			}
		}*/
		
		/////////////////////////////////////////////////////////////////
		// PROCESS					/////////////////////////////////////
		/////////////////////////////////////////////////////////////////	
		/** 
		 * calcule la prochaine direction pour aller vers la destination 
		 *(ou renvoie Direction.NONE si aucun déplacement n'est nécessaire)
		 * */
		public Direction update() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			// on met d'abord à jour la matrice de cout
			ai.updateCostCalculator();
		
			Direction result = Direction.NONE;
			if(!hasArrived())
			{	// on vérifie que le joueur est toujours sur le chemin
				checkIsOnPath();
				// si le chemin est vide ou invalide, on le recalcule.
				if(path.isEmpty() || !checkPathValidity())
					updatePath();
				// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
				AiTile tile = null;
				if(path.getLength()>1)
					tile = path.getTile(1);
				// sinon, s'il ne reste qu'une seule case, on va au centre
				else if(path.getLength()>0)
					tile = path.getTile(0);
				// on détermine la direction du prochain déplacement
				if(tile!=null)
					result = gameZone.getDirection(ourHero,tile);			
			}
			return result;
		}
		
		public AiPath getPath() throws StopRequestException{
			ai.checkInterruption();
			return path;
		}
	}


