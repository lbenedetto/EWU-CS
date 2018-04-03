#version 430 core


layout (location = 0) in vec4 position;
out Fragment
{
    vec4 color;
} fragment;

void main(void){
    gl_Position = position;
    fragment.color = vec4(1.0f, 1.0f, 1.0f, 1.0f);
}
