package src.Admin;

import src.Common.Draw.AdminDrawer;
import src.Common.Draw.GraphicalDraw;
import src.Common.IObserver;
import src.Common.ISubject;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.List;
import java.util.Optional;

/**
 * This uses the Observer Pattern.
 */
public class TournamentObserver implements IObserver {

  private JFrame frame = new JFrame();

  @Override
  public void notify(ISubject caller) {
    //Should be called at the beginning and end of each tournament round as well as the end of a tournament.

    Optional<List<List<String>>> gameGroups = caller.getGameGroups();
    Optional<List<CompetitionResult>> gameResults = caller.getGameResults();
    Optional<CompetitionResult> tournamentResult = caller.getResult();

    RenderedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
    if (tournamentResult.isPresent()) {
      AdminDrawer drawer = new AdminDrawer();
      image = drawer.renderTournamentResult(tournamentResult.get());
    } else if (gameGroups.isPresent()) {
      AdminDrawer drawer = new AdminDrawer();
      image = drawer.renderRound(gameGroups.get(), gameResults);
    }

    JLabel label = new JLabel(new ImageIcon((Image) image));
    frame.setContentPane( label );

    frame.pack();
    frame.setVisible(true);
  }

}
