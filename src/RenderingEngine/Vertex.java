package RenderingEngine;

public class Vertex {

    private float m_x;
    private float m_y;

    public float getX() {
        return m_x;
    }
    public float getY() {
        return m_y;
    }

    public void setX(float m_x) {
        this.m_x = m_x;
    }
    public void setY(float m_y) {
        this.m_y = m_y;
    }


    public Vertex(float x, float y)
    {
        m_x = x;
        m_y = y;
    }


    public float TriangleArea(Vertex b, Vertex c)
    {
        float x1 = b.getX() - m_x;
        float y1 = b.getY() - m_y;

        float x2 = c.getX() - m_x;
        float y2 = c.getY() - m_y;

        return (x1 * y2 - x2 * y1);
    }
}
