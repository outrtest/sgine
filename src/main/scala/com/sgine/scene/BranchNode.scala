package com.sgine.scene

import java.util.concurrent._;

class BranchNode extends Node {
	val children = new ConcurrentLinkedQueue[Node];
}