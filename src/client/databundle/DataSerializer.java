package client.databundle;

import genericdatatype.Pair;

import java.io.IOException;

/**
 * An interface for other parts of code such that they can deserialize, make changes and serialize the DataBundle.
 */
public interface DataSerializer {
    void serialize() throws IOException;

    void deserialize() throws IOException, ClassNotFoundException;

    Pair<DataBundle, DataSerializer> getStartingInfo();
}
