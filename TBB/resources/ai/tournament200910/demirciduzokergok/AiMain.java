package tournament200910.demirciduzokergok;

import org.totalboumboum.ai.adapter200910.AiManager;

import tournament200910.demirciduzokergok.v5_2.DemirciDuzokErgok;

/**
 * class utilisée par le moteur du jeu pour retrouver les IA
 */
public class AiMain extends AiManager
{
	/**
	 * constructeur utilisé pour créer une instance de l'IA
	 */
	public AiMain()
	{	super(new DemirciDuzokErgok());		
	}
}
