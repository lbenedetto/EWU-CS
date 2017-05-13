#version 430 core

layout (location = 0) in vec4 position;
layout (location = 1) in vec3 normal;

uniform mat4 model_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;


out Fragment
{
    vec3 normal;
    
} fragment;

void main(void)
{
    gl_Position = (projection_matrix*view_matrix*model_matrix*position);
fragment.normal = mat3(model_matrix)*normal;

}
