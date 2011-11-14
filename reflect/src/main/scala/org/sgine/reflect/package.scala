package org.sgine

import java.lang.reflect.Method
import ref.SoftReference
import java.util.concurrent.ConcurrentHashMap

package object reflect {
  private val map = new ConcurrentHashMap[Class[_], SoftReference[EnhancedClass]]

  implicit def class2EnhancedClass(c: Class[_]): EnhancedClass = map.get(c) match {
    case null => {
      val ec = new EnhancedClass(c)
      map.put(c, new SoftReference[EnhancedClass](ec))
      ec
    }
    case ref if (!ref.isEnqueued) => ref()
  }

  implicit def method2EnhancedMethod(m: Method) = class2EnhancedClass(m.getDeclaringClass)(m)

  implicit def f0ToEM(f: Function0[_]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 0)
  }

  implicit def f1ToEM(f: Function1[_, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 1)
  }

  implicit def f2ToEM(f: Function2[_, _, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 2)
  }

  implicit def f3ToEM(f: Function3[_, _, _, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 3)
  }

  implicit def f4ToEM(f: Function4[_, _, _, _, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 4)
  }

  implicit def f5ToEM(f: Function5[_, _, _, _, _, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 5)
  }

  implicit def f6ToEM(f: Function6[_, _, _, _, _, _, _]) = {
    class2EnhancedClass(f.getClass).methods.find(m => m.name == "apply" && m.args.length == 6)
  }

  def method(absoluteSignature: String) = {
    val absoluteMethodName = absoluteSignature.substring(0, absoluteSignature.indexOf('('))
    val className = absoluteMethodName.substring(0, absoluteMethodName.lastIndexOf('.'))
    val c = Class.forName(className)
    c.methodBySignature(absoluteSignature)
  }
}