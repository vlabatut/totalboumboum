package org.totalboumboum.ai.v201112.ais.capmunavarov.v1;

import java.util.List;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Fahri Cap
 * @author Suhrob Munavarov
 */
public class MoveHandler extends AiMoveHandler<CapMunavarov>
{	
	/**
	 * Construit un gestionnaire pour l'agent passé en paramètre.
	 * 
	 * @param ai	
	 * 		l'agent que cette classe doit gérer.
	 * 
	 * @throws StopRequestException	
	 * 		Au cas où le moteur demande la terminaison de l'agent.
	 */
	
	
	protected MoveHandler(CapMunavarov ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		
		System.out.println("in Movehadnler ..."); 
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private Direction goTowards (){
		boolean result=true;
		
		List<AiBomb> bombes =ai.getZone().getBombs();
		double xpos =ai.getZone().getOwnHero().getPosX();
		double ypos =ai.getZone().getOwnHero().getRow();
		
		/*
		if (bombes.size()>0){
		for(AiBomb bmb: bombes ){
			if ((bmb.getPosX()-xpos)/ai.getZone().getOwnHero().getCurrentSpeed()>bmb.getNormalDuration()+bmb.getBurningDuration()+bmb.getExplosionDuration() )
			{
				return true;
			}
			
		}	
			
		}*/	
		
		
		
		if (bombes.size()>0){
			for(AiBomb bmb: bombes ){
				if ((bmb.getCol()+bmb.getRange())>ai.getZone().getOwnHero().getCol()  )
				{
					return Direction.UP;
				}else if ((bmb.getCol()-bmb.getRange())<ai.getZone().getOwnHero().getCol() )
					return Direction.DOWN;
				else if ((bmb.getRow()+bmb.getRange())>ai.getZone().getOwnHero().getRow() )
				{
					return Direction.RIGHT;
				}else if ((bmb.getRow()-bmb.getRange())<ai.getZone().getOwnHero().getRow() ){
					return Direction.LEFT;
				}
			}	
		}
		
		

		return Direction.UP;
	}
	
	
	@SuppressWarnings({ "unused", "static-access" })
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	
		ai.checkInterruption();
	
		AiZone zone =ai.getZone();
		int height=ai.zoneHeight;
		int width=ai.zoneWidth;
		
		int currentCol =ai.getZone().getOwnHero().getCol();
		int currentRow =ai.getZone().getOwnHero().getRow();
		
		
		print("Considering moving");
		return goTowards();
	}

	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		// TODO à redéfinir, si vous voulez afficher d'autres informations
	}
}
