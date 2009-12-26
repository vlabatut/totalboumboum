package tournament200910.aldanmazyenigun.v4_2;

import java.util.Iterator;
import java.util.List;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.AiPath;
import fr.free.totalboumboum.ai.adapter200910.path.astar.Astar;
import fr.free.totalboumboum.ai.adapter200910.path.astar.cost.MatrixCostCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.BasicHeuristicCalculator;
import fr.free.totalboumboum.ai.adapter200910.path.astar.heuristic.HeuristicCalculator;
import fr.free.totalboumboum.engine.content.feature.Direction;


public class SafeStateController {

		/**
		 * cr�e un EscapeManager charg� d'amener le personnage au centre d'une case s�re
		 */
		public SafeStateController(AldanmazYenigun ai) throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			this.ai = ai;
			zone = ai.getZone();
			
			
			// init A*
			double costMatrix[][] = new double[zone.getHeigh()][zone.getWidth()];
			costCalculator = new MatrixCostCalculator(costMatrix);
			heuristicCalculator = new BasicHeuristicCalculator();
			astar = new Astar(ai.getOwnHero(),costCalculator,heuristicCalculator);
			
			// init destinations
			arrived = false;
			AiHero ownHero = ai.getOwnHero();
			possibleDest = ai.findItemsTiles(ownHero.getTile());
			updatePath();
		}
		
		/////////////////////////////////////////////////////////////////
		// ARTIFICIAL INTELLIGENCE		/////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/** l'IA concern�e par ce gestionnaire de chemin */
		private AldanmazYenigun ai;
		/** zone de jeu */
		private AiZone zone;	
		
		
		/////////////////////////////////////////////////////////////////
		// DESTINATION	/////////////////////////////////////////////////
		/////////////////////////////////////////////////////////////////
		/** indique si le personnage est arriv� � destination */
		private boolean arrived;
		/** la case de destination s�lectionn�e pour la fuite */
		private AiTile tileDest;
		/** destinations potentielles */
		private List<AiTile> possibleDest;

		/**
		 * d�termine si le personnage est arriv� dans la case de destination.
		 * S'il n'y a pas de case de destination, on consid�re que le personnage
		 * est arriv�.
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
		/** le chemin � suivre */
		private AiPath path;
	
		
		private void updatePath() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			path = astar.processShortestPath(ai.getActualTile(),possibleDest);
			tileDest = path.getLastTile();
		}
		
		/**
		 * v�rifie que le personnage est bien sur le chemin pr�-calcul�,
		 * en supprimant si besoin les cases inutiles (car pr�cedant la case courante).
		 * Si le personnage n'est plus sur le chemin, alors le chemin
		 * est vide apr�s l'ex�cution de cette m�thode.
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
		 * aucun obstacle n'est apparu depuis la derni�re it�ration.
		 * Contrairement au PathManager, ici pour simplifier on ne teste
		 * que l'apparition de nouveaux obstacles (feu, bombes, murs), et non pas 
		 * les changement concernant la s�ret� des cases. En d'autres termes,
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
				result = tile.isCrossableBy(ai.getOwnHero());			
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
		private MatrixCostCalculator costCalculator;

		private void updateCostCalculator() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
			
			// calcul de la matrice de co�t : on prend l'oppos� du niveau de s�ret�
			// i.e. : plus le temps avant l'explosion est long, plus le co�t est faible 
		//	double dangerMatrix[][] = ai.getZoneFormee().getMatrix();
			for(int line=0;line<zone.getHeigh();line++)
			{	ai.checkInterruption(); //APPEL OBLIGATOIRE
				for(int col=0;col<zone.getWidth();col++)
				{	ai.checkInterruption(); //APPEL OBLIGATOIRE
					double cost = ai.getZoneFormee().getCaseLevel(line, col);
					costCalculator.setCost(line,col,cost);
				}
			}
		}
		
		/////////////////////////////////////////////////////////////////
		// PROCESS					/////////////////////////////////////
		/////////////////////////////////////////////////////////////////	
		/** 
		 * calcule la prochaine direction pour aller vers la destination 
		 *(ou renvoie Direction.NONE si aucun d�placement n'est n�cessaire)
		 * */
		
		private boolean isBonusAccessible = true;
		
		
		public Direction update() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			// on met d'abord � jour la matrice de cout
			updateCostCalculator();
		
			Direction result = Direction.NONE;
			ai.setAccessible(true);
			if(!hasArrived())
			{	// on v�rifie que le joueur est toujours sur le chemin
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
				// on d�termine la direction du prochain d�placement
				if(tile!=null)
					result = zone.getDirection(ai.getOwnHero(),tile);
			}
			if(path.isEmpty())
				ai.setAccessible(false);
			return result;
		}
		
		public boolean isAccessible() throws StopRequestException
		{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
			boolean result = true;
			// on met d'abord � jour la matrice de cout
			updateCostCalculator();
			if(!hasArrived())
			{	// on v�rifie que le joueur est toujours sur le chemin
				checkIsOnPath();
				if(path.isEmpty())
					result = false;
				// si le chemin est vide ou invalide, on le recalcule.
				if(path.isEmpty() || !checkPathValidity())
					updatePath();
				// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
				@SuppressWarnings("unused")
				AiTile tile = null;
				if(path.getLength()>1)
					tile = path.getTile(1);
				// sinon, s'il ne reste qu'une seule case, on va au centre
				else if(path.getLength()>0)
					tile = path.getTile(0);
				// on d�termine la direction du prochain d�placement
				//if(tile!=null)	
				
				
			}
			return result;
		}

		public void setBonusAccessible(boolean isBonusAccessible) {
			this.isBonusAccessible = isBonusAccessible;
		}

		public boolean isBonusAccessible() {
			return isBonusAccessible;
		}
		
		

	}


