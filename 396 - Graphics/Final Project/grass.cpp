#include <stdio.h>
#include "grass.h"

GLuint grass_vao;
GLuint grass_vbo;

void createGrass() {

	// A single grass blade
	static const GLfloat grass_vertices[] =
	{
		-0.3f, 0.0f,0.0f, 1.0f,
		0.3f, 0.0f,0.0f,1.0f,
		-0.20f, 1.0f,0.0f, 1.0f,
		0.1f, 1.3f,0.0f, 1.0f,
		-0.05f, 2.3f,0.0f, 1.0f,
		0.0f, 3.3f, 0.0f, 1.0f,
	};

	static const GLfloat grass_normals[] =
	{
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
	};

	
	glGenVertexArrays(1, &grass_vao);
	glGenBuffers(1, &grass_vbo);
	glBindVertexArray(grass_vao);
	glBindBuffer(GL_ARRAY_BUFFER, grass_vbo);
	glBufferData(GL_ARRAY_BUFFER, sizeof(grass_vertices) + sizeof(grass_normals), NULL, GL_STATIC_DRAW);
	glBufferSubData(GL_ARRAY_BUFFER, 0, sizeof(grass_vertices), grass_vertices);
	glBufferSubData(GL_ARRAY_BUFFER, sizeof(grass_vertices), sizeof(grass_normals), grass_normals);
	glVertexAttribPointer((GLuint)0, 4, GL_FLOAT, GL_FALSE, 0, 0);
	glEnableVertexAttribArray(0);
	glVertexAttribPointer( (GLuint)1, 3, GL_FLOAT, GL_FALSE, 0, (GLvoid *)sizeof(grass_vertices));
	glEnableVertexAttribArray(1);
	glBindVertexArray(0);

}

void renderGrass() {

	glBindVertexArray(grass_vao);

	glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, 6, 1024 * 1024);

	glBindVertexArray(0);

}