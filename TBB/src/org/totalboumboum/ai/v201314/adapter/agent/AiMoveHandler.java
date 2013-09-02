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

import org.totalboumboum.ai.v201314.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201314.adapter.data.AiHero;
import org.totalboumboum.ai.v201314.adapter.data.AiTile;
import org.totalboumboum.ai.v201314.adapter.data.AiZone;
import org.totalboumboum.ai.v201314.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * En particulier, elle implémente la méthode
 * {@link #considerMoving} de l'algorithme général.
 * Cette méthode fait appel à trois méthodes pour 
 * réaliser son traitement : {@link #processCurrentDestination()},
 * {@link #processCurrentPath()} et {@link #processCurrentDirection()}.
 * Ces trois méthodes doivent être surchargées.
 * <br/>
 * Ces trois méthodes permettent chacune de mettre à jour une variable : 
 * <ul>
 * 		<li>{@link #currentDestination} : la case de destination courante ;</li>
 * 		<li>{@link #currentPath} : le chemin courant (pour aller à la destination courante) ;</li>
 * 		<li>{@link #currentDirection} : la direction courante (qui dépend directement du chemin courant).</li>
 * </ul>
 * Ces variables sont notamment utilisées lors du traitement (méthode {@link #processCurrentDestination()})
 * et de l'affichage (méthode {@link #updateOutput()}).
 * 
 * @param <T> 
 * 		Classe de l'agent.
 * 
 * @author Vincent Labatut
 */
public abstract class AiMoveHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * Cette méthode doit obligatoirement être appelée par une classe 
	 * héritant de celle-ci, grâce au mot-clé {@code super}.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 */
	protected AiMoveHandler(T ai)
    {	// init ai object
		super(ai);
		print("    init move handler");
    	
    	// init data
    	AiZone zone = ai.getZone();
    	AiHero ownHero = zone.getOwnHero();
    	currentDestination = ownHero.getTile();
    	currentDirection = Direction.NONE;
    	currentPath = new AiPath();
	}
	
    /////////////////////////////////////////////////////////////////
	// DESTINATION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La case objectif courante, i.e. celle dans laquelle on veut aller */ 
	private AiTile currentDestination = null;

	/**
	 * Renvoie la destination courante de l'agent.
	 * 
	 * @return
	 * 		La destination courante de l'agent.
	 */
	public final AiTile getCurrentDestination()
	{	return currentDestination;
	}
	
	/**
	 * Calcule l'objectif courant de l'agent, c'est à dire
	 * la case dans laquelle il veut aller. 
	 * <br/>
	 * Ce calcul dépend devrait dépendre au moins des valeurs 
	 * de préférence déjà calculées, et éventuellement d'autres 
	 * calculs supplémentaires.
	 * 
	 * @return
	 * 		La case correspondant à la destination courante de l'agent.
	 */
	protected abstract AiTile processCurrentDestination();
	
	/////////////////////////////////////////////////////////////////
	// PATH						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Le chemin courant, permettant d'aller dans la case de destination */ 
	private AiPath currentPath = null;
	
	/**
	 * Renvoie le chemin courant de l'agent.
	 * 
	 * @return
	 * 		Le chemin courant de l'agent.
	 */
	public final AiPath getCurrentPath()
	{	return currentPath;
	}
	
	/**
	 * Calcule le chemin courant de l'agent, c'est à dire 
	 * la séquence de cases à parcourir pour atteindre
	 * (directement ou indirectement) la case objectif.
	 * <br/>
	 * Ce traitement devrait a priori faire usage des méthodes
	 * et classes de l'API permettant de rechercher des chemins. 
	 * 
	 * @return
	 * 		Le chemin courant suivant par l'agent.
	 */
	protected abstract AiPath processCurrentPath();

	/////////////////////////////////////////////////////////////////
	// DIRECTION				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La direction à prendre pour suivre le chemin courant */ 
	private Direction currentDirection = null;

	/**
	 * Renvoie la direction courante de l'agent.
	 * 
	 * @return
	 * 		La direction courante de l'agent.
	 */
	public final Direction getCurrentDirection()
	{	return currentDirection;
	}

	/**
	 * Calcule la direction courante suivie par l'agent.
	 * <br/>
	 * Ce traitement devrait a priori dépendre du chemin courant,
	 * et éventuellement d'autres informations. 
	 * 
	 * @return
	 * 		La direction courante de l'agent.
	 */
	protected abstract Direction processCurrentDirection();

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit se déplacer, et dans quelle direction.
	 * Cette décision dépend des valeurs de préférence courantes.
	 * <br/>
	 * La méthode renvoie une {@link Direction} indiquant le
	 * sens du déplacement, ou bien {@code null} ou {@link Direction#NONE}
	 * si aucun déplacement ne doit être effectué.
	 * 
	 * @return
	 * 		Renvoie une direction indiquant le sens (ou l'absence) de déplacement de l'agent.
	 */
	protected final Direction considerMoving()
	{	ai.checkInterruption();
	
		// si nécessaire, on change la destination courante
		{	long before = print("    > entering updateCurrentDestination");
			currentDestination = processCurrentDestination();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < exiting updateCurrentDestination duration="+elapsed);
		}
		
		// on cherche un chemin vers cette destination
		{	long before = print("    > entering updateCurrentPath");
			currentPath = processCurrentPath();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < exiting updateCurrentPath duration="+elapsed);
		}
		
		// on utilise le chemin pour déterminer la direction de déplacement
		{	long before = print("    > entering updateCurrentDirection");
			currentDirection = processCurrentDirection();
			long after = ai.getCurrentTime();
			long elapsed = after - before;
			print("    < exiting updateCurrentDirection duration="+elapsed);
		}
		
		return currentDirection;
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** Détermine si le gestionnaire colorie une case représentant la destination dans la sortie graphique */ 
	public boolean outputDestination = true;
	/** Détermine si le gestionnaire affiche le chemin courant dans la sortie graphique */ 
	public boolean outputPath = true;
	
	/**
	 * Met à jour les sorties graphiques de l'agent en considérant
	 * les données de ce gestionnaire.
	 * <br/>
	 * Ici, on représente la case de destination courante
	 * en la coloriant, ainsi que le chemin courant, représenté
	 * par une ligne. La couleur représente le mode : bleu pour
	 * {@link AiMode#COLLECTING} et rouge pour {@link AiMode#ATTACKING}.
	 * <br/>
	 * Cette méthode peut être surchargée si vous voulez afficher
	 * les informations différemment, ou d'autres informations. A
	 * noter que cette méthode n'est pas appelée automatiquement : 
	 * elle doit l'être par {@link ArtificialIntelligence#updateOutput()}
	 * si vous désirez l'utiliser. 
	 */
	public void updateOutput()
	{	AiOutput output = ai.getOutput();
		AiMode mode = ai.getModeHandler().getMode();
		
		// color
		Color color = null;
		if(mode==AiMode.ATTACKING)
			color = Color.RED;
		else if(mode==AiMode.COLLECTING)
			color =Color.BLUE;
		
		// path
		if(outputPath && currentPath!=null)
			output.addPath(currentPath,color);
		
		// destination
		if(outputDestination && currentDestination!=null)
			output.addTileColor(currentDestination,Color.BLACK);
	}
}
