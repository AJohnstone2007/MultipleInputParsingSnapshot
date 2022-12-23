
/******************************************************************************
 * ARTV3TestGenerated.java
 *
 * This is a test harness for V3 ART generated Java parsers
 *
 * (c) Adrian Johnstone 2013-22
 *****************************************************************************/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;

import uk.ac.rhul.cs.csle.art.core.ARTUncheckedException;
import uk.ac.rhul.cs.csle.art.term.ITerms;
import uk.ac.rhul.cs.csle.art.term.ITermsLowLevelAPI;
import uk.ac.rhul.cs.csle.art.util.text.ARTText;

public class ARTV3TestGenerated {
  public static void main(String[] args) throws FileNotFoundException {
    String inputFilename = null;
    ARTGeneratedParser parser = new ARTGeneratedParser(new ARTGeneratedLexer());

    if (args.length == 0) {
      System.out.println("No arguments supplied\n");
      System.exit(1);
    }

    for (String arg : args)
      if (arg.charAt(0) != '!')
        inputFilename = arg;
      else
        parser.artDirectives.set(arg.substring(1, arg.length()), true);

    if (inputFilename == null) throw new ARTUncheckedException("No input file specified");
    String input = "";

    try {
      input = ARTText.readFile(inputFilename);
      parser.artParse(input);
    } catch (ARTUncheckedException e) {
      System.out.println("Fatal error: " + e.getMessage());
    }

    long parseMillis = parser.artParseCompleteTime - parser.artLexCompleteTime;

    double throughPut = ((double) (input.length()) / ((double) parseMillis));

    System.out.println((parser.artIsInLanguage ? "** Accept " : "** Reject ") + inputFilename + " " + input.length() + " bytes in " + parseMillis + "ms, "
        + String.format("%,.2f", throughPut) + "kByte/s" + (parser.tweSet == null ? "" : " with choose time " + parser.tweSet.chooserMillis + "ms"));
    System.out.println((parser.artIsInLanguage ? "** Data GLL,Accept," : "Reject,") + inputFilename + "," + input.length() + "," + parseMillis + ","
        + String.format("%,.2f", throughPut) + "kByte/s," + (parser.tweSet == null ? "" : parser.tweSet.chooserMillis));

    if (parser.artDirectives.b("tweFromSPPF")) parser.artProcessPostParseTWE(true);

    if (parser.artDirectives.b("tweTokenWrite") && parser.tweSet != null) parser.tweSet.printTWESet(new PrintStream(new File("ARTTokenGrammar.str")), false);

    if (parser.artDirectives.b("parseCounts")) {
      parser.artComputeParseCounts();
      parser.artLog(inputFilename, true);
      parser.artLog(inputFilename, false);
    }

    if (parser.artDirectives.b("sppfShow")) parser.artWriteSPPF("sppf.dot", parser.artRenderKindSPPFFull);
    if (parser.artDirectives.b("gssShow")) parser.artWriteSPPF("gss.dot", parser.artRenderKindGSS);
    if (parser.artDirectives.b("treeShow")) parser.artWriteRDT("rdt.dot");
    if (parser.artDirectives.b("treePrint")) parser.artPrintRDT();
    if (parser.artDirectives.b("termWrite") || parser.artDirectives.b("termPrint")) {
      // Use new term library to create an ITerm from a derivation
      ITerms iTerms = new ITermsLowLevelAPI();
      String termString = iTerms.toString(parser.artDerivationAsTerm(iTerms));

      if (parser.artDirectives.b("termPrint")) System.out.println(termString);

      if (parser.artDirectives.b("termWrite")) {
        PrintWriter pw = new PrintWriter("artTerm.art");
        pw.println(termString);
        pw.close();
      }
    }

  }
}
