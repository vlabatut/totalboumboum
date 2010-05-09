package fr.free.totalboumboum.game.point;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointDiscretize implements PointProcessor
{
	private PointProcessor source;
	/**
	 * example:
	 * thresholds:     | 2 | 5 | 12 | 33 |
	 * values:       | 1 | 2 | 4  |  6 | 10 |
	 */
	private float[] thresholds;
	private float[] values;
	private boolean exaequoShare;
	
	public PointDiscretize(PointProcessor source, float[] thresholds, float[] values, boolean exaequoShare)
	{	this.source = source;
		this.thresholds = thresholds;
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
		if(exaequoShare && source instanceof PointRankings)
		{	int[] count = new int[values.length];
			for(int i=0;i<count.length;i++)
				count[i] = 0;
			for(int i=0;i<temp.length;i++)
			{	int j=0;
				boolean found = false;
				do
				{	float threshold = thresholds[j];
					if(temp[i]<=threshold)
					{	count[j] = count[j] + 1;
						found = true;
					}
					else
						j++;
				}
				while(!found && j<thresholds.length);
				if(!found)
					count[j] = count[j] + 1;
			}
			for(int i=0;i<count.length;i++)
			{	float pts = 0;
				if(count[i]>0)
				{	for(int j=0;j<count[i];j++)
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
		{	int j=0;
			boolean found = false;
			do
			{	float threshold = thresholds[j];
				if(temp[i]<=threshold)
				{	result[i] = values2[j];
					found = true;
				}
				else
					j++;
			}
			while(!found && j<thresholds.length);
			if(!found)
				result[i] = values2[j];
		}
		//
		return result;
	}
}
