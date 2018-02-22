
public class Transition<T> {
	
	State<T> state;
	String transition;
	
	public Transition(State<T> state, String transition){
		this.state = state;
		this.transition = transition;		
	}
	
	public String toString(){
		return "<" + transition + "," + state.stateName + ">";
	}

}
