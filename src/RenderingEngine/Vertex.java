package RenderingEngine;

public class Vertex {

    private Vector4f m_pos;

    public float getX() {
        return m_pos.GetX();
    }
    public float getY() { return m_pos.GetY(); }


    public Vertex(float x, float y)
    {
        m_pos = new Vector4f(x, y, 0, 1);
    }

    public Vertex(Vector4f pos)
    {
        m_pos = pos;
    }

    public Vertex Transform(Matrix4f transform)
    {
        return new Vertex(transform.Transform(m_pos));
    }


    public float TriangleAreaTimesTwo(Vertex b, Vertex c)
    {
        float x1 = b.getX() - m_pos.GetX();
        float y1 = b.getY() - m_pos.GetY();

        float x2 = c.getX() - m_pos.GetX();
        float y2 = c.getY() - m_pos.GetY();

        return (x1 * y2 - x2 * y1);
    }
}
