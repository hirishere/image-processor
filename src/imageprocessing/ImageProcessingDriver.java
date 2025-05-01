package imageprocessing;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import imageprocessing.controller.ImageProcessingController;

/**
 * Driver class
 */
public class ImageProcessingDriver {

  /**
   * Main method
   *
   * @param args any command input args
   * @throws IOException if transmission fails
   */
  public static void main(String[] args) throws IOException {
    if (args.length == 0) {
      ImageProcessingController controller = new ImageProcessingController();
      controller.begin();
    }
    else {
      switch (args[0]) {
        case "-file":
          try {
            String filename = args[1];
            Scanner scanner = new Scanner(new File(filename));
            ImageProcessingController controller = new ImageProcessingController(scanner);
            controller.begin();
            scanner.close();
            break;
          }
          catch (ArrayIndexOutOfBoundsException e) {
            errorMessage();
            break;
        }
        case "-text":
          if (args.length != 1) {
            errorMessage();
            break;
          }
          go(args);
          break;
        default:
          errorMessage();
      }
    }
  }

  private static void errorMessage() {
    System.out.println("Invalid command-line input");
  }

  private static void go(String[] args) throws IOException {
    ImageProcessingController controller = new ImageProcessingController();
    controller.begin();
  }
}
