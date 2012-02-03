package org.totalboumboum.ai.v201112.adapter.agent;

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

import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;

/**
 * Classe gérant les déplacements de l'agent. Elle
 * implémente la méthode {@link #update}, utilisée pour 
 * mettre le mode à jour, et qui ne peut pas être modifiée 
 * ni surchargée. Cette méthode implémente l'algorithme de 
 * sélection du mode défini en cours. Elle fait appel aux 
 * méthodes {@link #hasEnoughItems} et {@link #isCollectPossible()}, 
 * qui elles doivent être surchargées : si elles ne le sont
 * pas, alors le mode sera toujours {@link AiMode#COLLECTING}
 * par défaut.
 * <br/>
 * Enfin, cette classe stocke le mode courant grâce au
 * champ {@link #mode}.
 * 
 * @author Vincent Labatut
 */
public abstract class AiModeHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
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
	protected AiModeHandler(T ai) throws StopRequestException
    {	super(ai);
		print("    init mode handler");
	}

    /////////////////////////////////////////////////////////////////
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le mode courant de l'agent */
	protected AiMode mode = AiMode.COLLECTING;

	/**
	 * Renvoie le mode courant de l'agent.
	 * 
	 * @return
	 * 		Le mode courant de l'agent.
	 */
	public AiMode getMode()
	{	return mode;
	}
	
    /////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de mettre à jour
	 * le mode de l'agent : {@link AiMode#ATTACKING}
	 * ou {@link AiMode#COLLECTING}.
	 * <br/>
	 * <b>Attention :</b> cette méthode ne peut pas être redéfinie.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected final void update() throws StopRequestException
	{	// si l'agent a assez d'items, on attaque
		long before = print("    > Entering hasEnoughItems");
		boolean enoughItems = hasEnoughItems();
		long after = System.currentTimeMillis();
		long elapsed = after - before;
		print("    < Exiting hasEnoughItems duration="+elapsed+" result="+enoughItems);
		if(enoughItems)
			mode = AiMode.ATTACKING;
		
		// sinon, il va essayer d'en ramasser
		else
		{	// s'il est possible d'en ramasser, il passe en mode collecte
			before = print("    > Entering isCollectPossible");
			boolean collectPossible = isCollectPossible();
			after = System.currentTimeMillis();
			elapsed = after - before;
			print("    < Exiting isCollectPossible duration="+elapsed+" result="+collectPossible);
			if(collectPossible)
				mode = AiMode.COLLECTING;
			
			// sinon, il est obligé d'attaquer (même s'il n'a pas assez d'armes)
			else
				mode = AiMode.ATTACKING;
		}
	}
	
	/**
	 * Détermine si l'agent possède assez d'item,
	 * ou bien s'il doit essayer d'en ramasser d'autres.
	 * Cette distinction est relative à l'environnement,
	 * à l'agent lui-même et à la stratégie qu'il utilise.
	 * <br/>
	 * Cette méthode est utilisée par lors de la mise 
	 * à jour du mode par {@link #update}.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent possède assez d'items.
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract boolean hasEnoughItems() throws StopRequestException;
	
	/**
	 * Détermine si l'agent a la possibilité de ramasser
	 * des items dans la zone courante : présence d'items
	 * cachés ou découverts, assez de temps restant, etc.
	 * <br/>
	 * Cette méthode est utilisée par lors de la mise 
	 * à jour du mode par {@link #update}.
	 * 
	 * @return
	 * 		{@code true} ssi l'agent a la possibilité de ramasser des items.
	 * @throws StopRequestException
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract boolean isCollectPossible() throws StopRequestException;
}
