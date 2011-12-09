package org.totalboumboum.ai.tests.ais.simplet;

import org.totalboumboum.ai.tests.ais.simplet.v1.Simplet;
import org.totalboumboum.ai.v201112.adapter.agent.AiManager;

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
