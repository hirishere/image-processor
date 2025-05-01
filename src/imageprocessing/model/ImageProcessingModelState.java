package imageprocessing.model;

import java.awt.*;

/**
 * This interface represents the operations that can be performed on an image processing model's state
 * without changing it.
 */
public interface ImageProcessingModelState {

  /**
   * @return height of this image processing model
   */
  int getHeight();

  /**
   * @return width of this image processing model
   */
  int getWidth();

  /**
   * Gets the Color value at the given row and col value of this image processing model
   *
   * @param row row value
   * @param col col value
   * @return Color at the given row and col
   */
  Color pixelRGB(int row, int col);
}
