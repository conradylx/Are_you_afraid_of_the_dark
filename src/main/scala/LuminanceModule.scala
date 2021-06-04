import java.io.File
import javax.imageio.ImageIO
import scala.collection.mutable.ArrayBuffer

object LuminanceModule extends App {
  def get_files_from_folder(): Unit = {
    val folder = new File("your_path")
    val listOfFiles = folder.listFiles
    val array = ArrayBuffer[String]()
    var counter = 0
    for (file <- listOfFiles) {
      if (file.isFile & (file.getName.endsWith(".jpg") || file.getName.endsWith(".png"))) {
        counter += 1
        array += file.getName
      }
    }
  calculate_luminance(array)
  }


  def calculate_luminance(array: ArrayBuffer[String]): Unit = {
    val array_size: Int = array.length - 1
    for (img_file <- 0 to array_size) {
      val img = ImageIO.read(new File("your_path" + array(img_file)))
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
      val percentage: Long = math.round((average * 100) / 255)
      println("Luminance: " + percentage + "%")
    }
  }

  get_files_from_folder()
}
