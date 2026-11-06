/**
 * A single node in a Huffman tree, implemented as a record.
 *
 * A HuffmanNode represents either a leaf (an actual character together
 * with its occurrence count) or an internal node created by combining two
 * subtrees during tree construction.
 *
 * Records are immutable and give you charAscii(), freq(), left(), and
 * right() accessor methods automatically, along with
 * equals()/hashCode()/toString(). Do not rename the components below.
 */
public record HuffmanNode(int charAscii, int freq, HuffmanTree left, HuffmanTree right)
        implements HuffmanTree{

    /**
     * Returns true if this object comes before the other object
     * If other is not a HuffmanNode or a HuffmanLeaf the method returns null
     */
     public boolean comesBefore(HuffmanTree other) {
         int otherFreq;
         int otherChar;

         if (other instanceof HuffmanNode(int ascii, int freq1, _, _)) {
             otherFreq = freq1;
             otherChar = ascii;
         } else if (other instanceof HuffmanLeaf(int freq1, char ascii)) {
             otherFreq = freq1;
             otherChar = ascii;
         } else {
             return false;
         }

         if (this.freq != otherFreq) {
             return this.freq < otherFreq;
         }
         return this.charAscii < otherChar;
    }

    /**
     * Combines a HuffmanTree with either another HuffmanTree or a HuffmanLeaf
     * The higher priority of the two goes on the left and the other on the right
     * The frequency is the combined frequency of the previous two Trees
     * The charAscii is the minimum of the previous two Trees
     */
    public HuffmanTree combine(HuffmanTree other) {
        int finalFreq;
        int finalChar;
        HuffmanTree newLeft;
        HuffmanTree newRight;

        // Properly gathers the new character and frequency of the new node
        if (other instanceof HuffmanNode(int ascii, int freq1, _, _)) {
            finalFreq = this.freq + freq1;
            finalChar = Math.min(this.charAscii, ascii);
        } else if (other instanceof HuffmanLeaf(int freq1, char ascii)) {
            finalFreq = this.freq + freq1;
            finalChar = Math.min(this.charAscii, ascii);
        } else {
            return null;
        }

        // Properly gathers the left and right branches of the new node
        if (this.comesBefore(other)){
            newLeft = this;
            newRight = other;
        } else {
            newLeft = other;
            newRight = this;
        }

        return new HuffmanNode(finalChar, finalFreq, newLeft, newRight);
    }

    /**
     * Natural ordering consistent with comesBefore(), so that HuffmanNode
     * objects can be stored directly in a sorted structure (e.g. a
     * PriorityQueue or a sorted List) while building the tree.
     */
    @Override
    public int compareTo(HuffmanTree other) {
        if (this.comesBefore(other)) return -1;
        if (other.comesBefore(this)) return 1;
        return 0;
    }
}
