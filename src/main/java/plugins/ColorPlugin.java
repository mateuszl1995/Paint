package tpal;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class ColorPlugin implements Plugin {
	JMenuItem menu;
	JMenuItem grayscale, sepia;
	Mediator mediator;

	public ColorPlugin() {
		menu = new JMenu("Color");
		grayscale = new JMenuItem("Grayscale");
		sepia = new JMenuItem("Sepia");
		menu.add(grayscale);
		menu.add(sepia);
	}

	@Override
	public JMenuItem getJMenuItem() {
		return menu;
	}

	@SuppressWarnings("serial")
	@Override
	public void initialize() {
		Plugin plugin = this;
		grayscale.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, grayscale);
			}
		});
		sepia.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mediator.execute(plugin, sepia);
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
		File outputfile = new File("before.png");
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (int x = 0; x < bi.getWidth(); x++)
			for (int y = 0; y < bi.getHeight(); y++) {
				
				int rgb = bi.getRGB(x, y);
				int A = (rgb & 0xff000000);
				int R = (rgb & 0x00ff0000) >> 16;
				int G = (rgb & 0x0000ff00) >> 8;
				int B = rgb & 0x000000ff;
				
				if (item == grayscale) {
					int gray = (int) (0.30f * R + 0.59f * G + 0.11f * B);
					//System.out.println(gray);
					int color = A + (gray << 16) + (gray << 8) + gray;
					bi.setRGB(x, y, color);
				} else if (item == sepia){
					int red = Math.min(255, (int) (0.393*R + 0.769*G + 0.189*B));
					int green = Math.min(255, (int) (0.349*R + 0.686*G + 0.168*B));
					int blue = Math.min(255, (int) (0.272*R + 0.534*G + 0.131*B));
					bi.setRGB(x, y, A + (red << 16) + (green << 8) + blue);
				}
			}
		outputfile = new File("after.png");
		try {
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediator.setImage(bi);
	}

	@Override
	public void setLanguage(String language) {
		if (language.equals("pl")) {
			menu.setText("Kolor");
			sepia.setText("Sepia");
			grayscale.setText("Skala szaroœci");
		} else {
			menu.setText("Color");
			sepia.setText("Sepia");
			grayscale.setText("Grayscale");
		}
	}

}
