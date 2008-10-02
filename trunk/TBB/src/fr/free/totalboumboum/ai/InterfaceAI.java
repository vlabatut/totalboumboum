package fr.free.totalboumboum.ai;

import fr.free.totalboumboum.engine.player.Player;

public interface InterfaceAI
{
	public void setPlayer(Player player);
	public void setClass(String c);
	public void update();
	public void finish();
	
}
