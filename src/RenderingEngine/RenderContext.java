package RenderingEngine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RenderContext extends Bitmap {

    private float[] m_zBuffer;

    public RenderContext(int width, int height)
    {
        super(width, height);
        m_zBuffer = new float[width * height];
    }

    public void clearDepthBuffer(){
        for (int i = 0; i < m_zBuffer.length; i++) {
            m_zBuffer[i] = Float.MAX_VALUE;
        }
    }

    public void DrawTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture){
        if(v1.isInsideViewFrustum() && v2.isInsideViewFrustum() && v3.isInsideViewFrustum()){
            FillTriangle(v1, v2, v3, texture);
            return;
        }


        List<Vertex> vertices = new ArrayList<>();
        List<Vertex> auxList = new ArrayList<>();

        vertices.add(v1);
        vertices.add(v2);
        vertices.add(v3);

        if(ClipPolygonAxis(vertices, auxList, 0) &&
                ClipPolygonAxis(vertices, auxList, 1) &&
                ClipPolygonAxis(vertices, auxList, 2))
        {
            Vertex initialVertex = vertices.get(0);

            for (int i = 0; i < vertices.size() - 1; i++) {
                FillTriangle(initialVertex, vertices.get(i), vertices.get(i + 1), texture);
            }
        }
    }

    private boolean ClipPolygonAxis(List<Vertex> vertices, List<Vertex> auxList, int componentIndex){
        ClipPolygonComponent(vertices, componentIndex, 1.0f, auxList);
        vertices.clear();
        if(auxList.isEmpty()){
            return false;
        }

        ClipPolygonComponent(auxList, componentIndex, -1.0f, vertices);
        auxList.clear();

        return !vertices.isEmpty();
    }

    private void ClipPolygonComponent(List<Vertex> vertices, int componentIndex, float componentFactor
                , List<Vertex> result){

        Vertex previousVertex = vertices.get(vertices.size()-1);
        float previousComponent = previousVertex.Get(componentIndex) * componentFactor;
        boolean previousInside = previousComponent <= previousVertex.getPos().GetW();

        Iterator<Vertex> it = vertices.iterator();

        while(it.hasNext()){
            Vertex currentVertex = it.next();
            float currentComponent = currentVertex.Get(componentIndex) * componentFactor;
            boolean currentInside = currentComponent <= currentVertex.getPos().GetW();

            if(currentInside ^ previousInside){
                float lerpAmt = (previousVertex.getPos().GetW() - previousComponent) /
                        ((previousVertex.getPos().GetW() - previousComponent) - (currentVertex.getPos().GetW() - currentComponent));

                result.add(previousVertex.Lerp(currentVertex, lerpAmt));
            }

            if(currentInside){
                result.add(currentVertex);
            }

            previousVertex = currentVertex;
            previousComponent = currentComponent;
            previousInside = currentInside;
        }

    }



    private void FillTriangle(Vertex v1, Vertex v2, Vertex v3, Bitmap texture)
    {
        Matrix4f screenSpaceTransform = new Matrix4f().InitScreenSpaceTransform(getWidth() / 2, getHeight() / 2);
        Matrix4f identity = new Matrix4f().InitIdentity();
        Vertex minY = v1.Transform(screenSpaceTransform, identity).PerspectiveDivide();
        Vertex midY = v2.Transform(screenSpaceTransform, identity).PerspectiveDivide();
        Vertex maxY = v3.Transform(screenSpaceTransform, identity).PerspectiveDivide();

        if(minY.TriangleAreaTimesTwo(maxY, midY) >= 0){return;}

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

        ScanEdges(topToBottom, topToMiddle, handedness, texture);
        ScanEdges(topToBottom, middleToBottom, handedness, texture);
    }

    private void ScanEdges(Edge a, Edge b, boolean handedness, Bitmap texture){
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
            DrawScanLine(left, right, j, texture);
            left.Step();
            right.Step();
        }
    }

    private void DrawScanLine(Edge left, Edge right, int j, Bitmap texture)
    {
        int xMin = (int)Math.ceil(left.getX());
        int xMax = (int)Math.ceil(right.getX());

        float xPrestep = xMin - left.getX();

        float xDist = right.getX() - left.getX();
        float texCoordXXStep = (right.getTexCoordX() - left.getTexCoordX()) / xDist;
        float texCoordYXStep = (right.getTexCoordY() - left.getTexCoordY()) / xDist;
        float oneOverZXStep =  (right.GetOneOverZ() - left.GetOneOverZ())/xDist;
        float depthXStep =  (right.GetDepth() - left.GetDepth())/xDist;

        float texCoordX = left.getTexCoordX() + texCoordXXStep * xPrestep;
        float texCoordY = left.getTexCoordY() + texCoordYXStep * xPrestep;
        float oneOverZ = left.GetOneOverZ() + oneOverZXStep * xPrestep;
        float depth = left.GetDepth() + depthXStep * xPrestep;

        for (int i = xMin; i < xMax; i++) {

            int index = i + j * getWidth();

            if(depth < m_zBuffer[index])
            {
                m_zBuffer[index] = depth;
                float z = 1.0f / oneOverZ;
                int srcX = (int) ((texCoordX * z) * (texture.getWidth() - 1) + 0.5f);
                int srcY = (int) ((texCoordY * z) * (texture.getHeight() - 1) + 0.5f);

                CopyPixel(i, j, srcX, srcY, texture);
            }

            oneOverZ += oneOverZXStep;
            texCoordX += texCoordXXStep;
            texCoordY += texCoordYXStep;
            depth += depthXStep;
        }
    }
}
