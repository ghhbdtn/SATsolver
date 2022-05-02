import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

public class SATsolver {

    static class Solution {
        private final Set<Integer> solution;
        public Solution(Set<Integer> solution) {
            this.solution = solution.stream().sorted(Comparator.comparingInt(Math::abs)).collect(Collectors.toCollection(LinkedHashSet::new));
        }

        public boolean haveSolution() {
            return !this.solution.isEmpty();
        }

        public int[] getModel() {
            return this.solution.stream().mapToInt(Integer::intValue).toArray();
        }

        @Override
        public String toString() {
            return this.solution.stream().map(String::valueOf).collect(Collectors.joining(" ")) + " 0";
        }
    }

    public static Set<Integer> variables = new HashSet<>();
    private final String inputName;

    public SATsolver(String inputName) {
        this.inputName = inputName;
    }

    public int[] solve() throws Exception {

        final List<Clause> clauses = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(this.inputName));
        String line = br.readLine();
        while (line != null) {
            if (line.endsWith(" 0")) {
                Clause clause = new Clause(line);
                clauses.add(clause);
            }
            line = br.readLine();
        }

        Set<Integer> literals = new HashSet<>();

        Solution solution = new Solution(DPLL(clauses, literals));

        if (solution.haveSolution()) {
            System.out.println("SAT");
            System.out.println(solution);
        } else {
            System.out.println("UNSAT");
        }
        return solution.getModel();
    }

    private Set<Integer> DPLL(List<Clause> clauses, Set<Integer> literals) {

        // Check if we have no more clauses that are unsatisfied:
        if (clauses.stream().allMatch(clause -> clause.clauseSatisfied)) {
            // SAT
            return literals;
        }

        Set<Integer> newLiterals = new HashSet<>();

//        Set<Integer> pureLiterals = findPureLiterals();
//        if (pureLiterals.size() > 0) {
//            pureLiteralsProcess(clauses, pureLiterals);
//            literals.addAll(pureLiterals);
//        }

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
                    return new HashSet<>();
                }
                if(!applyStep(clauses, unit)) {
                    for (Integer literal : newLiterals) {
                        undoStep(clauses, literal);
                    }
                    return new HashSet<>();
                } else newLiterals.add(unit);
            }
            unitPropagation.removeAll(newLiterals);

            unitPropagation.addAll(findUnitClauses(clauses));
        }

        // Get all the unassignedLiterals from the alive clauses:
        Set<Integer> unassignedLiterals = clauses.stream()
                .filter(clause -> !clause.clauseSatisfied).flatMap(clause -> clause.unassignedLiterals.stream()).collect(Collectors.toSet());


        if (unassignedLiterals.isEmpty()) {
            newLiterals.addAll(literals);
            return newLiterals;
        }


        for(Integer decisionLiteral : unassignedLiterals) {
//            boolean q = applyStep(clauses, decisionLiteral);
//            if (!q) {
//                undoStep(clauses, decisionLiteral);
//                return new HashSet<>();
//            }
            applyStep(clauses, decisionLiteral);
            Set<Integer> deeperAssignedLiterals = new HashSet<>();
            deeperAssignedLiterals.addAll(literals);
            deeperAssignedLiterals.addAll(newLiterals);
            deeperAssignedLiterals.add(decisionLiteral);


            Set<Integer> s = DPLL(clauses, deeperAssignedLiterals);
            if (s.isEmpty()) {
                undoStep(clauses, decisionLiteral);
            } else {
                return  s;
            }
        }

        // Undo all we've done this step:
        for(Integer literal : newLiterals) {
            undoStep(clauses, literal);
        }
        return new HashSet<>();
    }

    private Set<Integer> findPureLiterals() {
        Set<Integer> pureLiterals = new HashSet<>(variables.stream().filter(p -> !variables.contains(-p)).collect(Collectors.toSet()));
        return pureLiterals;
    }

    private void pureLiteralsProcess(List<Clause> clauses, Set<Integer> pureLiterals) {
        for (Integer literal : pureLiterals) {
            for (Clause clause : clauses) {
                if (!clause.clauseSatisfied && clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                }
            }
        }
    }


    private boolean applyStep(final List<Clause> clauses, final Integer literal) {
        for(Clause clause : clauses) {
            if(!clause.clauseSatisfied) {
                if(clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                } else if(clause.unassignedLiterals.contains(-literal)) {
                     if (clause.unassignedLiterals.size() != 1) {
                         clause.unassignedLiterals.remove((Integer) (-literal));
                         clause.deadLiterals.add(-literal);
                     }
                     else {
                         if (clause.unassignedLiterals.get(0) == -literal) {
                             return false;
                         }
                         else {
                             return true;
                         }
                     }
                }
            }
        }
        return true;
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


        public static class Clause {
            private List<Integer> unassignedLiterals = new ArrayList<>();
            private List<Integer> deadLiterals = new ArrayList<>();
            private boolean clauseSatisfied = false;
            private Clause(String inputLine) {
                String[] line = inputLine.substring(0, inputLine.length() - 2).split("\\s+");
                for (String literal : line) {
                    int num = Integer.parseInt(literal);
                    unassignedLiterals.add(num);
                    variables.add(num);
                }
            }
            boolean isUnitClause() {
                return !clauseSatisfied && unassignedLiterals.size() == 1;
            }
    }
}
