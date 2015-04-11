package phs.bitcamp.amisafe.data;

public class Crime {
	private int id;
	public enum TIME_OF_DAY {
		DAY,
		EVENING,
		MIDNIGHT
	}
	private TIME_OF_DAY tod;
	private String offense;
	private double latitude;
	private double longitude;
	
	public Crime(){
		//whelp, nothing is here
	}
	
	public Crime(int i, String time, String crime_offense, double lat, double lon){
		this.id = i;
		this.offense = crime_offense;
		this.latitude = lat;
		this.longitude = lon;
		
		if(time.equals("DAY"))
			tod = TIME_OF_DAY.DAY;
		else if (time.equals("EVENING"))
			tod = TIME_OF_DAY.EVENING;
		else if (time.equals("MIDNIGHT"))
			tod = TIME_OF_DAY.MIDNIGHT;
	}
	
	/***
	 * Returns the coordinates of the crime in the format
	 * [latitude, longitude].
	***/
	public double[] getCoords(){
		double[] myCoords = {latitude, longitude};
		return myCoords;	
	}
	
	/***
	 * @return Short description of the crime.
	 */
	
	public String getOffense(){
		return offense;
	}
	
	public int getID(){
		return this.id;
	}
	
	public TIME_OF_DAY getTOD(){
		return this.tod;
	}
}
