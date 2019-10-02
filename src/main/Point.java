package main;

public class Point {

	public int pos;
	public double x, y, z;    // położenia
	public double px, py, pz; // pędy
	public double Fx, Fy, Fz; // siły
	
	public Point() {}
	
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Point mult(double a) {
		return new Point(a * x, a * y, a * z);
	}
	
	public double abs() {
		return Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString() {
		return "(" + x + "," + y + "," + z + ")"; 
	}
	
	public String toFile() {
		return x + "\t" + y + "\t" + z + "\n"; 		
	}
	
	public String momentumsToFile() {
		return px + "\t" + py + "\t" + pz + "\n"; 				
	}
}
