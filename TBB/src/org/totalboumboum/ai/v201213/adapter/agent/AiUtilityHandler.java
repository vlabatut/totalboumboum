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
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201213.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * En particulier, elle implémente la méthode
 * {@link #update} de l'algorithme général.
 * <br/>
 * Cette classe contient 2 variables qui doivent être 
 * obligatoirement être mises à jour par {@code update} :
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
 * 
 * TODO mentionner le cache pour les critères
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
	 * Cette méthode doit être appelée par une classe héritant de celle-ci
	 * grâce au mot-clé {@code super}.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected AiUtilityHandler(T ai) throws StopRequestException
    {	super(ai);
		print("    init utility handler");
   
		// on initialise les maps de réference
		createReference();
		
    	// on initialise les critères une fois pour toutes
		{	long before = print("    > Entering initCriteria");
	    	initCriteria();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < Exiting initCriteria duration="+elapsed);
		}
		
    	// on initialise les cas une fois pour toutes
		{	long before = print("    > Entering initCases");
			initCases();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < Exiting initCases duration="+elapsed);
		}
		
    	// on initialise les utilités de réference une fois pour toutes
		{	long before = print("    > Entering initReference");
			initReferenceUtilities();
			long after = System.currentTimeMillis();
			long elapsed = after - before;
			print("    < Exiting initReference duration="+elapsed);
		}
		
		// on calcule les utilités maximales une fois pour toutes
		{	long before = print("    > Entering initMaxUtilities");
    		initMaxUtilities();
			long after = System.currentTimeMillis();
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
	 */
	protected final void resetData()
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected final void resetCustomData() throws StopRequestException
	{
		// peut être surchargée par le concepteur de l'agent
	}
	
	/////////////////////////////////////////////////////////////////
	// UTILITIES		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant les valeurs d'utilité (les cases absentes sont inutiles) */
	protected final HashMap<AiTile,Float> utilitiesByTile = new HashMap<AiTile,Float>();
	/** Map contenant les cases rangées par valeur d'utilité */
	protected final HashMap<Float,List<AiTile>> utilitiesByValue = new HashMap<Float,List<AiTile>>();

	/**
	 * Renvoie les utilités courantes, rangées par case.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par case.
	 */
	public final HashMap<AiTile, Float> getUtilitiesByTile()
	{	return utilitiesByTile;
	}

	/**
	 * Renvoie les utilités courantes, rangées par valeur.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par valeur.
	 */
	public final HashMap<Float, List<AiTile>> getUtilitiesByValue()
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
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected final void update() throws StopRequestException
	{	ai.checkInterruption();
		// on vide les structures
		print("    Reset data structures");
		resetData();
		resetCustomData();
		
		// on sélectionne les cases dont on veut calculer l'utilité
		long before = print("    > Entering selectTiles");
		ArrayList<AiTile> selectedTiles = new ArrayList<AiTile>(selectTiles());
		Collections.shuffle(selectedTiles); // on désordonne les cases pour introduire du hasard
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		print("    < Exiting selectTiles duration="+elapsed+"number="+selectedTiles.size());
		
		// on calcule l'utilité de chaque case
		before = print("    > Processing each tile");
		for(AiTile tile: selectedTiles)
		{	ai.checkInterruption();
			print("      > Processing tile "+tile);
			
			// on identifie le cas de cette case (en fonction du mode)
			print("        > Identifying the case");
			AiUtilityCase caze = identifyCase(tile);
			print("        < case="+caze);
			
			// on identifie la combinaison de valeurs des critères pour le cas détecté
			print("        > Processing the combination");
			AiUtilityCombination combination = caze.processCombination(tile);
			print("        < combination="+combination);
			
			// on calcule la valeur d'utilité correspondant à cette combinaison (en fonction de son rang)
			float utility = referenceUtilities.get(combination);
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
		after = System.currentTimeMillis();
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
	/** Map de tous les critères créés */
	private final HashMap<String,AiUtilityCriterion<?,?>> criterionMap = new HashMap<String,AiUtilityCriterion<?,?>>();

	/**
	 * Vérifie si un critère portant le nom passé
	 * en paramètre existe déjà.
	 * Cette méthode est destinée à un usage interne.
	 * 
	 * @param name
	 * 		Nom à tester.
	 * @return
	 * 		{@code true} ssi un critère de ce nom existe déjà.
	 */
	boolean checkCriterionName(String name)
	{	boolean result = criterionMap.keySet().contains(name);
		return result;
	}
	
	/**
	 * Initialise d'abord tous les critères, puis tous les cas.
	 * Les cas doivent obligatoirement être stockés dans la map
	 * {@link #caseMap} (les clés {@code String} correspondant aux
	 * noms des cas).
	 * <br/>
	 * Ensuite, la méthode doit initialiser les valeurs d'utilités
	 * de chaque combinaison possible (pour tous les cas). Ces
	 * valeurs doivent obligatoirement être stockées dans la map 
	 * {@link #referenceUtilities}. De plus, les valeurs d'utilité
	 * maximales pour chaque mode doivent être stockées dans
	 * la map {@link #maxReferenceUtilities}.
	 * <br/>
	 * Bien entendu, cette initialisation est réalisée une seule
	 * fois lors de la création de l'agent. On suppose donc
	 * que les cas/critères/combinaisons sont constantes au cours
	 * d'une partie.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract void initCriteria() throws StopRequestException;
	
	/////////////////////////////////////////////////////////////////
	// CASES	/////////////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant tous les cas, à initialiser dans {@link #initCriteria()} */
	protected final HashMap<String,AiUtilityCase> caseMap = new HashMap<String,AiUtilityCase>();

	/**
	 * Vérifie si un cas portant le nom passé
	 * en paramètre existe déjà.
	 * Cette méthode est destinée à un usage interne.
	 * 
	 * @param name
	 * 		Nom à tester.
	 * @return
	 * 		{@code true} ssi un cas de ce nom existe déjà.
	 */
	boolean checkCaseName(String name)
	{	boolean result = caseMap.keySet().contains(name);
		return result;
	}

	// TODO
	protected abstract void initCases() throws StopRequestException;
	
	/**
	 * Cette méthode prend une case en paramètre, et identifie
	 * le cas correspondant. Bien sûr le traitement dépend
	 * à la fois de la zone de jeu et du mode courant, qui est
	 * accessible grâce à la méthode {@link ArtificialIntelligence#getModeHandler()}.
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
	/** Map contenant l'utilité de chaque combinaison, à initialiser dans {@link #initCriteria()} */
	protected final HashMap<AiMode, HashMap<AiUtilityCombination,Integer>> referenceUtilities = new HashMap<AiMode, HashMap<AiUtilityCombination,Integer>>();
	/** Indique l'utilité maximale pour chaque mode */
	protected final HashMap<AiMode,Integer> maxReferenceUtilities = new HashMap<AiMode,Integer>();
	
	/**
	 * Crée une map vide pour chaque mode. Ces
	 * maps devront être remplies par {@link #initReferenceUtilities()}.
	 */
	private void createReference()
	{	// pour chaque mode, on crée une map vide,
		// qui devra être remplie par initReference()
		for(AiMode mode: AiMode.values())
		{	HashMap<AiUtilityCombination, Integer> map = new HashMap<AiUtilityCombination, Integer>();
			referenceUtilities.put(mode,map);
		}
	}
	
	// TODO
	protected abstract void initReferenceUtilities() throws StopRequestException;
	
	/**
	 * Calcule l'utilité de réference maximale pour chaque mode.
	 * Ces valeurs sont utilisées lors de l'initialisation de
	 * la sortie graphique (pour normaliser les valeurs d'utilité)
	 */
	private void initMaxUtilities()
	{	for(AiMode mode: AiMode.values())
		{	HashMap<AiUtilityCombination,Integer> map = referenceUtilities.get(mode);
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
	 * 
	 * @param combination
	 * 		La combinaison dont on veut l'utilité.
	 * @return
	 * 		L'utilité associée à la combinaison spécifiée.
	 * 
	 * @throws IllegalArgumentException
	 * 		Ssi la combinaison spécifiée n'est pas présente dans {@link #referenceUtilities}.
	 * 
	 * TODO renommer en "retrieveUtilityValue"
	 */
	protected final int getUtilityValue(AiUtilityCombination combination)
	{	Integer result = referenceUtilities.get(combination);
		if(result==null)
			throw new IllegalArgumentException("No utility value was associated to the specified combination ("+combination+").");
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// CACHE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Cache stockant temporairement les résultats du calcul des critères */
	protected final HashMap<String,HashMap<AiTile,Object>> criterionCache = new HashMap<String, HashMap<AiTile,Object>>();
	
	/**
	 * Méthode permettant de rechercher si un critère a déjà été calculé lors
	 * de cette itération. Si c'est le cas, on ne le recalcule pas : le cache
	 * nous renvoie la valeur précédemment calculée. Sinon, le cache renvoie 
	 * {@code null}.
	 * <br/>
	 * Cette méthode est automatiquement utilisée par les classes héritant
	 * de {@link AiUtilityCriterion}. Le concepteur d'un agent n'a, a priori,
	 * pas besoin de l'utiliser.
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
		HashMap<AiTile,Object> map = criterionCache.get(name);
		if(map!=null)
			result = map.get(tile);
		return result;
	}
	
	/**
	 * Méthode permettant d'insérer une valeur dans le cache. Cette opération
	 * est réalisée quand cette valeur n'a pas été trouvée dans le cache.
	 * <br/>
	 * Cette méthode est automatiquement utilisée par les classes héritant
	 * de {@link AiUtilityCriterion}. Le concepteur d'un agent n'a, a priori,
	 * pas besoin de l'utiliser.
	 * 
	 * @param name
	 * 		Nom (unique) du critère calculé.
	 * @param tile
	 * 		Case pour laquelle on a calculé le critère.
	 * @param value
	 * 		Valeur du critère.
	 */
	final void putValueForCriterion(String name, AiTile tile, Object value)
	{	HashMap<AiTile,Object> map = criterionCache.get(name);
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
		List<AiUtilityCombination> combis = new ArrayList<AiUtilityCombination>(referenceUtilities.keySet());
		Collections.sort(combis,new Comparator<AiUtilityCombination>()
		{	@Override
			public int compare(AiUtilityCombination o1, AiUtilityCombination o2)
			{	int u1 = referenceUtilities.get(o1);
				int u2 = referenceUtilities.get(o2);
				int result = u1 - u2;
				return result;
			}	
		});
		
		for(AiUtilityCombination combi: combis)
		{	int utility = referenceUtilities.get(combi);
			print("    "+utility+"."+combi);
		}
		print("    < Utilities done");
	}
}
