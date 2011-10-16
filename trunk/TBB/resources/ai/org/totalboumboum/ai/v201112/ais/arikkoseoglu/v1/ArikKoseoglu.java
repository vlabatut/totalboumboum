package org.totalboumboum.ai.v201112.ais.arikkoseoglu.v1;

import org.totalboumboum.ai.v201112.adapter.agent.ArtificialIntelligence;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * TODO A compléter
 * 
 * <br/>
 * Ceci est la classe principale de votre agent, que vous devez compléter.
 * Effacez ces commentaires et remplacez-les par votre propre Javadoc.<br/>
 * L'algorithme général est implémenté par la méthode
 * {@link ArtificialIntelligence#processAction() processAction()}, qui
 * ne peut pas être surchargée. Vous devez surcharger
 * les différentes méthodes que l'algorithme général utilise :
 * <ul>
 * 		<li>{@link #updatePercepts()} : mise à jour des percepts et
 * 			plus généralement des informations utilisées lors du reste du traitement ;</li>
 * 		<li>{@link #updateUtility()} : mise à jour de valeurs d'utilité en
 * 			fonction des percepts et du mode courant ;</li>
 *  	<li>{@link #considerBombing()} : détermine si l'agent doit poser une bombe ou pas ;</li>
 *  	<li>{@link #considerMoving()} : détermine si l'agent doit se déplacer ou pas, et si oui
 *  		dans quelle direction ;</li>
 *  	<li>{@link #updateOutput()} : met à jour la sortie <i>graphique</i> de l'agent.</li>
 * </ul>
 * Le choix du mode obéit aussi à un algorithme fixé en cours, qui est
 * implémenté par la méthode {@link ArtificialIntelligence#updateMode()}. Celle-ci
 * n'est ni modifiable ni surchargeable. Elle utilise deux méthode que vous devez redéfinir :
 * <ul>
 * 		<li>{@link #hasEnoughItems()} : détermine si l'agent possède assez d'items, ou
 * 			doit en ramasser d'autres ;</li>
 * 		<li>{@link #isCollectPossible()} : détermine si l'agent peut encore ramasser
 * 			des items, ou si ce n'est plus possible.</li>
 * </ul>
 * Le mode est disponible en utilisant la méthode {@link ArtificialIntelligence#getMode()}.
 * Les percepts sont accessibles à travers l'objet de classe {@link AiZone} renvoyé
 * par la méthode {@link ArtificialIntelligence#getZone()}. Vous pouvez obtenir et modifier
 * la sortie graphique de l'agent à travers la méthode {@link ArtificialIntelligence#getOutput()}.<br/>
 * Le reste de votre traitement doit être externe à cette classe.
 *
 * @author Furkan Arık
 * @author Aksel Köseoğlu
 */
public class ArikKoseoglu extends ArtificialIntelligence
{
	/////////////////////////////////////////////////////////////////
	// PERCEPTS			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void init() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}
	
	@Override
	protected void updatePercepts() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}
	
	/////////////////////////////////////////////////////////////////
	// MODE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean hasEnoughItems() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
		return false;
	}

	@Override
	protected boolean isCollectPossible() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
		return true;
	}
	
	/////////////////////////////////////////////////////////////////
	// UTILITY			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateUtility() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}

	/////////////////////////////////////////////////////////////////
	// DROP BOMB		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected boolean considerBombing() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
		return false;
	}

	/////////////////////////////////////////////////////////////////
	// MOVE				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
		return Direction.NONE;
	}
	
	/////////////////////////////////////////////////////////////////
	// GRAPHICAL OUTPUT		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected void updateOutput() throws StopRequestException
	{	
		// TODO à compléter
		// cf. la java doc dans ArtificialIntelligence pour une description de la méthode
	}
}
