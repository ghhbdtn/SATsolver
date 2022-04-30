import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SATsolver {
    public SATsolver(String inputName) throws Exception {
        solve(inputName);
    }

    private void solve(final String inputName) throws Exception {

        final List<Clause> clauses = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(inputName));
        String line = br.readLine();
        while (line != null) {
            if (line.endsWith(" 0")) {
                Clause clause = new Clause(line);
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

    private void DPLL(List<Clause> clauses, Set<Integer> literals) {

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
        Set<Integer> unassignedLiterals = clauses.stream()
                .filter(clause -> !clause.clauseSatisfied)
                .flatMap(clause -> clause.unassignedLiterals.stream()).collect(Collectors.toSet());


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

    private Set<Integer> findPureLiterals(List<Clause> clauses) {
        Set<Integer> pureLiterals = new HashSet<>();
        Set<Integer> allAliveLiterals = new HashSet<>();
        for (Clause clause : clauses) {
            if (!clause.clauseSatisfied) {
                allAliveLiterals.addAll(clause.unassignedLiterals);
            }
        }
        for (Integer literal : allAliveLiterals) {
            if (!allAliveLiterals.contains(-literal)) {
                pureLiterals.add(literal);
            }
        }
        return pureLiterals;
    }

    private void pureLiteralsProcess(List<Clause> clauses, Set<Integer> pureLiterals) {
        for (Integer literal : pureLiterals) {
            for (Clause clause : clauses) {
                if (clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                }
            }
        }
    }


    private void applyStep(final List<Clause> clauses, final Integer literal) {
        for(Clause clause : clauses) {
            if(!clause.clauseSatisfied) {
                if(clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                } else if(clause.unassignedLiterals.contains(-literal)) {
                    clause.unassignedLiterals.remove((Integer) (-literal));
                    clause.deadLiterals.add(-literal);
                }
            }
        }
    }


    private void undoStep(final List<Clause> clauses, final Integer literal) {
        for (Clause clause : clauses) {
            if (clause.clauseSatisfied && clause.unassignedLiterals.contains(literal)) {
                clause.clauseSatisfied = false;
            }
            if (clause.deadLiterals.contains(-literal)) {
                clause.deadLiterals.remove((Integer) (-literal));
                clause.unassignedLiterals.add(-literal);
            }
        }
    }

    private Set<Integer> findUnitClauses(final List<Clause> clauses) {
        Set<Integer> unitPropagation = new HashSet<>();
        for(Clause clause : clauses) {
            if(clause.isUnitClause()) {
                unitPropagation.add(clause.unassignedLiterals.get(0));
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


        public static class Clause {
        private List<Integer> unassignedLiterals = new ArrayList<>();
        private List<Integer> deadLiterals = new ArrayList<>();
        private boolean clauseSatisfied = false;
        private Clause(String inputLine) {
            String[] line = inputLine.substring(0, inputLine.length() - 2).split("\\s");
            for (String literal : line) {
                unassignedLiterals.add(Integer.parseInt(literal));
            }
        }
        boolean isUnitClause() {
            return !clauseSatisfied && unassignedLiterals.size() == 1;
        }
    }
}
