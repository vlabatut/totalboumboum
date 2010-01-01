package tournament200910.aksoytangay;

import org.totalboumboum.ai.v200910.adapter.AiManager;

import tournament200910.aksoytangay.v5_2.AksoyTangay;

/**
 * class utilisée par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new AksoyTangay());		
	}
}
