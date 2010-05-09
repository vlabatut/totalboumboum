package fr.free.totalboumboum.game.points;

import java.text.NumberFormat;
import java.util.ArrayList;

import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsDiscretize extends PointsProcessor implements PPFunction
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
	
	public PointsProcessor getSource()
	{	return source;	
	}
	public float[] getValues()
	{	return values;	
	}
	public float[] getThresholds()
	{	return thresholds;	
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

	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// function
		result.append("Discretize");
		result.append("(");
		// values
		result.append("<"); 
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		String thresholds2[] = new String[thresholds.length+2];
		thresholds2[0] = new Character('\u2212').toString()+new Character('\u221E').toString();
		for(int i=0;i<thresholds.length;i++)
			thresholds2[i+1] = nf.format(thresholds[i]);
		thresholds2[thresholds2.length-1] = new Character('\u002B').toString()+new Character('\u221E').toString();
		for(int i=0;i<values.length;i++)
		{	result.append("]");
			result.append(thresholds2[i]);
			result.append(";");
			result.append(thresholds2[i+1]);
			result.append("]->");
			result.append(nf.format(values[i]));
			result.append("; "); 
		}
		result.deleteCharAt(result.length()-1);
		result.append("> ; "); 
		// argument
		result.append("<"); 
		result.append(source.toString());
		result.append(">"); 
		// result
		result.append(")");
		return result.toString();
	}
}
