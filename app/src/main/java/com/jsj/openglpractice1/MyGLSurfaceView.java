package com.jsj.openglpractice1;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

class MyGLSurfaceView extends GLSurfaceView {
    private final MyGLRenderder mRenderer;
    private Triangle mTriangle;
    private Square mSquate;
    //mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float[] mRotationMatrix = new float[16];

    private final float TOUCH_SCALE_FACTOR = 180.f/320;
    private float mPreviewX;
    private float mPreviewY;

    public MyGLSurfaceView(Context context) {
        super(context);
        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        mRenderer = new MyGLRenderder();
        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);
        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        //Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //MotionEvent reports input details from the touch screen
        //and other input controls. In this case ,you are only
        //interested in events where the touch osition changed

        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviewX;
                float dy = y - mPreviewY;

                //reverse direction of rotation above the mid-line
                if(y < getHeight() / 2){
                    dx = dx * -1;
                }

                //reverse direction of rotation to left of the mid-line
                if(x < getWidth() / 2) {
                   dy = dy * -1;
                }

                mRenderer.setmAngle(mRenderer.getmAngle() +
                        ((dx + dy) * TOUCH_SCALE_FACTOR));
                requestRender();
        }

        mPreviewX = x;
        mPreviewY = y;
        return true;
    }

    private class MyGLRenderder implements GLSurfaceView.Renderer {
        public volatile float mAngle;

        public float getmAngle() {
            return mAngle;
        }

        public void setmAngle(float mAngle) {
            this.mAngle = mAngle;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            // initialize a trangle;
            mTriangle = new Triangle();
            //Squate = new Square();
            mSquate = new Square();


            // Set the background frame color
            GLES20.glClearColor(0.0f,0.0f,0.0f,1.0f);
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0,0,width,height);

            float ratio = (float) width / height;

            //this projection matrix is applied to object coordinates
            //in the onDrawFrame method
            Matrix.frustumM(mProjectionMatrix,0,-ratio,ratio,-1,1,3,7);

        }

        @Override
        public void onDrawFrame(GL10 gl) {
            float[] scratch = new float[16];

            // Redraw background color
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            //Set the camera position ( View matix)
            Matrix.setLookAtM(mViewMatrix,0,0,0,-3,0f,0f,0f,0f,1.0f,0.0f);

            //Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix,0,mProjectionMatrix,0,mViewMatrix,0);

            //Create a rotation transformation for the trangle
            long time = SystemClock.uptimeMillis() % 4000L;
            float angle = 0.090f * ((int) time);
            Matrix.setRotateM(mRotationMatrix,0,angle,0,0,-1.0f);

            //Combine the rotation matrix with the projection and camera view
            //Note that the mMVPMatrix factor *must be first in order
            //for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch,0,mMVPMatrix,0,mRotationMatrix,0);

            //Draw shape1
            //  mTriangle.draw();
            //Draw shape2
            // mTriangle.draw(mMVPMatrix);
            //Draw triangle3
            mTriangle.draw(scratch);
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
