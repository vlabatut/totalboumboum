package tournament200910.adatepeozbek.v3;

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

public class AdatepeOzbek extends ArtificialIntelligence 
{	
	private AiHero ownHero = null;
	private AiZone zone = null;
	private AiAction actionToDo = null;
	private AiPath path = null;
	private List<AiTile> allPassedTiles = null;
	private boolean debug = false;
	public boolean canTheyReachMe = false;
	
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
