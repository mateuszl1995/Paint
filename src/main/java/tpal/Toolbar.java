package tpal;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Toolbar {
	JPanel panel;
	JButton colorBtn, colorBtn2;
	JLabel brush, line, rectangle, oval, actual;
	JComboBox<String> brushSize;
	DrawArea drawArea;
	private MouseListener mouseListener;
	private JCheckBox fill, dashed;

	public JPanel getPanel() {
		return panel;
	}

	public Toolbar(DrawArea d) {
		mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				setTool((JLabel) e.getSource());
			}
		};
		
		drawArea = d;
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(130, 100));
		panel.setBorder(new EmptyBorder(10, 5, 10, 5));
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		JPanel colorsPanel = new JPanel();
		colorsPanel.setLayout(new BoxLayout(colorsPanel, BoxLayout.X_AXIS));
		int iconSize = 23;
		colorBtn = new JButton("");
		colorBtn.addActionListener(toolbarActionListener);
		setSize(colorBtn, iconSize, iconSize);
		colorBtn2 = new JButton("");
		colorBtn2.addActionListener(toolbarActionListener);
		setSize(colorBtn2, iconSize, iconSize);
		colorsPanel.add(colorBtn);
		colorsPanel.add(colorBtn2);
		panel.add(colorsPanel);

		brush = new JLabel(new ImageIcon("img/paint-brush.png"), JLabel.CENTER);
		line = new JLabel(new ImageIcon("img/line.png"), JLabel.CENTER);
		rectangle = new JLabel(new ImageIcon("img/rectangle2.png"), JLabel.CENTER);
		oval = new JLabel(new ImageIcon("img/oval2.png"), JLabel.CENTER);

		addLabel("Colors");
		addLabel("1");
		panel.add(colorBtn);
		addLabel("2");
		panel.add(colorBtn2);
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		addBrushSize();
		
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		addLabel("Tools");
		addLabel(brush, "Brush");
		addLabel(line, "Line");
		addLabel(oval, "Oval");
		addLabel(rectangle, "Rectangle");
		

		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		addLabel("Options");
		fill = new JCheckBox("Fill");
		dashed = new JCheckBox("Dashed");
		fill.setName("Fill");
		dashed.setName("Dashed");
		fill.addActionListener(toolbarActionListener);
		dashed.addActionListener(toolbarActionListener);
		panel.add(fill);
		panel.add(dashed);
		clear();
	}
	
	public void clear() {
		colorBtn.setBackground(Color.BLACK);
		colorBtn2.setBackground(Color.WHITE);
		setTool(brush);
		brushSize.setSelectedIndex(0);
		fill.setSelected(false);
		dashed.setSelected(false);
	}
	
	
	private ActionListener toolbarActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object source = e.getSource();
			if (source == colorBtn) {
				Color color = JColorChooser.showDialog(colorBtn, "Choose foreground color", colorBtn.getBackground());
				colorBtn.setBackground(color);
			} else if (source == colorBtn2) {
				Color color = JColorChooser.showDialog(colorBtn2, "Choose background color", colorBtn2.getBackground());
				colorBtn2.setBackground(color);
			}
		}
		
	};

	private void addBrushSize() {
		String[] sizes = new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "12", "14", "16", "18", "20", "25", "30" ,"40" };
		brushSize = new JComboBox<String>(sizes);
		brushSize.setSelectedIndex(0);
		brushSize.addActionListener(toolbarActionListener);
		addLabel("Size");
		setSize(brushSize, 40, 20);
		brushSize.setAlignmentX(0);
		panel.add(brushSize);
	}

	void setTool(JLabel tool) {
		if (actual != null)
			actual.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		tool.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
		actual = tool;
	}

	String getTool() {
		return actual.getName();
	}

	void addLabel(JLabel img, String name) {
		img.addMouseListener(mouseListener);
		img.setName(name);
		img.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		panel.add(Box.createRigidArea(new Dimension(0, 5)));
		panel.add(img);

	}

	void addLabel(String name) {
		JLabel l = new JLabel(name);
		l.setName(name);
		panel.add(l);
	}

	void setSize(Component c, int width, int height) {
		c.setMinimumSize(new Dimension(width, height));
		c.setPreferredSize(new Dimension(width, height));
		c.setMaximumSize(new Dimension(width, height));
	}

	int getBrushSize() {
		return Integer.parseInt((String) brushSize.getSelectedItem());
	}
	public boolean getFill() {
		return fill.isSelected();
	}
	public boolean getDotted() {
		return dashed.isSelected();
	}
	public void refresh() {
		Translator translator = Translator.getInstance();
		for (Component jc : getPanel().getComponents()) {
		    if ( jc instanceof JLabel ) {
		    	JLabel j = (JLabel) jc;
		    	if (j.getIcon() == null)
		    		j.setText(translator.translate(j.getName()));
		    } else if ( jc instanceof JCheckBox ) {
		    	JCheckBox j = (JCheckBox) jc;
		    	j.setText(translator.translate(j.getName()));
		    }
		}
	}
}
