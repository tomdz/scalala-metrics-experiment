package org.tomdz

import scala.math.pow
import scala.collection.JavaConversions._
import scalala.scalar._
import scalala.tensor.dense._
import scalala.library.Plotting._
import scalala.operators.Implicits.richArray
import org.apache.commons.math.distribution.GammaDistributionImpl
import org.apache.commons.math.distribution.NormalDistributionImpl
import org.apache.commons.math.distribution.BetaDistributionImpl
import com.yammer.metrics.stats.UniformSample
import scalala.library.plotting.StaticHistogramBins
import java.awt.Color

object ScalalaDerivates {
  val NUM_VALUES = 1000000
  val NUM_SAMPLES = 10000
  val SCALE_FAC = 1000
  val DECAY_FACTOR = 0.015

  def sampleUniformly(values: DenseVector[Double], numSamples: Int, scaleFac: Long): DenseVector[Double] = {
    val sample = new UniformSample(numSamples)
    for (idx <- 0 until values.length) {
      sample.update((scaleFac * values(idx)).toLong)
    }
    return sample.values.map(v => v.toDouble).toArray.asVector
  }

  def sampleWithDecay(values: DenseVector[Double], alpha: Double, numSamples: Int, scaleFac: Long, msPerStep: Int): DenseVector[Double] = {
    val sample = new ExponentiallyDecayingSampleWithTicker(numSamples, alpha)
    var time = System.currentTimeMillis()

    for (idx <- 0 until values.length) {
      sample.update((scaleFac * values(idx)).toLong)
      sample.addTime(msPerStep)
    }
    return sample.values.map(v => v.toDouble).toArray.asVector
  }

  def uniform(numValues: Int): DenseVector[Double] = {
    return DenseVector.rand(numValues)
  }

  def normal(numValues: Int): DenseVector[Double] = {
    val dist = new NormalDistributionImpl

    return dist.sample(numValues).asVector;
  }

  def line(numValues: Int, start: Double, end: Double): DenseVector[Double] = {
    val result: DenseVector[Double] = DenseVector.zeros(numValues)

    for (idx <- 0 until numValues) {
      result(idx) = start + (end - start) * idx.toDouble / numValues.toDouble
    }
    return result
  }

  def gamma(numValues: Int): DenseVector[Double] = {
    val dist = new GammaDistributionImpl(9, 0.5)

    return dist.sample(numValues).asVector;
  }

  def beta(numValues: Int): DenseVector[Double] = {
    val dist = new BetaDistributionImpl(0.5, 0.5)

    return dist.sample(numValues).asVector;
  }

  def addPlot(dist: => DenseVector[Double], row: Int, titleStr: String) {
    val values = dist
    val uniformlySampledValues = sampleUniformly(values, NUM_SAMPLES, SCALE_FAC)
    val valuesSampledWithDecay = sampleWithDecay(values, DECAY_FACTOR, NUM_SAMPLES, SCALE_FAC, 1)

    subplot(row, 3, 3*(row - 1) + 1)
    hist(values :* SCALE_FAC, NUM_SAMPLES)
    title(titleStr)

    subplot(row, 3, 3*(row - 1) + 2)
    hist(uniformlySampledValues, NUM_SAMPLES)
    title(titleStr + " (Uniformly sampled)")

    subplot(row, 3, 3*(row - 1) + 3)
    hist(valuesSampledWithDecay, NUM_SAMPLES)
    title(titleStr + " (Sampled with decay)")
  }
  
  def main(args: Array[String]): Unit = {
    plot.hold = true
    figure.frame.setSize(1600, 1200)
    addPlot(uniform(NUM_VALUES), 1, "Uniform distribution")
    addPlot(normal(NUM_VALUES), 2, "Normal distribution")
    addPlot(gamma(NUM_VALUES), 3, "Gamma distribution")
    addPlot(beta(NUM_VALUES), 4, "Beta distribution")
    addPlot(line(NUM_VALUES, 0, 1), 5, "Increasing values")
    saveas("plot.png")
  }
}