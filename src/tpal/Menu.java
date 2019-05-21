package tpal;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.text.NumberFormatter;

public class Menu {
	DrawArea drawArea;
	FileManager fileManager;
	History history;
	JMenuBar menuBar;
	JMenu language;
	JMenu file;
	JMenu image;
	JMenu edit;

	public Menu(DrawArea d) {
		fileManager = new FileManager(d);
		drawArea = d;
		history = drawArea.history;
		menuBar = new JMenuBar();
		addFile();
		addEdit();
		addImage();
		addLanguage();
		//menuBar.add(Box.createRigidArea(new Dimension(100, 25)));
	}

	private void addEdit() {
		edit = new JMenu("Edit");
		edit.setName("Edit");
		menuBar.add(edit);
		JMenuItem undo = new JMenuItem("Undo");
		undo.setName("Undo");
		JMenuItem redo = new JMenuItem("Redo");
		redo.setName("Redo");
		JMenuItem size = new JMenuItem("Size...");
		size.setName("Size...");

		undo.addActionListener(menuListener);
		redo.addActionListener(menuListener);
		size.addActionListener(menuListener);

		edit.add(undo);
		edit.add(redo);
		edit.add(size);
	}

	public JMenuBar getBar() {
		return menuBar;
	}

	void addLanguage() {
		language = new JMenu("Language");
		language.setName("Language");
		menuBar.add(language);
		JMenuItem polish = new JMenuItem("Polish");
		polish.setName("Polish");
		JMenuItem english = new JMenuItem("English");
		english.setName("English");

		polish.addActionListener(menuListener);
		english.addActionListener(menuListener);

		language.add(polish);
		language.add(english);
	}

	void addFile() {
		file = new JMenu("File");
		file.setName("File");
		menuBar.add(file);
		JMenuItem newFile = new JMenuItem("New");
		newFile.setName("New");
		JMenuItem open = new JMenuItem("Open");
		open.setName("Open");
		JMenuItem save = new JMenuItem("Save");
		save.setName("Save");
		JMenuItem saveAs = new JMenuItem("Save as...");
		saveAs.setName("Save as...");

		newFile.addActionListener(menuListener);
		open.addActionListener(menuListener);
		save.addActionListener(menuListener);
		saveAs.addActionListener(menuListener);

		file.add(newFile);
		file.add(open);
		file.add(save);
		file.add(saveAs);
	}

	void addImage() {
		image = new JMenu("Image");
		image.setName("Image");
		menuBar.add(image);
	}

	public void addOperation(JMenuItem imageOperation) {
		image.add(imageOperation);
	}

	public void refresh() {
		Translator translator = Translator.getInstance();
		for (int i = 0; i < menuBar.getMenuCount(); i++) {
			JMenu menu = menuBar.getMenu(i);
			menu.setText(translator.translate(menu.getName()));
			if (menu != image)
			for (int j = 0; j < menu.getMenuComponentCount(); j++) {
				java.awt.Component comp = menu.getMenuComponent(j);
				if (comp instanceof JMenuItem) {
					JMenuItem item = (JMenuItem) comp;
					item.setText(translator.translate(item.getName()));
				}
			}
		}
		Paint paint = drawArea.history.paint;
		paint.refresh();
	}

	@SuppressWarnings("serial")
	private ActionListener menuListener = new AbstractAction() {
		public void actionPerformed(ActionEvent e) {
			String name = ((JMenuItem) e.getSource()).getName();
			if (name.equals("New"))
				drawArea.clear();
			else if (name.equals("Open"))
				fileManager.open();
			else if (name.equals("Save"))
				fileManager.save();
			else if (name.equals("Save as..."))
				fileManager.saveAs();
			else if (name.equals("Undo"))
				history.undo();
			else if (name.equals("Redo"))
				history.redo();
			else if (name.equals("Size...")) {
				MyDialogPopup dialog = new MyDialogPopup();
				dialog.setModal(true);
				dialog.setVisible(true);
				if (dialog.width != 0)
					drawArea.editSize(dialog.width, dialog.height);
			} else if (name.equals("Polish")) {
				Translator translator = Translator.getInstance();
				translator.setLanguage("pl");
				refresh();
				drawArea.toolbar.refresh();
			} else if (name.equals("English")) {
				Translator translator = Translator.getInstance();
				translator.setLanguage("en");
				refresh();
				drawArea.toolbar.refresh();
			}

		}
	};

	@SuppressWarnings("serial")
	class MyDialogPopup extends JDialog {
		int width;
		int height;

		public MyDialogPopup() {
			Translator translator = Translator.getInstance();
			setBounds(100, 100, 250, 100);
			setTitle(translator.translate("Set size of picture"));
			setLocationRelativeTo(null);
			getContentPane().setLayout(new GridLayout(3, 2));
			
			getContentPane().add(new JLabel(translator.translate("Width")));
			getContentPane().add(new JLabel(translator.translate("Height")));

			NumberFormat format = NumberFormat.getInstance();
			NumberFormatter formatter = new NumberFormatter(format);
			formatter.setValueClass(Integer.class);
			formatter.setMinimum(0);
			formatter.setMaximum(Integer.MAX_VALUE);
			formatter.setAllowsInvalid(false);
			JFormattedTextField widthText = new JFormattedTextField(formatter);
			getContentPane().add(widthText);
			JFormattedTextField heightText = new JFormattedTextField(formatter);
			getContentPane().add(heightText);

			JButton btnOK = new JButton("OK");
			btnOK.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					width = Integer.parseInt(widthText.getText());
					height = Integer.parseInt(heightText.getText());
					dispose();
				}
			});
			getContentPane().add(btnOK);

			JButton btnCancel = new JButton(translator.translate("Cancel"));
			btnCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					width = height = 0;
					dispose();
				}
			});
			getContentPane().add(btnCancel);

		}
	}

}
