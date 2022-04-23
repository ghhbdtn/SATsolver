import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

public class SATLauncher {
    @Argument(required = true, usage = "InputName")
    private String inputName;

    public static void main(String[] args) throws Exception {
        new SATLauncher().launch(args);
        //SATsolver solver = new SATsolver("src/inputtest.txt");
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

        SATsolver solver = new SATsolver(inputName);
    }
}
