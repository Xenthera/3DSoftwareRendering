package RenderingEngine;

import java.util.Arrays;

public class Bitmap {


    private final int m_width;
    private final int m_height;
    private final byte[] m_components;

    public Bitmap(int width, int height)
    {
        m_width      = width;
        m_height     = height;
        m_components = new byte[width * height * 4];
    }

    public void Clear(byte shade)
    {
        Arrays.fill(m_components, shade);
    }

    public void DrawPixel(int x, int y, byte a, byte b, byte g, byte r)
    {
        if(x < 0 || x >= m_width || y < 0 || y >= m_height)
        {
            return;
        }
        int index = (x + y * m_width) * 4;
        m_components[index    ] = a;
        m_components[index + 1] = r;
        m_components[index + 2] = g;
        m_components[index + 3] = b;
    }


    public void CopyToIntArray(int[] dest)
    {
        for(int i = 0; i < m_width * m_height; i++)
        {
            int r = ((int)m_components[i * 4 + 1] & 0xFF) << 16 ;
            int g = ((int)m_components[i * 4 + 2] & 0xFF) << 8;
            int b = ((int)m_components[i * 4 + 3] & 0xFF);

            dest[i] = r | g | b;
        }


    }

    public int getWidth() {
        return m_width;
    }

    public int getHeight() {
        return m_height;
    }
}