package Util;

/**
 * Represents a node in a Huffman tree used for encoding and decoding data.
 */
public class HuffmanNode implements Comparable<HuffmanNode> {

    public ByteWrapper data;
    public final int frequency;
    public HuffmanNode leftChild;
    public HuffmanNode rightChild;

    /**
     * Constructor for a leaf node in the Huffman tree.
     *
     * @param data      The data stored in the node.
     * @param frequency The frequency of the data.
     */
    public HuffmanNode(ByteWrapper data, int frequency) {
        this.data = data;
        this.frequency = frequency;
    }

    /**
     * Constructor for an internal node in the Huffman tree.
     *
     * @param leftChild  The left child of the node.
     * @param rightChild The right child of the node.
     */
    public HuffmanNode(HuffmanNode leftChild, HuffmanNode rightChild) {
        this.leftChild = leftChild;
        this.rightChild = rightChild;
        this.frequency = leftChild.frequency + rightChild.frequency;
    }

    /**
     * Checks if the node is a leaf node.
     *
     * @return True if the node is a leaf node, false otherwise.
     */
    public boolean isLeaf() {
        return leftChild == null && rightChild == null;
    }

    /**
     * Compares the frequency of this node with another node.
     *
     * @param other The other node to compare with.
     * @return The difference between the frequencies of the two nodes.
     */
    @Override
    public int compareTo(HuffmanNode other) {
        return this.frequency - other.frequency;
    }
}
