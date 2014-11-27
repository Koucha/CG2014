#version 150
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

uniform sampler2D myTexture;

in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

void main()
{
	frag_shaded = texture(myTexture, frag_texcoord)*0.6;
}

