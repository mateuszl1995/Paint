package tpal;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileManager {
	Status status;
	FileManager(DrawArea drawArea) {
		this.drawArea = drawArea;
	}
	public void setStatus (Status s ) { this.status = s;}
	String filename = "";
	DrawArea drawArea;
	public void open() {
		Image image = null;
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			filename = chooser.getSelectedFile().getAbsolutePath();
			File file = chooser.getSelectedFile();
			try {
				image = ImageIO.read(file);
				drawArea.setImage(image);
				drawArea.opened = drawArea.copyImage(image);
			} catch (IOException e) {}
		}
		
	}

	void askSave() {
		int dialogButton = JOptionPane.YES_NO_OPTION;
		int dialogResult = JOptionPane.showConfirmDialog(null, "Would you like to save file?", "Warning", dialogButton);
		if (dialogResult == JOptionPane.YES_OPTION) {
			save();
		}
	}
	public void saveAs() {
		filename = "";
		save();
	}
	public void save() {
		if (filename.equals("")) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("png", "bmp", "jpg", "gif");
			chooser.setFileFilter(filter);
	
			int returnVal = chooser.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				filename = chooser.getSelectedFile().getAbsolutePath();
			}
		}
		if (filename.equals(""))
			return;
		try {
			BufferedImage bi = (BufferedImage) drawArea.getImage();
			if (!filename.endsWith(".png"))
				filename = filename + ".png";
			File outputfile = new File(filename);
			ImageIO.write(bi, "png", outputfile);
			status.setInfo("Zapisano plik");
			drawArea.history.clear();
			drawArea.opened = drawArea.image;
		} catch (IOException e) {}
		
	}
}
