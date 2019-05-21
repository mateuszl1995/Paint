package tpal;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.*;

public class PluginLoader {
	public List<Plugin> loadFromDirectory(String pluginsDirectory) {
		List<Plugin> plugins = new ArrayList<Plugin>();
		File dir = new File(pluginsDirectory);
		for (File f : dir.listFiles()) {
			String pathToJar = f.getAbsolutePath();
			if (f.getName().endsWith("jar")) {
				try {
					JarFile jarFile = new JarFile(f);
					Enumeration<JarEntry> entries = jarFile.entries();
					
					URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return plugins;
	}
}
