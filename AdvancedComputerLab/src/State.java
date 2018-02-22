import java.util.HashSet;

public class State<T> {
	
	boolean accept;
	boolean start;
	T stateName;
//	int stateId;
	HashSet<String> elementsStates;
	String action;
	
	public State(T stateName){
		this.stateName = stateName;
//		this.stateId = stateId;
		this.accept = false;
		this.start = false;
		this.elementsStates = null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((stateName == null) ? 0 : stateName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		State<T> other = (State) obj;
		if (stateName == null) {
			if (other.stateName != null)
				return false;
		} else if (!stateName.equals(other.stateName))
			return false;
		return true;
	}

	public String toString(){
		return "stateName: " + stateName + ", accept: " + accept + ", start: " + start;
	}

}
