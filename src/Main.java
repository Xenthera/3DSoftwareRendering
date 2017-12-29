import RenderingEngine.Bitmap;
import RenderingEngine.Display;
import RenderingEngine.RenderContext;
import RenderingEngine.Vertex;
import engine.Game;
import engine.Renderer;

public class Main extends Game{

    static Main game;

    Display display;
    RenderContext target;

    Stars3D stars;

    private float dt;

    Vertex minY, midY, maxY;

    public static void main(String[] args){

        game = new Main();
        game.start("3D Software RenderingEngine", 150,100, 8);

    }


    @Override
    public void onCreate() {
        display = new Display(game.getRenderer());
        target = display.getFrameBuffer();
        stars = new Stars3D(3, 64f, 10f);

        minY = new Vertex(100/4,100/4);
        midY = new Vertex(150/4,200/4);
        maxY = new Vertex(80/4, 300/4);
    }


    @Override
    public void onUpdate(double elapsedTime) {
        dt = (float)elapsedTime;
        game.setTitle(String.valueOf(game.getCurrentFPS()));


    }


    @Override
    public void onRender(Renderer renderer) {
        //stars.UpdateAndRender(target, dt);
        target.Clear((byte)0x00);
        display.SwapBuffers(renderer);
    }
}
