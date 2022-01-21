#import "Common/ShaderLib/GLSLCompat.glsllib"
#define TEXENV textureCube
precision mediump float;
uniform samplerCube m_Map;
varying vec2 texCoord;
uniform float m_RotationX;
#define M_PI 3.1415926535897932384626433832795
//varying vec3 dir;

void main()  {
    vec2 uv = texCoord;
    uv.x = mod(uv.x + m_RotationX, 1.0);
    float latitude = uv.y * M_PI;
        float longitude = uv.x * 2. * M_PI - M_PI * 0.5;
        vec3 dir = vec3(
		- sin( longitude ) * sin( latitude ),
		cos( latitude ),
		- cos( longitude ) * sin( latitude )
	);
        //dir = vec3(0.0, 0.0, 1.0);
	dir = normalize( dir );

	gl_FragColor = textureCube(m_Map, dir);
        //gl_FragColor = mix(gl_FragColor, vec4(1.0, 0.0, 0.0, 1.0), 0.5);
        //gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}