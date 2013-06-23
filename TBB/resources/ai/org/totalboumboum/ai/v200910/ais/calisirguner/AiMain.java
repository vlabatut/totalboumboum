package org.totalboumboum.ai.v200910.ais.calisirguner;

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.ais.calisirguner.v5c.CalisirGuner;

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
	{	super(new CalisirGuner());		
	}
}
