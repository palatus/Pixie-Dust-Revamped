package light;

import java.awt.*;

public class Threads {

	public static class LightmapThread extends Thread {

	    private Pixel[] pixels;
		private LightMap lightmap;
		private Graphics2D graphics2D;

	    // Will calculate a given set of pixels from the lightmap
	    public LightmapThread(LightMap l, Pixel[] p, Graphics2D g) {

	        pixels = p;
	        lightmap = l;
			graphics2D = g;

	    }
	    
	    public void run() {
	    	
	    	if(!lightmap.getOwner().isUseThreads()) {
	    		while(!lightmap.getOwner().isUseThreads()) {
		    		try {
						this.wait(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}
	    	}
				for (int i = 0; i < pixels.length; i++) {
					Pixel p = pixels[i];
					p.calculateColor(lightmap.getLights(), lightmap.getOwner().getScene().getSceneObjects().getContents(), graphics2D);
					
					lightmap.getOwner().drawPixel(graphics2D, p);
					
				}

		}
		
	}
	
	public static class FPSCounter extends Thread{
		
	    private long lastTime;
	    private double fps; 

	    public void run(){
	        while (true){
	            lastTime = System.nanoTime();
	            
	            try{
	                Thread.sleep(1000);
	            }
	            catch (InterruptedException e){

	            }
	            fps = 1000000000.0 / (System.nanoTime() - lastTime);
	            lastTime = System.nanoTime();
	        }
	    }
	    public double fps(){
	        return fps;
	    } 
	}
	
}
