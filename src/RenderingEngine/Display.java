package RenderingEngine;

import engine.Renderer;

public class Display {


    RenderContext m_frameBuffer;

    public Display(Renderer renderer){
        m_frameBuffer = new RenderContext(renderer.getWidth(), renderer.getHeight());
        m_frameBuffer.Clear((byte)0x00);

    }

    public void SwapBuffers(Renderer renderer)
    {
        m_frameBuffer.CopyToIntArray(renderer.getPixels());
    }

    public RenderContext getFrameBuffer() {
        return m_frameBuffer;
    }
}
