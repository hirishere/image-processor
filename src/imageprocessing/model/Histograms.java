package imageprocessing.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a collection of value to frequency maps
 */
public class Histograms {
  /**
   * - histograms: list of maps
   */
  private final ArrayList<HashMap<Integer, Integer>> histograms;

  /**
   * bar width constant for bar graphs
   */
  private static final int BAR_WIDTH = 5;

  /**
   * Default constructor, initializes histograms to the given list of maps
   *
   * @param histograms list of maps
   */
  public Histograms(ArrayList<HashMap<Integer, Integer>> histograms) {
    this.histograms = histograms;
  }

  /**
   * Empty constructor, initializes histograms to an empty list
   */
  public Histograms() {
    this(new ArrayList<>());
  }

  /**
   * Creates an image of a graph or graphs representing the date in the histograms list
   *
   * @return image representation
   */
  public BufferedImage createGraph() {
    if (histograms.size() == 1) {
      return this.barGraph(this.histograms.get(0), Color.BLACK, "greyscale values");
    } else {
      BufferedImage reds = this.barGraph(this.histograms.get(0), Color.RED, "red component values");
      BufferedImage greens = this.barGraph(this.histograms.get(1), Color.GREEN, "green component values");
      BufferedImage blues = this.barGraph(this.histograms.get(2), Color.BLUE, "blue component values");
      BufferedImage intensities = this.barGraph(this.histograms.get(3), Color.BLACK, "intensity (average) values");
      int totalWidth = reds.getWidth() + greens.getWidth() + blues.getWidth() + intensities.getWidth();
      BufferedImage allbarGraphs = new BufferedImage(totalWidth, 800, BufferedImage.TYPE_INT_RGB);
      Graphics g = allbarGraphs.createGraphics();
      g.setColor(Color.WHITE);
      g.fillRect(0, 0, totalWidth, 800);
      g.drawImage(reds, 0, 0, null);
      g.drawImage(greens, 0, 200, null);
      g.drawImage(blues, 0, 400, null);
      g.drawImage(intensities, 0, 600, null);
      g.dispose();
      return allbarGraphs;
    }
  }

  /**
   * Creates a bar graph image given a hashmap, a color value, and a graph title
   *
   * @param hashmap map of values
   * @param color   color value
   * @param title   graph title
   * @return bar graph image
   */
  private BufferedImage barGraph(HashMap<Integer, Integer> hashmap, Color color, String title) {
    int graphWidth = BAR_WIDTH * hashmap.size();
    int graphHeight = 200;

    BufferedImage image = new BufferedImage(graphWidth, graphHeight, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.createGraphics();

    g.setColor(Color.WHITE);
    g.fillRect(0, 0, graphWidth, graphHeight);

    g.setColor(Color.BLACK);
    g.setFont(new Font("Arial", Font.BOLD, 16));
    FontMetrics fontMetrics = g.getFontMetrics();
    int titleWidth = fontMetrics.stringWidth(title);
    g.drawString(title, (graphWidth - titleWidth) / 2, fontMetrics.getAscent());

    int x = 0;
    int yBase = graphHeight - 50;
    int maxValue = hashmap.values().stream().max(Integer::compareTo).orElse(0);
    for (Map.Entry<Integer, Integer> entry : hashmap.entrySet()) {
      int barHeight = (int) ((double) entry.getValue() / maxValue * (graphHeight - 100));
      g.setColor(color);
      g.fillRect(x, yBase - barHeight, BAR_WIDTH, barHeight);

//      g.setColor(Color.BLACK);
//      g.drawString(String.valueOf(entry.getKey()), x + BAR_WIDTH
//                      / 2 - g.getFontMetrics().stringWidth(String.valueOf(entry.getKey())) / 2,
//              yBase + 20);
      x += BAR_WIDTH;
    }

    g.dispose();
    return image;
  }
}