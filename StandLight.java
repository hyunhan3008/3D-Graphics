/* This code is based on Light calss from Robot Exercise */
import gmaths.*;
import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import java.lang.Object;
//import com.sun.org.apache.xerces.internal.xni.QName;

public class StandLight {

    private Material material;
    private Vec3 position, lightBoxColor2;
    private Vec3 spotDir;
    private Mat4 model;
    private Shader shader;
    private Camera camera;
    protected Lamp lamp;
    private TransformNode headBoxTransform;
    private Vec3 fixedposition;
    public boolean lightCheck2 = false;

    public StandLight(GL3 gl) {
        material = new Material();
        material.setAmbient(1f, 1f, 1f);
        material.setDiffuse(0.8f, 0.8f, 0.8f);
        material.setSpecular(0.8f, 0.8f, 0.8f);
        position = new Vec3(0, 0f, 0f);
        fixedposition = new Vec3(0, 0f, 0f);
        lightBoxColor2 = new Vec3(0f, 0f, 0f);
        spotDir = new Vec3(0.5f, 0.5f, 0.5f);
        model = new Mat4(1);
        shader = new Shader(gl, "vs_light_01.txt", "fs_light_02.txt");
        fillBuffers(gl);
    }

    public void setPosition(Vec3 v) {
        position.x = v.x;
        position.y = v.y;
        position.z = v.z;
    }

    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Vec3 getPosition() {
        return position;
    }

    public Vec3 spotDirPosition(){
        return spotDir;
    }

    public void setMaterial(Material m) {
        material = m;
    }

    public Material getMaterial() {
        return material;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public void render(GL3 gl, Mat4 model) {
        //When the swithc is clicked, change the lighting setting
        if (lightCheck2) {
            material.setAmbient(0, 0, 0);
            material.setDiffuse(0f, 0f, 0f);
            material.setSpecular(0f, 0f, 0f);
            lightBoxColor2 = new Vec3(0f, 0f, 0f);
        } else {
            material.setAmbient(0.5f, 0.5f, 0.5f);
            material.setDiffuse(0.8f, 0.8f, 0.8f);
            material.setSpecular(0.8f, 0.8f, 0.8f);
            lightBoxColor2 = new Vec3(1f, 1f, 1f);
        }

        // headBox
        Mat4 model_headBox = Mat4.multiply(model, Mat4.inverse(Mat4Transform.translate(1.2f, 0.1f ,0f)));
        float xpos2 = model_headBox.get(0, 3);
        float ypos2 = model_headBox.get(1, 3);
        float zpos2 = model_headBox.get(2, 3);

        //light
        float xpos = model.get(0,3);
        float ypos = model.get(1,3);
        float zpos = model.get(2,3);
        position = new Vec3(xpos, ypos, zpos);
        spotDir = new Vec3(xpos-xpos2, ypos-ypos2, zpos-zpos2);

        Mat4 mvpMatrix = Mat4.multiply(camera.getPerspectiveMatrix(), Mat4.multiply(camera.getViewMatrix(), model));
        
        shader.use(gl);
        shader.setVec3(gl, "colorIn2", lightBoxColor2);

        shader.setFloatArray(gl, "model", model.toFloatArrayForGLSL());
        shader.setFloatArray(gl, "mvpMatrix", mvpMatrix.toFloatArrayForGLSL());

        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glDrawElements(GL.GL_TRIANGLES, indices.length, GL.GL_UNSIGNED_INT, 0);
        gl.glBindVertexArray(0);
    }

    public void dispose(GL3 gl) {
        gl.glDeleteBuffers(1, vertexBufferId, 0);
        gl.glDeleteVertexArrays(1, vertexArrayId, 0);
        gl.glDeleteBuffers(1, elementBufferId, 0);
    }

    // ***************************************************
    /*
     * THE DATA
     */
    // anticlockwise/counterclockwise ordering

    private float[] vertices = new float[] { // x,y,z
            -0.5f, -0.5f, -0.5f, // 0
            -0.5f, -0.5f, 0.5f, // 1
            -0.5f, 0.5f, -0.5f, // 2
            -0.5f, 0.5f, 0.5f, // 3
            0.5f, -0.5f, -0.5f, // 4
            0.5f, -0.5f, 0.5f, // 5
            0.5f, 0.5f, -0.5f, // 6
            0.5f, 0.5f, 0.5f // 7
    };

    private int[] indices = new int[] { 0, 1, 3, // x -ve
            3, 2, 0, // x -ve
            4, 6, 7, // x +ve
            7, 5, 4, // x +ve
            1, 5, 7, // z +ve
            7, 3, 1, // z +ve
            6, 4, 0, // z -ve
            0, 2, 6, // z -ve
            0, 4, 5, // y -ve
            5, 1, 0, // y -ve
            2, 3, 7, // y +ve
            7, 6, 2 // y +ve
    };

    private int vertexStride = 3;
    private int vertexXYZFloats = 3;

    // ***************************************************
    /*
     * THE LIGHT BUFFERS
     */

    private int[] vertexBufferId = new int[1];
    private int[] vertexArrayId = new int[1];
    private int[] elementBufferId = new int[1];

    private void fillBuffers(GL3 gl) {
        gl.glGenVertexArrays(1, vertexArrayId, 0);
        gl.glBindVertexArray(vertexArrayId[0]);
        gl.glGenBuffers(1, vertexBufferId, 0);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, vertexBufferId[0]);
        FloatBuffer fb = Buffers.newDirectFloatBuffer(vertices);

        gl.glBufferData(GL.GL_ARRAY_BUFFER, Float.BYTES * vertices.length, fb, GL.GL_STATIC_DRAW);

        int stride = vertexStride;
        int numXYZFloats = vertexXYZFloats;
        int offset = 0;
        gl.glVertexAttribPointer(0, numXYZFloats, GL.GL_FLOAT, false, stride * Float.BYTES, offset);
        gl.glEnableVertexAttribArray(0);

        gl.glGenBuffers(1, elementBufferId, 0);
        IntBuffer ib = Buffers.newDirectIntBuffer(indices);
        gl.glBindBuffer(GL.GL_ELEMENT_ARRAY_BUFFER, elementBufferId[0]);
        gl.glBufferData(GL.GL_ELEMENT_ARRAY_BUFFER, Integer.BYTES * indices.length, ib, GL.GL_STATIC_DRAW);
        gl.glBindVertexArray(0);
    }

}