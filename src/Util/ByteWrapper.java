package Util;

import java.util.Arrays;

/**
 * Wrapper class for byte arrays to enable comparison and hashing.
 */
public class ByteWrapper {

    private final byte[] bytes;

    /**
     * Constructor to initialize the byte array.
     *
     * @param bytes The byte array to be wrapped.
     */
    public ByteWrapper(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Retrieves the byte array.
     *
     * @return The byte array.
     */
    public byte[] getBytes() {
        return bytes;
    }

    /**
     * Compares the byte array with another byte array.
     *
     * @param obj The other byte array to compare with.
     * @return True if the byte arrays are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof ByteWrapper)) return false;
        ByteWrapper other = (ByteWrapper) obj;
        return Arrays.equals(this.bytes, other.bytes);
    }

    /**
     * Generates a hash code for the byte array.
     *
     * @return The hash code for the byte array.
     */
    @Override
    public int hashCode() {
        return Arrays.hashCode(bytes);
    }

    /**
     * Retrieves the string representation of the byte array.
     *
     * @return The string representation of the byte array.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < bytes.length; i++) {
            sb.append(bytes[i]);
            if(i != bytes.length - 1) sb.append(",");
        }
        return sb.toString();
    }

}
