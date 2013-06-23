package org.totalboumboum.ai.v200910.ais.adatepeozbek.v2;

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

/**
 * 
 * @version 2
 * 
 * @author Can Adatape
 * @author Sena Ozbek
 *
 */
public class AdatepeOzbek extends ArtificialIntelligence 
{	
	private AiHero ownHero = null;
	private AiZone zone = null;
	private AiAction ActionToDo = null;
	private AiPath path = null;
	private List<AiTile> allPassedTiles = null;
	private boolean debug = false;
	
	@Override
	public AiAction processAction() throws StopRequestException
	{	checkInterruption(); //APPEL OBLIGATOIRE
		
		if(ownHero == null)
		{
			zone = getPercepts();
			ownHero = zone.getOwnHero();
			allPassedTiles = new ArrayList<AiTile>();
		}
	
		
		if(!ownHero.hasEnded())
		{
			
			// Ajoute toutes les cases visités dans le liste
			if(allPassedTiles.size() > 0)
			{
				if(allPassedTiles.get(allPassedTiles.size()-1) != ownHero.getTile())
					allPassedTiles.add(ownHero.getTile());
			}
			
			ActionToDo = new AiAction(AiActionName.NONE);
			
			AiEscape EscapeAI = new AiEscape(this);
			
			if(debug)
				EscapeAI.printTiles();
			
			
		}
		
		return ActionToDo;
	}
	
	public AiAction GetActionToDo() throws StopRequestException
	{
		checkInterruption();
		
		return ActionToDo;
	}
	
	public void SetActionToDo(AiAction action) throws StopRequestException
	{
		checkInterruption();
		
		ActionToDo = action;
	}
	
	public AiHero GetOwnHero() throws StopRequestException
	{
		checkInterruption();
		return ownHero;
	}
	
	public AiZone GetZone() throws StopRequestException
	{
		checkInterruption();
		return zone;
	}	
	
	public AiPath GetPath() throws StopRequestException
	{
		checkInterruption();
		return path;
	}
	
	public void SetPath(AiPath path) throws StopRequestException
	{
		checkInterruption();
		this.path = path;
	}
	
	public List<AiTile> GetPassedTiles() throws StopRequestException
	{
		checkInterruption();
		return allPassedTiles;
	}
}
