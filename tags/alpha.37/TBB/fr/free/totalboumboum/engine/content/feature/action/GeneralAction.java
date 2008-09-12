package fr.free.totalboumboum.engine.content.feature.action;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.engine.content.feature.Contact;
import fr.free.totalboumboum.engine.content.feature.Direction;
import fr.free.totalboumboum.engine.content.feature.Orientation;
import fr.free.totalboumboum.engine.content.feature.TilePosition;


public class GeneralAction extends AbstractAction
{
	/** 
	 * directions of the action 
	 */
	private ArrayList<Direction> directions;
	/** 
	 * contacts between the actor and the target 
	 */
	private ArrayList<Contact> contacts;
	/** 
	 * compared directions of the target and the action  
	 */
	private ArrayList<Orientation> orientations;
	/** 
	 * positions of the target in termes of tile
	 */
	private ArrayList<TilePosition> tilePositions;
	/** 
	 * roles of the acting sprite 
	 */
	private ArrayList<Class<?>> actors;
	/** 
	 * role of the targeted sprite 
	 */
	private ArrayList<Class<?>> targets;

	public GeneralAction(String name)
	{	super(name);
		directions = new ArrayList<Direction>();
		contacts = new ArrayList<Contact>();
		orientations = new ArrayList<Orientation>();
		tilePositions =  new ArrayList<TilePosition>();
		actors = new ArrayList<Class<?>>(); 		
		targets = new ArrayList<Class<?>>();
	}
	
	public ArrayList<Direction> getDirections()
	{	return directions;
	}
	public void addDirection(Direction direction)
	{	directions.add(direction);
	}
	
	public ArrayList<Contact> getContacts()
	{	return contacts;
	}
	public void addContact(Contact contact)
	{	contacts.add(contact);
	}
	
	public ArrayList<Orientation> getOrientations()
	{	return orientations;
	}
	public void addOrientation(Orientation orientation)
	{	orientations.add(orientation);
	}
	
	public ArrayList<TilePosition> getTilePositions()
	{	return tilePositions;
	}
	public void addTilePosition(TilePosition tilePosition)
	{	tilePositions.add(tilePosition);
	}

	public ArrayList<Class<?>> getActors()
	{	return actors;
	}
	public void addActor(Class<?> actor)
	{	actors.add(actor);
	}
	public void setActor(Class<?> actor)
	{	actors.clear();
		actors.add(actor);
	}
	
	public ArrayList<Class<?>> getTargets()
	{	return targets;
	}
	public void addTarget(Class<?> target)
	{	targets.add(target);
	}
	public void setTarget(Class<?> actor)
	{	targets.clear();
		targets.add(actor);
	}

	public String toString()
	{	String result = name;
		// actors
		{	result = result+" ( ";
			Iterator<Class<?>> i = actors.iterator();
			while(i.hasNext())
			{	Class<?> a = i.next();
				result = result + a + " "; 
			}
			result = result+">> ";			
		}
		// targets
		{	Iterator<Class<?>> i = targets.iterator();
			while(i.hasNext())
			{	Class<?> a = i.next();
				result = result + a + " "; 
			}
			result = result+") ";			
		}
		// directions
		{	result = result+" [ ";
			Iterator<Direction> i = directions.iterator();
			while(i.hasNext())
			{	Direction d = i.next();
				result = result + d + " "; 
			}
			result = result+"] ";
		}
		return result;
	}

/*	
	public GeneralAction copy()
	{	GeneralAction result;
		result = new GeneralAction(name);
		result.setActor(actor);
		result.setTarget(target);
		result.setDirection(direction);
		result.setContact(contact);
		result.setOrientation(orientation);
		result.setTilePosition(tilePosition);
		return result;
	}
*/
	
	/**
	 * la specificAction pass�e en param�tre est elle g�n�ralis�e par 
	 * cette generalAction ?
	 */
	public boolean subsume(AbstractAction a)
	{	boolean result;
		if(a instanceof SpecificAction)
		{	SpecificAction action = (SpecificAction)a;
			// name
			result = name.equalsIgnoreCase(action.getName());
			// actor
			if(result)
				result = actors.contains(action.getActor().getClass());
			// target
			if(result && action.getTarget()!=null)
				result = targets.contains(action.getTarget().getClass());
			// direction
			if(result)
				result = directions.contains(action.getDirection());
			// contact
			if(result)
				result = contacts.contains(action.getContact());
			// orientation
			if(result)
				result = orientations.contains(action.getOrientation());
			// tile position
			if(result)
				result = tilePositions.contains(action.getTilePosition());
		}
		else //if(a instanceof GeneralAction)
		{	GeneralAction action = (GeneralAction)a;
			// name
			result = name.equalsIgnoreCase(action.getName());
			// actor
			if(result)
				result = actors.containsAll(action.getActors());
			// target
			if(result)
				result = targets.containsAll(action.getTargets());
			// direction
			if(result)
				result = directions.containsAll(action.getDirections());
			// contact
			if(result)
				result = contacts.containsAll(action.getContacts());
			// orientation
			if(result)
				result = orientations.containsAll(action.getOrientations());
			// tile position
			if(result)
				result = tilePositions.containsAll(action.getTilePositions());
		}
		return result;
	}

	public void finish()
	{	if(!finished)
		{	super.finish();
			// misc
			actors = null;
			contacts = null;
			directions = null;
			orientations = null;
			targets = null;
			tilePositions = null;
		}
	}
}
