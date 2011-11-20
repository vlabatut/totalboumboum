package org.totalboumboum.ai.v201112.adapter.agent;

/*
 * Total Boum Boum
 * Copyright 2008-2011 Vincent Labatut 
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Set;

import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * En particulier, elle doit implémenter la méthode
 * {@link #updateUtility} de l'algorithme général.
 * <br/>
 * Cette classe contient 2 variables qui doivent être 
 * obligatoirement être mises à jour par {@code updateUtility} :
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
    
    	// on initialise les cas/critères/combinaisons une fois pour toutes
    	initCriteria();
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant les valeurs d'utilité (les cases absentes sont inutiles) */
	protected final HashMap<AiTile,Float> utilitiesByTile = new HashMap<AiTile,Float>();
	/** Map contenant les cases rangées par valeur d'utilité */
	protected final HashMap<Float,List<AiTile>> utilitiesByValue = new HashMap<Float,List<AiTile>>();

	/**
	 * Réinitialise les structures de données
	 * modifiées à chaque itération.
	 * <br/>
	 * <b>Attention </b>: vous avez la possibilité
	 * de surcharger cette méthode, par exemple
	 * si vous avez besoin de réinitialiser certains
	 * objets propre à votre programme. Mais dans ce
	 * cas-là, vous mais vous devez impérativement
	 * appeler {@code super.resetData()} ou bien effectuer
	 * vous même la réinitialisation de {@link #utilitiesByTile}
	 * et {@link #utilitiesByValue}.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected void resetData() throws StopRequestException
	{	utilitiesByTile.clear();
		utilitiesByValue.clear();
	}
	
	/**
	 * Renvoie les utilités courantes, rangées par case.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par case.
	 */
	public HashMap<AiTile, Float> getUtilitiesByTile()
	{	return utilitiesByTile;
	}

	/**
	 * Renvoie les utilités courantes, rangées par valeur.
	 * 
	 * @return
	 * 		Une map contenant les utilités rangées par valeur.
	 */
	public HashMap<Float, List<AiTile>> getUtilitiesByValue()
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
	 * <b>Attention :</b> n'oubliez pas de vider les maps
	 * à chaque itération, avant de refaire les calculs d'utilité.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected final void update() throws StopRequestException
	{	// on vide les structures
		print("    Reset data structures");
		resetData();
		
		// on sélectionne les cases dont on veut calculer l'utilité
		long before = print("    >> Entering selectTiles");
		Set<AiTile> selectedTiles = selectTiles();
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		print("    << Exiting selectTiles duration="+elapsed);
		
		// on calcule l'utilité de chaque case
		before = print("    >> Processing each tile");
		for(AiTile tile: selectedTiles)
		{	// on identifie le cas de cette case (en fonction du mode)
			print("      >> Identifying the case");
			AiUtilityCase caze = identifyCase(tile);
			print("      << case="+caze);
			
			// on identifie la combinaison de valeurs des critères pour le cas détecté
			print("      >> Processing the combination");
			AiUtilityCombination combination = caze.processCombination(tile);
			print("      << combination="+combination);
			
			// on calcule la valeur d'utilité correspondant à cette combinaison (en fonction de son rang)
			float utility = referenceUtilities.get(combination);
			
			// on la rajoute dans les structures
			utilitiesByTile.put(tile,utility);
			List<AiTile> tiles = utilitiesByValue.get(utility);
			if(tiles==null)
			{	tiles = new ArrayList<AiTile>();
				utilitiesByValue.put(utility,tiles);
			}
			tiles.add(tile);
		}
		after = System.currentTimeMillis();
		elapsed = after - before;
		print("    << Tile processing finished duration="+elapsed);
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
	// CRITERIA			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Map contenant tous les cas, à initialiser dans {@link #initCriteria()} */
	protected final HashMap<String,AiUtilityCase> cases = new HashMap<String,AiUtilityCase>();
	/** Map contenant l'utilité de chaque combinaison, à initialiser dans {@link #initCriteria()} */
	protected final HashMap<AiUtilityCombination,Integer> referenceUtilities = new HashMap<AiUtilityCombination,Integer>();
	/** Indique l'utilité maximale pour chaque mode */
	protected final HashMap<AiMode,Integer> maxUtilities = new HashMap<AiMode,Integer>();
	
	/**
	 * Initialise d'abord tous les critères, puis tous les cas.
	 * Les cas doivent obligatoirement être stockés dans la map
	 * {@link #cases} (les clés {@code String} correspondant aux
	 * noms des cas).
	 * <br/>
	 * Ensuite, la méthode doit initialiser les valeurs d'utilités
	 * de chaque combinaison possible (pour tous les cas). Ces
	 * valeurs doivent obligatoirement être stockées dans la map 
	 * {@link #referenceUtilities}. De plus, les valeurs d'utilité
	 * maximales pour chaque mode doivent être stockées dans
	 * la map {@link #maxUtilities}.
	 * <br/>
	 * Bien entendu, cette initialisation est réalisée une seule
	 * fois lors de la création de l'agent. On suppose donc
	 * que les cas/critères/combinaisons sont constantes au cours
	 * d'une partie.
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	protected abstract void initCriteria() throws StopRequestException;;
	
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
	 * @throw IllegalArgumentException
	 * 		ssi la combinaison spécifiée n'est pas présente dans {@link #referenceUtilities}.
	 */
	protected final int getUtilityValue(AiUtilityCombination combination)
	{	Integer result = referenceUtilities.get(combination);
		if(result==null)
			throw new IllegalArgumentException("No utility value was associated to the specified combination ("+combination+").");
		return result;
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Détermine si le gestionnaire colorie les cases dans la sortie graphique */ 
	protected boolean outputColors = true;
	/** Détermine si le gestionnaire affiche du texte dans la sortie graphique */ 
	protected boolean outputText = true;
	
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
	{	AiMode mode = ai.getModeHandler().getMode();
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
		Integer limit = maxUtilities.get(mode);
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
				output.setTileColor(tile,color);
			}
		}
	}
}
