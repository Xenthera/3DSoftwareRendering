import RenderingEngine.*;
import engine.Game;
import engine.Renderer;

public class Main extends Game{

    static Main game;

    Display display;
    RenderContext target;

    //Stars3D stars;

    private float dt, rotCounter;

    Vertex minY, midY, maxY;

    Matrix4f projection;

    public static void main(String[] args){

        game = new Main();
        game.start("3D Software RenderingEngine", 300,200, 4);

    }


    @Override
    public void onCreate() {
        display = new Display(game.getRenderer());
        target = display.getFrameBuffer();
        //stars = new Stars3D(3, 64f, 10f);

        minY = new Vertex(new Vector4f(-1,-1,0,1), new Vector4f(1.0f,0.0f,0.0f,0.0f));
        midY = new Vertex(new Vector4f( 0, 1,0,1), new Vector4f(0.0f,1.0f,0.0f,0.0f));
        maxY = new Vertex(new Vector4f( 1,-1,0,1), new Vector4f(0.0f,0.0f,1.0f,0.0f));

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

        target.FillTriangle(maxY.Transform(transform), midY.Transform(transform), minY.Transform(transform));

        display.SwapBuffers(renderer);
    }
}
