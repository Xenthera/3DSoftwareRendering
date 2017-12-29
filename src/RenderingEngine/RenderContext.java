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
                DrawPixel(i, j, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0x00);
            }
        }
    }

    public void FillTriangle(Vertex v1, Vertex v2, Vertex v3)
    {
        Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(getWidth() / 2, getHeight() / 2);
        Vertex minY = v1.Transform(screenSpaceTransform).PerspectiveDivide();
        Vertex midY = v2.Transform(screenSpaceTransform).PerspectiveDivide();
        Vertex maxY = v3.Transform(screenSpaceTransform).PerspectiveDivide();

        if(maxY.getY() < midY.getY()){
            Vertex temp = maxY;
            maxY = midY;
            midY = temp;
        }
        if(midY.getY() < minY.getY()){
            Vertex temp = midY;
            midY = minY;
            minY = temp;
        }
        if(maxY.getY() < midY.getY()){
            Vertex temp = maxY;
            maxY = midY;
            midY = temp;
        }

        float area = minY.TriangleAreaTimesTwo(maxY, midY);
        int handedness = area >= 0 ? 1 : 0;

        ScanConvertTriangle(minY,midY,maxY, handedness);
        FillShape((int)Math.ceil(minY.getY()), (int)Math.ceil(maxY.getY()));
    }

    public void ScanConvertTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, int handedness){
        ScanConvertLine(minYVert, maxYVert, 0 + handedness);
        ScanConvertLine(minYVert, midYVert, 1 - handedness);
        ScanConvertLine(midYVert, maxYVert, 1 - handedness);
    }

    private void ScanConvertLine(Vertex minYVert, Vertex maxYVert, int whichSide){
        int yStart = (int)Math.ceil(minYVert.getY());
        int yEnd =   (int)Math.ceil(maxYVert.getY());
        int xStart = (int)Math.ceil(minYVert.getX());
        int xEnd =   (int)Math.ceil(maxYVert.getX()) ;

        float yDist = maxYVert.getY() - minYVert.getY();
        float xDist = maxYVert.getX() - minYVert.getX();

        if(yDist <= 0)
        {
            return;
        }

        float xStep = xDist / yDist;
        float yPrestep = yStart - minYVert.getY();
        float curX = minYVert.getX() + yPrestep * xStep;

        for (int j = yStart; j < yEnd; j++) {
            if(j * 2 + whichSide >= m_scanBuffer.length) {
                return;
            }
            m_scanBuffer[j * 2 + whichSide] = (int)Math.ceil(curX);
            curX += xStep;
        }
    }

}
