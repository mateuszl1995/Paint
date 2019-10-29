package tpal;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.*;

public class PluginLoader {
	public List<Plugin> loadPluginsFromDirectory(String directory) {
		List<Plugin> plugins = new ArrayList<Plugin>();
		File dir = new File(directory);
		if (!dir.exists()) {
			System.out.println("Directory plugins does not exist: " + directory + ".");
			return plugins;
		}
		for (File file : dir.listFiles()) {
			if (file.getName().endsWith("jar")) {
				try {
					List<Plugin> pluginsInFile = loadPluginsFromFile(file);
					plugins.addAll(pluginsInFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return plugins;
	}

	private List<Plugin> loadPluginsFromFile(File file) throws Exception {
		List<Plugin> plugins = new ArrayList<Plugin>();
		JarFile jarFile = new JarFile(file);
		Enumeration<JarEntry> entries = jarFile.entries();
		
		URL[] urls = { new URL("jar:file:" + file.getAbsolutePath()+"!/") };
	    URLClassLoader cl = URLClassLoader.newInstance(urls);
		
		while (entries.hasMoreElements()) {
			JarEntry je = entries.nextElement();
			if (je.getName().endsWith(".class")) {
				String className = je.getName().substring(0,je.getName().length()-6);
				className = className.replace('/', '.');
				Class<?> clazz = cl.loadClass(className);
				if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
					System.out.println("Plugin " + clazz.getName() + " added.");
					Plugin p = (Plugin) clazz.newInstance();
					plugins.add(p);
				}
			}
		}
		jarFile.close();
		return plugins;
	}
}
