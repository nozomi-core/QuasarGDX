package app.quasar.qgl.serialize;

public class BinaryUtils {

    public static byte[] xor255(byte[] input) {
        for(int i=0; i < input.length; i++) {
            int xorResult = input[i] ^ 255;
            input[i] = (byte) xorResult;
        }
        return input;
    }
}
