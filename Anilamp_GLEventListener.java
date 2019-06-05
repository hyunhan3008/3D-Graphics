/* I declare that this code is my own work */
/* Author <Hyun Han> <hhan5@sheffield.ac.uk> */

import gmaths.*;
import java.util.Random;
import java.awt.*;
import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D.Float;
import java.awt.event.ActionEvent;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
  
public class Anilamp_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
    
  public Anilamp_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,12f,18f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled'
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory, if necessary */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    light.dispose(gl);
    torchLight.dispose(gl);
    secondLight.dispose(gl);
    floor.dispose(gl);
	  wall.dispose(gl);
    sphere.dispose(gl);
    deskObj.dispose(gl);
  }
  
  
  // ***************************************************
  /* INTERACTION
   *
   *
   */


  /* Different Type of interpolation */

  //Basic linear_interpolation
  public double linear_interpolation(double targetP, double currentP, double t) {
    double mediumValue = targetP * t + (currentP * (1 - t));
    return mediumValue;
  }

  //Interpolation that returns bact to starting point(currentP)
  public double returnInterpolation(double targetP, double currentP, double t) {
    double middle = targetP * Math.sin(2 * Math.PI * (t / 2)) + (currentP * (1 - Math.sin(2 * Math.PI * (t / 2))));
    return middle;
  }

  //Giving shakng to base of the lamp 
  public double shakingInterpolation(double targetP, double currentP, double t) {
    //Period of cos function is bigger to shake multiple times
    double middle = currentP* (Math.cos(Math.PI * 6 *t))+ targetP* (1 - (Math.cos(Math.PI * 6 *t)));
    return middle;
  }

  
  Random rand = new Random(); // Random Number for random pose
  private Long start_time; // delete this

  //To make a randomose at once
  protected boolean poseCheck = true;
  /* Make a random pose for the lamp */
  public void RandomPoseMotion() {
    poseCheck = false;
    long playTime = 1000; // duration time for animation
    double baseTarget = (-180) + rand.nextFloat() * (180 - (-180)); //Base can rotate 360 degrees
    double currentBaseAngle = lamp.rotateBaseAngle;
    double lowerTarget = (-15) + rand.nextFloat() * (60 - (-15));
    double currentLower = lamp.rotateLowerAngle;
    double upperTarget = (-70) + rand.nextFloat() * (0 - (-70));
    double currentUpper = lamp.rotateUpperAngle;

    start_time = null; // resetting start_time null  for the next movement
    Timer timer = new Timer(40, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (start_time == null) {
           start_time = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - start_time;

        //If the playtime is over, then stop the animation
        if (diff >= playTime) {
          diff = playTime;
          ((Timer) e.getSource()).stop();
          poseCheck = true; // Now make randompose again
        }

        double t = (double) diff / (double) playTime;;

        // Get the moving points depending on the changin time t
        double movingBase = linear_interpolation(baseTarget, currentBaseAngle, t);
        double movingLower = linear_interpolation(lowerTarget, currentLower, t);
        double movingHigher = linear_interpolation(upperTarget, currentUpper, t);

        //update the position towards the traget point
        lamp.rotateBase.setTransform(Mat4Transform.rotateAroundY((float)movingBase));
        lamp.rotateLower.setTransform(Mat4Transform.rotateAroundZ((float)movingLower));
        lamp.rotateUpper.setTransform(Mat4Transform.rotateAroundZ((float)movingHigher));
        lamp.lampRoot.update();

        //After the animation done, current points 
        lamp.rotateBaseAngle = (float) baseTarget;
        lamp.rotateLowerAngle = (float) lowerTarget;
        lamp.rotateUpperAngle = (float) upperTarget;
      }

    });
    timer.start();
  }

  /*
   * Those values are fro jumpoing animation but have to be precalculated to get
   * degree of readymotion for jumpoing and turning angle depending on distance.
   */
  double targetAngle;
  public void turningAround() {

    jumpingCheck = false; // Do not jump if it is jumping already
    long playtime = 550;
    //distance for x axis
    horizontalTarget = (-4) + rand.nextFloat() * (5 - (-4));
    currentHorizontal = lamp.xPosLamp;
    degree1 = horizontalTarget - currentHorizontal; // plus value
    //distance for z axis
    positionTarget = (-9 + rand.nextFloat() * (-3 - (-9)));
    currentposition = lamp.depth;
    degree2 = currentposition - positionTarget; // plus value


    double currentAngle = lamp.rotateBaseAngle;
    /* Trigonometric is used
       Dependig on the target point(minus or plus), angles change
    */
    if (degree1<0 & degree2>0) {
      targetAngle = (180+Math.toDegrees(Math.atan(degree2/degree1)));
    }
    else if (degree1<0 & degree2<0) {
      // calculation remains same as sign of vlaues change
      targetAngle = (180 + Math.toDegrees(Math.atan(degree2 / degree1)));
    }
    else if (degree1>0 & degree2>0) {
      targetAngle = (Math.toDegrees(Math.atan(degree2 / degree1)));
    }
    else{
      targetAngle = (Math.toDegrees(Math.atan(degree2 / degree1)));
    }

    //If it turns more than 180, it is better to turn to the opposite direction
    if (targetAngle>180){
      targetAngle = -(360-targetAngle);
    }
    if (targetAngle<-180){
      targetAngle = 360+ targetAngle;
    }

    start_time = null;
    Timer timer = new Timer(40, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (start_time == null) {
          start_time = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - start_time;

        if (diff >= playtime) {
          diff = playtime;
          ((Timer) e.getSource()).stop();
          readyMotion();
        }

        double t = (double) diff / (double) playtime;

        double turningAround = linear_interpolation(targetAngle, currentAngle, t);
        lamp.rotateBase.setTransform(Mat4Transform.rotateAroundY((float) turningAround));
        lamp.lampRoot.update();
        lamp.rotateBaseAngle = (float) targetAngle;

      }

    });

    timer.start();

  }
  
  double horizontalTarget, currentHorizontal, targetHeight, currentHeight, positionTarget, currentposition;
  double degree1, degree2;

  protected boolean jumpingCheck = true;
  /* Shaking base and bending to ready for jumping */
  public void readyMotion() {
    targetHeight = lamp.lampPosition+2f; //jump distane is always same
    currentHeight = lamp.lampPosition;

    jumpingCheck = false; // Do not jump if it is jumpin already
    long playtime = 550;
    // move the lower support of the lamp
    // bigger motion for longer distance
    // degree should be always positive value
    double lowerTarget = lamp.rotateLowerAngle + (Math.abs(degree1)+Math.abs(degree2))*4.5;
    double currentLower = lamp.rotateLowerAngle;
    // move the upper support of the lamp
    double upperTarget = (lamp.rotateUpperAngle - (Math.abs(degree1) + Math.abs(degree2)) * 4.5);
    double currentUpper = lamp.rotateUpperAngle;
    // shake the base of the lamp
    double targetShaking = lamp.rotateBaseAngle + 2;
    double currentShaking = lamp.rotateBaseAngle;

    start_time = null;
    Timer timer = new Timer(40, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (start_time == null) {
          start_time = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - start_time;

        if (diff >= playtime) {
          diff = playtime;
          ((Timer) e.getSource()).stop();
          Jumping();
        }

        double t = (double) diff / (double) playtime;

        double movingLower = returnInterpolation(lowerTarget, currentLower, t);
        double movingHigher = returnInterpolation(upperTarget, currentUpper, t);
        double shaking = shakingInterpolation(targetShaking, currentShaking, t);

        lamp.rotateLower.setTransform(Mat4Transform.rotateAroundZ((float) movingLower));
        lamp.rotateUpper.setTransform(Mat4Transform.rotateAroundZ((float) movingHigher));
        lamp.rotateBase.setTransform(Mat4Transform.rotateAroundY((float) shaking));
        lamp.lampRoot.update();
        // No need to update the current position because it will return to the origianl position

      }

    });

    timer.start();

  }

  /*Make a jumping action */
  public void Jumping() {
    long jumpingTime = 1400;
    
    start_time = null;
    Timer timer = new Timer(40, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (start_time == null) {
          start_time = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - start_time;

        if (diff >= jumpingTime) {
          diff = jumpingTime;
          ((Timer) e.getSource()).stop();
          landingMotion(); // Make some bounce after jumping
        }

        double t = (double) diff / (double) jumpingTime;

        double depthMoving = linear_interpolation(positionTarget, currentposition, t);
        double horizonMoving = linear_interpolation(horizontalTarget, currentHorizontal, t);
        //after jumping it has to come back to the original place(on the desk)
        double slowJumping = returnInterpolation(targetHeight, currentHeight, t);

        lamp.translateX.setTransform(Mat4Transform.translate((float) horizonMoving, (float)slowJumping, (float)depthMoving));

        lamp.lampRoot.update();
        //current points become target points
        lamp.depth = (float)positionTarget;
        lamp.xPosLamp = (float)horizontalTarget;
        
      }

    });
    timer.start();

  }

  /* Give some bounce after landing */
  public void landingMotion() {
    long playtime = 200;
    //Different degree for bending motion after jumping depending on distance
    double lowerTarget = lamp.rotateLowerAngle + (degree1+degree2);
    double currentLower = lamp.rotateLowerAngle;
    double upperTarget = (lamp.rotateUpperAngle - (degree1 + degree2));
    double currentUpper = lamp.rotateUpperAngle;

    start_time = null;
    Timer timer = new Timer(40, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (start_time == null) {
          start_time = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - start_time;

        if (diff >= playtime) {
          diff = playtime;
          ((Timer) e.getSource()).stop();
          jumpingCheck = true; // Now jump again
        }

        double t = (double) diff / (double) playtime;

        //Need to comback to original place
        double movingLower = returnInterpolation(lowerTarget, currentLower, t);
        double movingHigher = returnInterpolation(upperTarget, currentUpper, t);

        lamp.rotateLower.setTransform(Mat4Transform.rotateAroundZ((float) movingLower));
        lamp.rotateUpper.setTransform(Mat4Transform.rotateAroundZ((float) movingHigher));
        lamp.lampRoot.update();

      }

    });

    timer.start();
  }

  
  /* turn on/off for each light */
  //maing light in the room rotating 
  public void lightSwitch() {
    if (light.lightCheck) {
      light.lightCheck = false;
    } else {
      light.lightCheck = true;
    }

  }

  //second light rotating above the main one
  public void secondLightSwitch() {
    if (secondLight.secondLightCheck) {
      secondLight.secondLightCheck = false;
    } else {
      secondLight.secondLightCheck = true;
    }

  }

  //torch light coming from the lamp
  public void standSwitch() {
    if (torchLight.lightCheck2) {
      torchLight.lightCheck2 = false;
    } else {
      torchLight.lightCheck2 = true;
    }
  }

  
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  private Camera camera;
  public SGNode roomRoot; // The root of the whole room
  private Mat4 perspective;
  private Model floor, wall, frontScene, roofScene, bottomScene, sideScenes, frame, sphere, paperWeight, tail, eyes, ears, deskObj, phone, lampFrame, display, hemisphere;
  private Light light; // Main light
  private BottomLight secondLight; // Another Light
  private StandLight torchLight; // Light on the lamp
  private Desk desk;
  private Floor floorObj;
  private Scenary background; // object for background
  private Lamp lamp;
  private TransformNode translateX;

  private void initialise(GL3 gl) {
    createRandomNumbers();
    int[] textureId0 = TextureLibrary.loadTexture(gl, "textures/floortexture.jpg");
    int[] textureId1 = TextureLibrary.loadTexture(gl, "textures/jade.jpg");
    int[] textureId2 = TextureLibrary.loadTexture(gl, "textures/jade_specular.jpg");
    int[] textureId3 = TextureLibrary.loadTexture(gl, "textures/desk.jpg");
    int[] textureId4 = TextureLibrary.loadTexture(gl, "textures/container2_specular.jpg");
    int[] textureId5 = TextureLibrary.loadTexture(gl, "textures/cloud.jpg");
    int[] textureId6 = TextureLibrary.loadTexture(gl, "textures/wallpaper.jpg");
    int[] textureId7 = TextureLibrary.loadTexture(gl, "textures/grass.jpg");
    // int[] textureId8 = TextureLibrary.loadTexture(gl, "textures/box.jpg");// not used
    int[] textureId9 = TextureLibrary.loadTexture(gl, "textures/rabit.jpg");
    int[] textureId10 = TextureLibrary.loadTexture(gl, "textures/eyes2.jpg");
    int[] textureId11 = TextureLibrary.loadTexture(gl, "textures/tail.jpg");
    // int[] textureId12 = TextureLibrary.loadTexture(gl, "textures/darkgloom.jpg"); not used
    int[] textureId13 = TextureLibrary.loadTexture(gl, "textures/cloudimg.jpg");
    int[] textureId14 = TextureLibrary.loadTexture(gl, "textures/darkforest.jpg");
    int[] textureId15 = TextureLibrary.loadTexture(gl, "textures/tree.jpg");
    int[] textureId16 = TextureLibrary.loadTexture(gl, "textures/ears.jpg");
    // int[] textureId17 = TextureLibrary.loadTexture(gl, "textures/wall2.jpg"); not used
    int[] textureId18 = TextureLibrary.loadTexture(gl, "textures/darktree.jpg");
    int[] textureId19 = TextureLibrary.loadTexture(gl, "textures/fancy.jpg");
    //changing Texture
    int[] textureId20 = TextureLibrary.loadTexture(gl, "textures/scene1.jpg");
    int[] textureId21 = TextureLibrary.loadTexture(gl, "textures/scene2.jpg");
    int[] textureId22 = TextureLibrary.loadTexture(gl, "textures/scene3.jpg");
    int[] textureId23 = TextureLibrary.loadTexture(gl, "textures/scene4.jpg");

    // Light objects
    light = new Light(gl); // main lamp
    light.setCamera(camera);
    secondLight = new BottomLight(gl);
    secondLight.setCamera(camera); // second lamp rotating the main one
    torchLight = new StandLight(gl);
    torchLight.setCamera(camera); // torch light for the lamp
    
    //For planes
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.0f, 0.0f, 0.0f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
	  floor = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId0);
    wall = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId6);
    frame = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId2);

    //For mixed textures
    shader = new Shader(gl, "vs_tt_05.txt", "fs_mixTexture.txt");
    bottomScene = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId14, textureId7);
    sideScenes = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId15, textureId18);
    //For changing textures over time
    shader = new Shader(gl, "vs_tt_05.txt", "fs_changeTexture.txt");
    frontScene = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId20, textureId21,textureId22, textureId23);
    //For moving texture
    shader = new Shader(gl, "vs_tt_05.txt", "fs_movingTexture.txt");
    roofScene = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId13);
    
    // For all types of spheres
    mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    sphere = new Model(gl, camera, light, secondLight, torchLight,shader, material, modelMatrix, mesh, textureId1, textureId2);
    tail = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId11);
    ears = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId16);
    eyes = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId10);
    
    //when there is spot light, paperWeight turns dark to give ironic effect
    shader = new Shader(gl, "vs_tt_05.txt", "fs_paperWeight.txt");
    paperWeight = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId9);

    //For a hemishpere
    mesh = new Mesh(gl, HemiSphere.vertices.clone(), HemiSphere.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    hemisphere = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId19);

    //For all cubes
    mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    shader = new Shader(gl, "vs_cube_04.txt", "fs_cube_04.txt");
    material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
    deskObj = new Model(gl, camera, light, secondLight, torchLight,shader, material, modelMatrix, mesh, textureId3);
    lampFrame = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId5);
    phone = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId6);
    display = new Model(gl, camera, light, secondLight, torchLight, shader, material, modelMatrix, mesh, textureId4);

    //floor and walls
    floorObj = new Floor(floor, wall, frame);
    floorObj.initialise(floor, wall, frame);

    //background walls
    background = new Scenary(frontScene, roofScene, bottomScene, sideScenes);
    background.initialise(frontScene, roofScene, bottomScene, sideScenes);

    // Desk and three random objects
    desk = new Desk(deskObj, paperWeight, ears, phone, display, hemisphere);
    desk.initialise(deskObj, paperWeight, ears, phone, display, hemisphere);

    //A fancy lamp
    lamp = new Lamp(lampFrame, eyes, tail, sphere, torchLight);
    lamp.initialise(lampFrame, eyes, tail, sphere, torchLight);

    //Add all the sub root in one big root
    roomRoot = new NameNode("root");
    roomRoot.addChild(floorObj.floorRoot);
    roomRoot.addChild(background.scenaryRoot);
    roomRoot.addChild(desk.deskRoot);
    roomRoot.addChild(lamp.lampRoot);

	// IMPORTANT - don't forget this
    //deskRoot.print(0, false);
    //System.exit(0);
  }
 
  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());  // changing light position each frame
      light.render(gl);
    secondLight.setPosition(getLightPosition2()); // changing light position each frame
      secondLight.render(gl);
    roomRoot.draw(gl);
    tailMoving(); // there is a moving tail on the lamp
    fanMoving(); // moving fan
  }

  /* Tail of the lamp is moving*/
  private void tailMoving() {
    double elapsedTime = getSeconds() - startTime;
    // Tail moves side to side
    lamp.rotateTail.setTransform(Mat4Transform.rotateAroundX((float) Math.sin(elapsedTime*10)*35));
    lamp.lampRoot.update();
  }

  /* fan is spnning like a propellar */
  private void fanMoving() {
    double elapsedTime = getSeconds() - startTime;
    // fan move fast enough so it looks like a fan
    desk.rotateLeft.setTransform(Mat4Transform.rotateAroundZ((float) Math.tan(elapsedTime*5) * 2000));
    desk.rotateRight.setTransform(Mat4Transform.rotateAroundX((float) Math.tan(elapsedTime*5) * 2000));
    desk.rotateRight2.setTransform(Mat4Transform.rotateAroundZ((float) Math.tan(elapsedTime*5) * 2000));
    desk.deskRoot.update();
  }
  
  //This code is based on Robot class from the tutorial
  // The light's postion is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLightPosition() {
    double elapsedTime = getSeconds()-startTime;
    float x = 4.0f*(float)(Math.sin(Math.toRadians(elapsedTime*30)));
    float y = 13.7f;
    float z = 4.0f*(float)(Math.cos(Math.toRadians(elapsedTime*30)));
    return new Vec3(x,y,z);   
    //return new Vec3(5f,3.4f,5f);
  }

  //Change position of the second light faster
  private Vec3 getLightPosition2() {
    double elapsedTime = getSeconds() - startTime;
    float x = 1.0f * (float) (Math.sin(Math.toRadians(elapsedTime * 70)));
    float y = 10.7f;
    float z = 1.0f * (float) (Math.cos(Math.toRadians(elapsedTime * 70)));
    return new Vec3(x, y, z);
    // return new Vec3(5f,3.4f,5f);
  }
  
  // ***************************************************
  /* TIME
   */ 
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  // This code is based on Robot class from the tutorial
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
}