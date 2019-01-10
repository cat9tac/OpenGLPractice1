package com.jsj.openglpractice1;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {
    private static final int COORDS_PER_VERTEX = 3;

    private final int vertexCount = trangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex
    private int mPositionHandle;
    private int mColorHandle;

    private final int mProgram;

    private FloatBuffer vertexBuffer;

    static float trangleCoords[] = { // in counterclockwise order:
            0.0f,0.622008459f,0.0f,     //top
            -0.5f,-0.311004243f,0.0f,   //bottom left
            0.5f,-0.311004243f,0.0f,    //bottom right
    };
    // Set color with red,green,blue and alpha (opacity) value
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
            "void main() {" +
            "   gl_Position = vPosition;" +
            "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
            "uniform vec4 vColor;" +
            "void main() {" +
             "  gl_FragColor = vColor;" +
             "}";


    public Triangle() {
        //initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                trangleCoords.length * 4);
        //use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());

        //create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(trangleCoords);
        vertexBuffer.position(0);

        int vetexShader = MyGLSurfaceView.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = MyGLSurfaceView.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);
        //create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        //add the vertex shader to program
        GLES20.glAttachShader(mProgram,vetexShader);

        //add the fragment shader to program
        GLES20.glAttachShader(mProgram,fragmentShader);

        //create OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    public void draw() {
        //Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(mProgram,"vPosition");

        //Enable a handle to the trangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle,COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,false,
                vertexStride,vertexBuffer);

        //get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram,"vColor");

        //Set color for drawing the trangle
        GLES20.glUniform4fv(mColorHandle,1,color,0);

        //Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,vertexCount);

        //Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);

    }
}
