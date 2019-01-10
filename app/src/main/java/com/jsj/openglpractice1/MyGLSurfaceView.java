package com.jsj.openglpractice1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderder mRenderer;
    private Triangle mTriangle;
    private Square mSquate;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderder();
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private class MyGLRenderder implements GLSurfaceView.Renderer {
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // initialize a trangle;
            mTriangle = new Triangle();
            //Squate = new Square();
            mSquate = new Square();


            // Set the background frame color
            GLES20.glClearColor(0.5f,0.0f,0.0f,1.0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            mTriangle.draw();
        }
    }

    public static int loadShader( int type, String shaderCode){
        //create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        //or a fragment shader type (GLES20.GL_GRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        //add the source code to the shader and compile it
        GLES20.glShaderSource(shader,shaderCode);
        GLES20.glCompileShader(shader);

        return  shader;
    }
}
