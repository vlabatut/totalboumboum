package org.totalboumboum.ai.v201011.ais.kantarcitaskin.v1;


import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;

public class KantarciTaskin extends ArtificialIntelligence
{	
	Matrix matrix;
	
	//private org.totalboumboum.ai.v201011.adapter.data.AiHero us=null;
	/** m�thode appel�e par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		AiAction result = new AiAction(AiActionName.NONE);
		createMatrix();
		return result;
	}
	
	
	/** une m�thode bidon pour l'exemple */
	private void createMatrix() throws StopRequestException
	{	
		checkInterruption();
	
	//	je cr�e un objet impl�mentant mon traitement
	//	MonTraitement mt = new MonTraitement(this);
		System.out.print("lala");
		matrix = new Matrix(this);	
		matrix.fillMatrix();
		
		
		// j'appelle la m�thode qui effectue le traitement
		//AiAction result = mt.gagneRound();
		//return result;
	}
}
