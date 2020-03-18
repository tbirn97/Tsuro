package src.Common.Draw;

import src.Admin.GameObserver;
import src.Admin.Referee;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.PlayerInterface;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Tiles;
import src.Player.Player;
import src.Player.SecondS;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphicalDraw implements IDraw {

    // board dependent values
    protected static Color boardBackground = new Color(59, 24, 12);
    protected static int boardBorderSize = 1;
    protected static int tokenDiameter = 10;

    // tile dependent values
    protected static int tileSideLength = 70;
    protected static int tilePathThickness = 5;
    protected static Color tileBackground = new Color(110, 57, 43);
    protected static Point tileControlPoint = new Point(tileSideLength / 2, tileSideLength / 2);
    protected static List<Color> tilePathColors = Arrays.asList(Color.red, Color.green, Color.blue, Color.orange);


    /**
     * Wrapper method for getting the x and y of a port.
     * @param port port number
     * @return Point on the tile
     */
    private static Point getPortPoint(int port) {
        return new Point(getPortXCoordinate(port), getPortYCoordinate(port));
    }

    /**
     * Gets the x coordinate of a port based on which port it is.
     *
     * @param port port number, a number from 1 to 8 inclusive
     * @return x coordinate value for a Graphics object to draw
     */
    private static int getPortXCoordinate(int port) {
        switch(port) {
            case 6:
            case 7:
                return 0;
            case 0:
            case 5:
                return (int) Math.ceil(tileSideLength * 2 / 5);
            case 1:
            case 4:
                return (int) Math.floor(tileSideLength * 3 / 5);
            case 2:
            case 3:
                return tileSideLength;
            default:
                return -1;
        }
    }

    /**
     * Gets the y coordinate of a port based on which port it is.
     *
     * @param port port number, a number from 1 to 8 inclusive
     * @return y coordinate value for a Graphics object to draw
     */
    private static int getPortYCoordinate(int port) {
        switch(port) {
            case 0:
            case 1:
                return 0;
            case 2:
            case 7:
                return (int) Math.ceil(tileSideLength * 2 / 5);
            case 3:
            case 6:
                return (int) Math.floor(tileSideLength * 3 / 5);
            case 4:
            case 5:
                return tileSideLength;
            default:
                return -1;
        }

    }

    @Override
    public RenderedImage drawTile(Tiles tile) {
        BufferedImage image = new BufferedImage(tileSideLength, tileSideLength, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(tileBackground);
        graphics.fillRect(0, 0, tileSideLength, tileSideLength);

        graphics.setStroke(new BasicStroke(tilePathThickness));
        for (Port port : Port.values()) {
            graphics.setColor(tilePathColors.get(port.getValue() % tilePathColors.size()));
            int end = tile.getEndOfConnection(port).getValue();

            Path2D path = new Path2D.Double();
            path.moveTo(getPortPoint(port.getValue()).getX(), getPortPoint(port.getValue()).getY());
            path.quadTo(tileControlPoint.getX(), tileControlPoint.getY(), getPortPoint(end).x, getPortPoint(end).y);
            graphics.draw(path);
        }

        return image;
    }


    private static int getBoardSize(Board board) {
        return (tileSideLength + boardBorderSize) * board.getBoardSize();
    }

    private static Color getAvatarColor(AvatarColor c) {
        switch(c) {
            case red:
                return Color.RED;
            case white:
                return Color.WHITE;
            case blue:
                return Color.BLUE;
            case green:
                return Color.GREEN;
            case black:
                return Color.BLACK;
            default:
                throw new IllegalArgumentException("Update getAvatarColor to use all values of the enum AvatarColor.");
        }
    }

    @Override
    public RenderedImage drawBoard(Board board) {
        int imageSize = getBoardSize(board);

        BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(boardBackground);
        graphics.fillRect(0, 0, imageSize, imageSize);

        // src.Admin.Draw the tiles
        for (Posn p : board.getTiles().keySet()) {
            int x = p.getX() * (tileSideLength + boardBorderSize);
            int y = p.getY() * (tileSideLength + boardBorderSize);
            if (x == 0) {
                x = boardBorderSize;
            }
            if (y == 0) {
                y = boardBorderSize;
            }
            Tiles t = board.getTiles().get(p);
            BufferedImage tileImage = (BufferedImage) this.drawTile(t);
            graphics.drawImage(tileImage, x, y, boardBackground,null);
        }

        // src.Admin.Draw the avatars
        for (AvatarColor c : board.getAvatars().keySet()) {
            AvatarLocation l = board.getAvatars().get(c);

            // get the port location as if the tile is the image
            Point relPortLocation = getPortPoint(l.getPort().getValue());
            int absPortLocationX = relPortLocation.x + (l.getX() * (tileSideLength + boardBorderSize));
            int absPortLocationY = relPortLocation.y + (l.getY() * (tileSideLength + boardBorderSize));

            graphics.setColor(getAvatarColor(c));
            graphics.fillOval(absPortLocationX - (tokenDiameter / 2), absPortLocationY - (tokenDiameter / 2), tokenDiameter, tokenDiameter);
        }

        return image;
    }

    public int getTileSize() {
        return tileSideLength;
    }

    public void renderAsJPanel(RenderedImage img) {
        JDialog dialog = new JDialog();
        JLabel label = new JLabel(new ImageIcon((Image) img));
        dialog.add( label );
        dialog.pack();
        dialog.setVisible(true);
    }

    /**
     * STOP
     * This function only exists for testing graphical interfaces. Do not put worthwhile code in
     * here
     * @param args
     */
    public static void main(String[] args) {
        Referee ref = new Referee();
        ArrayList<PlayerInterface> playerInterfaces = new ArrayList<>();
        playerInterfaces.add(new Player("Player1", new SecondS()));
        playerInterfaces.add(new Player("Player2", new SecondS()));
        playerInterfaces.add(new Player("Player3", new SecondS()));
        playerInterfaces.add(new Player("Player4", new SecondS()));
        GameObserver observer = new GameObserver();
        ref.addObserver(observer);
        ref.startGame(playerInterfaces);

    }
}
