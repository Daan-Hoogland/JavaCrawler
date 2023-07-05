public class OSHelper {
    private static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() {
        return (OS.contains("win"));
    }


    public static boolean isUnix() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix") || (OS.contains("mac")));
    }
}
