package tournament200910.adatepeozbek.v2;

import java.util.ArrayList;
import java.util.List;

import fr.free.totalboumboum.ai.adapter200910.ArtificialIntelligence;
import fr.free.totalboumboum.ai.adapter200910.communication.AiAction;
import fr.free.totalboumboum.ai.adapter200910.communication.AiActionName;
import fr.free.totalboumboum.ai.adapter200910.communication.StopRequestException;
import fr.free.totalboumboum.ai.adapter200910.data.AiHero;
import fr.free.totalboumboum.ai.adapter200910.data.AiTile;
import fr.free.totalboumboum.ai.adapter200910.data.AiZone;
import fr.free.totalboumboum.ai.adapter200910.path.*;

public class Senacan extends ArtificialIntelligence 
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
