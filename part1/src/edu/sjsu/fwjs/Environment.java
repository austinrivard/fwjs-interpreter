package edu.sjsu.fwjs;

import java.util.Map;
import java.util.HashMap;

public class Environment {
    private Map<String,Value> env = new HashMap<String,Value>();
    private Environment outerEnv;

    /**
     * Constructor for global environment
     */
    public Environment() {}

    /**
     * Constructor for local environment of a function
     */
    public Environment(Environment outerEnv) {
        this.outerEnv = outerEnv;
    }

    /**
     * Handles the logic of resolving a variable.
     * If the variable name is in the current scope, it is returned.
     * Otherwise, search for the variable in the outer scope.
     * If we are at the outermost scope (AKA the global scope)
     * null is returned (similar to how JS returns undefined.
     */
    public Value resolveVar(String varName) {
        Environment e = new Environment(this);

        while ((e != null) && (e.env.get(varName) == null)) {
            // searching outer scope(s) if var not found in current
            e = e.outerEnv;
        }
        if(e == null) {
            // outermost scope reached
            return new NullVal();
        } else {
            return e.env.get(varName);
        }
    }

    /**
     * Used for updating existing variables.
     * If a variable has not been defined previously in the current scope,
     * or any of the function's outer scopes, the var is stored in the global scope.
     */
    public void updateVar(String key, Value v) {
        if (env.containsKey(key)) {
            // variable found in current scope
            env.replace(key, v);
        }
        else if (this.outerEnv != null && this.outerEnv.resolveVar(key) != null) {
            // try in outer scope(s)
            this.outerEnv.updateVar(key, v);
        } else {
            // declare as global variable
            this.env.put(key, v);
        }
    }

    /**
     * Creates a new variable in the local scope.
     * If the variable has been defined in the current scope previously,
     * a RuntimeException is thrown.
     */
    public void createVar(String key, Value v) {
        if (this.env.containsKey(key)) {
            throw new RuntimeException("Variable" + key + "has already been defined");
        }
        this.env.put(key, v);
    }
}
