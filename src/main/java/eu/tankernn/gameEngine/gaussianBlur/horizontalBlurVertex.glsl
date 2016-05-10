#version 150

in vec2 position;

const int blurFactor = 11;

out vec2 blurTextureCoords[blurFactor]; //11 is number of samples

uniform float targetWidth;

void main(void){

	gl_Position = vec4(position, 0.0, 1.0);
	vec2 centerTexCoords = position * 0.5 + 0.5;
	float pixelSize = 1.0 / targetWidth;
	
	int offset = (blurFactor-1) / 2;
	
	for (int i = -offset; i <= offset; i++) {
		blurTextureCoords[i + offset] = centerTexCoords + vec2(pixelSize * i, 0.0);
	}
	
}