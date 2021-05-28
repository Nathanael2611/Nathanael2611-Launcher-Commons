package fr.nathanael2611.commons.launcher.util;

import fr.nathanael2611.commons.launcher.BaseLauncher;

import java.io.File;
import java.lang.reflect.Field;

/**
 * The Java Util
 *
 * <p>
 *     Contains some useful things about the launching
 * </p>
 *
 * @author Litarvan
 * @version 3.0.4
 * @since 3.0.0-BETA
 */
public class JavaUtil
{

    public static boolean hasJava64()
    {
        return System.getProperty("sun.arch.data.model").equalsIgnoreCase("64");
    }

    /**
     * Return the special default VM arguments
     *
     * @return The special VM args
     */
    public static String[] getSpecialArgs()
    {
        return new String[]{"-XX:-UseAdaptiveSizePolicy", "-XX:+UseConcMarkSweepGC"};
    }

    /**
     * Create an argument for the mac dock name
     *
     * @param name The name to set
     *
     * @return The generated argument
     */
    public static String macDockName(String name)
    {
        return "-Xdock:name=" + name;
    }

    /**
     * Return the java executable path
     *
     * @return The java command
     */
    public static String getJavaCommand(BaseLauncher launcher)
    {
        String dir = System.getProperty("java.home");
        if(!hasJava64() && launcher.getUpdateManager().hasCustomJre()) {
            dir = launcher.CUSTOM_JRE_DIR.getAbsolutePath();
        }
        return dir + File.separator + "bin" + File.separator + "java";
    }

    /**
     * Manually set the Java Library Path
     *
     * @param path The new library path
     * @throws Exception If it failed
     */
    public static void setLibraryPath(String path) throws Exception
    {
        System.setProperty("java.library.path", path);

        Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
        fieldSysPath.setAccessible(true);
        fieldSysPath.set(null, null);
    }
}
