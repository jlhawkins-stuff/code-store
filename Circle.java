package coding;

/**
 * used to precalculate points around a center x,y with a given radius
 * could be used for creating any geometric shape
 * if slices was only 3 would create a triangle, etc...
 */

public class Circle extends Point{
	
	protected double radius;
	//private double degrees;
	private double degrees = 360;
	private Point[] points = new Point[(int) (degrees)];
	//private Point[] points;
	private double angle;
	private double cosAngle;
	private double sinAngle;
	
	public Circle(){
	}
	
	/*
	 * cx = the circles center x value
	 * cy = the circles center y value
	 * radius = radius
	 */
	public Circle(Point center, double radius){
		super.setPoint(center);
		this.radius = radius;
		generatePoints();
	}
	
	public Circle(double cx, double cy, double cz, double radius){
		super.setPoint(cx, cy, cz);
		this.radius = radius;
		generatePoints();
	}
	
	public Circle(float cx, float cy, float cz, float radius){
		super.setPoint(cx, cy, cz);
		this.radius = radius;
		generatePoints();
	}

	public Circle(int cx, int cy, int cz, int radius){
		super.setPoint(cx, cy, cz);
		this.radius = radius;
		generatePoints();
	}
	
	/*private double getNumCircleSegments(double radius){ 
		return 10 * Math.sqrt(radius);//change the 10 to a smaller/bigger number as needed 
	}*/
	
	/*
	 * returns an array of points
	 */
	public Point[] getPoints(){
		return points;
	}
	
	private void calculateAngles(){
		this.angle = 2.0F * Math.PI / this.degrees;
		this.cosAngle = Math.cos(angle);
		this.sinAngle = Math.sin(angle);
	}
	/*
	 * creates points in a circle when the object is instanuated
	 * the more points the more it looks like a circle
	 */
	private void generatePoints(){
		/*
		 * generates the points of a circle given a radius around 0,0,0
		 * moving the circle to a center is done from the calling method
		 */
		points[0] = new Point(radius,0,0);
		//precalculate angles
		calculateAngles();
		int i = 1;
		for(; i < (int) degrees; i++){
			double x = cosAngle * points[i-1].x - sinAngle * points[i-1].y;
			double y = sinAngle * points[i-1].x + cosAngle * points[i-1].y;
			double z = super.z;
			points[i] = new Point(x,y,z);
			centerPoint(i-1);
		}
		centerPoint(i-1);
	}
	
	private void centerPoint(int i){
		points[i].x += super.x;
		points[i].y += super.y;
		points[i].z += super.z;
	}
}
