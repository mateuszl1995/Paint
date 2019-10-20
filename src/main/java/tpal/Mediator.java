package tpal;

import java.awt.image.BufferedImage;

import javax.swing.JMenuItem;

public interface Mediator {
	public void execute(Plugin plugin, JMenuItem item);
	public BufferedImage getImage();
	public void setImage(BufferedImage image);
}
