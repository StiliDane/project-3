import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HuffmanTreeTest {

    @Test
    void leavesOrderByFrequencyThenCharacter() {
        HuffmanLeaf lowFrequency = new HuffmanLeaf(2, 'z');
        HuffmanLeaf highFrequency = new HuffmanLeaf(3, 'a');
        HuffmanLeaf lowerCharacter = new HuffmanLeaf(2, 'a');
        HuffmanLeaf sameKey = new HuffmanLeaf(2, 'z');

        assertTrue(lowFrequency.comesBefore(highFrequency));
        assertFalse(highFrequency.comesBefore(lowFrequency));
        assertTrue(lowerCharacter.comesBefore(lowFrequency));
        assertFalse(lowFrequency.comesBefore(lowerCharacter));
        assertFalse(lowFrequency.comesBefore(sameKey));
    }

    @Test
    void nodesOrderByFrequencyThenCharacterAgainstLeavesAndNodes() {
        HuffmanNode lowFrequency = node(1, 2);
        HuffmanNode highFrequency = node(0, 3);
        HuffmanNode lowerCharacter = node(0, 2);
        HuffmanNode sameKey = node(1, 2);
        HuffmanLeaf higherFrequencyLeaf = new HuffmanLeaf(4, 'a');

        assertTrue(lowFrequency.comesBefore(highFrequency));
        assertFalse(highFrequency.comesBefore(lowFrequency));
        assertTrue(lowerCharacter.comesBefore(lowFrequency));
        assertFalse(lowFrequency.comesBefore(lowerCharacter));
        assertFalse(lowFrequency.comesBefore(sameKey));
        assertTrue(lowFrequency.comesBefore(higherFrequencyLeaf));
        assertFalse(higherFrequencyLeaf.comesBefore(lowFrequency));
    }

    @Test
    void leavesCombineWithLeavesAndNodesInPriorityOrder() {
        HuffmanLeaf earlierLeaf = new HuffmanLeaf(1, 'a');
        HuffmanLeaf laterLeaf = new HuffmanLeaf(2, 'b');
        HuffmanNode leafCombination = (HuffmanNode) earlierLeaf.combine(laterLeaf);

        assertEquals(3, leafCombination.freq());
        assertEquals('a', leafCombination.charAscii());
        assertSame(earlierLeaf, leafCombination.left());
        assertSame(laterLeaf, leafCombination.right());

        HuffmanLeaf earlierLeafForNode = new HuffmanLeaf(1, 'a');
        HuffmanNode laterNode = node('b', 2);
        HuffmanNode leafNodeCombination = (HuffmanNode) earlierLeafForNode.combine(laterNode);

        assertSame(earlierLeafForNode, leafNodeCombination.left());
        assertSame(laterNode, leafNodeCombination.right());

        HuffmanNode earlierNode = node('c', 1);
        HuffmanLeaf laterLeafForNode = new HuffmanLeaf(2, 'd');
        HuffmanNode nodeCombination = (HuffmanNode) laterLeafForNode.combine(earlierNode);

        assertEquals(3, nodeCombination.freq());
        assertEquals('c', nodeCombination.charAscii());
        assertSame(earlierNode, nodeCombination.left());
        assertSame(laterLeafForNode, nodeCombination.right());

    }

    @Test
    void nodesCombineWithLeavesAndNodesInPriorityOrder() {
        HuffmanNode earlierNode = node('a', 1);
        HuffmanNode laterNode = node('b', 2);
        HuffmanNode nodeCombination = (HuffmanNode) earlierNode.combine(laterNode);

        assertEquals(3, nodeCombination.freq());
        assertEquals('a', nodeCombination.charAscii());
        assertSame(earlierNode, nodeCombination.left());
        assertSame(laterNode, nodeCombination.right());

        HuffmanNode earlierNodeForReverseCombine = node('a', 1);
        HuffmanNode laterNodeForReverseCombine = node('b', 2);
        HuffmanNode reverseNodeCombination =
                (HuffmanNode) laterNodeForReverseCombine.combine(earlierNodeForReverseCombine);

        assertSame(earlierNodeForReverseCombine, reverseNodeCombination.left());
        assertSame(laterNodeForReverseCombine, reverseNodeCombination.right());

        HuffmanNode earlierNodeForLeaf = node('c', 1);
        HuffmanLeaf laterLeaf = new HuffmanLeaf(2, 'd');
        HuffmanNode nodeLeafCombination = (HuffmanNode) earlierNodeForLeaf.combine(laterLeaf);

        assertSame(earlierNodeForLeaf, nodeLeafCombination.left());
        assertSame(laterLeaf, nodeLeafCombination.right());

        HuffmanNode laterNodeForLeaf = node('d', 2);
        HuffmanLeaf earlierLeaf = new HuffmanLeaf(1, 'c');
        HuffmanNode leafCombination = (HuffmanNode) laterNodeForLeaf.combine(earlierLeaf);

        assertEquals(3, leafCombination.freq());
        assertEquals('c', leafCombination.charAscii());
        assertSame(earlierLeaf, leafCombination.left());
        assertSame(laterNodeForLeaf, leafCombination.right());
    }

    @Test
    void compareToReturnsNegativePositiveOrZeroForLeaves() {
        HuffmanLeaf lower = new HuffmanLeaf(1, 'a');
        HuffmanLeaf higher = new HuffmanLeaf(2, 'a');
        HuffmanLeaf sameKey = new HuffmanLeaf(1, 'a');

        assertEquals(-1, lower.compareTo(higher));
        assertEquals(1, higher.compareTo(lower));
        assertEquals(0, lower.compareTo(sameKey));
    }

    @Test
    void compareToReturnsNegativePositiveOrZeroForNodes() {
        HuffmanNode lower = node('a', 1);
        HuffmanNode higher = node('a', 2);
        HuffmanNode sameKey = node('a', 1);

        assertEquals(-1, lower.compareTo(higher));
        assertEquals(1, higher.compareTo(lower));
        assertEquals(0, lower.compareTo(sameKey));
    }

    @Test
    void unsupportedTreeImplementationIsHandled() {
        HuffmanTree unsupported = new HuffmanTree() {
            @Override
            public boolean comesBefore(HuffmanTree other) {
                return false;
            }

            @Override
            public HuffmanTree combine(HuffmanTree other) {
                return null;
            }

            @Override
            public int compareTo(HuffmanTree other) {
                return 0;
            }
        };
        HuffmanLeaf leaf = new HuffmanLeaf(1, 'a');
        HuffmanNode node = node('a', 1);

        assertFalse(leaf.comesBefore(unsupported));
        assertNull(leaf.combine(unsupported));
        assertEquals(0, leaf.compareTo(unsupported));
        assertFalse(node.comesBefore(unsupported));
        assertNull(node.combine(unsupported));
        assertEquals(0, node.compareTo(unsupported));
    }

    private static HuffmanNode node(int charAscii, int frequency) {
        return new HuffmanNode(charAscii, frequency, null, null);
    }
}
