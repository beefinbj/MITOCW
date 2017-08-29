from production import AND, OR, NOT, PASS, FAIL, IF, THEN, \
     match, populate, simplify, variables
from zookeeper import ZOOKEEPER_RULES

# This function, which you need to write, takes in a hypothesis
# that can be determined using a set of rules, and outputs a goal
# tree of which statements it would need to test to prove that
# hypothesis. Refer to the problem set (section 2) for more
# detailed specifications and examples.

# Note that this function is supposed to be a general
# backchainer.  You should not hard-code anything that is
# specific to a particular rule set.  The backchainer will be
# tested on things other than ZOOKEEPER_RULES.


def backchain_to_goal_tree(rules, hypothesis):
    out = OR(hypothesis)
    #AND hypotheses
    if isinstance(hypothesis,AND):
        subtree = AND()
        for hyp in hypothesis:
            conj = backchain_to_goal_tree(rules,hyp)
            subtree = AND(subtree,conj)
        out = OR(subtree)
    #OR hypotheses
    elif isinstance(hypothesis,OR):
        subtree = OR()
        for hyp in hypothesis:
            disj = backchain_to_goal_tree(rules,hyp)
            subtree = OR(subtree,disj)
        out = OR(subtree)
    #Leaf hypothesis
    else:
        for rule in rules:
            bind = match(rule.consequent()[0],hypothesis)
            if (bind != None):
                ant = rule.antecedent()
                newHyp = populate(ant,bind)
                subtree = backchain_to_goal_tree(rules,newHyp)
                out = OR(out,subtree)
    return simplify(out)

# Here's an example of running the backward chainer - uncomment
# it to see it work:
# print backchain_to_goal_tree(ZOOKEEPER_RULES, 'opus is a penguin')
