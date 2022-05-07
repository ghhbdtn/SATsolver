import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class SATLauncher {
    @Argument(required = true, usage = "InputName")
    private String inputName;
    public static long startTime;
    public static void main(String[] args) throws Exception {
        startTime = System.currentTimeMillis();
        //new SATLauncher().launch(args);
        int[] solver = new SATsolver("src/input/inputtest6.txt").solve();
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

        int[] solver = new SATsolver(inputName).solve();
    }
}
