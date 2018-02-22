
public interface AutomatonUtils {
	
	/**
	 * Defines the states present in the automaton
	 * @param s -> the string containing the states that should be present in the automaton state0,state1,...,stateN
	 * @return true if the states were added successfully false otherwise
	 */
	public boolean addStates(String input);
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public boolean setAcceptStates(String input);
	
	public boolean setStartState(String input);
	
	public boolean setAlphabet(String input);
	
	/**
	 * Adds transition to the automaton by splitting the string on ","
	 * @param s -> the string describing the transition from,to,alphabet
	 * @return true if the transition was added successfully false otherwise
	 */
	public boolean addTransition(String[] input);
	
	

}
