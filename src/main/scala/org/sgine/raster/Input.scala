package org.sgine.raster

/**
 * 
 */
case class Input(host: FuncDef, name: Symbol) extends Port(host, name, true) {
  private var _source : Output = null

  def source = _source

  def <-- (outputPort: Output) = _source = outputPort
}
