import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.IOException;

import imageprocessing.model.ImageProcessingModel;
import imageprocessing.model.ImageProcessingModelImpl;
import imageprocessing.model.RGB;

import static org.junit.Assert.assertEquals;

public class ImageProcessingTest {

  ImageProcessingModel model;

  @Before
  public void setUp() throws IOException {
    model = new ImageProcessingModelImpl("Koala.ppm");
  }

  @Test
  public void testFlipHorizontalAndVertical() throws IOException {
    model.flip(true);
    ImageProcessingModel horizontal = new ImageProcessingModelImpl("koala-horizontal.png");
    assertEquals(horizontal, model);
    model.flip(false);
    ImageProcessingModel horizontalvertical = new ImageProcessingModelImpl("koala-horizontal-vertical.png");
    assertEquals(horizontalvertical, model);
    model.flip(true);
    ImageProcessingModel vertical = new ImageProcessingModelImpl("koala-vertical.png");
    assertEquals(vertical, model);
  }

  @Test
  public void testRed() throws IOException {
    model.rgb(RGB.RED);
    ImageProcessingModel redGreyscale = new ImageProcessingModelImpl("koala-red-greyscale.png");
    assertEquals(redGreyscale, model);
  }

  @Test
  public void testGreen() throws IOException {
    model.rgb(RGB.GREEN);
    ImageProcessingModel greenGreyscale = new ImageProcessingModelImpl("koala-green-greyscale.png");
    assertEquals(greenGreyscale, model);
  }

  @Test
  public void testBlue() throws IOException {
    model.rgb(RGB.BLUE);
    ImageProcessingModel blueGreyscale = new ImageProcessingModelImpl("koala-blue-greyscale.png");
    assertEquals(blueGreyscale, model);
  }

  @Test
  public void testValue() throws IOException {
    model.value();
    ImageProcessingModel value = new ImageProcessingModelImpl("koala-value-greyscale.png");
    assertEquals(value, model);
  }

  @Test
  public void testLuma() throws IOException {
    model.luma();
    ImageProcessingModel luma = new ImageProcessingModelImpl("koala-luma-greyscale.png");
    assertEquals(luma, model);
  }

  @Test
  public void testIntensity() throws IOException {
    model.intensity();
    ImageProcessingModel intensity = new ImageProcessingModelImpl("koala-intensity-greyscale.png");
    assertEquals(intensity, model);
  }

  @Test
  public void testBrighten() throws IOException {
    ImageProcessingModel modelCopy = model.makeCopy();
    model.brighten(50);
    ImageProcessingModel brighter = new ImageProcessingModelImpl("koala-brighter-by-50.png");
    assertEquals(brighter, model);
    model.brighten(50);
    modelCopy.brighten(100);
    assertEquals(model, modelCopy);
  }
}
