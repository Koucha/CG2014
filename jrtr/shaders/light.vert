#version 150
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 modelview;

// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec3 normal;
in vec4 position;

// Output variables for fragment shader
out vec4 frag_normal;

void main()
{
	frag_normal = modelview * vec4(normal,0);
	
	gl_Position = projection * modelview * position;
}
