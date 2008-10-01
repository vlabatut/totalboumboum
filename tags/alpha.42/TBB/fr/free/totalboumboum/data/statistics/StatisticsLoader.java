package fr.free.totalboumboum.data.statistics;

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
