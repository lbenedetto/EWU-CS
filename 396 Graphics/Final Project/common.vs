#version 430 core

layout (location = 0) in vec4 vVertex;
layout (location = 1) in vec3 vNormal;
uniform mat4 model_matrix;
uniform mat4 projection_matrix;
uniform mat4 view_matrix;

uniform vec4 AmbientProduct, DiffuseProduct, SpecularProduct;
uniform vec4 LightPosition;
uniform float Shininess;

out vec4 fColor;


void main(void)
{


vec4 ambient = AmbientProduct;

// you need to complete the code for diffuse and specular illumination models

fColor = ambient;
fColor.a = 1.0;


gl_Position = (projection_matrix*view_matrix*model_matrix*vVertex);


}
