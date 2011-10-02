package org.totalboumboum.ai.v201011.ais.isbirkoyas.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.totalboumboum.ai.v201011.adapter.path.AiPath;
import org.totalboumboum.ai.v201011.adapter.path.astar.Astar;
import org.totalboumboum.ai.v201011.adapter.path.astar.AstarNode;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.BasicCostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.cost.CostCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.BasicHeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.path.astar.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201011.adapter.data.AiBlock;
import org.totalboumboum.ai.v201011.adapter.data.AiBomb;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiItem;
import org.totalboumboum.ai.v201011.adapter.data.AiTile;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;
import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * @author Göksu İsbir
 * @author Ela Koyaş
 */
@SuppressWarnings({ "unused", "deprecation" })
public class IsbirKoyas extends ArtificialIntelligence
{	
	//Le chemin a suivre pour ramasser le bonus
	private AiPath nextMoveBonus = null;
	//Notre intelligence artificielle 
	private AiHero ourHero;
	//private AiPath nextMoveCollect = null;
	private AiTile openBombTile = null;
	private List<AiTile> pathTiles = new ArrayList<AiTile>();
	
		
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	
		//On teste d'interruption
		checkInterruption();
		//La zone du jeu et la perception de l'environnement
		AiZone gameZone = getPercepts();	
		//Le resultat du traitement indiquant l'action mais pour le moment IA fait rien
		AiAction result = new AiAction(AiActionName.NONE);
		//La longueur de la zone
		int width = gameZone.getWidth();
		//La largeur de la zone 
		int height = gameZone.getHeight();
		//On cree la matrice collecte 
		double[][] matrice = new double[height][width];
		//On initialise notre hero
		this.ourHero = gameZone.getOwnHero();
		//On initialise la matrice avec les '0'
		this.initialiseMatrice(matrice, gameZone);
		//On remplie la matrice collecte
		this.collect_matrice(matrice,gameZone);
		System.out.println("la premiere version de la matrice collecte :");
		//On affiche la matrice collecte en utilisant l'API fournie
		this.affiche(matrice, gameZone);
		
		
		Algorithm(gameZone,bonus_Cible(gameZone,matrice));
		System.out.println("le bonus cible:");
		this.affiche(matrice, gameZone);

		
		return result;
	}
	
	
	
	/**
	 * Methode  qui initialise la matrice collecte avec les '0'. 
	 * 
	 * @param matrice
	 *            La Matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private void initialiseMatrice(double[][] matrice, AiZone gameZone)
	throws StopRequestException 
	{
		checkInterruption();
		for (int i = 0; i < gameZone.getHeight(); i++) 
		{
			checkInterruption();
			for (int j = 0; j < gameZone.getWidth(); j++) 
			{
				checkInterruption();
				matrice[i][j] = 0;
			}
		}
}
	/**
	 * Methode qui remplie la matrice collecte avec les bonus en fonction du temps
	 * 
	 * @param matrice
	 *            La Matrice de collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */  
	private void collect_matrice(double[][] matrice, AiZone gameZone)
	throws StopRequestException 
	{
		checkInterruption();
		gameZone = getPercepts();
		Collection<AiItem> items = gameZone.getItems();
		System.out.println("items"+items);
		Iterator<AiItem> iteratorItems = items.iterator();
		while (iteratorItems.hasNext()) 
		{
			checkInterruption();
			AiItem item = iteratorItems.next();
			double T = plus_proche_Hero(gameZone,item)-duree_aller_a_Bonus(gameZone, item);
			matrice[item.getLine()][item.getCol()] = T;		
			plus_proche_bombe(gameZone,item,matrice);
		} 

		List <AiTile> bonuses= bonusDestinations(gameZone);
		System.out.println(bonuses);				
	}
	
	
	/**
	 * Methode qui donne la liste de la destination des bombes en fonction du temps
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private List<AiTile> bonusDestinations(AiZone zone) throws StopRequestException
	{
		
		checkInterruption();
		List<AiTile> dest = new ArrayList<AiTile>();
		for(int line=0;line<zone.getHeight();line++)
		{	checkInterruption();
			for(int col=0;col<zone.getWidth();col++)
			{   checkInterruption();
				AiTile tile = zone.getTile(line,col);
				if(!tile.getItems().isEmpty())
				{
					dest.add(tile);
				}
			}
		}
		return dest;
	}
	
	/**
	 * Methode qui calcule le temps d'aller au bonus du plus proche adversaire 
	 * @param bonus
	 *            item bonus
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	private double plus_proche_Hero(AiZone gameZone, AiItem bonus) throws StopRequestException
	{
		
		checkInterruption();
		List<AiHero> heros = gameZone.getRemainingHeroes();
		AiHero enemy = heros.get(0);
		heros.remove(ourHero);
		AiTile bonusPos = bonus.getTile(), enemyPos = enemy.getTile();
		int distance = gameZone.getTileDistance(bonusPos, enemyPos);
		double Vitesse= enemy.getWalkingSpeed();
		double T= distance/Vitesse;
		for (int i = 1; i < heros.size(); i++) {
			checkInterruption();
			AiHero temp = heros.get(i);
			int tempDis = gameZone.getTileDistance(bonusPos, temp.getTile());
			double tempVitesse= temp.getWalkingSpeed();
			double tempT= (tempDis/tempVitesse);						
			if (tempT < T) 
			{
				enemy = temp;
				distance = tempDis;
				T=tempT;
			}
		}	
		return T;

	}
	/**
	 * Methode qui permet l'affichage de la matrice collecte en utilisant l'API fournie
	 * @param matrice
	 *            la matrice collecte
	 * @param gameZone
	 *            la zone du jeu
	 * @throws StopRequestException
	 */
	 private void affiche(double[][] matrice, AiZone gameZone)
	 {
		 
		 for (int i = 0; i < gameZone.getHeight(); i++) 
			{
				for (int j = 0; j < gameZone.getWidth(); j++) 
				{
					System.out.print(matrice[i][j]+" ");
				}
				System.out.println();
			}
			
			System.out.println("-----------------");
	 }
	 
	 	/**
		 * Methode qui calcule la duree necessaire pour aller a la case du bonus 
		 * @param bonus
		 *            item bonus
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */ 
	 private double duree_aller_a_Bonus(AiZone gameZone, AiItem bonus) throws StopRequestException {
			checkInterruption();
			AiTile bonusPos = bonus.getTile();
			AiTile MonPos = ourHero.getTile();
			int distance = gameZone.getTileDistance(bonusPos, MonPos);
			double Vitesse= ourHero.getWalkingSpeed();
			double T= distance/Vitesse;
			return T;
		}
	 
	 
	 	/**
		 * Methode qui trouve le plus proche bombe au bonus 
		 * cette methode controle les 4 cases voisins
		 * @param bonus
		 *            item bonus
		 * @param gameZone
		 *            la zone du jeu
		 * @param matrice 
		 *            La Matrice de collecte
		 * @throws StopRequestException
		 */
		private void plus_proche_bombe(AiZone gameZone, AiItem bonus,double[][] matrice) throws StopRequestException 
		{
			checkInterruption();
								
			gameZone = getPercepts();
			Collection<AiBomb> bombes = gameZone.getBombs();
			Iterator<AiBomb> iteratorBombes = bombes.iterator();
			AiBomb tempbombe=null;
			int distance;
			double T,T2;
			while (iteratorBombes.hasNext()) 
			{
				checkInterruption();
				tempbombe = iteratorBombes.next();
				AiTile bonusPos = bonus.getTile(), bombePos = tempbombe.getTile();
				Direction dir=gameZone.getDirection(bonusPos.getPosX(), bonusPos.getPosY(), bombePos.getPosX(),bombePos.getPosY());
				System.out.println("direction"+dir);
				if(dir==Direction.UP||dir==Direction.DOWN||dir==Direction.LEFT||dir==Direction.RIGHT)
				{
					distance = gameZone.getTileDistance(bonusPos, bombePos);
					System.out.println("distance"+distance);
					// la distance d'effet va etre calculee, a completer
					T=tempbombe.getExplosionDuration()+tempbombe.getFailureProbability()-tempbombe.getLatencyDuration();		
					T2=T-duree_aller_a_Bonus(gameZone, bonus);
					matrice[tempbombe.getLine()][tempbombe.getCol()]=T2;
				}
				else
					matrice[tempbombe.getLine()][tempbombe.getCol()]=0;
			} 

		}
		
		/**
		 * Methode qui donne la liste de la destination des bombes
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */
		private List<AiTile> bombeDestinations(AiZone zone) throws StopRequestException
		{
			
			checkInterruption();
			List<AiTile> dest = new ArrayList<AiTile>();
			for(int line=0;line<zone.getHeight();line++)
			{	checkInterruption();
				for(int col=0;col<zone.getWidth();col++)
				{   checkInterruption();
					AiTile tile = zone.getTile(line,col);
					if(!tile.getBombs().isEmpty())
					{
						dest.add(tile);
					}
				}
			}
			return dest;
		}
		/**
		 * Methode qui donne le bonus cible
		 * 
		 * 
		 * @param gameZone
		 *            la zone du jeu
		 * @throws StopRequestException
		 */
		private AiTile bonus_Cible(AiZone zone, double[][] matrice)throws StopRequestException
		{
			//on calcule le maximum
			double max=0;
			double a=ourHero.getPosX();
			double b=ourHero.getPosY();
			//on elimine les cases a valeurs negatifs et il donne la valeur 0 a ces cases
			for(int line=0;line<zone.getHeight();line++)
			{	
				for(int col=0;col<zone.getWidth();col++)
				{ 
					AiTile tile = zone.getTile(line,col);
					if(!tile.getItems().isEmpty())
						if(matrice[line][col]<0)
							matrice[line][col]=0;
					if(!tile.getBombs().isEmpty())
						if(matrice[line][col]<0)
							matrice[line][col]=0;
					if(matrice[line][col]>max)
					{
						max=matrice[line][col];
						a=line;
						b=col;
						
					}
					else
						matrice[line][col]=0;
				}
			}
			AiTile bonustile = zone.getTile(a,b);
			return bonustile;
		
		}			

		private AiPath calculateShortestPath(AiHero ownHero, AiTile startPoint,
				AiTile endPoints) throws StopRequestException {
			checkInterruption();
			//le chemin le plus court possible
			AiPath shortestPath=null;
			//L'objet pour implementer l'algo A*
			Astar astar;
			ArtificialIntelligence ai=null;
			//Calcul du cout par la classe de l'API
			CostCalculator cost = new BasicCostCalculator();
			//Calcul de l'heuristic par la classe de l'API
			HeuristicCalculator heuristic = new BasicHeuristicCalculator();
			 astar = new Astar(ai,ownHero,cost,heuristic);
			 try{
			shortestPath = astar.processShortestPath(startPoint, endPoints);
			 }
			 catch(Exception e){}
			 return shortestPath;
		}

		
		private void Algorithm(AiZone gameZone, AiTile bonustile)throws StopRequestException {
			checkInterruption();
			
			//La position actuelle de notre hero
			AiTile startPoint = ourHero.getTile();

			//Les positions finales possibles de notre hero calcule par la methode calculateEndPoints
		
			//Le chemin le plus court est calcule utilisant la methode calculateShortestPath
			this.nextMoveBonus = this.calculateShortestPath(ourHero,startPoint,bonustile);
			newAction(nextMoveBonus,bonustile.getPosX(),bonustile.getPosY());
		}
		
		private AiAction newAction(AiPath nextMove,double x, double y) throws StopRequestException {
			checkInterruption();
			
			//deplacement sur l'abcisse
			double dx;
			//deplacement sur l'ordonne
			double dy;
			
			//calcul de deplacement sur l'abcisse par rapport a la position de l'hero et la premiere 
			//case du chemin le plus court. 
			dx = (this.ourHero.getPosX()) - x;
			//(tiles.get(0).getPosX());
			//calcul de deplacement sur l'ordonne par rapport a la position de l'hero et la premiere 
			//case du chemin le plus court. 
			dy = (this.ourHero.getPosY()) -y;
			//(tiles.get(0).getPosY());
			
			//Determine la direction ou le hero va se deplacer.
			if (dx < 0 && dy == 0) {
				return new AiAction(AiActionName.MOVE, Direction.LEFT);
			} else if (dx < 0 && dy < 0) {
				return new AiAction(AiActionName.MOVE, Direction.UPLEFT);
			} else if (dx == 0 && dy < 0) {
				return new AiAction(AiActionName.MOVE, Direction.UP);
			} else if (dx > 0 && dy == 0) {
				return new AiAction(AiActionName.MOVE, Direction.RIGHT);
			} else if (dx > 0 && dy > 0) {
				return new AiAction(AiActionName.MOVE, Direction.DOWNRIGHT);
			} else if (dx == 0 && dy > 0) {
				return new AiAction(AiActionName.MOVE, Direction.DOWN);
			} else if (dx > 0 && dy < 0) {
				return new AiAction(AiActionName.MOVE, Direction.UPRIGHT);
			} else if (dx < 0 && dy > 0) {
				return new AiAction(AiActionName.MOVE, Direction.DOWNLEFT);
			} else {
				return new AiAction(AiActionName.NONE);
			}

		}
		
}
