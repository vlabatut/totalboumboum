package org.totalboumboum.statistics.detailed;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class StatisticsLoader
{
	public static Statistics loadStatistics() throws IOException, ClassNotFoundException
	{	Statistics result;
		String statisticsFolder = FilePaths.getStatisticsPath();
		String individualFolder = statisticsFolder+File.separator+FileNames.FILE_STATISTICS;
		File file = new File(individualFolder);
		InputStream in = new FileInputStream(file);
		BufferedInputStream inBuf = new BufferedInputStream(in);
		ObjectInputStream ois = new ObjectInputStream(inBuf);
		result = (Statistics)ois.readObject();
		ois.close();
		return result;
	}
	
	public static void saveStatistics(Statistics stats) throws IOException
	{	String statisticsFolder = FilePaths.getStatisticsPath();
		String individualFolder = statisticsFolder+File.separator+FileNames.FILE_STATISTICS;
		File file = new File(individualFolder);
		OutputStream out = new FileOutputStream(file);
		BufferedOutputStream outBuf = new BufferedOutputStream(out);
		ObjectOutputStream oos = new ObjectOutputStream(outBuf);
		oos.writeObject(stats);
		oos.close();
	}
}
