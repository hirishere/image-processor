package imageprocessing.view;

import imageprocessing.model.Histograms;
import imageprocessing.model.ImageProcessingModel;

/**
 * This interface represents the operations that can be performed on an image processing view object.
 */
public interface ImageProcessingView {
  /**
   * Displays this view
   *
   */
  void view();

  /**
   * Updates this view with the given model, histogram, and error message
   *
   * @param currentModel current model object
   * @param currentHistograms current corresponding histograms object
   * @param currentError current error message
   */
  void updateView(ImageProcessingModel currentModel, Histograms currentHistograms, String currentError);

  /**
   * Updates this view with only a message
   *
   * @param error error message
   */
  void updateMessage(String error);
}
