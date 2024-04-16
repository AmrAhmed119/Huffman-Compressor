package Util;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Represents a Huffman tree used for encoding and decoding data.
 */
public class HuffmanTree {

    private final Map<ByteWrapper, Integer> frequencyTable;
    private final Map<ByteWrapper, String> codeWords;
    private HuffmanNode root;

    /**
     * Constructor to initialize the Huffman tree with a frequency table.
     *
     * @param frequencyTable The frequency table of the data to be encoded.
     */
    public HuffmanTree(Map<ByteWrapper, Integer> frequencyTable) {
        this.frequencyTable = frequencyTable;
        codeWords = new HashMap<>();
    }

    /**
     * Builds the Huffman tree using the frequency table.
     */
    public void buildTree() {

        // create a priority queue to store the nodes of the Huffman tree
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        // populate the priority queue with the nodes of the Huffman tree
        for (Map.Entry<ByteWrapper, Integer> entry : frequencyTable.entrySet()) {
            HuffmanNode huffmanNode = new HuffmanNode(entry.getKey(), entry.getValue());
            priorityQueue.add(huffmanNode);
        }

        // build the Huffman tree
        while (priorityQueue.size() > 1) {

            // pick the two nodes with the lowest frequency
            HuffmanNode leftChild = priorityQueue.poll();
            HuffmanNode rightChild = priorityQueue.poll();

            // create a parent node for the two nodes
            HuffmanNode parent = new HuffmanNode(leftChild, rightChild);
            priorityQueue.add(parent);
        }

        // get the root of the Huffman tree
        this.root = priorityQueue.poll();
    }

    /**
     * Traverses the Huffman tree to build the code words for each byte.
     *
     * @param node     The current node in the traversal.
     * @param codeWord The code word for the current node.
     */
    private void traverseTree(HuffmanNode node, String codeWord) {
        // if we reach a leaf node, add the code word to the map
        if (node.isLeaf()) {
            codeWords.put(node.data, codeWord);
        } else {
            // add a 0 to the code word if we go left and a 1 if we go right
            traverseTree(node.leftChild, codeWord + "0");
            traverseTree(node.rightChild, codeWord + "1");
        }
    }

    /**
     * Builds the code words for each byte in the Huffman tree.
     *
     * @return The map of code words for each byte.
     */
    public Map<ByteWrapper, String> buildCodeWords() {
        if(root.isLeaf()) {
            codeWords.put(root.data, "0");
            return codeWords;
        }
        traverseTree(root, "");
        return codeWords;
    }

}