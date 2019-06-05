/* This code is based on model class from the Robot exercise 
   THe part I added is written in comment */
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;

public class Model {
  
  private Mesh mesh;
  private int[] textureId1; 
  private int[] textureId2; 
  private int[] textureId3;
  private int[] textureId4;
  private Material material;
  private Shader shader;
  private Mat4 modelMatrix;
  private Camera camera;
  private Light light;
  private BottomLight secondLight;
  private StandLight light2;
  
  public Model(GL3 gl, Camera camera, Light light, BottomLight secondLight, StandLight light2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2, int[] textureId3, 
      int[] textureId4) {
    this.mesh = mesh;
    this.material = material;
    this.modelMatrix = modelMatrix;
    this.shader = shader;
    this.camera = camera;
    this.light = light;
    this.secondLight = secondLight;
    this.light2 = light2;
    this.textureId1 = textureId1;
    this.textureId2 = textureId2;
    this.textureId3 = textureId3;
    this.textureId4 = textureId4;
  }
  
  //This part has been added for four different chanigng textures
  public Model(GL3 gl, Camera camera, Light light, BottomLight secondLight, StandLight light2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1) {
    this(gl, camera, light, secondLight, light2, shader, material, modelMatrix, mesh, textureId1, null, null, null);
  }
  
  public Model(GL3 gl, Camera camera, Light light, BottomLight secondLight, StandLight light2, Shader shader, Material material, Mat4 modelMatrix, Mesh mesh) {
    this(gl, camera, light, secondLight, light2, shader, material, modelMatrix, mesh, null, null, null, null);
  }

  public Model(GL3 gl, Camera camera, Light light, BottomLight secondLight, StandLight light2, Shader shader,
      Material material, Mat4 modelMatrix, Mesh mesh, int[] textureId1, int[] textureId2) {
    this(gl, camera, light, secondLight, light2, shader, material, modelMatrix, mesh, textureId1, textureId2, null, null);
  }
  
  public void setModelMatrix(Mat4 m) {
    modelMatrix = m;
  }
  
  public void setCamera(Camera camera) {
    this.camera = camera;
  }
  
  public void setLight(Light light, BottomLight secondLight, StandLight light2) {
    this.light2 = light2;
    this.light = light;
    this.secondLight = secondLight;
  }

  private double getSeconds() {
    return System.currentTimeMillis() / 1000.0;
  }

  public void render(GL3 gl, Mat4 modelMatrix) {
    Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), modelMatrix));
    shader.use(gl);
    shader.setFloatArray(gl, "model", modelMatrix.toFloatArrayForGLSL());
    shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());
    shader.setVec3(gl, "viewPos", camera.getPosition());

    //This code is for moving texture over time
    double elapsedTime = getSeconds() - 1.543533791033E9f;
    double t = elapsedTime * 0.1;
    //Texture move horizontally
    float offsetX = (float) (t - Math.floor(t));
    float offsetY = 0;
    shader.setFloat(gl, "offset", offsetX, offsetY);

    //This code is for calculation for changing textures
    int currentTexture = (int) Math.floor(2 * (1 + Math.sin(elapsedTime*0.2 + Math.toRadians(270))));

    shader.setVec3(gl, "light.position", light.getPosition());
    shader.setVec3(gl, "light.ambient", light.getMaterial().getAmbient());
    shader.setVec3(gl, "light.diffuse", light.getMaterial().getDiffuse());
    shader.setVec3(gl, "light.specular", light.getMaterial().getSpecular());

    //Second light properties was added to the shader
    shader.setVec3(gl, "secondLight.position3", secondLight.getPosition());
    shader.setVec3(gl, "secondLight.ambient3", secondLight.getMaterial().getAmbient());
    shader.setVec3(gl, "secondLight.diffuse3", secondLight.getMaterial().getDiffuse());
    shader.setVec3(gl, "secondLight.specular3", secondLight.getMaterial().getSpecular());

    //Stand light was added tot he shader
    shader.setVec3(gl, "light2.position2", light2.getPosition());
    shader.setVec3(gl, "light2.spotDir", light2.spotDirPosition());
    shader.setVec3(gl, "light2.ambient2", light2.getMaterial().getAmbient());
    shader.setVec3(gl, "light2.diffuse2", light2.getMaterial().getDiffuse());
    shader.setVec3(gl, "light2.specular2", light2.getMaterial().getSpecular());
    shader.setFloat(gl, "light2.cutOff", (float)Math.cos(Math.toRadians(20)));
    shader.setFloat(gl, "light2.outercutOff", (float) Math.cos(Math.toRadians(22)));


    shader.setVec3(gl, "material.ambient", material.getAmbient());
    shader.setVec3(gl, "material.diffuse", material.getDiffuse());
    shader.setVec3(gl, "material.specular", material.getSpecular());
    shader.setFloat(gl, "material.shininess", material.getShininess());  

    //If there is third textures that means it needs changing textures
    if (textureId3!=null){
      shader.setInt(gl, "first_texture", currentTexture);
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
      gl.glActiveTexture(GL.GL_TEXTURE2);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId3[0]);
      gl.glActiveTexture(GL.GL_TEXTURE3);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId4[0]);
    }
    //If not there is no chanign textures skill applied
    else {
      shader.setInt(gl, "first_texture", 0);  // be careful to match these with GL_TEXTURE0 and GL_TEXTURE1
      gl.glActiveTexture(GL.GL_TEXTURE0);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId1[0]);
    }
    if (textureId2!=null) {
      shader.setInt(gl, "second_texture", 1);
      gl.glActiveTexture(GL.GL_TEXTURE1);
      gl.glBindTexture(GL.GL_TEXTURE_2D, textureId2[0]);
    }
    mesh.render(gl);
  } 
  
  public void render(GL3 gl) {
    render(gl, modelMatrix);
  }
  
  public void dispose(GL3 gl) {
    mesh.dispose(gl);
    if (textureId1!=null) gl.glDeleteBuffers(1, textureId1, 0);
    if (textureId2!=null) gl.glDeleteBuffers(1, textureId2, 0);
  }
  
}