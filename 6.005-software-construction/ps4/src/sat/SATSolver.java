package sat;

import java.util.Iterator;

import immutable.*;
import sat.env.Environment;
import sat.env.Variable;
import sat.formula.*;

/**
 * A simple DPLL SAT solver. See http://en.wikipedia.org/wiki/DPLL_algorithm
 */
public class SATSolver {
    /**
     * Solve the problem using a simple version of DPLL with backtracking and
     * unit propagation. The returned environment binds literals of class
     * bool.Variable rather than the special literals used in clausification of
     * class clausal.Literal, so that clients can more readily use it.
     * 
     * @return an environment for which the problem evaluates to Bool.TRUE, or
     *         null if no such environment exists.
     */
    public static Environment solve(Formula formula) {
        return solve(formula.getClauses(),new Environment());
    }

    /**
     * Takes a partial assignment of variables to values, and recursively
     * searches for a complete satisfying assignment.
     * 
     * @param clauses
     *            formula in conjunctive normal form
     * @param env
     *            assignment of some or all variables in clauses to true or
     *            false values.
     * @return an environment for which all the clauses evaluate to Bool.TRUE,
     *         or null if no such environment exists.
     */
    private static Environment solve(ImList<Clause> clauses, Environment env) {
    	//If empty, return null
        if (clauses.isEmpty()) {
        	return env;
        }
        
        //Look for an empty clause so we can return env
        else if (clauses.contains(new Clause())) {
        	return null;
        }
        
        //Find the smallest clause by number of literals
        int min_size = Integer.MAX_VALUE;
        Clause min_clause = null;
        Iterator<Clause> iter_clause = clauses.iterator();
        for (Clause c : clauses) {
        	if (c.size() < min_size) {
        		min_clause = c;
        		min_size = c.size();
        	}
        }
        
        Literal lit = min_clause.chooseLiteral();
        Variable var = lit.getVariable();
        
        //If the smallest clause is 1-ary, bind so it's true
        //then solve recursively
        if (min_clause.isUnit()) {
        	//Substitute for the literal by setting it to TRUE
        	ImList<Clause> reducedClauses = substitute(clauses,lit);
        	//Bind value in environment, solve recursively
        	if (lit.equals(PosLiteral.make(var))) {
        		env = env.putTrue(var);
        	}
        	else {
        		env = env.putFalse(var);
        	}
        	return solve(reducedClauses,env);
        	
        }
        
        //Otherwise, pick an arbitrary variable and set it to true
        //then solve recursively
        else {
        	if (lit.equals(PosLiteral.make(var))) {
        		Environment trueAttempt = solve(substitute(clauses,lit),env.putTrue(var));
        		if (trueAttempt != null) {
        			return trueAttempt;
        		}
        		else {
        			lit = lit.getNegation();
        			return solve(substitute(clauses,lit),env.putFalse(var));
        		}
        	}
        	else {
        		Environment falseAttempt = solve(substitute(clauses,lit),env.putFalse(var));
        		if (falseAttempt != null) {
        			return falseAttempt;
        		}
        		else {
        			lit = lit.getNegation();
        			return solve(substitute(clauses,lit),env.putTrue(var));
        		}
        	}
        }
    }

    /**
     * given a clause list and literal, produce a new list resulting from
     * setting that literal to true
     * 
     * @param clauses
     *            , a list of clauses
     * @param l
     *            , a literal to set to true
     * @return a new list of clauses resulting from setting l to true
     */
    private static ImList<Clause> substitute(ImList<Clause> clauses,
            Literal l) {
    	ImList<Clause> output = new EmptyImList();
    	for (Clause c: clauses) {
    		Clause reducedC = c.reduce(l);
    		if (reducedC != null) {
    			output = output.add(reducedC);
    		}
    	}
    	return output;
    	
    }

}
