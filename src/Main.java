import RenderingEngine.*;
import engine.Game;
import engine.Renderer;

import java.io.IOException;

public class Main extends Game{

    static Main game;

    Display display;
    RenderContext target;

    Stars3D stars;

    Mesh mesh;

    private float dt, rotCounter;

    Vertex minY, midY, maxY;

    Matrix4f projection;

    Bitmap texture;

    public static void main(String[] args){

        game = new Main();
        game.start("3D Software RenderingEngine", 300,200, 4);

    }


    @Override
    public void onCreate() {
        display = new Display(game.getRenderer());
        target = display.getFrameBuffer();
        stars = new Stars3D(3, 64f, 10f);

        try {
            mesh = new Mesh("./res/monkey.obj");
            texture = new Bitmap("./res/bricks.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


//
//        for (int j = 0; j < texture.getHeight(); j++)
//        {
//            for (int i = 0; i < texture.getWidth(); i++)
//            {
//                texture.DrawPixel(i, j, (byte)(Math.random() * 255 + 0.5),
//                                        (byte)(Math.random() * 255 + 0.5),
//                                        (byte)(Math.random() * 255 + 0.5),
//                                        (byte)(Math.random() * 255 + 0.5));
//            }
//        }
//
//        texture = new Bitmap(2,2);
//        texture.DrawPixel(0, 0,  (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF);
//        texture.DrawPixel(0, 1,  (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0x00);
//        texture.DrawPixel(1, 0,  (byte)0x00, (byte)0xFF, (byte)0x00, (byte)0x00);
//        texture.DrawPixel(1, 1,  (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0xFF);

        minY = new Vertex(new Vector4f(-1,-1,0,1), new Vector4f(0.0f,0.0f,0.0f,0.0f));
        midY = new Vertex(new Vector4f( 0, 1,0,1), new Vector4f(0.5f,1.0f,0.0f,0.0f));
        maxY = new Vertex(new Vector4f( 1,-1,0,1), new Vector4f(1.0f,0.0f,1.0f,0.0f));

        projection = new Matrix4f().InitPerspective((float)Math.toRadians(70f),
                    (float)target.getWidth()/(float)target.getHeight(),
                    0.1f, 1000f);

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
        Matrix4f translation = new Matrix4f().InitTranslation(0.0f,0.0f,3.0f);
        Matrix4f rotation = new Matrix4f().InitRotation(0.0f, rotCounter, 0.0f);
        Matrix4f transform = projection.Mul(translation.Mul(rotation));

        target.Clear((byte)0x00);
        target.clearDepthBuffer();

        target.DrawMesh(mesh, transform, texture);

        display.SwapBuffers(renderer);
    }
}
