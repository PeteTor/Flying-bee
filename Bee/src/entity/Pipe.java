package entity;

import java.awt.Image;

public class Pipe {
	 static final int PIPE_WIDTH = 64;
	    public static final int PIPE_HEIGHT = 512;

	    public int x;
		public int y;
		public int width;
		public int height;
	    public Image img;
	    public boolean passed = false;

	    public Pipe(Image img, int x, int y) {
	        this.img = img;
	        this.x = x;
	        this.y = y;
	        this.width = PIPE_WIDTH;
	        this.height = PIPE_HEIGHT;
	    }
}
