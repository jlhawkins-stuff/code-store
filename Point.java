package coding;

public class Point {
	
	public double x = 0;
	public double y = 0;
	public double z = 0;
	
	public Point(){
	}
	
	/*
	 * 2D points
	 */
	public Point(double x, double y){
		setPoint(x,y,0);
	}
	
	public Point(float x, float y){
		setPoint(x,y,0);
	}
	
	public Point(int x, int y){
		setPoint(x,y,0);
	}
	
	/*
	 * 3D points
	 */
	public Point(double x, double y, double z){
		setPoint(x,y,z);
	}
	
	public Point(float x, float y, float z){
		setPoint(x,y,z);
	}
	
	public Point(int x, int y, int z){
		setPoint(x,y,z);
	}
	
	public void setPoint(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setPoint(Point point){
		this.x = point.x;
		this.y = point.y;
		this.z = point.z;
	}
	
	public Point getPoint(){
		return this;
	}
}
