package me.rsk.hiroshima.util;

import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

// Coded by Rina.
// Memezs!! Look, if you want use OpenGL you can use my helper i will make somethings for gui.
// Like DoubleButton or other.
// Or talk with me.

// TurokGL, autor: SrRina, created in 06/03/20.
public class TurokGL {
    // RGBA, or be red, green, blue and alpha.
    public static void turok_RGBA(float red, float green, float blue, float alpha) {
        GL11.glColor4f(red / 255.0f, green / 255.0f, blue / 255.0f, alpha / 255.0f);
    }

    // RGB, or be red, green and blue.
    public static void turok_RGB(float red, float green, float blue) {
        GL11.glColor3f(red / 255.0f, green / 255.0f, blue / 255.0f);
    }

    // Enable GL.
    public static void turok_Enable(int opengl) {
        GL11.glEnable(opengl);
    }

    // Disable GL.
    public static void turok_Disable(int opengl) {
        GL11.glDisable(opengl);
    }

    // Use it.
    public static void turok_StartGL(String tag) {
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    // Use it too.
    public static void turok_FixGL(String tag) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static void turok_Translatef(int x_, int y_, int z_) {
        GL11.glTranslatef(x_, y_, z_);
    }
}