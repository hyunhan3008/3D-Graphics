/* This code has the same structure with robot class form the exercise */
import gmaths.*;

public class Lamp {

    public Model sphere, lampFrame, eyes, tail;
    public SGNode lampRoot;
    public TransformNode translateX, rotateBase, rotateNose, rotateSpike, rotateTail, rotateTail2, rotateLeftEye, rotateRightEye, rotateUpper, rotateLower, rotateUpper2, rotateCube, translateToBaseAnkle;
    public float rotateLowerStart = 40, rotateLowerAngle=rotateLowerStart;
    public float rotateBaseStart = 0, rotateBaseAngle = rotateBaseStart;
    public float rotateUpperAngleStart = -50, rotateUpperAngle = rotateUpperAngleStart;
    public float rotateUpper2Angle = 0;
    public float rotateWingAngle = 0;
    public StandLight light;
    public float base_ankle_height;
    public float lower_support_height;
    public float ankle_height;
    public float upper_support_height;

    public Vec3 spotDir = new Vec3(0, 0, 0);
    // public final Vec3 DEFAULT_POSITION_2 = new Vec3(25, 0, 0);
    // public final Vec3 DEFAULT_POSITION_2 = new Vec3(25, 0, 0);

    float holderHeight = 0.2f;
    float holderWidth = 9f;
    float holderDepth = 5f;
    // float headScale = 2f; head
    float legLength = 3.5f;
    float legScale = 0.25f;
    float depth = -3f;
    float depth_front = 2.3f;
    float deskPosition = -4f;
    float lampPosition = 3.75f;
    float xPosLamp = -2f;


    public Lamp(Model lampFrame, Model eyes, Model tail, Model sphere, StandLight light) {
        this.lampFrame = lampFrame;
        this.sphere = sphere;
        this.light = light;
        this.eyes = eyes;
        this.tail = tail;
    }

    public void initialise(Model lampFrame, Model eyes, Model tail, Model sphere, StandLight light) {

    //shit the fuck
    lampRoot = new NameNode("lamp structure");
    translateX = new TransformNode("translate(" + xPosLamp + "," + lampPosition + "," + depth + ")",Mat4Transform.translate(xPosLamp, lampPosition, depth));
    //Base Support
    float base_support_height = 0.04f;
    NameNode base_support = new NameNode("base_support");
    rotateBase = new TransformNode("rotateAroundX("+ rotateBaseAngle+")",Mat4Transform.rotateAroundY(rotateBaseAngle));
    Mat4 m = Mat4Transform.scale(0.9f, base_support_height, 0.9f);
    m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
    TransformNode base_supportTransform = new TransformNode("scale(0.9,"+ base_support_height+",0.9); translate(0,0.5,0)", m);
    ModelNode base_support_shape = new ModelNode("Cube(base_support)", lampFrame);

    //Base Ankle
    base_ankle_height = 0.2f;                
    TransformNode translateToBaseAnkle = new TransformNode("translate(0,"+ base_support_height+",0)",Mat4Transform.translate(0, base_support_height,0));
    NameNode ankleBaseBranch = new NameNode("baseAnkle branch");
    m = Mat4Transform.scale(0.2f, base_ankle_height, 0.2f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode makeAnkleBaseBranch = new TransformNode("scale(0.4f,"+ base_ankle_height+",0.4f);translate(0,0.5,0)", m);
    ModelNode baseAnkle = new ModelNode("Sphere(baseAnkle)", sphere);

    //Lower Support
    lower_support_height = 1.1f;
    TransformNode translateTolower = new TransformNode("translate(0,"+ base_ankle_height+",0)",Mat4Transform.translate(0, base_ankle_height,0));
    NameNode lower_support = new NameNode("lower_support");
    rotateLower = new TransformNode("rotateAroundZ("+ rotateLowerAngle+")",Mat4Transform.rotateAroundZ(rotateLowerAngle));
    m = Mat4Transform.scale(0.2f, lower_support_height, 0.2f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode lowerTransform = new TransformNode("scale(0.2f,"+ lower_support_height+",0.2f);translate(0,0.5,0)", m);
    ModelNode lower_support_shape = new ModelNode("Cube(lower_support)", lampFrame);
    
    //Ankle
    ankle_height = 0.4f;                
    TransformNode translateToAnkle = new TransformNode("translate(0,"+ lower_support_height+",0)",Mat4Transform.translate(0, lower_support_height,0));
    NameNode ankleBranch = new NameNode("ankle branch");
    m = Mat4Transform.scale(0.4f, ankle_height, 0.4f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode makeAnkleBranch = new TransformNode("scale(0.4f,"+ ankle_height+",0.4f);translate(0,0.5,0)", m);
    ModelNode ankle = new ModelNode("Sphere(ankle)", sphere);

    //Upper Support
    upper_support_height = 0.9f;
    TransformNode translateToUpper = new TransformNode("translate(0,"+ ankle_height+",0)",Mat4Transform.translate(0, ankle_height,0));
    NameNode upper_support = new NameNode("upper_support");
    rotateUpper = new TransformNode("rotateAroundZ("+ rotateUpperAngle+")",Mat4Transform.rotateAroundZ(rotateUpperAngle));
    m = Mat4Transform.scale(0.2f, upper_support_height, 0.2f);
    m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
    TransformNode upperTransform = new TransformNode("scale(0.4f,"+ upper_support_height+",0.4f);translate(0,0.5,0)", m);
    ModelNode upper_support_shape = new ModelNode("Cube(upper_support)", lampFrame);

    //tail
    TransformNode translateSpike = new TransformNode("translate(0,"+ upper_support_height / 2+",0)",Mat4Transform.translate(-0.1f, ankle_height*0.8f, 0f));
    rotateSpike = new TransformNode("rotateAroundZ(" + rotateWingAngle + ")", Mat4Transform.rotateAroundZ(22));
    rotateTail = new TransformNode("rotateAroundZ(" + rotateWingAngle + ")", Mat4Transform.rotateAroundX(-20));
    NameNode spikeNode = new NameNode("spikeNode");
    m = Mat4Transform.scale(0.1f, upper_support_height, 0.1f);
    m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f));
    TransformNode makeSpike = new TransformNode("scale(2.5,4,2.5); translate(0,0.5,0)", m);
    ModelNode spikeModelNode = new ModelNode("Sphere(spikeNode)", tail);

    // head
    TransformNode translateToHead = new TransformNode("translate(0,"+ upper_support_height+",0)",Mat4Transform.translate(0f, upper_support_height,0f));
    NameNode headBox = new NameNode("headBox");
    m = Mat4Transform.scale(1.5f, 0.25f, 0.8f);
    m = Mat4.multiply(m, Mat4Transform.translate(0.25f,0.5f,0));
    TransformNode headBoxTransform = new TransformNode("headBox transform", m);
    ModelNode headBoxShape = new ModelNode("Cube(headBox)", lampFrame);

    // head2
    TransformNode translateToHead2 = new TransformNode("translate(0,"+ upper_support_height+",0)",Mat4Transform.translate(0f, 0.25f ,0f));
    NameNode headBox2 = new NameNode("headBox");
    m = Mat4Transform.scale(0.7f, 0.2f, 0.4f);
    m = Mat4.multiply(m, Mat4Transform.translate(0f,0.5f,0));
    TransformNode headBoxTransform2 = new TransformNode("headBox transform", m);
    ModelNode headBoxShape2 = new ModelNode("Cube(headBox)", lampFrame);

    // head3
    TransformNode translateToHead3 = new TransformNode("translate(0,"+ upper_support_height+",0)",Mat4Transform.translate(0f, 0.2f ,0f));
    NameNode headBox3 = new NameNode("headBox");
    m = Mat4Transform.scale(0.4f, 0.1f, 0.2f);
    m = Mat4.multiply(m, Mat4Transform.translate(-0.25f,0.5f,0));
    TransformNode headBoxTransform3 = new TransformNode("headBox transform", m);
    ModelNode headBoxShape3 = new ModelNode("Cube(headBox)", lampFrame);
    
    //Left Eye             
    TransformNode translateLeftEye = new TransformNode("translate(0,"+ lower_support_height+",0)",Mat4Transform.translate(0.9f, 0.35f,0.22f));
    rotateLeftEye = new TransformNode("rotateAroundZ(" + rotateWingAngle + ")", Mat4Transform.rotateAroundY(-90));
    NameNode LeftEyebranch = new NameNode("LeftEyebranch");
    m = Mat4Transform.scale(0.3f, 0.3f, 0.3f);
    m = Mat4.multiply(m, Mat4Transform.translate(0f,0.1f,0));
    TransformNode makeLeftEye = new TransformNode("scale(0.4f,"+ ankle_height+",0.4f);translate(0,0.5,0)", m);
    ModelNode leftEye = new ModelNode("Sphere(leftEye)", eyes);

    //right eye
    TransformNode translateRightEye = new TransformNode("translate(0,"+ lower_support_height+",0)",Mat4Transform.translate(0.9f, 0.35f,-0.22f));
    rotateRightEye = new TransformNode("rotateAroundZ(" + rotateWingAngle + ")", Mat4Transform.rotateAroundY(-80));
    NameNode RightEyeBranch = new NameNode("RightEyeBranch");
    m = Mat4Transform.scale(0.3f, 0.3f, 0.3f);
    m = Mat4.multiply(m, Mat4Transform.translate(0f,0.1f,0));
    TransformNode makeRightEye = new TransformNode("scale(0.4f,"+ ankle_height+",0.4f);translate(0,0.5,0)", m);
    ModelNode rightEye = new ModelNode("Sphere(rightEye)", eyes);

     //nose
    TransformNode translateNose = new TransformNode("translate(0,"+ ankle_height+",0)",Mat4Transform.translate(1f, 0.25f,0));
    rotateNose = new TransformNode("rotateAroundZ(" + rotateWingAngle + ")", Mat4Transform.rotateAroundZ(-90));
    NameNode NoseNode = new NameNode("NoseNode");
    m = Mat4Transform.scale(0.1f, 0.7f, 0.3f);
    m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f));
    TransformNode makeNose = new TransformNode("scale(2.5,4,2.5); translate(0,0.5,0)", m);
    ModelNode noseModelNode = new ModelNode("Sphere(spikeNode)", sphere);

    // light lamp
    TransformNode translateToLamp = new TransformNode("translate(0, 0, 0)",Mat4Transform.translate(1.2f, 0.1f ,0f));
    NameNode lightBox = new NameNode("lightBox");
    m = Mat4Transform.scale(0.2f, 0.1f, 0.5f);
   // m = Mat4.multiply(m, Mat4Transform.translate(0.0f,0f,0));
    TransformNode lightBoxTransForm = new TransformNode("lightbox transform", m); 
    LightNode lightShape = new LightNode("lightnode", light);
    
    // Then, put all the pieces together to make the scene graph
    // Indentation is meant to show the hierarchy.
    lampRoot.addChild(translateX);
      translateX.addChild(base_support);
        base_support.addChild(rotateBase);
          rotateBase.addChild(base_supportTransform);
            base_supportTransform.addChild(base_support_shape);
          rotateBase.addChild(translateToBaseAnkle);
            translateToBaseAnkle.addChild(ankleBaseBranch);
              ankleBaseBranch.addChild(makeAnkleBaseBranch);
                makeAnkleBaseBranch.addChild(baseAnkle);
              ankleBaseBranch.addChild(translateTolower);
                translateTolower.addChild(lower_support);
                  lower_support.addChild(rotateLower);
                      rotateLower.addChild(lowerTransform);
                        lowerTransform.addChild(lower_support_shape);
                      rotateLower.addChild(translateToAnkle);
                        translateToAnkle.addChild(ankleBranch);
                          ankleBranch.addChild(translateSpike);
                                  translateSpike.addChild(rotateSpike);
                                    rotateSpike.addChild(rotateTail);
                                      rotateTail.addChild(spikeNode);
                                        spikeNode.addChild(makeSpike);
                                          makeSpike.addChild(spikeModelNode);
                          ankleBranch.addChild(makeAnkleBranch);
                            makeAnkleBranch.addChild(ankle);
                          ankleBranch.addChild(translateToUpper);
                            translateToUpper.addChild(upper_support);
                              upper_support.addChild(rotateUpper);
                                rotateUpper.addChild(upperTransform);
                                  upperTransform.addChild(upper_support_shape);
                                rotateUpper.addChild(translateToHead);
                                  translateToHead.addChild(headBox);
                                    headBox.addChild(translateLeftEye);
                                      translateLeftEye.addChild(rotateLeftEye);
                                        rotateLeftEye.addChild(LeftEyebranch);
                                          LeftEyebranch.addChild(makeLeftEye);
                                            makeLeftEye.addChild(leftEye);
                                    headBox.addChild(translateRightEye);
                                      translateRightEye.addChild(rotateRightEye);
                                        rotateRightEye.addChild(RightEyeBranch);
                                          RightEyeBranch.addChild(makeRightEye);
                                            makeRightEye.addChild(rightEye);
                                    headBox.addChild(translateNose);
                                      translateNose.addChild(rotateNose);
                                        rotateNose.addChild(NoseNode);
                                          NoseNode.addChild(makeNose);
                                            makeNose.addChild(noseModelNode);
                                    headBox.addChild(headBoxTransform);
                                      headBoxTransform.addChild(headBoxShape);
                                    headBox.addChild(translateToLamp);
                                      translateToLamp.addChild(lightBox);
                                        lightBox.addChild(lightBoxTransForm);
                                          lightBoxTransForm.addChild(lightShape);
                                    headBox.addChild(translateToHead2);
                                      translateToHead2.addChild(headBox2);
                                        headBox2.addChild(headBoxTransform2);
                                          headBoxTransform2.addChild(headBoxShape2);
                                        headBox2.addChild(translateToHead3);
                                          translateToHead3.addChild(headBox3);
                                            headBox3.addChild(headBoxTransform3);
                                              headBoxTransform3.addChild(headBoxShape3);
                                          
    lampRoot.update();

    }
}