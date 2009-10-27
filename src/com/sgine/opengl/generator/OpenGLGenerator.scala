package com.sgine.opengl.generator

import java.io._
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javax.media.opengl._;
import javax.media.opengl.fixedfunc._;

object OpenGLGenerator {
	private var sb:StringBuilder = null;

	private var currentClass:Class[_] = null;
	
	private var methods:List[Method] = Nil;
	
	def main(args:Array[String]):Unit = {
		createClass("GLMatrixFuncTrait", classOf[GLMatrixFunc]);
		createClass("GL1Trait", classOf[GL]);
		createClass("GL2Trait", classOf[GL2]);
		createClass("GL3Trait", classOf[GL3]);
		createClass("GLES1Trait", classOf[GLES1]);
		createClass("GLES2Trait", classOf[GLES2]);
		createClass("GL2ES1Trait", classOf[GL2ES1]);
		createClass("GL2ES2Trait", classOf[GL2ES2]);
		
		createSimpleClass("OpenGL", "GLMatrixFuncTrait", "GL1Trait", "GL2Trait", "GL3Trait", "GLES1Trait", "GLES2Trait", "GL2ES1Trait", "GL2ES2Trait");
		createSimpleClass("OpenGL2", "GLMatrixFuncTrait", "GL1Trait", "GL2Trait", "GL2ES1Trait", "GL2ES2Trait");
	}
	
	def createSimpleClass(name:String, traits:String*):Unit = {
		sb = new StringBuilder();
		sb.append("package com.sgine.opengl.generated\r\n\r\n");
		sb.append("object ");
		sb.append(name);
		for ((s, index) <- traits.toArray.zipWithIndex) {
			if (index == 0) {
				sb.append(" extends ");
			} else {
				sb.append(" with ");
			}
			sb.append(s);
		}
		
		val output = new FileOutputStream(new File("src/com/sgine/opengl/generated/" + name + ".scala"));
		output.write(sb.toString().getBytes());
		output.flush();
	}
	
	def createClass(name:String, c:Class[_]):Unit = {
		currentClass = c;
		sb = new StringBuilder();
		sb.append("package com.sgine.opengl.generated\r\n\r\n");
		sb.append("import com.sgine.opengl.GLContext\r\n");
		sb.append("import javax.media.opengl._\r\n");
		sb.append("import javax.media.opengl.fixedfunc._;\r\n\r\n");
		sb.append("trait ");
		sb.append(name);
		sb.append(" {\r\n");
		c.getFields.foreach(processField);
		sb.append("\r\n\r\n");
		c.getMethods.foreach(processMethod);
		sb.append("}");
		
		val output = new FileOutputStream(new File("src/com/sgine/opengl/generated/" + name + ".scala"));
		output.write(sb.toString().getBytes());
		output.flush();
	}
	
	def processField(f:Field):Unit = {
		if (Modifier.isStatic(f.getModifiers())) {
			if (f.getDeclaringClass() == currentClass) {
				sb.append("\tval ");
				sb.append(f.getName());
				sb.append(" = ");
				sb.append(f.getDeclaringClass().getSimpleName());
				sb.append('.');
				sb.append(f.getName);
				sb.append(";\r\n");
			}
		}
	}
	
	def processMethod(m:Method):Unit = {
		if (m.getDeclaringClass() == currentClass) {
			if (isOriginalMethod(m)) {
				sb.append('\t');
				sb.append("def ");
				sb.append(m.getName());
				sb.append('(');
				for ((paramType, index) <- m.getParameterTypes zipWithIndex) {
					processType(paramType, index);
				}
				sb.append("):");
				var rt = convertType(m.getReturnType());
				sb.append(rt);
				sb.append(" = {\r\n\t\t");
				sb.append("GLContext.");
				sb.append(currentClass.getSimpleName().toLowerCase());
				sb.append('.');
				sb.append(m.getName());
				sb.append('(');
				for ((paramType, index) <- m.getParameterTypes zipWithIndex) {
					if (index > 0) {
						sb.append(", ");
					}
					sb.append("arg");
					sb.append(index);
				}
				sb.append(");\r\n\t}\r\n");
			}
		}
	}
	
	def isOriginalMethod(m:Method):Boolean = {
		var exists = false;
		for (cm <- methods) {
			if (cm.getName() == m.getName()) {
				exists = true;
//				for ((p1, p2) <- cm.getParameterTypes() zip m.getParameterTypes()) {
//					if (p1 != p2) {
//						exists = false;
//					}
//				}
			}
		}
		if (!exists) {
			methods = m :: methods;
		} else {
			println("Method exists: " + m);
		}
		!exists;
	}
	
	def processType(c:Class[_], index:Int):Unit = {
		if (index > 0) {
			sb.append(", ");
		}
		sb.append("arg");
		sb.append(index);
		sb.append(':');
		
		sb.append(convertType(c));
	}
 
	private def convertType(c:Class[_]):String = {
		if (c == classOf[boolean]) "Boolean";
		else if (c == classOf[byte]) "Byte";
		else if (c == classOf[short]) "Short";
		else if (c == classOf[int]) "Int";
		else if (c == classOf[long]) "Long";
		else if (c == classOf[float]) "Float";
		else if (c == classOf[double]) "Double";
		else if (c == classOf[Array[boolean]]) "Array[Boolean]";
		else if (c == classOf[Array[byte]]) "Array[Byte]";
		else if (c == classOf[Array[short]]) "Array[Short]";
		else if (c == classOf[Array[int]]) "Array[Int]";
		else if (c == classOf[Array[long]]) "Array[Long]";
		else if (c == classOf[Array[float]]) "Array[Float]";
		else if (c == classOf[Array[double]]) "Array[Double]";
		else if (c == classOf[Array[String]]) "Array[String]";
		else if (c.getCanonicalName() == "void") "Unit";
		else c.getCanonicalName();
	}
}