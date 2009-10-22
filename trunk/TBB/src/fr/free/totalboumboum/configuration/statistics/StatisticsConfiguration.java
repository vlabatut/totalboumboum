package fr.free.totalboumboum.configuration.statistics;

/*
 * Total Boum Boum
 * Copyright 2008-2009 Vincent Labatut 
 * 
 * This file is part of Total Boum Boum.
 * 
 * Total Boum Boum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Total Boum Boum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Total Boum Boum.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

public class StatisticsConfiguration
{

	public StatisticsConfiguration copy()
	{	StatisticsConfiguration result = new StatisticsConfiguration();

		result.setDefaultRating(defaultRating);
		result.setDefaultRatingDeviation(defaultRatingDeviation);
		result.setDefaultRatingVolatility(defaultRatingVolatility);
		result.setGamesPerPeriod(gamesPerPeriod);
		
		result.setIncludeSimulations(includeSimulations);
		result.setIncludeQuickStarts(includeQuickStarts);
		
		return result;
	}

	/////////////////////////////////////////////////////////////////
	// GLICKO-2			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** default rating */
	private int defaultRating = 1500;
	/** default rating deviation */
	private int defaultRatingDeviation = 350;
	/** default rating volatility */
	private float defaultRatingVolatility = 0.06f;
	/** default rating volatility */
	private int gamesPerPeriod = 15;

	public int getDefaultRating()
	{	return defaultRating;
	}

	public void setDefaultRating(int defaultRating)
	{	this.defaultRating = defaultRating;
	}

	public int getDefaultRatingDeviation()
	{	return defaultRatingDeviation;
	}

	public void setDefaultRatingDeviation(int defaultRatingDeviation)
	{	this.defaultRatingDeviation = defaultRatingDeviation;
	}

	public float getDefaultRatingVolatility()
	{	return defaultRatingVolatility;
	}

	public void setDefaultRatingVolatility(float defaultRatingVolatility)
	{	this.defaultRatingVolatility = defaultRatingVolatility;
	}

	public int getGamesPerPeriod()
	{	return gamesPerPeriod;
	}

	public void setGamesPerPeriod(int gamesPerPeriod)
	{	this.gamesPerPeriod = gamesPerPeriod;
	}

	/////////////////////////////////////////////////////////////////
	// INCLUSIONS		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	/** include quick starts in statistics */
	private boolean includeQuickStarts = false;
	/** include simulated rounds in statistics */
	private boolean includeSimulations = false;

	public boolean getIncludeQuickStarts()
	{	return includeQuickStarts;
	}

	public void setIncludeQuickStarts(boolean includeQuickStarts)
	{	this.includeQuickStarts = includeQuickStarts;
	}

	public boolean getIncludeSimulations()
	{	return includeSimulations;
	}

	public void setIncludeSimulations(boolean includeSimulations)
	{	this.includeSimulations = includeSimulations;
	}
}
