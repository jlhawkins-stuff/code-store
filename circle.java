package coding;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluOrtho2D;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.Point;

public class Circle {

	private static DisplayMode newMode = new DisplayMode(500, 500);

	private static Point topMiddle = new Point((int) (newMode.getWidth()/2.0d), 0);
	private static Point bottomMiddle = new Point((int) (newMode.getWidth()/2.0d), newMode.getHeight());
	private static Point rightMiddle = new Point(0, (int) (newMode.getHeight()/2.0d));
	private static Point leftMiddle = new Point(newMode.getWidth(), (int) (newMode.getHeight()/2.0d));
	
	private static float slices = 360;
	private static float angle = 2.0F * (float) Math.PI / slices;
	private static float cosAngle = (float) Math.cos(angle); //precalculate the cosine
	private static float sinAngle = (float) Math.sin(angle); //precalculate the sine
	//private static float x;
	//private static float y;
	
	public static void main(String[] args){
		try {
			Display.setDisplayModeAndFullscreen(newMode);
			Display.create();
			Display.setTitle("Circle");
			glInit();
			run();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		cleanup();
	}
	
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
	}
	
	public static void run(){
		
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

	private static Color color = new Color();
	private static void render() {
		//clear background
		glClear(GL_COLOR_BUFFER_BIT);

		glPushMatrix();
		{
			colorRed();
			drawCircle(450,450,50);
		}
		glPopMatrix();

		glPushMatrix();
		colorGreen();
		GL11.glScaled(0.1, 0.1, 0);
		drawCircle(2500,2500,50);
		glPopMatrix();

		glPushMatrix();
		color.set(0, 255, 255, 255);//cyan
		GL11.glScaled(1, 1, 0);
		drawCircle(500,500,60);
		glPopMatrix();

		glPushMatrix();
		colorBlue();
		drawCircle(newMode.getWidth()/2.0f,newMode.getHeight()/2.0f,60);
		glPopMatrix();

		glPushMatrix();
		randomColor();
		randomLineWidth();
		drawLine(topMiddle,bottomMiddle);
		glPopMatrix();

		glPushMatrix();
		randomColor();
		randomLineWidth();
		drawLine(rightMiddle,leftMiddle);
		glPopMatrix();
	}
	
	private static void colorRed(){
		color.set(255, 0, 0, 255);
	}
	
	private static void colorBlue(){
		color.set(0, 0, 255, 255);
	}
	
	private static void colorGreen(){
		color.set(0, 255, 0, 255);
	}
	
	private static void randomLineWidth(){
		float lineWidth = (float)Math.random() * 10;
		//System.out.println(lineWidth);
		GL11.glLineWidth(lineWidth);
	}
	
	private static void randomColor(){
		double red = Math.random();
		double green = Math.random();
		double blue = Math.random();
		double alpha = Math.random();
		//color.set(red,green,blue,alpha);
		//GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
		GL11.glColor4d(red, green, blue, alpha);
	}
	
	
	private static void drawLine(Point point, Point point2) {
	   // GL11.glColor3f(0.0f, 1.0f, 0.2f);
	    //GL11.glBegin(GL11.GL_LINE_STRIP);
		GL11.glBegin(GL11.GL_LINE_LOOP);
	    GL11.glVertex2d(point.getX(), point.getY());
	    GL11.glVertex2d(point2.getX(), point2.getY());
	    GL11.glEnd();
	}

	protected static void drawCircle(float centerX, float centerY, float radius){
		
		float x = radius;//we start at angle = 0
		float x2 = radius -5;
		float y = 0; 
		float y2 = 0; 
		float t; //storage of x value
		float t2; //storage of x value
		
		GL11.glBegin(GL11.GL_LINE_LOOP);
		//GL11.glBegin(GL11.GL_POLYGON); //can be used for solid circle 
		for(int i = 0; i < slices; i++) 
		{
			GL11.glColor4d(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
			//GL11.glColor4d(red, green, blue, alpha);

			GL11.glVertex2f(x + centerX, y + centerY);//output vertex 
			GL11.glVertex2f(x2 + centerX, y2 + centerY);//output vertex 

			//apply the rotation matrix
			t = x;
			x = cosAngle * x - sinAngle * y;
			y = sinAngle * t + cosAngle * y;
			
			t2 = x2;
			x2 = cosAngle * x2 - sinAngle * y2;
			y2 = sinAngle * t2 + cosAngle * y2;
		} 
		GL11.glEnd(); 
	}
	
	/**
	 * Cleans up the test
	 */
	private static void cleanup() {
		Display.destroy();
	}
}
