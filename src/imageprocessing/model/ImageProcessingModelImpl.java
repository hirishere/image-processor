package imageprocessing.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

import javax.imageio.ImageIO;

/**
 * Represents an image processing model (one image)
 */
public class ImageProcessingModelImpl implements ImageProcessingModel {
  /**
   * One field:
   * - image: a 2d Color array, each element represents a pixel
   */
  private Color[][] image;

  // helpful constant
  private static final RGB[] rgbs = new RGB[]{RGB.RED, RGB.GREEN, RGB.BLUE};

  /**
   * Default constructor, initializes the image array
   *
   * @param image array representing pixels of an image
   */
  public ImageProcessingModelImpl(Color[][] image) {
    this.image = image;
  }

  /**
   * Convenience constructor, initializes an array manually converted from a ppm or image file
   *
   * @param filename file name
   * @throws IOException if given an invalid file name
   */
  public ImageProcessingModelImpl(String filename) throws IOException {
    if (filename.endsWith(".ppm")) {
      this.loadImage(filename);
    } else {
      this.imageToArray(filename);
    }
  }

  /**
   * Initializes the image field of this image processing model
   * with a manual array built from the given ppm file
   *
   * @param filename ppm file name
   */
  private void loadImage(String filename) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File " + filename + " not found!");
      return;
    }

    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }

    //now set up the scanner to read from the string we just built
    sc = new Scanner(builder.toString());

    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    int height = sc.nextInt();
    this.image = new Color[height][width];

    for (int h = 0; h < height; h += 1) {
      for (int w = 0; w < width; w += 1) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        this.image[h][w] = new Color(r, g, b);
      }
    }
  }

  /**
   * Initializes the image field of this image processing model
   * with a manual array built from the given image file
   *
   * @param filename image file name
   * @throws IOException if given an invalid image file name
   */
  private void imageToArray(String filename) throws IOException {
    BufferedImage image = ImageIO.read(new File(filename));
    this.image = new Color[image.getHeight()][image.getWidth()];
    for (int h = 0; h < image.getHeight(); h += 1) {
      for (int w = 0; w < image.getWidth(); w += 1) {
        this.image[h][w] = new Color(image.getRGB(w, h));
      }
    }
  }

  /**
   * Converts this image processing model to a greyscale image,
   * representing the given RGB's channel of an image
   *
   * @param rgb RGB value
   */
  @Override
  public void rgb(RGB rgb) {
    switch (rgb) {
      case RED:
        for (int height = 0; height < this.getHeight(); height += 1) {
          for (int width = 0; width < this.getWidth(); width += 1) {
            int red = this.image[height][width].getRed();
            this.image[height][width] = new Color(red, red, red);
          }
        }
        break;
      case GREEN:
        for (int height = 0; height < this.getHeight(); height += 1) {
          for (int width = 0; width < this.getWidth(); width += 1) {
            int green = this.image[height][width].getGreen();
            this.image[height][width] = new Color(green, green, green);
          }
        }
        break;
      case BLUE:
        for (int height = 0; height < this.getHeight(); height += 1) {
          for (int width = 0; width < this.getWidth(); width += 1) {
            int blue = this.image[height][width].getBlue();
            this.image[height][width] = new Color(blue, blue, blue);
          }
        }
        break;
      default:
        throw new IllegalArgumentException("given color is not red, green, or blue");
    }
  }

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the maximum value of its three components
   */
  @Override
  public void value() {
    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = this.image[height][width].getRed();
        int green = this.image[height][width].getGreen();
        int blue = this.image[height][width].getBlue();
        int max = Math.max(red, Math.max(green, blue));
        this.image[height][width] = new Color(max, max, max);
      }
    }
  }

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the average of its three components
   */
  @Override
  public void intensity() {
    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = this.image[height][width].getRed();
        int green = this.image[height][width].getGreen();
        int blue = this.image[height][width].getBlue();
        int average = (red + green + blue) / 3;
        this.image[height][width] = new Color(average, average, average);
      }
    }
  }

  /**
   * Converts this image processing model to a greyscale image,
   * with each pixel representing the weight sum of its three components
   * with formula 0.2126r + 0.7152g + 0.0722b
   */
  @Override
  public void luma() {
    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = this.image[height][width].getRed();
        int green = this.image[height][width].getGreen();
        int blue = this.image[height][width].getBlue();
        int luma = (int) (red * .2126 + green * .7152 + blue * .0722);
        this.image[height][width] = new Color(luma, luma, luma);
      }
    }
  }

  /**
   * Flips this image processing model
   * horizontally if given true
   * and vertically if given false
   *
   * @param horizontal boolean that determines horizontal/vertical flip
   */
  @Override
  public void flip(boolean horizontal) {
    Color[][] newImage = new Color[this.getHeight()][this.getWidth()];
    if (horizontal) {
      for (int height = 0; height < this.getHeight(); height += 1) {
        for (int width = 0; width < this.getWidth(); width += 1) {
          Color left = this.image[height][width];
          Color right = this.image[height][this.getWidth() - width - 1];
          newImage[height][width] = right;
          newImage[height][this.getWidth() - width - 1] = left;
        }
      }
    } else {
      for (int width = 0; width < this.getWidth(); width += 1) {
        for (int height = 0; height < this.getHeight(); height += 1) {
          Color top = this.image[height][width];
          Color bottom = this.image[this.getHeight() - height - 1][width];
          newImage[height][width] = bottom;
          newImage[this.getHeight() - height - 1][width] = top;
        }
      }
    }
    this.image = newImage;
  }

  /**
   * Reverts a value outside the 0-255 numerical range to 0 or 255
   *
   * @param value number that should be within 0-255
   * @return the given value if it is already within the range, 0 if smaller, and 255 if larger
   */
  private static int clamp(int value) {
    if (value < 0) {
      return 0;
    } else if (value > 255) {
      return 255;
    }
    return value;
  }

  /**
   * Converts this image processing model to a brighter/darker image
   * based on the given positive/negative integer
   *
   * @param increment change to apply to each component of each pixel
   */
  @Override
  public void brighten(int increment) {
    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = clamp(this.image[height][width].getRed() + increment);
        int green = clamp(this.image[height][width].getGreen() + increment);
        int blue = clamp(this.image[height][width].getBlue() + increment);
        this.image[height][width] = new Color(red, green, blue);
      }
    }
  }

  /**
   * Computes the filtered value of the middle pixel of the given current array
   * section of this image processing model's image using the given filter kernel
   *
   * @param current a portion of this image as an integer array
   * @param kernel  filter kernel
   * @return the filtered value of the middle pixel
   */
  private int computeFilteredValue(Integer[][] current, Double[][] kernel) {
    double value = 0;
    for (int i = 0; i < current.length; i += 1) {
      for (int j = 0; j < current[0].length; j += 1) {
        value += (current[i][j] * kernel[i][j]);
      }
    }
    return (int) value;
  }

  /**
   * Determines the filtered value of a pixel at the given height and width
   * of this image processing model according to the given filter kernel
   * If portions of the kernel do not overlap with pixels,
   * the pixel value is assumed to be 0 (and therefore not included in the computation)
   *
   * @param h      height value of the current pixel
   * @param w      width value of the current pixel
   * @param kernel filter kernel
   * @return the filtered value of the pixel (of the current RGB channel)
   */
  private int filteredValue(int h, int w, Double[][] kernel) {
    Integer[][] current = new Integer[kernel.length][kernel[0].length];
    int midKernelH = kernel.length / 2;
    int midKernelW = kernel[0].length / 2;
    current[midKernelH][midKernelW] = this.image[h][w].getRed();
    for (int i = 0; i < kernel.length; i += 1) {
      for (int j = 0; j < kernel[0].length; j += 1) {
        int imageH;
        int imageW;

        if (i < midKernelH) {
          imageH = h - i;
        } else if (i > midKernelH) {
          imageH = h + i;
        } else {
          imageH = h;
        }

        if (j < midKernelW) {
          imageW = w - j;
        } else if (j > midKernelW) {
          imageW = w - j;
        } else {
          imageW = w;
        }

        try {
          Color color = this.image[imageH][imageW];
          // the red green blue values should be the same,
          // since we are accessing this method after converting to a channel representation of the image
          current[i][j] = color.getRed();
        } catch (ArrayIndexOutOfBoundsException e) {
          current[i][j] = 0;
        }
      }
    }
    return this.computeFilteredValue(current, kernel);
  }

  /**
   * Converts this image processing model to a filtered version
   * according to the given filter kernel (an array computation to apply to each pixel using its surrounding pixels)
   *
   * @param kernel kernel to apply to each pixel
   */
  private void filter(Double[][] kernel) {
    if (kernel.length % 2 == 0 || kernel[0].length % 2 == 0) {
      throw new IllegalArgumentException("kernel must have odd dimensions");
    }

    Integer[][] reds = new Integer[this.getHeight()][this.getWidth()];
    Integer[][] greens = new Integer[this.getHeight()][this.getWidth()];
    Integer[][] blues = new Integer[this.getHeight()][this.getWidth()];
    for (RGB rgb : rgbs) {
      ImageProcessingModelImpl editCopy = this.makeCopy();
      editCopy.rgb(rgb);
      for (int height = 0; height < this.getHeight(); height += 1) {
        for (int width = 0; width < this.getWidth(); width += 1) {
          int filtered = editCopy.filteredValue(height, width, kernel);
          switch (rgb) {
            case RED:
              reds[height][width] = filtered;
              break;
            case GREEN:
              greens[height][width] = filtered;
              break;
            case BLUE:
              blues[height][width] = filtered;
              break;
          }
        }
      }
    }

    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = clamp(reds[height][width]);
        int green = clamp(greens[height][width]);
        int blue = clamp(blues[height][width]);
        this.image[height][width] = new Color(red, green, blue);
      }
    }
  }

  /**
   * Converts this image processing model to a blurred version
   * by applying a filter kernel
   */
  @Override
  public void blur() {
    Double[][] kernel = new Double[][] {
            {0.0625, 0.125, 0.0625},
            {0.125, 0.25, 0.125},
            {0.0625, 0.125, 0.0625}
    };
    this.filter(kernel);
  }

  /**
   * Converts this image processing model to a sharpened version
   * by applying a filter kernel
   */
  @Override
  public void sharpen() {
    Double[][] kernel = new Double[][] {
            {-0.125, -0.125, -0.125, -0.125, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, 0.25, 1.0, 0.25, -0.125},
            {-0.125, 0.25, 0.25, 0.25, -0.125},
            {-0.125, -0.125, -0.125, -0.125, -0.125}
    };
    this.filter(kernel);
  }

  /**
   * Converts this image processing model by transforming its colors
   * according to the given matrix
   *
   * @param transform matrix to apply to each pixel
   */
  private void linearColorTransform(Double[][] transform) {
    if (transform.length != 3 || transform[0].length != 3) {
      throw new IllegalArgumentException("transform must have 3 rows and 3 columns");
    }
    for (int height = 0; height < this.getHeight(); height += 1) {
      for (int width = 0; width < this.getWidth(); width += 1) {
        int red = this.image[height][width].getRed();
        int green = this.image[height][width].getGreen();
        int blue = this.image[height][width].getBlue();
        double r = transform[0][0] * red + transform[0][1] * green + transform[0][2] * blue;
        double g = transform[1][0] * red + transform[1][1] * green + transform[1][2] * blue;
        double b = transform[2][0] * red + transform[2][1] * green + transform[2][2] * blue;
        red = clamp((int) r);
        green = clamp((int) g);
        blue = clamp((int) b);
        this.image[height][width] = new Color(red, green, blue);
      }
    }
  }

  /**
   * Converts this image processing model to greyscale by color transformation
   * through linear combination of each pixel's color values with a transform
   */
  @Override
  public void greyscale() {
    Double[][] transform = new Double[][] {
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722},
            {0.2126, 0.7152, 0.0722}
    };
    this.linearColorTransform(transform);
  }

  /**
   * Converts this image processing model to greyscale by color transformation
   * through linear combination of each pixel's color values with a transform
   */
  @Override
  public void sepia() {
    Double[][] transform = new Double[][] {
            {0.393, 0.769, 0.189},
            {0.349, 0.686, 0.168},
            {0.272, 0.534, 0.131}
    };
    this.linearColorTransform(transform);
  }

  /**
   * Outputs this image processing model to the given filename
   *
   * @param filename file name
   * @throws IOException if this image processing model cannot be outputted
   */
  @Override
  public void saveImage(String filename) throws IOException {
    FileWriter writer = new FileWriter(filename);
    StringBuilder builder = new StringBuilder();
    builder.append("P3\n");
    builder.append(this.getWidth()).append(" ").append(this.getHeight()).append("\n");
    builder.append("255\n");
    for (int h = 0; h < this.getHeight(); h += 1) {
      for (int w = 0; w < this.getWidth(); w += 1) {
        int red = this.image[h][w].getRed();
        int green = this.image[h][w].getGreen();
        int blue = this.image[h][w].getBlue();
        builder.append(red).append(" ").append(green).append(" ").append(blue).append(" \n");
      }
    }
    writer.write(builder.toString());
    writer.close();
  }

  /**
   * Creates a copy of this ImageProcessingModel
   *
   * @return a new ImageProcessingModel object with the same representation
   */
  @Override
  public ImageProcessingModelImpl makeCopy() {
    Color[][] copy = new Color[this.getHeight()][this.getWidth()];
    for (int h = 0; h < this.getHeight(); h += 1) {
      if (this.getWidth() >= 0) {
        System.arraycopy(this.image[h], 0, copy[h], 0, this.getWidth());
      }
    }
    return new ImageProcessingModelImpl(copy);
  }

  private static void addValue(int value, HashMap<Integer, Integer> hashmap) {
    if (hashmap.containsKey(value)) {
      hashmap.put(value, hashmap.get(value) + 1);
    }
    else {
      hashmap.put(value, 1);
    }
  }

  /**
   * Creates maps of value to frequency entries
   * representing the distribution of colors in an image
   *
   * @return map of values of entire image
   */
  @Override
  public Histograms histograms() {
    ArrayList<HashMap<Integer, Integer>> histograms = new ArrayList<>();
    HashMap<Integer, Integer> reds = new HashMap<>();
    HashMap<Integer, Integer> greens = new HashMap<>();
    HashMap<Integer, Integer> blues = new HashMap<>();
    ImageProcessingModelImpl copy = this.makeCopy();
    copy.intensity();
    HashMap<Integer, Integer> intensities = new HashMap<>();
    for (int h = 0; h < this.getHeight(); h += 1) {
      for (int w = 0; w < this.getWidth(); w += 1) {
        int red = this.image[h][w].getRed();
        addValue(red, reds);
        int green = this.image[h][w].getGreen();
        addValue(green, greens);
        int blue = this.image[h][w].getBlue();
        addValue(blue, blues);
        // can be any rgb value since intensity is an average
        int intensity = copy.image[h][w].getRed();
        addValue(intensity, intensities);
      }
    }
    if (reds.equals(greens) && greens.equals(blues)) {
      histograms.add(intensities);
    }
    else {
      histograms.add(reds);
      histograms.add(greens);
      histograms.add(blues);
      histograms.add(intensities);
    }
    return new Histograms(histograms);
  }

  /**
   * Creates a map of value ot frequency entries
   * representing the distribution of greyscale values
   *
   * @return histogram of this image processing model
   */
  @Override
  public Histograms greyscaleHistogram() {
    ArrayList<HashMap<Integer, Integer>> histograms = new ArrayList<>();
    HashMap<Integer, Integer> hashmap = new HashMap<>();
    for (int h = 0; h < this.getHeight(); h += 1) {
      for (int w = 0; w < this.getWidth(); w += 1) {
        // can be any value because this image is greyscale
        int value = this.image[h][w].getRed();
        addValue(value, hashmap);
      }
    }
    histograms.add(hashmap);
    return new Histograms(histograms);
  }

  /**
   * @return height of this image processing model
   */
  @Override
  public int getHeight() {
    return this.image.length;
  }

  /**
   * @return width of this image processing model
   */
  @Override
  public int getWidth() {
    return this.image[0].length;
  }

  /**
   * Gets the Color value at the given row and col value of this image processing model
   *
   * @param row row value
   * @param col col value
   * @return Color at the given row and col
   */
  @Override
  public Color pixelRGB(int row, int col) {
    return this.image[row][col];
  }

  /**
   * Overrides equals method
   *
   * @param o an object
   * @return true if the given is an ImageProcessingModelImpl
   * and has the same number of Color elements with the same values
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ImageProcessingModelImpl)) {
      return false;
    }
    ImageProcessingModelImpl other = (ImageProcessingModelImpl) o;
    if (this.getWidth() != other.getWidth() || this.getHeight() != other.getHeight()) {
      return false;
    }
    boolean isEqual = true;
    for (int i = 0; i < this.getHeight(); i += 1) {
      for (int j = 0; j < this.getWidth(); j += 1) {
        isEqual &= this.pixelRGB(i, j).equals(other.pixelRGB(i, j));
      }
    }
    return isEqual;
  }

  /**
   * Overrides hashCode method
   *
   * @return unique hash code from this image processing model's width and height
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.getWidth(), this.getHeight());
  }

}