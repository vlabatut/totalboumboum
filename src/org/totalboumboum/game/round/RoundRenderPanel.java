package org.totalboumboum.game.round;

/*
 * Total Boum Boum
 * Copyright 2008-2014 Vincent Labatut 
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

/**
 * Interface which must be implemented
 * by the GUI object used to display a round info.
 * 
 * @author Vincent Labatut
 */
public interface RoundRenderPanel
{	/**
	 * Method called when the round is finished.
	 */
	public void roundOver();
	
	/**
	 * Method called when one step is performed
	 * during the round loading.
	 */
	public void loadStepOver();
	
	/**
	 * Method called when one step is performed
	 * during the round simulation.
	 */
	public void simulationStepOver();
	
	/**
	 * Method called when the round simulation
	 * is completely finished.
	 */
	public void simulationOver();
}
