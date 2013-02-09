package org.totalboumboum.ai.v201314.adapter.agent;

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

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant le calcul des valeurs de préférence de l'agent.
 * En particulier, elle implémente la méthode
 * {@link #update} de l'algorithme général.
 * <br/>
 * Cette classe contient 2 variables qui sont mises à 
 * jour par {@code update} :
 * <ul>
 * 		<li>{@link #preferencesByTile} : map associant une valeur de préférence à chaque case traitée ;</li>
 * 		<li>{@link #preferencesByValue} : map associant à chaque valeur la liste des cases qui ont cette preference.</li>
 * </ul>
 * Ces variables contiennent les mêmes informations, mais présentées
 * différemment afin d'y accéder rapidement dans tous les cas. Par exemple,
 * si on connait la case et qu'on veut la préférence, on utilise la première map.
 * Si on veut toutes les cases de préférence maximale, on cherche la préférence maximale
 * dans les clés de la deuxième map, et on utilise cette même map pour
 * récupérer la liste des cases qui possèdent cette préférence.
 * <br/>
 * Elles sont notamment utilisées par la méthode {@link #updateOutput()}
 * qui est donnée ici en exemple afin d'afficher les valeurs de préférence courantes.
 * <br/>
 * Cette classe est aussi solicitée lors de la création de catégories et de critères,
 * afin de vérifier que leurs noms sont uniques pour cet agent (ceci afin
 * d'éviter toute confusion).
 * <br/>
 * Cette classe contient un système de cache utilisé lors du calcul des critères.
 * En effet, certains critères peuvent être utilisés à plusieurs reprises lors
 * d'une même itération. Afin de ne pas les recalculer à chaque fois, ils sont
 * donc stockés en mémoire, dans une structure qui est réinitialisée à chaque
 * itération.
 * 
 * @param <T> 
 * 		Classe de l'agent.
 * 
 * @author Vincent Labatut
 */
public abstract class AiPreferenceHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit être appelée par une classe héritant de 
	 * celle-ci, grâce au mot-clé {@code super}.
	 * <br/>
	 * A la différence des autres gestionnaires, l'initisation
	 * est réalisée dans une méthode séparée, qu'il faut penser
	 * à appeler avoir créé l'object. Il s'agit de la méthode
	 * {@link #init()}.
	 * 
	 * @param ai	
	 * 		L'agent que cette classe doit gérer.
	 */
	protected AiPreferenceHandler(T ai)
    {	super(ai);
    }
	
	/**
	 * Initialise le gestionnaire de préférence, en créant
	 * les structures nécessaires ainsi que les catégories,
	 * critères et combinaisons.
	 * <br/>
	 * Pour des raisons techniques, ces opérations
	 * ne peuvent pas être faites à l'instanciation
	 * de l'objet. Cette méthode est appelée automatique,
	 * vous (le concepteur de l'agent) ne devez pas l'invoquer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	final void init() throws StopRequestException
	{	print("    init preference handler");
   
//		// on initialise les maps de réference (vides pour l'instant)
//		createReference();
//		
//    	// on initialise les critères une fois pour toutes
//		{	long before = print("    > Entering initCriteria");
//	    	initCriteria();
//			long after = ai.getCurrentTime();
//			long elapsed = after - before;
//			print("    < Exiting initCriteria duration="+elapsed);
//		}
//		
//    	// on initialise les cas une fois pour toutes
//		{	long before = print("    > Entering initCases");
//			initCases();
//			long after = ai.getCurrentTime();
//			long elapsed = after - before;
//			print("    < Exiting initCases duration="+elapsed);
//		}
//		
//    	// on initialise les préférences de réference une fois pour toutes
//		{	long before = print("    > Entering initReference");
//			initReferenceUtilities();
//			long after = ai.getCurrentTime();
//			long elapsed = after - before;
//			print("    < Exiting initReference duration="+elapsed);
//		}
//		
//		// on calcule les préférences maximales une fois pour toutes
//		{	long before = print("    > Entering initMaxPreferences");
//    		initMaxPreferences();
//			long after = ai.getCurrentTime();
//			long elapsed = after - before;
//			print("    < Exiting initMaxPreferences duration="+elapsed);
//		}
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Réinitialise les structures de données
	 * modifiées à chaque itération. Cette méthode
	 * ne traite que les structures de données imposées.
	 * Si le concepteur veut réinitialiser ses propres
	 * structures de données, il doit surcharger la méthode
	 * {@link #resetCustomData()}.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 */
	private final void resetData()
	{	// le cache des critères
		criterionCache.clear();
		
		// les valeurs de préférence précédemment calculées.
		preferencesByTile.clear();
		preferencesByValue.clear();
	}
	
	/**
	 * Réinitialise les structures de données
	 * modifiées à chaque itération. Cette méthode concerne
	 * seulement les structures de données créées par le 
	 * concepteur : les structures imposées sont réinitialisées
	 * par {@link #resetData()}.
	 * <br/>
	 * Cette méthode est appelée automatiquement, vous (le 
	 * concepteur de l'agent) n'avez pas besoin de l'appeler.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void resetCustomData() throws StopRequestException
	{
		// peut être surchargée par le concepteur de l'agent
	}
	
	/////////////////////////////////////////////////////////////////
	// PREFERENCES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant les valeurs de préférences (seulement pour les cases sélectionnées) */
	private final Map<AiTile,Integer> preferencesByTile = new HashMap<AiTile,Integer>();
	/** Map contenant les cases rangées par valeur de préférence */
	private final Map<Integer,List<AiTile>> preferencesByValue = new HashMap<Integer,List<AiTile>>();

	/**
	 * Renvoie les préférences courantes, rangées par case.
	 * Ces valeurs sont recalculées à chaque itération.
	 * 
	 * @return
	 * 		Une map contenant les préférences rangées par case.
	 */
	public final Map<AiTile, Integer> getPreferencesByTile()
	{	return preferencesByTile;
	}

	/**
	 * Renvoie les préférences courantes, rangées par valeur.
	 * Ces valeurs sont recalculées à chaque itération.
	 * 
	 * @return
	 * 		Une map contenant les préférences rangées par valeur.
	 */
	public final Map<Integer, List<AiTile>> getPreferencesByValue()
	{	return preferencesByValue;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de mettre à jour
	 * les valeurs de préférence de l'agent. Ces valeurs
	 * doivent être placées dans les deux maps {@link #preferencesByTile}
	 * et {@link #preferencesByValue}. Le calcul de ces valeurs
	 * est fonction de la zone, mais aussi du mode
	 * courant de l'agent.
	 * <br/>
	 * Cette méthode implémente l'algorithme imposé et
	 * ne peut donc être ni modifiée, ni surchargée.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected final void update() throws StopRequestException
	{	ai.checkInterruption();
		PredefinedColor color = ai.getZone().getOwnHero().getColor();
	
		// on vide les structures
		print("    Reset data structures");
		resetData();
		resetCustomData();
		
		// on récupère le mode courant
		AiMode mode = ai.getModeHandler().getMode();
		
		// on sélectionne les cases dont on veut calculer la préférence
		long before = print("    > Entering selectTiles");
		ArrayList<AiTile> selectedTiles = new ArrayList<AiTile>(selectTiles());
		Collections.shuffle(selectedTiles); // on désordonne les cases pour introduire du hasard
		long after = ai.getCurrentTime();
		long elapsed = after - before;
		print("    < Exiting selectTiles duration="+elapsed+" number="+selectedTiles.size());
		
		// on calcule la préférence de chaque case
		before = print("    > Processing each tile");
		for(AiTile tile: selectedTiles)
		{	ai.checkInterruption();
			print("      > Processing tile "+tile);
			
			// on identifie la catégorie de cette case (en fonction du mode)
			print("        > Identifying the category");
			AiCategory category = identifyCategory(tile);
			if(category == null)
				throw new NullPointerException("The value returned by the method identifyCategory@"+color+" is null. It should not.");
			print("        < category="+category);
			
			// on identifie la combinaison de valeurs des critères pour la catégorie détectée
			print("        > Processing the combination");
			AiCombination combination = category.processCombination(tile);
			if(combination == null)
				throw new NullPointerException("The value returned by the method processCombination@"+color+" (for category "+category.getName()+") is null. It should not.");
			print("        < combination="+combination);
			
			// on calcule la valeur de préférence correspondant à cette combinaison (en fonction de son rang)
			int preference = retrievePreferenceValue(mode, combination);
			print("        Result: preference="+preference);
			
			// on la rajoute dans les structures
			preferencesByTile.put(tile,preference);
			List<AiTile> tiles = preferencesByValue.get(preference);
			if(tiles==null)
			{	tiles = new ArrayList<AiTile>();
				preferencesByValue.put(preference,tiles);
			}
			tiles.add(tile);
			print("      < Tile "+tile+" processing finished");
		}
		after = ai.getCurrentTime();
		elapsed = after - before;
		print("    < Tile processing finished duration="+elapsed);
	}
	
	/**
	 * Effectue une sélection sur les cases de la zone de jeu,
	 * car il n'est pas forcément nécessaire de calculer
	 * la préférence de chacune d'entre elles. Dans la méthode
	 * {@link #update()}, la préférence sera calculée seulement
	 * pour cette sélection.
	 * 
	 * @return
	 * 		L'ensemble des cases dont on veut calculer la préférence.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract Set<AiTile> selectTiles() throws StopRequestException;

	/////////////////////////////////////////////////////////////////
	// CRITERIA	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map de tous les critères créés, à ne surtout pas modifier manuellement */
	private final Map<String,AiCriterion<T,?>> criterionMap = new HashMap<String,AiCriterion<T,?>>();

	/**
	 * Ajoute un nouveau critère à la map.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param criterion
	 * 		Le critère à ajouter.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Si un critère de même nom existe déjà.
	 */
	final void insertCriterion(AiCriterion<T, ?> criterion) throws IllegalArgumentException
	{	String name = criterion.getName();
		if(criterionMap.keySet().contains(name))
			throw new IllegalArgumentException("A criterion with the same name '"+name+"' already exists for this agent.");
		criterionMap.put(name,criterion);
	}
	
	/**
	 * Renvoie le critère dont le nom
	 * est passé en paramètre (s'il existe).
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param name
	 * 		Nom du critère demandé. 
	 * @return 
	 * 		Le critère correspondant, ou {@code null} s'il n'existe pas.
	 */
	final AiCriterion<T,?> getCriterion(String name)
	{	AiCriterion<T,?> result = criterionMap.get(name);
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CATEGORIES	/////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant toutes les catégories, à ne surtout pas modifier manuellement */
	private final Map<String,AiCategory> categoryMap = new HashMap<String,AiCategory>();
	
	/**
	 * Ajoute une nouvelle catégorie à la map.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param category
	 * 		La catégorie à ajouter.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Si une catégorie de même nom existe déjà.
	 */
	final void insertCategory(AiCategory category) throws IllegalArgumentException
	{	String name = category.getName();
		if(categoryMap.keySet().contains(name))
			throw new IllegalArgumentException("A category with the same name '"+name+"' already exists for this agent.");
		categoryMap.put(name,category);
	}
	
	/**
	 * Renvoie la catégorie dont le nom
	 * est passé en paramètre (si elle
	 * existe).
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param name
	 * 		Nom de la catégorie demandée. 
	 * @return 
	 * 		La catégorie correspondant, ou {@code null} si elle n'existe pas.
	 */
	final AiCategory getCategory(String name)
	{	AiCategory result = categoryMap.get(name);
		return result;
	}

	/**
	 * Cette méthode prend une case en paramètre, et identifie
	 * la catégorie correspondante. Bien sûr le traitement dépend
	 * à la fois de la zone de jeu et du mode courant, qui est
	 * accessible grâce à la méthode {@link ArtificialIntelligence#getModeHandler()}.
	 * <br/>
	 * Vous devez définir cette méthode, mais vous n'avez (a
	 * priori) pas besoin de l'utiliser, elle est automatiquement
	 * appelée quand elle est nécessaire.
	 * 
	 * @param tile
	 * 		La case dont on veut identifier la catégorie.
	 * @return
	 * 		Un objet représentant la catégorie correspondant à cette case.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract AiCategory identifyCategory(AiTile tile) throws StopRequestException;

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant la préférence de chaque combinaison, , à ne surtout pas modifier manuellement */
	private final Map<AiMode, List<AiCombination>> referencePreferences = new HashMap<AiMode, List<AiCombination>>();
	
	/**
	 * Renvoie la valeur de préférence associée à la
	 * combinaison passée en paramètre. Si aucune
	 * valeur de préférence ne lui a été associée dans
	 * {@link #referencePreferences}, alors la
	 * méthode lève une {@code IllegalArgumentException}.
	 * <br/>
	 * Une même catégorie peut être utilisée dans plusieurs modes
	 * différents, il est donc nécessaire de spécifier
	 * en paramètre le mode qui caractérise la combinaison
	 * à traiter.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) n'avez (a priori)
	 * pas besoin de l'utiliser.
	 * 
	 * @param mode
	 * 		Le mode caractérisant cette combinaison.
	 * @param combination
	 * 		La combinaison dont on veut la préférence.
	 * @return
	 * 		La préférence de la combinaison passée en paramètre,
	 * 		pour le mode spécifié.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si la combinaison passée en paramètre est introuvable dans {@link #referencePreferences}.
	 */
	final int retrievePreferenceValue(AiMode mode, AiCombination combination)
	{	List<AiCombination> list = referencePreferences.get(mode);
		Integer result = list.indexOf(combination);
		if(result==-1)
		{	PredefinedColor color = ai.getZone().getOwnHero().getColor();
			throw new IllegalArgumentException("No preference value was associated to the specified combination (combination="+combination+", player="+color+").");
		}
		return result;
	}
	
	/**
	 * Ajoute la combinaison passée en paramètre à la liste interne de
	 * ce gestionnaire, pour le mode spécifié. L'ordre dans lequel la 
	 * combinaison est insérée détermine la préférence de l'agent. La 
	 * première combinaison insérée étant la préférée.
	 * <br/>
	 * Si la combinaison a déjà été insérée pour ce mode, alors une
	 * {@link IllegalArgumentException} est levée.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) n'avez (a priori)
	 * pas besoin de l'utiliser.
	 * 
	 * @param mode
	 * 		Le mode caractérisant cette combinaison.
	 * @param combination
	 * 		La combinaison à insérer.
	 * 
	 * @throws IllegalArgumentException 
	 * 		Si une combinaison similaire existe déjà pour le mode spécifié.
	 */
	final void insertCombination(AiMode mode, AiCombination combination) throws IllegalArgumentException
	{	List<AiCombination> list = referencePreferences.get(mode);
		if(list.contains(combination))
			throw new IllegalArgumentException("Trying to insert combination '"+combination+"' for mode '"+mode+"', but this combination is already present (a combination cannot appear twice in the preferences).");
		list.add(combination);
	}
	
	/////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cache stockant temporairement les résultats du calcul des critères. Il est réinitialisé à chaque itération */
	private final Map<String,Map<AiTile,Object>> criterionCache = new HashMap<String, Map<AiTile,Object>>();
	
	/**
	 * Méthode permettant de rechercher si un critère a déjà été calculé lors
	 * de cette itération. Si c'est le cas, on ne le recalcule pas : le cache
	 * nous renvoie la valeur précédemment calculée. Sinon, le cache renvoie 
	 * {@code null}.
	 * <br/>
	 * Cette méthode est automatiquement utilisée par les classes héritant
	 * de {@link AiCriterion}. Vous (le concepteur de l'agent) n'avez
	 * (a priori) pas besoin de l'utiliser.
	 * 
	 * @param name
	 * 		Nom (unique) du critère à calculer.
	 * @param tile
	 * 		Case pour laquelle on veut calculer le critère.
	 * @return
	 * 		Valeur du critère, si elle existe (sinon : {@code null}).
	 */
	final Object getValueForCriterion(String name, AiTile tile)
	{	Object result = null;
		Map<AiTile,Object> map = criterionCache.get(name);
		if(map!=null)
			result = map.get(tile);
		return result;
	}
	
	/**
	 * Méthode permettant d'insérer une valeur dans le cache. Cette opération
	 * est réalisée quand cette valeur n'a pas été trouvée dans le cache.
	 * <br/>
	 * Cette méthode est automatiquement utilisée par les classes héritant
	 * de {@link AiCriterion}. Vous (le concepteur de l'agent) n'avez
	 * (a priori) pas besoin de l'utiliser.
	 * 
	 * @param name
	 * 		Nom (unique) du critère calculé.
	 * @param tile
	 * 		Case pour laquelle on a calculé le critère.
	 * @param value
	 * 		Valeur du critère.
	 */
	final void putValueForCriterion(String name, AiTile tile, Object value)
	{	Map<AiTile,Object> map = criterionCache.get(name);
		if(map==null)
		{	map = new HashMap<AiTile, Object>();
			criterionCache.put(name, map);
		}
		map.put(tile,value);
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Détermine si le gestionnaire colorie les cases dans la sortie graphique */ 
	public boolean outputColors = true;
	/** Détermine si le gestionnaire affiche du texte dans la sortie graphique */ 
	public boolean outputText = true;
	
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant
	 * les données de ce gestionnaire.
	 * <br/>
	 * Ici, on affiche la valeur numérique de la préférence dans chaque
	 * case, et on colorie la case en fonction de cette valeur : la couleur
	 * dépend du mode (bleu pour la collecte, rouge pour l'attaque)
	 * et son intensité dépend de la préférence (clair pour une préférence
	 * faible, foncé pour une préférence élevée).
	 * <br/>
	 * Cette méthode peut être surchargée si vous voulez afficher
	 * les informations différemment, ou d'autres informations. A
	 * noter que cette méthode n'est pas appelée automatiquement : 
	 * elle doit l'être par la fonction surchargeant 
	 * {@link ArtificialIntelligence#updateOutput()}
	 * si vous désirez l'utiliser. 
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		AiMode mode = ai.getModeHandler().getMode();

		int rCoeff = 1;
		int gCoeff = 1;
		int bCoeff = 1;
		if(mode==AiMode.ATTACKING)
			rCoeff = 0;
		else if(mode==AiMode.COLLECTING)
			bCoeff = 0;
		Integer limit = referencePreferences.get(mode).size();
		if(limit==0)
			limit = 50;
		AiOutput output = ai.getOutput();
		
		for(Entry<AiTile,Integer> entry: preferencesByTile.entrySet())
		{	ai.checkInterruption();
			AiTile tile = entry.getKey();
			int value = entry.getValue();
			
			// text
			if(outputText)
			{	String text = "-\u221E";
				if(value!=Integer.MAX_VALUE)
					text = Integer.toString(value);
				output.setTileText(tile,text);
			}
			
			// color
			if(outputColors)
			{	if(value<0)
					value = 0;
				else if(value>limit)
					value = limit;
				int r = 255 - rCoeff*(int)((limit-value)/limit*255);
				int g = 255 - gCoeff*(int)((limit-value)/limit*255);
				int b = 255 - bCoeff*(int)((limit-value)/limit*255);
				Color color = new Color(r,g,b);
				output.addTileColor(tile,color);
			}
		}
	}
	
	/**
	 * Affiche une représentation textuelle des préférences
	 * chargées à partir du fichier XML défini dans le
	 * package de l'agent.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public final void displayPreferences() throws StopRequestException
	{	ai.checkInterruption();
		print("    > Declared preferences :");
		
		for(AiMode mode: AiMode.values())
		{	print("      > Mode: ----- " + mode + " -------------------------------");
			final List<AiCombination> list = referencePreferences.get(mode);
			for(int i=0;i<list.size();i++)
				print("      "+i+"."+list.get(i));
			print("      < Mode " + mode + " done");
		}
		print("    < Preferences done.");
	}
}
