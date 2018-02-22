import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class NFA extends Automaton {

	DFA equiDFA;

	public NFA() {
		super();
		equiDFA = new DFA();
		alphabet.add("$");
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("NFA_in.in"));
		PrintWriter p = new PrintWriter("NFA_out_sample.out");
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

	public boolean convertNFAtoDFA() {
		HashSet<String> visited = new HashSet<>();
		equiDFA.addState("Dead");
		for (String alpha : alphabet) {
			if (!alpha.equals("$")) {
				String[] s = { "Dead", "Dead", alpha };
				equiDFA.addTransition(s);
			}
		}
		visited.add("Dead");
		Queue<String> q = new LinkedList<>();
		HashSet<String> tmp = new HashSet<>();
		tmp.add(start.stateName);
		q.add(createState(epsillonClosure(tmp)));
		while (!q.isEmpty()) {
			System.out.println(q.toString());
			State<String> cur = equiDFA.states.get(q.poll());
			System.out.println("State being processed: " + cur.toString());
			visited.add(cur.stateName);
			if (equiDFA.start == null) {
				equiDFA.start = cur;
				equiDFA.start.start = true;
				equiDFA.curState = cur;
			}
			for (String alpha : alphabet) {
				tmp = new HashSet<>();
				if (!alpha.equals("$")) {
					// get all the transitions with the alphabet alpha for all
					// the states present in this dfa state
					for (String s : cur.elementsStates) {
						for (Transition<String> t : stateMap.get(s)) {
							if (t.transition.equals(alpha)) {
								tmp.add(t.state.stateName);
							}
						}
					}
				}
				if (tmp.size() != 0) {
					String i = createState(epsillonClosure(tmp));
					if (!visited.contains(i))
						q.add(i);
					String[] s = { cur.stateName, i, alpha };
					boolean flag = equiDFA.addTransition(s);
					System.out.println(flag + " added transition: " + Arrays.toString(s));
				} else {
					String[] s = { cur.stateName, "Dead", alpha };
					boolean flag = equiDFA.addTransition(s);
					System.out.println(flag + " added transition: " + Arrays.toString(s));
				}

			}
		}
		return true;
	}

	public HashSet<String> epsillonClosure(HashSet<String> states) {
		// get all the states that have epsillon transitions to others
		int tmp = 0;
		HashSet<String> newStates = new HashSet<>(states);
		for (String s : states) {
			for (Transition<String> t : stateMap.get(s)) {
				if (t.transition.equals("$") && !states.contains(t.state.stateName)) {
					// epsillon transition found
					newStates.add(t.state.stateName);
					tmp++;
				}
			}
		}
		if (tmp != 0)
			return epsillonClosure(newStates);
		else
			return newStates;
	}

	public String createState(HashSet<String> s) {

		ArrayList<String> stateNames = new ArrayList<>();
		boolean accept = false;
		for (String state : s) {
			stateNames.add(state);
			if (states.get(state).accept)
				accept = true;
		}
		Collections.sort(stateNames);
		StringBuilder res = new StringBuilder(stateNames.get(0));
		for (int i = 1; i < stateNames.size(); i++)
			res.append("*" + stateNames.get(i));
		boolean flag = equiDFA.addState(res.toString());
		if (flag) {
			// System.out.println("Successfully added state " + res.toString());
			State<String> cur = equiDFA.states.get(res.toString());
			cur.elementsStates = s;
			if (accept)
				cur.accept = true;
			return cur.stateName;
		} else {
			// System.out.println("FAILED to add state " + res.toString());
			return res.toString();
		}
	}

	public String toString() {
		StringBuilder res = new StringBuilder();
		// res.append("start state: " + start.stateName + "\n");
		return res.toString();
	}

}
