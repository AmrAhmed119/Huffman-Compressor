package Decompression;

import java.io.IOException;

/**
 * Manages file decompression operations including reading headers and
 * decompressing file content using Huffman coding.
 */
public class HuffmanDecompressor {

    private final String filePath;

    /**
     * Constructs a HuffmanDecompressor.
     *
     * @param filePath The path to the file to be decompressed.
     */
    public HuffmanDecompressor(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Method to perform the decompression.
     * This method decompresses the specified file using Huffman coding algorithm.
     * It prints decompression statistics such as execution time.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void decompress() throws IOException {
        double startTime = System.currentTimeMillis();
        FileDecompressorManager fileDecompressorManager = new FileDecompressorManager(filePath);
        fileDecompressorManager.decompressFile();
        double endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Decompression execution time: " + elapsedTime + " seconds");
    }
}
