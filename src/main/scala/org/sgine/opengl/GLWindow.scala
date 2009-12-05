package org.sgine.opengl

import java.util.concurrent._;

import scala.collection.JavaConversions._

import org.sgine.util._;
import org.sgine.work._;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11._;
import org.lwjgl.util.glu.GLU._;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class GLWindow (val title:String, val width:Int, val height:Int, val workManager:WorkManager = DefaultWorkManager) {
	private var keepAlive = true;
	private var renders:Long = 0;
	private var lastRender:Long = System.nanoTime();
	private val frame = new Frame();						// TODO: allow for custom container, applets, etc.
	private val canvas = new Canvas();
	private val thread = new Thread(FunctionRunnable(run));
	
	val displayables = new ConcurrentLinkedQueue[(Double) => Unit]();
	
	def start() = {
		thread.start();
		
		while(renders < 2) {
			Thread.sleep(10);
		}
	}
	
	private def run() = {
		// Configure Swing aspect
		frame.setLayout(new BorderLayout());
		frame.add(BorderLayout.CENTER, canvas);
		frame.setSize(width, height);
		frame.setTitle(title);
		frame.setResizable(false);		// TODO: support resizing at some point
		frame.addWindowListener(new WindowAdapter() {
			override def windowClosing(e:WindowEvent):Unit = {
				keepAlive = false;
			}
		});
		frame.setVisible(true);
		
		// Configure display
		Display.setFullscreen(false);		// TODO: revisit
		Display.setVSyncEnabled(false);		// TODO: revisit
		Display.setDisplayMode(determineDisplayMode());
		Display.setParent(canvas);
		Display.create();		// TODO: incorporate PixelFormat
		
		// Initialize the GL context
		init();
		
		while(keepAlive) {
			Display.update();
			
			if (Display.isCloseRequested()) {		// Window close
				keepAlive = false;
			}
			render();
		}
		Display.destroy();
		frame.dispose();
//		System.exit(0);
	}
	
	private def determineDisplayMode():DisplayMode = {		// TODO: make better
    var fallbackMode: DisplayMode = null
		for (mode <- Display.getAvailableDisplayModes()) {
      fallbackMode = mode
			if (mode.getWidth() == width) {
				if (mode.getHeight() == height) {
					return mode;
				}
			}
		}

    // FIXME: This gives some picture even when the program doesn't know the users screen resolution, 
    // but will leave width and height in an inconsistent state with the actual size of the display.
    // Best would probably to select / allow the user to select from the available display modes.
		return fallbackMode;
	}
	
	def init() = {
		glClearDepth(1.0);
		glEnable(GL_BLEND);
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_FASTEST);
		glEnable(GL_TEXTURE_2D);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
		reshape(0, 0, width, height);
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
	    if (renders == Long.MaxValue) {
	    	renders = 0;
	    }
	    
	    lastRender = currentRender;
	}
}