package fr.free.totalboumboum.game.point;

import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointRankings implements PointProcessor
{
	private PointProcessor source;
	private boolean inverted;
	
	public PointRankings(PointProcessor source, boolean inverted)
	{	this.source = source;
		this.inverted = inverted;
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	float[] result;
		if(inverted)
			result = processInverted(stats);
		else
			result = processNormal(stats);
		return result;
	}
	
	public float[] processNormal(StatisticBase stats)
	{	// init
		ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;
		float[] temp = source.process(stats);
		// process
		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<temp.length;j++)
				if(temp[j]>temp[i])
					result[i] = result[i] + 1;
				else if(temp[j]<temp[i])
					result[j] = result[j] + 1;
		}
		//
		return result;
	}
	
	public float[] processInverted(StatisticBase stats)
	{	ArrayList<String> players = stats.getPlayers();
		float[] result = new float[players.size()];
		for(int i=0;i<result.length;i++)
			result[i] = 1;
		float[] temp = source.process(stats);
		// process
		for(int i=0;i<result.length-1;i++)
		{	for(int j=i+1;j<temp.length;j++)
				if(temp[j]<temp[i])
					result[i] = result[i] + 1;
				else
					result[j] = result[j] + 1;
		}
		//
		return result;
	}
}
