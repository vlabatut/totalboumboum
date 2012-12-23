package org.totalboumboum.ai.v201213.adapter.agent;

/*
 * Total Boum Boum
 * Copyright 2008-2012 Vincent Labatut 
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
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.tools.images.PredefinedColor;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * En particulier, elle implémente la méthode
 * {@link #update} de l'algorithme général.
 * <br/>
 * Cette classe contient 2 variables qui sont mises à 
 * jour par {@code update} :
 * <ul>
 * 		<li>{@link #utilitiesByTile} : map associant une valeur d'utilité à chaque case accessible ;</li>
 * 		<li>{@link #utilitiesByValue} : map associant à chaque valeur la liste des cases qui ont cette utilité.</li>
 * </ul>
 * Ces variables contiennent les mêmes informations, mais présentées
 * différemment afin d'y accéder rapidement dans tous les cas. Par exemple,
 * si on connait la case et qu'on veut l'utilité, on utilise la première map.
 * Si on veut toutes les cases d'utilité maximale, on cherche l'utilité maximale
 * dans les clés de la deuxième map, et on utilise cette même map pour
 * récupérer la liste des cases qui possèdent cette utilité.
 * <br/>
 * Elles sont notamment utilisées par la méthode {@link #updateOutput()}
 * qui est donnée ici en exemple afin d'afficher les valeurs d'utilité courantes.
 * <br/>
 * Cette classe est aussi solicitée lors de la création de cas et de critères,
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
public abstract class AiUtilityHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
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
	protected AiUtilityHandler(T ai)
    {	super(ai);
    }
	
	/**
	 * Initialise le gestionnaire d'utilité, en créant
	 * les structures nécessaires ainsi que les cas,
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
	{	print("    init utility handler");
   
		// on initialise les maps de réference (vides pour l'instant)
		createReference();
		
    	// on initialise les critères une fois pour toutes
		{	long before = print("    > Entering initCriteria");
	    	initCriteria();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < Exiting initCriteria duration="+elapsed);
		}
		
    	// on initialise les cas une fois pour toutes
		{	long before = print("    > Entering initCases");
			initCases();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < Exiting initCases duration="+elapsed);
		}
		
    	// on initialise les utilités de réference une fois pour toutes
		{	long before = print("    > Entering initReference");
			initReferenceUtilities();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < Exiting initReference duration="+elapsed);
		}
		
		// on calcule les utilités maximales une fois pour toutes
		{	long before = print("    > Entering initMaxUtilities");
    		initMaxUtilities();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < Exiting initMaxUtilities duration="+elapsed);
		}

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
		
		// les valeurs d'utilité précédemment calculées.
		utilitiesByTile.clear();
		utilitiesByValue.clear();
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
	// UTILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant les valeurs d'utilité (les cases absentes sont inutiles) */
	private final Map<AiTile,Float> utilitiesByTile = new HashMap<AiTile,Float>();
	/** Map contenant les cases rangées par valeur d'utilité */
	private final Map<Float,List<AiTile>> utilitiesByValue = new HashMap<Float,List<AiTile>>();

	/**
	 * Renvoie les utilités courantes, rangées par case.
	 * Ces valeurs sont recalculées à chaque itération.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par case.
	 */
	public final Map<AiTile, Float> getUtilitiesByTile()
	{	return utilitiesByTile;
	}

	/**
	 * Renvoie les utilités courantes, rangées par valeur.
	 * Ces valeurs sont recalculées à chaque itération.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par valeur.
	 */
	public final Map<Float, List<AiTile>> getUtilitiesByValue()
	{	return utilitiesByValue;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de mettre à jour
	 * les valeurs d'utilité de l'agent. Ces valeurs
	 * doivent être placées dans les deux maps {@link #utilitiesByTile}
	 * et {@link #utilitiesByValue}. Le calcul de ces valeurs
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
		
		// on sélectionne les cases dont on veut calculer l'utilité
		long before = print("    > Entering selectTiles");
		ArrayList<AiTile> selectedTiles = new ArrayList<AiTile>(selectTiles());
		Collections.shuffle(selectedTiles); // on désordonne les cases pour introduire du hasard
		long after = ai.getCurrentTime();
		long elapsed = after - before;
		print("    < Exiting selectTiles duration="+elapsed+" number="+selectedTiles.size());
		
		// on calcule l'utilité de chaque case
		before = print("    > Processing each tile");
		for(AiTile tile: selectedTiles)
		{	ai.checkInterruption();
			print("      > Processing tile "+tile);
			
			// on identifie le cas de cette case (en fonction du mode)
			print("        > Identifying the case");
			AiUtilityCase caze = identifyCase(tile);
			if(caze == null)
				throw new NullPointerException("The value returned by the method identifyCase@"+color+" is null. It should not.");
			print("        < case="+caze);
			
			// on identifie la combinaison de valeurs des critères pour le cas détecté
			print("        > Processing the combination");
			AiUtilityCombination combination = caze.processCombination(tile);
			if(combination == null)
				throw new NullPointerException("The value returned by the method processCombination@"+color+" (for case "+caze.getName()+") is null. It should not.");
			print("        < combination="+combination);
			
			// on calcule la valeur d'utilité correspondant à cette combinaison (en fonction de son rang)
			float utility = retrieveUtilityValue(mode, combination);
			print("        Result: utility="+utility);
			
			// on la rajoute dans les structures
			utilitiesByTile.put(tile,utility);
			List<AiTile> tiles = utilitiesByValue.get(utility);
			if(tiles==null)
			{	tiles = new ArrayList<AiTile>();
				utilitiesByValue.put(utility,tiles);
			}
			tiles.add(tile);
			print("      < Tile "+tile+" processing finished");
		}
		after = ai.getCurrentTime();
		elapsed = after - before;
		print("    < Tile processing finished duration="+elapsed);
	}
	
	/**
	 * Effectue une sélection sur les case de la zone de jeu,
	 * car il n'est pas forcément nécessaire de calculer
	 * l'utilité de chacune d'entre elles. Dans la méthode
	 * {@link #update()}, l'utilité sera calculée seulement
	 * pour cette sélection.
	 * 
	 * @return
	 * 		L'ensemble des cases dont on veut calculer l'utilité.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract Set<AiTile> selectTiles() throws StopRequestException;

	/////////////////////////////////////////////////////////////////
	// CRITERIA	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map de tous les critères créés, à ne surtout pas modifier manuellement */
	protected final Map<String,AiUtilityCriterion<?,?>> criterionMap = new HashMap<String,AiUtilityCriterion<?,?>>();
	// TODO à rendre privé + méthode d'accès en lecture seule (pour éviter tout pb de modification abusive)

	/**
	 * Vérifie si un critère portant le nom passé
	 * en paramètre existe déjà.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param name
	 * 		Nom à tester.
	 * @return
	 * 		{@code true} ssi un critère de ce nom existe déjà.
	 */
	final boolean checkCriterionName(String name)
	{	boolean result = criterionMap.keySet().contains(name);
		return result;
	}
	
	/**
	 * Ajoute un nouveau critère à la map.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param criterion
	 * 		Le critère à ajouter.
	 */
	final void insertCriterion(AiUtilityCriterion<?, ?> criterion)
	{	String name = criterion.getName();
		criterionMap.put(name,criterion);
	}
	
	/**
	 * Initialise tous les critères nécessaires à la définitions
	 * des cas pour cet agent. Ces critères sont automatiquement
	 * stockés dans la map {@link #criterionMap} (les clés 
	 * {@code String} correspondent aux noms des critères).
	 * <br/>
	 * Bien entendu, cette initialisation est réalisée une seule
	 * fois lors de la création de l'agent. On suppose donc
	 * que les critères sont constants au cours d'une partie.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract void initCriteria() throws StopRequestException;
	
	/////////////////////////////////////////////////////////////////
	// CASES	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant tous les cas, à initialiser dans {@link #initCases} */
	protected final Map<String,AiUtilityCase> caseMap = new HashMap<String,AiUtilityCase>();
	// TODO à rendre privé + méthode d'accès en lecture seule (pour éviter tout pb de modification abusive)
	
	/**
	 * Vérifie si un cas portant le nom passé
	 * en paramètre existe déjà.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param name
	 * 		Nom à tester.
	 * @return
	 * 		{@code true} ssi un cas de ce nom existe déjà.
	 */
	final boolean checkCaseName(String name)
	{	boolean result = caseMap.keySet().contains(name);
		return result;
	}

	/**
	 * Ajoute un nouveau cas à la map.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 * 
	 * @param caze
	 * 		Le cas à ajouter.
	 */
	final void insertCase(AiUtilityCase caze)
	{	String name = caze.getName();
		caseMap.put(name,caze);
	}
	
	/**
	 * Initialise tous les cas nécessaires à cet agent. Ces 
	 * cas sont automatiquement stockés dans la map 
	 * {@link #caseMap} (les clés {@code String} correspondent 
	 * aux noms des cas). Cette map contient tous les cas
	 * sans distinction de mode (i.e. à la fois les cas de
	 * collecte et ceux d'attaque).
	 * <br/>
	 * Bien entendu, cette initialisation est réalisée une seule
	 * fois lors de la création de l'agent. On suppose donc
	 * que les critères sont constants au cours d'une partie.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract void initCases() throws StopRequestException;
	
	/**
	 * Cette méthode prend une case en paramètre, et identifie
	 * le cas correspondant. Bien sûr le traitement dépend
	 * à la fois de la zone de jeu et du mode courant, qui est
	 * accessible grâce à la méthode {@link ArtificialIntelligence#getModeHandler()}.
	 * <br/>
	 * Vous devez définir cette méthode, mais vous n'avez (a
	 * priori) pas besoin de l'utiliser, elle est automatiquement
	 * appelée quand elle est nécessaire.
	 * 
	 * @param tile
	 * 		La case dont on veut identifier le cas.
	 * @return
	 * 		Un objet représentant le cas correspondant à cette case.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract AiUtilityCase identifyCase(AiTile tile) throws StopRequestException;

	/////////////////////////////////////////////////////////////////
	// REFERENCE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant l'utilité de chaque combinaison, à initialiser dans {@link #initReferenceUtilities} */
	private final Map<AiMode, Map<AiUtilityCombination,Integer>> referenceUtilities = new HashMap<AiMode, Map<AiUtilityCombination,Integer>>();
	/** Indique l'utilité maximale pour chaque mode (map automatiquement initialisée) */
	private final Map<AiMode,Integer> maxReferenceUtilities = new HashMap<AiMode,Integer>();
	
	/**
	 * Crée une map vide pour chaque mode. Ces
	 * maps devront être remplies par la méthode
	 * {@link #initReferenceUtilities}.
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 */
	private final void createReference()
	{	// pour chaque mode, on crée une map vide,
		// qui devra être remplie par initReference()
		for(AiMode mode: AiMode.values())
		{	HashMap<AiUtilityCombination, Integer> map = new HashMap<AiUtilityCombination, Integer>();
			referenceUtilities.put(mode,map);
		}
	}
	
	/**
	 * Crée toutes les combinaisons possibles pour les
	 * cas définis dans {@link #initCases()}, et les
	 * insère dans la map {@link #referenceUtilities} en
	 * associant à chacune une utilité unique.
	 * <br/>
	 * Les cas peuvent être récupérés à partir de leur
	 * nom, en utilisant la map {@link #caseMap}.
	 * <br/>
	 * <b>Important</b> : utilisez la méthode {@link #defineUtilityValue}
	 * pour insérer une combinaison et sa valeur d'utilité dans
	 * la map {@link #referenceUtilities}. Ne le faites pas directement.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract void initReferenceUtilities() throws StopRequestException;
	
	/**
	 * Calcule l'utilité de réference maximale pour chaque mode.
	 * Ces valeurs sont utilisées lors de l'initialisation de
	 * la sortie graphique (pour normaliser les valeurs d'utilité)
	 * <br/>
	 * Cette méthode est destinée à un usage interne,
	 * vous (le concepteur de l'agent) ne devez pas l'utiliser.
	 */
	private final void initMaxUtilities()
	{	for(AiMode mode: AiMode.values())
		{	Map<AiUtilityCombination,Integer> map = referenceUtilities.get(mode);
			Collection<Integer> utilities = map.values();
			int max = Collections.max(utilities);
			maxReferenceUtilities.put(mode, max);
		}
	}
	
	/**
	 * Renvoie la valeur d'utilité associée à la
	 * combinaison passée en paramètre. Si aucune
	 * valeur d'utilité ne lui a été associée dans
	 * {@link #referenceUtilities}, alors la
	 * méthode lève une {@code IllegalArgumentException}.
	 * <br/>
	 * Un même cas peut être utilisé dans plusieurs modes
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
	 * 		La combinaison dont on veut l'utilité.
	 * @return
	 * 		L'utilité de la combinaison passée en paramètre,
	 * 		pour le mode passé en paramètre.
	 * 
	 * @throws IllegalArgumentException
	 * 		Si la combinaison passée en paramètre est introuvable dans {@link #referenceUtilities}.
	 */
	protected final int retrieveUtilityValue(AiMode mode, AiUtilityCombination combination)
	{	Map<AiUtilityCombination,Integer> map = referenceUtilities.get(mode);
		Integer result = map.get(combination);
		if(result==null)
			throw new IllegalArgumentException("No utility value was associated to the specified combination ("+combination+").");
		return result;
	}
	
	/**
	 * Définit la valeur d'utilité de la combinaison passée en paramètre,
	 * relativement au mode spécifié. Si une valeur a été déjà définie
	 * pour ce mode et cette combinaison, alors l'ancienne valeur est
	 * remplacée par celle spécifiée ici.
	 * <br/>
	 * Vous devez obligatoirement utiliser cette méthode pour insérer
	 * vos valeurs d'utilité dans {@link #referenceUtilities}, normalement
	 * à partir de la méthode {@link #initReferenceUtilities()}.
	 * 
	 * @param mode
	 * 		Le mode caractérisant cette combinaison.
	 * @param combination
	 * 		La combinaison dont on veut l'utilité.
	 * @param utility
	 * 		L'utilité de la combinaison passée en paramètre, 
	 * 		pour le mode passé en paramètre.
	 */
	protected final void defineUtilityValue(AiMode mode, AiUtilityCombination combination, int utility)
	{	Map<AiUtilityCombination,Integer> map = referenceUtilities.get(mode);
		map.put(combination,utility);
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
	 * de {@link AiUtilityCriterion}. Vous (le concepteur de l'agent) n'avez
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
	 * de {@link AiUtilityCriterion}. Vous (le concepteur de l'agent) n'avez
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
	 * Ici, on affiche la valeur numérique de l'utilité dans chaque,
	 * et on colorie la case en fonction de cette valeur : la couleur
	 * dépend du mode (bleu pour la collecte, rouge pour l'attaque)
	 * et son intensité dépend de l'utilité (clair pour une utilité
	 * faible, foncé pour une utilité élevée).
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
		NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		int rCoeff = 1;
		int gCoeff = 1;
		int bCoeff = 1;
		if(mode==AiMode.ATTACKING)
			rCoeff = 0;
		else if(mode==AiMode.COLLECTING)
			bCoeff = 0;
		Integer limit = maxReferenceUtilities.get(mode);
		if(limit==0)
			limit = 50;
		AiOutput output = ai.getOutput();
		
		for(Entry<AiTile,Float> entry: utilitiesByTile.entrySet())
		{	ai.checkInterruption();
			AiTile tile = entry.getKey();
			float value = entry.getValue();
			
			// text
			if(outputText)
			{	String text = "-\u221E";
				if(value!=Long.MAX_VALUE)
					text = nf.format(value);
				output.setTileText(tile,text);
			}
			
			// color
			if(outputColors)
			{	if(value<0)
					value = 0;
				else if(value>limit)
					value = limit;
				int r = 255 - rCoeff*(int)(value/limit*255);
				int g = 255 - gCoeff*(int)(value/limit*255);
				int b = 255 - bCoeff*(int)(value/limit*255);
				Color color = new Color(r,g,b);
				output.addTileColor(tile,color);
			}
		}
	}
	
	/**
	 * Affiche une représentation textuelle des utilités déclarées.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public final void displayUtilities() throws StopRequestException
	{	ai.checkInterruption();
		print("    > Declared utilities :");
		
		for(AiMode mode: AiMode.values())
		{	print("      > Mode: ----- " + mode + " -------------------------------");
			final Map<AiUtilityCombination,Integer> map = referenceUtilities.get(mode);
			List<AiUtilityCombination> combis = new ArrayList<AiUtilityCombination>(map.keySet());
			Collections.sort(combis,new Comparator<AiUtilityCombination>()
			{	@Override
				public int compare(AiUtilityCombination o1, AiUtilityCombination o2)
				{	int u1 = map.get(o1);
					int u2 = map.get(o2);
					int result = u1 - u2;
					return result;
				}	
			});
			
			for(AiUtilityCombination combi: combis)
			{	int utility = map.get(combi);
				print("      "+utility+"."+combi);
			}
			print("      < Mode " + mode + " done");
		}
		print("    < Utilities done");
	}
}
