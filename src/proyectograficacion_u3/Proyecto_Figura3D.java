/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author ERIKA GARCIA
 */

package proyectograficacion_u3;


import com.jogamp.opengl.GL;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLJPanel;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

    class Proyecto_Figura3D extends GLJPanel implements GLEventListener, KeyListener {

    private static String TITLE = "Proyecto_EJGG";  // window's title
    private static final int CANVAS_WIDTH = 800;  // width of the drawable
    private static final int CANVAS_HEIGHT = 480; // height of the drawable
    private static final int FPS = 60; // animator's target frames per second
    private static final float factInc = 5.0f; // animator's target frames per second
    private float fovy = 45.0f;

    //////////////// Variables /////////////////////////
    // Referencias de rotacion
    float rotX = 90.0f;
    float rotY = 0.0f;
    float rotZ = 0.0f;

    // Posicion de la luz.
    float lightX = 1f;
    float lightY = 1f;
    float lightZ = 1f;
    float dLight = 0.05f;

    // Posicion de la camara
    float camX = 2.0f;
    float camY = 2.0f;
    float camZ = 8.0f;

    // Material y luces.       R        G       B      A
    private float aspect;
    
    ///////////////// Funciones /////////////////////////
    public Proyecto_Figura3D() {
        this.addGLEventListener(this);
        this.addKeyListener(this);
    }

    /////////////// Define Luz y Material /////////
    private GLU glu;  // para las herramientas GL (GL Utilities)
    private GLUT glut;
    private Texture textura;
   
    Texture cargarTextura(String imageFile){
        Texture text1 = null;
       try {
            BufferedImage buffImage = ImageIO.read(new File(imageFile));           
            text1 = AWTTextureIO.newTexture(GLProfile.getDefault(),buffImage,false);
             System.out.println("Textura cargada correctamente: " + imageFile);
       } catch (IOException ioe){
           System.out.println("Problema al cargar el archivo "+imageFile);
       }  
       return text1;
    }
    
    public void init(GLAutoDrawable drawable) {
    GL2 gl = drawable.getGL().getGL2();

    // Establece un color de fondo para simular el cielo del desierto
 // gl.glClearColor(0.9f, 0.9f, 0.8f, 1.0f); // Un tono claro de amarillo/beige
    gl.glClearColor(0.937f, 0.847f, 0.667f, 1.0f);
    // gl.glClearColor(0.678f, 0.847f, 0.902f, 1.0f); //Azulg
    // Habilita la iluminación y la prueba de profundidad
    gl.glEnable(GL2.GL_LIGHTING);
    gl.glEnable(GL2.GL_LIGHT0);
    gl.glEnable(GL.GL_DEPTH_TEST);

    // Configura la posición de la luz 
    float lightPosition[] = {1.0f, 1.0f, 1.0f, 0.0f}; // Dirección de la luz
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, lightPosition, 0);

    // Ajusta el color de la luz
    final float[] colorDesertSun = {1.0f, 0.8f, 0.6f, 1.0f};
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, colorDesertSun, 0);
    gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, colorDesertSun, 0);

    // Ajusta el color de la iluminación ambiental
    final float[] ambientDesert = {0.8f, 0.8f, 0.7f, 1.0f};
    gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, ambientDesert, 0);

    // Ajusta el material para reflejar la arena del desierto
    final float[] desertMaterialDiffuse = {0.9f, 0.8f, 0.6f, 1.0f};
    final float[] desertMaterialSpecular = {0.3f, 0.3f, 0.2f, 1.0f};
    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, desertMaterialDiffuse, 0);
    gl.glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, desertMaterialSpecular, 0);

     this.initPosition(gl);

        glu = new GLU(); // get GL Utilities
        glut = new GLUT();
        this.textura = this.cargarTextura("imagenes/PYRAMID.jpg");
}

    public void initPosition(GL2 gl) {
        float posLight1[] = {lightX, lightY, lightZ, 1.0f};
        float spotDirection1[] = {0.0f, -1.f, 0.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight1, 0);   
    }

    /////////////// Move light ////////////////////////////
    // Move light 0.
    public void moveLightX(boolean positivDirection) {
        lightX += positivDirection ? dLight : -dLight;
    }

    public void moveLightY(boolean positivDirection) {
        lightY += positivDirection ? dLight : -dLight;
    }

    public void moveLightZ(boolean positivDirection) {
        lightZ += positivDirection ? dLight : -dLight;
    }

    public void animate(GL2 gl, GLU glu, GLUT glut) {
        float posLight0[] = {lightX, lightY, lightZ, 1.f};
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, posLight0, 0);   
        // Realiza la rotación 
        rotY += 0.2f;  
    }

    public void drawFigure(GL2 gl, GLUT glut) {
     //Tamaños de las piramides
     float scaleFactor = 5.0f; 
     float smallScaleFactor = 1.0f;
     float smallScaleFactor2 = 2.0f;
     // Agrega la imagen/textura
     textura.enable(gl);
     textura.bind(gl);
     gl.glRotatef(rotY, 0.0f, 1.0f, 0.0f);  
     gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);  
     // Base de la pirámide
     gl.glBegin(GL2.GL_QUADS);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado frontal de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado de atrás de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glEnd();

     // Lado izquierda de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(-0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado derecha de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(0.0f, 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(0.5f * scaleFactor, -0.5f, -0.5f * scaleFactor);
     gl.glEnd();
    
     // Piramide izquierda
     drawSmallFigure(gl, -4.0f, 0.0f, smallScaleFactor);
     drawSmallFigure(gl, -6.0f, 0.0f, smallScaleFactor2);
    
     // Piramide derecha
     drawSmallFigure(gl, 4.0f, 0.0f, smallScaleFactor);
     drawSmallFigure(gl, 6.0f, 0.0f, smallScaleFactor2);

}

     void drawSmallFigure(GL2 gl, float x, float y, float scaleFactor) {
   
         // Base de la pirámide
     gl.glBegin(GL2.GL_QUADS);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 1.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 1.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado frontal de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(x, y + 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado trasero de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(x, y + 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glEnd();
    
      // Lado izquierdo de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(x, y + 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x - 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glEnd();

     // Lado derecho de la pirámide
     gl.glBegin(GL2.GL_TRIANGLES);
     gl.glTexCoord2f(0.5f, 1.0f); gl.glVertex3f(x, y + 0.5f * scaleFactor, 0.0f);
     gl.glTexCoord2f(1.0f, 0.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, 0.5f * scaleFactor);
     gl.glTexCoord2f(0.0f, 0.0f); gl.glVertex3f(x + 0.5f * scaleFactor, y - 0.5f, -0.5f * scaleFactor);
     gl.glEnd();
}
     
    @Override
    public void dispose(GLAutoDrawable glad) {
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();  // get the OpenGL 2 graphics context
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();
        glu.gluPerspective(fovy, aspect, 0.1, 20.0);
        glu.gluLookAt(this.camX, this.camY, this.camZ, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();  // reset the model-view matrix
        gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
        //this.setSomeGreenMaterial( gl, GL.GL_FRONT );      
        //this.drawFigure(gl, glut);
        gl.glTranslatef(-2.0f, 0.0f, -2.0f);
        this.drawFigure(gl, glut);
        this.animate(gl, this.glu, this.glut);
        // Asociar la textura con el canvas
        this.textura.bind(gl);
        this.textura.enable(gl);    
        //this.drawFigure(gle);
        this.drawFigure(gl, glut);
        //this.drawCubeUVWmapped(gl);  
        this.textura.disable(gl);
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl = glad.getGL().getGL2();  // get the OpenGL 2 graphics context
        if (height == 0) {
            height = 1;   // prevent divide by zero
        }
        aspect = (float) width / height;
        // Set the view port (display area) to cover the entire window
        gl.glViewport(0, 0, width, height);
        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
        gl.glLoadIdentity();             // reset projection matrix
        glu.gluPerspective(fovy, aspect, 0.1, 50.0); // fovy, aspect, zNear, zFar
        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }

    public static void main(String[] args) {
        // Run the GUI codes in the event-dispatching thread for thread safety
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create the OpenGL rendering canvas
                GLJPanel canvas = new Proyecto_Figura3D();
                canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
                // Create a animator that drives canvas' display() at the specified FPS.
                final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);
                // Create the top-level container
                final JFrame frame = new JFrame(); // Swing's JFrame or AWT's Frame
                FlowLayout fl = new FlowLayout();
                frame.setLayout(fl);  
                frame.getContentPane().add(canvas);
                frame.addKeyListener((KeyListener) canvas);
                frame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        // Use a dedicate thread to run the stop() to ensure that the
                        // animator stops before program exits.
                        new Thread() {
                            @Override
                            public void run() {
                                if (animator.isStarted()) {
                                    animator.stop();
                                }
                                System.exit(0);
                            }
                        }.start();
                    }
                });
                frame.setTitle(TITLE);
                frame.pack();
                frame.setVisible(true);
                animator.start(); // start the animation loop
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int codigo = e.getKeyCode();
        //  lightX, lightY, lightZ
        System.out.println("codigo presionado = " + codigo);
        switch (codigo) {
            case KeyEvent.VK_DOWN:
                this.moveLightY(false);
                break;
            case KeyEvent.VK_UP:
                this.moveLightY(true);
                break;
            case KeyEvent.VK_RIGHT:
                this.moveLightX(true);
                break;
            case KeyEvent.VK_LEFT:
                this.moveLightX(false);
                break;
            case KeyEvent.VK_PAGE_UP:
                this.moveLightZ(false);
                break;
            case KeyEvent.VK_PAGE_DOWN:
                this.moveLightZ(true);
                break;

            case KeyEvent.VK_NUMPAD8:
                this.camY += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD2:
                this.camY -= 0.2f;
                break;
            case KeyEvent.VK_NUMPAD6:
                this.camX += 0.2f;
                break;
            case KeyEvent.VK_NUMPAD4:
                this.camX -= 0.2f;
                break;
            case KeyEvent.VK_Z:
                this.camZ += 0.2f;
                break;
            case KeyEvent.VK_A:
                this.camZ -= 0.2f;
                break;
        }
        System.out.println("rotX = " + rotX);
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}


