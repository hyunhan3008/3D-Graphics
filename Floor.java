/* THis has the same struture with the robot calss from the exercise */
/* Creates floor and the walls */
import gmaths.*;

public class Floor {

    public SGNode floorRoot;
    public Model frontscene, roofscene, floor, wall, frame;
    public TransformNode wallRotate, bottomwall, leftTransform;

    //float bodyHeight = 0.2f;

    public Floor(Model floor, Model wall, Model frame) {
        this.floor = floor;
        this.wall = wall;
        this.frame = frame;
    }

    public void initialise(Model floor, Model wall, Model frame) {

      //Creating a floor
      floorRoot = new NameNode("root");
      NameNode floorNode = new NameNode("body");
      Mat4 m = Mat4Transform.scale(18f, 1f, 21f);
      TransformNode floorTransform = new TransformNode("floor transform", m);
      ModelNode floorShape = new ModelNode("Cube(body)", floor);

      /* The wall is made of four different squared to make a window in the middle */
      wallRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundX(90));
      TransformNode bottomwall = new TransformNode("translate(0, 0,0)",Mat4Transform.translate(0f, -11f, 0f));
      NameNode wallBottom = new NameNode("wallBottom");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(18f, 1f, 5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode wallBottomTransform = new TransformNode("floor transform", m);
      ModelNode wallBottomShape = new ModelNode("Cube(wallBottom)", wall);

      TransformNode topWall = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0f, -0f, -10f));
      NameNode wallTop = new NameNode("wallTop");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(18f, 1f, 5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode wallTopTransform = new TransformNode("floor transform", m);
      ModelNode wallTopShape = new ModelNode("Cube(wallTopShape)", wall);

      TransformNode leftTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(-6.7f, 0f, -5f));
      NameNode leftWall = new NameNode("wallBottom");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(4.5f, 1f, 5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode leftWallTransform = new TransformNode("floor transform", m);
      ModelNode leftShape = new ModelNode("Cube(leftShape)", wall);

      TransformNode rightTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(6.7f, 0f, -5f));
      NameNode rightWall = new NameNode("rightWall");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(4.5f, 1f, 5f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode rightWallTransform = new TransformNode("floor transform", m);
      ModelNode rightShape = new ModelNode("Cube(rightShape)", wall);

      /*Window frame is made of four different long quad */
      TransformNode leftFrameTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(-4.3f, 0f, -5f));
      NameNode leftFrame = new NameNode("rightWall");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.3f, 1f, 6f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode FrameLeft = new TransformNode("floor transform", m);
      ModelNode leftFrameShape = new ModelNode("Cube(leftFrameShape)", frame);

      TransformNode rightFrameTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(4.3f, 0f, -5f));
      NameNode rightFrame = new NameNode("rightWall");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(0.3f, 1f, 6f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode FrameRight = new TransformNode("floor transform", m);
      ModelNode rightFrameShape = new ModelNode("Cube(rightFrameShape)", frame);

      TransformNode bottomFrameTrans = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0f, 0f, -5f));
      NameNode bottomFrame = new NameNode("rightWall");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(10f, 1f, 0.25f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, -0.5f));
      TransformNode FrameBottom = new TransformNode("floor transform", m);
      ModelNode bottomFrameShape = new ModelNode("Cube(bottomFrameShape)", frame);

      TransformNode topFrameTrans = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0f, 0f, -10f));
      NameNode topFrame = new NameNode("rightWall");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(10f, 1f, 0.25f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0.5f));
      TransformNode FrameTop = new TransformNode("floor transform", m);
      ModelNode topFrameShape = new ModelNode("Cube(topFrameShape)", frame);
      
      
      floorRoot.addChild(floorNode);
        floorNode.addChild(floorTransform);
          floorTransform.addChild(floorShape);
      floorRoot.addChild(wallRotate);
        wallRotate.addChild(bottomwall);
          bottomwall.addChild(wallBottom);
            wallBottom.addChild(wallBottomTransform);
              wallBottomTransform.addChild(wallBottomShape);
          bottomwall.addChild(leftTransform);
            leftTransform.addChild(leftWall);
              leftWall.addChild(leftWallTransform);
                leftWallTransform.addChild(leftShape);
          bottomwall.addChild(rightTransform);
            rightTransform.addChild(rightWall);
              rightWall.addChild(rightWallTransform);
                rightWallTransform.addChild(rightShape);
          bottomwall.addChild(topWall);
            topWall.addChild(wallTop);
              wallTop.addChild(wallTopTransform);
                wallTopTransform.addChild(wallTopShape);
          bottomwall.addChild(leftFrameTransform);
            leftFrameTransform.addChild(leftFrame);
              leftFrame.addChild(FrameLeft);
                FrameLeft.addChild(leftFrameShape);
          bottomwall.addChild(rightFrameTransform);
            rightFrameTransform.addChild(rightFrame);
              rightFrame.addChild(FrameRight);
                FrameRight.addChild(rightFrameShape);
          bottomwall.addChild(bottomFrameTrans);
            bottomFrameTrans.addChild(bottomFrame);
              bottomFrame.addChild(FrameBottom);
                FrameBottom.addChild(bottomFrameShape);
          bottomwall.addChild(topFrameTrans);
            topFrameTrans.addChild(topFrame);
              topFrame.addChild(FrameTop);
                FrameTop.addChild(topFrameShape);
      floorRoot.update();

    }
}