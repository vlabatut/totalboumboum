package fr.free.totalboumboum.game.points;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public abstract class PointProcessor
{	
	public abstract float[] process(StatisticBase stats);
	
	private final ArrayList<String> notes = new ArrayList<String>();

	public void setNotes(ArrayList<String> notes)
	{	this.notes.addAll(notes);
	}
	public ArrayList<String> getNotes()
	{	return notes;
	}
}
