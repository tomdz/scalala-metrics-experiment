package org.tomdz

import com.yammer.metrics.stats.ExponentiallyDecayingSample

class ExponentiallyDecayingSampleWithTicker(val reservoirSize: Int, val alpha: Double) extends ExponentiallyDecayingSample(reservoirSize, alpha) {
  private[this] var time: Long = System.nanoTime()

  def addTime(timeInMs: Long) {
    time += timeInMs * 1000000
  }
  override def nowInNs: Long = time
}