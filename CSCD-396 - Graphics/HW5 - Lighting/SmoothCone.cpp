#include <GL/glew.h>
#include <GL/freeglut.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define GLM_FORCE_RADIANS 

#include <glm/mat4x4.hpp> // glm::mat4
#include <glm/gtc/matrix_transform.hpp> // glm::translate, glm::rotate, glm::scale, glm::perspective
using namespace glm;

#define NumConePoints  18
#define NumTriangles   18
#define NumIndices     3*NumTriangles

GLuint  cone_vao;
GLuint  cone_vbo;
GLuint  cone_ebo;

GLuint position_loc;
GLuint normal_loc;

mat4 view;
mat4 projection;

GLuint program;

float aspect = 0.0;
bool show_line = false;


GLuint indices[NumIndices];
vec4 points[NumConePoints + 1];
vec3 normals[NumConePoints + 1];

static const double kPI = 3.1415926535897932384626433832795;
int index = 0;



static const GLchar* ReadFile(const char* filename);
GLuint initShaders(const char* v_shader, const char* f_shader);
void Initialize();
void Display(void);
void Reshape(int width, int height);
void initializeCone();
void updateVertexNormals();


void initializeCone(){

	points[index][0] = 0.0;
	points[index][1] = 1.0;
	points[index][2] = 0.0;
	points[index][3] = 1.0;

	normals[index][0] = 0.0;
	normals[index][1] = 0.0;
	normals[index][2] = 0.0;

	index++;

	int i;
	float theta;
	int tIndices = 0;


	for (i = 0; i < NumConePoints; ++i) {

		theta = i*20.0f*kPI / 180.0f;

		points[index][0] = cos(theta);
		points[index][1] = -1.0;
		points[index][2] = -sin(theta);
		points[index][3] = 1.0;

		normals[index][0] = 0.0;
		normals[index][1] = 0.0;
		normals[index][2] = 0.0;

		if (i <= (NumConePoints - 2)){

			indices[tIndices] = 0; tIndices++;
			indices[tIndices] = index; tIndices++;
			indices[tIndices] = index + 1; tIndices++;

		}
		else{
			indices[tIndices] = 0; tIndices++;
			indices[tIndices] = index; tIndices++;
			indices[tIndices] = 1; tIndices++;
		}
		index++;
	}

	updateVertexNormals();

}

void updateVertexNormals(){}


static const GLchar* ReadFile(const char* filename)
{
	FILE* infile;

#ifdef WIN32

	fopen_s(&infile, filename, "rb");

#else
	infile = fopen(filename, "r");
#endif
	if (!infile) {
		printf("Unable to open file %s\n", filename);
		return NULL;
	}

	fseek(infile, 0, SEEK_END);
	int len = ftell(infile);
	fseek(infile, 0, SEEK_SET);

	GLchar* source = (GLchar*)malloc(sizeof(GLchar)*(len + 1));
	fread(source, 1, len, infile);
	fclose(infile);
	source[len] = 0;

	return (GLchar*)(source);
}

GLuint initShaders(const char *v_shader, const char *f_shader) {

	GLuint p = glCreateProgram();
	GLuint v = glCreateShader(GL_VERTEX_SHADER);
	GLuint f = glCreateShader(GL_FRAGMENT_SHADER);

	const char * vs = ReadFile(v_shader);
	const char * fs = ReadFile(f_shader);

	glShaderSource(v, 1, &vs, NULL);
	glShaderSource(f, 1, &fs, NULL);
	free ((char*)vs);
	free ((char*) fs);

	glCompileShader(v);
	GLint compiled;
	glGetShaderiv(v, GL_COMPILE_STATUS, &compiled);
	if (!compiled) {
		GLsizei len;
		glGetShaderiv(v, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = (GLchar*)malloc(sizeof(GLchar)*(len + 1));
		glGetShaderInfoLog(v, len, &len, log);
		printf("Vertex Shader compilation failed: %s\n", log);
		free ((GLchar*)log);


	}

	glCompileShader(f);

	glGetShaderiv(f, GL_COMPILE_STATUS, &compiled);
	if (!compiled) {
		GLsizei len;
		glGetShaderiv(f, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = (GLchar*)malloc(sizeof(GLchar)*(len + 1));
		glGetShaderInfoLog(f, len, &len, log);
		printf("Fragment Shader compilation failed: %s\n", log);
		free((GLchar*) log);
	}

	glAttachShader(p, v);
	glAttachShader(p, f);
	glLinkProgram(p);

	GLint linked;
	glGetProgramiv(p, GL_LINK_STATUS, &linked);
	if (!linked) {
		GLsizei len;
		glGetProgramiv(p, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = (GLchar*) malloc(sizeof(GLchar)*(len + 1));
		glGetProgramInfoLog(p, len, &len, log);
		printf("Shader linking failed: %s\n",  log);
		free ((char*)log);
	}
	glUseProgram(p);
	return p;

}

void Initialize(void){
	// Create the program for rendering the model

	initializeCone();

	GLuint offset = 0;
	glGenVertexArrays(1, &cone_vao);
	glBindVertexArray(cone_vao);
	glGenBuffers(1, &cone_vbo);

	glBindBuffer(GL_ARRAY_BUFFER, cone_vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(points) + sizeof(normals), NULL, GL_STATIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(points), points);
	offset += sizeof(points);
	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(normals), normals);

	glGenBuffers(1, &cone_ebo);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cone_ebo);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(indices), (indices), GL_STATIC_DRAW);


	program = initShaders("smoothshader.vert", "smoothshader.frag");

	// attribute indices

	position_loc = glGetAttribLocation(program, "VertexPosition");
	glVertexAttribPointer(position_loc, 4, GL_FLOAT, GL_FALSE, 0, 0);
	glEnableVertexAttribArray(position_loc);

	normal_loc = glGetAttribLocation(program, "VertexNormal");
	glVertexAttribPointer(normal_loc, 3, GL_FLOAT, GL_FALSE, 0, (GLvoid *)sizeof(points));
	glEnableVertexAttribArray(normal_loc);

	
	view = glm::lookAt(vec3(0.0f, 0.0f, 5.0f), vec3(0.0f, 0.0f, 0.0f), vec3(0.0f, 1.0f, 0.0f));
	
	// Initialize shader material and lighting parameters

	vec3 material_ambient(0.9, 0.5, 0.3);
	vec3 material_diffuse(0.9, 0.5, 0.3);
	vec3 material_specular(0.8, 0.8, 0.8);
	

	vec3 light_ambient(0.4, 0.4, 0.4);
	vec3 light_diffuse(1.0, 1.0, 1.0);
	vec3 light_specular(1.0, 1.0, 1.0);

	float material_shininess = 150.0f;

	glUniform3fv(glGetUniformLocation(program, "Material.Ka"), 1, (GLfloat*)&material_ambient);
	glUniform3fv(glGetUniformLocation(program, "Light.La"), 1, (GLfloat*)&light_ambient);
	
	// Set other lighting parameters here

	
	projection = mat4(1.0f);
	
	
	glEnable(GL_DEPTH_TEST);

	glClearColor(0.0, 0.0, 0.0, 1.0);

}

void Display(void)
{
	// Clear
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Choose whether to draw in wireframe mode or not

	if (show_line)
	glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	else
	glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

	vec4 lightpos = view*vec4(0.0f, 0.0f, 2.0f, 1.0f);
	glUniform4fv(glGetUniformLocation(program, "Light.Position"), 1, (GLfloat*)&lightpos);

	
	// Setup matrices

	mat4 model = glm::translate(glm::mat4(1.0f), vec3(0.0f, 0.0f, -1.0f));
	projection = glm::perspective(70.0f, aspect, 0.3f, 100.0f);
	mat4 mvp = projection*view*model;


	glUniformMatrix4fv(glGetUniformLocation(program, "MVP"), 1, GL_FALSE, (GLfloat*)&mvp[0]);
	glUniformMatrix4fv(glGetUniformLocation(program, "ProjectionMatrix"), 1, GL_FALSE, (GLfloat*)&projection[0]);

	// You need to add normal matrix and model view matrix

	glBindVertexArray(cone_vao);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cone_ebo);


	glDrawElements(GL_TRIANGLES, NumIndices, GL_UNSIGNED_INT, NULL);
	glutSwapBuffers();
}

void Reshape(int width, int height)
{
	glViewport(0, 0, width, height);

	aspect = float(width) / float(height);
}


void keyboard(unsigned char key, int x, int y){
	switch (key){
	case 'q':case 'Q':
		exit(EXIT_SUCCESS);
		break;

	case 's':
		show_line = !show_line;
		break;
	}
	glutPostRedisplay();
}

/*********/
int main(int argc, char** argv){

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA);
	glutInitWindowSize(512, 512);

	glutCreateWindow("SmoothCone");

	if (glewInit()){
		printf("Unable to initialize GLEW ... exiting\n");
	}

	Initialize();
	printf("%s\n",glGetString(GL_VERSION));
	glutDisplayFunc(Display);
	glutKeyboardFunc(keyboard);
	glutReshapeFunc(Reshape);
	glutMainLoop();
	return 0;
}




/*************/
