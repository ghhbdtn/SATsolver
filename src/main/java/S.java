import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class S {

    public static Set<Integer> variables = new HashSet<>();
    public S(String inputName) throws Exception {
        solve(inputName);
    }

    private void solve(final String inputName) throws Exception {

        final List<Clause1> clauses = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputName));
        String line = br.readLine();
        while (line != null) {
            if (line.endsWith(" 0")) {
                Clause1 clause = new Clause1(line);
                clauses.add(clause);
            }
            line = br.readLine();
        }

        Set<Integer> literals = new HashSet<>();
        Set<Integer> pureLiterals = findPureLiterals(clauses);
        if (pureLiterals.size() > 0) {
            pureLiteralsProcess(clauses, pureLiterals);
            literals.addAll(pureLiterals);
        }

        DPLL(clauses, literals);

        // If we here UNSAT:
        System.out.println("UNSAT");
    }

    private void DPLL(List<Clause1> clauses, Set<Integer> literals) {

        // Check if we have no more clauses that are unsatisfied:
        if (clauses.stream().allMatch(clause -> clause.clauseSatisfied)) {
            // SAT
            exitWithSAT(literals);
        }

        Set<Integer> newLiterals = new HashSet<>();

        // Find unit clauses:
        Set<Integer> unitPropagation = findUnitClauses(clauses);

        // Process the unit variables:
        while(unitPropagation.size() > 0) {

            // Remove this unit from all clauses:
            for (Integer unit : unitPropagation) {
                // Detect conflicts: We can not have both unit and -unit in the set, if there is we've hit a conflict.
                if (unitPropagation.contains(-unit)) {// Undo everything we've done and return.
                    for (Integer literal : newLiterals) {
                        undoStep(clauses, literal);
                    }
                    return;
                }
                newLiterals.add(unit);
                applyStep(clauses, unit);
            }
            unitPropagation.removeAll(newLiterals);

            unitPropagation.addAll(findUnitClauses(clauses));
        }

        // Get all the unassignedLiterals from the alive clauses:

        Set<Integer> unassignedLiterals = variables.stream().filter(p -> !newLiterals.contains(p) && !literals.contains(p)).collect(Collectors.toSet());
                /*clauses.stream()
                .filter(clause -> !clause.clauseSatisfied)
                .flatMap(clause -> clause.unassignedLiterals.stream()).collect(Collectors.toSet());*/


        if (unassignedLiterals.isEmpty()) {
            newLiterals.addAll(literals);
            exitWithSAT(newLiterals);
        }

        for(Integer decisionLiteral : unassignedLiterals) {
            applyStep(clauses, decisionLiteral);

            Set<Integer> deeperAssignedLiterals = new HashSet<>();
            deeperAssignedLiterals.addAll(literals);
            deeperAssignedLiterals.addAll(newLiterals);
            deeperAssignedLiterals.add(decisionLiteral);

            DPLL(clauses, deeperAssignedLiterals);

            undoStep(clauses, decisionLiteral);
        }

        // Undo all we've done this step:
        for(Integer literal : newLiterals) {
            undoStep(clauses, literal);
        }

    }

    private Set<Integer> findPureLiterals(List<Clause1> clauses) {
        Set<Integer> pureLiterals = new HashSet<>();
        for (int literal : variables) {
            if (!variables.contains(-literal)) {
                pureLiterals.add(literal);
            }
        }
        return pureLiterals;
    }

    private void pureLiteralsProcess(List<Clause1> clauses, Set<Integer> pureLiterals) {
        for (Integer literal : pureLiterals) {
            for (Clause1 clause : clauses) {
                if (clause.positive[literal - 1] != null || clause.negative[literal - 1] != null) {
                    clause.clauseSatisfied = true;
                }
            }
        }
    }

    private void applyStep(final List<Clause1> clauses, final int literal) {
        boolean positiveN;
        if (literal > 0){
            positiveN = true;
        } else positiveN = false;
        if (positiveN) {
            for(Clause1 clause : clauses) {
                if(!clause.clauseSatisfied) {
                    if(clause.positive[literal - 1] != null) {
                        clause.clauseSatisfied = true;
                    } else if(clause.negative[literal - 1] != null) {
                        clause.negative[literal - 1] = "DEAD";
                    }
                }
            }
        }else {
            for (Clause1 clause : clauses) {
                if(clause.negative[literal - 1] != null) {
                    clause.clauseSatisfied = true;
                } else if(clause.positive[literal - 1] != null) {
                    clause.positive[literal - 1] = "DEAD";
                }
            }
        }
    }


    private void undoStep(final List<Clause1> clauses, final int literal) {
        boolean positiveN;
        if (literal > 0){
            positiveN = true;
        } else positiveN = false;
        if (positiveN) {
            for (Clause1 clause : clauses) {
                if (clause.clauseSatisfied && clause.positive[literal - 1] != null) {
                    clause.clauseSatisfied = false;
                }
                if (clause.negative[literal - 1] == "DEAD") {
                    clause.negative[literal - 1] = "UNSAT";
                }
            }
        }else {
            for (Clause1 clause : clauses) {
                if (clause.clauseSatisfied && clause.negative[literal - 1] != null) {
                    clause.clauseSatisfied = false;
                }
                if (clause.positive[literal - 1] == "DEAD") {
                    clause.positive[literal - 1] = "UNSAT";
                }
            }
        }
    }

    private Set<Integer> findUnitClauses(final List<Clause1> clauses) {
        Set<Integer> unitPropagation = new HashSet<>();
        for(Clause1 clause : clauses) {
            if(clause.isUnitClause()) {
                for (int i = 0; i < clause.positive.length; i++){
                    if (clause.positive[i] == "UNSAT")
                        unitPropagation.add(i + 1);
                }
                for (int i = 0; i < clause.negative.length; i++){
                    if (clause.negative[i] == "UNSAT")
                        unitPropagation.add(-i - 1);
                }
            }
        }
        return unitPropagation;
    }

    private void exitWithSAT(final Set<Integer> literals) {
        System.out.println("SAT");
        // Sort the output as absolute values.
        System.out.println(literals.stream().sorted(Comparator.comparingInt(Math::abs)).map(String::valueOf).collect(Collectors.joining(" ")) + " 0");
        long time = System.currentTimeMillis() - SATLauncher.startTime;
        System.out.println(time);
        System.exit(1);
    }

    public static class Clause1 {
        private String[] positive;
        private String[] negative;
        private boolean clauseSatisfied = false;
        private int numberOfLiterals = 0;
        private Clause1(String inputLine) {
            String[] line = inputLine.substring(0, inputLine.length() - 2).split("\\s");
            for (String literal : line) {
                int num = Integer.parseInt(literal);
                variables.add(num);
                numberOfLiterals++;
                if (num > 0) {
                    positive[numberOfLiterals] = "UNSAT";
                } else {
                    negative[numberOfLiterals] = "UNSAT";
                }
            }
        }
        boolean isUnitClause() {
            return !clauseSatisfied && numberOfLiterals == 1;
        }
    }
}
