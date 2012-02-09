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

import org.totalboumboum.ai.v201213.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201213.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201213.adapter.data.AiHero;
import org.totalboumboum.ai.v201213.adapter.data.AiTile;
import org.totalboumboum.ai.v201213.adapter.data.AiZone;
import org.totalboumboum.ai.v201213.adapter.path.AiPath;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * En particulier, elle doit implémenter la méthode
 * {@link #considerMoving} de l'algorithme général.
 * Cette méthode doit être surchargée, car par défaut
 * elle ne fait rien du tout et renvoie toujours {@link Direction#NONE}.
 * <br/>
 * Cette classe contient 3 variables qui doivent être 
 * obligatoirement être mises à jour par {@code considerMoving} :
 * <ul>
 * 		<li>{@link #currentDestination} : la case de destination courante ;</li>
 * 		<li>{@link #currentPath} : le chemin courant (pour aller à la destination courante) ;</li>
 * 		<li>{@link #currentDirection} : la direction courante (qui dépend directement du chemin courant).</li>
 * </ul>
 * Ces variables sont notamment utilisées par la méthode {@link #updateOutput()}
 * qui est donnée ici en exemple afin d'afficher le mode,
 * la destination et le chemin courants.
 * 
 * @author Vincent Labatut
 */
public abstract class AiMoveHandler<T extends ArtificialIntelligence> extends AiAbstractHandler<T>
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
	protected AiMoveHandler(T ai) throws StopRequestException
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
	// DATA						/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** La case objectif courante, i.e. celle dans laquelle on veut aller */ 
	protected AiTile currentDestination = null;
	/** Le chemin courant, permettant d'aller dans la case de destination */ 
	protected AiPath currentPath = null;
	/** La direction à prendre pour suivre le chemin courant */ 
	protected Direction currentDirection = null;
	
	/**
	 * Renvoie la destination courante de l'agent.
	 * 
	 * @return
	 * 		La destination courante de l'agent.
	 */
	public AiTile getCurrentDestination()
	{	return currentDestination;
	}

	/**
	 * Renvoie le chemin courant de l'agent.
	 * 
	 * @return
	 * 		Le chemin courant de l'agent.
	 */
	public AiPath getCurrentPath()
	{	return currentPath;
	}

	/**
	 * Renvoie la direction courante de l'agent.
	 * 
	 * @return
	 * 		La direction courante de l'agent.
	 */
	public Direction getCurrentDirection()
	{	return currentDirection;
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/**
	 * Méthode permettant de déterminer si l'agent
	 * doit se déplacer, et dans quelle direction.
	 * Cette décision dépend des valeurs d'utilité courantes.
	 * <br/>
	 * La méthode renvoie une {@link Direction} indiquant le
	 * sens du déplacement, ou bien {@code null} ou {@link Direction#NONE}
	 * si aucun déplacement ne doit être effectué.
	 * 
	 * @return
	 * 		Renvoie une direction indiquant le sens (ou l'absence) de déplacement de l'agent.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	protected abstract Direction considerMoving() throws StopRequestException;

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
	 * 
	 * @throws StopRequestException
	 * 		Le moteur du jeu a demandé à l'agent de s'arrêter. 
	 */
	public void updateOutput() throws StopRequestException
	{	AiOutput output = ai.getOutput();
		AiMode mode = ai.getModeHandler().mode;
		
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
