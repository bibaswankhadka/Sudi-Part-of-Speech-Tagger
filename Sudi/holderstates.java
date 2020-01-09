
public class holderstates {
	String currState;
	String nextState;
	Double nextScore;
	
	public holderstates(String currState, String nextState, Double currScore){
		this.currState = currState;
		this.nextState = nextState;
//		this.nextScore = currScore + Sudi.getout().get(currState).get(nextState);
	}
	
	public String getcurrent() {
		return currState;
	}
	
	public String getnext() {
		return nextState;
	}
	
	public Double getnextScore() {
		return nextScore;
	}

}
