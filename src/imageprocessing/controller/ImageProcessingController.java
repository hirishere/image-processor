package imageprocessing.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import imageprocessing.model.Histograms;
import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelImpl;
import imageprocessing.model.RGB;
import imageprocessing.view.ImageProcessingImageView;
import imageprocessing.view.ImageProcessingView;

/**
 * Represents an image processing controller
 */
public class ImageProcessingController {
  /**
   * Two fields:
   * - models: a map of names to image processing models
   * - view: an image processing view object
   * - scanner: an input object
   */
  private final HashMap<String, ImageProcessingModel> models;
  private ImageProcessingView view;
  private Scanner scanner;

  private ImageProcessingModel currentModel;
  private Histograms currentHistograms;
  private String currentError;

  /**
   * Default constructor, initializes the map and view
   *
   * @param models map of names to image processing models
   * @param view   an image processing view object
   */
  public ImageProcessingController(HashMap<String, ImageProcessingModel> models, ImageProcessingView view) {
    this.models = models;
    this.view = view;
    this.view.view();
    this.scanner = new Scanner(System.in);
  }

  /**
   * Empty constructor, initializes an empty map and an image processing image view object
   */
  public ImageProcessingController() {
    this(new HashMap<>(), new ImageProcessingImageView());
  }

  public ImageProcessingController(Scanner scanner) {
    this(new HashMap<>(), new ImageProcessingImageView(), scanner);
  }

  private void update(ImageProcessingModel model, Histograms histograms, String message) {
    if (model != null && histograms != null) {
      currentModel = model;
      currentHistograms = histograms;
    }
    currentError = message;
  }

  private void updateView() {
    if (currentModel != null && currentHistograms != null) {
      this.view.updateView(currentModel, currentHistograms, currentError);
      this.view.view();
    }
    else if (!currentError.isEmpty()) {
      this.view.updateMessage(currentError);
      this.view.view();
    }
  }

  /**
   * Default constructor for a specific scanner object
   *
   * @param scanner scanner object
   */
  public ImageProcessingController(HashMap<String, ImageProcessingModel> models, ImageProcessingView view, Scanner scanner) {
    this(models, view);
    this.scanner = scanner;
  }

  /**
   * Outputs a welcome message and prompts user input
   */
  public void begin() throws IOException {
    System.out.println("welcome to the image processing editor.");
    System.out.println("type options to see all command options");
    System.out.println("otherwise type your desired command!");
    this.interpret();
  }

  /**
   * Output message for invalid input
   */
  private void invalid() {
    System.out.println("\nthis is not a valid command option!");
    System.out.println("type \"options\" to see all command options");
    System.out.println("otherwise type a different, valid command");
    this.update(currentModel, currentHistograms, "error: invalid command entered");
    this.updateView();
  }

  /**
   * Output message for invalid path input
   *
   * @param loc path input
   */
  private void nonExistentPath(String loc) {
    System.out.println("\n" + loc + " is not an existing or accessible path.\n");
    this.update(currentModel, currentHistograms, "error: " + loc + " is not a valid path");
    this.updateView();
  }

  /**
   * Output message for invalid name input
   *
   * @param name name input
   */
  private void nonExistentName(String name) {
    System.out.println("\n" + name + " is not a saved image name in this program.\n");
    this.update(currentModel, currentHistograms, "error: " + name + " is not a saved image name");
    this.updateView();
  }

  /**
   * Output message for an existing name entered
   *
   * @param destName name entered
   */
  private void nameAlreadyExists(String destName) {
    System.out.println("\n" + destName + " is already an existing name in this program.");
    System.out.println("you can use \"load\" to overwrite " + destName + " with another file");
    this.update(currentModel, currentHistograms,"error: " + destName + " already exists");
    this.updateView();
  }

  /**
   * Primary driver method that interprets valid input and redirects for invalid input
   */
  private void interpret() throws IOException {
    boolean exit = false;
    while (!exit) {
      String command = scanner.nextLine();
      String[] inputs = command.split(" ");
      switch (inputs.length) {
        case 1:
          switch (inputs[0]) {
            case "options":
              this.userOptions();
              break;
            case "exit":
              exit = true;
              scanner.close();
              System.exit(0);
              break;
            default:
              this.invalid();
          }
          break;
        case 2:
          String loc = inputs[1];
          if (inputs[0].equals("-file")) {
            exit = this.acceptScript(loc);
          } else {
            this.invalid();
          }
          break;
        case 3:
          String loc1 = inputs[1];
          String loc2 = inputs[2];
          switch (inputs[0]) {
            case "load":
              this.loadImage(loc1, loc2);
              break;
            case "save":
              this.saveImage(loc1, loc2);
              break;
            case "horizontal-flip":
              this.edit("horizontal", loc1, loc2);
              break;
            case "vertical-flip":
              this.edit("vertical", loc1, loc2);
              break;
            case "red-component":
              this.edit("red", loc1, loc2);
              break;
            case "green-component":
              this.edit("green", loc1, loc2);
              break;
            case "blue-component":
              this.edit("blue", loc1, loc2);
              break;
            case "value-component":
              this.edit("value", loc1, loc2);
              break;
            case "luma-component":
              this.edit("luma", loc1, loc2);
              break;
            case "intensity-component":
              this.edit("intensity", loc1, loc2);
              break;
            case "blur":
              this.edit("blur", loc1, loc2);
              break;
            case "sharpen":
              this.edit("sharpen", loc1, loc2);
              break;
            case "greyscale":
              this.edit("greyscale", loc1, loc2);
              break;
            case "sepia":
              this.edit("sepia", loc1, loc2);
              break;
            default:
              this.invalid();
          }
          break;
        case 4:
          if (inputs[0].equals("brighten")) {
            String inc = inputs[1];
            String name = inputs[2];
            String destName = inputs[3];
            this.brighten(inc, name, destName);
          } else {
            this.invalid();
          }
      }
    }
  }

  /**
   * Loads an image processing model given the file path by creating a new model,
   * and updates the models map of this controller with the given name and created model
   * Outputs a non-existent path message if path does not exist
   *
   * @param path operating system file name
   * @param name program name
   */
  private void loadImage(String path, String name) {
    try {
      ImageProcessingModel model = new ImageProcessingModelImpl(path);
      this.models.put(name, model);
      this.update(model, model.histograms(), "");
      this.updateView();
      System.out.println();
    } catch (IOException e) {
      this.nonExistentPath(path);
    }
  }

  /**
   * Saves an image given the name (retrieves from the models map)
   * to the given path
   * Outputs a non-existent name message if name does not exist
   *
   * @param path file to save image to
   * @param name name of image processing model in system
   */
  private void saveImage(String path, String name) throws IOException {
    if (!this.models.containsKey(name)) {
      this.nonExistentName(name);
      return;
    }
    this.models.get(name).saveImage(path);
    System.out.println();
  }

  private boolean validInput(String lines) {
    Scanner sc = new Scanner(lines);
    boolean valid = false;
    while (sc.hasNextLine()) {
      String line = sc.nextLine();
      String[] inputs = line.split(" ");
      String command = inputs[0];
      ArrayList<String> commands = new ArrayList<>(Arrays.asList("options", "exit", "-file", "load", "save",
      "horizontal-flip", "vertical-flip", "red-component", "green-component", "blue-component",
      "value-component", "luma-component", "intensity-component", "blur", "sharpen", "greyscale", "sepia"));
      if (commands.contains(command)) {
        valid = true;
      }
    }
    return valid;
  }

  /**
   * Accepts a txt file and reads script,
   * continues if no command line options are in the given file
   * @param filename file name
   */
  private boolean acceptScript(String filename) throws IOException {
    Scanner sc;
    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("file " + filename + " not found!\n");
      return false;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      char comment = '#';
      if (s.charAt(0) != comment) {
        builder.append(s).append(System.lineSeparator());
      }
    }
    //now set up the scanner to read from the string we just built
    if (this.validInput(builder.toString())) {
      sc = new Scanner(builder + System.lineSeparator() + "exit" + System.lineSeparator());
      ImageProcessingController controller = new ImageProcessingController(models, view, sc);
      controller.interpret();
      return true;
    }
    else {
      System.out.println("file " + filename + " has no command lines.\n");
      return false;
    }
  }

  /**
   * Implements the given edit (as a String) with the given name and destination name
   * Supports horizontal flip, vertical flip, red channel, green channel, blue channel,
   * value representation, luma representation, intensity representation,
   * blur, and sharpen functions
   * Updates the models map accordingly
   *
   * @param edit     name of edit to be implemented
   * @param name     name of image processing model to be edited
   * @param destName name for the edited model to be saved as
   */
  private void edit(String edit, String name, String destName) {
    if (!this.models.containsKey(name)) {
      this.nonExistentName(name);
      return;
    }
    if (this.models.containsKey(destName)) {
      this.nameAlreadyExists(destName);
      return;
    }
    ImageProcessingModel newModel = this.models.get(name).makeCopy();
    Histograms histograms = new Histograms();
    switch (edit) {
      case "horizontal":
        newModel.flip(true);
        histograms = newModel.histograms();
        break;
      case "vertical":
        newModel.flip(false);
        histograms = newModel.histograms();
        break;
      case "red":
        newModel.rgb(RGB.RED);
        histograms = newModel.greyscaleHistogram();
        break;
      case "green":
        newModel.rgb(RGB.GREEN);
        histograms = newModel.greyscaleHistogram();
        break;
      case "blue":
        newModel.rgb(RGB.BLUE);
        histograms = newModel.greyscaleHistogram();
        break;
      case "value":
        newModel.value();
        histograms = newModel.greyscaleHistogram();
        break;
      case "luma":
        newModel.luma();
        histograms = newModel.greyscaleHistogram();
        break;
      case "intensity":
        newModel.intensity();
        histograms = newModel.greyscaleHistogram();
        break;
      case "blur":
        newModel.blur();
        histograms = newModel.histograms();
        break;
      case "sharpen":
        newModel.sharpen();
        histograms = newModel.histograms();
        break;
      case "greyscale":
        newModel.greyscale();
        histograms = newModel.greyscaleHistogram();
        break;
      case "sepia":
        newModel.sepia();
        histograms = newModel.greyscaleHistogram();
        break;
    }
    this.models.put(destName, newModel);
    this.update(newModel, histograms, "");
    this.updateView();
    System.out.println();
  }

  /**
   * Edit method alternative with 3 additional inputs
   * Supports the brighten function
   *
   * @param inc      inputted increment
   * @param name     name of image processing model to be edited
   * @param destName name for the edited model to  be saved as
   */
  private void brighten(String inc, String name, String destName) {
    int increment;
    try {
      increment = Integer.parseInt(inc);
    } catch (NumberFormatException e) {
      this.invalid();
      return;
    }
    if (!this.models.containsKey(name)) {
      this.nonExistentName(name);
      return;
    }
    if (this.models.containsKey(destName)) {
      this.nameAlreadyExists(destName);
      return;
    }
    ImageProcessingModel newModel = this.models.get(name).makeCopy();
    newModel.brighten(increment);
    this.models.put(destName, newModel);
    this.update(newModel, newModel.histograms(), "");
    this.updateView();
    System.out.println();
  }

  /**
   * Outputs the total list of accepted user input by this program
   */
  private void userOptions() {
    System.out.println("\nload <image-path> <image-name>" +
            "\n-- loads an image from a given path and define a name for reference" +
            "\n-- can also overwrite a name with a given path");
    System.out.println("\nsave <image-path> <image-name>" +
            "\n-- saves an image to the specified path (should include the name of the file)");
    System.out.println("\nhorizontal-flip <image-name> <dest-image-name>" +
            "\n-- flips an image horizontally");
    System.out.println("\nvertical-flip <image-name> <dest-image-name>" +
            "\n-- flips an image vertically");
    System.out.println("\nred-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the red-component of an image");
    System.out.println("\ngreen-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the green-component of an image");
    System.out.println("\nblue-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the blue-component of an image");
    System.out.println("\nvalue-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the value-component of an image");
    System.out.println("\nluma-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the luma-component of an image");
    System.out.println("\nintensity-component <image-name> <dest-image-name>" +
            "\n-- creates a greyscale image with the intensity-component of an image");
    System.out.println("\nbrighten <increment> <image-name> <dest-image-name>" +
            "\n-- brightens an image by the given increment;" +
            " the increment may be positive (brightening) or negative (darkening))");
    System.out.println("\nblur <image-name> <dest-image-name>" +
            "\n-- creates a blurred image of an image");
    System.out.println("\nsharpens <image-name> <dest-image-name>" +
            "\n-- creates a sharpened image of an image");
    System.out.println("\ngreyscale <image-name> <dest-image-name>" +
            "\n-- creates a greyscale colored image of an image");
    System.out.println("\nsepia <image-name> <dest-image-name>" +
            "\n-- creates a sepia colored image of an image");
    System.out.println("\n-file <name-of-script.txt>" +
            "\n-- accepts a script to run and exits; " +
            "command line will continue if text file has no command line options");
    System.out.println("\nexit\n" +
            "-- exit the system\n");
  }
}

