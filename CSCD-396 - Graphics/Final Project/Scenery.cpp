#include <GL/glew.h>
#include <GL/freeglut.h>

#include <stdlib.h>
#include <iostream>
#include <math.h>

#include "grass.h"
#include "ground.h"

#define GLM_FORCE_RADIANS 

#include <glm/mat4x4.hpp>
#include <glm/gtc/matrix_transform.hpp> 

using namespace glm;

void Initialize(void);
void Display(void);

GLuint prog;
GLuint render_prog;

typedef struct {
	GLenum       type;
	const char*  filename;
	GLuint       shader;
} ShaderInfo;


float angle = 0.0;

vec4 light_position(20.0, 40.0, 60.0, 0.0);  // directional light source

float material_shininess = 50.0;

vec4 ambient_product;
vec4 diffuse_product;
vec4 specular_product;

mat4 view_matrix;
mat4 projection_matrix;
mat4 model_matrix;
GLuint model_matrix_loc;
GLuint view_matrix_loc;
GLuint projection_matrix_loc;
float aspect;
//----------------------------------------------------------------------------

const GLchar* ReadShader(const char* filename) {
#ifdef WIN32
	FILE* infile;
	fopen_s(&infile, filename, "rb");

#else
	FILE* infile = fopen(filename, "rb");
#endif // WIN32

	if (!infile) {
#ifdef _DEBUG
		printf("Unable to open file %s", filename);
#endif /* DEBUG */
		return NULL;
	}

	fseek(infile, 0, SEEK_END);
	int len = ftell(infile);
	fseek(infile, 0, SEEK_SET);

	GLchar* source = (GLchar*)malloc(sizeof(GLchar)*(len + 1));

	fread(source, 1, len, infile);
	fclose(infile);

	source[len] = 0;

	return ((GLchar*)source);
}

//----------------------------------------------------------------------------

GLuint LoadShaders(ShaderInfo* shaders){
	if (shaders == NULL) { return 0; }

	GLuint program = glCreateProgram();

	ShaderInfo* entry = shaders;
	while (entry->type != GL_NONE) {
		GLuint shader = glCreateShader(entry->type);

		entry->shader = shader;

		const GLchar* source = ReadShader(entry->filename);

		if (source == NULL) {


			for (entry = shaders; entry->type != GL_NONE; ++entry) {
				glDeleteShader(entry->shader);
				entry->shader = 0;
			}

			return 0;
		}

		glShaderSource(shader, 1, &source, NULL);
		free((GLchar*)source);

		glCompileShader(shader);

		GLint compiled;
		glGetShaderiv(shader, GL_COMPILE_STATUS, &compiled);
		if (!compiled) {
#ifdef _DEBUG
			GLsizei len;
			glGetShaderiv(shader, GL_INFO_LOG_LENGTH, &len);

			GLchar* log = (GLchar*)malloc(sizeof(GLchar)*(len + 1));
			glGetShaderInfoLog(shader, len, &len, log);
			printf("Shader compilation failed.\n");
			free(log);
#endif /* DEBUG */

			return 0;
		}

		glAttachShader(program, shader);

		++entry;
	}

	glLinkProgram(program);

	GLint linked;
	glGetProgramiv(program, GL_LINK_STATUS, &linked);
	if (!linked) {
#ifdef _DEBUG
		GLsizei len;
		glGetProgramiv(program, GL_INFO_LOG_LENGTH, &len);

		GLchar* log = (GLchar*)malloc(sizeof(GLchar)*(len + 1));
		glGetProgramInfoLog(program, len, &len, log);
		printf("Shader linking failed: %s\n", log);
		free(log);
#endif /* DEBUG */

		for (entry = shaders; entry->type != GL_NONE; ++entry) {
			glDeleteShader(entry->shader);
			entry->shader = 0;
		}

		return 0;
	}

	return program;
}
////////////////////////////////////////////////////////////////////////////////////////
/********************************************************************************/

void Reshape(int width, int height) {

	glViewport(0, 0, width, height);
	aspect = float(width) / float(height);
}
/*****************************************************************************/
////////////////////////////////////////////////////////////////////////////////////////


void Initialize(void)
{
	// Create the program for rendering the model
	
	// Initialize shader lighting parameters

	
	ShaderInfo shader[] = { { GL_VERTEX_SHADER, "grassOne.VERT" },
	{ GL_FRAGMENT_SHADER, "grassOne.FRAG" },
	{ GL_NONE, NULL }
	};
	
	render_prog = LoadShaders(shader);
	glUseProgram(render_prog);
	
	createGrass();


	ShaderInfo common_shaders[] = { { GL_VERTEX_SHADER, "common.vs" },
	                                { GL_FRAGMENT_SHADER, "common.fs" },
	                                { GL_NONE, NULL } };

	prog = LoadShaders(common_shaders);

	
	glUseProgram(prog);


	createGround();
	
	glClearColor(0.5f, 0.75f, 0.85f, 1.0f);  // sky color
}

/**************************************************************************************************************/
void Display(void){

	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	glEnable(GL_DEPTH_TEST);

	glUseProgram(render_prog);

	vec4 light_ambient(0.5, 0.5, 1.0, 1.0);
	vec4 light_diffuse(0.5, 0.5, 1.0, 1.0);
	vec4 light_specular(1.0, 1.0, 1.0, 1.0);

	vec4 material_ambient(0.1, 1.0, 0.1, 1.0);
	vec4 material_diffuse(0.1, 1.0, 0.1, 1.0);
	vec4 material_specular(1.0, 1.0, 1.0, 1.0);

	ambient_product = light_ambient*material_ambient;
	diffuse_product = light_diffuse*material_diffuse;
	specular_product = light_specular*material_specular;

	glUniform4fv(glGetUniformLocation(render_prog, "AmbientProduct"), 1, (GLfloat*)&ambient_product[0]);
	glUniform4fv(glGetUniformLocation(render_prog, "DiffuseProduct"), 1, (GLfloat*)&diffuse_product[0]);
	glUniform4fv(glGetUniformLocation(render_prog, "SpecularProduct"), 1, (GLfloat*)&specular_product[0]);

	glUniform4fv(glGetUniformLocation(render_prog, "LightPosition"), 1, (GLfloat*)&light_position[0]);
	glUniform1f(glGetUniformLocation(render_prog, "Shininess"), material_shininess);

	view_matrix_loc = glGetUniformLocation(render_prog, "view_matrix");
	projection_matrix_loc = glGetUniformLocation(render_prog, "projection_matrix");
	model_matrix_loc = glGetUniformLocation(render_prog, "model_matrix");
	view_matrix = glm::lookAt(glm::vec3(0.0f, 40.0f, 200.0f), glm::vec3(0.0f, 0.0f, 0.0f), glm::vec3(0.0f, 1.0f, 0.0f));

	glUniformMatrix4fv(view_matrix_loc, 1, GL_FALSE, (GLfloat*)&view_matrix[0]);
	projection_matrix = perspective(radians(60.0f), aspect, 1.0f, 3000.0f);
	model_matrix = rotate(mat4(1.0f), radians(0.0f), vec3(0.0f, 1.0f, 0.0f));

	glUniformMatrix4fv(model_matrix_loc, 1, GL_FALSE, (GLfloat*)&model_matrix[0]);
	glUniformMatrix4fv(projection_matrix_loc, 1, GL_FALSE, (GLfloat*)&projection_matrix[0]);

	renderGrass();
	
	glUseProgram(prog);

	material_ambient = vec4(0.25, 0.25, 0.25, 1.0);
	material_diffuse = vec4(0.25, 0.25, 0.25, 1.0);
	material_specular = vec4(0.25, 0.25, 0.25, 1.0);

	ambient_product = light_ambient*material_ambient;
	diffuse_product = light_diffuse*material_diffuse;
	specular_product = light_specular*material_specular;
	glUniform4fv(glGetUniformLocation(prog, "AmbientProduct"), 1, (GLfloat*)&ambient_product[0]);
	glUniform4fv(glGetUniformLocation(prog, "DiffuseProduct"), 1, (GLfloat*)&diffuse_product[0]);
	glUniform4fv(glGetUniformLocation(prog, "SpecularProduct"), 1, (GLfloat*)&specular_product[0]);

	glUniform4fv(glGetUniformLocation(prog, "LightPosition"), 1, (GLfloat*)&light_position[0]);
	glUniform1f(glGetUniformLocation(prog, "Shininess"), material_shininess);

	view_matrix_loc = glGetUniformLocation(prog, "view_matrix");
	projection_matrix_loc = glGetUniformLocation(prog, "projection_matrix");
	model_matrix_loc = glGetUniformLocation(prog, "model_matrix");
	view_matrix = glm::lookAt(glm::vec3(0.0f, 40.0f, 200.0f), glm::vec3(0.0f, 0.0f, 0.0f), glm::vec3(0.0f, 1.0f, 0.0f));

	projection_matrix = perspective(radians(60.0f), aspect, 1.0f, 3000.0f);

	glUniformMatrix4fv(view_matrix_loc, 1, GL_FALSE, (GLfloat*)&view_matrix[0]);
	glUniformMatrix4fv(model_matrix_loc, 1, GL_FALSE, (GLfloat*)&model_matrix[0]);
	glUniformMatrix4fv(projection_matrix_loc, 1, GL_FALSE, (GLfloat*)&projection_matrix[0]);
	
	
	

	renderGround();

	glutSwapBuffers();

}
/**************************************************************************************************************/

/*************************************************************************/
void Rotate(int n)  //the "glutTimerFunc"
{
	// implement this function!

}

/************************************************************************/
int main(int argc, char** argv) {

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA);
	glutInitWindowSize(1024, 1024);

	glutCreateWindow("Scenery");

	if (glewInit()) {
		printf("Unable to initialize GLEW ... exiting\n");
	}

	Initialize();
	printf("%s\n", glGetString(GL_VERSION));
	glutDisplayFunc(Display);
	glutReshapeFunc(Reshape);
	glutTimerFunc(100, Rotate, 0);
	glutMainLoop();
}

/*****/



/*************/