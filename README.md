# nozzle.java

> This codebase is currently in its AI-slob prototyping phase: the code runs on momentum, vibes, and plausible intent.
> Proper debugging will be introduced once demand graduates from hypothetical to measurable.

Java bindings for [nozzle](https://github.com/nozzle-io/nozzle) — cross-platform GPU texture sharing between local processes.

## Disclaimer / Notice

This library is currently a work in progress and contains many incomplete features and unverified implementations.
Although it may appear usable at first glance, it may not function correctly.

## Build Requirements

- Java 11+
- C++17 compiler (clang / MSVC / g++)
- macOS 12+, Windows 10+, or Linux

The nozzle C library is built from source via a git submodule. A `Makefile` compiles the JNI shared library before Gradle runs Java compilation and tests.

## Build

```bash
make
./gradlew build
```

### Run Tests

```bash
make
./gradlew test
```

## Usage

### Sender

```java
import nozzle.*;

try (Sender sender = Sender.create(new SenderDesc("java-sender", "MyApp", 3, false))) {
    try (Frame frame = sender.acquireWritableFrame(1920, 1080, TextureFormat.RGBA8_UNORM)) {
        try (MappedPixels pixels = frame.lockWritablePixels(TextureOrigin.TOP_LEFT)) {
            for (int y = 0; y < pixels.height(); y++) {
                ByteBuffer row = pixels.row(y);
                while (row.hasRemaining()) {
                    row.put((byte) 0xFF);
                }
            }
        }
        sender.commitFrame(frame);
    }
}
```

### Receiver

```java
import nozzle.*;

try (Receiver receiver = Receiver.create(new ReceiverDesc("java-sender", "MyViewer", ReceiveMode.LATEST_ONLY))) {
    try (Frame frame = receiver.acquireFrame(5000)) {
        FrameInfo info = frame.info();
        System.out.printf("%dx%d frame #%d%n", info.width(), info.height(), info.frameIndex());
    }
}
```

### Discovery

```java
import nozzle.*;

SenderInfo[] senders = Nozzle.enumerateSenders();
System.out.printf("found %d senders%n", senders.length);
```

### GPU Check

```java
if (Nozzle.isGpuAvailable()) {
    System.out.println("GPU available");
}
```

## Error Handling

All fallible operations throw `NozzleException` (a `RuntimeException`):

```java
try {
    Sender sender = Sender.create(new SenderDesc("", "", 0, false));
} catch (NozzleException e) {
    if (e.errorCode() == ErrorCode.INVALID_ARGUMENT) {
        // handle bad args
    }
}
```

## Texture Formats

| Format | Bytes/Pixel |
|--------|-------------|
| `R8_UNORM` | 1 |
| `RG8_UNORM` | 2 |
| `RGBA8_UNORM` / `BGRA8_UNORM` | 4 |
| `RGBA8_SRGB` / `BGRA8_SRGB` | 4 |
| `R16_UNORM` | 2 |
| `RG16_UNORM` | 4 |
| `RGBA16_UNORM` | 8 |
| `R16_FLOAT` | 2 |
| `RG16_FLOAT` | 4 |
| `RGBA16_FLOAT` | 8 |
| `R32_FLOAT` | 4 |
| `RG32_FLOAT` | 8 |
| `RGBA32_FLOAT` | 16 |
| `R32_UINT` | 4 |
| `RGBA32_UINT` | 16 |
| `DEPTH32_FLOAT` | 4 |

## Platform Notes

- **macOS**: Links Metal, IOSurface, Foundation, Accelerate, OpenGL frameworks
- **Windows**: Links d3d11, dxgi, opengl32, bcrypt
- **Linux**: Links drm, gbm, EGL, GL

## License

MIT
