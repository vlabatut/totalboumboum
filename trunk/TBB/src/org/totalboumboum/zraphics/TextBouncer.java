package org.totalboumboum.zraphics;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("javadoc")
public class TextBouncer extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

private boolean trucking = true;

  private long[] previousTimes; // milliseconds

  private int previousIndex;

  private boolean previousFilled;

  private double frameRate; // frames per second

  private Image image = null;

  public static void main(String[] args) {
    String s = "Java Tips";
    final int size = 64;
    if (args.length > 0)
      s = args[0];

    Panel controls = new Panel();
    final Choice choice = new Choice();
    GraphicsEnvironment ge = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    Font[] allFonts = ge.getAllFonts();
    for (int i = 0; i < allFonts.length; i++)
      choice.addItem(allFonts[i].getName());
    Font defaultFont = new Font(allFonts[0].getName(), Font.PLAIN, size);

    final TextBouncer bouncer = new TextBouncer(s, defaultFont);
    Frame f = new AnimationFrame(bouncer);
    f.setFont(new Font("Serif", Font.PLAIN, 12));
    controls.add(bouncer.createCheckbox("Antialiasing",
        TextBouncer.ANTIALIASING));
    controls.add(bouncer.createCheckbox("Gradient", TextBouncer.GRADIENT));
    controls.add(bouncer.createCheckbox("Shear", TextBouncer.SHEAR));
    controls.add(bouncer.createCheckbox("Rotate", TextBouncer.ROTATE));
    controls.add(bouncer.createCheckbox("Axes", TextBouncer.AXES));

    Panel fontControls = new Panel();
    choice.addItemListener(new ItemListener() {
    	@Override
      	public void itemStateChanged(ItemEvent ie) {
        Font font = new Font(choice.getSelectedItem(), Font.PLAIN, size);
        bouncer.setFont(font);
      }
    });
    fontControls.add(choice);

    Panel allControls = new Panel(new GridLayout(2, 1));
    allControls.add(controls);
    allControls.add(fontControls);
    f.add(allControls, BorderLayout.NORTH);
        f.setSize(300,300);
    f.setVisible(true);
  }

  private boolean mAntialiasing = false, mGradient = false;

  private boolean mShear = false, mRotate = false, mAxes = false;

  public static final int ANTIALIASING = 0;

  public static final int GRADIENT = 1;

  public static final int SHEAR = 2;

  public static final int ROTATE = 3;

  public static final int AXES = 5;

  private float mDeltaX, mDeltaY;

  private float mX, mY, mWidth, mHeight;

  private float mTheta;

  private float mShearX, mShearY, mShearDeltaX, mShearDeltaY;

  private String mString;

  public TextBouncer(String s, Font f) {
    previousTimes = new long[128];
    previousTimes[0] = System.currentTimeMillis();
    previousIndex = 1;
    previousFilled = false;
    mString = s;
    setFont(f);
    Random random = new Random();
    mX = random.nextFloat() * 500;
    mY = random.nextFloat() * 500;
    mDeltaX = random.nextFloat() * 3;
    mDeltaY = random.nextFloat() * 3;
    mShearX = random.nextFloat() / 2;
    mShearY = random.nextFloat() / 2;
    mShearDeltaX = mShearDeltaY = .05f;
    FontRenderContext frc = new FontRenderContext(null, true, false);
    Rectangle2D bounds = getFont().getStringBounds(mString, frc);
    mWidth = (float) bounds.getWidth();
    mHeight = (float) bounds.getHeight();
    // Make sure points are within range.
    addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent ce) {
        Dimension d = getSize();
        if (mX < 0)
          mX = 0;
        else if (mX + mWidth >= d.width)
          mX = d.width - mWidth - 1;
        if (mY < 0)
          mY = 0;
        else if (mY + mHeight >= d.height)
          mY = d.height - mHeight - 1;
      }
    });
  }

  public void setSwitch(int item, boolean value) {
    switch (item) {    case ANTIALIASING:
      mAntialiasing = value;
      break;
    case GRADIENT:
      mGradient = value;
      break;
    case SHEAR:
      mShear = value;
      break;
    case ROTATE:
      mRotate = value;
      break;
    case AXES:
      mAxes = value;
      break;
    default:
      break;
    }
  }

  protected Checkbox createCheckbox(String label, final int item) {
    Checkbox check = new Checkbox(label);
    check.addItemListener(new ItemListener() {
    @Override
	@SuppressWarnings("static-access")
	public void itemStateChanged(ItemEvent ie) {
        setSwitch(item, (ie.getStateChange() == ie.SELECTED));
      }
    });
    return check;
  }

  public void timeStep() {
    Dimension d = getSize();
    if (mX + mDeltaX < 0)
      mDeltaX = -mDeltaX;
    else if (mX + mWidth + mDeltaX >= d.width)
      mDeltaX = -mDeltaX;
    if (mY + mDeltaY < 0)
      mDeltaY = -mDeltaY;
    else if (mY + mHeight + mDeltaY >= d.height)
      mDeltaY = -mDeltaY;
    mX += mDeltaX;
    mY += mDeltaY;

    mTheta += Math.PI / 192;
    if (mTheta > (2 * Math.PI))
      mTheta -= (2 * Math.PI);

    if (mShearX + mShearDeltaX > .5)
      mShearDeltaX = -mShearDeltaX;
    else if (mShearX + mShearDeltaX < -.5)
      mShearDeltaX = -mShearDeltaX;
    if (mShearY + mShearDeltaY > .5)
      mShearDeltaY = -mShearDeltaY;
    else if (mShearY + mShearDeltaY < -.5)
      mShearDeltaY = -mShearDeltaY;
    mShearX += mShearDeltaX;
    mShearY += mShearDeltaY;
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    setAntialiasing(g2);
    setTransform(g2);
    setPaint(g2);
    // Draw the string.
    g2.setFont(getFont());
    g2.drawString(mString, mX, mY + mHeight);
    drawAxes(g2);
  }

  protected void setAntialiasing(Graphics2D g2) {
    if (mAntialiasing == false)
      return;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
  }

  protected void setTransform(Graphics2D g2) {
    Dimension d = getSize();
    int cx = d.width / 2;
    int cy = d.height / 2;
    g2.translate(cx, cy);
    if (mShear)
      g2.shear(mShearX, mShearY);
    if (mRotate)
      g2.rotate(mTheta);
    g2.translate(-cx, -cy);
  }

  protected void setPaint(Graphics2D g2) {
    if (mGradient) {
      GradientPaint gp = new GradientPaint(0, 0, Color.blue, 50, 25,
          Color.green, true);
      g2.setPaint(gp);
    } else
      g2.setPaint(Color.orange);
  }

  protected void drawAxes(Graphics2D g2) {
    if (mAxes == false)
      return;
    g2.setPaint(getForeground());
    g2.setStroke(new BasicStroke());
    Dimension d = getSize();
    int side = 20;
    int arrow = 4;
    int w = d.width / 2, h = d.height / 2;
    g2.drawLine(w - side, h, w + side, h);
    g2.drawLine(w + side - arrow, h - arrow, w + side, h);
    g2.drawLine(w, h - side, w, h + side);
    g2.drawLine(w + arrow, h + side - arrow, w, h + side);
  }

  @Override
  public void run() {
    while (trucking) {
      render();
      timeStep();
      calculateFrameRate();
    }
  }

  protected void render() {
    Graphics g = getGraphics();
    if (g != null) {
      Dimension d = getSize();
      if (checkImage(d)) {
        Graphics imageGraphics = image.getGraphics();
        // Clear the image background.
        imageGraphics.setColor(getBackground());
        imageGraphics.fillRect(0, 0, d.width, d.height);
        imageGraphics.setColor(getForeground());
        // Draw this component offscreen.
        paint(imageGraphics);
        // Now put the offscreen image on the screen.
        g.drawImage(image, 0, 0, null);
        // Clean up.
        imageGraphics.dispose();
      }
      g.dispose();
    }
  }

  // Offscreen image.
  protected boolean checkImage(Dimension d) {
    if (d.width == 0 || d.height == 0)
      return false;
    if (image == null || image.getWidth(null) != d.width
        || image.getHeight(null) != d.height) {
      image = createImage(d.width, d.height);
    }
    return true;
  }

  protected void calculateFrameRate() {
    // Measure the frame rate
    long now = System.currentTimeMillis();
    int numberOfFrames = previousTimes.length;
    double newRate;
    // Use the more stable method if a history is available.
    if (previousFilled)
      newRate = (double) numberOfFrames
          / (double) (now - previousTimes[previousIndex]) * 1000.0;
    else
      newRate = 1000.0 / (double) (now - previousTimes[numberOfFrames - 1]);
    firePropertyChange("frameRate", frameRate, newRate);
    frameRate = newRate;
    // Update the history.
    previousTimes[previousIndex] = now;
    previousIndex++;
    if (previousIndex >= numberOfFrames) {
      previousIndex = 0;
      previousFilled = true;
    }
  }

  public double getFrameRate() {
    return frameRate;
  }

  // Property change support.
  private transient AnimationFrame mRateListener;

  public void setRateListener(AnimationFrame af) {
    mRateListener = af;
  }

  @Override
  public void firePropertyChange(String name, double oldValue, double newValue) {
    mRateListener.rateChanged(newValue);
  }

  private static Component sComponent = new Component() {

	private static final long serialVersionUID = 1L;
  };

  private static final MediaTracker sTracker = new MediaTracker(sComponent);

  private static int sID = 0;

  public static boolean waitForImage(Image image) {
    int id;
    synchronized (sComponent) {
      id = sID++;
    }
    sTracker.addImage(image, id);
    try {
      sTracker.waitForID(id);
    } catch (InterruptedException ie) {
      return false;
    }
    if (sTracker.isErrorID(id))
      return false;
    return true;
  }

  public Image blockingLoad(String path) {
    Image image = Toolkit.getDefaultToolkit().getImage(path);
    if (waitForImage(image) == false)
      return null;
    return image;
  }

  public static Image blockingLoad(URL url) {
    Image image = Toolkit.getDefaultToolkit().getImage(url);
    if (waitForImage(image) == false)
      return null;
    return image;
  }

  public BufferedImage makeBufferedImage(Image image) {
    return makeBufferedImage(image, BufferedImage.TYPE_INT_RGB);
  }

  public BufferedImage makeBufferedImage(Image image, int imageType) {
    if (waitForImage(image) == false)
      return null;

    BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
        image.getHeight(null), imageType);
    Graphics2D g2 = bufferedImage.createGraphics();
    g2.drawImage(image, null, null);
    return bufferedImage;
  }
}

@SuppressWarnings("javadoc")
class AnimationFrame extends JFrame {
  private static final long serialVersionUID = 1L;

private Label mStatusLabel;

  private NumberFormat mFormat;

  public AnimationFrame(TextBouncer ac) {
    super();
    setLayout(new BorderLayout());
    add(ac, BorderLayout.CENTER);
    add(mStatusLabel = new Label(), BorderLayout.SOUTH);
    // Create a number formatter.
    mFormat = NumberFormat.getInstance();
    mFormat.setMaximumFractionDigits(1);
    // Listen for the frame rate changes.
    ac.setRateListener(this);
    // Kick off the animation.
    Thread t = new Thread(ac);
    t.setName("TextBouncer");
    t.start();
  }

  public void rateChanged(double frameRate) {
    mStatusLabel.setText(mFormat.format(frameRate) + " fps");
  }

}