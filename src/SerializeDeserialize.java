import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class SerializeDeserialize {
    private static final String delimiter = ",";
    private static final String leaf = "L";

    public static void main(String[] args) throws IOException {
        BinaryTree root = new BinaryTree(2);
        root.left = new BinaryTree(3);
        root.right = new BinaryTree(8);
        root.left.left = new BinaryTree(0);
        root.left.right = new BinaryTree(8);
        root.right.right = new BinaryTree(7);

        // Test Case 1 = When tree is well formed
        final File serializedFile = serialize(root);
        final BinaryTree deserializedTree = deserialize(serializedFile);

        // Test Case 2 = When Tree is null
//        final File serializedFile1 = serialize(null);
//        final BinaryTree deserializedTree1 = deserialize(serializedFile);
    }

    // Encodes a tree to a single string.
    public static File serialize(BinaryTree root) throws IOException {
        if(Objects.isNull(root)) return null;
        File folder = new File("test");
        folder.mkdir(); // create a folder in your current work space
        File file = new File(folder, "testingSerialize.txt"); // put the file inside the folder
        file.createNewFile();// create the file
        FileWriter writer = new FileWriter(file);
        preOrder(root, writer);
        writer.close();
        return file;
    }

    //Traverse tree in preOrder depth first search
    private static void preOrder(BinaryTree root, FileWriter writer) throws IOException {
        if (root == null) {
            //when node is null, mark it as leaf
            writer.append(leaf).append(delimiter);
        } else {
            //Write root node value to disk with a separator
            writer.append(String.valueOf(root.value)).append(delimiter);
            preOrder(root.left, writer);
            preOrder(root.right, writer);
        }
    }

    // Decodes encoded data to tree.
    public static BinaryTree deserialize(File file) throws IOException {
        if(Objects.isNull(file)) return null;
        // Read serialized data from the disk
        final String data = new String(Files.readAllBytes(Paths.get(file.getPath())));
        // Add data to queue backed by linkedList
        Queue<String> serializedData = new LinkedList<>(Arrays.asList(data.split(delimiter)));
        return deserialize(serializedData);
    }

    private static BinaryTree deserialize(Queue<String> serializedData) {
        //Keep polling queue recursively
        String value = serializedData.remove();
        if (value.equals(leaf)) return null;
        else {
            //Since the serialization was preOrder, we build tree in preOrder mode
            BinaryTree root = new BinaryTree(Integer.parseInt(value));
            root.left = deserialize(serializedData);
            root.right = deserialize(serializedData);
            return root;
        }
    }

    static class BinaryTree {
        int value;
        BinaryTree left;
        BinaryTree right;

        BinaryTree(int value) {
            this.value = value;
        }
    }
}
