package RenderingEngine;

public class RenderContext extends Bitmap {

    public RenderContext(int width, int height)
    {
        super(width, height);
    }

    public void FillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture)
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

        ScanTriangle(minY,midY,maxY, minY.TriangleAreaTimesTwo(maxY, midY) >= 0, texture);

    }

    private void ScanTriangle(Vertex minYVert, Vertex midYVert, Vertex maxYVert, boolean handedness, Bitmap texture)
    {
        Gradients gradients = new Gradients(minYVert, midYVert, maxYVert);
        Edge topToBottom    = new Edge(gradients, minYVert, maxYVert, 0);
        Edge topToMiddle    = new Edge(gradients, minYVert, midYVert, 0);
        Edge middleToBottom = new Edge(gradients, midYVert, maxYVert, 1);

        ScanEdges(gradients, topToBottom, topToMiddle, handedness, texture);
        ScanEdges(gradients, topToBottom, middleToBottom, handedness, texture);
    }

    private void ScanEdges(Gradients gradients, Edge a, Edge b, boolean handedness, Bitmap texture){
        Edge left = a;
        Edge right = b;

        if(handedness){
            Edge temp = left;
            left = right;
            right = temp;
        }

        int yStart = b.getYStart();
        int yEnd   = b.getYEnd();

        for (int j = yStart; j < yEnd; j++) {
            DrawScanLine(gradients, left, right, j, texture);
            left.Step();
            right.Step();
        }
    }

    private void DrawScanLine(Gradients gradients, Edge left, Edge right, int j, Bitmap texture)
    {
        int xMin = (int)Math.ceil(left.getX());
        int xMax = (int)Math.ceil(right.getX());

        float xPrestep = xMin - left.getX();

        float texCoordX = left.getTexCoordX() + gradients.GetTexCoordXXStep() * xPrestep;
        float texCoordY = left.getTexCoordY() + gradients.GetTexCoordYXStep() * xPrestep;


//        Vector4f minColor = left.getColor().Add(gradients.getColorXStep().Mul(xPresetep));
//        Vector4f maxColor = right.getColor().Add(gradients.getColorXStep().Mul(xPresetep));


        for (int i = xMin; i < xMax; i++) {

//            byte r = (byte)(color.GetX() * 255.0f + 0.5f);
//            byte g = (byte)(color.GetY() * 255.0f + 0.5f);
//            byte b = (byte)(color.GetZ() * 255.0f + 0.5f);

//            DrawPixel(i, j, (byte)0xFF, b, g, r);
            int srcX = (int)(texCoordX * (texture.getWidth() - 1) + 0.5f);
            int srcY = (int)(texCoordY * (texture.getWidth() - 1) + 0.5f);

            CopyPixel(i, j, srcX, srcY, texture);

            texCoordX += gradients.GetTexCoordXXStep();
            texCoordY += gradients.GetTexCoordYXStep();

        }
    }
}
