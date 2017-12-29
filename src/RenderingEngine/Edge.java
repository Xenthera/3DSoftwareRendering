package RenderingEngine;

public class Edge
{


    private float m_x;
    private float m_xStep;
    private int m_yStart;
    private int m_yEnd;

    public float getX() { return m_x; }

    public int getYStart() { return m_yStart; }

    public int getYEnd() { return m_yEnd; }

    public Edge(Vertex minYVert, Vertex maxYVert){
        m_yStart = (int)Math.ceil(minYVert.getY());
        m_yEnd = (int)Math.ceil(maxYVert.getY());

        float yDist = maxYVert.getY() - minYVert.getY();
        float xDist = maxYVert.getX() - minYVert.getX();

        float yPrestep = m_yStart - minYVert.getY();
        m_xStep = xDist / yDist;
        m_x = minYVert.getX() + yPrestep * m_xStep;
    }

    public void Step()
    {
        m_x += m_xStep;
    }


}
