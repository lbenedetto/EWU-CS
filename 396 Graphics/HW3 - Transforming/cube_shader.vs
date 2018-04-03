#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec3 normal;
uniform vec4 color;
uniform mat4 model_matrix;


out Fragment
{
    vec3 normal;
    vec4 color;
} fragment;

void main(void)
{
    gl_Position = (model_matrix*position)*vec4(0.15, 0.15, 0.15, 1.0);
fragment.normal = mat3(model_matrix)*normal;
fragment.color = color;

}

