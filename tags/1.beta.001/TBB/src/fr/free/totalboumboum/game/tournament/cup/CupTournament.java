package fr.free.totalboumboum.game.tournament.cup;

/*
 * Total Boum Boum
 * Copyright 2008 Vincent Labatut 
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

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.tournament.AbstractTournament;


public class CupTournament extends AbstractTournament
{

	@Override
	public void matchOver() {
		// 
		
	}

	@Override
	public void finish() {
		// 
		
	}

	@Override
	public Match getCurrentMatch() {
		// 
		return null;
	}

	@Override
	public void init() throws IllegalArgumentException, SecurityException,
			ParserConfigurationException, SAXException, IOException,
			IllegalAccessException, NoSuchFieldException,
			ClassNotFoundException {
		// 
		
	}

	@Override
	public boolean isOver() {
		// 
		return false;
	}

	@Override
	public boolean isReady() {
		// 
		return false;
	}

	@Override
	public void progress() {
		// 
		
	}

	@Override
	public void updatePlayerNumber() {
		// 
		
	}
	
}