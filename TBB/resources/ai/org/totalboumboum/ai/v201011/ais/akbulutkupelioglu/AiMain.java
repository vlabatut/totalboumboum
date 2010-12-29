package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu;

import org.totalboumboum.ai.v201011.adapter.AiManager;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v5.AkbulutKupelioglu;

/**
 * classe utilisee par le moteur du jeu pour retrouver les IA
 * (a ne pas modifier)
 * 
 * @author Vincent Labatut
 *
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilise pour creer une instance de l'IA
	 */
	public AiMain()
	{	super(new AkbulutKupelioglu());		
	}
}
