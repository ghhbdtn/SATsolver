import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

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

    private final String inputName;
    //public static HashMap<Integer, Integer> watch = new HashMap<>();

    public SATsolver(String inputName) {
        this.inputName = inputName;
    }

    public int[] solve() throws Exception {

        final List<Clause> clauses = new ArrayList<>();
        HashMap<Integer, Integer> watch = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(this.inputName));
        String line = br.readLine();
        while (line != null) {
            if (line.endsWith(" 0")) {
                Clause clause = new Clause(line, watch);
                clauses.add(clause);
            }
            line = br.readLine();
        }

        Set<Integer> pureLiterals = findPureLiterals(clauses);
        if (pureLiterals.size() > 0) {
            pureLiteralsProcess(clauses, pureLiterals, watch);
        }

        Set<Integer> literals = new HashSet<>(pureLiterals);

        Solution solution = new Solution(DPLL(clauses, literals, watch));

        if (solution.haveSolution()) {
            System.out.println("SAT");
            System.out.println(solution);
        } else {
            System.out.println("UNSAT");
        }
        return solution.getModel();
    }

    private Set<Integer> DPLL(List<Clause> clauses, Set<Integer> literals, HashMap<Integer, Integer> watch) {

        if (clauses.stream().allMatch(clause -> clause.clauseSatisfied)) {
            return literals;
        }
//        if (clauses.stream().anyMatch(clause -> clause.emptyClause)) {
//            return new HashSet<>();
//        }

        //literals.stream().anyMatch(l -> literals.contains(-l))

        Set<Integer> newLiterals = new HashSet<>();

        Set<Integer> unitPropagation = findUnitClauses(clauses);

        while(unitPropagation.size() > 0) {
            for (Integer unit : unitPropagation) {

                if (unitPropagation.contains(-unit) || clauses.stream().anyMatch(clause -> clause.emptyClause)) {
                    for (Integer literal : newLiterals) {
                        undoStep(clauses, literal, watch);
                    }

                    return new HashSet<>();
                }
                newLiterals.add(unit);
                applyStep(clauses, unit, watch);
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

        applyStep(clauses, deepLit, watch);
        literals.addAll(newLiterals);
        literals.add(deepLit);
        Set<Integer> s = DPLL(clauses, literals, watch);
        if (s.isEmpty()) {
            literals.remove(deepLit);
            undoStep(clauses, deepLit, watch);
        } else {
            return  s;
        }
        applyStep(clauses, -deepLit, watch);
        literals.add(-deepLit);
        Set<Integer> s2 = DPLL(clauses, literals, watch);
        if (s2.isEmpty()) {
            literals.remove(-deepLit);
            undoStep(clauses, -deepLit, watch);
        } else {
            return  s2;
        }


        literals.removeAll(newLiterals);
        for(Integer literal : newLiterals) {
            undoStep(clauses, literal, watch);
        }
        return new HashSet<>();
    }

    private int chooseLiteral(List<Clause> clauses, HashMap<Integer, Integer> watch) {
        Set<Integer> un = clauses.stream().filter(clause -> !clause.clauseSatisfied).
                flatMap(clause -> clause.unassignedLiterals.stream()).collect(Collectors.toSet());
        int literal = 0;
        int c = 0;
        for (Integer el : un){
            if (watch.get(abs(el)) > c ) {
                c = watch.get(abs(el));
                literal = abs(el);
            }
        }
        return literal;
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

    private void pureLiteralsProcess(List<Clause> clauses, Set<Integer> pureLiterals, HashMap<Integer, Integer> watch) {
        for (Integer literal : pureLiterals) {
            for (Clause clause : clauses) {
                if (!clause.clauseSatisfied && clause.unassignedLiterals.contains(literal)) {
                    for (Integer num : clause.unassignedLiterals){
                        watch.put(abs(num), watch.get(abs(num)) - 1);
                    }
                    clause.assignedLiterals.add(literal);
                    clause.unassignedLiterals.remove(literal);
                    clause.clauseSatisfied = true;
                }
            }
        }
    }


    private void applyStep(final List<Clause> clauses, final Integer literal, HashMap<Integer, Integer> watch) {
        for(Clause clause : clauses) {
            if(!clause.clauseSatisfied) {
                if(clause.unassignedLiterals.contains(literal)) {
                    clause.clauseSatisfied = true;
                    clause.assignedLiterals.add(literal);
                    for (Integer num : clause.unassignedLiterals){
                        watch.put(abs(num), watch.get(abs(num)) - 1);
                    }
                    clause.unassignedLiterals.remove(literal);
                } else if(clause.unassignedLiterals.contains(-literal)) {
                    if (clause.unassignedLiterals.size() == 1) clause.emptyClause = true;
                    clause.unassignedLiterals.remove((Integer) (-literal));
                    clause.deadLiterals.add(-literal);
                    watch.put(abs(-literal), watch.get(abs(-literal)) - 1);
                }
            }
        }
    }


    private void undoStep(final List<Clause> clauses, final Integer literal, HashMap<Integer, Integer> watch) {
        for (Clause clause : clauses) {
            if (clause.clauseSatisfied && clause.assignedLiterals.contains(literal) && clause.assignedLiterals.size() == 1) {
                clause.clauseSatisfied = false;
                clause.assignedLiterals.remove(literal);
                clause.unassignedLiterals.add(literal);
                for (Integer num : clause.unassignedLiterals){
                    watch.put(abs(num), watch.get(abs(num)) + 1);
                }
            } else  if ((clause.clauseSatisfied && clause.assignedLiterals.contains(literal))) {
                clause.assignedLiterals.remove(literal);
                clause.unassignedLiterals.add(literal);
            }
            if (clause.deadLiterals.contains(-literal)) {
                if (clause.unassignedLiterals.size() == 0) clause.emptyClause = false;
                clause.deadLiterals.remove((Integer) (-literal));
                clause.unassignedLiterals.add(-literal);
                watch.put(abs(-literal), watch.get(abs(-literal)) + 1);
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
        private List<Integer> assignedLiterals = new ArrayList<>();
        private List<Integer> deadLiterals = new ArrayList<>();
        private boolean clauseSatisfied = false;
        private boolean emptyClause = false;
        private Clause(String inputLine, HashMap<Integer, Integer> watch) {
            String[] line = inputLine.substring(0, inputLine.length() - 2).split("\\s+");
            for (String literal : line) {
                int num = Integer.parseInt(literal);
                unassignedLiterals.add(num);
                if (!watch.containsKey(abs(num))) watch.put(abs(num), 1);
                else watch.put(abs(num), watch.get(abs(num)) + 1);
            }
        }
        boolean isUnitClause() {
            return !clauseSatisfied && unassignedLiterals.size() == 1;
        }
    }
//    private void pureLiteralsProcess(List<Clause> clauses, Set<Integer> pureLiterals, HashMap<Integer, Integer> watch) {
//        for (Integer literal : pureLiterals) {
//            for (Clause clause : clauses) {
//                if (!clause.clauseSatisfied && clause.unassignedLiterals.contains(literal)) {
//                    for (Integer num : clause.unassignedLiterals){
//                        watch.put(abs(num), watch.get(abs(num)) - 1);
//                    }
//                    clause.clauseSatisfied = true;
//                }
//            }
//        }
//    }
//
//
//    private void applyStep(final List<Clause> clauses, final Integer literal, HashMap<Integer, Integer> watch) {
//        for(Clause clause : clauses) {
//            if(!clause.clauseSatisfied) {
//                if(clause.unassignedLiterals.contains(literal)) {
//                    clause.clauseSatisfied = true;
//                    for (Integer num : clause.unassignedLiterals){
//                        watch.put(abs(num), watch.get(abs(num)) - 1);
//                    }
//                } else if(clause.unassignedLiterals.contains(-literal)) {
//                    if (clause.unassignedLiterals.size() == 1) clause.emptyClause = true;
//                    clause.unassignedLiterals.remove((Integer) (-literal));
//                    clause.deadLiterals.add(-literal);
//                    watch.put(abs(-literal), watch.get(abs(-literal)) - 1);
//                }
//            }
//        }
//    }
//
//
//    private void undoStep(final List<Clause> clauses, final Integer literal, HashMap<Integer, Integer> watch) {
//        for (Clause clause : clauses) {
//            if (clause.clauseSatisfied && clause.unassignedLiterals.contains(literal)) {
//                clause.clauseSatisfied = false;
//                for (Integer num : clause.unassignedLiterals){
//                    watch.put(abs(num), watch.get(abs(num)) + 1);
//                }
//            }
//            if (clause.deadLiterals.contains(-literal)) {
//                if (clause.unassignedLiterals.size() == 0) clause.emptyClause = false;
//                clause.deadLiterals.remove((Integer) (-literal));
//                clause.unassignedLiterals.add(-literal);
//                watch.put(abs(-literal), watch.get(abs(-literal)) + 1);
//            }
//        }
//    }
//
//    private Set<Integer> findUnitClauses(final List<Clause> clauses) {
//        Set<Integer> unitPropagation = new HashSet<>();
//        for(Clause clause : clauses) {
//            if(clause.isUnitClause()) {
//                unitPropagation.add(clause.unassignedLiterals.get(0));
//            }
//        }
//        return unitPropagation;
//    }
//
//
//    public static class Clause {
//        private List<Integer> unassignedLiterals = new ArrayList<>();
//        private List<Integer> deadLiterals = new ArrayList<>();
//        private boolean clauseSatisfied = false;
//        private boolean emptyClause = false;
//        private Clause(String inputLine, HashMap<Integer, Integer> watch) {
//            String[] line = inputLine.substring(0, inputLine.length() - 2).split("\\s+");
//            for (String literal : line) {
//                int num = Integer.parseInt(literal);
//                unassignedLiterals.add(num);
//                if (!watch.containsKey(abs(num))) watch.put(abs(num), 1);
//                else watch.put(abs(num), watch.get(abs(num)) + 1);
//            }
//        }
//        boolean isUnitClause() {
//            return !clauseSatisfied && unassignedLiterals.size() == 1;
//        }
//    }
}
