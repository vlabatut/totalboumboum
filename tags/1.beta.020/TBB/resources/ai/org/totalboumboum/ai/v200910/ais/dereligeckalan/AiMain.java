package org.totalboumboum.ai.v200910.ais.dereligeckalan;

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.ais.dereligeckalan.v5c.DereliGeckalan;

/**
 * classe utilis�e par le moteur du jeu pour retrouver les IA
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
	{	super(new DereliGeckalan());		
	}
}