#version 150
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

#define MAX_LIGHTS 4

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 modelview;


// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;
in vec2 texcoord;

// Output variables for fragment shader
out vec3 frag_normal;
out vec2 frag_texcoord;
out vec4 frag_viewPosition;

void main()
{
	frag_normal = normalize((transpose(inverse(modelview)) * vec4(normal,0)).xyz);
	
	frag_texcoord = texcoord;
	
	frag_viewPosition = modelview * position;
	gl_Position = projection * modelview * position;
}
