package fr.free.totalboumboum.data.statistics;

import java.util.ArrayList;


public interface StatisticBase
{
	public ArrayList<String> getPlayers();
	public long[] getScores(Score score);
	public float[] getPartialPoints();
	public int getConfrontationCount();
}
