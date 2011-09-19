package org.totalboumboum.ai.tests.ais._demo;

import org.totalboumboum.ai.v201011.adapter.AiManager;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * (à ne pas modifier)
 * 
 * @author Vincent Labatut
 *
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new Demo());		
	}
}
