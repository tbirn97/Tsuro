import Common.*;
import Common.Actions.TurnAction;
import Common.Rule.*;
import org.json.simple.JSONArray;

import java.util.Arrays;
import java.util.List;

class RulesTestHarness extends BoardTestHarness {

    protected static String INVALIDTURN = "Invalid turn pattern";


    public static void main(String[] argv) {
        JSONArray input = parseInput();
        RulesTestHarness testHarness = new RulesTestHarness();

        JSONArray statePats = parseObjToArray(input.get(0));

        Board board = testHarness.setUpBoard(statePats);

        List<InitialRule> initialRules = Arrays.asList(new InitialRuleNoAdjacent(), new InitialRulePortFacesInward());
        List<TurnRule> turnRules = Arrays.asList(new TurnRuleNoInfiniteLoops(), new TurnRuleNoSuicide());
        Rules rules = new Rules(initialRules, turnRules);

        JSONArray turnPat = parseObjToArray(input.get(1));
        String output = testHarness.parseTurnPat(rules, board, turnPat);

        System.out.println(output);
    }

    public String parseTurnPat(Rules rules, Board board, JSONArray turnPat) {
        if (turnPat.size() != 3) {
            return INVALIDTURN;
        }

        int tileNum1 = parseObjToInt(turnPat.get(1));
        int tileNum2 = parseObjToInt(turnPat.get(2));
        Tiles tile1 = getTileFromIndex(tileNum1);
        Tiles tile2 = getTileFromIndex(tileNum2);

        JSONArray actionPat = parseObjToArray(turnPat.get(0));
        if (actionPat.size() != 2) {
            return INVALIDTURN;
        }

        String color = parseObjToString(actionPat.get(0));
        AvatarColor avatarColor = getAvatarColor(color);

        JSONArray tilePat = parseObjToArray(actionPat.get(1));

        if (tilePat.size() != 2) {
            return INVALIDTURN;
        }

        int chosenTileNum = parseObjToInt(tilePat.get(0));
        Tiles chosenTile = getTileFromIndex(chosenTileNum);
        int rotation = parseObjToInt(tilePat.get(1));
        Facing facing = getFacingFromInt(rotation);

        boolean isValidMove = rules.isValidTurn(board, Arrays.asList(tile1, tile2),
                new TurnAction(avatarColor, chosenTile, facing));


        //System.out.println(board.toString());
        //System.out.println(new TurnAction(avatarColor, chosenTile, facing).toString());
        if (isValidMove) {
            return "\"legal\"";
        }
        else {
            return "\"cheating\"";
        }
    }
}