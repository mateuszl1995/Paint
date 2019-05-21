package tpal;

import javax.swing.JMenuItem;

public interface Plugin {
	public void execute(JMenuItem item);
	public JMenuItem getJMenuItem();
	public void setLanguage(String language);
	public void initialize();
	public void setMediator(Mediator m);
}