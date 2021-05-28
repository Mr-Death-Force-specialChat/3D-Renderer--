package renderer;

import renderer.input.ClickType;
import renderer.input.Mouse;
import renderer.point.MyPoint;
import renderer.point.PointConverter;
import renderer.shapes.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serial;

public class Display extends Canvas implements Runnable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Thread thread;
    private JFrame frame;
    private static String title = "3D Renderer";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static boolean running = false;
    static int MouseButton = 0;
    static int mouseState = 0;
    int MXFrames = 0;

    private Tetrahedon tetra;

    private Mouse mouse;

    public Display(){
        this.frame = new JFrame();

        Dimension size = new Dimension(WIDTH, HEIGHT);
        this.setPreferredSize(size);

        this.mouse = new Mouse();

        this.addMouseListener(this.mouse);
        this.addMouseMotionListener(this.mouse);
        this.addMouseWheelListener(this.mouse);
    }

    public static void main(String[] args){
        Display display = new Display();
        display.frame.setTitle(title);
        display.frame.add(display);
        display.frame.pack();
        display.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.frame.setLocationRelativeTo(null);
        display.frame.setResizable(false);
        display.frame.setVisible(true);

        display.start();
    }

    public synchronized void start(){
        running = true;
        this.thread = new Thread(this, "renderer.Display System");
        this.thread.start();
    }

    public synchronized void stop(){
        running = false;
        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60;
        double delta = 0;
        int frames = 0;
        MXFrames = 0;

        init();

        while (running){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                delta--;
                frames++;
                render();
            }
            MXFrames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                this.frame.setTitle(title + " " + " | " + frames + " FPS | " + MXFrames + " RendersPerSecond");
                frames = 0;
                MXFrames = 0;
            }
        }

        stop();
    }

    private void init(){
        int s = 100;
        MyPoint p1 = new MyPoint(s/2, -s/2, -s/2);
        MyPoint p2 = new MyPoint(s/2, s/2, -s/2);
        MyPoint p3 = new MyPoint(s/2, s/2, s/2);
        MyPoint p4 = new MyPoint(s/2, -s/2, s/2);
        MyPoint p5 = new MyPoint(-s/2, -s/2, -s/2);
        MyPoint p6 = new MyPoint(-s/2, s/2, -s/2);
        MyPoint p7 = new MyPoint(-s/2, s/2, s/2);
        MyPoint p8 = new MyPoint(-s/2, -s/2, s/2);
        this.tetra = new Tetrahedon(
                new MyPolygon(Color.blue, p5, p6, p7, p8),
                new MyPolygon(Color.white, p1, p2, p6, p5),
                new MyPolygon(Color.yellow, p1, p5, p8, p4),
                new MyPolygon(Color.green, p2, p6, p7, p3),
                new MyPolygon(Color.orange, p4, p3, p7, p8),
                new MyPolygon(Color.red, p1, p2, p3, p4));
    }

    private void render(){
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH * 2, HEIGHT * 2);

        tetra.render(g);

        g.setColor(Color.cyan);
        g.drawString("Frame: " + this.MXFrames, 0, 10);
        g.drawString("MouseX: " + this.mouse.getMouseX(), 0, 22);
        g.drawString("MouseY: " + this.mouse.getMouseY(), 0, 36);
        g.drawString("MouseDown: " + MouseButton, 0, 50);
        g.drawString("scroll:  " + mouseState, 0, 64);
        g.drawString("zoom:  " + PointConverter.scale, 0, 78);
        if (this.mouse.getButton() == ClickType.LeftClick)
            MouseButton = 1;
        else if (this.mouse.getButton() == ClickType.ScrollClick)
            MouseButton = 2;
        else if (this.mouse.getButton() == ClickType.RightClick)
            MouseButton = 3;
        else if (this.mouse.getButton() == ClickType.Back)
            MouseButton = 4;
        else if (this.mouse.getButton() == ClickType.Front)
            MouseButton = 5;
        else if (this.mouse.getButton() == ClickType.Unknown)
            MouseButton = 0;

        g.dispose();
        bs.show();
    }

    ClickType prevMouse = ClickType.Unknown;
    int initialX;
    int initialY;
    int initialZ;
    private void update(){
        int x = this.mouse.getMouseX();
        int y = this.mouse.getMouseY();
        double mouseSensitivity = 2.5;
        if (this.mouse.getButton() == ClickType.LeftClick){

            int xDiff = x - initialX;
            int yDiff = y - initialY;

            this.tetra.rotate(true, 0, -yDiff/mouseSensitivity, -xDiff/mouseSensitivity);

        }
        else if (this.mouse.getButton() == ClickType.RightClick){

            int xDiff = x - initialX;

            this.tetra.rotate(true, xDiff/mouseSensitivity, 0,0);

        }
        else if (this.mouse.getButton() == ClickType.Back){
            this.tetra.rotate(false, 0.5, 0, 0);
        }
        else if (this.mouse.getButton() == ClickType.Front){
            this.tetra.rotate(true, 0.5, 0, 0);
        }

        if (this.mouse.isScrollingUp()) {
            PointConverter.zoomIn();
            mouseState = 1;
        }
        else if (this.mouse.isScrollingDown()) {
            PointConverter.zoomOut();
            mouseState = -1;
        }
        else
            mouseState = 0;

        this.mouse.reset();

        initialX = x;
        initialY = y;

    }
}
