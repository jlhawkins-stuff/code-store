package coding;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

public class RenderTester {
	
	private static DisplayMode newMode = new DisplayMode(800, 600);
	
	/**
	 * Initializes OGL
	 */
	private static void glInit() {
		// Go into orthographic projection mode.
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		//(float left, float right, float bottom, float top)
		gluOrtho2D(0, newMode.getWidth(), 0, newMode.getHeight());
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		//(int x, int y, int width, int height)
		glViewport(0, 0, newMode.getWidth(), newMode.getHeight());
		//set clear color to black
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		//sync frame (only works on windows)
		Display.setVSyncEnabled(true);
		//System.out.println(" display height is " + Display.getHeight() + " width " + Display.getWidth());
		//System.out.println(" display Y is      " + Display.getY() + " X " + Display.getX());
	}

	private static Point point1 = new Point(150,150);
	private static Point point2 = new Point(100,100);
	private static Circle circle1 = new Circle(point1,100.0F);
	private static Line line1 = new Line(point1,point2);
	
	/**
	 * 
	 */
	private static void render() {
		//clear background
		glClear(GL_COLOR_BUFFER_BIT);
		drawCircle(circle1, new Color());
		drawLine(line1);
	}
	
	private static void drawLine(Line line) {
		GL11.glPushMatrix();
	   // GL11.glColor3f(0.0f, 1.0f, 0.2f);
	    //GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glBegin(GL11.GL_LINE_LOOP);
	    GL11.glVertex2d(line.point1.x, line.point1.y);
	    GL11.glVertex2d(line.point2.x, line.point2.y);
	    GL11.glEnd();
		GL11.glPopMatrix();
	}
	
	/**
	 * 
	 * @param circle
	 * @param color
	 */
	protected static void drawCircle(Circle circle, Color color){
		GL11.glPushMatrix();
		//GL11.glShadeModel(GL11.GL_SMOOTH);
		//GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
		//GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glBegin(GL11.GL_LINE_LOOP);
		//GL11.glBegin(GL11.GL_POLYGON);
		Point[] circlePoints = circle.getPoints();
		for(int i = 0; i < circlePoints.length; i++){
			//GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			//GL11.glColor4d(red, green, blue, alpha);
			randomColor();
			GL11.glVertex2d(circlePoints[i].x, circlePoints[i].y);
		} 
		GL11.glEnd(); 
		GL11.glPopMatrix();
	}
	
	public static void sphere(double cx, double cy, double cz, double radius){
		Circle[] circles = new Circle[(int) (radius)];
		for(int i = 0;i<radius;i++){
			circles[i] = new Circle(cx, cy, cz-i,radius-i);
		}
	}
	
	protected static void drawSphere(){
		
	}
	
	/*private static double red = Math.random();
	private static double green = Math.random();
	private static double blue = Math.random();
	private static double alpha = Math.random();*/
	
	private static void randomColor(){
		double red = Math.random();
		double green = Math.random();
		double blue = Math.random();
		double alpha = Math.random();
		//color.set(red,green,blue,alpha);
		//GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		GL11.glColor4d(red, green, blue, alpha);
	}
	
	public static void main(String[] args){
		try {
			Display.setDisplayModeAndFullscreen(newMode);
			Display.setTitle("Render Testing");
			Display.create();
			glInit();
			start();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		cleanup();
	}
	
	private static void start(){
		while (!Keyboard.isKeyDown(Keyboard.KEY_ESCAPE) && !Display.isCloseRequested()) {
			if (Display.isVisible()) {
				render();
			}else {
				// no need to render/paint if nothing has changed (ie. window dragged over)
				if (Display.isDirty()) {
					render();
				}
				// don't waste cpu time, sleep more
				try {
					Thread.sleep(100);
				} catch (InterruptedException inte) {
				}
			}
			// Update window
			Display.update();
		}
	}
	
	/**
	 * Cleans up the test
	 */
	private static void cleanup() {
		Display.destroy();
	}
}
