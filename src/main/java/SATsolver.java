import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class SATsolver {

    static class Solution {
        private final Set<Integer> solution;
        public Solution(Set<Integer> solution) {
            this.solution = solution.stream().sorted(Comparator.comparingInt(Math::abs))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        public int[] getModel() {
            return this.solution.stream().mapToInt(Integer::intValue).toArray();
        }
    }

    private final String inputName;
    //public static HashMap<Integer, Integer> watch = new HashMap<>();

    public SATsolver(String inputName) {
        this.inputName = inputName;
    }

    public int[] solve() throws Exception {

        ParseFile file = new ParseFile(inputName);

        List<Clause> clauses = file.getClauses();
        Map<Integer, List<Clause>> watch = file.getWatch();

        Set<Integer> pureLiterals = findPureLiterals(clauses);
        if (pureLiterals.size() > 0) {
            pureLiteralsProcess(pureLiterals, watch);
        }

        Set<Integer> literals = new HashSet<>(pureLiterals);

        Solution solution = new Solution(DPLL(clauses, literals, watch));

        return solution.getModel();
    }

    private Set<Integer> DPLL(List<Clause> clauses, Set<Integer> literals, Map<Integer, List<Clause>> watch) {

        if (clauses.stream().allMatch(clause -> clause.clauseSatisfied)) {
            return literals;
        }

        Set<Integer> newLiterals = new HashSet<>();

        Set<Integer> unitPropagation = findUnitClauses(clauses);

        while(unitPropagation.size() > 0) {
            for (Integer unit : unitPropagation) {

                if (unitPropagation.contains(-unit) || clauses.stream().anyMatch(clause -> clause.emptyClause)) {
                    for (Integer literal : newLiterals) {
                        undoStep(literal, watch);
                    }

                    return new HashSet<>();
                }
                newLiterals.add(unit);
                applyStep(unit, watch);
            }
            unitPropagation.removeAll(newLiterals);

            unitPropagation.addAll(findUnitClauses(clauses));
        }


        literals.addAll(newLiterals);
        Integer deepLit = chooseLiteral(clauses, watch);

        if (deepLit == 0 && clauses.stream().allMatch(clause -> clause.clauseSatisfied)) {
            return literals;
        } else if (deepLit == 0) {
            return new HashSet<>();
        }

        applyStep(deepLit, watch);

        literals.add(deepLit);
        Set<Integer> s = DPLL(clauses, literals, watch);
        if (s.isEmpty()) {
            literals.remove(deepLit);
            undoStep(deepLit, watch);
        } else {
            return  s;
        }
        applyStep(-deepLit, watch);
        literals.add(-deepLit);
        Set<Integer> s2 = DPLL(clauses, literals, watch);
        if (s2.isEmpty()) {
            literals.remove(-deepLit);
            undoStep(-deepLit, watch);
        } else {
            return  s2;
        }


        literals.removeAll(newLiterals);
        for(Integer literal : newLiterals) {
            undoStep(literal, watch);
        }
        return new HashSet<>();
    }

    private int chooseLiteral(List<Clause> clauses, Map<Integer, List<Clause>> watch) {
        Set<Integer> un = clauses.stream().filter(clause -> !clause.clauseSatisfied).
                flatMap(clause -> clause.unassignedLiterals.stream()).collect(Collectors.toSet());
        int literal = 0;
        int c = 0;
        for (Integer el : un){
            if (watch.get(abs(el)).stream().filter(cl -> !cl.clauseSatisfied).count() > c ) {
                c = watch.get(abs(el)).size();
                literal = abs(el);
            }
        }
        return literal;
    }

    private Set<Integer> findPureLiterals(List<Clause> clauses) {
        Set<Integer> pureLiterals;
        Set<Integer> allLiterals = clauses.stream().flatMap(clause -> clause.unassignedLiterals.stream())
                .collect(Collectors.toSet());
        pureLiterals = allLiterals.stream().filter(l -> !allLiterals.contains(-l)).collect(Collectors.toSet());
        return pureLiterals;
    }

    private void pureLiteralsProcess(Set<Integer> pureLiterals, Map<Integer, List<Clause>> watch) {
        for (Integer literal : pureLiterals) {
            for (Clause clause : watch.get(abs(literal))) {
                clause.assignedLiterals.add(literal);
                clause.unassignedLiterals.remove(literal);
                clause.clauseSatisfied = true;
            }
        }
    }


    private void applyStep(final Integer literal, Map<Integer, List<Clause>> watch) {
        for(Clause clause : watch.get(abs(literal))) {
            if(!clause.clauseSatisfied) {
                if(clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                    clause.assignedLiterals.add(literal);
                    clause.unassignedLiterals.remove(literal);
                } else {
                    if (clause.unassignedLiterals.size() == 1) clause.emptyClause = true;
                    clause.unassignedLiterals.remove((Integer) (-literal));
                    clause.deadLiterals.add(-literal);
                }
            }
        }
    }


    private void undoStep(final Integer literal, Map<Integer, List<Clause>> watch) {
        for (Clause clause : watch.get(abs(literal))) {
            if (clause.clauseSatisfied && clause.assignedLiterals.contains(literal) && clause.assignedLiterals.size() == 1) {
                clause.clauseSatisfied = false;
                clause.assignedLiterals.remove(literal);
                clause.unassignedLiterals.add(literal);
            } else  if ((clause.clauseSatisfied && clause.assignedLiterals.contains(literal))) {
                clause.assignedLiterals.remove(literal);
                clause.unassignedLiterals.add(literal);
            }
            if (clause.deadLiterals.contains(-literal)) {
                if (clause.unassignedLiterals.size() == 0) clause.emptyClause = false;
                clause.deadLiterals.remove((Integer) (-literal));
                clause.unassignedLiterals.add(-literal);
            }
        }
    }

    private Set<Integer> findUnitClauses(List<Clause> clauses) {
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
        private List<Integer> assignedLiterals = new ArrayList<>();
        private List<Integer> deadLiterals = new ArrayList<>();
        private boolean clauseSatisfied = false;
        private boolean emptyClause = false;
        public Clause(List<Integer> literals, Map<Integer, List<Clause>> watch) {
            for (Integer literal : literals) {
                unassignedLiterals.add(literal);
                if (!watch.containsKey(abs(literal))) watch.put(abs(literal), new ArrayList<>());
                watch.get(abs(literal)).add(this);
            }

        }
        boolean isUnitClause() {
            return !clauseSatisfied && unassignedLiterals.size() == 1;
        }
    }
}
