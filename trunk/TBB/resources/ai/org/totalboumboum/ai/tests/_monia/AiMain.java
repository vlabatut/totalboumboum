package org.totalboumboum.ai.tests._monia;

import org.totalboumboum.ai.v201011.adapter.AiManager;

/**
 * classe utilis�e par le moteur du jeu pour retrouver les IA
 * (� ne pas modifier)
 * 
 * @author Vincent Labatut
 *
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilis� pour cr�er une instance de l'IA
	 */
	public AiMain()
	{	super(new MonIA());		
	}
}
