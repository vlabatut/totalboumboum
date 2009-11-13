package tournament200910.bektasmazilyah;

import fr.free.totalboumboum.ai.adapter200910.AiManager;

/**
 * class utilisée par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new BektasMazilyah());		
	}
}
