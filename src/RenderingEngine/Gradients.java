package RenderingEngine;

public class Gradients
{
    private Vector4f[] m_color;
    private Vector4f m_colorXStep;
    private Vector4f m_colorYStep;

    public Vector4f getColor(int loc) { return m_color[loc]; }

    public Vector4f getColorXStep() { return m_colorXStep; }

    public Vector4f getColorYStep() { return m_colorYStep; }

    public Gradients(Vertex minYVert, Vertex midYVert, Vertex maxYVert)
    {
        m_color = new Vector4f[3];

        m_color[0] = minYVert.getColor();
        m_color[1] = midYVert.getColor();
        m_color[2] = maxYVert.getColor();

        float oneOverdX = 1.0f /
                (((midYVert.getX() - maxYVert.getX()) * (minYVert.getY() - maxYVert.getY()))
                        - ((minYVert.getX() - maxYVert.getX()) * (midYVert.getY() - maxYVert.getY())));

        float oneOverdY = -oneOverdX;

        m_colorXStep = (((m_color[1].Sub(m_color[2])).Mul((minYVert.getY() - maxYVert.getY()))).Sub(
                ((m_color[0].Sub(m_color[2])).Mul((midYVert.getY() - maxYVert.getY()))))).Mul(oneOverdX);

        m_colorYStep = (((m_color[1].Sub(m_color[2])).Mul((minYVert.getX() - maxYVert.getX()))).Sub(
                ((m_color[0].Sub(m_color[2])).Mul((midYVert.getX() - maxYVert.getX()))))).Mul(oneOverdY);


    }
}
