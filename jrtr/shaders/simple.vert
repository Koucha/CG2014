#version 150
// GLSL version 1.50 
// Vertex shader for diffuse shading in combination with a texture map

// Uniform variables, passed in from host program via suitable 
// variants of glUniform*
uniform mat4 projection;
uniform mat4 modelview;


// Input vertex attributes; passed in from host program to shader
// via vertex buffer objects
in vec4 position;
in vec4 color;

// Output variables
out vec4 frag_color;

void main()
{
	frag_color = color;
	gl_Position = projection * modelview * position;
}
