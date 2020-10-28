import client.databundle.DataBundle;
import client.databundle.DataSerializer;
import genericdatatype.Pair;

import java.io.*;

/**
 * Class used to serialize data to local files.
 */
public class LocalDataSerializer implements DataSerializer {
    private DataBundle dataBundle;
    private final String filePath = "DataBundle.ser";

    /**
     * If the .ser file exists, deserialize it.
     * Otherwise, create a new data bundle and serialize it.
     *
     * @throws IOException            thrown when (de)serialization fails
     * @throws ClassNotFoundException thrown when (de)serialization fails
     */
    public void setup() throws IOException, ClassNotFoundException {
        // if ser is nonexistent, then create it.
        if (new File(filePath).exists()) {
            deserialize();
        } else {
            dataBundle = new DataBundle();
            serialize();
        }
    }

    public Pair<DataBundle, DataSerializer> getStartingInfo() {
        return new Pair<>(dataBundle, this);
    }

    public void serialize() throws IOException {
        OutputStream file = new FileOutputStream(filePath);
        OutputStream buffer = new BufferedOutputStream(file);
        ObjectOutput output = new ObjectOutputStream(buffer);

        output.writeObject(dataBundle);
        output.close();
    }

    public void deserialize() throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream(filePath);
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        this.dataBundle = (DataBundle) input.readObject();
        input.close();
    }
}
