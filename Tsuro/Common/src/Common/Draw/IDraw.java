package src.Common.Draw;

import src.Common.Board;
import src.Common.Tiles;

import java.awt.image.RenderedImage;

/**
 * Encapsulates a way to render (draw) Tsuro's classes.
 */
public interface IDraw {
    RenderedImage drawTile(Tiles t);

    RenderedImage drawBoard(Board b);
}
