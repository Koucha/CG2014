#version 150
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

#define MAX_LIGHTS 4

// Uniform variables passed in from host program
uniform sampler2D myTexture;

uniform vec3 lightDiffuse[MAX_LIGHTS];
uniform vec3 lightAmbient[MAX_LIGHTS];
uniform vec4 lightDirection[MAX_LIGHTS];
uniform vec4 lightPosition[MAX_LIGHTS];
uniform int lightType[MAX_LIGHTS];	//0: directional, 1: point
uniform int nLights;

uniform vec3 mat_drc;
uniform vec3 mat_src;
uniform vec3 mat_arc;
uniform float shininess;

// Variables passed in from the vertex shader
in vec3 frag_normal;
in vec4 frag_viewPosition;
in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{
	vec3 suma = vec3(0,0,0);
	vec3 sumb = vec3(0,0,0);
	vec3 normal = normalize(frag_normal);
	
	for(int i = 0; i < nLights; i++)
	{
		vec4 L = -lightDirection[i];
		vec3 rad = lightDiffuse[i];
		
		if(lightType[i] == 1)
		{
			L = lightPosition[i] - frag_viewPosition;
			float dist = length(L);
			rad = 100*rad/(dist*dist);
		}
		
		L = normalize(L);
		vec4 e = -normalize(frag_viewPosition);
		vec4 R = -reflect(L, vec4(normal, 0));
		
		float ndot = max(dot(vec4(normal, 0), L), 0);
		float rdote = max(0, dot(normalize(R), e));
		
		vec3 diffuseTerm = mat_drc * ndot;
		vec3 specularTerm = mat_src * pow(rdote, shininess);
		vec3 ambientTerm = mat_arc * lightAmbient[i];
		suma = suma + rad * diffuseTerm + ambientTerm;
		sumb = sumb + rad * specularTerm;
		
	}
	frag_shaded = vec4(suma, 0) /*** texture(myTexture, frag_texcoord)*/ + vec4(sumb, 0);
}

