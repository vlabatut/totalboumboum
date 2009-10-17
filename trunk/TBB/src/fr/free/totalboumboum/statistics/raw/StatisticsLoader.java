package fr.free.totalboumboum.statistics.raw;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import fr.free.totalboumboum.tools.FileTools;


public class StatisticsLoader
{
	public static Statistics loadStatistics() throws IOException, ClassNotFoundException
	{	Statistics result;
		String statisticsFolder = FileTools.getStatisticsPath();
		String individualFolder = statisticsFolder+File.separator+FileTools.FILE_STATISTICS;
		File file = new File(individualFolder);
		InputStream in = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(in);
		result = (Statistics)ois.readObject();
		return result;
	}
	
	public static void saveStatistics(Statistics stats) throws IOException
	{	String statisticsFolder = FileTools.getStatisticsPath();
		String individualFolder = statisticsFolder+File.separator+FileTools.FILE_STATISTICS;
		File file = new File(individualFolder);
		OutputStream out = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(stats);
	}
}
