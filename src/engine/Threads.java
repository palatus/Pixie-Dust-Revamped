package engine;

import light.LightMap;

public class Threads {

	public static class LightmapThread extends Thread {
		
		private LightMap owner = null;
		private Runnable runnable;
		
	    public LightmapThread() {
	        super("lightMapThread");
	    }
	    
	    public LightmapThread(Runnable r, LightMap l) {
	        super(r);
	        this.owner = l;
	    }
	    
	    public void setRunnable(Runnable r) {
	    	
	    	runnable = r;
	    	
	    }
	    
	    public void run() {
	    	
	    	this.getRunnable().run();
	    	
	    }

		public LightMap getOwner() {
			return owner;
		}

		public void setOwner(LightMap owner) {
			this.owner = owner;
		}

		public Runnable getRunnable() {
			return runnable;
		}
		
		
		
	}
	
}
