package Decompression;

import Util.ByteWrapper;

import java.io.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Manages file decompression operations including reading headers, writing files,
 * and decompressing file content using Huffman coding.
 */
public class FileDecompressorManager {

    private final String filePath;
    private int chunkSize;
    private int bytesRead;
    private int fileMatches;

    /**
     * Constructs a FileDecompressorManager.
     *
     * @param filePath The path to the file to be decompressed.
     */
    public FileDecompressorManager(String filePath) {
        this.filePath = filePath;
        this.bytesRead = 0;
    }

    /**
     * Retrieves the output file path.
     *
     * @return The output file path.
     */
    private String getExtractedFileName() {
        int lastSlashIndex = filePath.lastIndexOf("\\");
        int extensionIndex = filePath.lastIndexOf(".");
        String fileWithExtension = filePath.substring(lastSlashIndex + 1, extensionIndex);
        return filePath.substring(0, lastSlashIndex + 1) + "Decompressed." + fileWithExtension.substring(11);
    }

    /**
     * Reads the header of the compressed file.
     *
     * @return The map of codewords.
     */
    private Map<String, ByteWrapper> readHeader() {
        Map<String, ByteWrapper> codewords = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            // read the size of the map
            line = reader.readLine();
            this.bytesRead += line.length() + 1;
            int size = Integer.parseInt(line);
            line = reader.readLine();
            this.bytesRead += line.length() + 1;
            this.fileMatches = Integer.parseInt(line);

            // read the codewords
            for(int i = 0; i < size; i++) {
                line = reader.readLine();
                this.bytesRead += line.length() + 1;
                String[] parts = line.split(",");
                int numberOfBytes = parts.length - 1;
                this.chunkSize = 1_000_000 - (1_000_000 % numberOfBytes);
                String codeword = parts[0];
                byte[] byteArr = new byte[parts.length - 1];
                for(int j = 1; j < parts.length; j++) {
                    byteArr[j - 1] = Byte.parseByte(parts[j]);
                }
                ByteWrapper byteWrapper = new ByteWrapper(byteArr);
                codewords.put(codeword, byteWrapper);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return codewords;
    }

    /**
     * Writes the decompressed data to the output file.
     *
     * @param fileContent The content of the file to be decompressed.
     * @param codewords    The map of codewords.
     * @return The remaining code to be written.
     */
    private StringBuilder writeFile(StringBuilder fileContent, Map<String, ByteWrapper> codewords) {

        StringBuilder currentCode = new StringBuilder();
        // write the decoded data to the new file
        try (FileOutputStream writer = new FileOutputStream(getExtractedFileName(), true)) {
            // Write the encoded data to the output file
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            for (int i = 0; i < fileContent.length(); i++) {
                if(fileMatches == 0)
                    break;

                currentCode.append(fileContent.charAt(i));

                // Check if the current code matches any codeword
                if (codewords.containsKey(currentCode.toString())) {
                    // Write the decoded byte to the output file
                    fileMatches--;
                    byte[] arr = codewords.get(currentCode.toString()).getBytes();
                    baos.write(arr);
                    currentCode.setLength(0);
                }

            }

            if(baos.size() > 0)
                writer.write(baos.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentCode;
    }

    /**
     * Decompresses the file using Huffman coding.
     *
     * @throws IOException If an I/O error occurs.
     */
    public void decompressFile() throws IOException {

        // read the header of the file
        Map<String, ByteWrapper> codeWords = readHeader();

        OutputStream outputStream = new FileOutputStream(getExtractedFileName(), false);

        // Clear the file by truncating it to zero length
        outputStream.close();

        // read the encoded data
        try (InputStream reader = new FileInputStream(filePath)) {
            // skip the bytes of the header that have already been read
            reader.skip(this.bytesRead);
            byte[] buffer = new byte[chunkSize];
            int bytesRead;
            StringBuilder encodedData = new StringBuilder();

            while ((bytesRead = reader.read(buffer)) != -1) {
                // Check if bytesRead is less than 1024 to handle the last chunk
                for(byte b : buffer) {
                    encodedData.append(byteToBinaryString(b));
                }
                encodedData = writeFile(encodedData, codeWords);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Converts a byte to a binary string.
     *
     * @param b The byte to convert.
     * @return The binary string representation of the byte.
     */
    public static String byteToBinaryString(byte b) {
        StringBuilder binaryString = new StringBuilder(8);
        for (int i = 7; i >= 0; i--) {
            binaryString.append((b >> i) & 1);
        }
        return binaryString.toString();
    }
}
