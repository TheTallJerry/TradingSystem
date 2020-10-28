package client.guiandpresenter;

import client.databundle.DataSerializer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

/**
 * A gui class <code>Screen</code>, all other screens extend from this class
 */
public abstract class Screen {
    protected final JFrame frame;
    protected final DataSerializer serializer;
    protected final JMenuBar menuBar;
    protected final JMenuItem returnItem;
    private final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    protected final Dimension btnDim;
    protected final Font fontPrompt;
    protected final Font fontInput;
    protected final int stringTextFieldSize, intTextFieldSize;
    protected boolean shouldSerialize = true;
    protected final int
            logWidth = SCREEN_DIMENSION.width / 3,
            logHeight = SCREEN_DIMENSION.height / 3,
            logX = SCREEN_DIMENSION.width / 2 - logWidth / 2,
            logY = SCREEN_DIMENSION.height / 2 - logHeight / 2;

    /**
     * Setting up return to previous page and other things that will be used for all screens
     *
     * @param serializer the serializer that will be used for initialize
     */
    protected Screen(DataSerializer serializer) {
        this.frame = new JFrame();
        this.serializer = serializer;

        menuBar = new JMenuBar();
        returnItem = new JMenuItem("Return to previous page");

        fontPrompt = new Font("Serif", Font.BOLD, SCREEN_DIMENSION.height / 55);
        fontInput = new Font("Serif", Font.PLAIN, SCREEN_DIMENSION.height / 75);

        stringTextFieldSize = SCREEN_DIMENSION.height / 100;
        intTextFieldSize = SCREEN_DIMENSION.height / 300;

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(SCREEN_DIMENSION.width / 2, SCREEN_DIMENSION.height / 2));
        frame.setResizable(true);


        returnItem.setFont(new Font("Arial", Font.BOLD, SCREEN_DIMENSION.height / 50));
        menuBar.add(returnItem);
        frame.setJMenuBar(menuBar);

        /* JFrame uses BorderLayout by default:
         * https://docs.oracle.com/javase/tutorial/uiswing/layout/howLayoutWorks.html
         */
        frame.setLayout(new GridBagLayout());
        frame.addWindowListener(new WindowAdapter() {

            /* When screen is closed by a call to screen.dispose() */
            @Override
            public void windowClosed(WindowEvent e) {
                try {
                    if (shouldSerialize)
                        serializer.serialize();
                } catch (IOException | RuntimeException e1) {
                    e1.printStackTrace();
                }
            }

            /* When the screen is closed by the default "X" on top right */
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    if (shouldSerialize)
                        serializer.serialize();
                } catch (IOException | RuntimeException e1) {
                    e1.printStackTrace();
                }
            }
        });
        final int BUTTON_WIDTH = SCREEN_DIMENSION.width / 6, BUTTON_HEIGHT = SCREEN_DIMENSION.height / 15;
        btnDim = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
    }

    /**
     * Set up a standard GridBagConstraints for all the screen to use.
     *
     * @return a GridBagConstraints for all the screen
     */
    protected GridBagConstraints setupConstraint() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 0.1;
        c.weighty = 0.1;
        c.gridx = 0;
        c.gridy = 0;
        return c;
    }
}
