package org.totalboumboum.ai.v200910.ais.enhoskarapazar;

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.ais.enhoskarapazar.v5c.EnhosKarapazar;

/**
 * class utilis�e par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilis� pour cr�er une instance de l'IA
	 */
	public AiMain()
	{	super(new EnhosKarapazar());		
	}
}