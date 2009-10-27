package com.sgine.opengl

import javax.media.opengl.GLAutoDrawable
import javax.media.opengl.glu.GLU;
import javax.media.opengl.GLEventListener;
import com.sun.javafx.newt.WindowEvent;
import com.sun.javafx.newt.WindowListener;
import com.sun.javafx.newt.opengl._;

import GLContext._;
import generated.OpenGL2._;

class TestGL extends GLEventListener with WindowListener {
	var window:GLWindow = null;
 	var keepAlive:Boolean = true;
 
	def start() = {
		window = GLWindow.create;
		window.setTitle("Testing");
		window.setSize(800, 600);
		window.setAutoDrawableClient(true);
		window.addGLEventListener(this);
		window.addWindowListener(this);
		window.setVisible(true);
  
		while (keepAlive) {
			window.display;
			Thread.`yield`;
		}
	}
 
	def init(g:GLAutoDrawable) = {
		gl = g.getGL();
		glClearDepth(1.0);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
	}
 
	def reshape(g:GLAutoDrawable, x:Int, y:Int, width:Int, height:Int) = {
	    gl = g.getGL();
	    val glu = new GLU();
	
	    val h = width / height;
	    glViewport(0, 0, width, height);
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    glu.gluPerspective(45.0f, h, 1.0, 20000.0);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	}
 
	def display(g:GLAutoDrawable) = {
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	
	    glLoadIdentity();
	    glTranslatef(0.0f, 0.0f, -15.0f);
	
	    glColor3f(1.0f, 1.0f, 1.0f);
	    glBegin(GL_QUADS);
	    glVertex3f(0.0f, 0.0f, 0.0f);
	    glVertex3f(1.0f, 0.0f, 0.0f);
	    glVertex3f(1.0f, 1.0f, 0.0f);
	    glVertex3f(0.0f, 1.0f, 0.0f);
	    glEnd();
	}
 
	def dispose(g:GLAutoDrawable) = {
	}
 
	def windowResized(e:WindowEvent) = {
	}
 
	def windowMoved(e:WindowEvent) = {
	}
 
	def windowLostFocus(e:WindowEvent) = {
	}
 
	def windowGainedFocus(e:WindowEvent) = {
	}
 
	def windowDestroyNotify(e:WindowEvent) = {
		keepAlive = false;
	}
}