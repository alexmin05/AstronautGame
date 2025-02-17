//Basic Game Application
//Version 2
// Basic Object, Image, Movement
// Astronaut moves to the right.
// Threaded

//K. Chun 8/2018

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************
// Class Definition Section

public class SpideyAndVenom implements Runnable {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 600;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;
    public Image spideyPic;
    public Image venomPic;
    public Image carnagePic;
    public Image backgroundPic;

    //Declare the objects used in the program
    //These are things that are made up of more than one variable type
    public SpiderMan spidey;
    public SpiderMan venom;
    public SpiderMan carnage;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        SpideyAndVenom ex = new SpideyAndVenom();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public SpideyAndVenom() { // BasicGameApp constructor

        setUpGraphics();

        //variable and objects
        //create (construct) the objects needed for the game and load up
      spideyPic = Toolkit.getDefaultToolkit().getImage("SpiderManPng.png"); //load the picture
        spidey = new SpiderMan("spidey",10,100); //construct the astronaut
        spidey.dy = 0;

        venomPic = Toolkit.getDefaultToolkit().getImage("Venom.png");
        venom = new SpiderMan("venom", 800, 400);

        carnagePic = Toolkit.getDefaultToolkit().getImage("carnage.png");
        carnage = new SpiderMan("carnage", 405, 250);

        backgroundPic = Toolkit.getDefaultToolkit().getImage("city.jpg");


    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section
//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {

        //for the moment we will loop things forever.
        while (true) {
            moveThings();  //move all the game objects
            crashVS();
            crashCV();
            crashSC();
            render();  // paint the graphics
            pause(20); // sleep for 10 ms
        }
    }

    public void moveThings() {
        //calls the move( ) code in the objects
        spidey.wraparound();
        venom.bounce();
        carnage.carnagebounce();

    }

    public void crashVS() {
        //if spidey and venom collide, they bounce
        if(venom.rec.intersects(spidey.rec)) {
            venom.dx = -venom.dx;
            spidey.dy = -spidey.dy;
        }
    }

    public void crashSC() {
        if(spidey.rec.intersects(carnage.rec)) {
            carnage.xpos = 500;
            carnage.ypos = 500;
            spidey.xpos = 500;
            spidey.ypos = 300;
        }
    }

    public void crashCV() {
        if(carnage.rec.intersects(venom.rec)) {
            carnage.xpos = venom.xpos;
            carnage.ypos = venom.ypos;
            carnage.bounce();
        }
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time ) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");
    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);
        g.drawImage(backgroundPic,0,0, WIDTH, HEIGHT, null);

        //draw the image of the astronaut
        if(spidey.isAlive == true) {
            g.drawImage(spideyPic, spidey.xpos, spidey.ypos, spidey.width, spidey.height, null);
            g.drawRect(spidey.rec.x, spidey.rec.y, spidey.rec.width, spidey.rec.height);
        }
        g.drawImage(venomPic, venom.xpos, venom.ypos, venom.width, venom.height, null);
        g.drawRect(venom.rec.x, venom.rec.y, venom.rec.width, venom.rec.height);

        g.drawImage(carnagePic, carnage.xpos, carnage.ypos, carnage.width, carnage.height, null);
        g.drawRect(carnage.rec.x, carnage.rec.y, carnage.rec.width, carnage.rec.height);

        g.dispose();
        bufferStrategy.show();
    }
}