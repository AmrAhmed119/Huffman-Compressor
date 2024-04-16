import Compression.HuffmanCompressor;
import Decompression.HuffmanDecompressor;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class Main {

    public static void main(String[] args) throws IOException {

        if(args[0].equals("c")) {
            HuffmanCompressor huffmanCompressor = new HuffmanCompressor(args[1], Integer.parseInt(args[2]));
            huffmanCompressor.compress();
        } else if(args[0].equals("d")) {
            HuffmanDecompressor huffmanDecompressor = new HuffmanDecompressor(args[1]);
            huffmanDecompressor.decompress();
        } else {
            throw new IllegalArgumentException("Invalid argument");
        }

    }

    /**
     * Calculate the SHA-256 hash of a file
     *
     * @param filePath The path to the file
     * @return The SHA-256 hash of the file
     */
    public static String calculateSHA256(String filePath) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filePath), md)) {
                // Read the file content, the DigestInputStream will automatically update the MessageDigest
                byte[] buffer = new byte[8192];
                while (dis.read(buffer) != -1) {
                    // Reading the file content here updates the digest
                }
            }

            // Get the hash bytes
            byte[] hashBytes = md.digest();

            // Convert the hash bytes to a hexadecimal string
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hexStringBuilder.append(String.format("%02x", hashByte));
            }

            return hexStringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if two strings are equal by comparing their SHA-256 hashes
     *
     * @param str1 The first string
     * @param str2 The second string
     * @return True if the strings are equal, false otherwise
     */
    public static boolean areEqual(String str1, String str2) {
        String res1 = calculateSHA256(str1);
        String res2 = calculateSHA256(str2);
        // Check if both strings are equal, handling null cases
        return (res1 == null && res2 == null) || (res1 != null && res1.equals(res2));
    }

}