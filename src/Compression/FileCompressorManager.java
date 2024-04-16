package Compression;

import Util.ByteWrapper;

import java.io.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages file compression operations including building frequency tables, writing headers,
 * and compressing file content using Huffman coding.
 */
public class FileCompressorManager {

    private final int numberOfBytes;

    private final String filePath;

    private final int chunkSize;

    // Counter for the number of matches found during compression
    private int fileMatches;

    /**
     * Constructs a FileCompressorManager.
     *
     * @param filePath      The path to the file to be compressed.
     * @param numberOfBytes The number of bytes to process at once.
     */
    public FileCompressorManager(String filePath, int numberOfBytes) {
        this.filePath = filePath;
        this.numberOfBytes = numberOfBytes;
        // Calculate the chunk size ensuring it's a multiple of the number of bytes
        this.chunkSize = 1_000_000 - (1_000_000 % numberOfBytes);
    }

    /**
     * Retrieves the output file path.
     *
     * @return The output file path.
     */
    private String getOutputFilePath() {
        File inputFile = new File(filePath);
        String parentDirectory = inputFile.getParent();
        String fileName = inputFile.getName();
        return parentDirectory + "\\Compressed." + numberOfBytes + "." + fileName + ".hc";
    }

    /**
     * Retrieves the size of the original file.
     *
     * @return The size of the original file.
     */
    public long getOriginalFileSize() {
        File inputFile = new File(filePath);
        return inputFile.length();
    }

    /**
     * Retrieves the size of the compressed file.
     *
     * @return The size of the compressed file.
     */
    public long getCompressedFileSize() {
        File outputFile = new File(getOutputFilePath());
        return outputFile.length();
    }

    /**
     * Calculates the compression ratio.
     *
     * @return The compression ratio.
     */
    public double getCompressionRatio() {
        return (double) getCompressedFileSize() / getOriginalFileSize();
    }

    /**
     * Builds a frequency table for the bytes in the file.
     *
     * @param numberOfBytes The number of bytes to process at once.
     * @param filePath      The path to the file to be compressed.
     * @return The frequency table.
     */
    public Map<ByteWrapper, Integer> buildFrequencyTable(int numberOfBytes, String filePath) {
        Map<ByteWrapper, Integer> frequencyTable = new HashMap<>();

        try (InputStream reader = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;

            while ((bytesRead = reader.read(buffer)) != -1) {
                // Process the bytes in the buffer
                for (int i = 0; i <= bytesRead - numberOfBytes; i += numberOfBytes) {
                    this.fileMatches++;
                    byte[] nByteUnit = Arrays.copyOfRange(buffer, i, i + numberOfBytes);
                    ByteWrapper byteWrapper = new ByteWrapper(nByteUnit);
                    frequencyTable.put(byteWrapper, frequencyTable.getOrDefault(byteWrapper, 0) + 1);
                }

                // Process the remaining bytes
                int mod = bytesRead % numberOfBytes;
                if (mod != 0) {
                    this.fileMatches++;
                    byte[] nByteUnit = Arrays.copyOfRange(buffer, bytesRead - mod, bytesRead);
                    ByteWrapper byteWrapper = new ByteWrapper(nByteUnit);
                    frequencyTable.put(byteWrapper, frequencyTable.getOrDefault(byteWrapper, 0) + 1);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return frequencyTable;
    }

    /**
     * Writes the header of the compressed file.
     *
     * @param codeWords The code words generated during compression.
     */
    private void writeHeader(Map<ByteWrapper, String> codeWords) {
        // write the header of the file (number of code words, code words)
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(getOutputFilePath()))) {
            writer.write(codeWords.size() + "\n");
            writer.write(fileMatches + "\n");
            // write the code words
            for (Map.Entry<ByteWrapper, String> entry : codeWords.entrySet()) {
                writer.write(entry.getValue() + "," + entry.getKey().toString() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes the compressed content of the file.
     *
     * @param codeWords The code words generated during compression.
     */
    private void writeContent(Map<ByteWrapper, String> codeWords) {

        try (InputStream reader = new FileInputStream(filePath)) {
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            StringBuilder compressedFile = new StringBuilder();

            try (FileOutputStream writer = new FileOutputStream(getOutputFilePath(), true)) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ((bytesRead = reader.read(buffer)) != -1) {
                    for (int i = 0; i <= bytesRead - numberOfBytes; i += numberOfBytes) {
                        byte[] nByteUnit = Arrays.copyOfRange(buffer, i, i + numberOfBytes);
                        ByteWrapper byteWrapper = new ByteWrapper(nByteUnit);
                        compressedFile.append(codeWords.get(byteWrapper));
                        while (compressedFile.length() >= 8) {
                            String chunk = compressedFile.substring(0, 8);
                            compressedFile.delete(0, 8);
                            int x = Integer.parseInt(chunk, 2);
                            byte y = (byte) x;
                            baos.write(y);
                        }
                    }
                    int mod = bytesRead % numberOfBytes;
                    if (mod != 0) {
                        byte[] nByteUnit = Arrays.copyOfRange(buffer, bytesRead - mod, bytesRead);
                        ByteWrapper byteWrapper = new ByteWrapper(nByteUnit);
                        compressedFile.append(codeWords.get(byteWrapper));
                        while (compressedFile.length() >= 8) {
                            String chunk = compressedFile.substring(0, 8);
                            compressedFile.delete(0, 8);
                            int x = Integer.parseInt(chunk, 2);
                            byte y = (byte) x;
                            baos.write(y);
                        }
                    }
                    writer.write(baos.toByteArray());
                    baos.reset();
                }

                if(!compressedFile.isEmpty()) {
                    byte[] compressedFileBytes = stringToBytes(compressedFile);
                    for (byte x : compressedFileBytes) {
                        writer.write(x);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Compresses the file using the provided code words.
     *
     * @param codeWords The code words generated during compression.
     */
    public void compressFile(Map<ByteWrapper, String> codeWords) {

        writeHeader(codeWords);
        writeContent(codeWords);

    }

    /**
     * Converts a binary string to a byte array.
     *
     * @param binaryString The binary string to convert.
     * @return The byte array.
     */
    public byte[] stringToBytes(StringBuilder binaryString) {
        int length = binaryString.length();
        int numOfBytes = (length + 7) / 8; // Calculate the number of bytes needed
        int paddedLength = numOfBytes * 8; // Calculate the total length after padding

        // Pad the binary string with zeros if needed
        binaryString = new StringBuilder(String.format("%-" + paddedLength + "s", binaryString).replace(' ', '0'));

        byte[] bytes = new byte[numOfBytes];

        for (int i = 0; i < numOfBytes; i++) {
            String chunk = binaryString.substring(i * 8, (i * 8) + 8);
            byte b = (byte) Integer.parseInt(chunk, 2);
            bytes[i] = b;
        }

        return bytes;
    }

}
