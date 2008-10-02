package fr.free.totalboumboum.ai;

import fr.free.totalboumboum.engine.player.Player;

public interface InterfaceAi
{
	public void setPlayer(Player player);
	public void update();
	public void finish();
	
}
