#version 330

in vec2 pass_textureCoords;

out vec4 out_Color;

uniform vec3 color;
uniform sampler2D fontAtlas;

const float width = 0.5;
const float edge = 0.1;

const float borderWidth = 0.4;
const float borderEdge = 0.5;

const vec2 offset = vec2(0.004, -0.004);

const vec3 outlineColor = vec3(0.2, 0.2, 0.2);

void main(void) {
	
	float distance = 1.0 - texture(fontAtlas, pass_textureCoords).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float distance2 = 1.0 - texture(fontAtlas, pass_textureCoords + offset).a;
	float outlineAlpha = 1.0 - smoothstep(borderWidth, borderWidth + borderEdge, distance2);
	
	float totalAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 totalColor = mix(outlineColor, color, alpha / totalAlpha);
	
	out_Color = vec4(totalColor, totalAlpha);
		
}