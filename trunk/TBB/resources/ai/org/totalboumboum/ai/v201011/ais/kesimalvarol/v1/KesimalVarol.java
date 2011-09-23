package org.totalboumboum.ai.v201011.ais.kesimalvarol.v1;

import java.awt.Color;

import org.totalboumboum.ai.v201011.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v201011.adapter.communication.AiAction;
import org.totalboumboum.ai.v201011.adapter.communication.AiActionName;
import org.totalboumboum.ai.v201011.adapter.communication.AiOutput;
import org.totalboumboum.ai.v201011.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201011.adapter.data.AiHero;
import org.totalboumboum.ai.v201011.adapter.data.AiZone;

/**
 * @author Ali Baran Kesimal
 * @author Işıl Varol
 */
@SuppressWarnings("unused")
enum Mode {
	NONE,COLLECTE,ATTAQUE
}

public class KesimalVarol extends ArtificialIntelligence
{	
	/** Variable pour garder la zone */
	private AiZone zone;
	public AiZone getZone() {
		return zone;
	}
	/** Representation modifiée de la zone  */
	private GameZone gZone;
	/** Repr. de notre agent */
	private AiHero selfHero;
	public AiHero getSelfHero() {
		return selfHero;
	}
	/** Repr. du mode de notre agent. */
	private Mode mode;
	public Mode getMode() {
		return mode;
	}

	/** Cstr pour initializer les variables des autres classes. */
	public KesimalVarol()
	{
		super();
		GameZone.setMonIA(this);
		mode=Mode.COLLECTE; //Seulement pour cette version, on n'a aucun changement de mode.
	}
	
	/** méthode appelée par le moteur du jeu pour obtenir une action de votre IA */
	public AiAction processAction() throws StopRequestException
	{	// avant tout : test d'interruption
		checkInterruption();
		
		zone=getPercepts();
		selfHero=zone.getOwnHero();

		/*System.out.print("Speed : ");
		System.out.print(selfHero.getWalkingSpeed());		
		System.out.print(" ");
		System.out.print(zone.getElapsedTime());
		System.out.print("\n");*/
		
		gZone=new GameZone();
		Matrix m=gZone.constructInterestMatrix();
		
		AiAction result = new AiAction(AiActionName.NONE);
		//uneMethode();
		
		colorizePath(m);
		return result;
	}
	
	//---------------------
	// Methodes a utiser lors de debogage
	//---------------------
	private void colorizePath(Matrix m) throws StopRequestException
	{
		checkInterruption();
		m.colorizeMapAccordingly();
	}
}
