package xyz.syodo.plugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader {

    private static PluginLoader loader;

    public static PluginLoader get() {
        if(loader == null) loader = new PluginLoader();
        return loader;
    }

    private PluginLoader() {}

    public void loadAll() throws URISyntaxException, IOException, ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String path = PluginLoader.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        System.out.println(path.substring(0, path.lastIndexOf("/")));
        File pluginsFolder = new File(path.substring(0, path.lastIndexOf("/")) + "/plugins");
        if(!pluginsFolder.exists()) pluginsFolder.mkdirs();
        for(File file : pluginsFolder.listFiles()) {
            if(file.getName().endsWith(".jar")) {
                loadJar(file);
            }
        }
    }

    public void loadJar(File file) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if(!file.getName().endsWith(".jar")) throw new IllegalArgumentException("File is not a jar file!");
        Path pathToJar = file.toPath();
        JarFile jarFile = new JarFile(pathToJar.toString());
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = { new URL("jar:file:" + pathToJar+"!/") };
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if(je.isDirectory() || !je.getName().endsWith(".class")){
                continue;
            }
            String className = je.getName().substring(0,je.getName().length()-6);
            className = className.replace('/', '.');
            Class c = cl.loadClass(className);
            if(Plugin.class.isAssignableFrom(c)) {
                if(c.getConstructors().length == 1) {
                    ((Plugin) c.getConstructors()[0].newInstance()).load();
                }
            }
        }
    }

}
