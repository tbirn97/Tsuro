package src.Common.Draw;

import src.Admin.CompetitionResult;
import src.Common.PlayerInterface;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AdminDrawer {

  private final int IMAGE_WIDTH = 1000;
  private final int IMAGE_HEIGHT = 800;
  private final int RANKS_ADVANCING = 2;

  /**
   * Create an image visualizing a round of Tsuro and highlight the players that finished in the
   * top two places for each game.
   *
   * Assume that gameGroups and gameResults are lists of the same length and that at each index i,
   * all names in gameGroups.get(i) appear once and only once in gameResults.
   * TODO: For now, duplicate names are not supported and the behavior with them is not defined.
   */
  public RenderedImage renderRound(List<List<String>> gameGroups, Optional<List<CompetitionResult>> gameResults) {
    BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    drawBackground(graphics);

    List<List<Color>> playerColors;
    if (gameResults.isPresent()) {
      playerColors = getPlayerColorList(gameGroups, gameResults.get());
    } else {
      playerColors = getFillerPlayerColors(gameGroups);
    }
    drawRound(graphics, gameGroups, playerColors);

    return image;
  }

  public RenderedImage renderTournamentResult(CompetitionResult tournamentResult) {
    BufferedImage image = new BufferedImage(IMAGE_WIDTH, IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
    Graphics2D graphics = image.createGraphics();
    drawBackground(graphics);

    List<List<PlayerInterface>> ranking = tournamentResult.getRankings();
    String title = "Tournament Winners";
    String first = getPlaceText(ranking, 1);
    String second = getPlaceText(ranking, 2);
    String third = getPlaceText(ranking, 3);


    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Roboto", Font.BOLD, 50));
    graphics.drawString(title, 20, 50);
    graphics.setFont(new Font("Roboto", Font.PLAIN, 50));
    graphics.setColor(new Color(255,215,0));
    graphics.drawString(first, 20, 150);
    graphics.setColor(new Color(192,192,192));
    graphics.drawString(second, 20, 250);
    graphics.setColor(new Color(205, 127, 50));
    graphics.drawString(third, 20, 350);

    return image;
  }


  private void drawBackground(Graphics2D graphics) {
    graphics.setColor(new Color(255, 255, 255));
    graphics.fillRect(0, 0, IMAGE_WIDTH, IMAGE_HEIGHT);
  }

  /**
   * Draw a round with the given graphics that shows each game in gameGroups with the color of players
   * determined by playerColors.
   */
  private void drawRound(Graphics2D graphics, List<List<String>> gameGroups, List<List<Color>> playerColors) {
    int totalGames = gameGroups.size();
    int padding = 10;
    int[] dimensions = getDimensions(totalGames);
    int gameWidth = (IMAGE_WIDTH - padding * (1 + dimensions[0])) / dimensions[0];
    int gameHeight = (IMAGE_HEIGHT - padding * (1 + dimensions[1])) / dimensions[1];
    for (int i = 0; i < totalGames; i++) {
      BufferedImage gameImage = renderTournamentGame(gameGroups.get(i), playerColors.get(i), gameWidth, gameHeight);

      int gameX = (i % dimensions[0]) * (gameWidth + padding) + padding;
      int gameY = (i / dimensions[0]) * (gameHeight + padding) + padding;
      graphics.drawImage(gameImage, gameX, gameY, null);
    }
  }

  /**
   * Creates an image drawing the player names and results for a single Tsuro game.
   */
  private BufferedImage renderTournamentGame(List<String> players, List<Color> playerColors, int maxWidth, int maxHeight) {
    int width = maxWidth;
    int textSize = maxHeight / 10;
    int height = textSize * players.size() * 2;

    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    Graphics2D graphics = image.createGraphics();
    graphics.setColor(new Color(200, 200, 200));
    graphics.fillRect(0, 0, width, height);

    graphics.setColor(Color.BLACK);
    graphics.setFont(new Font("Roboto", Font.PLAIN, textSize));

    for (int i = 0; i < players.size(); i++) {
      int rectY = i * height / players.size();
      int rectHeight = (i + 1) * height / players.size();
      int textY = (1 + i) * (height / players.size()) - textSize / 2;

      graphics.setColor(playerColors.get(i));
      graphics.fillRect(0, rectY, width, rectHeight);

      graphics.setColor(Color.BLACK);
      graphics.setStroke(new BasicStroke(3));
      graphics.drawRect(0, rectY, width, (i + 1) * rectHeight);
      graphics.drawString(players.get(i), 3, textY);
    }

    return image;
  }

  /**
   * Gets the dimensions necessary to display size number of cells in a grid pattern where there are
   * about 3 columns for every 2 rows.
   *
   * @return A size 2 integer array with the number of columns as the first element and number of rows as the second.
   */
  private int[] getDimensions(int size) {
    int columns = (int) Math.ceil(Math.sqrt(1.5) * Math.sqrt(size));
    int rows = (2 * columns) / 3;
    return new int[]{columns, rows};
  }

  /**
   * Gets a list of list of colors for each player in gameGroups that are all a standard
   * color to indicate no one has played yet.
   */
  private List<List<Color>> getFillerPlayerColors(List<List<String>> gameGroups) {
    List<List<Color>> allPlayerColors = new ArrayList<>();
    for (List<String> game : gameGroups) {
      List<Color> playerColors = new ArrayList<>();
      for (String player : game) {
        playerColors.add(new Color(200, 200, 200));
      }
      allPlayerColors.add(playerColors);
    }
    return allPlayerColors;
  }

  /**
   * Get a list of list of colors that maps to each player in gameGroups to the color they should be highlighted
   * based on their placement in their game.
   */
  private List<List<Color>> getPlayerColorList(List<List<String>> gameGroups, List<CompetitionResult> gameResults) {
    List<List<Color>> playerColors = new ArrayList<>();
    for (int i = 0; i < gameGroups.size(); i++) {
      playerColors.add(getPlayerColorsFromResult(gameGroups.get(i), gameResults.get(i)));
    }
    return playerColors;
  }

  /**
   * Get a list of colors that maps the list of playerNames to the color they should be highlighted
   * based on their placement in a game.
   *
   * GREEN = Advancing
   * YELLOW = Lost
   * RED = Cheated
   */
  private List<Color> getPlayerColorsFromResult(List<String> playerNames, CompetitionResult result) {
    Map<String, Color> colorMap = new HashMap<>();
    for (int i = 0; i < result.getRankings().size(); i++) {
      if (i < RANKS_ADVANCING) {
        for (PlayerInterface player : result.getRankings().get(i)) {
          colorMap.put(player.getName(), Color.GREEN);
        }
      } else {
        for (PlayerInterface player : result.getRankings().get(i)) {
          colorMap.put(player.getName(), Color.YELLOW);
        }
      }
    }
    for (PlayerInterface player : result.getCheaters()) {
      colorMap.put(player.getName(), Color.RED);
    }
    return playerNames.stream().map(colorMap::get).collect(Collectors.toList());
  }

  /**
   * With the given ranking, return a string showing the players that finished at a certain ranking.
   */
  private String getPlaceText(List<List<PlayerInterface>> ranking, int place) {
    String lead = String.format("Rank %d: ", place);
    if (ranking.size() <= place - 1) {
      return lead + "None";
    } else {
      List<String> placeNames = ranking.get(place - 1).stream()
          .map(PlayerInterface::getName)
          .collect(Collectors.toList());
      String placeText = String.join(" & ", placeNames);
      return lead + placeText;
    }
  }
}
