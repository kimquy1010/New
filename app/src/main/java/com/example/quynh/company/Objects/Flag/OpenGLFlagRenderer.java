package com.example.quynh.company.Objects.Flag;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import com.example.quynh.company.Constants.AppConstants;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.content.ContentValues.TAG;

public class OpenGLFlagRenderer implements GLSurfaceView.Renderer {

    private Context context;

    private int textures[];

    private OpenGLFlag flag;

    private boolean paused = false;

    public OpenGLFlagRenderer(Context context) {
        this.context = context;
    }

    public void togglePause() {
        paused = !paused;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // bind texture
        textures = new int[1];
        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glGenTextures(textures.length, textures, 0);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), AppConstants.FLAG_TEXTURE);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_NEAREST);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        // ratio
        float ratio = (float) width / height;
        Log.d(TAG, "Test ratio: " + ratio + ", width: " + width + ", height: " + height);

        // flag
        flag = new OpenGLFlag(textures[0], 0, 0, 0, ratio, ratio);

        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glClearDepthf(1.0f);
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glDisable(GL10.GL_BLEND); //Anti-aliasing by blending color
        gl.glEnable(GL10.GL_POINT_SMOOTH);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA); // https://www.opengl.org/sdk/docs/man2/xhtml/glBlendFunc.xml Blend color

        gl.glViewport(0, 0, width, height);
        gl.glClearColor(1,1,1,0); //make back ground
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glFrustumf(-ratio, ratio, -1, 1, 3, 7); // https://www.opengl.org/sdk/docs/man2/xhtml/glFrustum.xml

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, -2f, 0, 3.5f, 0, 0, 0, 0.05f, 1, 0f); // https://www.opengl.org/sdk/docs/man2/xhtml/gluLookAt.xml
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        // clear
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        gl.glPushMatrix();

        // rotate
        gl.glRotatef(AppConstants.FLAG_ROTATION_X, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(AppConstants.FLAG_ROTATION_Y, 0.0f, 1.0f, 0.0f);
        gl.glRotatef(AppConstants.FLAG_ROTATION_Z, 0.0f, 0.0f, 1.0f);
        Canvas canvas = new Canvas();
        RectF r = new RectF(10, 10, 150, 150);
        Paint paint = new Paint();
        paint.setColor(context.getResources().getColor(android.R.color.white));
        canvas.drawRect(r , paint );
        // draw
        flag.draw(gl, paused);

        gl.glPopMatrix();
    }


}
