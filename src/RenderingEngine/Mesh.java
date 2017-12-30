package RenderingEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private List<Vertex> m_vertices;
    private List<Integer> m_indices;



    public Mesh(String fileName) throws IOException{
        IndexedModel model = new OBJModel(fileName).ToIndexedModel();

        m_vertices = new ArrayList<Vertex>();
        for (int i = 0; i < model.GetPositions().size(); i++) {
            m_vertices.add(new Vertex(model.GetPositions().get(i),
                                      model.GetTexCoords().get(i),
                                    model.GetNormals().get(i)));
        }

        m_indices = model.GetIndices();
    }

    public void Draw(RenderContext context, Matrix4f viewProjection, Matrix4f transform, Bitmap texture)
    {
        for (int i = 0; i < m_indices.size(); i += 3)
        {
            context.DrawTriangle(
                    m_vertices.get(m_indices.get(i)).Transform(viewProjection, transform),
                    m_vertices.get(m_indices.get(i + 1)).Transform(viewProjection, transform),
                    m_vertices.get(m_indices.get(i + 2)).Transform(viewProjection, transform),
                    texture);
        }
    }
}
