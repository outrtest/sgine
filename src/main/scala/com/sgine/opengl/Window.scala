package com.sgine.opengl

import java.util.concurrent._;

import com.sgine.work._;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11._;
import org.lwjgl.util.glu.GLU._;

class Window (val title:String, val width:Int, val height:Int, val workManager:WorkManager = DefaultWorkManager) {
	import com.sgine.util.JavaConversions.clq2iterable;
	
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
		// Configure display
		Display.setTitle(title);
		Display.setFullscreen(false);		// TODO: revisit
		Display.setVSyncEnabled(false);		// TODO: revisit
		// TODO: set DisplayMode
		Display.create();
		
		// Initialize the GL context
		init();
		
		while(keepAlive) {
			Display.update();
			
			if (Display.isCloseRequested()) {		// Window close
				keepAlive = false;
			} else if (Display.isActive()) {		// Window is in foreground
				render();
			} else {								// Window in background
				Thread.sleep(100);			// TODO: revisit
			}
			
			Thread.`yield`();
		}
	}
	
	def init() = {
		glClearDepth(1.0);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		reshape(0, 0, 640, 480); 		// TODO: revisit
	}
 
	def reshape(x:Int, y:Int, width:Int, height:Int) = {
	    val h = width.toFloat / height.toFloat;
	    glViewport(0, 0, width, height);
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    gluPerspective(45.0f, h, 1.0f, 20000.0f);
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	}
 
	def render() = {
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
}