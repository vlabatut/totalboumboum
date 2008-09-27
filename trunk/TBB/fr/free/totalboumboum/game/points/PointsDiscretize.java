package fr.free.totalboumboum.game.points;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsDiscretize extends PointsProcessor
{
	private PointsProcessor source;
	/*
	 * example:
	 * thresholds:     | 2 | 5 | 12 | 33 |
	 * values:       | 1 | 2 | 4  |  6 | 10 |
	 */
	private float[] thresholds;
	private float[] values;
	
	public PointsDiscretize(PointsProcessor source, float[] thresholds, float[] values)
	{	this.source = source;
		this.thresholds = thresholds;
		this.values = values;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		float[] temp = source.process(stats);
		
		// process
		for(int i=0;i<temp.length;i++)
		{	int j=0;
			boolean found = false;
			do
			{	float threshold = thresholds[j];
				if(temp[i]<=threshold)
				{	result[i] = values[j];
					found = true;
				}
				else
					j++;
			}
			while(!found && j<thresholds.length);
			if(!found)
				result[i] = values[j];
		}

		return result;
	}
}
