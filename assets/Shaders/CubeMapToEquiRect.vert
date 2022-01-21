#import "Common/ShaderLib/GLSLCompat.glsllib"
uniform mat4 g_ViewMatrix;
uniform mat4 g_ProjectionMatrix;
uniform mat4 g_WorldMatrixInverse;
uniform mat4 g_WorldViewProjectionMatrix;

attribute vec3 inPosition;
attribute vec2 inTexCoord;
varying vec2 texCoord;
varying vec3 dir;
#define M_PI 3.1415926535897932384626433832795

void main()  {
    texCoord = vec2( 1.0 - inTexCoord.x, 1.0 - inTexCoord.y );
    
    vec2 uv = texCoord;
    gl_Position = g_WorldViewProjectionMatrix * vec4(inPosition, 1.0);
}