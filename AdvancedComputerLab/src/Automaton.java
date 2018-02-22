import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class Automaton implements AutomatonUtils {

	Hashtable<String, ArrayList<Transition<String>>> stateMap;
	Hashtable<String, State<String>> states;
	HashSet<String> alphabet;
	boolean ignore;
	State<String> start;
	State<String> curState;
	ArrayList<String> errMsg;
	ArrayList<State<String>> acceptStates;

	public Automaton() {
		states = new Hashtable<>();
		stateMap = new Hashtable<>();
		alphabet = new HashSet<>();
		ignore = false;
		errMsg = new ArrayList<>();
		acceptStates = new ArrayList<>();
	}

	@Override
	public boolean addStates(String input) {
		StringTokenizer s = new StringTokenizer(input, ",");
		while (s.hasMoreTokens()) {
			String tmp = s.nextToken();
			addState(tmp);
			// System.out.println(states.toString());
		}
		// stateMap = new ArrayList[states.size()];
		// for (int i = 0; i < stateMap.length; i++)
		// stateMap[i] = new ArrayList<>();
		return true;
	}

	public boolean addState(String input) {
		if (!states.containsKey(input)) {
			State<String> cur = new State<String>(input);
			states.put(cur.stateName, cur);
			stateMap.put(input, new ArrayList<>());
			System.out.println(states.toString());
			return true;
		} else
			System.out.println(states.toString());
		return false;
	}

	@Override
	public boolean setAcceptStates(String input) {
		if (ignore)
			return false;
		StringTokenizer s = new StringTokenizer(input, ",");
		while (s.hasMoreTokens()) {
			String cur = s.nextToken();
			if (states.containsKey(cur)) {
				states.get(cur).accept = true;
				acceptStates.add(states.get(cur));
			} else {
				// if (!ignore) {
				errMsg.add("Invalid accept state " + cur);
				ignore = true;
				// }
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean setStartState(String input) {
//		if (ignore)
//			return false;
		String cur = input;
		if (states.containsKey(cur)) {
			start = states.get(cur);
			curState = start;
			start.start = true;
		} else {
			// if (!ignore) {
			errMsg.add("Invalid start state");
			ignore = true;
			// }
			return false;
		}
		return true;
	}

	@Override
	public boolean setAlphabet(String input) {
		StringTokenizer s = new StringTokenizer(input, ",");
		while (s.hasMoreTokens()) {
			alphabet.add(s.nextToken());
		}
		return true;
	}

	@Override
	public boolean addTransition(String[] input) {
		// System.err.println(Arrays.toString(input));
//		if (ignore) {
//			System.out.println(errMsg);
//			System.out.println("ERRORS OCCURRED PREVIOUSLY");
//			return false;
//		}
		// adding a transition between two states in the automaton
		if (input.length == 3) {
			State<String> u = null, v = null;
			if (states.containsKey(input[0]) && states.containsKey(input[1])) {
				u = states.get(input[0]);
				v = states.get(input[1]);
				System.out.println("from: " + u.stateName);
				System.out.println("to: " + v.stateName);
				if (alphabet.contains(input[2])) {
					// add the transition
					stateMap.get(u.stateName).add(new Transition<>(v, input[2]));
					return true;
				} else {
					// alphabet used isn't part of the language being detected
					if (!input[2].equals("$")) {
						errMsg.add("Invalid transition " + input[0] + "," + input[1] + "," + input[2] + " input "
								+ input[2] + " is not in the alphabet");
						ignore = true;
					}
					return false;
				}
			} else {
				// one of the states making the transition doesn't belong to
				// this dfa
				if (!states.containsKey(input[0])) {
					// if (!ignore) {
					errMsg.add("Invalid transition " + input[0] + "," + input[1] + "," + input[2] + " input " + input[0]
							+ " is not one of the dfa's states");
					ignore = true;
					// }
					return false;
				} else {
					// if (!ignore) {
					errMsg.add("Invalid transition " + input[0] + "," + input[1] + "," + input[2] + " input " + input[1]
							+ " is not one of the dfa's states");
					ignore = true;
					// }
					return false;
				}
			}
		} else {
			// an incomplete transition
			// if (!ignore) {
			String t = "Incomplete Transition ";
			for (int i = 0; i < input.length; i++) {
				if (i != 0)
					t += ",";
				t += input[i];
			}
			errMsg.add(t);
			ignore = true;
			// }
			return false;
		}
	}

}
