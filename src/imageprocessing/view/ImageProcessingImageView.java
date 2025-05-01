package imageprocessing.view;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import imageprocessing.model.Histograms;
import imageprocessing.model.ImageProcessingModel;

public class ImageProcessingImageView extends JFrame implements ImageProcessingView {

  private JLabel display;
  private JLabel graph;
  private JLabel message;

  public ImageProcessingImageView() {
    super();
    setSize(1200, 800);
  }

  public ImageProcessingImageView(ImageProcessingModel image, Histograms histograms, String message) {
    this();
    this.updateView(image, histograms, message);
  }

  public ImageProcessingImageView(ImageProcessingModel image, Histograms histograms) {
    this(image, histograms, "");
  }

  /**
   * Converts the given image state to an image and displays it on this frame
   *
   * @param model an image processing model
   */
  private void imageDisplay(ImageProcessingModel model) {
    BufferedImage image = new BufferedImage(model.getWidth(),
            model.getHeight(), BufferedImage.TYPE_INT_RGB);
    for (int x = 0; x < model.getHeight() - 1; x += 1) {
      for (int y = 0; y < model.getWidth() - 1; y += 1) {
        image.setRGB(y, x, model.pixelRGB(x, y).getRGB());
      }
    }
    this.display = new JLabel(new ImageIcon(image));
  }

  /**
   * Converts the given histogram values to a graph display
   *
   * @param histograms list of hashmaps to convert to graph
   */
  private void createGraph(Histograms histograms) {
    this.graph = new JLabel(new ImageIcon(histograms.createGraph()));
  }

  private void renderMessage(String message) {
    this.message = new JLabel(message);
  }

  /**
   * Displays this view
   *
   */
  @Override
  public void view() {
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  /**
   * Updates this view with the given model, histogram, and error message
   *
   * @param currentModel current model object
   * @param currentHistograms current corresponding histograms object
   * @param currentError current error message
   */
  @Override
  public void updateView(ImageProcessingModel currentModel, Histograms currentHistograms, String currentError) {
    this.imageDisplay(currentModel);
    JPanel imagePanel = new JPanel();
    imagePanel.add(this.display);
    imagePanel.setLayout(new BoxLayout(imagePanel, BoxLayout.PAGE_AXIS));
    JScrollPane imageScrollPane = new JScrollPane(imagePanel);

    this.createGraph(currentHistograms);
    JPanel graphPanel = new JPanel();
    graphPanel.add(this.graph);
    graphPanel.setLayout(new BoxLayout(graphPanel, BoxLayout.PAGE_AXIS));
    JScrollPane graphScrollPane = new JScrollPane(graphPanel);

    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, imageScrollPane, graphScrollPane);
    splitPane.setDividerLocation(800);

    this.renderMessage(currentError);
    JPanel textPanel = new JPanel();
    JLabel textLabel = this.message;
    textPanel.add(textLabel);

    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(splitPane, BorderLayout.CENTER);
    this.getContentPane().add(textPanel, BorderLayout.SOUTH);
  }

  /**
   * Updates this view with only a message
   *
   * @param error error message
   */
  public void updateMessage(String error) {
    this.renderMessage(error);
    JPanel textPanel = new JPanel();
    JLabel textLabel = this.message;
    textPanel.add(textLabel);
    this.getContentPane().setLayout(new BorderLayout());
    this.getContentPane().add(textPanel, BorderLayout.SOUTH);
  }
}
