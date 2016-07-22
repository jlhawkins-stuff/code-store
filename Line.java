package coding;

public class Line extends Point{
	
	public Point point1;
	public Point point2;

	public Line(){
	}
	
	public Line(Point point1, Point point2){
		this.point1 = point1;
		this.point2 = point2;
	}
	
	public Line(double x1, double y1, double x2, double y2){
		point1 = new Point(x1,y1);
		point2 = new Point(x2,y2);
	}
	
	public Line(double x1, double y1, double z1, double x2, double y2, double z2){
		point1 = new Point(x1,y1,z1);
		point2 = new Point(x2,y2,z2);
	}
}
