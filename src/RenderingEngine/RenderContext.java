package RenderingEngine;

public class RenderContext extends Bitmap {

    private final int[] m_scanBuffer;

    public RenderContext(int width, int height)
    {
        super(width, height);
        m_scanBuffer = new int[height * 2];

    }

    public void DrawScanBuffer(int yCoord, int xMin, int xMax)
    {
        m_scanBuffer[yCoord * 2    ] = xMin;
        m_scanBuffer[yCoord * 2 + 1] = xMax;
    }

    public void FillShape(int yMin, int yMax)
    {
        for(int j = yMin; j < yMax; j++) {
            if(j * 2 + 1 > m_scanBuffer.length){return;}
            int xMin = m_scanBuffer[j * 2];
            int xMax = m_scanBuffer[j * 2 + 1];


            for (int i = xMin; i < xMax; i++) {
                DrawPixel(i, j, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF);
            }
        }
    }

    public void FillTriangle

    public void ScanConvertTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, int handedness){
        ScanConvertLine(minYVert, maxYVert, 0 + handedness);
        ScanConvertLine(minYVert, midYVert, 1 - handedness);
        ScanConvertLine(midYVert, maxYVert, 1 - handedness);
    }

    private void ScanConvertLine(Vertex minYVert, Vertex maxYVert, int whichSide){
        int yStart = (int)minYVert.getY();
        int yEnd =   (int)maxYVert.getY();
        int xStart = (int)minYVert.getX();
        int xEnd =   (int)maxYVert.getX();

        int yDist = yEnd - yStart;
        int xDist = xEnd - xStart;

        if(yDist <= 0)
        {
            return;
        }

        float xStep = (float)xDist / (float)yDist;
        float curX = (float)xStart;

        for (int j = yStart; j < yEnd; j++) {
            if(j * 2 + whichSide >= m_scanBuffer.length) {
                return;
            }
            m_scanBuffer[j * 2 + whichSide] = (int)curX;
            curX += xStep;
        }
    }

}
