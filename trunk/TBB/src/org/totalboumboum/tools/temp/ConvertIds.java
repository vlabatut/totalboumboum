package org.totalboumboum.tools.temp;

/*
 * Total Boum Boum
 * Copyright 2008-2013 Vincent Labatut 
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;

import org.totalboumboum.statistics.glicko2.Glicko2Loader;
import org.totalboumboum.statistics.glicko2.Glicko2Saver;
import org.totalboumboum.statistics.glicko2.jrs.RankingService;
import org.totalboumboum.statistics.overall.OverallStatsLoader;
import org.totalboumboum.statistics.overall.OverallStatsSaver;
import org.totalboumboum.statistics.overall.PlayerStats;
import org.xml.sax.SAXException;

/**
 * CLass used to convert from the previous
 * type of player id. Not used anymore,
 * kept only as a reference.
 * 
 * @deprecated This class is not useful anymore.
 * 
 * @author Vincent Labatut
 *
 */
public class ConvertIds
{
	/**
	 * Converts old to new ids.
	 * 
	 * @param args
	 * 		None needed.
	 * @throws IOException
	 * 		--
	 * @throws ClassNotFoundException
	 * 		--
	 * @throws IllegalArgumentException
	 * 		--
	 * @throws SecurityException
	 * 		--
	 * @throws ParserConfigurationException
	 * 		--
	 * @throws SAXException
	 * 		--
	 * @throws IllegalAccessException
	 * 		--
	 * @throws NoSuchFieldException
	 * 		--
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	displayNewId();
		//generateIds();
		//convertIds();
	}
	
	/**
	 * Display some new id (test).
	 */
	private static void displayNewId()
	{	UUID id = UUID.randomUUID();
		System.out.println(id);
	}
	
	/**
	 * Generates new ids (test).
	 */
	@SuppressWarnings("unused")
	private static void generateIds()
	{	// generate new ids
		for(int i=0;i<49;i++)
			displayNewId();
		
		// check size
		System.out.println(getUUIDs().size());
	}
	
	/** 
	 * Maps old to new ids.
	 *  
	 * @return
	 * 		Id map. 
	 */
	private static HashMap<String,UUID> getUUIDs()
	{	HashMap<String,UUID> result = new HashMap<String, UUID>();
		result.put("1", UUID.fromString("d6aa991b-f803-4074-8259-220e648a8cd5"));
		result.put("10",UUID.fromString("02226a8f-e736-4f28-8772-5143952e861c"));
		result.put("11",UUID.fromString("127620c6-fca0-4804-9e11-19b6bcee883b"));
		result.put("12",UUID.fromString("128fbe6a-0de2-4cb9-9625-bb461c4962e6"));
		result.put("13",UUID.fromString("c1f4127a-f70d-447e-94ee-679a66bb08ad"));
		result.put("14",UUID.fromString("6a64cee4-173c-41c7-a6e4-e00434ca73f8"));
		result.put("15",UUID.fromString("04064071-3aee-4319-a871-e7f5579fbbff"));
		result.put("16",UUID.fromString("48ecfdd6-9904-4e80-be3a-2d41277ded4e"));
		result.put("17",UUID.fromString("0ec23e1a-67b3-46a7-89ff-90aa4d6c581d"));
		result.put("18",UUID.fromString("edd5a2e2-9ce4-4523-b76a-192733dd588f"));
		result.put("2", UUID.fromString("79558c2f-52d6-4855-9c1b-e2f71881d45d"));
		result.put("27",UUID.fromString("26db3658-cf0e-4650-96f4-55080b4720f8"));
		result.put("3", UUID.fromString("da8e37f2-e75e-4e07-b18c-317a9283d47d"));
		result.put("32",UUID.fromString("22e045ae-1ce7-4eac-b28a-4960d486eef1"));
		result.put("4", UUID.fromString("dd226be2-5b7a-4017-a415-963b546bb716"));
		result.put("49",UUID.fromString("ba47c16d-8657-419b-99a4-ade71c8c77ea"));
		result.put("5", UUID.fromString("fa0e458a-a12b-41ef-921a-e494933ef80c"));
		result.put("50",UUID.fromString("af92d497-7975-4dcf-b05d-89e706c3eec3"));
		result.put("51",UUID.fromString("0d3dfd83-6632-423f-b63e-d7e5743ad998"));
		result.put("52",UUID.fromString("6bdd7857-18dd-4128-b5d9-4d52543ba1f8"));
		result.put("53",UUID.fromString("bc8b7b98-7456-486a-a5b0-9cc9715d6e45"));
		result.put("54",UUID.fromString("05cf1752-e44b-452a-9fd9-7cff6070042b"));
		result.put("55",UUID.fromString("c3776dca-9424-4531-9e05-5da565d16bf7"));
		result.put("56",UUID.fromString("90d43698-a591-4f03-aa22-8e662f9d0b89"));
		result.put("57",UUID.fromString("cae2cc16-dddb-479b-a022-412de414ef0f"));
		result.put("58",UUID.fromString("1d8af34e-db74-4826-86e5-4d88492a27d3"));
		result.put("59",UUID.fromString("d8eecd8c-dda2-4e94-b1a8-87e0675d5b10"));
		result.put("6", UUID.fromString("062b8f25-1ca4-4c6f-9944-ce9753b09777"));
		result.put("60",UUID.fromString("9bed4abd-b956-4bf1-bf01-42d490d26dce"));
		result.put("61",UUID.fromString("c0a06ae0-4039-4a31-8a6a-7a1b0e72013e"));
		result.put("62",UUID.fromString("ee126aa0-f391-45a6-8159-0f2d7173b0ac"));
		result.put("63",UUID.fromString("a7e0a85a-819f-4d43-88c9-80fced007e4f"));
		result.put("64",UUID.fromString("553c0fb4-5418-4e33-b244-d5f399ae40f2"));
		result.put("65",UUID.fromString("cc207601-8afe-4c67-94d9-17cdc9bea77d"));
		result.put("69",UUID.fromString("8bba1aef-591e-484b-942a-89ee4aacb665"));
		result.put("7", UUID.fromString("5e4ee407-71c3-4220-9265-26253cc08f1a"));
		result.put("70",UUID.fromString("358f7188-1cb2-4852-9536-6e04f86da5e5"));
		result.put("71",UUID.fromString("77015d7c-ddf2-4b46-89b3-46a24d91f2d0"));
		result.put("72",UUID.fromString("8b561f4b-4614-4d7e-9371-4f134e014685"));
		result.put("73",UUID.fromString("ac0853de-bb8f-4141-8fa9-4a3b2708d331"));
		result.put("74",UUID.fromString("309ac62f-8346-4281-83ab-086902e9e39c"));
		result.put("75",UUID.fromString("24e6583c-8fbc-43e9-b841-1052986a963c"));
		result.put("76",UUID.fromString("bbe5482b-6fa0-4204-a5a6-43c4efff917c"));
		result.put("77",UUID.fromString("3c898e42-a3e2-4b6d-866e-f672ec19d326"));
		result.put("8", UUID.fromString("c6bea657-b93e-4e7c-8aa8-11eddca19f41"));
		result.put("88",UUID.fromString("27a298f3-ea71-4e96-8e5f-2b50862d4798"));
		result.put("9", UUID.fromString("c41afbee-c7b1-431e-9d9d-97e28a6ec13a"));
		result.put("90",UUID.fromString("40b32bca-16e3-4421-90bd-466e42c2c18a"));
		result.put("91",UUID.fromString("c27af211-5529-4e06-99e8-ca436f7cc755"));
		return result;
	}
	
	/**
	 * Converts old to new ids.
	 * 
	 * @throws IOException
	 * 		--
	 * @throws ClassNotFoundException
	 * 		--
	 * @throws IllegalArgumentException
	 * 		--
	 * @throws SecurityException
	 * 		--
	 * @throws ParserConfigurationException
	 * 		--
	 * @throws SAXException
	 * 		--
	 * @throws IllegalAccessException
	 * 		--
	 * @throws NoSuchFieldException
	 * 		--
	 */
	@SuppressWarnings("unused")
	private static void convertIds() throws IOException, ClassNotFoundException, IllegalArgumentException, SecurityException, ParserConfigurationException, SAXException, IllegalAccessException, NoSuchFieldException
	{	// load stats
		HashMap<String,PlayerStats> playersStats = OverallStatsLoader.loadOverallStatistics();
		RankingService glicko2Stats = Glicko2Loader.loadGlicko2Statistics();
		
		HashMap<String,UUID> new_ids = getUUIDs();
		List<String> oldIds = new ArrayList<String>(playersStats.keySet());
		for(String oldId: oldIds)
		{	// ids
			UUID uuid = new_ids.get(oldId);
			String newId = uuid.toString();
			// player stats
			PlayerStats playerStats = playersStats.get(oldId);
			playerStats.setPlayerId(newId);
			playersStats.remove(oldId);
			playersStats.put(newId,playerStats);
			// glicko stats
			if(glicko2Stats.getPlayers().contains(oldId))
				glicko2Stats.changePlayerId(oldId,newId);
		}

		// save stats
		OverallStatsSaver.saveOverallStatistics(playersStats);
		Glicko2Saver.saveGlicko2Statistics(glicko2Stats);		
	}
}
