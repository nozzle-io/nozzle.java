package nozzle;

import java.nio.ByteBuffer;

/**
 * Static utility methods for discovery and pixel manipulation.
 */
public final class Nozzle {

    private Nozzle() {}

    public static SenderInfo[] enumerateSenders() {
        return NozzleNative.enumerateSenders();
    }

    public static Device getDefaultDevice() {
        return Device.getDefault();
    }

    public static void swizzleChannels(ByteBuffer src, ByteBuffer dst, int width, int height,
                                        int srcRowBytes, int dstRowBytes,
                                        TextureFormat format, byte[] permuteMap) {
        NozzleNative.swizzleChannels(src, dst, width, height,
            srcRowBytes, dstRowBytes, format.value(), permuteMap);
    }

    public static void widenUint16ToUint32(ByteBuffer src, ByteBuffer dst, int width, int height,
                                            int srcRowBytes, int dstRowBytes, int channels) {
        NozzleNative.widenUint16ToUint32(src, dst, width, height, srcRowBytes, dstRowBytes, channels);
    }

    public static void convertUint32ToFloat32(ByteBuffer src, ByteBuffer dst, int width, int height,
                                               int srcRowBytes, int dstRowBytes, int channels) {
        NozzleNative.convertUint32ToFloat32(src, dst, width, height, srcRowBytes, dstRowBytes, channels);
    }

    public static boolean isGpuAvailable() {
        // On CI there is no GPU — avoid native crash (SIGSEGV in Metal init)
        if (System.getenv("CI") != null || System.getenv("GITHUB_ACTIONS") != null) {
            return false;
        }
        try (Sender s = Sender.create(new SenderDesc("nozzle-java-gpu-check", "nozzle-java", 1, false))) {
            return true;
        } catch (NozzleException e) {
            return false;
        }
    }
}
