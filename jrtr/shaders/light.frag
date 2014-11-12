#version 150
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

uniform vec3 mat_drc;

// Variables passed in from the vertex shader
in vec4 frag_normal;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{
	frag_shaded = vec4(mat_drc*(sin(max(0.1+0.9*frag_normal.z, 0))), 0.5);
}

