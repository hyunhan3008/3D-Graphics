/*This class is for attatching the light on the lamp scene graph */
/*The code is based on modelnode which is from the Robot exercise */
import com.jogamp.opengl.*;

public class LightNode extends SGNode {

    //Now the stand light have to be passed instead of model
    protected StandLight light;

    public LightNode(String name, StandLight l) {
        super(name);
        light = l;
    }

    public void draw(GL3 gl) {
        light.render(gl, worldTransform);
        for (int i = 0; i < children.size(); i++) {
            children.get(i).draw(gl);
        }
    }

}
