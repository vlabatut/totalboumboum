package org.totalboumboum.ai.v201011.ais.goncuonat;

import org.totalboumboum.ai.v201011.adapter.AiManager;
import org.totalboumboum.ai.v201011.ais.goncuonat.v6.GoncuOnat;

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
	{	super(new GoncuOnat());		
	}
}