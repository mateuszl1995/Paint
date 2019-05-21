package tpal;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JMenuItem;

public class Paint implements Mediator {

	Toolbar toolbar;
	DrawArea drawArea;
	Status status;
	Menu menu;
	History history;
	List<Plugin> plugins;

	public static void main(String[] args) {
		Paint paint = new Paint();
		paint.show();
	}

	public Paint() {
		drawArea = new DrawArea();
		toolbar = new Toolbar(drawArea);
		drawArea.setToolbar(toolbar);
		history = drawArea.history;
		history.setPaint(this);
		menu = new Menu(drawArea);
		status = new Status();
		drawArea.setStatus(status);
		menu.fileManager.setStatus(status);

		PluginLoader loader = new PluginLoader();
		plugins = loader.loadFromDirectory("plugins");
		initializePlugins();
	}

	public void show() {
		JFrame frame = new JFrame("Paint");
		Container content = frame.getContentPane();
		content.setLayout(new BorderLayout());

		content.add(drawArea, BorderLayout.CENTER);
		content.add(toolbar.getPanel(), BorderLayout.WEST);
		content.add(status.getPanel(), BorderLayout.SOUTH);

		frame.setJMenuBar(menu.getBar());
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

		for (Plugin plugin : plugins) {
			menu.addOperation(plugin.getJMenuItem());
		}
	}

	private void initializePlugins() {
		for (Plugin plugin : plugins) {
			plugin.setMediator(this);
			plugin.initialize();
		}
	}

	@Override
	public void execute(Plugin plugin, JMenuItem item) {
		plugin.execute(item);
		history.push(plugin, item);
		drawArea.repaint();
	}

	@Override
	public BufferedImage getImage() {
		return drawArea.getImage();
	}

	@Override
	public void setImage(BufferedImage image) {
		drawArea.setImage(image);
	}

	public void doStep(Object source, Object step) {
		Plugin p = (Plugin) source;
		JMenuItem item = (JMenuItem) step;
		p.execute(item);
	}

	public void refresh() {
		Translator translator = Translator.getInstance();
		for (Plugin p : plugins) {
			p.setLanguage(translator.getLanguage());
		}
	}

}