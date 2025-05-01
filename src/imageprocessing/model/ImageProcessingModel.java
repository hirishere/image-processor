package imageprocessing.model;

import java.io.IOException;

/**
 * This interface represents the operations that can be performed on an image processing model.
 * One ImageProcessingModel object represents one image.
 */
public interface ImageProcessingModel extends ImageProcessingModelState {

  /**
   * Converts this image processing model to a greyscale image,
   * representing the given RGB's channel of an image
   *
   * @param rgb RGB value
   */
  void rgb(RGB rgb);

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the maximum value of its three components
   */
  void value();

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the average of its three components
   */
  void intensity();

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the weight sum of its three components
   * with formula 0.2126r + 0.7152g + 0.0722b
   */
  void luma();

  /**
   * Flips this image processing model
   * horizontally if given true
   * and vertically if given false
   *
   * @param horizontal boolean that determines horizontal/vertical flip
   */
  void flip(boolean horizontal);

  /**
   * Converts this image processing model to a brighter/darker image
   * based on the given positive/negative integer
   *
   * @param increment change to apply to each component of each pixel
   */
  void brighten(int increment);

  /**
   * Converts this image processing model to a blurred version
   */
  void blur();

  /**
   * Converts this image processing model to a sharpened version
   */
  void sharpen();

  /**
   * Converts this image processing model to greyscale by color transformation
   */
  void greyscale();

  /**
   * Converts this image processing model to greyscale by color transformation
   */
  void sepia();

  /**
   * Outputs this image processing model to the given filename
   *
   * @param filename file name
   * @throws IOException if this image processing model cannot be outputted
   */
  void saveImage(String filename) throws IOException;

  /**
   * Creates a copy of this ImageProcessingModel
   *
   * @return a new ImageProcessingModel object with the same representation
   */
  ImageProcessingModel makeCopy();

  /**
   * Creates maps of value to frequency entries
   * representing the distribution of colors in an image
   *
   * @return histograms of this image processing model
   */
  Histograms histograms();

  /**
   * Creates a map of value ot frequency entries
   * representing the distribution of greyscale values
   *
   * @return histogram of this image processing model
   */
  Histograms greyscaleHistogram();
}
