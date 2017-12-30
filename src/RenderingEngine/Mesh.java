package RenderingEngine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private List<Vertex> m_vertices;
    private List<Integer> m_indices;

    public Vertex getVertex(int i){return m_vertices.get(i);}
    public int    getIndex(int i) {return m_indices.get(i);}
    public int    getNumIndicies(){return m_indices.size();}

    public Mesh(String fileName) throws IOException{
        IndexedModel model = new OBJModel(fileName).ToIndexedModel();

        m_vertices = new ArrayList<Vertex>();
        for (int i = 0; i < model.GetPositions().size(); i++) {
            m_vertices.add(new Vertex(model.GetPositions().get(i),
                                      model.GetTexCoords().get(i)));
        }

        m_indices = model.GetIndices();
    }
}
