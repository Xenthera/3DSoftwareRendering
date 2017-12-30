package RenderingEngine;



public class Edge
{
    private float m_x;
    private float m_xStep;
    private int m_yStart;
    private int m_yEnd;

    private float m_texCoordX;
    private float m_texCoordXStep;
    private float m_texCoordY;
    private float m_texCoordYStep;

    private float m_oneOverZ;
    private float m_oneOverZStep;

    public float getX() { return m_x; }
    public int getYStart() { return m_yStart; }
    public int getYEnd() { return m_yEnd; }
    public float getTexCoordX() { return m_texCoordX; }
    public float getTexCoordY() { return m_texCoordY; }
    public float GetOneOverZ() { return m_oneOverZ; }


    public Edge(Gradients gradients, Vertex minYVert, Vertex maxYVert, int minYVertIndex){
        m_yStart = (int)Math.ceil(minYVert.getY());
        m_yEnd = (int)Math.ceil(maxYVert.getY());

        float yDist = maxYVert.getY() - minYVert.getY();
        float xDist = maxYVert.getX() - minYVert.getX();

        float yPrestep = m_yStart - minYVert.getY();
        m_xStep = xDist / yDist;
        m_x = minYVert.getX() + yPrestep * m_xStep;
        float xPrestep = m_x - minYVert.getX();

        m_texCoordX = gradients.GetTexCoordX(minYVertIndex) +
                gradients.GetTexCoordXXStep() * xPrestep +
                gradients.GetTexCoordXYStep() * yPrestep;
        m_texCoordXStep = gradients.GetTexCoordXYStep() + gradients.GetTexCoordXXStep() * m_xStep;

        m_texCoordY = gradients.GetTexCoordY(minYVertIndex) +
                gradients.GetTexCoordYXStep() * xPrestep +
                gradients.GetTexCoordYYStep() * yPrestep;
        m_texCoordYStep = gradients.GetTexCoordYYStep() + gradients.GetTexCoordYXStep() * m_xStep;

        m_oneOverZ = gradients.GetOneOverZ(minYVertIndex) +
                gradients.GetOneOverZXStep() * xPrestep +
                gradients.GetOneOverZYStep() * yPrestep;
        m_oneOverZStep = gradients.GetOneOverZYStep() + gradients.GetOneOverZXStep() * m_xStep;
    }

    public void Step()
    {
        m_x += m_xStep;
        m_texCoordX += m_texCoordXStep;
        m_texCoordY += m_texCoordYStep;
        m_oneOverZ += m_oneOverZStep;
    }


}
