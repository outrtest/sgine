package com.sgine.opengl;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.javafx.newt.WindowEvent;
import com.sun.javafx.newt.WindowListener;
import com.sun.javafx.newt.opengl.GLWindow;

public class TestJOGL2 implements GLEventListener {
	private GLWindow window;
	private boolean keepAlive;
	
	public TestJOGL2() {
		window = GLWindow.create();
		window.setTitle("Testing");
		window.setSize(800, 600);
		window.addGLEventListener(this);
		window.setAutoDrawableClient(true);
		
		keepAlive = true;
		window.addWindowListener(new WindowListener() {
			public void windowResized(WindowEvent e) {
			}
			
			public void windowMoved(WindowEvent e) {
			}
			
			public void windowLostFocus(WindowEvent e) {
			}
			
			public void windowGainedFocus(WindowEvent e) {
			}
			
			public void windowDestroyNotify(WindowEvent e) {
				keepAlive = false;
			}
		});
		window.setVisible(true);
		
		while (keepAlive) {
			window.display();
			Thread.yield();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new TestJOGL2();
	}

	public void display(GLAutoDrawable d) {
		GL2 gl = d.getGL().getGL2();
		
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
		
		gl.glLoadIdentity();
		gl.glTranslatef(0.0f, 0.0f, -15.0f);
		
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		gl.glBegin(GL2.GL_QUADS);
		gl.glVertex3f(0.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 0.0f, 0.0f);
		gl.glVertex3f(1.0f, 1.0f, 0.0f);
		gl.glVertex3f(0.0f, 1.0f, 0.0f);
		gl.glEnd();
	}

	public void dispose(GLAutoDrawable d) {
	}

	public void init(GLAutoDrawable d) {
		GL2 gl = d.getGL().getGL2();
		gl.setSwapInterval(0);
		gl.glClearDepth(1.0f);
		gl.glEnable(GL.GL_BLEND);
		gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_FASTEST);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		gl.glBlendFunc(GL2.GL_ONE, GL2.GL_ONE_MINUS_SRC_ALPHA);
	}

	public void reshape(GLAutoDrawable d, int x, int y, int width, int height) {
		GL2 gl = d.getGL().getGL2();
		GLU glu = new GLU();
		
		if (height <= 0) { // avoid a divide by zero error!
			height = 1;
		}
		float h = (float) width / (float) height;
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(45.0f, h, 1.0, 20000.0);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
	}
}