package light;

import java.awt.Color;

public class Colors {

	public static Color blend(Color c1, Color c2, float ratio) {

		if (ratio > 1f)
			ratio = 1f;
		else if (ratio < 0f)
			ratio = 0f;
		float iRatio = 1.0f - ratio;

		int i1 = c1.getRGB();
		int i2 = c2.getRGB();

		int a1 = (i1 >> 24 & 0xff);
		int r1 = ((i1 & 0xff0000) >> 16);
		int g1 = ((i1 & 0xff00) >> 8);
		int b1 = (i1 & 0xff);

		int a2 = (i2 >> 24 & 0xff);
		int r2 = ((i2 & 0xff0000) >> 16);
		int g2 = ((i2 & 0xff00) >> 8);
		int b2 = (i2 & 0xff);

		int a = (int) ((a1 * iRatio) + (a2 * ratio));
		int r = (int) ((r1 * iRatio) + (r2 * ratio));
		int g = (int) ((g1 * iRatio) + (g2 * ratio));
		int b = (int) ((b1 * iRatio) + (b2 * ratio));

		return new Color(a << 24 | r << 16 | g << 8 | b);

	}
	
	public static int getBlackness(Color c) { // find how dark a color is relative to a location
		
		float blackness = 0f;
		
		blackness += 255 - c.getRed(); // get avg difference between a color and full white light
		blackness += 255 - c.getGreen();
		blackness += 255 - c.getBlue();
		blackness /= 3;
		
		return (int) Math.ceil(blackness);
		
	}
	
	  public static Color blendB (Color c0, Color c1) {
		    double totalAlpha = c0.getAlpha() + c1.getAlpha();
		    double weight0 = c0.getAlpha() / totalAlpha;
		    double weight1 = c1.getAlpha() / totalAlpha;

		    double r = weight0 * c0.getRed() + weight1 * c1.getRed();
		    double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
		    double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
		    double a = Math.max(c0.getAlpha(), c1.getAlpha());

		    return new Color((int) r, (int) g, (int) b, (int) a);
		  }

}
