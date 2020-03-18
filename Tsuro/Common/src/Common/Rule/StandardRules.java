package src.Common.Rule;

/**
 * This rule set includes all the standard rules for a game of Tsuro including all default rules,
 * no adjacent initial tiles, no suicide, and no infinite loops.
 */
public class StandardRules extends BaseRules {

  public StandardRules() {
    super();
    this.addInitialRule(new InitialRuleNoAdjacent());
    this.addInitialRule(new InitialRulePortFacesInward());
    this.addTurnRule(new TurnRuleNoSuicide());
    this.addTurnRule(new TurnRuleNoInfiniteLoops());
  }
}
