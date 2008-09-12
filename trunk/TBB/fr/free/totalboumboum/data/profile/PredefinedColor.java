package fr.free.totalboumboum.data.profile;

import java.awt.Color;

public enum PredefinedColor 
{
	BLACK,
	BLUE,
	BROWN,
	CYAN,
	GRASS,
	GREEN,
	GREY,
	INDIGO,
	ORANGE,
	PINK,
	PURPLE,
	RED,
	RUST,
	ULTRAMARINE,
	WHITE,
	YELLOW;
	
	public Color getColor()
	{	Color result;
		switch(this)
		{	case BLACK:
				result = new Color(0,0,0);
				break;
			case BLUE:
				result = new Color(3,3,250);
				break;
			case BROWN:
				result = new Color(82,33,14);
				break;
			case CYAN:
				result = new Color(9,240,252);
				break;
			case GRASS:
				result = new Color(73,229,41);
				break;
			case GREEN:
				result = new Color(24,109,9);
				break;
			case GREY:
				result = new Color(115,106,108);
				break;
			case INDIGO:
				result = new Color(65,11,169);
				break;
			case ORANGE:
				result = new Color(255,118,6);
				break;
			case PINK:
				result = new Color(209,76,117);
				break;
			case PURPLE:
				result = new Color(149,114,234);
				break;
			case RED:
				result = new Color(236,34,38);
				break;
			case RUST:
				result = new Color(196,57,2);
				break;
			case ULTRAMARINE:
				result = new Color(10,10,78);
				break;
			case WHITE:
				result = new Color(253,253,253);
				break;
			case YELLOW:
				result = new Color(253,217,0);
				break;
			default:
				result = Color.WHITE;
				break;
		}
		return result;
	}
}
