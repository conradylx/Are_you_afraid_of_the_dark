package Luminance

import Luminance.LuminanceModule.input_origin_directory

import java.io.File
import java.nio.file.{Files, Paths}
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

object LuminanceModule extends App {
  val input_origin_directory = "XX"
  val output_origin_directory = "XX"
  val cut_off_origin_directory = "XX"

  def get_files_from_folder(input_origin_directory: String): ArrayBuffer[String] = {
    val folder = new File(input_origin_directory)
    val listOfFiles = folder.listFiles
    val array = ArrayBuffer[String]()
    var counter = 0
    for (file <- listOfFiles) {
      if (file.isFile & (file.getName.endsWith(".jpg") || file.getName.endsWith(".png"))) {
        counter += 1
        array += file.getName
      }
    }
    array
  }

  def take_treshold(): Integer = {
    val content = new String(Files.readAllBytes(Paths.get(cut_off_origin_directory)))
    content.toInt
  }

  def prepare_file_name(filename: String, percentage: Long): String = {
    var new_filename: String = ""
    var iterator: Integer = 0
    val treshold: Integer = take_treshold()
    val extension = filename.split("\\.").last
    do {
      new_filename += filename(iterator)
      iterator += 1
    } while (filename(iterator) != '.')
    if (percentage >= treshold) new_filename += "_dark_" + percentage + '.' + extension
    else new_filename += "_bright_" + percentage + '.' + extension
    new_filename
  }


  def prepare_output_operations(array: ArrayBuffer[String], input_origin_directory: String): ArrayBuffer[File] = {
    var output_dir: String = ""
    val array_size: Int = array.length - 1
    val prepared_data = ArrayBuffer[File]()
    for (img_file <- 0 to array_size) {
      val img = ImageIO.read(new File(input_origin_directory + "\\" + array(img_file)))
      var width: Int = 0
      var height: Int = 0
      var counter: Int = 0
      var average: Double = 0
      if (img != null) {
        width = img.getWidth() - 1
        height = img.getHeight() - 1
      }
      for (width_iterator <- 0 to width) {
        for (height_iterator <- 0 to height) {
          val pixelCol: Int = img.getRGB(width_iterator, height_iterator)
          val red: Int = (pixelCol >>> 16) & 0xff
          val green: Int = (pixelCol >>> 8) & 0xff
          val blue: Int = pixelCol & 0xff

          var luminance: Double = (0.2126 * red) + (0.7152 * green) + (0.0722 * blue)
          average += luminance
          counter += 1
        }
      }
      average = average / counter
      val percentage: Long = 100 - math.round((average * 100) / 255)
      val new_file_name: String = prepare_file_name(array(img_file), percentage)
      output_dir = output_origin_directory + "\\" + new_file_name
      val file_to_save = new File(output_dir)
      ImageIO.write(img, "bmp", file_to_save)
      prepared_data += file_to_save
    }
    prepared_data
  }

  val array: ArrayBuffer[String] = get_files_from_folder(input_origin_directory)
  println(prepare_output_operations(array, input_origin_directory))
}
