package engine;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;

import light.Light;
import light.LightMap;
import light.Particle;
import light.Pixel;

@SuppressWarnings("serial")
public class Window extends JComponent implements KeyListener, MouseListener{

	private JFrame stage; 
	private Dimension dimension;
	private String title;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private String backgroundURL = "";
	private Color bcolor = Color.GRAY;
	private boolean debug = true, moveUp, moveDown, moveLeft, moveRight, dragging = false;
	private double resolutionScale = 32, graphicsScale = 2;
	private int shiftX = 0, shiftY = 0;
	private int midShiftX = 0, midShiftY = 0;
	private int apparentMouseX = 0, apparentMouseY = 0;
	double hdRatio = 1;

	private Point lastDrag;
	
	private Timer controller = new Timer();
	
	private GraphicalObject focusObject = new GraphicalObject(-1); // game object to follow

	private Scene scene = new Scene(this); // current displayed scene
	
	public Window(String n) { // initialize window. initialize the JFrame object that Window, a child of JComponent, is a component of.
		
		
		this.setTitle(n);
		JFrame f = new JFrame();
		f.setTitle(n);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Make sure the frame is full screen
        f.setSize(screenSize.width , screenSize.height);
    	f.setUndecorated(true);
    	f.setVisible(true);
    	
		this.setStage(f);
		this.addKeyListener(this);
		this.addMouseListener(this);
		this.repaint();
		this.revalidate();
		this.setLayout(null);
		
		f.add(this);
		
		paintComponent(this.getGraphics());
		
		resolutionScale = resolutionScale();
		
		Dimension s = new Dimension((int)(screenSize.width/graphicsScale), (int)(screenSize.height/graphicsScale));
		
		this.getScene().setLightmap(LightMap.create(s, resolutionScale/graphicsScale , this)); // generate a Light Map (dimension d, pixelSizeSquared p)
		
		focusObject.setX(481);
		focusObject.setY(276);
		focusObject.setWidth(32);
		focusObject.setHeight(32);
		focusObject.setRender(false);

		this.getScene().getSceneObjects().getContents().add(focusObject);
		
//		controller.schedule(new TimerTask() {
//
//			@Override
//			public void run() {
//				
//				if(moveUp) {
//					getFocusObject().shift(0, -4); // UP
//				}
//				if(moveDown) {
//					getFocusObject().shift(0, 4); // DOWN
//				}
//				if(moveLeft) {
//					getFocusObject().shift(-4, 0); // LEFT
//				}
//				if(moveRight) {
//					getFocusObject().shift(4, 0); // RIGHT
//				}
//				
//			}
//			
//		}, 0, 10);
		
		graphicsScale = resolutionGraphicsScale();
		
	}
	
	public double resolutionScale() {
		
		double rs = 32;
		
		double ratio = 1; // default shading resolution for a LightMap is 6 based on a 1080p resolution
													  // Create a ratio based on detected screen size and apply it to rs.
		hdRatio = ratio;
		
		return Math.ceil(rs * ratio); // try to avoid strange aspect ratio interference
		
	}
	
	public double resolutionGraphicsScale() {
		
		double rs = 2;
		
		double ratio = 1; // default shading resolution for a LightMap is 6 based on a 1080p resolution
													  // Create a ratio based on detected screen size and apply it to rs.
		return Math.ceil(rs * ratio); // try to avoid strange aspect ratio interference
		
	}
	
	public Window() {
		this("Stage Name");
	}
	
	public void paintComponent(Graphics g) { // Graphics draw method

		GraphicalObject cameraObj = focusObject;
		Point camera = cameraObj.getLocation();
		
		Graphics2D g2 = (Graphics2D)g.create();
		g2.scale(this.getGraphicsScale(), this.getGraphicsScale());

		int midX = (int) (this.getScreenSize().width/(2*graphicsScale)); // Confusing math warning (!)
		int midY = (int) (this.getScreenSize().height/(2*graphicsScale));

		midX = (int) ((resolutionScale()/graphicsScale)*(Math.round(midX/(resolutionScale()/graphicsScale)))); // get rounded mid screen for graphics transformations
		midY = (int) ((resolutionScale()/graphicsScale)*(Math.round(midY/(resolutionScale()/graphicsScale))));
		midShiftX = midX;
		midShiftY = midY;
		
		int shiftNumX = (int) ((resolutionScale()/graphicsScale)*(Math.round(camera.getX()/(resolutionScale()/graphicsScale))) - midX); // Get rounded shift coordinates of screen for graphics transformations
		int shiftNumY = (int) ((resolutionScale()/graphicsScale)*(Math.round(camera.getY()/(resolutionScale()/graphicsScale))) - midY);

		shiftGraphics(g2, -shiftNumX, -shiftNumY); // offset coordinates
		
		bcolor = Color.white; // END CONFUSING MATH
		drawBackgound(g2);
		drawScene(g2);

		shiftPixels(g2, shiftNumX, shiftNumY);

		drawLightMap(g2);
		paintParticles(g2);
		
		shiftGraphics(g2, shiftNumX, shiftNumY); // back to real screen coordinates
		
		g2.scale(1/this.getGraphicsScale(), 1/this.getGraphicsScale());
		drawDebugStuff(g2);

		if(dragging)
			calcDrag();

		super.repaint();
		
	}
	
	public void paintParticles(Graphics2D g) {

		if (this.getLightmap() != null)
			for (int i = 0; i < this.getLightmap().getLights().size(); i++) {

				Light l = this.getLightmap().getLights().get(i);

				if (l instanceof Particle) {
					Particle p = (Particle) l;
					Color pc = new Color(l.getRed(), l.getGreen(), l.getBlue(), p.getSaturation());
					g.setColor(pc);
					g.fillRect(p.getLocation().x+p.getWidth()/2, p.getLocation().y+p.getHeight()/2, p.getEffectiveWidth(), p.getEffectiveHeight());
				}

			}
		
	}
	
	public void shiftPixels(Graphics2D g, int x, int y) {
		
		shiftX = x;
		shiftY = y;
		
	}
	
	public void shiftGraphics(Graphics2D g, int x, int y) {
		
		if (this.getLightmap() != null) {
			g.translate(x, y);
			
		}
		
	}
	
	public void drawLightMap(Graphics2D g) {

		if (this.getLightmap() != null) {
			
			this.getLightmap().setUpdateReady(true);
			for (int i = 0; i < this.getLightmap().getLitPixels().size(); i++) {

				Pixel p = this.getLightmap().getLitPixels().get(i);
				
				p.calculateColor(this.getScene().getLightmap().getLights(), this.getScene().getSceneObjects().getContents() ,g);
				
				drawPixel(g, p);
				
			}

			this.getScene().getLightmap().updateLights();

		} else {

		}

	}
	
	public void drawDebugStuff(Graphics2D g) {
		
		if(this.debug) {

			g.setColor(Color.GREEN.brighter());
			g.drawString("Resolution: "+this.getResolutionScale(), 32, 32);
			g.drawString("Graphics Scale: "+this.getGraphicsScale(), 32, 48);
			g.drawString("Shift Coordinates: ("+this.getFocusObject().getLocation()+")", 32, 64);
			if(dragging){
				g.drawString("Dragging", 32, 80);
			}
			
		}
		
	}
	
	public void drawPixel(Graphics2D g, Pixel p) {
		
		Color old = g.getColor();
		
		g.setColor(p.getRenderColor());
		
		g.fillRect(p.getRealLocation().x, p.getRealLocation().y, (int)p.getWidthPrecise(), (int)p.getHeightPrecise());
		
		g.setColor(old);
		
	}
	
	public void drawScene(Graphics2D g) { // The list of objects should be painted in reverse so that shadow casting objects are drawn on top
										  // This order must be maintained because of the shading algorithm - Shadow casters come first, in order
										  // for shadows to appear correctly on non-shadow casting objects.
		
		for(int i = this.getScene().getSceneObjects().getContents().size()-1; i>=0; i--) { // draw backwards
			
			GraphicalObject obj = this.getScene().getSceneObjects().getContents().get(i);

			if(!obj.doesRender()){
				continue;
			}

			if((obj.CastsShadows()) && obj.isCastingShadow()) {
				g.setColor(new Color(0.03f,0.03f,0.03f,1f));
				g.fillRect(obj.getX(), obj.getY(), obj.getEffectiveWidth(), obj.getEffectiveHeight());
			}
			
			if(obj != null && obj.getImageUrl() != null && obj.getImageUrl().length() > 2) {
				g.drawImage(new ImageIcon("Images\\"+obj.getImageUrl()).getImage(), obj.getX(), obj.getY(), obj.getEffectiveWidth(), obj.getEffectiveHeight(), null);
			} else {
				g.fillRect(obj.getX(), obj.getY(), obj.getEffectiveWidth(), obj.getEffectiveHeight());
			}
			
		}
		
		g.setColor(Color.blue);
		
	}
	
	public void rebuildLightMap() {
		
		Dimension s = new Dimension((int)(screenSize.width/graphicsScale), (int)(screenSize.height/graphicsScale));
		this.getLightmap().recreate(s, this.getResolutionScale()/graphicsScale);
		
	}

	public void drawBackgound(Graphics2D g) {
		
		g.setColor(bcolor);
		if(this.getBackgroundURL() == null || this.getBackgroundURL().length() == 0) {
			
			g.fillRect(0, 0, this.getScreenSize().width, this.getScreenSize().height);
			
		} else {

			g.drawImage(new ImageIcon("Images\\"+this.getBackgroundURL()).getImage(), 0, 0, null);
			
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) { // What to do with key presses
		
		switch (e.getKeyCode()) {

		case 68:
			moveRight = true;
			break;
		case 65:
			moveLeft = true;
			break;
		case 87:
			moveUp = true;
			break;
		case 83:
			moveDown = true;
			break;
			
		case 49:
			this.setGraphicsScale(this.getGraphicsScale()/2);
			rebuildLightMap();
			break;
		
		case 50:
			this.setGraphicsScale(this.getGraphicsScale()*2);
			rebuildLightMap();
			break;
		
		case 48:
			this.getLightmap().getTaggedLight("mouselight").setDistance(this.getLightmap().getTaggedLight("mouselight").getDistance()+16);
			break;
		
		case 57:
			this.getLightmap().getTaggedLight("mouselight").setDistance(this.getLightmap().getTaggedLight("mouselight").getDistance()-16);
			break;
			
		case 38:

			this.setResolutionScale(resolutionScale *2);
			rebuildLightMap();
			
			break;
			
		case 40:
			
			//this.getLightmap().getTaggedLight("").setDistance(this.getLightmap().getTaggedLight("").getDistance()-8);
			if (this.getResolutionScale() >= 2) {
				this.setResolutionScale(resolutionScale /2);
				rebuildLightMap();
			}
			
			break;
		
		case 27:
			System.exit(0);
			break;

			case KeyEvent.VK_R:
				scene.getLightmap().setShowRays(!scene.getLightmap().isShowRays());
				break;


		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		switch (e.getKeyCode()) {

		case 68:
			moveRight = false;
			break;
		case 65:
			moveLeft = false;
			break;
		case 87:
			moveUp = false;
			break;
		case 83:
			moveDown = false;
			break;
			
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {

		dragging = true;

	}

	public void calcDrag(){

		Point now = new Point((int) MouseInfo.getPointerInfo().getLocation().getX(), (int) (MouseInfo.getPointerInfo().getLocation().getY()));

		if(lastDrag != null) {

			int diffX = 0, diffY = 0;
			diffX = lastDrag.x - now.x;
			diffY = lastDrag.y - now.y;

			int modX = (int)Math.ceil(diffX/graphicsScale);
			int modY = (int)Math.ceil(diffY/graphicsScale);

			focusObject.shift(modX,modY);

		}

		lastDrag = now;

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {

		dragging = false;
		lastDrag = null;

	}

	public JFrame getStage() {
		return stage;
	}

	public void setStage(JFrame stage) {
		this.stage = stage;
	}

	public Dimension getDimension() {
		return dimension;
	}

	public void setDimension(Dimension dimension) {
		this.dimension = dimension;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Dimension getScreenSize() {
		return screenSize;
	}

	public void setScreenSize(Dimension screenSize) {
		this.screenSize = screenSize;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public String getBackgroundURL() {
		return backgroundURL;
	}

	public void setBackgroundURL(String backgroundURL) {
		this.backgroundURL = backgroundURL;
	}

	public GraphicalObject getFocusObject() {
		return focusObject;
	}

	public void setFocusObject(GraphicalObject focusObject) {
		this.focusObject = focusObject;
	}

	public Color getBcolor() {
		return bcolor;
	}

	public void setBcolor(Color bcolor) {
		this.bcolor = bcolor;
	}

	public LightMap getLightmap() {
		return this.getScene().getLightmap();
	}

	public void setLightmap(LightMap lightmap) {
		this.getScene().setLightmap(lightmap);
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public double getResolutionScale() {
		return resolutionScale;
	}

	public void setResolutionScale(double resolutionScale) {
		this.resolutionScale = resolutionScale;
	}

	public double getGraphicsScale() {
		return graphicsScale;
	}

	public void setGraphicsScale(double graphicsScale) {
		this.graphicsScale = graphicsScale;
	}

	public int getShiftY() {
		return shiftY;
	}

	public void setShiftY(int shiftY) {
		this.shiftY = shiftY;
	}

	public int getShiftX() {
		return shiftX;
	}

	public void setShiftX(int shiftX) {
		this.shiftX = shiftX;
	}

	public int getMidShiftY() {
		return midShiftY;
	}

	public void setMidShiftY(int midShiftY) {
		this.midShiftY = midShiftY;
	}

	public int getMidShiftX() {
		return midShiftX;
	}

	public void setMidShiftX(int midShiftX) {
		this.midShiftX = midShiftX;
	}

	public boolean isMoveUp() {
		return moveUp;
	}

	public void setMoveUp(boolean moveUp) {
		this.moveUp = moveUp;
	}

	public boolean isMoveDown() {
		return moveDown;
	}

	public void setMoveDown(boolean moveDown) {
		this.moveDown = moveDown;
	}

	public boolean isMoveLeft() {
		return moveLeft;
	}

	public void setMoveLeft(boolean moveLeft) {
		this.moveLeft = moveLeft;
	}

	public boolean isMoveRight() {
		return moveRight;
	}

	public void setMoveRight(boolean moveRight) {
		this.moveRight = moveRight;
	}

	public Timer getController() {
		return controller;
	}

	public void setController(Timer controller) {
		this.controller = controller;
	}

	public int getApparentMouseY() {
		return apparentMouseY;
	}

	public void setApparentMouseY(int apparentMouseY) {
		this.apparentMouseY = apparentMouseY;
	}

	public int getApparentMouseX() {
		return apparentMouseX;
	}

	public void setApparentMouseX(int apparentMouseX) {
		this.apparentMouseX = apparentMouseX;
	}
	
	public void changeCursor(String url) {
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(url);
		Cursor c = toolkit.createCustomCursor(image , new Point(this.getX(), 
		           this.getY()), "img");
		this.setCursor (c);
		
	}

	public double getHdRatio() {
		return hdRatio;
	}

	public void setHdRatio(double hdRatio) {
		this.hdRatio = hdRatio;
	}

	public Point getLastDrag() {
		return lastDrag;
	}

	public void setLastDrag(Point lastDrag) {
		this.lastDrag = lastDrag;
	}
}
