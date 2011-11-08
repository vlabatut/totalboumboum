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

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;

import org.totalboumboum.ai.v201112.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;

/**
 * 
 * Classe gérant le calcul des valeurs d'utilité de l'agent.
 * En particulier, elle doit implémenter la méthode
 * {@link #updateUtility} de l'algorithme général.<br/>
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
 * récupérer la liste des cases qui possèdent cette utilité.<br/>
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
	}

	/////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
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
	protected abstract void update() throws StopRequestException;

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant
	 * les données de ce gestionnaire.<br/>
	 * Ici, on se contente d'afficher la valeur numérique
	 * de l'utilité dans chaque case.<br/>
	 * Cette méthode peut être surchargée si vous voulez afficher
	 * les informations différemment, ou d'autres informations. A
	 * noter que cette méthode n'est pas appelée automatiquement : 
	 * elle doit l'être par {@link ArtificialIntelligence#updateOutput()}
	 * si vous désirez l'utiliser. 
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public void updateOutput() throws StopRequestException
	{	NumberFormat nf = NumberFormat.getInstance(Locale.FRENCH);
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);

		AiOutput output = ai.getOutput();
		for(Entry<AiTile,Float> entry: utilitiesByTile.entrySet())
		{	AiTile tile = entry.getKey();
			float value = entry.getValue();
			
			String text = "-\u221E";
			if(value!=Long.MAX_VALUE)
				text = nf.format(value);
			output.setTileText(tile,text);
		}
	}
}
