/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jme3.convert;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.app.state.CustomVideoRecorderAppState;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.generation.JobProgressListener;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Quad;
import com.jme3.shader.VarType;
import com.jme3.texture.FrameBuffer;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.TextureCubeMap;
import java.io.File;

/**
 *
 * @author rickard
 */
public class CubeMapToEquirectangular extends BaseAppState implements JobProgressListener<TextureCubeMap>{
    
    private EnvironmentCamera cubeCam;
    private Geometry renderQuad;
    private ViewPort offView;
    private Texture2D offTex;
    private String fileName;
    private int frameRate;
    private float quality;
    private int outputWidth;
    private int outputHeight;
    private float aspectRatio;
    private float rotationX;
    
    public CubeMapToEquirectangular(EnvironmentCamera cubeCam, String fileName, float quality, int frameRate, int outputWidth, int outputHeight, float rotationX){
        this.cubeCam = cubeCam;
        this.fileName = fileName;
        this.frameRate = frameRate;
        this.quality = quality;
        this.outputWidth = outputWidth;
        this.outputHeight = outputHeight;
        aspectRatio = outputWidth / outputHeight;
        this.rotationX = (rotationX / FastMath.TWO_PI);
    }

    @Override
    protected void initialize(Application app) {

        Material m = new Material(app.getAssetManager(), "MatDefs/CubeMapToEquiRect.j3md");
        m.setFloat("RotationX", rotationX);
        renderQuad = new Geometry("Projection", new Quad(2f, 1f));
        
        renderQuad.setMaterial(m);
        renderQuad.move(-1f, -0.5f, 1f);
        

        createOffView(app.getRenderManager());
        CustomVideoRecorderAppState customRecorder = new CustomVideoRecorderAppState(new File(fileName), quality, frameRate);
        app.getStateManager().attach(customRecorder);
        
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
        renderQuad.updateLogicalState(tpf);
        renderQuad.updateGeometricState();
    }
   
    
    @Override
    public void render(RenderManager rm) {
        super.render(rm);
        
    }

    @Override
    public void postRender() {
        super.postRender();
        cubeCam.snapshot(((SimpleApplication)getApplication()).getRootNode(), this );
    }
    
    
    

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

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
        renderQuad.getMaterial().setTextureParam("Map", VarType.TextureCubeMap, result);
        
    }
    
    private ViewPort createOffView(RenderManager renderManager){
        Camera offCamera = new Camera(outputWidth, outputHeight);
        offView = renderManager.createPostView("Offscreen View", offCamera);
        offView.setClearFlags(false, false, false);
        offView.setBackgroundColor(ColorRGBA.Yellow);
        offView.attachScene(renderQuad);
        // create offscreen framebuffer
        FrameBuffer offBuffer = new FrameBuffer(outputWidth, outputHeight, 1);

        //setup framebuffer's cam
        offCamera.setParallelProjection(true);
        float frustumSize = 1f;
        float aspect = 1f;
        offCamera.setFrustum(-1000, 1000, -aspect * frustumSize, aspect * frustumSize, frustumSize * 0.5f, -frustumSize * 0.5f);
        
        offCamera.lookAtDirection(new Vector3f(0f, 0f, -1f), Vector3f.UNIT_Y);

        //setup framebuffer's texture
        offTex = new Texture2D(outputWidth, outputHeight, Image.Format.RGB8);
        offTex.setMinFilter(Texture.MinFilter.Trilinear);
        offTex.setMagFilter(Texture.MagFilter.Bilinear);
        offBuffer.setDepthBuffer(Image.Format.Depth);
        offBuffer.setColorTexture(offTex);
        offView.setOutputFrameBuffer(offBuffer);
        return offView;
    }
}
