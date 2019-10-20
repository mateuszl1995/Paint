package tpal;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Status {
	final static int WIDTH = 100;
	final static int HEIGHT = 20;

	JPanel panel;
	JLabel position;
	JLabel shapeSize;
	JLabel info;

	public Status() {
		panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		position = new JLabel("asd", JLabel.LEFT);
		position.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.add(position);
		shapeSize = new JLabel("dda", JLabel.LEFT);
		shapeSize.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.add(shapeSize);
		info = new JLabel("", JLabel.LEFT);
		info.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		panel.add(info);
	}
	
	public JPanel getPanel() { return panel; }
	void setStatus(int x, int y, int w, int h) {
		position.setText(x +" x " + y);
		shapeSize.setText(w + " x " + h);
	}
	void setInfo(String info) {
		this.info.setText(info);
	}
}
