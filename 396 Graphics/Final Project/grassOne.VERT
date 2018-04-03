#version 430 core
precision highp float;

layout (location = 0) in vec4 vVertex;
layout (location = 1) in vec3 vNormal;
uniform mat4 view_matrix;
uniform mat4 projection_matrix;
uniform mat4 model_matrix;
uniform vec4 AmbientProduct, DiffuseProduct, SpecularProduct;
uniform vec4 LightPosition;
uniform float Shininess;


out vec4 fColor;

int random(int seed, int iterations)
{
    int value = seed;
    int n;
    for (n = 0; n < iterations; n++) {
        value = ((value >> 7) ^ (value << 9)) * 15485863;
    }
    return value;
}
vec4 random_vector(int seed){

    int r = random(gl_InstanceID, 4);
    int g = random(r, 2);
    int b = random(g, 2);
    int a = random(b, 2);

    return vec4(float(r & 0x3FF) / 1024.0,
                float(g & 0x3FF) / 1024.0,
                float(b & 0x3FF) / 1024.0,
                float(a & 0x3FF) / 1024.0);
}

mat4 construct_rotation_matrix(float angle)
{
    float st = sin(angle);
    float ct = cos(angle);

    return mat4(vec4(ct, 0.0, st, 0.0),
                vec4(0.0, 1.0, 0.0, 0.0),
                vec4(-st, 0.0, ct, 0.0),
                vec4(0.0, 0.0, 0.0, 1.0));
}

void main(void){

// Normalize the input lighting vectors

   vec3 N = normalize(vNormal);
   vec3 E = normalize(vVertex.xyz);
   vec3 L = normalize(LightPosition.xyz);

   vec3 H = normalize(L+E);

vec4 ambient = AmbientProduct;
float Kd = max(dot(L,N), 0.0);
vec4 diffuse = Kd*DiffuseProduct;

float Ks = pow(max(dot(N, H), 0.0), Shininess);
vec4 specular = Ks*SpecularProduct;

//discard the specular highlight if the light is behind the vertex

if (dot(L, N) < 0.0){
    specular = vec4 (0.0, 0.0, 0.0, 1.0);
}
fColor = ambient + diffuse + specular;

fColor.a = 1.0;



vec4 offset = vec4(float(gl_InstanceID >> 10) - 512.0,0.0f,
                   float(gl_InstanceID & 0x3FF)- 512.0, 0.0f);
int number1 = random(gl_InstanceID, 3);
int number2 = random(number1, 2);
offset += vec4(float(number1 & 0xFF) / 1024.0, 0.0f,
                   float(number2 & 0xFF) / 1024.0, 0.0f);
     
float angle = float(random(number2, 2) & 0x3FF) / 1024.0;
vec2 coord = offset.xz / 1024.0 + vec2(0.5);

float bend_factor = coord.x;
float bend_amount = cos(vVertex.y);

  
    mat4 rot = construct_rotation_matrix(angle);

vec4 position = (rot * (vVertex + vec4(0.0, 0.0, bend_amount * bend_factor, 0.0))) + offset;

    gl_Position = projection_matrix*view_matrix*model_matrix * position;
 
      
}

