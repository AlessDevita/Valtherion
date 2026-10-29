package fr.alessdevita.valtherion.utils;

public class Util {
    public enum EnumOS {
        WINDOWS,
        OSX,
        LINUX,
        OTHER
    }

    public static EnumOS getOSType() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("mac") || os.contains("darwin")) {
            return EnumOS.OSX;
        }
        if (os.contains("win")) {
            return EnumOS.WINDOWS;
        }
        if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
            return EnumOS.LINUX;
        }
        return EnumOS.OTHER;
    }
}
