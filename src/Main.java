import RenderingEngine.*;
import engine.Game;
import engine.Renderer;

import java.io.IOException;

public class Main extends Game{

    static Main game;
    Display display;
    RenderContext target;

    Mesh monkeyMesh, terrainMesh;
    Bitmap texture, texture2;

    Transform monkeyTransform, terrainTransform;

    Camera camera;

    static Input input;

    private float dt, rotCounter;



    public static void main(String[] args){
        input = new Input();
        game = new Main();
        game.start(input,"3D Software RenderingEngine", 300,200, 4);

    }


    @Override
    public void onCreate() {
        display = new Display(game.getRenderer());
        target = display.getFrameBuffer();


        try {
            monkeyMesh = new Mesh("./res/monkey.obj");
            terrainMesh = new Mesh("/Users/bobbylucero/IdeaProjects/3DSoftwareRendering/res/terrain.obj");
            texture = new Bitmap("./res/bricks.jpg");
            texture2 = new Bitmap("./res/bricks2.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

        monkeyTransform = new Transform(new Vector4f(0.0f,0.0f,0.0f));
        terrainTransform = new Transform(new Vector4f(0.0f,-1.0f,0.0f));

        camera = new Camera(new Matrix4f().InitPerspective((float)Math.toRadians(70.0f), (float)target.getWidth() / (float)target.getHeight(), 0.1f, 1000f));


        rotCounter = 0.0f;

    }


    @Override
    public void onUpdate(double elapsedTime) {
        dt = (float)elapsedTime;
        game.setTitle(String.valueOf(game.getCurrentFPS()));


    }


    @Override
    public void onRender(Renderer renderer) {
        //stars.UpdateAndRender(target, dt);


        rotCounter += dt;
        camera.Update(game.input, dt);
        Matrix4f vp = camera.GetViewProjection();

        monkeyTransform = monkeyTransform.Rotate(new Quaternion(new Vector4f(0,1,0), dt));

        target.Clear((byte)0x00);
        target.clearDepthBuffer();

        monkeyMesh.Draw(target, vp.Mul(monkeyTransform.GetTransformation()), monkeyTransform.GetTransformation(), texture2);
        terrainMesh.Draw(target, vp.Mul(terrainTransform.GetTransformation()), terrainTransform.GetTransformation(), texture);

        display.SwapBuffers(renderer);
    }
}
