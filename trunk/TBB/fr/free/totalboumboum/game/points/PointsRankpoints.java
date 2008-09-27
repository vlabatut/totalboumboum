package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsRankpoints extends PointsProcessor
{
	/*
	 * Ranks according to the sources and gives points :
	 * values = {pts for #1, pts for #2, ...}
	 */
	private PointsRankings source;
	private float[] values;
	private boolean exaequoShare;
	
	public PointsRankpoints(ArrayList<PointsProcessor> sources, float[] values, boolean inverted, boolean exaequoShare)
	{	this.source = new PointsRankings(sources,inverted);
		this.values = values;
		this.exaequoShare = exaequoShare;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] temp = source.process(stats);
		float[] values2 = new float[values.length];
		
		// count
		if(exaequoShare)
		{	// init
			int[] count = new int[values.length];
			for(int i=0;i<count.length;i++)
				count[i] = 0;
			// process the rankings
			for(int i=0;i<temp.length;i++)
			{	int index = (int)temp[i]-1;
				if(index<count.length)
					count[index]++;
//				else
//					count[count.length-1]++;			
			}
			// process the points
			for(int i=0;i<count.length;i++)
			{	float pts = 0;
				if(count[i]>0)
				{	for(int j=0;j<count[i] && (i+j)<count.length;j++)
						pts = pts + values[i+j];
					pts = pts / count[i];
				}
				values2[i] = pts;
			}
		}
		else
			values2 = values;
		
		// process
		for(int i=0;i<temp.length;i++)
		{	int index = (int)temp[i]-1;
			if(index<values2.length)
				result[i] = values2[index];
			else
				result[i] = 0;
		}

		return result;
	}
}
