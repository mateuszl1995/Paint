package plugins;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import tpal.Mediator;
import tpal.Plugin;

public class RotatePlugin implements Plugin {
	JMenuItem menu;
	JMenuItem rotateLeft90, rotateRight90, flipVertical, flipHorizontal;
	Mediator mediator;

	public RotatePlugin() {
		menu = new JMenu("Rotate");
		rotateLeft90 = new JMenuItem("Rotate left 90");
		rotateRight90 = new JMenuItem("Rotate right 90");
		flipHorizontal = new JMenuItem("Flip horizontal");
		flipVertical = new JMenuItem("Flip vertical");
		menu.add(rotateLeft90);
		menu.add(rotateRight90);
		menu.add(flipHorizontal);
		menu.add(flipVertical);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menu;
	}

	@SuppressWarnings("serial")
	@Override
	public void initialize() {
		Plugin plugin = this;
		rotateLeft90.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, rotateLeft90);
			}
		});
		flipVertical.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, flipVertical);
			}
		});
		rotateRight90.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, rotateRight90);
			}
		});
		flipHorizontal.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, flipHorizontal);
			}
		});
	}

	@Override
	public void setMediator(Mediator m) {
		mediator = m;
	}

	@Override
	public void execute(JMenuItem item) {
		BufferedImage bi = mediator.getImage();
		int w = (item == rotateLeft90 || item == rotateRight90) ? bi.getHeight() : bi.getWidth();
		int h = (item == rotateLeft90 || item == rotateRight90) ? bi.getWidth() : bi.getHeight();
		
		BufferedImage biFlip = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++) {
				//System.out.println(x + " " + y);
				if (item == rotateLeft90) {
					biFlip.setRGB(bi.getHeight() - 1 - y, x, bi.getRGB(x, y));
				} else if (item == rotateRight90) {
					biFlip.setRGB(y, bi.getWidth()-1-x, bi.getRGB(x, y));
				} else if (item == flipHorizontal) {
					biFlip.setRGB(x, bi.getHeight() - 1 - y, bi.getRGB(x, y));
				} else {
					biFlip.setRGB(bi.getWidth()-1-x, y, bi.getRGB(x, y));
				}
			}
		mediator.setImage(biFlip);
	}

	@Override
	public void setLanguage(String language) {
		if (language.equals("pl")) {
			menu.setText("Obr�t");
			rotateLeft90.setText("Obr�t 90 st. w lewo");
			rotateRight90.setText("Obr�t 90 st. w prawo");
			flipVertical.setText("Odbicie w pionie");
			flipHorizontal.setText("Odbicie w poziomie");
		} else {
			menu.setText("Rotate");
			rotateLeft90.setText("Rotate left 90");
			rotateRight90.setText("Rotate right 90");
			flipVertical.setText("Flip vertical");
			flipHorizontal.setText("Flip horizontal");
		}
	}

}
