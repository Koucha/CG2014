#version 150
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

#define MAX_LIGHTS 4

// Uniform variables passed in from host program
uniform sampler2D myTexture;

uniform vec3 lightColour[MAX_LIGHTS];
uniform int nLights;

uniform vec3 mat_drc;

// Variables passed in from the vertex shader
in vec4 frag_normal;
in vec2 frag_texcoord;
in vec4 lightDir[MAX_LIGHTS];

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{
	float ndot = 0;
	frag_shaded = vec4(0,0,0,0);
	
	for(int i = 0; i < nLights; i++)
	{
		ndot = max(dot(normalize(frag_normal), normalize(lightDir[i])), 0);
		
		frag_shaded = frag_shaded + (ndot * vec4(lightColour[i], 0) * vec4(mat_drc,0));
	}
	//frag_shaded = max(dot(frag_normal, lightDirection[0]), 0) * vec4(1,0,0,0);
}

