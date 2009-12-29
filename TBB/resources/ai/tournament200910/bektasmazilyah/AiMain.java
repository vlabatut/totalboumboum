package tournament200910.bektasmazilyah;

import tournament200910.bektasmazilyah.v5.BektasMazilyah;
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
	{	super(new BektasMazilyah());		
	}
}
