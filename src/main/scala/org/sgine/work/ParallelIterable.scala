package org.sgine.work

class ParallelIterable[A](iterable: Iterable[A]) {
	def foreachParallel[B](f: (A) => B)(implicit workManager: WorkManager = DefaultWorkManager) = {
		workManager.process(iterable, f)
	}
}