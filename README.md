# Image Processor

A Java-based image processing application built using the Model-View-Controller (MVC) design pattern. The program supports both a GUI (built with Swing) and a script-based command-line interface. Users can load, transform, and save images with real-time visual feedback and histogram display.

## Project Structure

- **Model**  
  Represents images as a 2D `Color[][]` array. Supports operations such as flipping, brightening/darkening, blurring, sharpening, and applying greyscale/sepia color transformations. Implements the `ImageProcessingModel` and `ImageProcessingModelState` interfaces.

- **View**  
  Java Swing-based interface that displays the current image alongside its histogram. Also shows messages and errors in a status bar.

- **Controller**  
  Interprets and validates user commands, manages program flow, and coordinates updates between the model and view. Supports both interactive user input and external script execution.

## How to Run

### GUI Mode

Launch the graphical user interface:

java -jar ImageProcessor.jar

### Script Mode

Run a script file with image commands:

java -jar ImageProcessor.jar -file path/to/script.txt

Each line in the script must follow a supported command format. Lines beginning with `#` are treated as comments and ignored.

### Running From Source

You can also run the program by executing the `main()` method in `ImageProcessingController.java`.

## Example Script

Included as examplecommand.txt

Type `options` at the command prompt to display all supported commands.

## Supported Commands

- `load <image-path> <image-name>`
- `save <image-path> <image-name>`
- `horizontal-flip <image-name> <dest-image-name>`
- `vertical-flip <image-name> <dest-image-name>`
- `red-component <image-name> <dest-image-name>`
- `green-component <image-name> <dest-image-name>`
- `blue-component <image-name> <dest-image-name>`
- `value-component <image-name> <dest-image-name>`
- `luma-component <image-name> <dest-image-name>`
- `intensity-component <image-name> <dest-image-name>`
- `brighten <increment> <image-name> <dest-image-name>`
- `blur <image-name> <dest-image-name>`
- `sharpen <image-name> <dest-image-name>`
- `greyscale <image-name> <dest-image-name>`
- `sepia <image-name> <dest-image-name>`
- `-file <script.txt>` (to run commands from a script file)
- `exit` (to quit the program)

## Notes

- You must load and name an image before applying any operations.
- Invalid input is caught and handled with a message prompt; the program waits for valid input or the `exit` command.

## Image Credits

- Tiger image: [Britannica](https://www.britannica.com/animal/tiger)
- All other images used were taken by me!
