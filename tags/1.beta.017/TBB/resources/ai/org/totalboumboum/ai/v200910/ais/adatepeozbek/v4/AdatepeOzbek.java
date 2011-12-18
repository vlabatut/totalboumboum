package org.totalboumboum.ai.v200910.ais.adatepeozbek.v4;

import java.util.ArrayList;
import java.util.List;

import org.totalboumboum.ai.v200910.adapter.ArtificialIntelligence;
import org.totalboumboum.ai.v200910.adapter.communication.AiAction;
import org.totalboumboum.ai.v200910.adapter.communication.AiActionName;
import org.totalboumboum.ai.v200910.adapter.communication.StopRequestException;
import org.totalboumboum.ai.v200910.adapter.data.AiHero;
import org.totalboumboum.ai.v200910.adapter.data.AiTile;
import org.totalboumboum.ai.v200910.adapter.data.AiZone;
import org.totalboumboum.ai.v200910.adapter.path.*;


public class AdatepeOzbek extends ArtificialIntelligence 
{	
	private AiHero ownHero = null;
	private AiZone zone = null;
	private AiAction actionToDo = null;
	private AiPath path = null;
	private List<AiTile> allPassedTiles = null;
	private boolean debug = false;
	public boolean canTheyReachMe = false;
	public TriedTiles triedTiles = null;
	public List<Enemy> enemies = null;
	public int urgentBombs = 0 ;
	public AiTile lastBombedTile = null;
	public long idleTime = 0;
	
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
			
			// Ajoute toutes les cases visit�s dans le liste
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
	
	public AiAction getActionToDo() throws StopRequestException
	{
		checkInterruption();
		
		return actionToDo;
	}
	
	public void setActionToDo(AiAction action) throws StopRequestException
	{
		checkInterruption();
		
		actionToDo = action;
	}
	
	public AiHero getOwnHero() throws StopRequestException
	{
		checkInterruption();
		return ownHero;
	}
	
	public AiZone getZone() throws StopRequestException
	{
		checkInterruption();
		return zone;
	}	
	
	public AiPath getPath() throws StopRequestException
	{
		checkInterruption();
		return path;
	}
	
	public void setPath(AiPath path) throws StopRequestException
	{
		checkInterruption();
		this.path = path;
	}
	
	public List<AiTile> getPassedTiles() throws StopRequestException
	{
		checkInterruption();
		return allPassedTiles;
	}
}