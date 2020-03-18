package src.Player;

import src.Common.Action;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.Draw.GraphicalDraw;
import src.Common.IObserver;
import src.Common.ISubject;
import src.Common.Tiles;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;
import java.util.Optional;

public class PlayerObserver implements IObserver {

    private GraphicalDraw drawer = new GraphicalDraw();

    @Override
    public void notify(ISubject caller) {
        RenderedImage image;
        TurnState state = TurnState.START;

        switch (state) {
            case START:
                Optional<Board> optionalBoard = caller.getBoard();
                Optional<List<Tiles>> optionalHand = caller.getHand();
                if (optionalBoard.isPresent() && optionalHand.isPresent()) {
                    image = this.renderStart(optionalBoard.get(), optionalHand.get());
                } else {
                    return;
                }
                break;
            case PLAY:
                Optional<Action> optionalAction = caller.getAction();
                if (optionalAction.isPresent()) {
                    image = this.renderAction(optionalAction.get());
                } else {
                    return;
                }
                break;
            default:
                return;
        }

        JDialog dialog = new JDialog();
        JLabel label = new JLabel(new ImageIcon((Image) image));
        dialog.add( label );
        dialog.pack();
        dialog.setVisible(true);
    }

    private RenderedImage renderStart(Board board, List<Tiles> hand) {
        int padding = 20;
        BufferedImage boardImg = (BufferedImage) drawer.drawBoard(board);
        int imageWidth = boardImg.getWidth() + (3 * drawer.getTileSize() + padding);
        int imageHeight = boardImg.getHeight() + (2 * padding);
        int tileLength = drawer.getTileSize();


        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, imageWidth, imageHeight);

        // place board on left side of board
        graphics.drawImage(boardImg, padding, padding, null);

        // Build hand on right side of board
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("Roboto", Font.PLAIN, 30));
        graphics.drawString("HAND", tileLength + boardImg.getWidth() + padding, tileLength * 2);

        // either the hand is three items or its two
        int y = tileLength * 2 + padding;
        for (Tiles t : hand) {
            RenderedImage tileImg = drawer.drawTile(t);
            graphics.drawImage((Image) tileImg, tileLength + boardImg.getWidth() + padding, y, null);
            y += tileLength + padding;
        }

        return image;
    }

    // ideally, post action board
    private RenderedImage renderAction(Action action) {
        int tileSize = drawer.getTileSize();

        if (action instanceof InitialAction) {
            InitialAction initialAction = (InitialAction) action;
            BufferedImage image = new BufferedImage(tileSize * 3, tileSize * 3, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();
            graphics.setColor(Color.WHITE);
            graphics.fillRect(0, 0, image.getWidth(), image.getHeight());

            RenderedImage tileImage = drawer.drawTile(initialAction.getTileToPlace().rotate(initialAction.getRotation()));
            graphics.drawImage((Image) tileImage, tileSize, tileSize, null);

            AvatarLocation avatarLocation = initialAction.getAvatarPlacement();
            String location = "(x, y) = " + avatarLocation.getPosn();
            graphics.setColor(Color.BLACK);
            graphics.setFont(new Font("Roboto", Font.PLAIN, 14));
            graphics.drawString(location, tileSize, tileSize * 5 / 2);
            graphics.drawString("Port = " + avatarLocation.getPort(), tileSize, tileSize * 5 / 2 + 20);

            return image;
        }
        else if (action instanceof TurnAction) {
            TurnAction turnAction = (TurnAction) action;
            return drawer.drawTile(turnAction.getTileToPlace().rotate(turnAction.getRotation()));
        }
        else {
            throw new IllegalArgumentException("Action given was not an initial or a turn action!");
        }
    }

}
