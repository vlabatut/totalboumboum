package org.totalboumboum.ai.v201213.ais._simplet;

import org.totalboumboum.ai.v201213.adapter.agent.AiManager;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * (à ne pas modifier)
 * 
 * @author Vincent Labatut
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new Simplet());		
	}
}
