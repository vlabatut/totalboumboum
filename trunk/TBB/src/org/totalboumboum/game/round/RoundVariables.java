package org.totalboumboum.game.round;

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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.totalboumboum.configuration.Configuration;
import org.totalboumboum.engine.container.level.Level;
import org.totalboumboum.engine.container.level.info.LevelInfo;
import org.totalboumboum.engine.container.level.instance.Instance;
import org.totalboumboum.engine.loop.ServerLoop;
import org.totalboumboum.engine.loop.event.ReplayEvent;
import org.totalboumboum.gui.tools.MessageDisplayer;
import org.totalboumboum.tools.GameData;
import org.totalboumboum.tools.files.FileNames;
import org.totalboumboum.tools.files.FilePaths;

public class RoundVariables
{
	/////////////////////////////////////////////////////////////////
	// INSTANCE PATH		/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Instance instance;
	
	/////////////////////////////////////////////////////////////////
	// GAME				/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static Level level;
	public static ServerLoop loop;
	
	/////////////////////////////////////////////////////////////////
	// SCALE			/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static double zoomFactor;
	public static double toleranceCoefficient = 1;
	public static double scaledTileDimension;

	public static void setZoomFactor(double zoomFactor)
	{	RoundVariables.zoomFactor = zoomFactor;
		toleranceCoefficient = zoomFactor*GameData.TOLERANCE;
		scaledTileDimension = GameData.STANDARD_TILE_DIMENSION*zoomFactor;
	}

	/////////////////////////////////////////////////////////////////
	// PRE-ROUND MESSAGES	/////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static MessageDisplayer messageDisplayers[] = null;
	
	public static void initMessageDisplayers(String texts[])
	{	if(messageDisplayers == null)
		{	messageDisplayers = new MessageDisplayer[texts.length];
			Dimension dim = Configuration.getVideoConfiguration().getPanelDimension();
			double coef = 0.9;
			Font displayedTextFont = loop.getPanel().getMessageFont(dim.width*coef,dim.height*coef);
			displayedTextFont = displayedTextFont.deriveFont(Font.BOLD);
			int xc = (int)Math.round(dim.width/2);
			int yc = (int)Math.round(dim.height/2);
			for(int i=0;i<texts.length;i++)
			{	if(texts[i]!=null)
				{	MessageDisplayer temp = new MessageDisplayer(displayedTextFont,xc,yc);
					temp.setFatten(3);
					temp.setTextColor(new Color(204, 18,128));
					temp.updateText(texts[i]);
					messageDisplayers[i] = temp;
				}
				else
					messageDisplayers[i] = null;
			}
		}
	}

	/////////////////////////////////////////////////////////////////
	// RECORDING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static boolean filterEvents = true;
	public static ObjectOutputStream out = null;
	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	public static void initRecording(LevelInfo levelInfo) throws IOException
	{	if(Configuration.getEngineConfiguration().isRecordRounds())
		{	String filename = FilePaths.getReplaysPath();
			Calendar calendar = GregorianCalendar.getInstance();
//			Date date = calendar.getTime();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH)+1;
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int second = calendar.get(Calendar.SECOND);
			NumberFormat nf = NumberFormat.getIntegerInstance();
			nf.setMinimumIntegerDigits(2);
			filename = filename + File.separator + year + "." + nf.format(month) + "." + nf.format(day) + ".";
			filename = filename + nf.format(hourOfDay) + "." + nf.format(minute) + "." + nf.format(second) + ".";
			filename = filename + levelInfo.getPackName() + "." + levelInfo.getFolder() + FileNames.EXTENSION_DATA;
			File file = new File(filename);
			FileOutputStream fileOut = new FileOutputStream(file);
			BufferedOutputStream outBuff = new BufferedOutputStream(fileOut);
//			ZipOutputStream outZip = new ZipOutputStream(outBuff);
//			out = new ObjectOutputStream(outZip);
			out = new ObjectOutputStream(outBuff);
		}
	}
	
	/**
	 * records an event in the currently open stream.
	 */
	public static void recordEvent(ReplayEvent event)
	{	if(out!=null && event.getSendEvent())
		{	try
			{	out.writeObject(event);
				System.out.println("recording: "+event);
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
		}
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public static void finishRecording() throws IOException
	{	if(out!=null)
			out.close();
	}

	/////////////////////////////////////////////////////////////////
	// REPLAYING		/////////////////////////////////////////////
	/////////////////////////////////////////////////////////////////
	public static ObjectInputStream in = null;
	
	/**
	 * creates and open a file named after the current date and time
	 * in order to record this game replay
	 */
	public static void initReplaying(String filename) throws IOException
	{	File file = new File(filename);
		FileInputStream fileIn = new FileInputStream(file);
		BufferedInputStream inBuff = new BufferedInputStream(fileIn);
//		ZipInputStream inZip = new ZipInputStream(inBuff);
//		in = new ObjectInputStream(inZip);
		in = new ObjectInputStream(inBuff);
	}
	
	/**
	 * records an event in the currently open stream.
	 */
	public static ReplayEvent loadEvent(ReplayEvent event)
	{	ReplayEvent result = null;
		if(in!=null)
		{	try
			{	Object object = in.readObject();
				result = (ReplayEvent) object;
				System.out.println("reading: "+result);
			}
			catch (EOFException e) 
			{	//
			}
			catch (IOException e)
			{	e.printStackTrace();
			}
			catch (ClassNotFoundException e)
			{	e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * close the replay output stream (if it was previously opened)
	 */
	public static void finishReplaying() throws IOException
	{	if(in!=null)
			in.close();
	}
}
