package fr.free.totalboumboum.data.statistics;

import java.io.Serializable;

public enum StatisticAction implements Serializable
{	
	/* general */
	DROP_BOMB, LOSE_ITEM, GATHER_ITEM, KILL_PLAYER, 
	/* paint */
	WIN_TILE,
	/* crown */
	GATHER_CROWN, LOSE_CROWN
}
