import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Starter test suite for Project 3 (Huffman Encoding/Decoding).
 * Write more tests!
 * These tests assume the test files are in the same directory as HuffmanTest.java
 */
public class HuffmanTest {

    @Test
    public void testCntFreq() throws IOException {
        int[] freqList = Huffman.cntFreq("src/test/java/file2.txt");
        int[] ansList = {2, 4, 8, 16, 0, 2, 0}; // counts for ascii 97..103
        for (int i = 0; i < ansList.length; i++) {
            assertEquals(ansList[i], freqList[97 + i], "mismatch at ascii " + (97 + i));
        }
    }

    @Test
    public void testComesBefore() {
        HuffmanLeaf d = new HuffmanLeaf(5, 'd');
        HuffmanLeaf k = new HuffmanLeaf(5, 'k');
        assertTrue(d.comesBefore(k));
        assertFalse(k.comesBefore(d));
    }

    @Test
    public void testCreateHeader() throws IOException {
        int[] freqList = Huffman.cntFreq("src/test/java/file2.txt");
        assertEquals("97 2 98 4 99 8 100 16 102 2", Huffman.createHeader(freqList));
    }

    @Test
    public void testCreateHuffTreeEmptyAndSingleCharacterCases() {
        int[] emptyFrequencies = new int[Huffman.NUM_CHARS];
        assertNull(Huffman.createHuffTree(emptyFrequencies));

        int[] singleCharacterFrequency = new int[Huffman.NUM_CHARS];
        singleCharacterFrequency['x'] = 4;
        HuffmanNode singleCharacterTree = Huffman.createHuffTree(singleCharacterFrequency);

        assertNotNull(singleCharacterTree);
        assertEquals('x', singleCharacterTree.charAscii());
        assertEquals(4, singleCharacterTree.freq());
        assertNull(singleCharacterTree.left());
        assertNull(singleCharacterTree.right());
    }

    @Test
    public void testCreateCodeForNullAndExistingMultilineTree() throws IOException {
        String[] emptyCodes = Huffman.createCode(null);
        assertEquals(Huffman.NUM_CHARS, emptyCodes.length);
        for (String code : emptyCodes) {
            assertEquals("", code);
        }

        int[] frequencies = Huffman.cntFreq("src/test/java/multiline.txt");
        HuffmanNode tree = Huffman.createHuffTree(frequencies);
        String[] codes = Huffman.createCode(tree);

        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                assertFalse(codes[i].isEmpty(), "missing code for ASCII " + i);
            } else {
                assertEquals("", codes[i], "unexpected code for ASCII " + i);
            }
        }
    }

    @Test
    public void testCreateHeaderForEmptyFrequencies() {
        assertEquals("", Huffman.createHeader(new int[Huffman.NUM_CHARS]));
    }

    @Test
    public void testParseHeaderEmptyAndFromEncodedFixture() throws IOException {
        assertArrayEquals(new int[Huffman.NUM_CHARS], Huffman.parseHeader(null));
        assertArrayEquals(new int[Huffman.NUM_CHARS], Huffman.parseHeader("  \t"));

        String encodedHeader = Files.readAllLines(Path.of("src/test/java/file2_soln.txt")).getFirst();
        assertArrayEquals(Huffman.cntFreq("src/test/java/file2.txt"), Huffman.parseHeader(encodedHeader));
    }

    @Test
    public void testEncodeAndDecodeFile2Fixture() throws IOException {
        Huffman.huffmanEncode("src/test/java/file2.txt", "src/test/java/file1_out.txt");
        assertTrue(compareOutput("src/test/java/file2_soln.txt"));

        Huffman.huffmanDecode("src/test/java/file2_soln.txt", "src/test/java/file1_out.txt");
        assertTrue(compareOutput("src/test/java/file2.txt"));
    }

    @Test
    public void testEncodeAndDecodeSingleCharacterFile3() throws IOException {
        String inputFile = "src/test/java/file3.txt";
        String encodedFile = "src/test/java/file1_out.txt";
        int frequency = Huffman.cntFreq(inputFile)['a'];

        Huffman.huffmanEncode(inputFile, encodedFile);
        assertEquals("97 " + frequency, Files.readString(Path.of(encodedFile)));

        Huffman.huffmanDecode(encodedFile, encodedFile);
        assertTrue(compareOutput(inputFile));
    }

    @Test
    public void testEncodeAndDecodeMultilineFixture() throws IOException {
        Huffman.huffmanEncode("src/test/java/multiline.txt", "src/test/java/file1_out.txt");
        String encodedHeader = Files.readAllLines(Path.of("src/test/java/file1_out.txt")).getFirst();
        assertEquals(Huffman.createHeader(Huffman.cntFreq("src/test/java/multiline.txt")), encodedHeader);

        Huffman.huffmanDecode("src/test/java/file1_out.txt", "src/test/java/file1_out.txt");
        assertTrue(compareOutput("src/test/java/multiline.txt"));

        Huffman.huffmanDecode("src/test/java/multiline_soln.txt", "src/test/java/file1_out.txt");
        assertTrue(compareOutput("src/test/java/multiline.txt"));
    }

    @Test
    public void test01Textfile() throws IOException {
        Huffman.huffmanEncode("src/test/java/file1.txt", "src/test/java/file1_out.txt");
        assertTrue(compareOutput("src/test/java/file1_soln.txt"));
    }

    /**
     * Compares two files line by line, trimming trailing CR/LF so the
     * comparison works the same on Windows and Unix-style line endings.
     */
    private static boolean compareOutput(String expectedFile) throws IOException {
        try (BufferedReader r1 = new BufferedReader(new FileReader("src/test/java/file1_out.txt"));
             BufferedReader r2 = new BufferedReader(new FileReader(expectedFile))) {
            String line1, line2;
            while (true) {
                line1 = r1.readLine();
                line2 = r2.readLine();
                if (line1 == null && line2 == null) {
                    return true;
                }
                if (line1 == null || line2 == null) {
                    return false;
                }
                if (!line1.trim().equals(line2.trim())) {
                    return false;
                }
            }
        }
    }
}
