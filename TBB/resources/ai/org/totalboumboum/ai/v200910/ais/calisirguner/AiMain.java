package org.totalboumboum.ai.v200910.ais.calisirguner;

import org.totalboumboum.ai.v200910.adapter.AiManager;
import org.totalboumboum.ai.v200910.ais.calisirguner.v5.CalisirGuner;

/**
 * class utilisée par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new CalisirGuner());		
	}
}
