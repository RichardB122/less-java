import java.io.IOException;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class LJCompiler {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: LJCompiler <Files>");
            System.exit(0);
        }

        for (String s: args) {
            if (!s.endsWith("lj")) {
                System.err.println("Only accepts .lj files");
                System.exit(0);
            }
        }

        LJLexer lexer = null;
        LJParser parser = null;
        ParseTree pTree = null;
        LJASTConverter converter = null;
        ParseTreeWalker walker = null;
        ASTProgram program = null;
        PrintDebugTree printTree = null;

        try {
            for (String s: args) {
                lexer = new LJLexer(new ANTLRFileStream(s));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(0);
        }

        parser = new LJParser(new CommonTokenStream(lexer));
        converter = new LJASTConverter();
        walker = new ParseTreeWalker();

        pTree = parser.program();
        walker.walk(converter, pTree);

        program = converter.getAST();
        printTree = new PrintDebugTree();

        program.traverse(printTree);
    }
}