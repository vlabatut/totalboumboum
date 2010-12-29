package org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu;

import org.totalboumboum.ai.v201011.adapter.AiManager;
import org.totalboumboum.ai.v201011.ais.gocmenogluhekimoglu.v5.GocmenogluHekimoglu;

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
	{	super(new GocmenogluHekimoglu());		
	}
}
