#version 150

in vec2 position;

const int blurFactor = 11; // This cannot be changed

out vec2 blurTextureCoords[blurFactor];

uniform float targetHeight;

void main(void){

	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoords = position * 0.5 + 0.5;
	
	float pixelSize = 1.0 / targetHeight;
	
	int offset = (blurFactor-1) / 2;
	
	for (int i = -offset; i <= offset; i++) {
		blurTextureCoords[i + offset] = centerTexCoords + vec2(0.0, pixelSize * i);
	}

}