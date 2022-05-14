import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.util.Arrays;

public class SATLauncher {
    @Argument(required = true, usage = "InputName")
    private String inputName;

    public static void main(String[] args) throws Exception {
        new SATLauncher().launch(args);
    }

    public void launch(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java - jar InputFileName");
            parser.printUsage(System.err);
            return;
        }

        int[] solution = new SATsolver(inputName).solve();

        if (solution.length != 0) {
            System.out.println("SAT");
            System.out.println(Arrays.toString(solution));
        } else {
            System.out.println("UNSAT");
        }
    }
}
