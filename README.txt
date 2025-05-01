DESIGN EXPLANATION:
To follow an MVC pattern, I created a model, view, and controller class.
The model implements the ImageProcessingModel interface which extends the ImageProcessingModelState interface,
which each represent a levels of functionality for the image processing model. The ImageProcessingModelState
interface contains getter methods of the model's state (width, height, and Color value at a specific row and column
value), and the ImageProcessingModel contains the methods that directly manipulate the Color values of an image
representation as defined by the assignment (flipping the image horizontally and vertically, inspecting
components by visualizing red, green, blue, value, intensity, and luma channels, brightening and darkening,
blurring and sharpening, and applying color filters like greyscale and sepia).
The ImageProcessingModel interface also has a saveImage method to save the image representation as an image
on the operating system.
The ImageProcessingModelImpl class implements the methods of both of these interfaces as well as others needed
for functionality, such as loading ppm or image files to convert into the Color array representation of the class
and making a copy of the model to be able to save multiple edits of the same image.
I also created an RGB enumeration for very minimal use in this class.
The ImageProcessingView interface contains one method that returns a JFrame of a given model, and the
ImageProcessingImageView class implements this method. This design is not yet incorporated in the controller,
but this view function exists separately for now.
The ImageProcessingController class acts as the driver class and contains void methods to collect and interpret input
and communicates with the model class as needed to carry out tasks interpreted from the user. There is also
a decent invalid input handling built into this controller, which redirects the user to enter valid input
until 'exit' is entered as input to exit the controller. This class also contains the main method for this program,
which "starts" the controller and opens the manually programmed command terminal.

IMAGE SOURCE:
https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.britannica.com
%2Fanimal%2Ftiger&psig=AOvVaw3zm72Se_a2FVnJMOjr1UOO&ust=1717802723050000&
source=images&cd=vfe&opi=89978449&ved=0CBIQjRxqFwoTCKD3k_qPyIYDFQAAAAAdAAAAABAE
- tiger image

- all other images are taken by me on my iphone!