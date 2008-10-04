package fr.free.totalboumboum.ai.adapter200809;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.feature.action.AbstractAction;
import fr.free.totalboumboum.engine.content.feature.action.SpecificAction;
import fr.free.totalboumboum.engine.content.feature.permission.TargetPermission;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;

public class AiBlock extends AiSprite<Block>
{
	public AiBlock(AiTile tile, Block sprite)
	{	super(tile,sprite);
		updateDestructible();
	}	
	
	@Override
	void update()
	{	super.update();
		updateDestructible();
	}

	@Override
	void finish()
	{	super.finish();
	}

	/////////////////////////////////////////////////////////////////
	// DESTRUCTIBLE		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** indique si ce mur peut être détruit par une bombe */
	private boolean destructible;
	
	/** 
	 * initialise l'indicateur de destructibilité de ce bloc 
	 */
	private void updateDestructible()
	{	Block sprite = getSprite();
		SpecificAction action = new SpecificAction(AbstractAction.CONSUME,new Fire(sprite.getLevel()),sprite,Direction.NONE,Contact.COLLISION,TilePosition.SAME,Orientation.SAME);
		TargetPermission perm = sprite.getTargetPermission(action);
		destructible = perm!=null;
	}	
	/** 
	 * renvoie vrai si ce bloc peut être détruit par une bombe, et faux sinon 
	 */
	public boolean isDestructible()
	{	return destructible;		
	}
}
