package RenderingEngine;

public class Vertex {

    private Vector4f m_pos;
    private Vector4f m_color;

    public float getX() {
        return m_pos.GetX();
    }
    public float getY() { return m_pos.GetY(); }

    public Vector4f getColor() {
        return m_color;
    }

    public Vertex(Vector4f pos, Vector4f color)
    {
        m_pos = pos;
        m_color = color;
    }

    public Vertex Transform(Matrix4f transform)
    {
        return new Vertex(transform.Transform(m_pos), m_color);
    }

    public Vertex PerspectiveDivide(){
        return new Vertex(new Vector4f(m_pos.GetX()/m_pos.GetW(),m_pos.GetY()/m_pos.GetW()
                         ,m_pos.GetZ()/m_pos.GetW(),m_pos.GetW()),

                m_color);
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
