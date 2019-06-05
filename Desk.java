
/* THis has the same struture with the robot calss from the exercise */
/* This creates a desk and three objects on it */
import gmaths.*;
  
public class Desk {
	
	public SGNode deskRoot, fanRoot, phoneRoot; // Different object have differen root
	public Model deskObj, paperWeight, ears, phone, display, hemisphere;
	public TransformNode rotateRight, rotateLeft, rotateFanBase, rotateRight2, rotatePhone;
	
	float bodyHeight = 0.2f;
	float bodyWidth = 15f;
	float bodyDepth = 8f;
	float legLength = 3.5f;
	float legScale = 0.25f;
	float depth = -2.8f;
	float depth_front = 3.3f;
	float deskPosition = -5.5f;
	
	public Desk (Model deskObj, Model paperWeight, Model ears, Model phone, Model display, Model hemisphere) {
		this.deskObj = deskObj;
		this.paperWeight = paperWeight;
		this.ears = ears;
		this.phone = phone;
		this.display = display;
		this.hemisphere = hemisphere;
	}
    
	public void initialise(Model deskObj, Model paperWeight, Model ears, Model phone, Model display, Model hemisphere) {		
		
		//Main root
		deskRoot = new NameNode("root");
		
		//Decide the position of the desk
		TransformNode deskTranslate = new TransformNode("deskObj transform",Mat4Transform.translate(0,legLength, deskPosition));

		/* Creating Desk */
		NameNode body = new NameNode("body");
		Mat4 m = Mat4Transform.scale(bodyWidth,bodyHeight,bodyDepth);
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode bodyTransform = new TransformNode("body transform", m);
		ModelNode bodyShape = new ModelNode("Cube(body)", deskObj);
		
		//creat back left leg
		NameNode backLeft = new NameNode("left arm");
		TransformNode backLeftTranslate = new TransformNode("backLeft translate", Mat4Transform.translate((bodyWidth*0.45f)+(legScale*0.25f),bodyHeight, depth));
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
		m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode backLeftScale = new TransformNode("backLeft scale", m);
		ModelNode backLeftShape = new ModelNode("Cube(left arm)", deskObj);
		
		//creat back right leg
		NameNode backRight = new NameNode("right arm");
		TransformNode backrightTranslate = new TransformNode("backRight translate", Mat4Transform.translate(-(bodyWidth*0.45f)-(legScale*0.25f),bodyHeight, depth));
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
		m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode backRightScale = new TransformNode("backRight scale", m);
		ModelNode backRightShape = new ModelNode("Cube(right arm)", deskObj);
		
		//create front left leg
		NameNode leftFront = new NameNode("left leg");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.translate((bodyWidth*0.45f)-(legScale*0.25f),0,depth_front));
		m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
		m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode leftFrontTransform = new TransformNode("leftFront transform", m);
		ModelNode leftFrontShape = new ModelNode("Cube(leftFront)", deskObj);
		
		// create front righ leg
		NameNode rightFront = new NameNode("right leg");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.translate(-(bodyWidth*0.45f)+(legScale*0.25f),0,depth_front));
		m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
		m = Mat4.multiply(m, Mat4Transform.scale(legScale,legLength,legScale));
		m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
		TransformNode rightFrontTransform = new TransformNode("rightFront transform", m);
		ModelNode rightFrontShape = new ModelNode("Cube(rightFront)", deskObj);

		/* Creating Paper Weight */
		TransformNode transLatePaper = new TransformNode("translate(0, 0 ,0)",Mat4Transform.translate(bodyWidth / 2 - 0.5f, bodyHeight, depth));
		NameNode paperWeightNode = new NameNode("paperWeight Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.rotateAroundY(180));
		m = Mat4.multiply(m, Mat4Transform.scale(0.6f, 0.9f, 0.6f));
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.4f, 0));
		TransformNode paperWeightTrans = new TransformNode("paperWeight transform", m);
		ModelNode paperWeightShape = new ModelNode("Cube(paperWeightShape)", paperWeight);

		//create left ear of paper wieght
		NameNode leftEar = new NameNode("leftEar Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.translate(-0.2f, 0.9f, 0));
		m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(35));
		m = Mat4.multiply(m, Mat4Transform.scale(0.2f, 0.6f, 0.2f));
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.2f, 0));
		TransformNode leftEarTransform = new TransformNode("paperWeight transform", m);
		ModelNode leftEarShape = new ModelNode("Cube(paperWeightShape)", ears);

		//creat right ear of paper weight
		NameNode rightEar = new NameNode("leftEar Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.translate(0.2f, 0.9f, 0));
		m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-35));
		m = Mat4.multiply(m, Mat4Transform.scale(0.2f, 0.6f, 0.2f));
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.2f, 0));
		TransformNode rightEarTransform = new TransformNode("paperWeight transform", m);
		ModelNode rightEarShape = new ModelNode("Cube(rightEarShape)", ears);

		/* Creating Phone */
		phoneRoot = new NameNode("phoneRoot");
		TransformNode mobileTransform = new TransformNode("translate(0, 0 ,0)",Mat4Transform.translate(bodyWidth / 2 -0.5f, bodyHeight, depth+2f));
		rotatePhone = new TransformNode("rotateAroundX(", Mat4Transform.rotateAroundY(-35));	
		NameNode mobilePhone = new NameNode("mobilePhone Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.scale(0.4f, 0.02f, 0.7f));
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
		TransformNode mobileNodeTrans = new TransformNode("mobileNodeTrans transform", m);
		ModelNode mobileShape = new ModelNode("Cube(mobileShape)", phone);

		//making disply for the phone
	    TransformNode displayTransform = new TransformNode("translate(0, 0 ,0)",Mat4Transform.translate(0, 0.02f, 0));
		NameNode displayNode = new NameNode("display Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.scale(0.4f, 0.005f, 0.7f));
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
		TransformNode displayTrans = new TransformNode("mobileNodeTrans transform", m);
		ModelNode displayShape = new ModelNode("Cube(displayShape)", display);

		//make a button
		TransformNode buttonTransform = new TransformNode("translate(0, 0 ,0)", Mat4Transform.translate(0, 0.005f, 0.32f));
		NameNode buttonNode = new NameNode("buttonNode Node");
		m = new Mat4(1);
		m = Mat4.multiply(m, Mat4Transform.scale(0.025f, 0.005f, 0.025f));
		m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.5f, 0f));
		TransformNode buttonTrans = new TransformNode("mobileNodeTrans transform", m);
		ModelNode buttonShape = new ModelNode("Cube(buttonShape)", display);

	
		/* Creating fan */
		fanRoot = new NameNode("root");
		
		//create hemisphere base
		TransformNode fanTranslate = new TransformNode("fanTranslate transform",Mat4Transform.translate(-bodyWidth / 2 + 1f, bodyHeight-0.2f, depth));
		float base_support_height = 0.4f;
		NameNode fanBaseNode = new NameNode("fanBaseNode");
		m = Mat4Transform.scale(1.2f, base_support_height, 1.2f);
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
		TransformNode baseTrans = new TransformNode("scale(0.9, 0.9,0.9); translate(0,0.5,0)", m);
		rotateFanBase = new TransformNode("rotateAroundX(", Mat4Transform.rotateAroundX(180));
		ModelNode baseShape = new ModelNode("Cube(baseShape)", hemisphere);

		//Create a support
		TransformNode supportTranslate = new TransformNode("fanTranslate transform",Mat4Transform.translate(0, base_support_height/2, 0));
		NameNode supportNode = new NameNode("supportNode");
		m = Mat4Transform.scale(0.1f, 1.5f, 0.1f);
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
		TransformNode supportTrans = new TransformNode("supportTrans transform", m);
		ModelNode supportShape = new ModelNode("Cube(baseShape)", ears);

		//create core
		TransformNode coreTransLate = new TransformNode("fanTranslate transform",Mat4Transform.translate(0, 1.5f, 0));
		NameNode coreNode = new NameNode("supportNode");
		m = Mat4Transform.scale(0.3f, 0.3f, 0.3f);
		m = Mat4.multiply(m, Mat4Transform.translate(0, 0.5f, 0));
		TransformNode coreTrans = new TransformNode("supportTrans transform", m);
		ModelNode coreShape = new ModelNode("Cube(coreShape)", paperWeight);

		//first propellar
		TransformNode prop1NodeTrans = new TransformNode("fanTranslate transform", Mat4Transform.translate(0, 0.15f, 0));
		rotateLeft = new TransformNode("rotateAroundZ()", Mat4Transform.rotateAroundZ(90));
		NameNode propellar1 = new NameNode("supportNode");
		m = Mat4Transform.scale(0.1f, 0.8f, 0.1f);
		m = Mat4.multiply(m, Mat4Transform.translate(0f, 0.6f, 0));
		TransformNode prop1Trans = new TransformNode("prop1Trans transform", m);
		ModelNode prop1Shape = new ModelNode("Cube(prop1Shape)", ears);

		// second propellar
	    TransformNode prop2NodeTrans = new TransformNode("fanTranslate transform", Mat4Transform.translate(0, 0.15f, 0));
		rotateRight = new TransformNode("rotateAroundZ()", Mat4Transform.rotateAroundX(90));
		rotateRight2 = new TransformNode("rotateAroundZ()", Mat4Transform.rotateAroundZ(90));
		NameNode propellar2 = new NameNode("supportNode");
		m = Mat4Transform.scale(0.1f, 0.8f, 0.1f);
		m = Mat4.multiply(m, Mat4Transform.translate(0f, -0.6f, 0));
		TransformNode prop2Trans = new TransformNode("prop2Trans transform", m);
		ModelNode prop2Shape = new ModelNode("Cube(prop2Shape)", ears);
			
		// Then, put all the pieces together to make the scene graph
		// Indentation is meant to show the hierarchy.
		deskRoot.addChild(deskTranslate);
			deskTranslate.addChild(body);
			  body.addChild(bodyTransform);
				  bodyTransform.addChild(bodyShape);
			body.addChild(backLeft);
				backLeft.addChild(backLeftTranslate);
					backLeftTranslate.addChild(backLeftScale);
						backLeftScale.addChild(backLeftShape);
			body.addChild(backRight);
				backRight.addChild(backrightTranslate);
					backrightTranslate.addChild(backRightScale);
						backRightScale.addChild(backRightShape);
			body.addChild(leftFront);
				leftFront.addChild(leftFrontTransform);
					leftFrontTransform.addChild(leftFrontShape);
			body.addChild(rightFront);
				rightFront.addChild(rightFrontTransform);
					rightFrontTransform.addChild(rightFrontShape);
			body.addChild(transLatePaper);
				transLatePaper.addChild(paperWeightNode);
					paperWeightNode.addChild(paperWeightTrans);
						paperWeightTrans.addChild(paperWeightShape);
					paperWeightNode.addChild(leftEar);
						leftEar.addChild(leftEarTransform);
							leftEarTransform.addChild(leftEarShape);
					paperWeightNode.addChild(rightEar);
						rightEar.addChild(rightEarTransform);
							rightEarTransform.addChild(rightEarShape);
			body.addChild(phoneRoot);
				phoneRoot.addChild(mobileTransform);
					mobileTransform.addChild(rotatePhone);
						rotatePhone.addChild(mobilePhone);
					      mobilePhone.addChild(mobileNodeTrans);
							mobileNodeTrans.addChild(mobileShape);
						  mobilePhone.addChild(displayTransform);
						    displayTransform.addChild(displayNode);
							  displayNode.addChild(displayTrans);
								displayTrans.addChild(displayShape);
							  displayNode.addChild(buttonTransform);
						        buttonTransform.addChild(buttonNode);
							      buttonNode.addChild(buttonTrans);
									buttonTrans.addChild(buttonShape);
			body.addChild(fanRoot);
			  fanRoot.addChild(fanTranslate);
					fanTranslate.addChild(fanBaseNode);
						fanBaseNode.addChild(baseTrans);
						  baseTrans.addChild(rotateFanBase);
							rotateFanBase.addChild(baseShape);
						fanBaseNode.addChild(supportTranslate);
						  supportTranslate.addChild(supportNode);
							supportNode.addChild(supportTrans);
							  supportTrans.addChild(supportShape);
							supportNode.addChild(coreTransLate);
						      coreTransLate.addChild(coreNode);
							    coreNode.addChild(coreTrans);
								  coreTrans.addChild(coreShape);
								coreNode.addChild(prop1NodeTrans);
								  prop1NodeTrans.addChild(rotateLeft);
						      	    rotateLeft.addChild(propellar1);
							    	  propellar1.addChild(prop1Trans);
										  prop1Trans.addChild(prop1Shape);
								coreNode.addChild(prop2NodeTrans);
								  prop2NodeTrans.addChild(rotateRight);
									  rotateRight.addChild(rotateRight2);
									    rotateRight2.addChild(propellar2);
							    	      propellar2.addChild(prop2Trans);
										    prop2Trans.addChild(prop2Shape);									  							
		deskRoot.update();

	}
}