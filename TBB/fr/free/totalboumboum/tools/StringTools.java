package fr.free.totalboumboum.tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

public class StringTools
{
	public static String formatTimeWithHours(long time)
	{	String result;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		String hours = nf.format(time/3600000);	time = time%3600000;
		String minutes = nf.format(time/60000);	time = time%60000;
		String seconds = nf.format(time/1000);	time = time%1000;
		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		String milliseconds = nf.format(time);
		result = hours+"h"+minutes+"m"+seconds+"s"+milliseconds+"ms";
		return result;
	}

	public static String formatTimeWithSeconds(long time)
	{	String result;
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(2);
		String seconds = nf.format(time/1000);	time = time%1000;
		nf = NumberFormat.getInstance();
		nf.setMinimumIntegerDigits(3);
		String milliseconds = nf.format(time);
		result = seconds+"''"+milliseconds;
		return result;
	}
}
