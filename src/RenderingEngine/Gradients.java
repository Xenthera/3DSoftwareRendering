package RenderingEngine;

public class Gradients
{
    private float[] m_texCoordX;
    private float[] m_texCoordY;
    private float[] m_oneOverZ;
    private float[] m_depth;

    private float m_texCoordXXStep;
    private float m_texCoordXYStep;
    private float m_texCoordYXStep;
    private float m_texCoordYYStep;
    private float m_oneOverZXStep;
    private float m_oneOverZYStep;
    private float m_depthXStep;
    private float m_depthYStep;

    public float GetTexCoordX(int loc) { return m_texCoordX[loc]; }
    public float GetTexCoordY(int loc) { return m_texCoordY[loc]; }
    public float GetOneOverZ(int loc) { return m_oneOverZ[loc]; }
    public float GetDepth(int loc) { return m_depth[loc]; }


    public float GetTexCoordXXStep() { return m_texCoordXXStep; }
    public float GetTexCoordXYStep() { return m_texCoordXYStep; }
    public float GetTexCoordYXStep() { return m_texCoordYXStep; }
    public float GetTexCoordYYStep() { return m_texCoordYYStep; }
    public float GetOneOverZXStep() { return m_oneOverZXStep; }
    public float GetOneOverZYStep() { return m_oneOverZYStep; }
    public float GetDepthXStep() { return m_depthXStep; }
    public float GetDepthYStep() { return m_depthYStep; }

    private float CalcXStep(float[] values, Vertex minYVert, Vertex midYVert,
                            Vertex maxYVert, float oneOverdX)
    {
        return
                (((values[1] - values[2]) *
                        (minYVert.getY() - maxYVert.getY())) -
                        ((values[0] - values[2]) *
                                (midYVert.getY() - maxYVert.getY()))) * oneOverdX;
    }

    private float CalcYStep(float[] values, Vertex minYVert, Vertex midYVert,
                            Vertex maxYVert, float oneOverdY)
    {
        return
                (((values[1] - values[2]) *
                        (minYVert.getX() - maxYVert.getX())) -
                        ((values[0] - values[2]) *
                                (midYVert.getX() - maxYVert.getX()))) * oneOverdY;
    }

    public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert)
    {
        float oneOverdX = 1.0f /
                (((midYVert.getX() - maxYVert.getX()) *
                        (minYVert.getY() - maxYVert.getY())) -
                        ((minYVert.getX() - maxYVert.getX()) *
                                (midYVert.getY() - maxYVert.getY())));

        float oneOverdY = -oneOverdX;

        m_oneOverZ = new float[3];
        m_texCoordX = new float[3];
        m_texCoordY = new float[3];
        m_depth = new float[3];


        m_depth[0] = minYVert.getPos().GetZ();
        m_depth[1] = midYVert.getPos().GetZ();
        m_depth[2] = maxYVert.getPos().GetZ();

        // Note that the W component is the perspective Z value;
        // The Z component is the occlusion Z value
        m_oneOverZ[0] = 1.0f/minYVert.getPos().GetW();
        m_oneOverZ[1] = 1.0f/midYVert.getPos().GetW();
        m_oneOverZ[2] = 1.0f/maxYVert.getPos().GetW();

        m_texCoordX[0] = minYVert.GetTexCoords().GetX() * m_oneOverZ[0];
        m_texCoordX[1] = midYVert.GetTexCoords().GetX() * m_oneOverZ[1];
        m_texCoordX[2] = maxYVert.GetTexCoords().GetX() * m_oneOverZ[2];

        m_texCoordY[0] = minYVert.GetTexCoords().GetY() * m_oneOverZ[0];
        m_texCoordY[1] = midYVert.GetTexCoords().GetY() * m_oneOverZ[1];
        m_texCoordY[2] = maxYVert.GetTexCoords().GetY() * m_oneOverZ[2];

        m_texCoordXXStep = CalcXStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdX);
        m_texCoordXYStep = CalcYStep(m_texCoordX, minYVert, midYVert, maxYVert, oneOverdY);
        m_texCoordYXStep = CalcXStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdX);
        m_texCoordYYStep = CalcYStep(m_texCoordY, minYVert, midYVert, maxYVert, oneOverdY);
        m_oneOverZXStep = CalcXStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdX);
        m_oneOverZYStep = CalcYStep(m_oneOverZ, minYVert, midYVert, maxYVert, oneOverdY);
        m_depthXStep = CalcXStep(m_depth, minYVert, midYVert, maxYVert, oneOverdX);
        m_depthYStep = CalcYStep(m_depth, minYVert, midYVert, maxYVert, oneOverdY);
    }
}
