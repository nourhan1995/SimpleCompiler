import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.StringTokenizer;

public class DFA extends Automaton{

	public DFA() {
		super();
	}

	public static void main(String[] args) throws IOException {

		BufferedReader br = new BufferedReader(new FileReader("in1.in"));
		PrintWriter p = new PrintWriter("out1_sample.out");
		int k = 0;
		while (br.ready()) {
//			System.out.println("Case: " + k++);
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

	}

	// Invalid start state -- done
	// Invalid accept state m -- done
	// Missing transition for state s
	// Incomplete Transition s,1 -- done
	// Invalid transition s,q1,10 input 10 is not in the alphabet -- done
	// Missing transition for state q2

	// Invalid input string at 11

	public boolean checkComplete() {
		// checks that all states have transitions with each element of the
		// alphabet
		Enumeration<String> keys = stateMap.keys();
		while(keys.hasMoreElements()){
			String cur = keys.nextElement();
			HashSet<String> tmp = new HashSet<>();
			for(Transition<String> t: stateMap.get(cur))
				tmp.add(t.transition);
			if (tmp.size() != alphabet.size()) {
				if (!ignore) {
					errMsg.add("Missing transition for state " + cur);
					ignore = true;
				}
				return false;
			}
		}
		return true;
	}

	public String runDFA(String input) {
		if (ignore)
			return "Ignored";
		curState = start;
		StringTokenizer s = new StringTokenizer(input, ",");
		while (s.hasMoreTokens()) {
			String cur = s.nextToken();
			if (alphabet.contains(cur)) {
				for (Transition<String> trans : stateMap.get(curState.stateName)) {
					if (trans.transition.equals(cur)) {
						curState = trans.state;
						break;
					}
				}
			} else {
				return "Invalid input string at " + cur;
			}
		}
		return curState.accept ? "Accepted" : "Rejected";

	}
	
	public String toString(){
		StringBuilder res = new StringBuilder();
		res.append("Equivalent DFA:\n");
		ArrayList<String> acceptStates = new ArrayList<>();
		Enumeration<String> keys = states.keys();
		while(keys.hasMoreElements()){
			String cur = keys.nextElement();
			if(!cur.equals("Dead"))
				res.append(cur + ",");
			if(states.get(cur).accept)
				acceptStates.add(cur);
		}
		res.append("Dead\n");
		int i = 0;
		for(String a: acceptStates){
			if(i != 0)
				res.append(",");
			res.append(a);
			i++;
		}
		i = 0;
		res.append("\n");
		for(String a: alphabet){
			if(i != 0)
				res.append(",");
			res.append(a);
			i++;
		}
		res.append("\n");
		res.append(start.stateName + "\n");
		Enumeration<String> k = states.keys();
		while(k.hasMoreElements()){
//			State<String> s = statesIds.get(k.nextElement());
			String cur = k.nextElement();
			for(Transition<String> t: stateMap.get(cur)){
				res.append(cur + "," + t.state.stateName + "," + t.transition + "#");
			}
		}
//		res.append("\n");
//		res.append(stateMap.toString());
		return res.toString();
	}

}
