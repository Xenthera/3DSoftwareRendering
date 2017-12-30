package RenderingEngine;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Bitmap {


    private final int m_width;
    private final int m_height;
    private final byte[] m_components;

    public byte GetComponent(int index){
         return m_components[index];
    }

    public Bitmap(int width, int height)
    {
        m_width      = width;
        m_height     = height;
        m_components = new byte[m_width * m_height * 4];
    }

    public Bitmap(String fileName) throws IOException
    {
        int width         = 0;
        int height        = 0;
        byte[] components = null;

        BufferedImage image = ImageIO.read(new File(fileName));

        width = image.getWidth();
        height = image.getHeight();

        int imgPixels[] =  new int[width * height];
        image.getRGB(0,0, width, height, imgPixels, 0, width);
        components = new byte[width * height * 4];

        for (int i = 0; i < width * height; i++) {
            int pixel = imgPixels[i];
            components[i * 4]     = (byte)((pixel >> 24) & 0xFF);
            components[i * 4 + 1] = (byte)((pixel >> 16 ) & 0xFF);
            components[i * 4 + 2] = (byte)((pixel >> 8 ) & 0xFF);
            components[i * 4 + 3] = (byte)((pixel      ) & 0xFF);
        }

        m_width = width;
        m_height = height;
        m_components = components;
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

    public void CopyPixel(int destX, int destY, int srcX, int srcY, Bitmap src)
    {

        int destIndex = (destX + destY * m_width) * 4;
        int srcIndex = (srcX + srcY * src.getWidth()) * 4;
        m_components[destIndex    ] = src.GetComponent(srcIndex);
        m_components[destIndex + 1] = src.GetComponent(srcIndex + 1);
        m_components[destIndex + 2] = src.GetComponent(srcIndex + 2);
        m_components[destIndex + 3] = src.GetComponent(srcIndex + 3);
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
