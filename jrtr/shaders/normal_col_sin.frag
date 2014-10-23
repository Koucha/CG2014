#version 150

#define M_PI 3.1415926535897932384626433832795


// Fragment shader shades with 0.2 offset and sinoid dependency on normal.z

in vec4 frag_normal;
in vec4 frag_color;

out vec4 frag_shaded;

void main()
{
	float normz = normalize(frag_normal).z;	
	frag_shaded = (0.1*normz + 0.9*(asin(normz)/M_PI + 0.5)) * frag_color;
}
