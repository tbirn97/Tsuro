package src.Common;

import org.json.JSONArray;
import org.junit.Test;

import src.Common.Actions.InitialAction;
import src.Common.json.ActionHandler;
import src.Common.json.ColorHandler;
import src.Common.json.StatePatHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JSONTest {
    private TilesFactory tilesFactory = new TilesFactory();

    private ArrayList<AvatarColor> createColorArrayTest1() {
        ArrayList<AvatarColor> colors = new ArrayList<>();
        colors.add(AvatarColor.blue);
        colors.add(AvatarColor.white);
        colors.add(AvatarColor.black);
        return colors;
    }

    private JSONArray createActionJSONTest1() {
        JSONArray tilePat = new JSONArray();
        tilePat.put(0);
        tilePat.put(0);
        JSONArray actions = new JSONArray();
        actions.put(tilePat);
        actions.put("A");
        actions.put(0);
        actions.put(0);
        return actions;
    }

    private Board createBoardForBoard1() {
        Map<Posn, Tiles> tiles = new HashMap<>();
        Map<AvatarColor, AvatarLocation> avatars = new HashMap<>();
        Tiles tile1 = tilesFactory.makeTile(0);
        Posn tile1Location = new Posn(0, 0);
        Tiles tile2 = tilesFactory.makeTile(1);
        Posn tile2Location = new Posn(2, 0);
        Tiles tile3 = tilesFactory.makeTile(2);
        Posn tile3Location = new Posn(4, 0);
        tiles.put(tile1Location, tile1);
        tiles.put(tile2Location, tile2);
        tiles.put(tile3Location, tile3);
        avatars.put(AvatarColor.red, new AvatarLocation(tile1Location, Port.E));
        avatars.put(AvatarColor.white, new AvatarLocation(tile2Location, Port.F));
        return new Board.Builder().setTiles(tiles).setAvatars(avatars).build();

    }
    private JSONArray createStatePatsForBoard1() {
        JSONArray boardPats = new JSONArray();

        JSONArray initial1 = new JSONArray();
        JSONArray initial1TilePat = new JSONArray();
        initial1TilePat.put(1);
        initial1TilePat.put(0);
        initial1.put(initial1TilePat);
        initial1.put(AvatarColor.white);
        initial1.put("F");
        initial1.put(2);
        initial1.put(0);

        JSONArray initial2 = new JSONArray();
        JSONArray initial2TilePat = new JSONArray();
        initial2TilePat.put(0);
        initial2TilePat.put(0);
        initial2.put(initial2TilePat);
        initial2.put(AvatarColor.red);
        initial2.put("E");
        initial2.put(0);
        initial2.put(0);

        JSONArray intermediate = new JSONArray();
        JSONArray intermediateTilePat = new JSONArray();
        intermediateTilePat.put(2);
        intermediateTilePat.put(0);
        intermediate.put(intermediateTilePat);
        intermediate.put(4);
        intermediate.put(0);

        boardPats.put(initial1);
        boardPats.put(initial2);
        boardPats.put(intermediate);

        return boardPats;
    }

    private InitialAction createInitialActionTestRedAvatar1() {
        AvatarColor avatar = AvatarColor.red;
        AvatarLocation avatarLocation = new AvatarLocation(0, 0, Port.A);
        Tiles tile = tilesFactory.makeTile(0);
        InitialAction action = new InitialAction(avatar, tile, Rotation.R0, avatarLocation);
        return action;
    }

    @Test
    public void testColorHandlerSerialization() {
        JSONArray colorsArray = ColorHandler.listToJson(createColorArrayTest1());
        assertEquals("[\"BLUE\",\"WHITE\",\"BLACK\"]", colorsArray.toString());
    }

    @Test
    public void testColorHandlerDeserialization() {
        JSONArray colorsArray = new JSONArray();
        colorsArray.put("BLUE");
        colorsArray.put("WHITE");
        colorsArray.put("BLACK");
        ArrayList expected = createColorArrayTest1();
        List<AvatarColor> actual = ColorHandler.jsonToList(colorsArray);
        assertEquals(expected, actual);
    }

    @Test
    public void testColorHandlerSerializationIntoDeserialization() {
        JSONArray colorsArray = ColorHandler.listToJson(createColorArrayTest1());
        List<AvatarColor>  colorsList = ColorHandler.jsonToList(colorsArray);
        List<AvatarColor> expected = createColorArrayTest1();
        assertEquals(expected, colorsList);
    }


    @Test
    public void testActionHandlerSerialization() {
        AvatarColor avatar = AvatarColor.red;
        JSONArray action = createActionJSONTest1();
        InitialAction expected = createInitialActionTestRedAvatar1();
        InitialAction actual = ActionHandler.jsonToInitialAction(action, avatar);
        assertEquals(expected, actual);
    }

    @Test
    public void testActionHandlerDeserialization() {
        InitialAction action = createInitialActionTestRedAvatar1();
        JSONArray expected = createActionJSONTest1();
        JSONArray actual = ActionHandler.initialActionToJSON(action);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testActionHandlerSerializationIntoDeserialization() {
        AvatarColor avatar = AvatarColor.red;
        InitialAction initialAction = createInitialActionTestRedAvatar1();

        JSONArray serialized = ActionHandler.initialActionToJSON(initialAction);
        InitialAction deserialized = ActionHandler.jsonToInitialAction(serialized, avatar);
        assertEquals(initialAction, deserialized);
    }

    @Test
    public void testStatePatHandlerSerialization() {
        JSONArray boardStatePats = createStatePatsForBoard1();
        Board expected = createBoardForBoard1();
        Board actual = StatePatHandler.jsonToBoard(boardStatePats);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    public void testStatePatHandlerDeserialization() {
        Board board = createBoardForBoard1();
        JSONArray expected = createStatePatsForBoard1();
        Set<String> allExpectedStatePats = new HashSet<>();
        for (int i = 0; i < expected.length(); i++) {
            allExpectedStatePats.add(expected.getJSONArray(i).toString());
        }

        JSONArray actual = StatePatHandler.boardToJSON(board);
        Set<String> allActualStatePats = new HashSet<>();
        for (int i = 0; i < expected.length(); i++) {
            allActualStatePats.add(actual.getJSONArray(i).toString());
        }

        assertEquals(allExpectedStatePats.size(), allActualStatePats.size());
        for (String expectedStatePat : allExpectedStatePats) {
            assertTrue(allActualStatePats.contains(expectedStatePat));
        }
    }

    @Test
    public void testStatePatHandlerSerializationIntoDeserialization() {
        Board board = createBoardForBoard1();
        JSONArray jsonBoard = StatePatHandler.boardToJSON(board);
        Board actual = StatePatHandler.jsonToBoard(jsonBoard);
        assertEquals(board.toString(), actual.toString());

    }


}
