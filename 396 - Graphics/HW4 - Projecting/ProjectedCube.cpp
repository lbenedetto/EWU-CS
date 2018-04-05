#include <GL/glew.h>
#include <GL/freeglut.h>
#include <iostream>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

#define GLM_FORCE_RADIANS 

#include <glm/mat4x4.hpp>
#include <glm/gtc/matrix_transform.hpp> 


GLuint  cube_vao;
GLuint  cube_vbo;
GLuint  cube_ebo;
GLuint loc;
GLuint normal_loc;
GLuint matrix_loc;
GLuint projection_matrix_loc;
GLuint view_matrix_loc;
GLuint program;

//different boolean variables

bool orthographic = true;
bool show_line = false;
bool cullface = false;

static const double kPI = 3.1415926535897932384626433832795;

glm::mat4 view_matrix;
glm::mat4 projection_matrix;
using namespace glm;

float aspect = 0.0;

GLfloat eye[3] = { 0.0f, 0.0f, 2.0f };

GLfloat center[3] = { 0.0f, 0.0f, 0.0f };

GLfloat zNear = 1.5f;
GLfloat zFar = 10.0f;

static const GLfloat cube_vertices[] = { 0.5f, 0.5f, 0.5f, 1.0f,  // v0,v1,v2,v3 (front)
-0.5f, 0.5f, 0.5f, 1.0f,
-0.5f, -0.5f, 0.5f, 1.0f,
0.5f, -0.5f, 0.5f, 1.0f,
0.5f, 0.5f, 0.5f, 1.0f,  // v0,v3,v4,v5 (right)
0.5f, -0.5f, 0.5f, 1.0f,
0.5f, -0.5f, -0.5f, 1.0f,
0.5f, 0.5f, -0.5f, 1.0f,
0.5f, 0.5f, 0.5f, 1.0f,  // v0,v5,v6,v1 (top)
0.5f, 0.5f, -0.5f, 1.0f,
-0.5f, 0.5f, -0.5f, 1.0f,
-0.5f, 0.5f, 0.5f, 1.0f,
-0.5f, 0.5f, 0.5f, 1.0f, // v1,v6,v7,v2 (left)
-0.5f, 0.5f, -0.5f, 1.0f,
-0.5f, -0.5f, -0.5f, 1.0f,
-0.5f, -0.5f, 0.5f, 1.0f,
-0.5f, -0.5f, -0.5f, 1.0f,// v7,v4,v3,v2 (bottom)
0.5f, -0.5f, -0.5f, 1.0f,
0.5f, -0.5f, 0.5f, 1.0f,
-0.5f, -0.5f, 0.5f, 1.0f,
0.5f, -0.5f, -0.5f, 1.0f,// v4,v7,v6,v5 (back)
-0.5f, -0.5f, -0.5f, 1.0f,
-0.5f, 0.5f, -0.5f, 1.0f,
0.5f, 0.5f, -0.5f, 1.0f };


static const GLfloat cube_normals[] = { 0.0f, 0.0f, 1.0f, // v0,v1,v2,v3 (front)
0.0f, 0.0f, 1.0f,
0.0f, 0.0f, 1.0f,
0.0f, 0.0f, 1.0f,
1.0f, 0.0f, 0.0f, // v0,v3,v4,v5 (right)
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
0.0f, 1.0f, 0.0f, // v0,v5,v6,v1 (top)
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
1.0f, 0.0f, 0.0f,// v1,v6,v7,v2 (left)
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
1.0f, 0.0f, 0.0f,
0.0f, 1.0f, 0.0f, // v7,v4,v3,v2 (bottom)
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 1.0f, 0.0f,
0.0f, 0.0f, 1.0f, // v4,v7,v6,v5 (back)
0.0f, 0.0f, 1.0f,
0.0f, 0.0f, 1.0f,
0.0f, 0.0f, 1.0f };


static const GLushort cube_indices[] = { 0, 1, 2, 2, 3, 0,      // front
4, 5, 6, 6, 7, 4,      // right
8, 9, 10, 10, 11, 8,      // top
12, 13, 14, 14, 15, 12,      // left
16, 17, 18, 18, 19, 16,      // bottom
20, 21, 22, 22, 23, 20 };    // back



static const GLchar* ReadFile(const char* filename);
GLuint initShaders(const char* v_shader, const char* f_shader);
void init();
void display(void);
void Reshape(int width, int height);
void keyboard(unsigned char key, int x, int y);

static const GLchar* ReadFile(const char* filename)
{
	FILE* infile;

#ifdef WIN32

	fopen_s(&infile, filename, "rb");

#else
	infile = fopen(filename, "r");
#endif
	if (!infile) {
		std::cerr << "Unable to open file '" << filename << "'" << std::endl;
		return NULL;
	}

	fseek(infile, 0, SEEK_END);
	int len = ftell(infile);
	fseek(infile, 0, SEEK_SET);

	GLchar* source = new GLchar[len + 1];
	fread(source, 1, len, infile);
	fclose(infile);
	source[len] = 0;

	return const_cast<const GLchar*> (source);
}

GLuint initShaders(const char *v_shader, const char *f_shader) {

	GLuint p = glCreateProgram();
	GLuint v = glCreateShader(GL_VERTEX_SHADER);
	GLuint f = glCreateShader(GL_FRAGMENT_SHADER);

	const char * vs = ReadFile(v_shader);
	const char * fs = ReadFile(f_shader);

	glShaderSource(v, 1, &vs, NULL);
	glShaderSource(f, 1, &fs, NULL);
	delete[] vs;
	delete[] fs;

	glCompileShader(v);
	GLint compiled;
	glGetShaderiv(v, GL_COMPILE_STATUS, &compiled);
	if (!compiled) {
		GLsizei len;
		glGetShaderiv(v, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = new GLchar[len + 1];
		glGetShaderInfoLog(v, len, &len, log);
		std::cerr << "Vertex Shader compilation failed: " << log << std::endl;
		delete[] log;


	}

	glCompileShader(f);

	glGetShaderiv(f, GL_COMPILE_STATUS, &compiled);
	if (!compiled) {
		GLsizei len;
		glGetShaderiv(f, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = new GLchar[len + 1];
		glGetShaderInfoLog(f, len, &len, log);
		std::cerr << "Vertex Shader compilation failed: " << log << std::endl;
		delete[] log;
	}

	glAttachShader(p, v);
	glAttachShader(p, f);
	glLinkProgram(p);

	GLint linked;
	glGetProgramiv(p, GL_LINK_STATUS, &linked);
	if (!linked) {
		GLsizei len;
		glGetProgramiv(p, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = new GLchar[len + 1];
		glGetProgramInfoLog(p, len, &len, log);
		std::cerr << "Shader linking failed: " << log << std::endl;
		delete[] log;
	}
	glUseProgram(p);
	return p;

}

void Initialize(void){
	// Create the program for rendering the model

	program = initShaders("shader_projected.vs", "shader_projected.fs");

	// attribute indices

	loc = glGetAttribLocation(program, "position");
	normal_loc = glGetAttribLocation(program, "normal");
	projection_matrix_loc = glGetUniformLocation(program, "projection_matrix");
	
	// uniform indices
	
	view_matrix_loc = glGetUniformLocation(program, "view_matrix");
	matrix_loc = glGetUniformLocation(program, "model_matrix");


	GLuint offset = 0;
	glGenVertexArrays(1, &cube_vao);
	glBindVertexArray(cube_vao);
	glGenBuffers(1, &cube_vbo);

	glBindBuffer(GL_ARRAY_BUFFER, cube_vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(cube_vertices) + sizeof(cube_normals), NULL, GL_STATIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(cube_vertices), cube_vertices);
	offset += sizeof(cube_vertices);
	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(cube_normals), cube_normals);


	glGenBuffers(1, &cube_ebo);

	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cube_ebo);
	glBufferData(GL_ELEMENT_ARRAY_BUFFER, sizeof(cube_indices), cube_indices, GL_STATIC_DRAW);

	glVertexAttribPointer(loc, 4, GL_FLOAT, GL_FALSE, 0, 0);
	glEnableVertexAttribArray(loc);

	glVertexAttribPointer(normal_loc, 3, GL_FLOAT, GL_FALSE, 0, (GLvoid *)sizeof(cube_vertices));
	glEnableVertexAttribArray(normal_loc);

	glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
}


void Display(void)
{
	// Clear
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	// Setup

	glEnable(GL_DEPTH_TEST);
	glDepthFunc(GL_LEQUAL);

	// Choose whether the back face will be enabled or not

	if (cullface)
		glEnable(GL_CULL_FACE);
	else
		glDisable(GL_CULL_FACE);

	// Choose whether to draw in wireframe mode or not

	if (show_line)
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
	else
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);


	// Setup view matrix


	view_matrix = glm::lookAt(glm::vec3(eye[0], eye[1], eye[2]), glm::vec3(center[0], center[1], center[2]), glm::vec3(0.0f, 1.0f, 0.0f)); 

	glUniformMatrix4fv(view_matrix_loc, 1, GL_FALSE, (GLfloat*)&view_matrix[0]);


	// Choose the type of Projection


	if (orthographic)

		projection_matrix = glm::ortho(-1.0f, 1.0f, -1.0f, 1.0f, zNear, zFar);

	else
		projection_matrix = glm::frustum(-1.0f, 1.0f, -1.0f, 1.0f, zNear, zFar);

	glUniformMatrix4fv(projection_matrix_loc, 1, GL_FALSE, (GLfloat*)&projection_matrix[0]);
	glm::mat4 model_matrix;
	glUniformMatrix4fv(matrix_loc, 1, GL_FALSE, (GLfloat*)&model_matrix[0]);


	glBindVertexArray(cube_vao);
	glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, cube_ebo);

	glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_SHORT, NULL);

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

	case 'o':
		orthographic = !orthographic;
		break;
	case 's':
		show_line = !show_line;
		break;
	case 'c':
		cullface = !cullface;
		break;
	
	case 'i':
		// implement isometric view
		break;
	case 'd':
		//implement dimetric view
		break;
	case 't':
		// implement trimetric view
		break;
	case '1':
		//implement one-point perspective 
		break;
	case '2':
		//implement two-point perspective
		break;
	case '3':
		// implement three point perspective
		break;

	}
	glutPostRedisplay();
}

/*********/
int main(int argc, char** argv){

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA);
	glutInitWindowSize(512, 512);

	glutCreateWindow("ProjectedCube");

	if (glewInit()){
		std::cerr << "Unable to initialize GLEW ... exiting" << std::endl;
	}

	Initialize();
	std::cout << glGetString(GL_VERSION) << std::endl;
	glutDisplayFunc(Display);
	glutKeyboardFunc(keyboard);
	glutReshapeFunc(Reshape);
	glutMainLoop();
	return 0;
}




/*************/



