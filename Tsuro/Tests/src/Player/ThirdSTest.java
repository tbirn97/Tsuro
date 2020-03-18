package src.Player;

import org.junit.Test;
import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.AvatarColor;
import src.Common.AvatarLocation;
import src.Common.Board;
import src.Common.BoardMutator;
import src.Common.Port;
import src.Common.Posn;
import src.Common.Rule.BaseRules;
import src.Common.Rules;
import src.Common.Tiles;
import src.Common.TilesFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class ThirdSTest {
  private AvatarColor avatar = AvatarColor.black;
  private Board board = new Board.Builder().build();
  private TilesFactory factory = new TilesFactory();
  private Rules ruleChecker = new BaseRules();
  private ThirdS strategy = new ThirdS();

  @Test
  public void testGetInitialTurnEmpty() {
    List<Tiles> hand = Arrays.asList(factory.makeTile(0),
        factory.makeTile(1),
        factory.makeTile(2));
    InitialAction blackAction = strategy.getInitialTurn(avatar, board, hand, ruleChecker);
    assertEquals(new AvatarLocation(new Posn(0, 1), Port.A), blackAction.getAvatarPlacement());
    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(2), blackAction.getTileToPlace());
  }

  @Test
  public void testGetInitialTurnZeroFilled() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(0, 0), factory.makeTile(0));
    Board board = new Board.Builder().setTiles(boardTiles).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(0),
        factory.makeTile(1),
        factory.makeTile(2));
    InitialAction blackAction = strategy.getInitialTurn(avatar, board, hand, ruleChecker);
    assertEquals(new AvatarLocation(new Posn(0, 2), Port.A), blackAction.getAvatarPlacement());
    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(2), blackAction.getTileToPlace());
  }

  @Test
  public void testGetInitialTurnLeftSideFilled() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(0, 0), factory.makeTile(0));
    boardTiles.put(new Posn(0, 3), factory.makeTile(0));
    boardTiles.put(new Posn(0, 6), factory.makeTile(0));
    boardTiles.put(new Posn(0, 9), factory.makeTile(0));
    Board board = new Board.Builder().setTiles(boardTiles).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(0),
        factory.makeTile(1),
        factory.makeTile(2));
    InitialAction blackAction = strategy.getInitialTurn(avatar, board, hand, ruleChecker);
    assertEquals(new AvatarLocation(new Posn(2, 9), Port.A), blackAction.getAvatarPlacement());
    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(2), blackAction.getTileToPlace());
  }

  @Test
  public void testGetTurnSimple() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(4, 4), factory.makeTile(2));
    Map<AvatarColor, AvatarLocation> boardAvatars = new HashMap<>();
    boardAvatars.put(AvatarColor.black, new AvatarLocation(new Posn(4, 4), Port.A));
    Board board = new Board.Builder().setTiles(boardTiles).setAvatars(boardAvatars).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(1),
        factory.makeTile(0));

    TurnAction blackAction = strategy.getTurn(AvatarColor.black, board, hand, ruleChecker);
    board = BoardMutator.placeTurnTile(board, avatar, blackAction.getTileToPlace(), blackAction.getRotation());

    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(1), blackAction.getTileToPlace());

    Optional<AvatarLocation> blackLocation = board.getAvatarLocation(avatar);
    assertTrue(blackLocation.isPresent());
    assertEquals(new AvatarLocation(new Posn(4,3 ), Port.B), blackLocation.get());
  }

  @Test
  public void testGetTurnFirstWrong() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(4, 4), factory.makeTile(2));
    Map<AvatarColor, AvatarLocation> boardAvatars = new HashMap<>();
    boardAvatars.put(AvatarColor.black, new AvatarLocation(new Posn(4, 4), Port.A));
    Board board = new Board.Builder().setTiles(boardTiles).setAvatars(boardAvatars).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(1),
        factory.makeTile(0));

    Rules ruleChecker = mock(Rules.class);
    when(ruleChecker.isValidTurn(any(), any(), any())).thenReturn(false, true);
    TurnAction blackAction = strategy.getTurn(AvatarColor.black, board, hand, ruleChecker);
    board = BoardMutator.placeTurnTile(board, avatar, blackAction.getTileToPlace(), blackAction.getRotation());

    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(1), blackAction.getTileToPlace());

    Optional<AvatarLocation> blackLocation = board.getAvatarLocation(avatar);
    assertTrue(blackLocation.isPresent());
    assertEquals(new AvatarLocation(new Posn(4,3 ), Port.A), blackLocation.get());
  }

  @Test
  public void testGetTurnFirstTileWrong() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(4, 4), factory.makeTile(2));
    Map<AvatarColor, AvatarLocation> boardAvatars = new HashMap<>();
    boardAvatars.put(AvatarColor.black, new AvatarLocation(new Posn(4, 4), Port.A));
    Board board = new Board.Builder().setTiles(boardTiles).setAvatars(boardAvatars).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(4),
        factory.makeTile(0));

    Rules ruleChecker = mock(Rules.class);
    when(ruleChecker.isValidTurn(any(), any(), any())).thenReturn(false, false, false, false, true);
    TurnAction blackAction = strategy.getTurn(AvatarColor.black, board, hand, ruleChecker);
    board = BoardMutator.placeTurnTile(board, avatar, blackAction.getTileToPlace(), blackAction.getRotation());

    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(0), blackAction.getTileToPlace());

    Optional<AvatarLocation> blackLocation = board.getAvatarLocation(avatar);
    assertTrue(blackLocation.isPresent());
    assertEquals(new AvatarLocation(new Posn(4,3 ), Port.G), blackLocation.get());
  }

  @Test
  public void testGetTurnAllInvalid() {
    Map<Posn, Tiles> boardTiles = new HashMap<>();
    boardTiles.put(new Posn(4, 4), factory.makeTile(2));
    Map<AvatarColor, AvatarLocation> boardAvatars = new HashMap<>();
    boardAvatars.put(AvatarColor.black, new AvatarLocation(new Posn(4, 4), Port.A));
    Board board = new Board.Builder().setTiles(boardTiles).setAvatars(boardAvatars).build();
    List<Tiles> hand = Arrays.asList(factory.makeTile(4),
        factory.makeTile(0));

    Rules ruleChecker = mock(Rules.class);
    when(ruleChecker.isValidTurn(any(), any(), any())).thenReturn(false);
    TurnAction blackAction = strategy.getTurn(AvatarColor.black, board, hand, ruleChecker);
    board = BoardMutator.placeTurnTile(board, avatar, blackAction.getTileToPlace(), blackAction.getRotation());

    assertEquals(avatar, blackAction.getPlayerAvatar());
    assertEquals(hand.get(1), blackAction.getTileToPlace());

    Optional<AvatarLocation> blackLocation = board.getAvatarLocation(avatar);
    assertTrue(blackLocation.isPresent());
    assertEquals(new AvatarLocation(new Posn(4,3 ), Port.B), blackLocation.get());
  }
}
