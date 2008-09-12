package fr.free.totalboumboum.data.statistics;

import java.util.ArrayList;

import fr.free.totalboumboum.game.score.Score;

public interface StatisticBase
{
	public ArrayList<String> getPlayers();
	public long[] getScores(Score score);
	public float[] getPartialPoints();
}
