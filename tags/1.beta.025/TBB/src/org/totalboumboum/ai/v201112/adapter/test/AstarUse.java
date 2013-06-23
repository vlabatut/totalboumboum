package org.totalboumboum.ai.v201112.adapter.test;

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

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.model.full.AiFullModel;
import org.totalboumboum.ai.v201112.adapter.model.full.AiSimZone;
import org.totalboumboum.ai.v201112.adapter.model.partial.AiPartialModel;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.cost.CostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.PixelCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TileCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.cost.TimeCostCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.HeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.PixelHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TileHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.heuristic.TimeHeuristicCalculator;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
import org.totalboumboum.ai.v201112.adapter.path.successor.BasicSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.SuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimeFullSuccessorCalculator;
import org.totalboumboum.ai.v201112.adapter.path.successor.TimePartialSuccessorCalculator;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe utilisée pour tester les fonctionnalités de l'API IA.
 * Cette classe permet d'appliquer l'algorithme A* de différentes
 * manières.
 * 
 * @author Vincent Labatut
 * 
 * @deprecated
 *		Ancienne API d'IA, à ne plus utiliser. 
 */
public final class AstarUse
{
	/**
	 * 
	 * @param args
	 * 		?	
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
		// fonctions à base de temps + modèle complet
		example5();
		// fonctions à base de temps + modèle partiel
		example6();
	}
	
	/**
	 * Utilisation simple : fonctions basées sur des distances,
	 * le temps et l'aspect dynamique de la zone ne sont pas
	 * pris en compte.
	 */
	private static void example1()
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 1 --------");
		// on crée l'objet a* avec les fonctions les plus simples
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation classique de A* +++");
		// on applique l'algorithme pour trouver une chemin 
		// entre la position courante et une case de destination
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(5,3);
		try
		{	AiPath path = astar.processShortestPath(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Ré-utilisation d'un arbre de recherche existant +++");
		// on peut relancer la recherche pour une autre destination
		// en ré-utilisant l'arbre déjà développé par A* lors de l'appel
		// précédent. ça permet de gagner (un peu) du temps.
		endTile = zone.getTile(2,5);
		try
		{	AiPath path = astar.processShortestPath(endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Recherche avec objectif multiple +++");
		// il est aussi possible de faire une recherche en considérant
		// plusieurs destinations en même temps. l'algorithme s'arrête
		// dès qu'il rencontre une de ces destinations. c'est pratique
		// par exemple quand l'utilité maximale est atteinte par plusieurs
		// cases. la recherche par objectif multiple peut être utilisée
		// aussi bien lors d'une première recherche que quand on réutilise 
		// un arbre de recherche issu d'une recherche précédente (comme
		// ci-dessus).
		Set<AiTile> destinations = new TreeSet<AiTile>();
		destinations.add(zone.getTile(3,4));
		destinations.add(zone.getTile(5,3));
		try
		{	AiPath path = astar.processShortestPath(destinations);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Continuer une recherche déjà commencée +++");
		// si pour une raison quelconque le résultat trouvé ne vous plait
		// pas, il est possible de reprendre la recherche. notez que le
		// résultat suivant ne sera pas forcément optimal relativement à
		// la fonction de coût (seule la première solution trouvée l'est).
		// cette approche est utilisée dans l'exemple3, à la suite d'une
		// exception.
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
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 2 --------");
		// on réutilise les fonctions les plus simples pour cet exemple
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation de A* sans solution possible +++");
		// on applique l'algorithme pour trouver une chemin 
		// entre la position courante et une case de destination
		// mais cette fois il n'existe pas de solution
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		try
		{	AiPath path = astar.processShortestPath(startLocation,endTile);
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
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		// utilisation d'une limite
		System.out.println("\n\n-------- EXEMPLE 3 --------");
		// on réutilise les fonctions les plus simples pour cet exemple
		CostCalculator costCalculator = new TileCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new TileHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation de A* dépassant une des 3 limites (cout/hauteur/nombre de noeud de l'arbre de recherche) +++");
		// on modifie la limite de hauteur de l'arbre de manière à ne
		// pas chercher des chemins plus longs que 4 déplacements.
		astar.setMaxHeight(4);
		AiLocation startLocation = new AiLocation(hero);
		// on choisit pour l'exemple une destination est accessible, 
		// mais avec un chemin minimal de longueur supérieure à 4
		AiTile endTile = zone.getTile(3,4);
		AiPath path = null;
		try
		{	path = astar.processShortestPath(startLocation,endTile);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	//e.printStackTrace();
			// le fait de lever cette exception indique qu'A* a
			// développé un arbre de recherche trop grand : il existe
			// peut-être une solution, mais elle n'a pas été trouvée.
			System.out.println("+++ path="+path);
			System.out.println(e.getSummary());
		}
		
		System.out.println("+++ On relance la recherche en augmentant la limite +++");
		// on augmente la limite à 5 et on continue la recherche.
		astar.setMaxHeight(5);
		try
		{	// on continue le traitement en utilisant le même
			// arbre, pour les mêmes points de départ et de destination.
			path = astar.continueProcess();

			// remarque 1 : en réalisant l'appel
			// 		path = astar.processShortestPath(endTile);
			// on aurait aussi le même résultat, sauf que la recherche
			// serait repartie de la racine de l'arbre. on aurait quand
			// même réutilisé l'arbre existant, mais le traitement aurait
			// nécessité plus d'itérations.

			// remarque 2 : en réalisant l'appel
			// 		path = astar.processShortestPath(startLocation,endTile);
			// on aurait le même résultat, la recherche aurait aussi
			// recommencé depuis le début, mais cette fois sans réutiliser
			// l'arbre existant. on aurait construit un nouvel arbre,
			// donc le traitement aurait été encore plus long
			
			// remarque 3 : l'exemple donné ici n'est pas du tout
			// équivalent à un approfondissement itératif, qui n'est
			// pas implément dans l'API.
			
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
	 * Utilisation de fonctions basées sur des distances
	 * exprimées en pixels plutôt qu'en cases (comme
	 * c'était le cas dans l'exemple 1).
	 */
	private static void example4()
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		System.out.println("\n\n-------- EXEMPLE 4 --------");
		// on réutilise les fonctions utilisant les pixels
		// au lieu des cases pour calculer les coûts et heuristiques.
		// -> les calculs seront très légèrement plus longs, mais aussi plus précis.
		// on garde la même fonction successeur (basique) 
		CostCalculator costCalculator = new PixelCostCalculator(ai);
		HeuristicCalculator heuristicCalculator = new PixelHeuristicCalculator(ai);
		SuccessorCalculator successorCalculator = new BasicSuccessorCalculator(ai);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de pixels +++");
		// on peut utilise un point de départ différent de la position d'une case
		// par exemple ici, on laisse le point de départ dans la même case (1,1),
		// mais on l'a en plus déplacé lègèrement vers le bas (il donc plus situé
		// au milieu de la case). >> du coup cette fois c'est le chemin partant
		// vers le bas qui sera exploré en premier (et non pas celui partant vers
		// la droite comme c'était le cas avec les fonctions basées sur les cases).
		// remarquez aussi les différences dans les valeurs d'heuristique et de coût.
		AiLocation startLocation = new AiLocation(24,25,zone);
		AiTile endTile = zone.getTile(5,3);
		try
		{	AiPath path = astar.processShortestPath(startLocation,endTile);
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
	 * de prédire ce qui va se passer. l'api propose deux 
	 * modèles : l'un ({@link AiFullModel}) permet d'avoir une 
	 * représentation complète de la zone, mais cela demande un 
	 * traitement plus lourd. l'autre ({@link AiPartialModel}) 
	 * utilise une représentation simplifiée de la zone, mais 
	 * suffisante pour y chercher un chemin, ce qui permet d'alléger 
	 * les calculs. il existe des fonctions successeur basées sur 
	 * ces deux modèles, et des fonctions heuristique et de coût 
	 * basées sur le temps plutôt que sur la distance.
	 */
	private static void example5()
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		
		// utilisation des fonctions tenant compte du temps, 
		// avec modèle complet pour commencer
		System.out.println("\n\n-------- EXEMPLE 5 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,hero);
		// les fonctions successeurs basées sur les modèles peuvent être
		// paramétrées. en effet, la grosse différence avec les autres
		// fonctions successeur est qu'ici on considère non seulement
		// les actions de déplacement, mais aussi l'action d'attente.
		// cela permet par exemple d'attendre qu'une explosion voisine
		// se termine avant de changer de case. seulement, le fait de
		// traiter l'attente rend les calculs beaucoup plus longs. le
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
		// 		- MODE_ALL ne fixe aucune limite : une déjà explorée sera
		//		  ré-explorée si nécessaire, ce qui permet d'envisager
		//		  absolument tous les chemins possibles.
		SuccessorCalculator successorCalculator = new TimeFullSuccessorCalculator(ai,hero,TimeFullSuccessorCalculator.MODE_ALL);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_ALL +++");
		// ici on ne limite absolument pas l'exploration de l'arbre de recherche
		// on veut aller à la case (4,5), qui était considérée comme inaccessible
		// par les fonctions non-temporelles. notez que les coûts et valeurs heuristiques
		// sont maintenant des durées exprimées en ms. dans le chemin solution, une
		// valeur temporelle indique s'il faut juste traverser la case (0) ou bien
		// s'il est nécessaire d'attendre dans la case (>0). notez aussi qu'à la différence
		// des exemples précédents, maintenant la zone évolue à chaque itération
		// de l'algorithme de recherche. par exemple, à l'itération 4 la bombe explose.
		// notez combien le temps nécessaire au calcul est supérieur à ceux observés 
		// pour les exemples précédents. notez que cela est essentiellement dû au fait
		// que ce mode de recherche considère les retours en arrière. par exemple, dès
		// la deuxième itération, on envisage le fait de revenir dans la première case.
		// ceci s'explique par le fait que l'état de la 1ère case a peut-être changé
		// pendant le déplacement (n'oubliez pas que la zone est modélisée dynamiquement
		// ici), et donc peut n'être qu'un obstacle passé n'existe plus.
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		AiPath path = null;
		try
		{	path = astar.processShortestPath(startLocation,endTile);
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
		// case si elle est déjà présente dans la branche courante. La conséquence
		// est que le chemin trouvé ne peut pas contenir de retour en arrière ou 
		// de boucle. A noter qu'une action d'attente est considérée comme le début
		// d'une nouvelle branche, i.e. : on autorise les retours en arrière seulement
		// après une attente. L'action d'attendre n'est considérée que si elle permet
		// d'éviter une explosion ou un obstacle sur le point d'être détruit. Notez
		// que ce mode de recherche est beaucoup plus rapide que le précédent. Notez
		// aussi que le chemin contient un temps d'attente (à la fin) dû au fait que
		// la bombe bloque le passage, et qu'il est nécessaire d'attendre qu'elle
		// explose pour pouvoir atteindre la case de destination.
		successorCalculator = new TimeFullSuccessorCalculator(ai,hero,TimeFullSuccessorCalculator.MODE_NOBRANCH);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.processShortestPath(startLocation,endTile);
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
		successorCalculator = new TimeFullSuccessorCalculator(ai,hero,TimeFullSuccessorCalculator.MODE_NOTREE);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.processShortestPath(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		// l'intérêt d'utiliser un modèle complet est que la zone est
		// mise à jour à chaque itération. Du coup, la zone obtenue dans
		// le dernier noeud de recherche correspond à l'état de
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
	private static void example6()
	{	// on utilise ici une ai anonyme, mais vous pouvez utiliser la vôtre à la place
		ArtificialIntelligence ai = InitData.initAi();
		
		// on initialise la zone
		AiSimZone zone = InitData.initZone();
		ai.setZone(zone);
		AiHero hero = zone.getHeroByColor(PredefinedColor.WHITE);
		
		
		// utilisation des fonctions tenant compte du temps, 
		// avec modèle partiel
		System.out.println("\n\n-------- EXEMPLE 6 --------");
		CostCalculator costCalculator = new TimeCostCalculator(ai,hero);
		HeuristicCalculator heuristicCalculator = new TimeHeuristicCalculator(ai,hero);
		// les paramètres sont les mêmes que pour le modèle complet
		SuccessorCalculator successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_ALL);
		Astar astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle partiel, MODE_ALL +++");
		AiLocation startLocation = new AiLocation(hero);
		AiTile endTile = zone.getTile(4,5);
		AiPath path = null;
		try
		{	path = astar.processShortestPath(startLocation,endTile);
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
		successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOBRANCH);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.processShortestPath(startLocation,endTile);
			System.out.println("+++ path="+path);
		}
		catch (StopRequestException e)
		{	e.printStackTrace();
		}
		catch (LimitReachedException e)
		{	e.printStackTrace();
		}
		
		System.out.println("+++ Utilisation des fonctions à base de temps, modèle complet, MODE_NOTREE +++");
		successorCalculator = new TimePartialSuccessorCalculator(ai,TimePartialSuccessorCalculator.MODE_NOTREE);
		astar = new Astar(ai,hero,costCalculator,heuristicCalculator,successorCalculator);
		astar.setVerbose(true); // pour afficher les détails du traitement
		try
		{	path = astar.processShortestPath(startLocation,endTile);
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
