package org.totalboumboum.ai.v201314.adapter.test;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

import java.util.Map;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.AiSearchNode;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.ApproximateCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Dijkstra;
import org.totalboumboum.ai.v201314.adapter.path.successor.ApproximateSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet d'appliquer l'algorithme de Dijkstra.
 * Il est recommandé de regarder d'abord {@link AstarUse}
 * avant cette classe, car elle reprend plus ou moins les mêmes
 * exemples, adaptés à Dijkstra.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class DijkstraUse
{
	/**
	 * Teste les fonctionalités de l'API IA
	 * liées à l'algorithme de Dijkstra.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 */
	public static void main(String args[])
	{	// utilisation de base
		example1();
		// fonctions à base de distance exprimée en pixels
		example2();
		// fonctions à base de temps + modèle complet
		example3();
		// fonctions à base de temps + modèle partiel
		example4();
		// fonctions approximatives
		example5();
		// chercher un chemin de fuite
		example6();
	}
	
	/**
	 * Affiche les coûts correspondant aux chemins
	 * associés à chaque case de la zone. C'est un
	 * affichage de l'objet renvoyé par dijkstra.
	 * 
	 * @param zone
	 * 		La zone considérée.
	 * @param paths
	 * 		Les chemins trouvés par dijkstra.
	 */
	private static void displayTimeMatrix(AiZone zone, Map<AiTile,AiSearchNode> paths)
	{	int width = zone.getWidth();
		int height = zone.getHeight();
		
		for(int col=0;col<width;col++)
			System.out.print("\t"+col);
		System.out.println();

		for(int row=0;row<height;row++)
		{	System.out.print(row);
			for(int col=0;col<width;col++)
			{	AiTile tile = zone.getTile(row,col);
				AiSearchNode leaf = paths.get(tile);
				if(leaf==null)
					System.out.print("\t+∞");
				else
					System.out.print("\t"+leaf.getCost());
			}
			System.out.println();
		}
	}
	
	/**
	 * Utilisation simple : fonctions basées sur des distances,
	 * le temps et l'aspect dynamique de la zone ne sont pas
	 * pris en compte.
	 */
	private static void example1()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 1 --------");
		// On crée l'objet dijkstra avec les fonctions les plus simples.
		// A noter qu'à la différence de A*, on n'a pas besoin de fonction
		// heuristique pour cet algorithme.
		CostCalculator costCalculator = new TileCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // pour afficher les détails du traitement
		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation classique de Dijkstra +++");
		// On applique l'algorithme pour trouver tous les chemins
		// entre la position courante et toutes les autres cases de la zone.
		// Donc, il est inutile de préciser une case de destination,
		// puisqu'elles le sont toutes (des destinations).
		AiLocation startLocation = new AiLocation(hero);
		try
		{	// Au lieu de renvoyer un simple chemin, dijkstra
			// renvoie une map associant à chaque case le noeud
			// de recherche feuille correspondant. Si aucun
			// chemin n'est trouvé, c'est la valeur null qui est 
			// associée à la case. Sinon, s'il y a bien un noeud
			// de recherche, alors la méthode processPath de ce
			// noeud permet d'obtenir le chemin pour aller dans
			// cette case.
			Map<AiTile,AiSearchNode> paths = dijkstra.startProcess(startLocation);
			// On affiche une matrice correspondant au temps nécessaire
			// pour aller dans chaque case.
			displayTimeMatrix(zone,paths);
			// on extrait et affiche le chemin pour l'une des cases
			AiTile tile = zone.getTile(3,4);
			AiSearchNode leaf = paths.get(tile);
			AiPath path = leaf.processPath();
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		// Comme pour A*, il est possible de reprendre une recherche depuis
		// le début en réutilisant l'arbre de recherche existant, en utilisant :
		// 		dijkstra.startProcess();
		// Il est aussi possible de reprendre une recherche qui avait été
		// interrompue par une exception en utilisant :
		//		dijkstra.continueProcess();
		
		// Les limites sont gérées comme pour A* et leur dépassement
		// déclenche lui aussi une LimitReachedException.
	}
	
	/**
	 * Utilisation de fonctions basées sur des distances
	 * exprimées en pixels plutôt qu'en cases (comme
	 * c'était le cas dans l'exemple 1).
	 */
	private static void example2()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 2 --------");
		// Comme pour A*, on peut utiliser les fonctions à base de pixels,
		// plutôt que celle à base de cases. 
		CostCalculator costCalculator = new PixelCostCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // pour afficher les détails du traitement
		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation des fonctions à base de pixels +++");
		AiLocation startLocation = new AiLocation(24,25,zone);
		try
		{	// Application de l'algo.
			Map<AiTile,AiSearchNode> paths = dijkstra.startProcess(startLocation);
			// Matrice des temps.
			displayTimeMatrix(zone,paths);
			// Chemin obtenu
			AiTile tile = zone.getTile(3,4);
			AiSearchNode leaf = paths.get(tile);
			AiPath path = leaf.processPath();
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * On peut utiliser les fonctions à base de temps exactement
	 * comme pour A*. Bien sûr, en raison du fait qu'on cherche
	 * tous les chemin, le traitement est encore plus long avec
	 * Dijkstra qu'avec A*.
	 */
	private static void example3()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Utilisation des fonctions tenant compte du temps, avec modèle complet.
		System.out.println("\n\n-------- EXEMPLE 3 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		// Pour l'exemple on prend MODE_NOTREE (le plus restrictif), 
		// mais les autres modes sont aussi utilisables ici.
		SuccessorCalculator successorCalculator = new TimeFullSuccessorCalculator(ai,hero,SearchMode.MODE_NOTREE);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // Pour afficher les détails du traitement
		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOBRANCH +++");
		AiLocation startLocation = new AiLocation(hero);
		try
		{	// Application de l'algo.
			Map<AiTile,AiSearchNode> paths = dijkstra.startProcess(startLocation);
			// Matrice des temps.
			displayTimeMatrix(zone,paths);
			// Chemin obtenu.
			AiTile tile = zone.getTile(3,4);
			AiSearchNode leaf = paths.get(tile);
			AiPath path = leaf.processPath();
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
	}

	/**
	 * L'utilisation du modèle partiel rend Dijkstra utilisable
	 * (sinon c'est trop long).
	 */
	private static void example4()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Utilisation des fonctions tenant compte du temps, 
		// avec modèle partiel.
		System.out.println("\n\n-------- EXEMPLE 4 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_NOTREE);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // Pour afficher les détails du traitement
		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOTREE +++");
		AiLocation startLocation = new AiLocation(hero);
		try
		{	// Application de l'algo.
			Map<AiTile,AiSearchNode> paths = dijkstra.startProcess(startLocation);
			// Matrice des temps.
			displayTimeMatrix(zone,paths);
			// Chemin obtenu.
			AiTile tile = zone.getTile(3,4);
			AiSearchNode leaf = paths.get(tile);
			AiPath path = leaf.processPath();
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
	}
	
	/**
	 * Autre utilisation intéressante : les fonctions faisant des
	 * calculs approximés. Ca peut être utile pour trouver des
	 * chemins passant à travers les cases destructibles, par exemple
	 * quand on veut identifier quels murs on doit détruire pour atteindre
	 * un item ou un adversaire inaccessible..
	 */
	private static void example5()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Utilisation des fonctions approximatives (sans modèle).
		System.out.println("\n\n-------- EXEMPLE 5 --------");
		CostCalculator costCalculator = new ApproximateCostCalculator(ai,hero);
		SuccessorCalculator successorCalculator = new ApproximateSuccessorCalculator(ai);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // pour afficher les détails du traitement
		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation des fonctions approximatives (à base de temps, mais sans modèle) +++");
		AiLocation startLocation = new AiLocation(hero);
		try
		{	// Application de l'algo.
			Map<AiTile,AiSearchNode> paths = dijkstra.startProcess(startLocation);
			// Matrice des temps.
			displayTimeMatrix(zone,paths);
			// Chemin obtenu.
			AiTile tile = zone.getTile(3,4);
			AiSearchNode leaf = paths.get(tile);
			AiPath path = leaf.processPath();
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
	}

	/**
	 * Dans cet exemple, on veut quitter la case courante, qui est
	 * menacée. On n'a pas de destination particulière, on veut juste
	 * que l'agent se mette à l'abri n'importe où. En l'absence de
	 * destination, c'est bien sur Dijkstra qui est le plus adapté.
	 * La différence avec la version classique est qu'ici, on s'arrête
	 * dès qu'on rencontre une case sûre (par opposition, on cherche
	 * les chemins vers toutes les destinations possibles dans la version
	 * classique de Dijkstra). La notion de "case sûre" est définie dans
	 * la fonction successeur choisie.
	 * <br/>
	 * Notez que ce n'est pas forcément une bonne idée d'utiliser cette 
	 * fonction : il est généralement plus efficace de ne pas fuir n'importe
	 * où, mais dans une case bien sélectionnée. Sinon, on peut tomber
	 * facilement dans un piège.
	 */
	private static void example6()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone5();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Utilisation de la fonction de fuite dans Dijkstra.
		System.out.println("\n\n-------- EXEMPLE 6 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_NOTREE);
		Dijkstra dijkstra = new Dijkstra(ai,hero,costCalculator,successorCalculator);
		dijkstra.setVerbose(true); // pour afficher les détails du traitement
//		dijkstra.setMaxHeight(2*(zone.getWidth()+zone.getHeight())); // on augmente la limite
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOTREE +++");
		AiLocation startLocation = new AiLocation(hero);
		try
		{	// Application de l'algo.
			AiPath path = dijkstra.processEscapePath(startLocation);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
	}
}
