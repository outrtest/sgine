package com.sgine.property

class AdvancedProperty[T] extends MutableProperty[T] with ListenableProperty[T] with BindingProperty[T] with AdjustableProperty[T]