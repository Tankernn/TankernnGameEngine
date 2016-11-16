#version 400 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[4]; //4 max light sources
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;
in vec3 reflectedVector;
in vec3 refractedVector;

layout (location = 0) out vec4 out_Color;
layout (location = 1) out vec4 out_BrightColor;

uniform sampler2D shadowMap;
uniform sampler2D modelTexture;
uniform sampler2D specularMap;
uniform samplerCube enviroMap;
uniform float usesSpecularMap;
uniform vec3 lightColor[4]; //4 max light sources
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

//const float levels = 3.0;

void main(void){
	
	float objectNearestLight = texture(shadowMap, shadowCoords.xy).r;
	float lightFactor = 1.0;
	if (shadowCoords.z > objectNearestLight + 0.002) {
		lightFactor = 1.0 - 0.4;
	}
	
	vec3 unitNormal = normalize(surfaceNormal);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for (int i = 0; i < 4; i++) {
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);
		//float level = floor(brightness * levels);
		//brightness = level / levels;
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		//level = floor(dampedFactor * levels);
		//dampedFactor = level / levels;
		totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
	}
	
	totalDiffuse = max(totalDiffuse * lightFactor, 0.2); //Ambient lighting 2.0
	
	vec4 textureColor = texture(modelTexture, pass_textureCoords);
	if (textureColor.a < 0.5) {
		discard;
	}
	
	out_BrightColor = vec4(0.0);
	if (usesSpecularMap > 0.5) {
		vec4 mapInfo = texture(specularMap, pass_textureCoords);
		totalSpecular *= mapInfo.r;
		if (mapInfo.g > 0.5) {
			out_BrightColor = textureColor + vec4(totalSpecular, 1.0);
			totalDiffuse = vec3(1.0);
		}
	}
	
	out_Color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	out_Color = mix(vec4(skyColor, 1.0), out_Color, visibility);
	
	vec4 reflectedColor = texture(enviroMap, reflectedVector);
	vec4 refractedColor = texture(enviroMap, refractedVector);
	vec4 enviroColor = mix(reflectedColor, refractedColor, 0.5);
	
	out_Color = mix(out_Color, enviroColor, 0.3);
}