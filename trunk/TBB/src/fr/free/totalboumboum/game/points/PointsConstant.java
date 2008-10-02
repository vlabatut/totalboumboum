package fr.free.totalboumboum.game.points;

import java.text.NumberFormat;

import fr.free.totalboumboum.data.statistics.Score;
import fr.free.totalboumboum.data.statistics.StatisticBase;

public class PointsConstant extends PointsProcessor implements PPConstant
{	
	private float value;
	
	public PointsConstant(float value)
	{	this.value = value;	
	}
	
	public float getValue()
	{	return value;	
	}
	
	@Override
	public float[] process(StatisticBase stats)
	{	int nbr = stats.getPlayers().size();
		float result[] = new float[nbr];
		for(int i=0;i<nbr;i++)
			result[i] = value;
		return result;
	}

	@Override
	public String toString()
	{	// init
		StringBuffer result = new StringBuffer();
		// value
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		String val = nf.format(value);
		result.append(val);
		// result
		return result.toString();
	}
}
