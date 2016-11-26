#version 400 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[4]; //4 max light sources
out vec3 toCameraVector;
out float visibility;
out vec4 shadowCoords;
out vec3 reflectedVector;
out vec3 refractedVector;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition[4]; //4 max light sources
uniform vec3 lightPositionEyeSpace[4]; //4 max light sources
uniform float usesNormalMap;

uniform float useFakeLighting;

uniform float numberOfRows;
uniform vec2 offset;

uniform mat4 toShadowMapSpace;

uniform vec3 cameraPosition;

const float density = 0.004;
const float gradient = 2.0;

uniform vec4 plane;

void main(void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	shadowCoords = toShadowMapSpace * worldPosition;
	gl_ClipDistance[0] = dot(worldPosition, plane);
	mat4 modelViewMatrix = viewMatrix * transformationMatrix;
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_textureCoords = (textureCoords/numberOfRows) + offset;
	
	vec3 actualNormal = normal;
	if (useFakeLighting > 0.5) {
		actualNormal = vec3(0.0, 1.0, 0.0);
	}
	
	surfaceNormal = (modelViewMatrix * vec4(actualNormal, 0.0)).xyz;

	if (usesNormalMap > 0.5) {
		vec3 norm = normalize(surfaceNormal);
		vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
		vec3 bitang = normalize(cross(norm, tang));
		
		mat3 toTangentSpace = mat3(
			tang.x, bitang.x, norm.x,
			tang.y, bitang.y, norm.y,
			tang.z, bitang.z, norm.z
		);

		for(int i = 0; i < 4; i++) {
			toLightVector[i] = toTangentSpace * (lightPositionEyeSpace[i] - positionRelativeToCam.xyz);
		}
		toCameraVector = toTangentSpace * (-positionRelativeToCam.xyz);
	} else {
		for (int i = 0; i < 4; i++) {
			toLightVector[i] = lightPosition[i] - worldPosition.xyz;
		}
		toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	}
	
	vec3 unitNormal = normalize(normal);
	vec3 viewVector = normalize(worldPosition.xyz - cameraPosition);
	reflectedVector = reflect(viewVector, unitNormal);
	refractedVector = refract(viewVector, unitNormal, 1.0/1.33);
	
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow((distance * density), gradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
