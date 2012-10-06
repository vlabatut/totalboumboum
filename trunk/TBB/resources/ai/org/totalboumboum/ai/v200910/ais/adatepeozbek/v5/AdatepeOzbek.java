package org.totalboumboum.ai.v200910.ais.adatepeozbek.v5;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.AiPath;

/**
 * 
 * @version 5
 * 
 * @author Can Adatape
 * @author Sena Özbek
 *
 */
@SuppressWarnings("deprecation")
public class AdatepeOzbek extends ArtificialIntelligence 
{	
	private AiHero ownHero = null;
	private AiZone zone = null;
	private AiAction actionToDo = null;
	private AiPath path = null;
	private List<AiTile> allPassedTiles = null;
	private boolean debug = false;
	/** */
	public boolean canTheyReachMe = false;
	/** */
	public TriedTiles triedTiles = null;
	/** */
	public List<Enemy> enemies = null;
	/** */
	public int urgentBombs = 0 ;
	/** */
	public AiTile lastBombedTile = null;
	/** */
	public long idleTime = 0;
	/** */
	public int privateRang = 0;
	
	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		zone = getPercepts();
		ownHero = zone.getOwnHero();
		allPassedTiles = new ArrayList<AiTile>();
		
		if(triedTiles == null)
			triedTiles = new TriedTiles(this);
		
		if(enemies == null)
		{
			enemies = new ArrayList<Enemy>();
			List<AiHero> remaining = zone.getRemainingHeroes();
			for(int i = 0; i<remaining.size();i++)
			{
				if(remaining.get(i) == zone.getOwnHero())
					continue;
				enemies.add(new Enemy(zone.getHeroes().get(i), EnemyTypes.UNKNOWN));
			}
		}
		
		if(!ownHero.hasEnded())
		{
			
			// Ajoute toutes les cases visités dans le liste
			if(allPassedTiles.size() > 0)
			{
				if(allPassedTiles.get(allPassedTiles.size()-1) != ownHero.getTile())
					allPassedTiles.add(ownHero.getTile());
			}
			
			actionToDo = new AiAction(AiActionName.NONE);
			
			AiEscape EscapeAI = new AiEscape(this);
			
			if(debug)
				EscapeAI.printTiles();
			
			
		}
		
		return actionToDo;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiAction getActionToDo() throws StopRequestException
	{
		checkInterruption();
		
		return actionToDo;
	}

	/**
	 * 
	 * @param action
	 * @throws StopRequestException
	 */
	public void setActionToDo(AiAction action) throws StopRequestException
	{
		checkInterruption();
		
		actionToDo = action;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiHero getOwnHero() throws StopRequestException
	{
		checkInterruption();
		return ownHero;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiZone getZone() throws StopRequestException
	{
		checkInterruption();
		return zone;
	}	
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public AiPath getPath() throws StopRequestException
	{
		checkInterruption();
		return path;
	}
	
	/**
	 * 
	 * @param path
	 * @throws StopRequestException
	 */
	public void setPath(AiPath path) throws StopRequestException
	{
		checkInterruption();
		this.path = path;
	}
	
	/**
	 * 
	 * @return
	 * 		?
	 * @throws StopRequestException
	 */
	public List<AiTile> getPassedTiles() throws StopRequestException
	{
		checkInterruption();
		return allPassedTiles;
	}
}
