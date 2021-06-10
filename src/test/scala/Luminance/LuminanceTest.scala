package Luminance

import Luminance.LuminanceModule.{input_origin_directory, take_treshold}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.io.File
import scala.collection.mutable.ArrayBuffer

class LuminanceTest extends AnyFlatSpec with should.Matchers {
  var data_array: ArrayBuffer[String] = ArrayBuffer[String]()

  "LuminanceModule" should "properly counter files in folder" in {
    val array = LuminanceModule.get_files_from_folder("XX")
    data_array = array
    array.size should be(1)
  }

  "LuminanceModule" should "properly get threshold" in {
    val treshold_limit: Integer = take_treshold()
    print(treshold_limit)
    assert(treshold_limit == 55)
  }
}