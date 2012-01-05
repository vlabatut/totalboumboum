package org.totalboumboum.ai.v201112.ais.demireloz.v1;

import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v201112.adapter.data.AiBomb;
import org.totalboumboum.engine.content.feature.Direction;

/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * TODO Effacez ces commentaires et remplacez-les par votre propre Javadoc.
 * 
 * @author Enis Demirel
 * @author Berke Öz
 */



public class MoveHandler extends AiMoveHandler<DemirelOz>
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
	protected MoveHandler(DemirelOz ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = false;
		
		// TODO à compléter
	}

	/////////////////////////////////////////////////////////////////
	// PROCESSING				/////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	/*ai.checkInterruption();
		float[] x=new float[50];
		 int a=0,b=0;
		 float temp=0,max=0;
	Set set = this.ai.getUtilityHandler().getUtilitiesByValue().entrySet();
	  Iterator i = set.iterator();

	    while(i.hasNext()){
	      Map.Entry me = (Map.Entry)i.next();
	     x[a]=(Float) me.getKey();
	      a++;
	      System.out.println("sads"+me.getKey() + " :" + me.getValue() );
	      
	    }
	    System.out.println("a: "+a);
	    for(b=0;b<=a;b++)
	    {
	    	
	    	temp=x[b];
	    	if(temp>=max)	
	    	{
	    	max=temp;
	    	}
	    }
	    
	   
	 }
	AiTile at= this.ai.getUtilityHandler().getUtilitiesByValue(); 
	    
	    System.out.println("Maximum "+max);


	    TileCostCalculator tc =new TileCostCalculator(ai);
	    NoHeuristicCalculator h =new NoHeuristicCalculator(ai);
	    BasicSuccessorCalculator bs =new BasicSuccessorCalculator(ai);
	    
	  Astar astar = new Astar(ai,this.ai.getZone().getOwnHero(),tc,h,bs);
	  AiPath ap = astar.processShortestPath(this.ai.getZone().getOwnHero().getTile(),)
	  */
		for (AiBomb currentBomb : this.ai.getZone().getBombs()) {
			if (currentBomb.getBlast().contains(this.ai.getZone().getOwnHero().getTile()))
			{
				
				return Direction.RIGHT;
			}
		}
	
		return Direction.NONE;
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
