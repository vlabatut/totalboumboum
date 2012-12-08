package org.totalboumboum.ai.v201112.ais.kayukataskin.v3;


import org.totalboumboum.ai.v201112.adapter.agent.AiMoveHandler;
import org.totalboumboum.ai.v201112.adapter.communication.StopRequestException;
import org.totalboumboum.engine.content.feature.Direction;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.totalboumboum.ai.v201112.adapter.data.AiHero;
import org.totalboumboum.ai.v201112.adapter.data.AiTile;
import org.totalboumboum.ai.v201112.adapter.data.AiZone;
import org.totalboumboum.ai.v201112.adapter.path.AiLocation;
import org.totalboumboum.ai.v201112.adapter.path.AiPath;
import org.totalboumboum.ai.v201112.adapter.path.LimitReachedException;
import org.totalboumboum.ai.v201112.adapter.path.search.Astar;
/**
 * Classe gérant le déplacement de l'agent.
 * Cf. la documentation de {@link AiMoveHandler} pour plus de détails.
 * 
 * @author Pol Kayuka
 * @author Ayça Taşkın
 */
@SuppressWarnings("deprecation")
public class MoveHandler extends AiMoveHandler<KayukaTaskin>
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
	protected MoveHandler(KayukaTaskin ai) throws StopRequestException
    {	super(ai);
		ai.checkInterruption();
		
		// on règle la sortie texte pour ce gestionnaire
		verbose = true;
		zone = ai.getZone();
		ownHero = zone.getOwnHero();
	}

	
	
	///////////////////////// DATA ///////////////////////////
	/** */
	private KayukaTaskin ai;
	/** */
	protected AiZone zone = null;
	/** */
	protected AiHero ownHero = null;
	/** */
	protected AiTile currentTile = null;
	/** */
	protected boolean bombDestination = false;
	/** */
	private boolean arrived;
	/** */
	private AiTile tileDest;
	/** */
	@SuppressWarnings("unused")
	private double xDest;
	/** */
	@SuppressWarnings("unused")
	private double yDest;
	/** */
	private Astar astar;
	/** */
	private AiPath path;
	
	
	/////////////////////////////////////////////////////////////////
	/**
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public boolean hasArrived() throws StopRequestException
	{	ai.checkInterruption(); //APPEL OBLIGATOIRE
		
		if(!arrived)
		{	if(tileDest==null)
				arrived = true;
			else
			{	AiTile currentTile = ownHero.getTile();
				arrived = (currentTile==tileDest);			
			}
		}
		
		return arrived;
	}
	
	@Override
	protected Direction considerMoving() throws StopRequestException
	{	ai.checkInterruption();
	zone = ai.getZone();
		
	ai.checkInterruption(); //APPEL OBLIGATOIRE
	
	// on met d'abord à jour la matrice de cout
	//updateCostCalculator();

	Direction result = Direction.NONE;
	if(!hasArrived())
	{	// on vérifie que le joueur est toujours sur le chemin
		//checkIsOnPath();
		// si le chemin est vide ou invalide, on le recalcule.
		
		if(path.isEmpty() ){
			ai.checkInterruption();
			updatePath();
		}
		// s'il reste deux cases au moins dans le chemin, on se dirige vers la suivante
		AiTile tile = null;
		AiLocation location;
		if(path.getLength()>1)
				{location = path.getLocation(1);
				tile=zone.getTile(location);}
		// sinon, s'il ne reste qu'une seule case, on va au centre
		else if(path.getLength()>0)
		{location = path.getLocation(0);
		tile=zone.getTile(location);}
		// on détermine la direction du prochain déplacement
		if(tile!=null)
			result = zone.getDirection(ownHero, tile);
	}
	updateOutput();
	return result;
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	
	/**
	 * @throws StopRequestException 
	 * 
	 */
	@SuppressWarnings("unused")
	private void updateDestination() throws StopRequestException
	{
		ai.checkInterruption();
		boolean changeDestination = false;
		
		if(currentDestination==null)
		{	
			changeDestination = true;
		}
	
		else if(currentTile.equals(currentDestination))
		{	
			changeDestination = true;
		}	
		else
		{	
			HashMap<AiTile,Float> utilitiesByTile = ai.utilityHandler.getUtilitiesByTile();
			
			Float destinationUtility = utilitiesByTile.get(currentDestination);
			
		
			if(destinationUtility==null)
			{	changeDestination = true;
				print("      current destination is obsolete and must be changed: destinationUtility="+destinationUtility);
			}
		}
		
		if(changeDestination)
		{	
			
			HashMap<Float,List<AiTile>> utilitiesByValue = ai.utilityHandler.getUtilitiesByValue();
			TreeSet<Float> values = new TreeSet<Float>(utilitiesByValue.keySet());
			
			
			
			Iterator<Float> it1 = values.descendingIterator();
			boolean goOn = true;
			while(it1.hasNext() && goOn)
			{	ai.checkInterruption();	
				
				
				float utility = it1.next();
				
		
				List<AiTile> tiles = utilitiesByValue.get(utility);
				for(AiTile tile: tiles)
				{	ai.checkInterruption();
					
					print("          +"+tile);
				}
				
				Collections.shuffle(tiles);
			
				if(!tiles.isEmpty())
					currentDestination = tiles.get(0);
	
					
			}
			
			if(currentDestination==null)
			{	print("      could not find any new destination");
				bombDestination = false;
			}
			else
				print("      new destination all set: currentDestination="+currentDestination+" bombDestination="+bombDestination);
		}
		
		else
		{	print("      no need to update the destination");
		}
		
	}
	
	/**
	 * 
	 * @throws StopRequestException
	 */
	private void updatePath() throws StopRequestException
	{
		ai.checkInterruption(); //APPEL OBLIGATOIRE
		Set<AiTile> endTiles = null;
		AiLocation firstLocat = path.getFirstLocation();
		try {
			path = astar.processShortestPath(firstLocat, endTiles);
		} catch (LimitReachedException e) {
			e.printStackTrace();
		}
		tileDest = path.getLastLocation().getTile();
		
	}
	
	/////////////////////////////////////////////////////////////////
	// OUTPUT			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	@Override
	public void updateOutput() throws StopRequestException
	{	ai.checkInterruption();
		
		// ici on se contente de faire le traitement par défaut
		super.updateOutput();
		//  à redéfinir, si vous voulez afficher d'autres informations
	}
}
