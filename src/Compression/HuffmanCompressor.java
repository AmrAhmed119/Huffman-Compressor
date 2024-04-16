package Compression;

import Util.ByteWrapper;
import Util.HuffmanTree;

import java.util.Map;

/**
 * Compresses a file using Huffman coding algorithm.
 */
public class HuffmanCompressor {

    private final String filePath;
    private final int numberOfBytes;

    /**
     * Constructor to initialize the compressor with file path and number of bytes.
     *
     * @param filePath      The path to the file to be compressed.
     * @param numberOfBytes The number of bytes to process at once.
     */
    public HuffmanCompressor(String filePath, int numberOfBytes) {
        this.filePath = filePath;
        this.numberOfBytes = numberOfBytes;
    }

    /**
     * Method to perform the compression.
     * This method compresses the specified file using Huffman coding algorithm.
     * It prints compression statistics such as execution time, original file size,
     * compressed file size, and compression ratio.
     */
    public void compress() {
        double startTime = System.currentTimeMillis();
        FileCompressorManager fileCompressorManager = new FileCompressorManager(filePath, numberOfBytes);
        Map<ByteWrapper, Integer> frequencyTable = fileCompressorManager.buildFrequencyTable(numberOfBytes, filePath);
        HuffmanTree huffmanTree = new HuffmanTree(frequencyTable);
        huffmanTree.buildTree();
        Map<ByteWrapper, String> codeWords = huffmanTree.buildCodeWords();
        fileCompressorManager.compressFile(codeWords);
        double endTime = System.currentTimeMillis();
        double elapsedTime = (endTime - startTime) / 1000.0;
        System.out.println("Compression execution time: " + elapsedTime + " seconds");
        System.out.println("Original file size: " + fileCompressorManager.getOriginalFileSize() + " bytes");
        System.out.println("New file size: " + fileCompressorManager.getCompressedFileSize() + " bytes");
        System.out.println("Compression ratio: " + fileCompressorManager.getCompressionRatio());
    }

}
