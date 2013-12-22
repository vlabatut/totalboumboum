package org.totalboumboum.ai.v201314.adapter.test;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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

import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201314.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201314.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201314.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201314.adapter.path.AiLocation;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.ai.v201314.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201314.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201314.adapter.path.search.Astar;
import org.totalboumboum.ai.v201314.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.SearchMode;
import org.totalboumboum.ai.v201314.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201314.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet d'appliquer l'algorithme A* de différentes
 * manières.
 * 
 * @author Vincent Labatut
 */
public final class AstarUse
{
	/**
	 * Teste les fonctionalités de l'API IA
	 * propres à l'algorithme A*.
	 * 
	 * @param args
	 * 		Pas utilisé.
	 */
	public static void main(String args[])
	{	// utilisation de base
		example1();
		// gestion du cas d'échec
		example2();
		// gestion des limites (LimitReachedException)
		example3();
		// fonctions à base de distance exprimée en pixels
		example4();
		// utilisation d'un coût supplémentaire
		example5();
		// fonctions à base de temps + modèle complet
		example6();
		// fonctions à base de temps + modèle partiel
		example7();
		// recherche de chemin cyclique avec A* 
		example8();
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
		// On crée l'objet a* avec les fonctions les plus simples.
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // Pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation classique de A* +++");
		// On applique l'algorithme pour trouver une chemin 
		// entre la position courante et une case de destination.
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(5,3);
		try
		{	AiPath path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Ré-utilisation d'un arbre de recherche existant +++");
		// On peut relancer la recherche pour une autre destination,
		// en ré-utilisant l'arbre déjà développé par A* lors de l'appel
		// précédent. Ca permet de gagner (un peu) de temps.
		endTile = zone.getTile(2,5);
		try
		{	AiPath path = astar.startProcess(endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Recherche avec objectif multiple +++");
		// Il est aussi possible de faire une recherche en considérant
		// plusieurs destinations en même temps. L'algorithme s'arrête
		// dès qu'il rencontre une de ces destinations. C'est pratique,
		// par exemple, quand la préférence minimale est atteinte par 
		// plusieurs cases. La recherche par objectif multiple peut être 
		// utilisée aussi bien lors d'une première recherche que quand 
		// on réutilise un arbre de recherche issu d'une recherche 
		// précédente (comme ci-dessus).
		Set<AiTile> destinations = new TreeSet<AiTile>();
		destinations.add(zone.getTile(3,4));
		destinations.add(zone.getTile(5,3));
		try
		{	AiPath path = astar.startProcess(destinations);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Continuer une recherche déjà commencée +++");
		// Si, pour une raison quelconque, le résultat trouvé ne vous plait
		// pas, il est possible de reprendre la recherche. Notez que le
		// résultat suivant ne sera pas forcément optimal relativement à
		// la fonction de coût (ceci n'est garanti que pour la première 
		// solution trouvée). Cette approche est utilisée dans l'exemple 3, 
		// à la suite d'une exception.
		try
		{	AiPath path = astar.continueProcess();
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
	 * Gestion du cas d'échec : il n'existe pas de chemin 
	 * allant du point de départ au(x) point(s) d'arrivée
	 * spécifié(s).
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
		// On réutilise les fonctions les plus simples pour cet exemple.
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation de A* sans solution possible +++");
		// On applique l'algorithme pour trouver une chemin 
		// entre la position courante et une case de destination,
		// mais cette fois il n'existe pas de solution.
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		try
		{	AiPath path = astar.startProcess(startLocation,endTile);
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
	 * Utilisation des limites définies pour l'algorithme
	 * de recherche (exception {@link LimitReachedException}).
	 */
	private static void example3()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Utilisation d'une limite.
		System.out.println("\n\n-------- EXEMPLE 3 --------");
		// On réutilise les fonctions les plus simples pour cet exemple.
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // Pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation de A* dépassant une des 3 limites (cout/hauteur/nombre de noeuds) +++");
		// On modifie la limite de hauteur de l'arbre, de manière à ne
		// pas chercher des chemins plus longs que 4 déplacements.
		astar.setMaxHeight(4);
		AiLocation startLocation = new AiLocation(hero);
		// On choisit pour l'exemple une destination accessible, 
		// mais avec un chemin minimal de longueur supérieure à 4
		AiTile endTile = zone.getTile(3,4);
		AiPath path = null;
		try
		{	path = astar.startProcess(startLocation,endTile);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			// Le fait de lever cette exception indique qu'A* a
			// développé un arbre de recherche trop grand : il existe
			// peut-être une solution, mais elle n'a pas été trouvée.
			System.out.println("+++ path="+path);
			System.out.println(e.getSummary());

			System.out.println("+++ On relance la recherche en augmentant la limite +++");
			// On augmente la limite à 5 et on continue la recherche (on ne recommence pas à zéro).
			astar.setMaxHeight(5);
			try
			{	// On continue le traitement en utilisant le même
				// arbre, pour les mêmes points de départ et de destination.
				path = astar.continueProcess();

				// Remarque 1 : en réalisant l'appel
				// 		path = astar.processShortestPath(endTile);
				// on aurait aussi le même résultat, sauf que la recherche
				// serait repartie de la racine de l'arbre. On aurait quand
				// même réutilisé l'arbre existant, mais le traitement aurait
				// nécessité plus d'itérations.

				// Remarque 2 : en réalisant l'appel
				// 		path = astar.processShortestPath(startLocation,endTile);
				// on aurait le même résultat, la recherche aurait aussi
				// recommencé depuis le début, mais cette fois sans réutiliser
				// l'arbre existant. On aurait construit un nouvel arbre,
				// donc le traitement aurait été encore plus long.
				
				// Remarque 3 : l'exemple donné ici n'est pas du tout
				// équivalent à la méthode de l'approfondissement itératif 
				// vue en cours, qui n'est pas implémentée dans l'API.
				
				System.out.println("+++ path="+path);
			}
			catch (StopRequestException ex)
			{	ex.printStackTrace();
			}
			catch (LimitReachedException ex)
			{	ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Utilisation de fonctions basées sur des distances
	 * exprimées en pixels plutôt qu'en cases (comme
	 * c'était le cas dans les exemples précédents).
	 */
	private static void example4()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 4 --------");
		// On réutilise les fonctions utilisant les pixels
		// au lieu des cases pour calculer les coûts et heuristiques.
		// -> Les calculs seront très légèrement plus longs, mais aussi plus précis.
		// On garde la même fonction successeur (basique).
		CostCalculator costCalculator = new PixelCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new PixelHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de pixels +++");
		// On peut utiliser un point de départ différent de la position d'une case.
		// Par exemple, ici, on laisse le point de départ dans la même case (1,1),
		// mais on l'a en plus déplacé lègèrement vers le bas (il n'est donc plus situé
		// au milieu de la case). >> Par conséquent, cette fois c'est le chemin partant
		// vers le bas qui sera exploré en premier (et non pas celui partant vers
		// la droite comme c'était le cas avec les fonctions basées sur les cases).
		// Remarquez aussi les différences dans les valeurs d'heuristique et de coût.
		AiLocation startLocation = new AiLocation(24,25,zone);
		AiTile endTile = zone.getTile(5,3);
		try
		{	AiPath path = astar.startProcess(startLocation,endTile);
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
	 * Utilisation d'une matrice de coût supplémentaire pour
	 * affecter des coûts additionnels à certaines cases de
	 * la zone. Cela permet d'exprimer le fait qu'on préfère
	 * ne pas passer par ces cases, mais qu'on permet d'y passer
	 * si aucun autre chemin n'est trouvé.
	 */
	private static void example5()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 5 --------");
		// On réutilise les fonctions utilisant les pixels, comme dans l'exemple précédent.
		CostCalculator costCalculator = new PixelCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new PixelHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		// On met en place une matrice de coûts supplémentaires dans la fonction de coût.
		double tileCosts[][] = new double[zone.getHeight()][zone.getWidth()];
		// On initialise avec des zéros pour chaque case.
		for(int r=0;r<tileCosts.length;r++)
		{	for(int c=0;c<tileCosts[0].length;c++)
			{	tileCosts[r][c] = 0;
			}
		}
		// Ici, on n'affecte un coût supplémentaire qu'à la case (1,2).
		// Il signifie que l'action consistant à traverser cette case
		// sera égal au coût de cette action plus 3 fois la taille d'une 
		// case, exprimée en pixels. Il est toujours préférable d'utiliser
		// des valeurs calculées par rapport à la dimension de la zone,
		// plutot que des valeurs numériques, qui seraient arbitraire.
		// Par exemple, la taille d'une case, ou bien le temps nécessaire
		// à l'agent pour traverser une case. Ici, on exprime le fait qu'on
		// considère que traverser (1,2) coûte autant que traverser 3 cases
		// normales.
		// Note : l'unité du coût supplémentaire est la même que celle du coût
		// de l'action. Donc pour un coût temporel il s'agira de ms, etc.
		tileCosts[1][2] = AiTile.getSize()*3;
		// Enfin, on met tout ça dans la fonctioin de coût.
		costCalculator.setTileCosts(tileCosts);
		
		System.out.println("+++ Effet du coût supplémentaire +++");
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(5,3);
		try
		{	AiPath path = astar.startProcess(startLocation,endTile);
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
	 * Les fonctions utilisées dans les exemples précédents
	 * supposent que la zone est statique, par exemple elles 
	 * ne tiennent pas compte du fait que la bombe va exploser.
	 * pour celà, il est nécessaire d'avoir un modèle permettant 
	 * de prédire ce qui va se passer. 
	 * <br/>
	 * L'API propose deux modèles : 
	 * <ul>
	 * 		<li>{@link AiFullModel} permet d'avoir une représentation 
	 * 			complète de la zone, mais cela demande un traitement 
	 * 			plus lourd. </li>
	 * 		<li>{@link AiPartialModel} utilise une représentation 
	 * 			simplifiée de la zone, mais suffisante pour y chercher 
	 * 			un chemin, ce qui permet d'alléger les calculs.<li/>
	 * </ul> 
	 * Il existe des fonctions successeur basées sur ces deux modèles, 
	 * et des fonctions heuristique et de coût basées sur le temps 
	 * plutôt que sur la distance.
	 */
	private static void example6()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		
		// Utilisation des fonctions tenant compte du temps, 
		// avec modèle complet pour commencer.
		System.out.println("\n\n-------- EXEMPLE 6 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,hero);
		// Les fonctions successeurs basées sur les modèles peuvent être
		// paramétrées. En effet, la grosse différence avec les autres
		// fonctions successeur est qu'ici on considère non seulement
		// les actions de déplacement, mais aussi l'action d'attente.
		// Cela permet par exemple d'attendre qu'une explosion voisine
		// se termine avant de changer de case. Seulement, le fait de
		// traiter l'attente rend les calculs beaucoup plus longs. Le
		// paramètre du mode d'exploration permet de limiter ces calculs,
		// en interdisant l'exploration de certaines branches de l'arbre
		// de recherche :
		//		- MODE_NOTREE est le mode le plus restrictif, il interdit
		//		  de traiter à nouveau une case qui a déjà été traitée
		//		  précédemment lors de l'exploration (sauf si le nouveau
		//		  chemin a un coût inférieur, là on retraite la case).
		// 		- MODE_NOBRANCH est moins restrictif car il n'interdit 
		//		  que de repasser par une case déjà présente dans la
		// 		  branche de l'arbre de recherche qui est en train d'être 
		//		  développée.
		// 		- MODE_ONEBRANCH est encore moins restrictif, car il permet 
		//		  que de repasser exactement une fois par une case déjà présente 
		//		  dans la branche de l'arbre de recherche qui est en train 
		//		  d'être développée.
		// 		- MODE_ALL ne fixe aucune limite : une déjà explorée sera
		//		  ré-explorée si nécessaire, ce qui permet d'envisager
		//		  absolument tous les chemins possibles.
		SuccessorCalculator successorCalculator = new TimeFullSuccessorCalculator(ai,hero,SearchMode.MODE_ALL);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // Pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_ALL +++");
		// Ici, on ne limite absolument pas l'exploration de l'arbre de recherche.
		// On veut aller à la case (4,5), qui était considérée comme inaccessible
		// par les fonctions non-temporelles. 
		// Notez que les coûts et valeurs heuristiques sont maintenant des durées 
		// exprimées en ms. Dans le chemin solution, une valeur temporelle indique 
		// s'il faut juste traverser la case (0) ou bien s'il est nécessaire d'attendre 
		// dans la case (>0). 
		// Notez aussi qu'à la différence des exemples précédents, maintenant la zone 
		// évolue à chaque itération de l'algorithme de recherche. par exemple, à 
		// l'itération 4 la bombe explose.
		// Notez combien le temps nécessaire au calcul est supérieur à ceux observés 
		// pour les exemples précédents. Cela est essentiellement dû au fait que ce 
		// mode de recherche considère les retours en arrière. Par exemple, dès la 
		// deuxième itération, on envisage le fait de revenir dans la première case.
		// Ceci s'explique par le fait que l'état de la 1ère case a peut-être changé
		// pendant le déplacement (n'oubliez pas que la zone est modélisée dynamiquement
		// ici), et donc peut n'être qu'un obstacle passé, qui n'existe plus maintenant.
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		AiPath path = null;
		try
		{	path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			System.out.println("+++ path="+path);
			System.out.println(e.getSummary());
		}
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOBRANCH +++");
		// On refait la même chose, mais en utilisant cette fois le mode de recherche 
		// MODE_NOBRANCH. Ceci permet de faire un contrôle empêchant d'explorer une
		// case si elle est déjà présente dans la branche courante. 
		// La conséquence est que le chemin trouvé ne peut pas contenir de retour en 
		// arrière ou de boucle. A noter qu'une action d'attente est considérée comme 
		// le début d'une nouvelle branche, i.e. : on autorise les retours en arrière 
		// seulement après une attente. L'action d'attendre n'est considérée que si 
		// elle permet d'éviter une explosion ou un obstacle sur le point d'être détruit. 
		// Notez que ce mode de recherche est beaucoup plus rapide que le précédent. Notez
		// aussi que le chemin contient un temps d'attente (à la fin) dû au fait que
		// la bombe bloque le passage, et qu'il est nécessaire d'attendre qu'elle
		// explose pour pouvoir atteindre la case de destination.
		successorCalculator = new TimeFullSuccessorCalculator(ai,hero,SearchMode.MODE_NOBRANCH);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOTREE +++");
		// On refait la même chose, mais en utilisant cette fois le mode de recherche 
		// MODE_NOTREE. Il est plus restrictif que les précédent, puisqu'il empêchera
		// deux chemins développés dans le même arbre de se croiser (le précédent
		// mode de recherche empêche seulement un chemin donné de se croiser lui-même).
		// Par conséquent, le traitement est plus court, mais le résultat est moins
		// précis : des chemins peut-être valides seront écartés. Comparez le nombre
		// d'itérations nécessaires à ceux observés pour les deux autres modes.
		successorCalculator = new TimeFullSuccessorCalculator(ai,hero,SearchMode.MODE_NOTREE);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		// L'intérêt d'utiliser un modèle complet est que la zone est
		// mise à jour à chaque itération. Par conséquent, la zone obtenue 
		// dans le dernier noeud de recherche correspond à l'état de
		// l'environnement tel qu'il sera quand l'agent arrivera à
		// destination. Il est donc possible d'utiliser cette zone pour
		// faire des tests complémentaires, si c'est nécessaire, avant
		// de vraiment choisir le chemin renvoyé par A*.
	}

	/**
	 * Le modèle complet {@link AiFullModel} offre la possibilité de
	 * faire un traitement complémentaire sur la zone obtenue, mais
	 * son défaut est qu'il ralentit significativement le traitement
	 * de A*. On peut donc alternativement utiliser le modèle partiel
	 * {@link AiPartialModel} : il offre une représentation incomplète
	 * de la zone, mais suffisante pour appliquer A* et obtenir les
	 * mêmes résultats qu'avec le modèle complet. Les seules différences
	 * sont que 1) le calcul est beaucoup plus rapide, et 2) on
	 * n'obtient pas à la fin du traitement une zone mise à jour, alors
	 * que c'est le cas avec le modèle complet.
	 */
	private static void example7()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone
		AiSimZone zone = InitData.initZone1();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		
		// Utilisation des fonctions tenant compte du temps, 
		// avec modèle partiel.
		System.out.println("\n\n-------- EXEMPLE 7 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,hero);
		// Les paramètres sont les mêmes que pour le modèle complet.
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_ALL);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // Pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle partiel, MODE_ALL +++");
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		AiPath path = null;
		try
		{	path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			System.out.println("+++ path="+path);
			System.out.println(e.getSummary());
		}
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle partiel, MODE_NOBRANCH +++");
		successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_NOBRANCH);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.startProcess(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle partiel, MODE_NOTREE +++");
		successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_NOTREE);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.startProcess(startLocation,endTile);
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
	 * Il est possible d'utiliser A* pour rechercher un chemin cyclique,
	 * i.e. partant de et arrivant à la même case. Ce type de chemin est
	 * utile quand l'agent est déjà sur sa case de destination, mais que
	 * celle-ci est menacée par une bombe : il faut alors en partir puis
	 * y revenir. 
	 * <br/>
	 * A noter que cette utilisation de A* ne peut être faite
	 * que si :
	 * <ol>
	 * 		<li>la fonction successeur tient compte du temps, donc seules
	 * 			les classes {@link TimeFullSuccessorCalculator} et 
	 * 			{@link TimePartialSuccessorCalculator} sont utilisables</li>
	 * 		<li>le mode de recherche n'est pas être trop restrictif :
	 * 			soit {@link SearchMode#MODE_ALL}, soit 
	 * 			{@link SearchMode#MODE_ONEBRANCH}.</li>
	 * </ol> 
	 * Ces deux conditions sont nécessaires pour pouvoir découvrir un chemin 
	 * dans lequel la même case figure plusieurs fois (ce qui est au moins le 
	 * cas de la case de départ/destination, qui sera à la fois la première et 
	 * dernière case du chemin).
	 */
	private static void example8()
	{	// On utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place.
		ArtificialIntelligence ai = InitData.initAi();
		ai.setVerbose(true);
		
		// On initialise la zone.
		AiSimZone zone = InitData.initZone5();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// Recherche d'un cycle permettant de revenir à la case de départ.
		System.out.println("\n\n-------- EXEMPLE 8 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,hero);
		// Les paramètres sont les mêmes que pour le modèle complet.
		System.out.println("+++ Recherche en mode MODE_ALL +++");
		TimePartialSuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_ALL);
		successorCalculator.setDetailedOutput(true); // On active la sortie détaillée, pour afficher les temps des explosions
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // Pour afficher les détails du traitement
		
		AiLocation startLocation = new AiLocation(hero);
		AiPath path = null;
		try
		{	path = astar.processLoopPath(startLocation);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	// On recommence avec un mode de recherche plus restrictif.
			System.out.println("+++ path="+path);
			System.out.println(e.getSummary());
			System.out.println("+++ On recommence en mode MODE_ONEBRANCH, cette fois +++");
			successorCalculator = new TimePartialSuccessorCalculator(ai,SearchMode.MODE_ONEBRANCH);
			successorCalculator.setDetailedOutput(true);
			astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
			astar.setVerbose(true);
			try
			{	path = astar.processLoopPath(startLocation);
				System.out.println("+++ path="+path);
			}
			catch (StopRequestException ex)
			{	ex.printStackTrace();
			}
			catch (LimitReachedException ex)
			{	ex.printStackTrace();
			}
		}
	}
}
