package com.jme3.test.equirect;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.AssetManager;
import com.jme3.convert.CubeMapToEquirectangular;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Image;
import com.jme3.util.SkyFactory;
import java.io.File;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.setShowSettings(false);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        Node scene = setupTestScene(assetManager);
        rootNode.attachChild(scene);
        EnvironmentCamera cubeCam = new EnvironmentCamera(256, new Vector3f(), Image.Format.RGB8);
        stateManager.attach(cubeCam);
        
        CubeMapToEquirectangular cubeToEqui = new CubeMapToEquirectangular(cubeCam, System.getProperty("user.home") + File.separator + "jMonkey-" + System.currentTimeMillis() / 1000 + ".avi", 0.8f, 30, 1024, 720, 0);
        stateManager.attach(cubeToEqui);
    }
    
    private Node setupTestScene(AssetManager assetManager){
        Node testScene = new Node();
        Material defMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        defMat.setColor("Color", ColorRGBA.Blue);
        
        Geometry cube = new Geometry("Cube", new Sphere(5,5,5));
        
        cube.setMaterial(defMat);
        cube.setLocalTranslation(-5, 0, 7);
        testScene.attachChild(cube);
        
        Geometry cube2 = cube.clone(true);
        cube2.getMaterial().setColor("Color", ColorRGBA.Red);
        cube2.setLocalTranslation(0, 0, -7);
        testScene.attachChild(cube2);
        
        Geometry cube3 = cube.clone(true);
        cube3.getMaterial().setColor("Color", ColorRGBA.Green);
        cube3.setLocalTranslation(7, 0, 0);
        testScene.attachChild(cube3);
        return testScene;
    }

    @Override
    public void simpleUpdate(float tpf) {
    }

    @Override
    public void simpleRender(RenderManager rm) {
    }
}
