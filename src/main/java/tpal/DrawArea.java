package tpal;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public class DrawArea extends JComponent {
	Image opened, cloned;
	BufferedImage image;
	Graphics2D g2;
	private int currentX, currentY, oldX, oldY;
	Toolbar toolbar;
	History history;
	DrawArea me;

	public DrawArea() {
		me = this;
		history = new History(this);
		setDoubleBuffered(false);
		setSize(600, 480);
		clear();
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				String tool = toolbar.getTool();
				if (tool.equals("Line") || tool.equals("Rectangle") || tool.equals("Oval")) {
					cloned = copyImage(image);
				}
				oldX = e.getX();
				oldY = e.getY();
				status.setInfo("");
			}

			public void mouseReleased(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				if (g2 == null)
					return;
				String tool = toolbar.getTool();
				if (tool.equals("Line") || tool.equals("Rectangle") || tool.equals("Oval")) {
					g2.drawImage(cloned, 0, 0, cloned.getWidth(null), cloned.getHeight(null), 0, 0,
							cloned.getWidth(null), cloned.getHeight(null), null);
					draw(tool, oldX, oldY, currentX, currentY);
					repaint();
				} else {
					Step s = new Step(); s.tool = "Release";
					history.push(me, s);
				}
			}
		});

		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				currentX = e.getX();
				currentY = e.getY();
				String tool = toolbar.getTool();
				if (g2 == null)
					return;
				if (toolbar.getTool().equals("Brush")) {
					draw("Brush", oldX, oldY, currentX, currentY);
					repaint(); // refresh draw area to repaint
					oldX = currentX;
					oldY = currentY;
				} else if (tool.equals("Line") || tool.equals("Rectangle") || tool.equals("Oval")) {
					g2.drawImage(cloned, 0, 0, cloned.getWidth(null), cloned.getHeight(null), 0, 0,
							cloned.getWidth(null), cloned.getHeight(null), null);
					draw(tool, oldX, oldY, currentX, currentY, false);
					repaint();
				}
				int x = e.getX();
				int y = e.getY();
				int w = (x - oldX > 0) ? x - oldX : oldX - x;
				int h = (y - oldY > 0) ? y - oldY : oldY - y;
				status.setStatus(x, y, w, h);

			}

			public void mouseMoved(MouseEvent e) {
				int x = e.getX();
				int y = e.getY();
				int w = (x - oldX > 0) ? x - oldX : oldX - x;
				int h = (y - oldY > 0) ? y - oldY : oldY - y;
				status.setStatus(x, y, w, h);
			}
		});
	}

	void setToolbar(Toolbar t) {
		toolbar = t;
		toolbar.clear();
	}

	protected void paintComponent(Graphics g) {
		g.drawImage(image, 0, 0, null);
	}

	public void clear() {
		clearGraphics();
		opened = copyImage(image);
		if (toolbar != null)
			toolbar.clear();
	}
	public void clearGraphics() {
		image = new BufferedImage(getSize().width, getSize().height, BufferedImage.TYPE_INT_ARGB);
		
		setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		g2 = (Graphics2D) image.createGraphics();
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, getSize().width, getSize().height);
		g2.setPaint(Color.black);
		g2.setBackground(Color.white);
		repaint();
	}

	public BufferedImage copyImage(Image source) {
		BufferedImage b = new BufferedImage(source.getWidth(null), source.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics g = b.getGraphics();
		g.drawImage(source, 0, 0, null);
		g.dispose();
		return b;
	}
	
	public void draw(String tool, int oldX, int oldY, int currentX, int currentY) {
		draw(tool, oldX, oldY, currentX, currentY, true);
	}
	
	public void doStep(Object s) { doStep((Step) s); }
	private void doStep(Step step) {
		if (step.tool.equals("Release")) return;
		else if (step.tool.equals("Edit size")) {
			BufferedImage newImage = new BufferedImage(step.currentX, step.currentY, BufferedImage.TYPE_INT_ARGB);
			g2 = (Graphics2D) newImage.createGraphics();
			image = image.getSubimage(0, 0, Math.min(step.currentX, image.getWidth()), Math.min(step.currentY, image.getHeight()));
			g2.drawImage(image, 0, 0, Math.min(step.currentX, image.getWidth()), Math.min(step.currentY, image.getHeight()), null);
			image = newImage;
		}
		Stroke s = (!step.dotted) ? new BasicStroke(step.brushSize)
				: new BasicStroke(step.brushSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
		g2.setStroke(s);
		g2.setColor(step.fg);
		
		if (step.tool.equals("Brush") || step.tool.equals("Line")) {
			g2.drawLine(step.oldX, step.oldY, step.currentX, step.currentY);
		} else {
			int x = Math.min(step.oldX, step.currentX);
			int y = Math.min(step.oldY, step.currentY);
			int w = Math.abs(step.oldX - step.currentX);
			int h = Math.abs(step.oldY - step.currentY);

			if (step.tool.equals("Rectangle")) {
				if (step.fill) {
					g2.setColor(step.bg);
					g2.fillRect(x, y, w, h);
					g2.setColor(step.fg);
					g2.drawRect(x, y, w, h);
				} else
					g2.drawRect(x, y, w, h);
			} else if (step.tool.equals("Oval")) {
				if (step.fill) {
					g2.setColor(step.bg);
					g2.fillOval(x, y, w, h);
					g2.setColor(step.fg);
					g2.drawOval(x, y, w, h);
				} else
					g2.drawOval(x, y, w, h);
			}

		}
	}
	public void draw(String tool, int oldX, int oldY, int currentX, int currentY, boolean save) {
		Step step = new Step();
		step.fg = new Color(toolbar.colorBtn.getBackground().getRGB());
		step.bg = new Color(toolbar.colorBtn2.getBackground().getRGB());
		step.fill = toolbar.getFill();
		step.dotted = toolbar.getDotted();
		step.brushSize = toolbar.getBrushSize();
		step.tool = toolbar.getTool();
		step.oldX = oldX;
		step.currentX = currentX;
		step.oldY = oldY;
		step.currentY = currentY;
		
		if (save)
			history.push(this, step);
		
		doStep(step);

	}
	
	class Step {
		Color fg, bg;
		int currentX, currentY, oldX, oldY;
		String tool;
		boolean fill, dotted;
		int brushSize;
	}
	Status status;

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setImage(Image im) {
		image = copyImage(im);
		g2 = (Graphics2D) image.getGraphics();
		repaint();

	}

	public BufferedImage getImage() {
		return image;
	}

	public void editSize(int width, int height) {
		Step s = new Step(); 
		s.tool = "Edit size"; s.currentX = width; s.currentY = height;
		history.push(this, s);
		doStep(s);
		repaint();
	}

}