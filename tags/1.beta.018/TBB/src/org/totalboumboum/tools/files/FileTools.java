package org.totalboumboum.tools.files;

/*
 * Total Boum Boum
 * Copyright 2008-2010 Vincent Labatut 
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
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author Vincent Labatut
 *
 */
public class FileTools
{
	/**
	 * parse an array of file object and returns the one corresponding
	 * to the specified filename
	 */
	public static File getFile(String fileName, File[] list)
	{	File result = null;
		int i = 0;
		while(i<list.length && result==null)
		{	String fName = list[i].getName();
			if(fName.equalsIgnoreCase(fileName))
				result = list[i];
			else
				i++;				
		}
		return result;
	}
	
	public static void deleteDirectory(File dir)
	{	if(dir.exists() && dir.isDirectory())
		{	File[] files = dir.listFiles();
			for(File f: files)
			{	if(f.isFile())
					f.delete();
				else
					deleteDirectory(f);
			}
			dir.delete();
		}		
	}
	
	public static void copyDirectory(File source, File target) throws IOException
	{	// recursively copy a directory
		if (source.isDirectory())
		{	if (!target.exists())
				target.mkdir();
            String[] files = source.list();
            for(int i=0;i<files.length;i++)
            {	String fileName = files[i];
            	File src = new File(source, fileName);
            	File trgt = new File(target, fileName);
            	copyDirectory(src,trgt);
            }
		}
		// or bit-to-bit copy a file
		else
		{	copyFile(source,target);
        }
	}
	
	public static void copyFile(File source, File target) throws IOException
	{	FileInputStream in = new FileInputStream(source);
		FileOutputStream out = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int len;
        
        while ((len=in.read(buf))>0)
        	out.write(buf, 0, len);
        
        in.close();
        out.close();
	}

	public static void copyFile(String source, String target) throws IOException
	{	File fileSource = new File(source);
		File fileTarget = new File(target);
		copyFile(fileSource,fileTarget);
	}
	
	public static String getFilenameCompatibleCurrentTime()
	{	Calendar cal = new GregorianCalendar();
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		int sec = cal.get(Calendar.SECOND);
		String secStr = nf.format(sec);
		int min = cal.get(Calendar.MINUTE);
		String minStr = nf.format(min);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		String hourStr = nf.format(hour);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		String dayStr = nf.format(day);
		int month = cal.get(Calendar.MONTH);
		String monthStr = nf.format(month);
		int year = cal.get(Calendar.YEAR);
		String yearStr = Integer.toString(year);
		String result = yearStr+"."+monthStr+"."+dayStr+"_"+hourStr+"."+minStr+"."+secStr;
		return result;
	}
}
