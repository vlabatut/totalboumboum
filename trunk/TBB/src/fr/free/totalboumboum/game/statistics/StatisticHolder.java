package fr.free.totalboumboum.game.statistics;

import java.util.ArrayList;

import fr.free.totalboumboum.configuration.profile.Profile;

public interface StatisticHolder
{
	public StatisticBase getStats();
	public ArrayList<Profile> getProfiles();
	public ArrayList<Boolean> getPlayersStatus();
}
