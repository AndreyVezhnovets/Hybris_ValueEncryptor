
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Base64 {

    private static final byte[] _STANDARD_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
    private static final byte[] _STANDARD_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, -9, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, -9, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
    private static final byte[] _URL_SAFE_ALPHABET = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
    private static final byte[] _URL_SAFE_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 62, -9, -9, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -9, -9, -9, -1, -9, -9, -9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -9, -9, -9, -9, 63, -9, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -9, -9, -9, -9};
    private static final byte[] _ORDERED_ALPHABET = new byte[]{45, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 95, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122};
    private static final byte[] _ORDERED_DECODABET = new byte[]{-9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -5, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -5, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, -9, 0, -9, -9, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, -9, -9, -9, -1, -9, -9, -9, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, -9, -9, -9, -9, 37, -9, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, -9, -9, -9, -9};

    private static byte[] getAlphabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_ALPHABET;
        } else {
            return (options & 32) == 32 ? _ORDERED_ALPHABET : _STANDARD_ALPHABET;
        }
    }

    private static byte[] getDecodabet(int options) {
        if ((options & 16) == 16) {
            return _URL_SAFE_DECODABET;
        } else {
            return (options & 32) == 32 ? _ORDERED_DECODABET : _STANDARD_DECODABET;
        }
    }

    private Base64() {
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            usage("Not enough arguments.");
        } else {
            String flag = args[0];
            String infile = args[1];
            String outfile = args[2];
            if (flag.equals("-e")) {
                encodeFileToFile(infile, outfile);
            } else if (flag.equals("-d")) {
                decodeFileToFile(infile, outfile);
            } else {
                usage("Unknown flag: " + flag);
            }
        }

    }

    private static void usage(String msg) {
        System.err.println(msg);
        System.err.println("Usage: java Base64 -e|-d inputfile outputfile");
    }

    private static byte[] encode3to4(byte[] b4, byte[] threeBytes, int numSigBytes, int options) {
        encode3to4(threeBytes, 0, numSigBytes, b4, 0, options);
        return b4;
    }

    private static void encode3to4(byte[] source, int srcOffset, int numSigBytes, byte[] destination, int destOffset, int options) {
        byte[] ALPHABET = getAlphabet(options);
        int inBuff = (numSigBytes > 0 ? source[srcOffset] << 24 >>> 8 : 0) | (numSigBytes > 1 ? source[srcOffset + 1] << 24 >>> 16 : 0) | (numSigBytes > 2 ? source[srcOffset + 2] << 24 >>> 24 : 0);
        switch(numSigBytes) {
            case 1:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = 61;
                destination[destOffset + 3] = 61;
                return;
            case 2:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = 61;
                return;
            case 3:
                destination[destOffset] = ALPHABET[inBuff >>> 18];
                destination[destOffset + 1] = ALPHABET[inBuff >>> 12 & 63];
                destination[destOffset + 2] = ALPHABET[inBuff >>> 6 & 63];
                destination[destOffset + 3] = ALPHABET[inBuff & 63];
                return;
            default:
        }
    }

    public static String encodeBytes(byte[] source, int options) {
        return encodeBytes(source, 0, source.length, options);
    }


    public static String encodeBytes(byte[] source, int off, int len, int options) {
        int dontBreakLines = options & 8;
        int gzip = options & 2;
        if (gzip == 2) {
            ByteArrayOutputStream baos = null;
            GZIPOutputStream gzos = null;
            Base64.OutputStream b64os = null;

            label239: {
                try {
                    baos = new ByteArrayOutputStream();
                    b64os = new Base64.OutputStream(baos, 1 | options);
                    gzos = new GZIPOutputStream(b64os);
                    gzos.write(source, off, len);
                    gzos.close();
                    break label239;
                } catch (IOException var31) {
                    var31.printStackTrace();
                } finally {
                    try {
                        assert gzos != null;
                        gzos.close();
                    } catch (Exception ignored) {

                    }

                    try {
                        b64os.close();
                    } catch (Exception ignored) {

                    }

                    try {
                        baos.close();
                    } catch (Exception ignored) {

                    }

                }

                return null;
            }

            return new String(baos.toByteArray(), StandardCharsets.UTF_8);
        } else {
            boolean breakLines = dontBreakLines == 0;
            int len43 = len * 4 / 3;
            byte[] outBuff = new byte[len43 + (len % 3 > 0 ? 4 : 0) + (breakLines ? len43 / 76 : 0)];
            int d = 0;
            int e = 0;
            int len2 = len - 2;

            for(int lineLength = 0; d < len2; e += 4) {
                encode3to4(source, d + off, 3, outBuff, e, options);
                lineLength += 4;
                if (breakLines && lineLength == 76) {
                    outBuff[e + 4] = 10;
                    ++e;
                    lineLength = 0;
                }

                d += 3;
            }

            if (d < len) {
                encode3to4(source, d + off, len - d, outBuff, e, options);
                e += 4;
            }

            return new String(outBuff, 0, e, StandardCharsets.UTF_8);
        }
    }

    private static int decode4to3(byte[] source, byte[] destination, int destOffset, int options) {
        byte[] decodAbet = getDecodabet(options);
        int outBuff;
        if (source[2] == 61) {
            outBuff = (decodAbet[source[0]] & 255) << 18 | (decodAbet[source[1]] & 255) << 12;
            destination[destOffset] = (byte)(outBuff >>> 16);
            return 1;
        } else if (source[3] == 61) {
            outBuff = (decodAbet[source[0]] & 255) << 18 | (decodAbet[source[1]] & 255) << 12 | (decodAbet[source[2]] & 255) << 6;
            destination[destOffset] = (byte)(outBuff >>> 16);
            destination[destOffset + 1] = (byte)(outBuff >>> 8);
            return 2;
        } else {
            try {
                outBuff = (decodAbet[source[0]] & 255) << 18 | (decodAbet[source[1]] & 255) << 12 | (decodAbet[source[2]] & 255) << 6 | decodAbet[source[3]] & 255;
                destination[destOffset] = (byte)(outBuff >> 16);
                destination[destOffset + 1] = (byte)(outBuff >> 8);
                destination[destOffset + 2] = (byte)outBuff;
                return 3;
            } catch (Exception var7) {
                return -1;
            }
        }
    }

    public static byte[] decode(byte[] source, int off, int len, int options) {
        byte[] DECODABET = getDecodabet(options);
        int len34 = len * 3 / 4;
        byte[] outBuff = new byte[len34];
        int outBuffPosn = 0;
        byte[] b4 = new byte[4];
        int b4Posn = 0;


        for(int i = off; i < off + len; ++i) {
            byte sbiCrop = (byte)(source[i] & 127);
            byte sbiDecode = DECODABET[sbiCrop];
            if (sbiDecode < -5) {
                return null;
            }

            if (sbiDecode >= -1) {
                b4[b4Posn++] = sbiCrop;
                if (b4Posn > 3) {
                    outBuffPosn += decode4to3(b4, outBuff, outBuffPosn, options);
                    b4Posn = 0;
                    if (sbiCrop == 61) {
                        break;
                    }
                }
            }
        }

        byte[] out = new byte[outBuffPosn];
        System.arraycopy(outBuff, 0, out, 0, outBuffPosn);
        return out;
    }

    public static byte[] decode(String s) {
        return decode(s, 0);
    }

    public static byte[] decode(String s, int options) {
        byte[] bytes;
        bytes = s.getBytes(StandardCharsets.UTF_8);

        bytes = decode(bytes, 0, bytes.length, options);
        if (bytes != null && bytes.length >= 4) {
            int head = bytes[0] & 255 | bytes[1] << 8 & '\uff00';
            if (35615 == head) {
                byte[] buffer = new byte[2048];

                try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ByteArrayInputStream bais = new ByteArrayInputStream(bytes); GZIPInputStream gzis = new GZIPInputStream(bais)) {

                    int length;
                    while ((length = gzis.read(buffer)) >= 0) {
                        baos.write(buffer, 0, length);
                    }

                    bytes = baos.toByteArray();
                } catch (IOException ignored) {
                }
            }
        }

        return bytes;
    }

    public static void encodeFileToFile(String infile, String outfile) {

        try (java.io.InputStream in = new InputStream(new BufferedInputStream(new FileInputStream(infile)), 1); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {
            byte[] buffer = new byte[65536];

            int read;
            while ((read = in.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }

        } catch (IOException var19) {
            var19.printStackTrace();
        }

    }

    public static void decodeFileToFile(String infile, String outfile) {

        try (java.io.InputStream in = new InputStream(new BufferedInputStream(new FileInputStream(infile)), 0); BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outfile))) {
            byte[] buffer = new byte[65536];

            int read;
            while ((read = in.read(buffer)) >= 0) {
                out.write(buffer, 0, read);
            }

        } catch (IOException var19) {
            var19.printStackTrace();
        }

    }

    public static class InputStream extends FilterInputStream {
        private final boolean encode;
        private int position;
        private final byte[] buffer;
        private final int bufferLength;
        private int numSigBytes;
        private int lineLength;
        private final boolean breakLines;
        private final int options;
        private final byte[] decodabet;

        public InputStream(java.io.InputStream in, int options) {
            super(in);
            this.breakLines = (options & 8) != 8;
            this.encode = (options & 1) == 1;
            this.bufferLength = this.encode ? 4 : 3;
            this.buffer = new byte[this.bufferLength];
            this.position = -1;
            this.lineLength = 0;
            this.options = options;
            this.decodabet = Base64.getDecodabet(options);
        }

        public int read() throws IOException {
            if (this.position < 0) {
                byte[] b4;
                int i;
                int b;
                if (!this.encode) {
                    b4 = new byte[4];

                    for(i = 0; i < 4; ++i) {

                        do {
                            b = this.in.read();
                        } while(b >= 0 && this.decodabet[b & 127] <= -5);

                        if (b < 0) {
                            break;
                        }

                        b4[i] = (byte)b;
                    }

                    if (i != 4) {
                        if (i == 0) {
                            return -1;
                        }

                        throw new IOException("Improperly padded Base64 input.");
                    }

                    this.numSigBytes = Base64.decode4to3(b4, this.buffer, 0, this.options);
                    this.position = 0;
                } else {
                    b4 = new byte[3];
                    i = 0;
                    b = 0;

                    while(true) {
                        if (b >= 3) {
                            if (i <= 0) {
                                return -1;
                            }

                            Base64.encode3to4(b4, 0, i, this.buffer, 0, this.options);
                            this.position = 0;
                            this.numSigBytes = 4;
                            break;
                        }

                        if (b >= 0) {
                            b4[b] = (byte)b;
                            ++i;
                        }

                        ++b;
                    }
                }
            }

            if (this.position >= this.numSigBytes) {
                return -1;
            } else if (this.encode && this.breakLines && this.lineLength >= 76) {
                this.lineLength = 0;
                return 10;
            } else {
                ++this.lineLength;
                int b = this.buffer[this.position++];
                if (this.position >= this.bufferLength) {
                    this.position = -1;
                }

                return b & 255;
            }
        }

        public int read(byte[] dest, int off, int len) throws IOException {
            int i;
            for(i = 0; i < len; ++i) {
                int b = this.read();
                if (b < 0) {
                    if (i == 0) {
                        return -1;
                    }
                    break;
                }

                dest[off + i] = (byte)b;
            }

            return i;
        }
    }

    public static class OutputStream extends FilterOutputStream {
        private final boolean encode;
        private int position;
        private byte[] buffer;
        private final int bufferLength;
        private int lineLength;
        private final boolean breakLines;
        private final byte[] b4;
        private boolean suspendEncoding;
        private final int options;
        private final byte[] decodabet;

        public OutputStream(java.io.OutputStream out, int options) {
            super(out);
            this.breakLines = (options & 8) != 8;
            this.encode = (options & 1) == 1;
            this.bufferLength = this.encode ? 3 : 4;
            this.buffer = new byte[this.bufferLength];
            this.position = 0;
            this.lineLength = 0;
            this.suspendEncoding = false;
            this.b4 = new byte[4];
            this.options = options;
            this.decodabet = Base64.getDecodabet(options);
        }

        public void write(int theByte) throws IOException {
            if (this.suspendEncoding) {
                super.out.write(theByte);
            } else {
                if (this.encode) {
                    this.buffer[this.position++] = (byte)theByte;
                    if (this.position >= this.bufferLength) {
                        this.out.write(Base64.encode3to4(this.b4, this.buffer, this.bufferLength, this.options));
                        this.lineLength += 4;
                        if (this.breakLines && this.lineLength >= 76) {
                            this.out.write(10);
                            this.lineLength = 0;
                        }

                        this.position = 0;
                    }
                } else if (this.decodabet[theByte & 127] > -5) {
                    this.buffer[this.position++] = (byte)theByte;
                    if (this.position >= this.bufferLength) {
                        int len = Base64.decode4to3(this.buffer, this.b4, 0, this.options);
                        this.out.write(this.b4, 0, len);
                        this.position = 0;
                    }
                } else if (this.decodabet[theByte & 127] != -5) {
                    throw new IOException("Invalid character in Base64 data.");
                }

            }
        }

        public void write(byte[] bytes, int off, int len) throws IOException {
            if (this.suspendEncoding) {
                super.out.write(bytes, off, len);
            } else {
                for(int i = 0; i < len; ++i) {
                    this.write(bytes[off + i]);
                }

            }
        }

        public void flushBase64() throws IOException {
            if (this.position > 0) {
                if (!this.encode) {
                    throw new IOException("Base64 input not properly padded.");
                }

                this.out.write(Base64.encode3to4(this.b4, this.buffer, this.position, this.options));
                this.position = 0;
            }

        }

        public void close() throws IOException {
            this.flushBase64();
            super.close();
            this.buffer = null;
            this.out = null;
        }
    }
}
