/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.test.equirect;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.generation.JobProgressAdapter;
import com.jme3.environment.generation.JobProgressListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.shader.VarType;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.TextureCubeMap;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import jme3tools.converters.ImageToAwt;

/**
 *
 * @author rickard
 */
public class CubeMapToEquirectangularTest extends SimpleApplication{
    
    private Node testScene = new Node();
    private EnvironmentCamera cubeCam;
    private Geometry quad, quad2;
    
    public static void main(String[] args){
        CubeMapToEquirectangularTest test = new CubeMapToEquirectangularTest();
        test.setShowSettings(false);
        test.start();
    }
    
    public CubeMapToEquirectangularTest(){
    }
    
    @Override
    public void simpleInitApp() {
        cubeCam = new EnvironmentCamera(256, new Vector3f(), Image.Format.RGB8);
        stateManager.attach(cubeCam);
        Material m2 = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
        quad2 = new Geometry("Projection", new Quad(50f, 50f));
        quad2.setMaterial(m2);
        quad2.move(25f, -25f, -50);
        getRootNode().attachChild(quad2);


        Material m = new Material(getAssetManager(), "MatDefs/CubeMapToEquiRect.j3md");
        
        quad = new Geometry("Projection", new Quad(50f, 50f));
        quad.setMaterial(m);
        quad.move(-25f, -25f, -50);
        getRootNode().attachChild(quad);
        
        
        setupTestScene(getAssetManager());
    }
    
    private void setupTestScene(AssetManager assetManager){
        Material defMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        defMat.setColor("Color", ColorRGBA.Blue);
        
        Geometry cube = new Geometry("Cube", new Sphere(5,5,5));
        
        cube.setMaterial(defMat);
        cube.setLocalTranslation(2, 0, 7);
        testScene.attachChild(cube);
        
        Geometry cube2 = cube.clone(true);
        cube2.getMaterial().setColor("Color", ColorRGBA.Red);
        cube2.setLocalTranslation(2, 0, -7);
        testScene.attachChild(cube2);
        
        Geometry cube3 = cube.clone(true);
        cube3.getMaterial().setColor("Color", ColorRGBA.Green);
        cube3.setLocalTranslation(9, 0, 0);
        testScene.attachChild(cube3);
    }

    @Override
    public void simpleUpdate(float tpf) {
        testScene.rotate(0, tpf * 0.01f, 0);
        testScene.updateLogicalState(tpf);
        testScene.updateGeometricState();
        
    }
    
    private boolean rendered = false;

    @Override
    public void simpleRender(RenderManager rm) {
//        if(!rendered){
            cubeCam.snapshot(testScene, progressListener );
            rendered = true;
//        }
        
    }
    
    private JobProgressListener<TextureCubeMap> progressListener = new JobProgressAdapter<TextureCubeMap>() {
            
    @Override
    public void start() {
    }

    @Override
    public void step(String message) {
    }

    @Override
    public void progress(double value) {
    }

    @Override
    public void done(TextureCubeMap result) {
//          System.out.println("done");
        Texture2D t = new Texture2D(result.getImage());
//        quad2.getMaterial().setTexture("ColorMap", t);
        quad.getMaterial().setTextureParam("Map", VarType.TextureCubeMap, result);
//        try {
//            saveImage((quad.getMaterial().getTextureParam("ColorMap")).getTextureValue().getImage());
//        } catch (IOException ex) {
//            Logger.getLogger(CubeMapToEquirectangular.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
};

    private void saveImage(Image image) throws IOException{
        System.out.println("format " + image.getFormat());
        
        BufferedImage img = ImageToAwt.convert(image, true, false, 0);
        ImageIO.write(img, "png", new File(System.getProperty("user.dir")+"/test.png"));
    }

}
