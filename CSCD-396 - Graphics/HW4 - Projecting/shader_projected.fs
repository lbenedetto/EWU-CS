#version 430 core


in Fragment
{
    vec3 normal;
    
} fragment;

out vec4 color;

void main(void)
{
color = 0.5*vec4(fragment.normal, 1.0);

}
