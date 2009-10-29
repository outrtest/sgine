package com.sgine.opengl

import java.util.concurrent._;
import javax.media.opengl._;
import javax.media.opengl.glu._;

import com.sun.javafx.newt._;
import com.sun.javafx.newt.opengl._;

import com.sgine.work._;

import GLContext._;
import generated.OpenGL2._;

class Window (val title:String, val width:Int, val height:Int, val workManager:WorkManager = DefaultWorkManager) extends GLEventListener with WindowListener {
	import com.sgine.util.JavaConversions.clq2iterable;
	
	private var glWindow:GLWindow = _;
	private var keepAlive = true;
	private var renders:Long = 0;
	private var lastRender:Long = System.nanoTime();
	
	val displayables = new ConcurrentLinkedQueue[(Double) => Unit]();
	
	def start() = {
		workManager += run;
		
		while(renders < 2) {
			Thread.sleep(10);
		}
	}
	
	private def run() = {
		glWindow = GLWindow.create();
		glWindow.setTitle(title);
		glWindow.setSize(width, height);
		glWindow.setAutoDrawableClient(true);
		glWindow.addGLEventListener(this);
		glWindow.addWindowListener(this);
		glWindow.setVisible(true);
		while(keepAlive) {
			glWindow.display();
			Thread.`yield`();
		}
		System.exit(0); // TODO: figure out what's not letting go
	}
	
	def init(g:GLAutoDrawable) = {
		gl = g.getGL();
		setSwapInterval(0);
		glClearDepth(1.0);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
	}
 
	def reshape(g:GLAutoDrawable, x:Int, y:Int, width:Int, height:Int) = {
	    gl = g.getGL();
	    val glu = new GLU();
	
	    val h = width.toDouble / height.toDouble;
	    glViewport(0, 0, width, height);
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    glu.gluPerspective(45.0f, h, 1.0, 20000.0);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	}
 
	def display(g:GLAutoDrawable) = {
		val currentRender = System.nanoTime();
		val time = (currentRender - lastRender) / 1000000000.0;
		
	    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	
	    glLoadIdentity();
	    glColor3f(1.0f, 1.0f, 1.0f);
	    
	    displayables.foreach(_(time));
	    
	    renders += 1;
	    if (renders == Math.MAX_LONG) {
	    	renders = 0;
	    }
	    
	    lastRender = currentRender;
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
		println("Destroy!");
		keepAlive = false;
	}
}