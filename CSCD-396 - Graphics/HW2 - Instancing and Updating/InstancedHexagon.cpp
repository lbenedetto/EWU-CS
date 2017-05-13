#include <GL/glew.h>
#include <GL/freeglut.h>
#include <stdio.h>
#include <stdlib.h>
#include <math.h>

GLuint render_prog;
GLuint              hex_vao;
GLuint              hex_vbo;

typedef struct {
	GLenum       type;
	const char*  filename;
	GLuint       shader;
} ShaderInfo;


// position of the vertices in the hexagon

GLfloat hex_vertices[] = {

	0.0, 0.0, 0.0, 1.0,             // index '0'
	-0.1125, -0.1875, 0.0, 1.0,     // index '1'
	0.1125, -0.1875, 0.0, 1.0,      // index '2'

	0.0, 0.0, 0.0, 1.0,             // index '0'
	0.1125, -0.1875, 0.0, 1.0,      // index '2'
	0.225, 0.0, 0.0, 1.0,           // index '3'

	0.0, 0.0, 0.0, 1.0,             // index '0'
	0.225, 0.0, 0.0, 1.0,           // index '3'
	0.1125, 0.1875, 0.0, 1.0,       // index '4'

	0.0, 0.0, 0.0, 1.0,             // index '0'
	0.1125, 0.1875, 0.0, 1.0,       // index '4'
	-0.1125, 0.1875, 0.0, 1.0,      // index '5'

	0.0, 0.0, 0.0, 1.0,             // index '0'
	-0.1125, 0.1875, 0.0, 1.0,      // index '5'
	-0.225, 0.0, 0.0, 1.0,          // index '6'

	0.0, 0.0, 0.0, 1.0,             // index '0'
	-0.1125, -0.1875, 0.0, 1.0,     // index '6'
	-0.225, 0.0, 0.0, 1.0           // index '1'
};


GLfloat hex_vertices_update[] = {
	0.0, 0.0, 0.0, 1.0,              // index '0'
	-0.1125, -0.1875, 0.0, 1.0,      // index '1'
	0.1125, -0.1875, 0.0, 1.0,       // index '2'
	0.1125, 0.0, 0.0, 1.0,           // index '3'
	0.1125, 0.1875, 0.0, 1.0,        // index '4'
	-0.1125, 0.1875, 0.0, 1.0,       // index '5'
	-0.1125, 0.0, 0.0, 1.0           // index '6'
};

// color of the vertices

GLfloat hex_colors[] = {
	1.0f, 1.0f, 1.0f, 1.0f,
	1.0f, 0.0f, 0.0f, 1.0f,
	0.0f, 1.0f, 0.0f, 1.0f,
	0.0f, 0.0f, 1.0f, 1.0f,
	1.0f, 1.0f, 0.0f, 1.0f,
	0.0f, 1.0f, 1.0f, 1.0f,
	0.5f, 0.5f, 0.5f, 1.0f
};

// position of the hexagon
GLfloat hex_positions[] =
{
	0.0f, 0.0f, 0.0f, 1.0f,
	-0.40f, -0.60f, 0.0f, 1.0f,
	0.40f, -0.60f, 0.0f, 1.0f,
	0.40f, 0.60f, 0.0f, 1.0f,
	-0.40f, 0.60f, 0.0f, 1.0f,
	-0.90f, 0.0f, 0.0f, 1.0f,
	0.90f, 0.0f, 0.0f, 1.0f
};


void Initialize(void);
void Display(void);

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
//-----------------------------------------------------------------------------

GLuint LoadShaders(ShaderInfo* shaders)
{
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

/***********************************************************************************/
void Initialize(void)
{
	// Create the program for rendering the model

	ShaderInfo shaders[] = { { GL_VERTEX_SHADER, "instancing.vs" },
	{ GL_FRAGMENT_SHADER, "instancing.fs" },
	{ GL_NONE, NULL }
	};

	render_prog = LoadShaders(shaders);
	glUseProgram(render_prog);
	int position_loc = glGetAttribLocation(render_prog, "position");
	
	glGenVertexArrays(1, &hex_vao);
	glGenBuffers(1, &hex_vbo);
	glBindVertexArray(hex_vao);
	glBindBuffer(GL_ARRAY_BUFFER, hex_vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(hex_vertices), hex_vertices, GL_STATIC_DRAW);
	
	glVertexAttribPointer(position_loc, 4, GL_FLOAT, GL_FALSE, 0, 0);
	glEnableVertexAttribArray(position_loc);

	glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
}

/**************************************************************************************/
void keyboard(unsigned char key, int x, int y) {

	switch (key) {
	case 'q':case 'Q':
		exit(EXIT_SUCCESS);
		break;
	
	}
	glutPostRedisplay();
}
/**************************************************************************************/

void Display(void)
{
	glClear(GL_COLOR_BUFFER_BIT);

	glBindVertexArray(hex_vao);
	glDrawArrays(GL_TRIANGLES, 0, 18);

	glutSwapBuffers();
}

/*******************************************/
int main(int argc, char** argv) {

	glutInit(&argc, argv);
	glutInitDisplayMode(GLUT_RGBA);
	glutInitWindowSize(1000, 1000);
	glutInitWindowPosition(0, 0);

	glutCreateWindow("Instanced Hexagon");

	if (glewInit()) {
		printf("Unable to initialize GLEW ... exiting\n");
	}

	Initialize();
	printf("%s\n", glGetString(GL_VERSION));
	glutDisplayFunc(Display);
	glutKeyboardFunc(keyboard);
	glutMainLoop();
}

/**********************************************/



