#version 150
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

#define MAX_LIGHTS 4

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 modelview;

uniform vec4 lightDirection[MAX_LIGHTS];
uniform vec4 lightPosition[MAX_LIGHTS];
uniform int lightType[MAX_LIGHTS];	//0: directional, 1: point
uniform int nLights;

// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;

// Output variables for fragment shader
out vec4 frag_normal;
out vec2 frag_texcoord;
out vec4 lightDir[MAX_LIGHTS];

void main()
{
	frag_normal = modelview * vec4(normal,0);
	
	frag_texcoord = texcoord;
	
	for(int i = 0; i < MAX_LIGHTS; i++)
	{
		if(lightType[i] == 0)
			lightDir[i] = lightDirection[i];
		else if(lightType[i] == 1)
			lightDir[i] = lightPosition[i] - position;
	}
	
	gl_Position = projection * modelview * position;
}
