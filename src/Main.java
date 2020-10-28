import client.guiandpresenter.mainscreen.MainScreen;

import java.io.IOException;

/**
 * Main class where the system runs.
 */
class Main {
    /**
     * Main method of the program.
     * Creates an instance of MainProgram, and call its setup() and run()
     *
     * @param args default parameter
     * @throws IOException            thrown when (de)serialization fails
     * @throws ClassNotFoundException thrown when (de)serialization fails
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LocalDataSerializer localDataSerializer = new LocalDataSerializer();
        localDataSerializer.setup();
        new MainScreen(localDataSerializer.getStartingInfo().value1, localDataSerializer.getStartingInfo().value2);
    }
}
