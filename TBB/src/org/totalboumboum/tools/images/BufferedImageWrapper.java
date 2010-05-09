package org.totalboumboum.tools.images;

import java.io.File;
import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.awt.image.ColorModel;
//import javax.swing.JFrame;
import java.awt.image.WritableRaster;
import java.awt.image.ImageProducer;
import java.awt.image.ImageObserver;
import java.util.Vector;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.Raster;
import java.awt.image.TileObserver;

public class BufferedImageWrapper implements Serializable
{	private static final long serialVersionUID = 1L;

	private BufferedImage im = null;
	private int drawPointX, drawPointY;

	public BufferedImageWrapper(BufferedImage im) {
		this.im = im;
	}

	public BufferedImageWrapper(String filepath) {
		try {
			this.im = ImageIO.read(new File(filepath));
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public BufferedImageWrapper(Image img) {
		this.im = new BufferedImage(img.getWidth(null), img.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics g = this.im.getGraphics();
		g.drawImage(img, 0, 0, null);
		g.dispose();
	}

	public BufferedImage getIm() {
		return im;
	}

	public int getHeight() {
		return this.im.getHeight();
	}

	public int getWidth() {
		return this.im.getWidth();
	}

	public int getMinX() {
		return this.im.getMinX();
	}

	public Object getProperty(String name, ImageObserver obs) {
		return this.im.getProperty(name, obs);
	}

	public Object getProperty(String name) {
		return this.im.getProperty(name);
	}

	public void coerceData(boolean isAlphaPremultipled) {
		this.im.coerceData(isAlphaPremultipled);
	}

	public int getMinY() {
		return this.im.getMinY();
	}

	public int getRGB(int x, int y) {
		return this.im.getRGB(x, y);
	}

	public int[] getRGB(int startX, int startY, int w, int h, int[] rgbArray,
			int offset, int scansize) {
		return this.im.getRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	public void setRGB(int x, int y, int rgb) {
		this.im.setRGB(x, y, rgb);
	}

	public void setRGB(int startX, int startY, int w, int h, int[] rgbArray,
			int offset, int scansize) {
		this.im.setRGB(startX, startY, w, h, rgbArray, offset, scansize);
	}

	public String toString() {
		return this.im.toString();
	}

	public SampleModel getSampleModel() {
		return this.im.getSampleModel();
	}

	public Graphics getGraphics() {
		return this.im.getGraphics();
	}

	public Graphics2D createGraphics() {
		return this.im.createGraphics();
	}

	public BufferedImage getSubImage(int x, int y, int w, int h) {
		return this.im.getSubimage(x, y, w, h);
	}

	public ColorModel getColorModel() {
		return this.im.getColorModel();
	}

	public WritableRaster getRaster() {
		return this.im.getRaster();
	}

	public WritableRaster getAlphaRaster() {
		return this.im.getAlphaRaster();
	}

	public int getType() {
		return this.im.getType();
	}

	public ImageProducer getSource() {
		return this.im.getSource();
	}
/*
	public void displayImage(int pointX, int pointY) {
		this.drawPointX = pointX;
		this.drawPointY = pointY;
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.im.getWidth(), this.im.getHeight());
	}

	public void displayImage() {
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.im.getWidth(), this.im.getHeight());
	}
*/
	public Vector<RenderedImage> getSources() {
		return this.im.getSources();
	}

	public String[] getPropertyNames() {
		return this.im.getPropertyNames();
	}

	public boolean isAlphaPremultipled() {
		return this.im.isAlphaPremultiplied();
	}

	public int getNumXTiles() {
		return this.im.getNumXTiles();
	}

	public int getNumYTiles() {
		return this.im.getNumYTiles();
	}

	public int getMinTileX() {
		return this.im.getMinTileX();
	}

	public int getMinTileY() {
		return this.im.getMinTileY();
	}

	public int getTileHeight() {
		return this.im.getTileHeight();
	}

	public int getTileWidth() {
		return this.im.getTileWidth();
	}

	public int getTileGridXOffset() {
		return this.im.getTileGridXOffset();
	}

	public int getTileGridYOffset() {
		return this.im.getTileGridYOffset();
	}

	public Raster getTile(int tileX, int tileY) {
		return this.im.getTile(tileX, tileY);
	}

	public Raster getData() {
		return this.im.getData();
	}

	public WritableRaster copyData(WritableRaster out) {
		return this.im.copyData(out);
	}

	public void setData(Raster r) {
		this.im.setData(r);
	}

	public void addTileObserver(TileObserver to) {
		this.im.addTileObserver(to);
	}

	public void removeTileObserver(TileObserver to) {
		this.im.removeTileObserver(to);
	}

	public boolean isTileWritable(int x, int y) {
		return this.im.isTileWritable(x, y);
	}

	public java.awt.Point[] getWritableTileIndices() {
		return this.im.getWritableTileIndices();
	}

	public boolean hasTileWriters() {
		return this.im.hasTileWriters();
	}

	public WritableRaster getWritableTile(int x, int y) {
		return this.im.getWritableTile(x, y);
	}

	public void releaseWritableTile(int x, int y) {
		this.im.releaseWritableTile(x, y);
	}

	public int getTransparency() {
		return this.im.getTransparency();
	}

	public java.awt.ImageCapabilities getCapabilities(
			java.awt.GraphicsConfiguration gs) {
		return this.im.getCapabilities(gs);
	}

	private BufferedImage fromByteArray(byte[] imagebytes) {
		try {
			if (imagebytes != null && (imagebytes.length > 0)) {
				BufferedImage im = ImageIO.read(new ByteArrayInputStream(
						imagebytes));
				return im;
			}
			return null;
		} catch (IOException e) {
			throw new IllegalArgumentException(e.toString());
		}
	}

	private byte[] toByteArray(BufferedImage o) {
		if (o != null) {
			BufferedImage image = (BufferedImage) o;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(image, "jpg", baos);
			} catch (IOException e) {
				throw new IllegalStateException(e.toString());
			}
			byte[] b = baos.toByteArray();
			return b;
		}
		return new byte[0];
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		byte[] b = toByteArray(im);
		out.writeInt(b.length);
		out.write(b);
		out.flush();
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		int length = in.readInt();
		byte[] b = new byte[length];
		in.readFully(b);
		im = fromByteArray(b);
	}

	public void paint(Graphics g) {
		g.drawImage(this.im, drawPointX, drawPointY, null);
	}
}