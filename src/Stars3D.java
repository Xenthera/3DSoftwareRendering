import RenderingEngine.Bitmap;
import RenderingEngine.RenderContext;
import RenderingEngine.Vertex;

public class Stars3D {

    private final float m_spread;
    private final float m_speed;

    private final float m_starX[];
    private final float m_starY[];
    private final float m_starZ[];

    public Stars3D(int numStars, float spread, float speed)
    {

        m_spread = spread;
        m_speed = speed;

        m_starX = new float[numStars];
        m_starY = new float[numStars];
        m_starZ = new float[numStars];

        for(int i = 0; i < m_starX.length; i++){
            InitStar(i);
        }

    }

    private void InitStar(int index)
    {
        m_starX[index] = 2 * ((float)Math.random() - 0.5f) * m_spread;
        m_starY[index] = 2 * ((float)Math.random() - 0.5f) * m_spread;
        m_starZ[index] = ((float)Math.random() + 0.00001f) * m_spread;
    }



    public void UpdateAndRender(RenderContext target, float delta)
    {
        final float tanHalfFOV = (float)Math.tan(Math.toRadians(90f/2f));

        target.Clear((byte)0x00);

        float halfWidth = target.getWidth()/2.0f;
        float halfHeight = target.getHeight()/2.0f;

        int triCounter = 0;
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;

        for (int i = 0; i < m_starX.length; i++) {
            m_starZ[i] -= delta * m_speed;

            if(m_starZ[i] <= 0 || m_starZ[i] >= 1 * m_spread){
                InitStar(i);
            }

            int x = (int)((m_starX[i] / (m_starZ[i] * tanHalfFOV)) * halfWidth + halfWidth);
            int y = (int)((m_starY[i] / (m_starZ[i] * tanHalfFOV)) * halfHeight + halfHeight);

            if(x < 0 || x >= target.getWidth() || (y < 0 || y >= target.getHeight()))
            {
                InitStar(i);
            }else {
                triCounter++;

                if(triCounter == 1){
                    x1 = x;
                    y1 = y;
                }else if(triCounter == 2){
                    x2 = x;
                    y2 = y;
                }else if(triCounter == 3){
                    triCounter = 0;
                    Vertex v1 = new Vertex(x1,y1,0);
                    Vertex v2 = new Vertex(x2, y2,0);
                    Vertex v3 = new Vertex(x, y,0);

                    target.FillTriangle(v1,v2,v3);
                }
            }



        }

    }
}
