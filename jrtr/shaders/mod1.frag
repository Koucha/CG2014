#version 150
// GLSL version 1.50
// Fragment shader for diffuse shading in combination with a texture map

#pragma optionNV (unroll all)

uniform float ix;
uniform float iy;

// Variables passed in from the vertex shader
in vec2 frag_texcoord;

// Output variable, will be written to framebuffer automatically
out vec4 frag_shaded;

vec4 color(float a);

void main()
{
    vec2 z;
    z.x = 4.0 * (frag_texcoord.x - 0.5);
    z.y = 4.0 * (frag_texcoord.y - 0.5);

    int i;
    float x = (z.x * z.x - z.y * z.y) + ix;
    float y = (z.y * z.x + z.x * z.y) + iy;
    for(i=0; i<10000; i++)
    {
        if((x * x + y * y) > 4.0) break;
        z.x = x;
        z.y = y;
        
        x = (z.x * z.x - z.y * z.y) + ix;
        y = (z.y * z.x + z.x * z.y) + iy;
    }

    frag_shaded = color((i == 10000 ? 0.0 : float(i%101)) / 100.0);
}

vec4 color(float a)
{
	if(a > 0.5)
	{
		a = 1 - a;
	}
	
	vec4 col = vec4(0,0,0,0);
	
	float si = sin((2*a)*3.1415926);
	
	col.x = si;
	col.y = si*si;
	
	return col;
}