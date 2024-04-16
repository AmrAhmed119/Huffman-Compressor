# Huffman Compression and Decompression

## Description

Huffman coding is a widely used method for lossless data compression. The algorithm collects statistics from the input file to determine the frequency of each symbol or group of symbols and then constructs an optimal prefix-free binary tree to represent the data efficiently.


## Features

- 📁 Supports compressing and decompressing arbitrary files.
- ⚙️ Flexible Configuration where users can specify the number of bytes considered together during compression, offering customization options for different file types and optimizing compression ratios.
- 📦 Provides a single runnable JAR file for both compression and decompression.
- 🔄 Simple Usage With a command-line interface, compressing and decompressing files becomes straightforward and accessible.

## Compression and Decompression Statistics 📋

| Test File   | Bytes (n) | Compression Time (s) | Decompression Time (s) | Original Size (bytes) | Compressed Size (bytes) | Compression Ratio |
|-------------|-----------|-----------------------|-------------------------|------------------------|--------------------------|-------------------|
| test1.seq    | 1         | 52.321                | 98.485                 | 494,252,260            | 250,469,785              | 0.50677           |
| test1.seq    | 2         | 25.702                | 61.99                 | 494,252,260            | 207,641,631              | 0.4201           |
| test1.seq    | 3         | 22.435                | 58.419                 | 494,252,260            | 187,004,146              | 0.37835           |
| test2.pdf   | 1         | 2.304                 | 6.088                   | 13,294,045             | 13,268,525               | 0.99808           |
| test2.pdf   | 2         | 4.548                 | 8.054                   | 13,294,045             | 14,751,570               | 1.10964           |
| test2.pdf   | 3         | 25.381                | 21.715                  | 13,294,045             | 133,335,497              | 10.02972          |
| test3.pdf   | 1         | 0.189                | 0.356                  | 825,392             | 773,590             | 0.937          |
| test3.pdf   | 2         | 0.401                | 0.464                  | 825,392             | 2,310,617              | 2.799          |
| test3.pdf   | 3         | 0.498                | 0.602                  | 825,392            | 6,050,805              | 7.330          |
| test3.pdf   | 4         | 0.392                 | 0.459                   | 825,392                | 5,356,948                | 6.49019           |
| test3.pdf   | 5         | 0.334                 | 0.398                   | 825,392                | 4,519,634                | 5.47574           |

## Conclusion

- The compression and decompression times vary significantly across different files and numbers of bytes grouped together. Generally, larger files and smaller values of n result in longer compression and decompression times due to the increased amount of data being processed.

- Huffman is not optimal in most of the cases since it depends on the distribution of the bytes in the file and their frequency where the frequency distribution of symbols may vary significantly across different types of data, making it challenging to achieve optimal compression in all cases.

- The choice of the number of bytes grouped together (n) for compression significantly impacts compression time, compression ratio, and decompression time. so It's essential to strike a balance between compression efficiency and processing time based on specific requirements and constraints.


## How To Use

1. **Download the JAR File:** 
   - Download the `Huffman.jar` file from the repository.

2. **Run Compression:**
   - To compress a file, open a terminal or command prompt and navigate to the directory containing the `Huffman.jar` file.
   - Use the following command:
     ```
     java -jar Huffman.jar c absolute_path_to_input_file n
     ```
     - Replace `absolute_path_to_input_file` with the absolute path to the file you want to compress.
     - Replace `n` with the number of bytes to consider together during compression.

3. **Run Decompression:**
   - To decompress a file, open a terminal or command prompt and navigate to the directory containing the `Huffman.jar` file.
   - Use the following command:
     ```
     java -jar Huffman.jar d absolute_path_to_input_file
     ```
     - Replace `absolute_path_to_input_file` with the absolute path to the compressed file you want to decompress.

4. **View Compression and Decompression Statistics:**
Upon Compression and Decompression you can see different statistics about the  file to help you choose the best number of bytes grouped for compression `n` as compressed and decompressed times and compression ratio.

**Compression ratio is calculated as the ratio of the compressed file size to the original file size**   
By following these steps, you can effectively use Huffman compression and decompression for lossless data compression of arbitrary files, considering different values of `n` to optimize compression ratios and processing times.

**Thanks for reading! If you have any further questions feel free to let me know.**

