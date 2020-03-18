package src.Admin;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to listen for left and right keyboard events to be used with the GameObserver.
 * It allows a humob to step forwards or backwards in time.
 */
public class DynamicGameObserver implements KeyListener {

    private List<JLabel> sequentialImageFrames;
    private int imageFrame;
    private JFrame frame;
    private boolean waitingForNewLabel;

    public DynamicGameObserver(JFrame frame) {
        this.sequentialImageFrames = new ArrayList<>();
        this.imageFrame = 0;
        this.waitingForNewLabel = true;
        this.frame = frame;
    }

    /**
     * Unused.
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Listening for if the left or right keys are pressed. If we want to go back in time, we check
     * if we are at the beginning of the list and if not we show the previous frame. If we want to
     * go forwards in time we check if we are at the end of the list and if not we show the next
     * frame.
     *
     * @param e The keyboard event.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                if(sequentialImageFrames.size()-1 != imageFrame) {
                    imageFrame++;
                    loadLabelIntoFrame(sequentialImageFrames.get(imageFrame));
                }
                break;
            case KeyEvent.VK_LEFT:
                if(imageFrame > 0) {
                    imageFrame--;
                    loadLabelIntoFrame(sequentialImageFrames.get(imageFrame));
                }
                break;
            default:
                break;
        }
    }

    /**
     * Unused.
     */
    @Override
    public void keyReleased(KeyEvent e) {}


    /**
     * Add a label to the ArrayList of labels we keep track of. This allows us to move forwards and
     * backwards in time. If it is the first label in the list, we want to show that label in the
     * frame.
     * @param label The label to append to the list we keep track of.
     */
    public void addLabel(JLabel label) {
        this.sequentialImageFrames.add(label);
        if(sequentialImageFrames.size() == 1) {
            loadLabelIntoFrame(sequentialImageFrames.get(0));
        }
    }

    /**
     * This function given a label loads that label into the frame. This allows for the frame to be
     * changed during runtime.
     * @param label The new label to show in the frame.
     */
    private void loadLabelIntoFrame(JLabel label) {
        frame.setContentPane( label );
        frame.pack();
        frame.setVisible(true);
    }
}
