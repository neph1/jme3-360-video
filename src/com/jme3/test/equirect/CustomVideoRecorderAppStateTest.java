/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.test.equirect;

import com.jme3.app.SimpleApplication;
import com.jme3.asset.AssetManager;
import com.jme3.convert.CubeMapToEquirectangular;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Image;

/**
 *
 * @author rickard
 */
public class CustomVideoRecorderAppStateTest extends SimpleApplication {
    
    public static void main(String[] args){
        CustomVideoRecorderAppStateTest test = new CustomVideoRecorderAppStateTest();
        test.setShowSettings(false);
        test.start();
    }
    
    private Node testScene = new Node();
    private Geometry quad, quad2;
    
    @Override
    public void simpleInitApp() {
        EnvironmentCamera envCam = new EnvironmentCamera(1440, Vector3f.ZERO, Image.Format.RGB8);
        Vector3f cameraPosition = new Vector3f(-2.2f, 1.6f, 2.3f);
        envCam.setPosition(cameraPosition);
        CubeMapToEquirectangular cubeMapToEqui = new CubeMapToEquirectangular(envCam, "./test.mp4", 0.8f, 30, 3840, 2160, FastMath.PI + FastMath.QUARTER_PI);
        stateManager.attach(envCam);
        stateManager.attach(cubeMapToEqui);
        
        setupTestScene(assetManager);
    }
    
    
    private void setupTestScene(AssetManager assetManager){
        Material m = new Material(getAssetManager(), "MatDefs/CubeMapToEquiRect.j3md");
        
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
        rootNode.attachChild(testScene);
    }
    
    
}
