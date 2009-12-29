package tournament200910.enhoskarapazar;

import tournament200910.enhoskarapazar.v5.EnhosKarapazar;
import fr.free.totalboumboum.ai.adapter200910.AiManager;
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
