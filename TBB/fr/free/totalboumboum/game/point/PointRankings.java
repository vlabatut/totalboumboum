package fr.free.totalboumboum.game.point;

import java.util.ArrayList;
import java.util.Iterator;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointRankings implements PointProcessor
{
	private ArrayList<PointProcessor> sources = new ArrayList<PointProcessor>();
	private boolean inverted;
	
	public PointRankings(ArrayList<PointProcessor> sources, boolean inverted)
	{	this.sources.addAll(sources);
		this.inverted = inverted;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// source
		ArrayList<float[]> values = new ArrayList<float[]>();
		Iterator<PointProcessor> it = sources.iterator();
		while (it.hasNext())
		{	PointProcessor source = it.next();
			values.add(source.process(stats));
		}
		// result
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;
		// process		
		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<result.length;j++)
			{	int cpr = comparePoints(j,i,values,inverted);
				if(cpr>0)
					result[i] = result[i] + 1;
				else if(cpr<0)
					result[j] = result[j] + 1;
			}
		}		
		return result;
	}
	
	/**
	 * Compares two players according to their points.
	 * The various sources are considered according to their order in the list. 
	 */
	public int comparePoints(int i, int j, ArrayList<float[]> values, boolean inverted)
	{	// inverted ?
		if(inverted)
		{	int temp = i;
			i = j;
			j = temp;
		}
		// process
		int result = 0;
		Iterator<float[]> it = values.iterator();
		while (it.hasNext() && result==0)
		{	float[] temp = it.next();
			if(temp[i]<temp[j])
				result = -1;
			else if(temp[i]>temp[j])
				result = +1;
		}
		return result;
	}		
}
