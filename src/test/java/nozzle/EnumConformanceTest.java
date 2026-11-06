package nozzle;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

class EnumConformanceTest {
    private static final String PROPERTY_PREFIX = "nozzle.enum.";

    // Exact identifiers, not family-wide exemptions: new C members require review.
    private static final Map<String, String> UNEXPOSED = Map.ofEntries(
        entry("NozzleTransferMode.NOZZLE_TRANSFER_UNKNOWN", "Transfer mode is not exposed in Java"),
        entry("NozzleTransferMode.NOZZLE_TRANSFER_ZERO_COPY_SHARED_TEXTURE", "Transfer mode is not exposed in Java"),
        entry("NozzleTransferMode.NOZZLE_TRANSFER_GPU_COPY", "Transfer mode is not exposed in Java"),
        entry("NozzleTransferMode.NOZZLE_TRANSFER_CPU_COPY", "Transfer mode is not exposed in Java"),
        entry("NozzleSyncMode.NOZZLE_SYNC_NONE", "Sync mode is not exposed in Java"),
        entry("NozzleSyncMode.NOZZLE_SYNC_ACCESS_GUARDED", "Sync mode is not exposed in Java"),
        entry("NozzleSyncMode.NOZZLE_SYNC_GPU_FENCE_BEST_EFFORT", "Sync mode is not exposed in Java"),
        entry("NozzleBackendSharingMechanism.NOZZLE_SHARING_NONE", "Backend sharing query is not exposed in Java"),
        entry("NozzleBackendSharingMechanism.NOZZLE_SHARING_IOSURFACE", "Backend sharing query is not exposed in Java"),
        entry("NozzleBackendSharingMechanism.NOZZLE_SHARING_D3D11_NT_HANDLE", "Backend sharing query is not exposed in Java"),
        entry("NozzleBackendSharingMechanism.NOZZLE_SHARING_DMA_BUF", "Backend sharing query is not exposed in Java"),
        entry("NozzleBackendSharingMechanism.NOZZLE_SHARING_OPENGL_TEXTURE", "Backend sharing query is not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_NONE", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_SENDER", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_RECEIVER", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_WRITABLE_FRAMES", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_NATIVE_TEXTURE_PUBLISH", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_DIRECT_EXTERNAL_PUBLISH", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_CPU_READ", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_CPU_WRITE", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_ZERO_COPY_RECEIVE", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_ZERO_COPY_PUBLISH", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_REQUIRES_MATCHING_BACKEND", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_SINGLE_SENDER_PER_PROCESS", "Backend capability flags are not exposed in Java"),
        entry("NozzleBackendCapabilityFlags.NOZZLE_BACKEND_CAP_MAY_REQUIRE_RUNTIME_PROBE", "Backend capability flags are not exposed in Java"),
        entry("NozzleFallbackCategory.NOZZLE_FALLBACK_CATEGORY_NONE", "Fallback category is not exposed in Java"),
        entry("NozzleFallbackCategory.NOZZLE_FALLBACK_CATEGORY_STORAGE_COMPATIBLE", "Fallback category is not exposed in Java"),
        entry("NozzleFallbackCategory.NOZZLE_FALLBACK_CATEGORY_CHANNEL_EXPANSION", "Fallback category is not exposed in Java"),
        entry("NozzleFallbackCategory.NOZZLE_FALLBACK_CATEGORY_QUALITY_LOSS", "Fallback category is not exposed in Java"),
        entry("NozzleChannelSwizzle.NOZZLE_CHANNEL_SWIZZLE_IDENTITY", "Channel swizzle is not exposed in Java"),
        entry("NozzleChannelSwizzle.NOZZLE_CHANNEL_SWIZZLE_SWAP_RB", "Channel swizzle is not exposed in Java")
    );

    @Test
    void productionEnumsMatchCoreManifest() {
        Set<String> covered = new HashSet<>();
        checkFamily(covered, "NozzleErrorCode", ErrorCode.values(),
            c -> c == ErrorCode.OK ? "NOZZLE_OK" : "NOZZLE_ERROR_" + c.name(),
            ErrorCode::value, ErrorCode::fromValue);
        checkFamily(covered, "NozzleBackendType", BackendType.values(),
            c -> "NOZZLE_BACKEND_" + c.name(), BackendType::value, BackendType::fromValue);
        checkFamily(covered, "NozzleTextureFormat", TextureFormat.values(),
            c -> "NOZZLE_FORMAT_" + c.name(), TextureFormat::value, TextureFormat::fromValue);
        checkFamily(covered, "NozzleReceiveMode", ReceiveMode.values(),
            c -> "NOZZLE_RECEIVE_" + c.name(), ReceiveMode::value, ReceiveMode::fromValue);
        checkFamily(covered, "NozzleFrameStatus", FrameStatus.values(),
            c -> "NOZZLE_FRAME_" + c.name(), FrameStatus::value, FrameStatus::fromValue);
        checkFamily(covered, "NozzleTextureOrigin", TextureOrigin.values(),
            c -> "NOZZLE_ORIGIN_" + c.name(), TextureOrigin::value, TextureOrigin::fromValue);
        checkFamily(covered, "NozzleFormatSource", FormatSource.values(),
            c -> "NOZZLE_FORMAT_SOURCE_" + c.name(), FormatSource::value, FormatSource::fromValue);
        checkFamily(covered, "NozzleNativeFormatKind", NativeFormatKind.values(),
            c -> "NOZZLE_NATIVE_KIND_" + c.name(), NativeFormatKind::value, NativeFormatKind::fromValue);

        UNEXPOSED.forEach((identifier, reason) -> {
            assertFalse(reason.isBlank(), identifier + " needs an unexposed reason");
            assertTrue(covered.add(identifier), identifier + " is both mapped and unexposed");
        });
        Set<String> oracleMembers = System.getProperties().stringPropertyNames().stream()
            .filter(key -> key.startsWith(PROPERTY_PREFIX))
            .map(key -> key.substring(PROPERTY_PREFIX.length()))
            .collect(Collectors.toSet());
        assertEquals(oracleMembers, covered, "Every C member must be mapped or explicitly unexposed");
    }

    private static <E extends Enum<E>> void checkFamily(
            Set<String> covered, String family, E[] cases, Function<E, String> cName,
            ToIntFunction<E> value, IntFunction<E> decode) {
        for (E item : cases) {
            String identifier = family + "." + cName.apply(item);
            assertTrue(covered.add(identifier), "Duplicate mapping: " + identifier);
            String property = System.getProperty(PROPERTY_PREFIX + identifier);
            assertNotNull(property, "Missing core oracle member: " + identifier);
            int expected = Integer.parseInt(property);
            assertEquals(expected, value.applyAsInt(item), identifier + " value");
            assertSame(item, decode.apply(expected), identifier + " fromValue");
        }
    }
}
