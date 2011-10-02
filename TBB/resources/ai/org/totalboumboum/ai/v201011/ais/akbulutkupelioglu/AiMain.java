package org.totalboumboum.ai.v201011.ais.akbulutkupelioglu;

import org.totalboumboum.ai.v201011.adapter.AiManager;
import org.totalboumboum.ai.v201011.ais.akbulutkupelioglu.v6.AkbulutKupelioglu;

/**
 * classe utilisée par le moteur du jeu pour retrouver les IA
 * (à ne pas modifier)
 * 
 * @author Vincent Labatut
 *
 */
@SuppressWarnings("deprecation")
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new AkbulutKupelioglu());		
	}
}
