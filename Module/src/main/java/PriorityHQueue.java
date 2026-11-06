import java.util.Arrays;

public class PriorityHQueue implements HQueue {

    private HuffmanTree[] data;
    private int size;

    public PriorityHQueue(){
        data = new HuffmanTree[10];
        size = 0;

    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int count() {
        return size;
    }

    @Override
    public void enqueue(HuffmanTree tree) {
        if (size == data.length)
            resize();

        data[size] = tree;
        int index = size;
        size++;

        // Bubble up till reached the proper spot
        while (index > 0){
            int parentIndex = (index - 1) / 2;
            if (data[index].comesBefore(data[parentIndex])){
                // swap the parent and the child
                HuffmanTree temp = data[parentIndex];
                data[parentIndex] = data[index];
                data[index] = temp;

                // update the index
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    @Override
    public HuffmanTree dequeue() {
        if (isEmpty())
            return null;

        HuffmanTree output = data[0];

        data[0] = data[size - 1];
        data[size - 1] = null;
        size--;

        // Sink down till reached the proper spot
        int index = 0;
        while ((2 * index) + 1 < size){
            int leftChildIndex = (2 * index) + 1;
            int rightChildIndex = (2 * index) + 2;
            int priorityChildIndex;
            priorityChildIndex = rightChildIndex >= size ||
                    data[leftChildIndex].comesBefore(data[rightChildIndex])
                    ? leftChildIndex : rightChildIndex;

            if (data[priorityChildIndex].comesBefore(data[index])){
                // swap the parent and the child
                HuffmanTree temp = data[index];
                data[index] = data[priorityChildIndex];
                data[priorityChildIndex] = temp;

                // update the index
                index = priorityChildIndex;
            } else {
                break;
            }
        }

        return output;
    }

    /** Doubles the size of the array without changing the data */
    private void resize(){
        data = Arrays.copyOf(data, data.length * 2);
    }
}
