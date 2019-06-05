/* This code is for creating scenary outside the room */
import gmaths.*;

public class Scenary {

    public SGNode scenaryRoot;
    public Model frontScene, roofScene, bottomScene, sideScenes;
    public TransformNode sceneRotate, skyRotate, landRotate, sideLeftRotate, sideRightRotate;

    public Scenary(Model frontScene, Model roofScene, Model bottomScene, Model sideScenes) {
        this.frontScene = frontScene;
        this.roofScene = roofScene;
        this.bottomScene = bottomScene;
        this.sideScenes = sideScenes;
    }

    public void initialise(Model frontScene, Model roofScene, Model bottomScene, Model sideScenes) {

      scenaryRoot = new NameNode("root");
      //Creating front scene
      sceneRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundX(90));
      TransformNode frontTransform = new TransformNode("translate(0, 0,0)",Mat4Transform.translate(0f, -30f, 0f));
      NameNode mainScene = new NameNode("mainScene");
      Mat4 m = Mat4Transform.scale(20f, 1f, 12f);
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, -0.5f));
      TransformNode frontSceneTrans = new TransformNode("floor transform", m);
      ModelNode frontShape = new ModelNode("Cube(frontShape)", frontScene);

      // Creating sky scene
      skyRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundX(80));
      TransformNode skyTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0f, -12f, 12f));
      NameNode skyNode = new NameNode("skyNode");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(20f, 1f, 22f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, 0f, -1f));
      TransformNode skyNodeTransform = new TransformNode("floor transform", m);
      ModelNode skyShape = new ModelNode("Cube(wallBottom)", roofScene);

      //creating land scene
      landRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundX(-80));
      TransformNode landTransform = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0f, 0f, 30f));
      NameNode landNode = new NameNode("landNode");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(20f, 1f, 22f));
      m = Mat4.multiply(m, Mat4Transform.translate(0f, -0.5f, -1f));
      TransformNode landNodeTransform = new TransformNode("floor transform", m);
      ModelNode landShape = new ModelNode("Cube(wallBottom)", bottomScene);

      //creating left side scene
      sideLeftRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundZ(-90));
      TransformNode leftSideTrans = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(0.5f, -9f, 15f));
      NameNode leftSideNode = new NameNode("landNode");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(20f, 1f, 22f));
      m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, -0.5f, -1f));
      TransformNode leftTransform = new TransformNode("floor transform", m);
      ModelNode leftSideShape = new ModelNode("Cube(leftSideShape)", sideScenes);

      //creating right side scene
      sideRightRotate = new TransformNode("rightarm rotate", Mat4Transform.rotateAroundZ(90));
      TransformNode rightSideTrans = new TransformNode("translate(0, 0,0)", Mat4Transform.translate(20f, -9f, 15f));
      NameNode rightSideNode = new NameNode("rightSideNode");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(20f, 1f, 22f));
      m = Mat4.multiply(m, Mat4Transform.translate(-0.5f, -0.5f, -1f));
      TransformNode rightTransForm = new TransformNode("floor transform", m);
      ModelNode rightsideShape = new ModelNode("Cube(rightsideShape)", sideScenes);

      // Then, put all the pieces together to make the scene graph
      // Indentation is meant to show the hierarchy.
      scenaryRoot.addChild(sceneRotate);
        sceneRotate.addChild(frontTransform);
          frontTransform.addChild(mainScene);
            mainScene.addChild(frontSceneTrans);
              frontSceneTrans.addChild(frontShape);
            mainScene.addChild(skyRotate);
              skyRotate.addChild(skyTransform);
                skyTransform.addChild(skyNode);
                  skyNode.addChild(skyNodeTransform);
                    skyNodeTransform.addChild(skyShape);
            mainScene.addChild(landRotate);
              landRotate.addChild(landTransform);
                landTransform.addChild(landNode);
                  landNode.addChild(landNodeTransform);
                    landNodeTransform.addChild(landShape);
            mainScene.addChild(sideLeftRotate);
              sideLeftRotate.addChild(leftSideTrans);
                leftSideTrans.addChild(leftSideNode);
                  leftSideNode.addChild(leftTransform);
                    leftTransform.addChild(leftSideShape);
            mainScene.addChild(sideRightRotate);
              sideRightRotate.addChild(rightSideTrans);
                rightSideTrans.addChild(rightSideNode);
                  rightSideNode.addChild(rightTransForm);
                    rightTransForm.addChild(rightsideShape);

      scenaryRoot.update();

    }
}