package RenderingEngine;

public class Gradients
{
    private float[] m_texCoordX;
    private float[] m_texCoordY;

    private float m_texCoordXXStep;
    private float m_texCoordXYStep;
    private float m_texCoordYXStep;
    private float m_texCoordYYStep;

    public float GetTexCoordX(int loc) { return m_texCoordX[loc]; }
    public float GetTexCoordY(int loc) { return m_texCoordY[loc]; }

    public float GetTexCoordXXStep() { return m_texCoordXXStep; }
    public float GetTexCoordXYStep() { return m_texCoordXYStep; }
    public float GetTexCoordYXStep() { return m_texCoordYXStep; }
    public float GetTexCoordYYStep() { return m_texCoordYYStep; }

    public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert)
    {
        m_texCoordX = new float[3];
        m_texCoordY = new float[3];

        float oneOverdX = 1.0f /
                (((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert.getY()))
                        - ((minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert.getY())));

        float oneOverdY = -oneOverdX;

        m_texCoordX[0] = minYVert.GetTexCoords().GetX();
        m_texCoordX[1] = midYVert.GetTexCoords().GetX();
        m_texCoordX[2] = maxYVert.GetTexCoords().GetX();

        m_texCoordY[0] = minYVert.GetTexCoords().GetY();
        m_texCoordY[1] = midYVert.GetTexCoords().GetY();
        m_texCoordY[2] = maxYVert.GetTexCoords().GetY();

        m_texCoordXXStep =
                (((m_texCoordX[1] - m_texCoordX[2]) *
                        (minYVert.getY() - maxYVert.getY())) -
                        ((m_texCoordX[0] - m_texCoordX[2]) *
                                (midYVert.getY() - maxYVert.getY()))) * oneOverdX;

        m_texCoordXYStep =
                (((m_texCoordX[1] - m_texCoordX[2]) *
                        (minYVert.getX() - maxYVert.getX())) -
                        ((m_texCoordX[0] - m_texCoordX[2]) *
                                (midYVert.getX() - maxYVert.getX()))) * oneOverdY;

        m_texCoordYXStep =
                (((m_texCoordY[1] - m_texCoordY[2]) *
                        (minYVert.getY() - maxYVert.getY())) -
                        ((m_texCoordY[0] - m_texCoordY[2]) *
                                (midYVert.getY() - maxYVert.getY()))) * oneOverdX;

        m_texCoordYYStep =
                (((m_texCoordY[1] - m_texCoordY[2]) *
                        (minYVert.getX() - maxYVert.getX())) -
                        ((m_texCoordY[0] - m_texCoordY[2]) *
                                (midYVert.getX() - maxYVert.getX()))) * oneOverdY;

    }
}
