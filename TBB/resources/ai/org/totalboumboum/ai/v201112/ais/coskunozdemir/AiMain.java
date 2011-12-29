package org.totalboumboum.ai.v201112.ais.coskunozdemir;

import org.totalboumboum.ai.v201112.adapter.agent.AiManager;
import org.totalboumboum.ai.v201112.ais.coskunozdemir.v2.CoskunOzdemir;

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
	{	super(new CoskunOzdemir());		
	}
}
