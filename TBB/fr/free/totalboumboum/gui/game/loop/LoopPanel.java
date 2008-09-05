package fr.free.totalboumboum.gui.game.loop;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fr.free.totalboumboum.engine.loop.Loop;
import fr.free.totalboumboum.engine.loop.LoopRenderPanel;
import fr.free.totalboumboum.game.match.Match;
import fr.free.totalboumboum.game.round.Round;
import fr.free.totalboumboum.gui.generic.MenuContainer;
import fr.free.totalboumboum.gui.generic.MenuPanel;
import fr.free.totalboumboum.gui.generic.SimpleMenuPanel;


public class LoopPanel extends SimpleMenuPanel implements LoopRenderPanel
{	private static final long serialVersionUID = 1L;
	private Loop loop;
	
	public LoopPanel(MenuContainer container, MenuPanel parent)
	{	super(container,parent);
		loop = getConfiguration().getTournament().getCurrentMatch().getCurrentRound().getLoop();
		setPreferredSize(getConfiguration().getPanelDimension());
		setDoubleBuffered(false);
		setBackground(Color.lightGray);
		setFocusable(true);
		Loop loop = getConfiguration().getTournament().getCurrentMatch().getCurrentRound().getLoop();
		loop.setPanel(this);
		// the JPanel now has focus, so receives key events
		// NOTE : surement à modifier, car un peu cra-cra (focus à donner à partir de l'extérieur)
		SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	requestFocus();	
			}
		});
		// thread
		Thread animator = new Thread(loop);
		animator.start();
	}

	// ----------------------------------------------

	// use active rendering to put the buffered image on-screen
	@Override
	public void paintScreen()
	{	Graphics g;
		try
		{	Image dbImage = loop.getImage();
			g = this.getGraphics();
			if ((g != null) && (dbImage != null))
				g.drawImage(dbImage, 0, 0, null);
			// Sync the display on some systems.
			// (on Linux, this fixes event queue problems)
			Toolkit.getDefaultToolkit().sync();

			g.dispose();
		}
		catch (Exception e)
		{	System.out.println("Graphics context error: " + e);
		}
	}

	@Override
	public Image getImage()
	{	
/*		
		int width = getPreferredSize().width;
		int height = getPreferredSize().height;
		Image result = createImage(width,height);
		return result;
*/
		return getFrame().getImage();
	}

	@Override
	public void loopOver()
	{	SwingUtilities.invokeLater(new Runnable()
		{	public void run()
			{	//System.out.println("the round is over.");		
				replaceWith(parent);
			}
		});				
	}

	
	
	
	
	
	@Override
	public void refresh()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{	// TODO Auto-generated method stub
	}

	@Override
	public void playerOut(int index)
	{	// TODO Auto-generated method stub		
System.out.println("the player n°"+index+" is out !");		
	}
}
