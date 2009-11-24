package com.sgine.scene

import com.sgine.property._;

class Node {
	val id = new ImmutableProperty[Long](0L);
	val name = new MutableProperty[String];
	val parent = new MutableProperty[Node];
}