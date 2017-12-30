package RenderingEngine;

public class Vertex {

    private Vector4f m_pos;
    private Vector4f m_texCoords;

    public float getX() {
        return m_pos.GetX();
    }
    public float getY() { return m_pos.GetY(); }

    public Vector4f getPos() {
        return m_pos;
    }

    public Vector4f GetTexCoords() {
        return m_texCoords;
    }

    public Vertex(Vector4f pos, Vector4f texCoords)
    {
        m_pos = pos;
        m_texCoords = texCoords;
    }

    public Vertex Transform(Matrix4f transform)
    {
        return new Vertex(transform.Transform(m_pos), m_texCoords);
    }

    public Vertex PerspectiveDivide(){
        return new Vertex(new Vector4f(m_pos.GetX()/m_pos.GetW(),m_pos.GetY()/m_pos.GetW()
                         ,m_pos.GetZ()/m_pos.GetW(),m_pos.GetW()),

                m_texCoords);
    }


    public float TriangleAreaTimesTwo(Vertex b, Vertex c)
    {
        float x1 = b.getX() - m_pos.GetX();
        float y1 = b.getY() - m_pos.GetY();

        float x2 = c.getX() - m_pos.GetX();
        float y2 = c.getY() - m_pos.GetY();

        return (x1 * y2 - x2 * y1);
    }

    public Vertex lerp(Vertex other, float amt){
        return new Vertex(m_pos.Lerp(other.getPos(), amt), m_texCoords.Lerp(other.GetTexCoords(), amt));
    }

    public boolean isInsideViewFrustum(){
        return Math.abs(m_pos.GetX()) <= Math.abs(m_pos.GetW()) &&
                Math.abs(m_pos.GetY()) <= Math.abs(m_pos.GetW()) &&
                Math.abs(m_pos.GetZ()) <= Math.abs(m_pos.GetW());
    }

    public float Get(int index)
    {
        switch (index){
            case 0:
                return m_pos.GetX();
            case 1:
                return m_pos.GetY();
            case 2:
                return m_pos.GetZ();
            case 3:
                return m_pos.GetW();
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
