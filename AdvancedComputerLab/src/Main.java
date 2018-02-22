import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class Main {
	
	public static void main(String[] args) throws IOException {

		createDFA("in.in", "DFA_out.out");
		createDFA("in1.in", "DFA_out1.out");

		createNFA("NFA_in.in", "NFA_out.out");
		createNFA("NFA_in1.in", "NFA_out1.out");

	}

	public static void createDFA(String input, String output) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter p = new PrintWriter(output);
		while (br.ready()) {
			// System.out.println("Case: " + k++);
			DFA dfa = new DFA();
			dfa.addStates(br.readLine());
			dfa.setAcceptStates(br.readLine());
			dfa.setAlphabet(br.readLine());
			dfa.setStartState(br.readLine());
			StringTokenizer s = new StringTokenizer(br.readLine(), "#");
			while (s.hasMoreTokens())
				dfa.addTransition(s.nextToken().split(","));
			dfa.checkComplete();
			if (dfa.ignore)
				p.println(dfa.errMsg.get(0));
			else
				p.println("DFA constructed");
			s = new StringTokenizer(br.readLine(), "#");
			while (s.hasMoreTokens()) {
				p.println(dfa.runDFA(s.nextToken()));
			}
			p.println();
			br.readLine();
		}
		p.flush();
		p.close();
		br.close();

	}

	public static void createNFA(String input, String output) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader(input));
		PrintWriter p = new PrintWriter(output);
		int k = 0;
		while (br.ready()) {
			System.out.println();
			System.out.println("CASE: " + k++);
			NFA nfa = new NFA();

			nfa.addStates(br.readLine());
			nfa.setAcceptStates(br.readLine());
			String alpha = br.readLine();
			nfa.setAlphabet(alpha);
			nfa.equiDFA.setAlphabet(alpha);
			System.out.println("DFA alphabet: " + nfa.equiDFA.alphabet);
			nfa.setStartState(br.readLine());
			StringTokenizer s = new StringTokenizer(br.readLine(), "#");
			while (s.hasMoreTokens())
				nfa.addTransition(s.nextToken().split(","));
			String c = br.readLine();
			if (nfa.ignore) {
				for (String e : nfa.errMsg)
					p.println(e);
				p.println("DFA Construction skipped and inputs are ignored");
			} else {
				p.println("NFA constructed");
				// System.out.println(nfa.toString());
				nfa.convertNFAtoDFA();
				nfa.equiDFA.checkComplete();
				p.println(nfa.equiDFA.toString());
				p.println(c);
				nfa.equiDFA.checkComplete();
				if (nfa.equiDFA.ignore)
					p.println(nfa.equiDFA.errMsg);
				else {
					p.println("DFA constructed");
				}
				s = new StringTokenizer(c, "#");
				while (s.hasMoreTokens()) {
					p.println(nfa.equiDFA.runDFA(s.nextToken()));
				}
			}
			p.println();
			br.readLine();
		}
		p.flush();
		p.close();
		br.close();

	}

}
