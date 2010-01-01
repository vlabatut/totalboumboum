package tournament200910.findiksirin;

import org.totalboumboum.ai.adapter200910.AiManager;

import tournament200910.findiksirin.v5.FindikSirin;

/**
 * class utilis�e par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilis� pour cr�er une instance de l'IA
	 */
	public AiMain()
	{	super(new FindikSirin());		
	}
}
