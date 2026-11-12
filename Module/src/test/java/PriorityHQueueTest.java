import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PriorityHQueueTest {

    @Test
    void tracksEmptyAndNonEmptyState() {
        PriorityHQueue queue = new PriorityHQueue();

        assertTrue(queue.isEmpty());
        assertEquals(0, queue.count());

        queue.enqueue(new HuffmanLeaf(4, 'd'));

        assertFalse(queue.isEmpty());
        assertEquals(1, queue.count());
        assertNull(new PriorityHQueue().dequeue());
    }

    @Test
    void keepsEnqueuedTreesInPriorityOrder() {
        PriorityHQueue queue = new PriorityHQueue();
        HuffmanLeaf first = new HuffmanLeaf(1, 'a');
        HuffmanLeaf second = new HuffmanLeaf(2, 'b');
        HuffmanLeaf third = new HuffmanLeaf(3, 'c');

        queue.enqueue(first);
        queue.enqueue(second);
        queue.enqueue(third);

        assertSame(first, queue.dequeue());
        assertSame(second, queue.dequeue());
        assertSame(third, queue.dequeue());
    }

    @Test
    void bubblesUpAndResizesWhenCapacityIsReached() {
        PriorityHQueue queue = new PriorityHQueue();
        HuffmanLeaf[] leaves = new HuffmanLeaf[11];

        for (int i = 0; i < leaves.length; i++) {
            leaves[i] = new HuffmanLeaf(leaves.length - i, (char) ('a' + i));
            queue.enqueue(leaves[i]);
        }

        assertEquals(leaves.length, queue.count());
        for (int i = 0; i < leaves.length; i++) {
            assertSame(leaves[leaves.length - i - 1], queue.dequeue());
        }
    }

    @Test
    void returnsTheOnlyTreeAndThenBecomesEmpty() {
        PriorityHQueue queue = new PriorityHQueue();
        HuffmanLeaf only = new HuffmanLeaf(7, 'g');
        queue.enqueue(only);

        assertSame(only, queue.dequeue());
        assertNull(queue.dequeue());
    }

    @Test
    void sinksUsingTheOnlyLeftChild() {
        PriorityHQueue queue = queueWithFrequencies(1, 2, 3);

        assertEquals(new HuffmanLeaf(1, 'a'), queue.dequeue());
        assertEquals(new HuffmanLeaf(2, 'b'), queue.dequeue());
    }

    @Test
    void selectsTheEarlierOfTwoChildrenAndSinksMultipleLevels() {
        PriorityHQueue queue = queueWithFrequencies(1, 2, 3, 4, 5, 6, 7);

        assertEquals(new HuffmanLeaf(1, 'a'), queue.dequeue());
        assertEquals(new HuffmanLeaf(2, 'b'), queue.dequeue());
        assertEquals(new HuffmanLeaf(3, 'c'), queue.dequeue());
    }

    @Test
    void selectsTheRightChildWhenItHasHigherPriority() {
        PriorityHQueue queue = queueWithFrequencies(1, 3, 2, 4);

        assertEquals(new HuffmanLeaf(1, 'a'), queue.dequeue());
        assertEquals(new HuffmanLeaf(2, 'c'), queue.dequeue());
    }

    @Test
    void doesNotSwapWhenParentAndChildHaveEqualPriority() {
        PriorityHQueue queue = new PriorityHQueue();
        HuffmanLeaf samePriority = new HuffmanLeaf(1, 'a');
        queue.enqueue(samePriority);
        queue.enqueue(new HuffmanLeaf(1, 'a'));
        queue.enqueue(new HuffmanLeaf(1, 'a'));

        assertSame(samePriority, queue.dequeue());
    }

    private static PriorityHQueue queueWithFrequencies(int... frequencies) {
        PriorityHQueue queue = new PriorityHQueue();
        for (int i = 0; i < frequencies.length; i++) {
            queue.enqueue(new HuffmanLeaf(frequencies[i], (char) ('a' + i)));
        }
        return queue;
    }
}
