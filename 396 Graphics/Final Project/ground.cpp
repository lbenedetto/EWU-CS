#include "ground.h"

GLuint ground_vao;
GLuint ground_vbo;
GLuint ground_ebo;

void createGround() {

	static const GLfloat ground_vertices[] = { -512.0f, 0.0f, 512.0f, 1.0f,// v0,v1,v2,v3 (top)
		512.0f, 0.0f, 512.0f, 1.0f,
		512.0f, 0.0f, -512.0f, 1.0f,
		-512.0f, 0.0f, -512.0f, 1.0f
	};

	static const GLfloat ground_normals[] =
	{ 0.0f, 1.0f, 0.0f,
	  0.0f, 1.0f, 0.0f,
	  0.0f, 1.0f, 0.0f,
	  0.0f, 1.0f, 0.0f,
	};

	glGenVertexArrays(1, &ground_vao);

	GLuint offset = 0;
	glGenBuffers(1, &ground_vbo);
	glBindVertexArray(ground_vao);

	glBindBuffer(GL_ARRAY_BUFFER, ground_vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(ground_vertices) + sizeof(ground_normals), NULL, GL_STATIC_DRAW);

	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(ground_vertices), ground_vertices);
	offset += sizeof(ground_vertices);
	glBufferSubData(GL_ARRAY_BUFFER, offset, sizeof(ground_normals), ground_normals);

	
	// attribute indices

	glVertexAttribPointer((GLuint)0, 4, GL_FLOAT, GL_FALSE, 0, 0);
	glEnableVertexAttribArray(0);

	glVertexAttribPointer((GLuint)1, 3, GL_FLOAT, GL_FALSE, 0, (GLvoid *)sizeof(ground_vertices));
	glEnableVertexAttribArray(1);
	
	glBindVertexArray(0);
	
}

void renderGround() {
	glBindVertexArray(ground_vao);
	glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
}