package src.Common;

import src.Common.Actions.InitialAction;
import src.Common.Actions.TurnAction;
import src.Common.Board;
import src.Common.Rule.InitialRule;
import src.Common.Rule.TurnRule;
import src.Common.Rules;
import src.Common.Tiles;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class RulesTest {

    private class InitialRuleImplTrue implements InitialRule {
        @Override
        public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn) {
            return true;
        }
    }

    private class InitialRuleImplFalse implements InitialRule {
        @Override
        public boolean isValidMove(Board board, List<Tiles> hand, InitialAction playerTurn) {
            return false;
        }

    }

    private class TurnRuleImplTrue implements TurnRule {
        @Override
        public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
            return true;
        }
    }

    private class TurnRuleImplFalse implements TurnRule {
        @Override
        public boolean isValidMove(Board board, List<Tiles> hand, TurnAction playerMove) {
            return false;
        }
    }

    private List<InitialRule> initialRulesPass = Arrays.asList(new InitialRuleImplTrue(), new InitialRuleImplTrue(), new InitialRuleImplTrue());

    private List<InitialRule> initialRulesFail1 = Arrays.asList(new InitialRuleImplTrue(), new InitialRuleImplTrue(), new InitialRuleImplTrue(), new InitialRuleImplFalse());

    private List<InitialRule> initialRulesFail2 = Arrays.asList(new InitialRuleImplTrue(), new InitialRuleImplFalse(), new InitialRuleImplTrue());

    private List<InitialRule> initialRulesFail3 = Arrays.asList(new InitialRuleImplFalse(), new InitialRuleImplTrue());

    private List<TurnRule> turnRulePass = Arrays.asList(new TurnRuleImplTrue(), new TurnRuleImplTrue(), new TurnRuleImplTrue());

    private List<TurnRule> turnRuleFail1 = Arrays.asList(new TurnRuleImplTrue(), new TurnRuleImplTrue(), new TurnRuleImplFalse());

    private List<TurnRule> turnRuleFail2 = Arrays.asList(new TurnRuleImplTrue(), new TurnRuleImplFalse(), new TurnRuleImplTrue());

    private List<TurnRule> turnRuleFail3 = Arrays.asList(new TurnRuleImplFalse(), new TurnRuleImplTrue(), new TurnRuleImplFalse());


    @Test
    public void complexConstructor() {
        Rules rules1 = new Rules(this.initialRulesFail1, this.turnRulePass);
        assertEquals(this.initialRulesFail1.size(), rules1.getInitialRules().size());
        assertEquals(this.turnRulePass.size(), rules1.getTurnRules().size());
    }

    @Test
    public void addInitialRule() {
        Rules rules = new Rules();
        assertEquals(0, rules.getInitialRules().size());
        rules.addInitialRule(new InitialRuleImplFalse());
        assertEquals(1, rules.getInitialRules().size());
        assertEquals(0, rules.getTurnRules().size());
    }

    @Test
    public void addTurnRule() {
        Rules rules = new Rules();
        assertEquals(0, rules.getTurnRules().size());
        rules.addTurnRule(new TurnRuleImplTrue());
        assertEquals(1, rules.getTurnRules().size());
        assertEquals(0, rules.getInitialRules().size());
    }

    @Test
    public void isValidInitTilePlace() {
        Rules rules = new Rules(initialRulesPass, turnRuleFail3);
        assertTrue(rules.isValidInitialMove(null, null, null));

        rules = new Rules(initialRulesFail1, turnRulePass);
        assertFalse(rules.isValidInitialMove(null, null, null));

        rules = new Rules(initialRulesFail2, turnRuleFail1);
        assertFalse(rules.isValidInitialMove(null, null, null));

        rules = new Rules(initialRulesFail3, turnRuleFail2);
        assertFalse(rules.isValidInitialMove(null, null, null));
    }

    @Test
    public void isValidTurn() {
        Rules rules = new Rules(initialRulesFail2, turnRulePass);
        assertTrue(rules.isValidTurn(null, null, null));

        rules = new Rules(initialRulesFail1, turnRuleFail1);
        assertFalse(rules.isValidTurn(null, null, null));

        rules = new Rules(initialRulesPass, turnRuleFail2);
        assertFalse(rules.isValidTurn(null, null, null));

        rules = new Rules(initialRulesFail3, turnRuleFail3);
        assertFalse(rules.isValidTurn(null, null, null));
    }
}