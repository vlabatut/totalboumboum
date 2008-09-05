package fr.free.totalboumboum.engine.content.feature.action;

import org.w3c.dom.Element;

import fr.free.totalboumboum.data.configuration.Configuration;
import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;
import fr.free.totalboumboum.engine.content.sprite.block.Block;
import fr.free.totalboumboum.engine.content.sprite.bomb.Bomb;
import fr.free.totalboumboum.engine.content.sprite.fire.Fire;
import fr.free.totalboumboum.engine.content.sprite.floor.Floor;
import fr.free.totalboumboum.engine.content.sprite.hero.Hero;
import fr.free.totalboumboum.engine.content.sprite.item.Item;
import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.tools.ClassTools;
import fr.free.totalboumboum.tools.XmlTools;


public class GeneralActionLoader
{	
	
	public static GeneralAction loadActionElement(Element root) throws ClassNotFoundException
    {	// name
		String name = root.getAttribute(XmlTools.ATT_NAME).trim();
		GeneralAction result = new GeneralAction(name);
		
		// actor
		{	if(root.hasAttribute(XmlTools.ATT_ACTOR))
			{	String actorStr = root.getAttribute(XmlTools.ATT_ACTOR).trim();
				if(actorStr.equals(AbstractAction.ROLE_ALL))
				{	result.addActor(Block.class);
					result.addActor(Bomb.class);
					result.addActor(Fire.class);
					result.addActor(Floor.class);
					result.addActor(Hero.class);
					result.addActor(Item.class);
				}
				else if(actorStr.equals(AbstractAction.ROLE_NONE))
					result.addActor(null);
				else
					result.addActor(ClassTools.getClassFromRole(actorStr));
			}
		}
		
		// target
		{	if(root.hasAttribute(XmlTools.ATT_TARGET))
			{	String targetStr = root.getAttribute(XmlTools.ATT_TARGET).trim();
				if(targetStr.equals(AbstractAction.ROLE_ALL))
				{	result.addTarget(Block.class);
					result.addTarget(Bomb.class);
					result.addTarget(Fire.class);
					result.addTarget(Floor.class);
					result.addTarget(Hero.class);
					result.addTarget(Item.class);
				}
				else if(targetStr.equals(AbstractAction.ROLE_NONE))
					result.addTarget(null);
				else
					result.addTarget(ClassTools.getClassFromRole(targetStr));
			}
		}
		
		// direction
		{	String directionStr = root.getAttribute(XmlTools.ATT_DIRECTION).trim();
			if(directionStr.equals(AbstractAction.DIRECTION_ALL))
			{	for(Direction d : Direction.values())
					result.addDirection(d);
			}
			else
				result.addDirection(Direction.valueOf(directionStr.toUpperCase()));
		}
		
		// contact
		{	String contactStr = root.getAttribute(XmlTools.ATT_CONTACT).trim();
			if(contactStr.equals(AbstractAction.CONTACT_ALL))
			{	for(Contact c : Contact.values())
					result.addContact(c);
			}
			else
				result.addContact(Contact.valueOf(contactStr.toUpperCase()));
		}
		
		// tilePosition
		{	String tilePositionStr = root.getAttribute(XmlTools.ATT_TILE_POSITION).trim();
			if(tilePositionStr.equals(AbstractAction.TILE_POSITION_ALL))
			{	for(TilePosition c : TilePosition.values())
					result.addTilePosition(c);
			}
			else
				result.addTilePosition(TilePosition.valueOf(tilePositionStr.toUpperCase()));
		}
		
		// orientation
		{	String orientationStr = root.getAttribute(XmlTools.ATT_ORIENTATION).trim();
			if(orientationStr.equals(AbstractAction.ORIENTATION_ALL))
			{	for(Orientation c : Orientation.values())
					result.addOrientation(c);
			}
			else
				result.addOrientation(Orientation.valueOf(orientationStr.toUpperCase()));
		}
		
		// result
		return result;
    }
}
