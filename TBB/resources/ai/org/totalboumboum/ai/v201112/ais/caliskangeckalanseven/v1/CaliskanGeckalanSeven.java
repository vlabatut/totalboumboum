package org.totalboumboum.ai.v201112.ais.caliskangeckalanseven.v1;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * TODO A compléter
 * 
 * L'algorithme général est implémenté par la méthode
 * {@link ArtificialIntelligence#processAction() processAction()}, qui
 * ne peut pas être surchargée. Vous devez surcharger
 * les différentes méthodes que l'algorithme général utilise :
 * <ul>
 * 		<li>{@link #updatePercepts()} : mise à jour des percepts et
 * 			plus généralement des informations utilisées lors du reste du traitement ;</li>
 * 		<li>{@link #updateMode()} : mise à jour de la variable {@link ArtificialIntelligence#mode mode}</li>
 * 		<li>{@link #updateUtility()} : mise à jour de valeurs d'utilité en
 * 			fonction des percepts et du mode courant ;</li>
 *  	<li>{@link #considerBombing()} : détermine si l'agent doit poser une bombe ou pas ;</li>
 *  	<li>{@link #considerMoving()} : détermine si l'agent doit se déplacer ou pas, et si oui
 *  		dans quelle direction ;</li>
 *  	<li>{@link #updateOutput()} : met à jour la sortie <i>graphique</i> de l'agent.</li>
 * </ul>
 * 
 * @author Mustafa Çalışkan
 * @author Cihan Seven
 */
public class CaliskanGeckalanSeven extends ArtificialIntelligence
{
	@Override
	public void updatePercepts() throws StopRequestException
	{	
		// TODO à compléter
	}
	
	@Override
	public void updateMode() throws StopRequestException
	{	
		// TODO à compléter
	}

	@Override
	public void updateUtility() throws StopRequestException
	{	
		// TODO à compléter
	}

	@Override
	public boolean considerBombing() throws StopRequestException
	{	
		// TODO à compléter
		return false;
	}

	@Override
	public Direction considerMoving() throws StopRequestException
	{	// TODO à compléter
		return Direction.NONE;
	}
	
	@Override
	public void updateOutput() throws StopRequestException
	{	
		// TODO à compléter
	}
}
