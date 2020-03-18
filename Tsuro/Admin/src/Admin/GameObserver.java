package src.Admin;

import src.Common.Action;
import src.Common.AvatarColor;
import src.Common.Board;
import src.Common.Draw.GraphicalDraw;
import src.Common.IObserver;
import src.Common.ISubject;
import src.Common.Tiles;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;
import java.util.Optional;

/**
 * This uses the Observer Pattern.
 */
public class GameObserver implements IObserver {

    private final int padding = 20;
    private GraphicalDraw drawer ;
    private JFrame frame;
    private DynamicGameObserver dynamicGameObserver;

    public GameObserver() {
        this.drawer = new GraphicalDraw();
        this.frame = new JFrame();
        this.dynamicGameObserver = new DynamicGameObserver(this.frame);
        this.frame.addKeyListener(this.dynamicGameObserver);
    }

    /**
     * Method that observed objects call to let this IObserver know that something of interest
     * happened. It is able to determine based on what data it was able to obtain, wether or not it
     * is being notified to draw a final board state or an current board state.
     *
     * @param caller the object that called this method
     */
    @Override
    public void notify(ISubject caller) {

        Optional<Board> board = caller.getBoard();
        Optional<List<Tiles>> hand = caller.getHand();
        Optional<Action> action = caller.getAction();
        Optional<CompetitionResult> result = caller.getResult();

        RenderedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        if(result.isPresent() && board.isPresent()) {
            image = this.renderFinalBoardState(board.get(), result.get());
        } else if(board.isPresent() && hand.isPresent() && action.isPresent()) {
            image = this.renderCurrentBoardState(board.get(), hand.get(), action.get().getPlayerAvatar());
        }

        JLabel label = new JLabel(new ImageIcon((Image) image));
        dynamicGameObserver.addLabel(label);
    }

    /**
     * Render the final board state. This includes the final scores, and the last state of the board
     * @param board The last state of the board
     * @param finalResult the final scores
     * @return
     */
    private RenderedImage renderFinalBoardState(Board board, CompetitionResult finalResult) {
        BufferedImage boardImg = this.drawBoard(board);

        //render image
        BufferedImage image = new BufferedImage(getOverallWidthFromBoardImg(boardImg),
                getOverallHeightFromBoardImg(boardImg), BufferedImage.TYPE_INT_RGB);

        //render graphics
        Graphics2D graphics = this.renderGraphics(boardImg, image,
                getOverallHeightFromBoardImg(boardImg), getOverallWidthFromBoardImg(boardImg));

        // place board on left side of board
        this.renderRightSideFinal(graphics, boardImg, finalResult);
        return image;
    }

    /**
     * Render the current board state. This includes the given hand, the player color whos turn
     * it is and the board.
     * @param board the given board state
     * @param hand the player's hand
     * @param playerColor the color of the player who's turn it is
     * @return
     */
    private RenderedImage renderCurrentBoardState(Board board, List<Tiles> hand, AvatarColor playerColor) {
        BufferedImage boardImg = this.drawBoard(board);

        //render image
        BufferedImage image = new BufferedImage(getOverallWidthFromBoardImg(boardImg),
                getOverallHeightFromBoardImg(boardImg), BufferedImage.TYPE_INT_RGB);

        //render graphics
        Graphics2D graphics = this.renderGraphics(boardImg, image,
                getOverallHeightFromBoardImg(boardImg), getOverallWidthFromBoardImg(boardImg));

        // Build hand on right side of board
        this.renderRightSideForHand(graphics, boardImg, playerColor, hand);

        return image;
    }

    /**
     * Given a board, draw the board.
     * @param board the given board
     * @return
     */
    private BufferedImage drawBoard(Board board) {
        return (BufferedImage) drawer.drawBoard(board);
    }

    /**
     * Create a graphics object to be used in the creation of the image.
     * @param boardImg the board image to place
     * @param image the overall image to place the board image onto
     * @param imageHeight the height of the frame
     * @param imageWidth the width of the frame
     * @return
     */
    private Graphics2D renderGraphics(BufferedImage boardImg, BufferedImage image,
                                int imageHeight, int imageWidth) {
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);

        // place board on left side of board
        graphics.drawImage(boardImg, padding, padding, null);
        return graphics;
    }

    /**
     * Create the right side of the board if we want to show the hand of a player
     * @param graphics the graphics object used to draw the right side
     * @param boardImg this is used to get the proper dimensions for the right side
     * @param playerColor the color of the player who's turn it is
     * @param hand the hand of the layer who's turn it is
     */
    private void renderRightSideForHand(Graphics graphics, BufferedImage boardImg,
                                        AvatarColor playerColor, List<Tiles> hand) {
        int tileLength = drawer.getTileSize();

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Roboto", Font.PLAIN, 30));
        int turnPadding = tileLength * 2;
        graphics.drawString("TURN", tileLength + boardImg.getWidth() + padding, turnPadding);
        int playerColorPadding = turnPadding + (padding*2);
        graphics.drawString(playerColor.name(), tileLength + boardImg.getWidth() + padding,
                playerColorPadding);
        int handPadding = playerColorPadding + (padding*4);
        graphics.drawString("HAND", tileLength + boardImg.getWidth() + padding, handPadding);

        // either the hand is three items or its two
        int y = handPadding + padding;
        for (Tiles t : hand) {
            RenderedImage tileImg = drawer.drawTile(t);
            graphics.drawImage((Image) tileImg, tileLength + boardImg.getWidth() + padding, y, null);
            y += tileLength + padding;
        }
    }

    /**
     * Create the right side of the board if the board is over, which would show the final scores
     * @param graphics the graphics object used to draw the right side
     * @param boardImg this is used to get the proper dimensions for the right side
     * @param finalResult the final standings after a game
     */
    private void renderRightSideFinal(Graphics graphics, BufferedImage boardImg, CompetitionResult finalResult) {
        int tileLength = drawer.getTileSize();
        graphics.drawImage(boardImg, padding, padding, null);

        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Roboto", Font.PLAIN, 30));
        int turnPadding = tileLength * 2;
        graphics.drawString("Finishers", tileLength + boardImg.getWidth() + padding, turnPadding);
        int finalScoreOverallPadding = padding + 10;
        for(int i = 0; i < finalResult.getRankings().size(); ++i) {
            int rankPadding = turnPadding + ((i+1) * finalScoreOverallPadding);
            graphics.drawString((i+1)+": ", boardImg.getWidth() + padding, rankPadding);
            for(int y = 0; y < finalResult.getRankings().get(i).size(); ++y) {
                int playerPadding = rankPadding + (y * finalScoreOverallPadding);
                graphics.drawString(finalResult.getRankings().get(i).get(y).getAvatar().name(),
                        tileLength + boardImg.getWidth(), playerPadding);
            }
        }
    }

    /**
     * Given a board image, get the overall width of what the image should be.
     * @param boardImg the board image to base the width off of
     * @return
     */
    private int getOverallWidthFromBoardImg(BufferedImage boardImg) {
        return boardImg.getWidth() + (3 * drawer.getTileSize() + padding);
    }

    /**
     * Given a board image, get the overall height of what the image should be.
     * @param boardImg the board image to base the height off of
     * @return
     */
    private int getOverallHeightFromBoardImg(BufferedImage boardImg) {
        return boardImg.getHeight() + (2 * padding);
    }
}
